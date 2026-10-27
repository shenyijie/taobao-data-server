#!/bin/bash
ps aux | grep data-server
ps aux | grep data-server | grep -v grep | awk '{print $2}' | xargs kill
java -jar data-server-0.0.1-SNAPSHOT.jar &