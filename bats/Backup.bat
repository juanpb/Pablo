rem 	En caso de que la versión de java default del sistema no sea la 6 hay
rem		que definir la variable %JAVA_6%, con el directorio del bin de java 6
if "%JAVA_6%" == "" goto noSetearPath
set path=%JAVA_6%
:noSetearPath

echo off
set javamail=F:/Java/javamail-1.2
set CP=%javamail%/mail.jar
set CP=%CP%;%javamail%/imap.jar
set CP=%CP%;%javamail%/mailapi.jar
set CP=%CP%;%javamail%/pop3.jar
set CP=%CP%;%javamail%/smtp.jar
set CP=%CP%;F:/Java/jaf-1.0.1/activation.jar
set CP=%CP%;F:/Java/ProyectosJava/Pablo/classes
echo on

java -classpath %CP% p.aplic.Backup F:\\backup.txt
pause