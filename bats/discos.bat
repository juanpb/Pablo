rem 	En caso de que la versi?de java default del sistema no sea la 6 hay
rem		que definir la variable %JAVA_6%, con el directorio del bin de java 6
if "%JAVA_6%" == "" goto noSetearPath
set path=%JAVA_6%
:noSetearPath
echo off
set CPL=..\classes
if "%CP%" == "" goto noSetearCP
set CPL=%CP%
:noSetearCP
echo on

java -Dfile.encoding=UTF-8 -classpath %CPL% p.aplic.ListarDiscos "V:\\" "J:\Discos.txt"
pause
rem exit
