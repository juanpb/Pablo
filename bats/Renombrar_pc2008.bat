rem 	En caso de que la versión de java default del sistema no sea la 6 hay
rem		que definir la variable %JAVA_6%, con el directorio del bin de java 6
if "%JAVA_6%" == "" goto noSetearPath
set path=%JAVA_6%
:noSetearPath

set CP=%CP%;%JARS%\exif-metadata-extractor-2.2.2.jar

set INI=Renombrar.ini

start javaw -classpath %CP% p.aplic.renombrar.Renombrar -d%1 %INI% 
rem java -classpath %CP% p.aplic.renombrar.Renombrar  -d%1 %INI% 
rem pause