#!/bin/bash
set -ev
declare -a VERSIONS=("[3.5.0,3.6.0)" "[3.6.0,3.7.0)" "[3.7.0,3.8.0)")
for version in "${VERSIONS[@]}"; do
  rm -f databasechangelog.csv databasechangelog.csv.new
  mvn clean test -B -Dliquibase.version=$version
done
# yaml was an optional dependency until 3.5+ so have to work around with profile
rm -f databasechangelog.csv databasechangelog.csv.new
mvn test -B -Dliquibase.version=3.4.2 -Dyaml.version=1.13
