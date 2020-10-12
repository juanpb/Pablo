set CP=..\classes;..\jar\log4j.jar
set CP=%CP%;..\jar\metadata-extractor-2.3.1.jar

set INI=Renombrar.ini


rem %1 es el directorio, cuando se ejecuta sobre una carpeta de Windows. Generar entrada 
rem en Regedit = HKEY_CLASSES_ROOT\Directory\shell\Renombrar\command

rem con '-dXXX' si XXX tiene algún directorio existente, arranca en ese directorio
start javaw  -Dlog4j_path=c:\tmp -Dlogfile.name=javas.log -classpath %CP% p.aplic.renombrar.Renombrar -d%1 %INI% 

rem java  -Dlog4j_path=c:\tmp -Dlogfile.name=javas.log -classpath %CP% p.aplic.renombrar.Renombrar  -d%1 %INI% 
rem pause