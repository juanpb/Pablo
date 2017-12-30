set CP=..\classes;..\jar\log4j.jar
set CP=%CP%;..\jar\jdom.jar
set INI="C:\Datos\P\Google Drive\libreta\ordenado.xml"


 start javaw -classpath %CP% p.aplic.libretadirecciones.gui.Principal %INI%
rem java -classpath %CP% p.aplic.libretadirecciones.gui.Principal %INI%
