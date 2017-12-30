set CP=..\classes;..\jar\log4j.jar
set VM=-Dlog4j_path=F:\ -Dlogfile.name=javas.log
java %VM% -classpath %CP% p.aplic.Iniciar Iniciar.txt
exit