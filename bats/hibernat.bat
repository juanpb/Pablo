D:\P\exe\psshutdown -h -t 20 -c -v 7
pause


rem 	
rem PsShutdown v2.52
rem 
rem By Mark Russinovich
rem 
rem Published: December 4, 2006
rem 
rem  
rem Introduction
rem 
rem PsShutdown is a command-line utility similar to the shutdown utility from the Windows 2000 Resource Kit, but with the ability to do much more. In addition to supporting the same options for shutting down or rebooting the local or a remote computer, PsShutdown can logoff the console user or lock the console (locking requires Windows 2000 or higher). PsShutdown requires no manual installation of client software.
rem 
rem  
rem Installation
rem 
rem Just copy PsShutdown onto your executable path, and type psshutdown with command-line options defined below.
rem 
rem  
rem Using PsShutdown
rem 
rem See the February 2005 issue of Windows IT Pro Magazine for Mark's article that covers advanced usage of PsKill.
rem 
rem You can use PsShutdown to initiate a shutdown of the local or a remote computer, logoff a user, lock a system, or to abort an imminent shutdown.
rem 
rem Usage: psshutdown [[\\computer[,computer[,..] | @file [-u user [-p psswd]]] -s|-r|-h|-d|-k|-a|-l|-o [-f] [-c] [-t nn|h:m] [-n s] [-v nn] [-e [u|p]:xx:yy] [-m "message"]
rem -	Displays the supported options.
rem computer	Perform the command on the remote computer or computers specified. If you omit the computer name the command runs on the local system, and if you specify a wildcard (\\*), the command runs on all computers in the current domain.
rem @file	Run the command on each computer listed in the text file specified.
rem -u	Specifies optional user name for login to remote computer.
rem -p	Specifies optional password for user name. If you omit this you will be prompted to enter a hidden password.
rem -a	Aborts a shutdown (only possible while a countdown is in progress).
rem -c	Allows the shutdown to be aborted by the interactive user.
rem -d	Suspend the computer.
rem -e	Shutdown reason code.
rem Specify 'u' for user reason codes and 'p' for planned shutdown reason codes.
rem xx is the major reason code (must be less than 256).
rem yy is the minor reason code (must be less than 65536).
rem -f	Forces all running applications to exit during the shutdown instead of giving them a chance to gracefully save their data.
rem -h	Hibernate the computer.
rem -k	Poweroff the computer (reboot if poweroff is not supported).
rem -l	Lock the computer.
rem -m	This option lets you specify a message to display to logged-on users when a shutdown countdown commences.
rem -n	Specifies timeout in seconds connecting to remote computers.
rem -o	Logoff the console user.
rem -r	Reboot after shutdown.
rem -s	Shutdown without power off.
rem -t	Specifies the countdown in seconds until the shutdown (default: 20 seconds) or the time of shutdown (in 24 hour notation).
rem -v	Display message for the specified number of seconds before the shutdown. If you omit this parameter the shutdown notification dialog displays and specifying a value of 0 results in no dialog.
