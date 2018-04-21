set CP=..\classes;..\jar\log4j.jar
set INI=..\config\verFotos\config.ini
set VM=-Dlog4j_path=c:\tmp -Dlogfile.name=javas.log

java %VM% -classpath %CP% p.aplic.verfotos.VisorSimple %INI%
rem pause
exit