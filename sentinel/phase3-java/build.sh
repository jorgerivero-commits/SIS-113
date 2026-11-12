#!/bin/bash
# Sentinel — Phase 3: compile everything and run the self-check.
#
# Usage (from sentinel/phase3-java):
#   ./build.sh
#
# Then, for example:
#   java -cp out sentinel.app.Main --replay=../phase1-bash/samples/muestra-2026-09-17.txt
#   java -cp out sentinel.app.Main --help

cd "$(dirname "$0")" || exit 1

if ! command -v javac > /dev/null; then
    echo "build.sh needs a JDK: sudo apt install openjdk-21-jdk" >&2
    exit 1
fi

rm -rf out out-test

echo "== compiling src/"
javac -d out $(find src -name "*.java") || exit 1

echo "== compiling test/"
javac -d out-test -cp out $(find test -name "*.java") || exit 1

echo "== running the self-check"
java -cp out:out-test sentinel.check.SentinelCheck
