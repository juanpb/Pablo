set CP=..\..\classes;..\..\jar\log4j.jar
set INI=C:\Datos\P\java\codigo\Pablo\config\verFotos\configEditarListaDeFotos.ini

rem start javaw -DmmX=1024 -classpath %CP% p.aplic.verfotos.VisorSimple %INI%
java -Dlog4j_path=C:\tmp -Dlogfile.name=javas.log -classpath %CP% p.aplic.verfotos.VisorSimpleEditarListaDeFotos %INI%

