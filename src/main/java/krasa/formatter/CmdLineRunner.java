package krasa.formatter;

import com.intellij.openapi.command.impl.DummyProject;
import com.intellij.psi.util.PsiUtilCore;
import krasa.formatter.eclipse.JavaCodeFormatterFacade;
import krasa.formatter.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CmdLineRunner {
    static Settings settings;

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: runFormatter.sh <configFile> <directoryWithJavaFiles>");
            return;
        }

        File directory = new File(args[1]);
        settings = new Settings();
        settings.setEnableJavaFormatting(true);
        settings.setEclipseVersion(Settings.FormatterVersion.NEWEST);
        settings.setFormatter(Settings.Formatter.ECLIPSE);
        settings.setPathToConfigFileJava(args[0]);
        settings.setImportOrdering(Settings.ImportOrdering.ECLIPSE_452);
        parseDirectory(directory);
    }

    private static void parseDirectory(File directory) {
        directory.listFiles((dir, name) -> {
            File f = new File(dir, name);
            if (f.isDirectory())
                parseDirectory(f);
            else if (f.getName().endsWith(".java")) {
                try {
                    formatFile(f);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return false;
        });
    }

    private static void formatFile(File file) throws IOException {
        String javaSource = Files.readString(file.toPath());
        JavaCodeFormatterFacade formatter = new JavaCodeFormatterFacade(settings, DummyProject.getInstance());
        String formattedSource = formatter.format(javaSource, 0, javaSource.length(), PsiUtilCore.NULL_PSI_FILE);
        if (javaSource.equals(formattedSource))
            return;
        System.out.println(formattedSource);
    }
}
