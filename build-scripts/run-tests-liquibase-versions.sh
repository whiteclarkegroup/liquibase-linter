#!/bin/bash
set -ev
declare -a VERSIONS=("[3.4.0,3.5.0)" "[3.5.0,3.6.0)" "[3.6.0,3.7.0)" "[3.7.0,3.8.0)")
declare -a YAMLVERSION=("1.13" "1.17" "1.18" "1.23")
for ((i = 0; i < ${#VERSIONS[@]}; ++i)); do
    rm -f databasechangelog.csv databasechangelog.csv.new
    mvn clean test -B -Dliquibase.version=${VERSIONS[i]} -Dyaml.version=${YAMLVERSION[i]}
done
