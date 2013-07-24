#!/bin/sh
DIR=`dirname $0`
cd $DIR
LIBS=""
for l in libraries/*.jar; do
  if [ "$LIBS" = "" ]; then
    LIBS=`echo $l`
  else 
    LIBS=`echo $LIBS:$l`
  fi
done
exec java -classpath $LIBS:WeeklyTimeSheet.jar net.oesterholt.urenregistratie.main.JWeeklyTimeSheet
