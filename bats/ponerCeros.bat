rem 	En caso de que la versión de java default del sistema no sea la 6 hay
rem		que definir la variable %JAVA_6%, con el directorio del bin de java 6
if "%JAVA_6%" == "" goto noSetearPath
set path=%JAVA_6%
:noSetearPath

echo off
set CP=..\classes
echo on
java -classpath %CP% p.proc.Numerar "G:\"
pause
exit