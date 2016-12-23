#!/bin/sh

DIR="$( cd "$( DIR "${BASH_SOURCE[0]}" )" && pwd )"
JARNAME=$DIR/dist/FileScanner.jar
start() {
  echo "Starting File scanner for weather.dat"

    #forward all logs to the files stated....needs file permissions 777
  java -jar $JARNAME
}

case "$1" in
    start)
        start
    ;;
    *)
        echo "Usage: fileScanner {start}"
        exit 1
    ;;
esac

exit $?
