
CLASSPATH=build/libs/*:build/classes/java/main:build/resources/java/main:idea_lib/*:idea_lib/modules/*:idea_lib/ext/*:idea_lib/frontend-split/*:idea_lib/rt/*
for F in `ls -1 idea_plugins/`;
do
	echo $F
	CLASSPATH=$CLASSPATH:idea_plugins/$F/lib/*
done;

echo $CLASSPATH
for F in "${FILES[@]}";
do
	echo Processing $F
	java -cp $CLASSPATH  krasa.formatter.CmdLineRunner /Users/emmy/prjs/resolveit-common/eclipse_java_settings.xml $F
done;
