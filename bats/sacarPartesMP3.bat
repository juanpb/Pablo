set CP=..\classes;..\jar\log4j.jar
set ARCH_ENTR=D:\P\videos\youtube\05-07.mp3
rem set DESDE=0:0:0
rem set HASTA=0:2:5 
rem 0:0:0 0:2:5
rem 0:42:5 0:45:30
set DESDE=0:42:5
set HASTA=0:45:30


java -classpath %CP% p.proc.EliminarPartesMP3 %ARCH_ENTR% %DESDE% %HASTA%

pause