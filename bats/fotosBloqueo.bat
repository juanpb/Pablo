set CP=..\classes;..\jar\log4j.jar
set INI=..\config\verFotos\configBloq.ini

rem start javaw -DmmX=1024 -classpath %CP% p.aplic.verfotos.VisorSimple %INI%
java -classpath %CP% p.aplic.verfotos.VisorSimple %INI%

exit