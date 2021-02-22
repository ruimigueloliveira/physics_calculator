#!/bin/bash
javac l*/Test*
echo javac...
echo ------------------
java lfaProject.TestDataStrt
rm lfaProject/*class
echo ------------------
echo removing classes...
