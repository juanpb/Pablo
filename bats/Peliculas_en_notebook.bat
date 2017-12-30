rem  %1 puede ser el parámetro ini
echo %1

set CP=..\classes;..\jar\log4j.jar
set CLASSPATH=%CLASSPATH%;..\jar\log4j.jar

set INI=D:\P\_env\config\pelis\config.ini
if "%1" == "" goto INI_DEFAULT
set INI=%1
:INI_DEFAULT

rem 	Si está seteada la variable de entorno %CP% entonces la uso
if "%CP%" == "" goto ELSE
set CLASSPATH=%CP%
:ELSE

if "%JARS%" == "" goto ELSE2
set CLASSPATH=%CLASSPATH%;%JARS%\jdom.jar
set CLASSPATH=%CLASSPATH%;%JARS%\svnant.jar
set CLASSPATH=%CLASSPATH%;%JARS%\svnClientAdapter.jar
set CLASSPATH=%CLASSPATH%;%JARS%\svnjavahl.jar
set CLASSPATH=%CLASSPATH%;%JARS%\svnkit.jar
set CLASSPATH=%CLASSPATH%;%JARS%\ant.jar
set CLASSPATH=%CLASSPATH%;%JARS%\ant-launcher.jar
set CLASSPATH=%CLASSPATH%;%JARS%\log4j.jar
:ELSE2

start javaw -classpath %CLASSPATH% p.aplic.peliculas.Main %INI% 
exit
rem java -classpath %CLASSPATH% p.aplic.peliculas.Main %INI%
rem pause
