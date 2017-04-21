#!/bin/bash
if [ -n 'which java' ];
then
        java -jar ../dist/config-manager.jar "$@" 2>&1;
else
        echo "Java is not found on your machine. Please install before restarting script"
fi
 
