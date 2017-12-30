set CP=./fwk_atuin_wfl_editor.jar
set CP=%CP%;./lib/crimson.jar
set CP=%CP%;./lib/js.jar
set CP=%CP%;./lib/xpp3_min.jar
set CP=%CP%;./lib/xstream.jar
set CP=%CP%;./lib/jedit.jar
"%JAVA_HOME%\bin\java" -Xmx256M -cp %CP% crm.core.wfl.editor.gui.WflMain
