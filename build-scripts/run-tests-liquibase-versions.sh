#!/bin/bash
set -ev
declare -a VERSIONS=("[3.5.0,3.6.0)" "[3.6.0,3.7.0)")
for version in "${VERSIONS[@]}"; do
  rm -f databasechangelog.csv
  rm -f databasechangelog.csv.new
  mvn clean test -B -Dliquibase.version=$version
done
