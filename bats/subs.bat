set CP=..\classes;..\jar\log4j.jar
set VM=-Dlog4j_path=D:\TMP -Dlogfile.name=javas.log

 start javaw  %VM% -classpath %CP% p.aplic.subtitulos.SubtitulosGUI
rem java %VM% -classpath %CP% p.aplic.subtitulos.SubtitulosGUI
rem pause