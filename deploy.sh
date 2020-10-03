#!/bin/bash -e

export PROJECT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
printf "\nProject version: %s\n" "$PROJECT_VERSION"

mvn clean install -DskipTests=true

printf "Stopping process running on port 8080 if exists\n"
sudo fuser -k 8080/tcp

printf "Running the application\n"
export SPRING_PROFILES_ACTIVE=prod
java -jar ./target/usermanagerservice-"${PROJECT_VERSION}".jar

printf "Exit"
