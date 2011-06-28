#!/bin/bash

CLASSPATH=.
for f in `/bin/ls ./*.jar`; do

CLASSPATH=$CLASSPATH":"$f
done

echo $CLASSPATH

java -cp $CLASSPATH org.hf.google.MyGoogle $1 $2 $3