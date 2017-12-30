set CP=..\classes;..\jar\log4j.jar
set INI=..\config\prc\config.ini

start javaw -classpath %CP% p.aplic.prec.ABMFrame %INI%
exit
rem java -classpath %CP% p.aplic.prec.ABMFrame %INI%
rem pause