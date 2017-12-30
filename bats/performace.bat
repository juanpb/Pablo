set CP=..\classes;..\jar\log4j.jar
set CP=%CP%;..\jar\jdom.jar

set JAVA_OPTIONS=-Dlog4j_path=F: -Dlogfile.name=conexionInfo.log
 
java %JAVA_OPTIONS% -classpath %CP% p.pruebas.Performance
pause