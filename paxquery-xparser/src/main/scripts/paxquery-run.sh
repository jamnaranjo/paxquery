#!/bin/bash

##########
## Configuration by the user
### set FLINK_PATH to the path to Flink directory in your system
FLINK_PATH="/Users/jalvaro/flink-0.6-incubating"
#FLINK_PATH=
### set PAXQUERY_INSTALLATION_PATH  to the path where Paxquery is installed
PAXQUERY_INSTALLATION_PATH="/Users/jalvaro/paxquery-installation"
#PAXQUERY_INSTALLATION_PATH=
##########

# Arguments check
if [ $# -lt 2 ]; then
	echo "Usage: paxquery-run.sh [query_file] [output_file] <paralellism> <drawtrees>"
	exit 1
fi

drawtrees=0
if [ $# -eq 4 ] && [ $4 == "drawtrees" ]; then
	drawtrees=1
fi

# Paths check
if [ -z "$FLINK_PATH" ]; then
	echo "Please set the script variable FLINK_PATH to the flink instalation path. Aborting execution."
	exit 1
fi
if [ -z "$PAXQUERY_INSTALLATION_PATH" ]; then
	echo "Please set the script variable PAXQUERY_INSTALLATION_PATH to the flink instalation path. Aborting execution."
	exit 1
fi

#Variables
QUERY_FILE=$1
OUTPUT_DIR=$2
PARALELLISM=$3

PAXQUERY_INSTALLATION_BIN=$PAXQUERY_INSTALLATION_PATH"/bin"
PAXQUERY_INSTALLATION_LIB=$PAXQUERY_INSTALLATION_PATH"/lib"
#export CC_CLASSPATH=$PAXQUERY_INSTALLATION_BIN"/paxquery-algebra-0.1.jar:"$PAXQUERY_INSTALLATION_BIN"/paxquery-translation-0.1.jar:"$PAXQUERY_INSTALLATION_LIB"/antlr-runtime-4.2.jar"
export CC_CLASSPATH=$PAXQUERY_INSTALLATION_BIN"/paxquery-algebra-0.1.jar:"$PAXQUERY_INSTALLATION_BIN"/paxquery-translation-0.1.jar:"$PAXQUERY_INSTALLATION_LIB"/antlr-runtime-4.2.jar:"$PAXQUERY_INSTALLATION_LIB"/json-simple-1.1.jar"
XPARSER_JAR=$PAXQUERY_INSTALLATION_BIN"/paxquery-xparser-0.1.jar"
WEB_PATH=$PAXQUERY_INSTALLATION_PATH/web/images/plans

echo ""
echo "-- This is PAXQuery 0.1 --"
echo ""

#Actions
rm $2 &> /dev/null	#hide rm output from the user
if [ $drawtrees -eq 0 ]; then
	"$FLINK_PATH"/bin/flink run -v $XPARSER_JAR file://"$QUERY_FILE" "$OUTPUT_DIR" $3
else
	"$FLINK_PATH"/bin/flink run -v $XPARSER_JAR file://"$QUERY_FILE" "$OUTPUT_DIR" $3 drawtrees $WEB_PATH
fi