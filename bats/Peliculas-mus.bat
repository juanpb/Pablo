set CP=..\classes;..\jar\log4j.jar
set CP=%CP%;..\jar\jdom.jar

set INI=..\config\pelis\config-mus.ini
start javaw -classpath %CP% p.aplic.peliculas.Main %INI%
rem java -classpath %CP% p.aplic.peliculas.Main %INI%
rem pause
exit