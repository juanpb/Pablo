set CP=..\classes;..\jar\log4j.jar

javaw -mx1024M -classpath %CP% p.gui.superGui.AplicPrincipal disparadorAplic.ini
pause



rem start javaw -classpath %CP% p.aplic.exportar.GUI 
rem java -classpath %CP% p.aplic.exportar.GUI 
rem pause