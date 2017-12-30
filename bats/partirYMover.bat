set CP=..\classes;..\jar\log4j.jar
set ARCH_ENTR=D:\P\videos\youtube\04-20.mp3
set CANT=10
set DIR_TMP=D:\P\videos\youtube\tmp
set DEST=F:\dol\a


java -classpath %CP% p.proc.PartirYMoverArchivosEnOrden %ARCH_ENTR% %DEST% %DIR_TMP% %CANT% 

pause