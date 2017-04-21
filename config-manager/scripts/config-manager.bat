@ECHO off
IF "%JAVA_HOME%"=="" (
	ECHO JAVA_HOME is not found.
	GOTO :theend
)	
java -jar ../dist/config-manager.jar %*

