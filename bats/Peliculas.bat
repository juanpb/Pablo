rem  %1 puede ser el parámetro ini
echo "%1"



set CP=%JPB_HOME%\\java\codigo\Pablo\classes
set JARS=%JPB_HOME%\\java\codigo\Pablo\jar

set INI=..\config\pelis\config.ini
if "%1" == "" goto INI_DEFAULT
set INI="%1"
:INI_DEFAULT


set CLASSPATH=%CP%;%JARS%\jdom.jar
set CLASSPATH=%CLASSPATH%;%JARS%\log4j.jar
set CLASSPATH=%CLASSPATH%;%JARS%\svnant.jar
set CLASSPATH=%CLASSPATH%;%JARS%\svnClientAdapter.jar
set CLASSPATH=%CLASSPATH%;%JARS%\svnjavahl.jar
set CLASSPATH=%CLASSPATH%;%JARS%\svnkit.jar
set CLASSPATH=%CLASSPATH%;%JARS%\ant.jar
set CLASSPATH=%CLASSPATH%;%JARS%\ant-launcher.jar


set JAVA_OPTIONS=-Dlog4j_path=C:\tmp -Dlogfile.name=pelis.log

 start javaw -classpath %CLASSPATH% p.aplic.peliculas.Main %INI%
 exit
 rem java %JAVA_OPTIONS% -classpath %CLASSPATH% p.aplic.peliculas.Main %INI%
 rem pause
