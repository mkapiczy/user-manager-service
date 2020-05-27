#!/bin/bash -e

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ] || [ -z "$4" ] || [ -z "$5" ]; then
  printf "Usage $0 bastion_public_ip instance_private_ip private_key db_address db_password \n"
  exit 1
fi

bastion_address=$1
instance_address=$2
private_key=$3
db_address=$4
db_password=$5

export PROJECT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
printf "\nProject version: %s\n" "$PROJECT_VERSION"

mvn clean install -DskipTests=true

printf "\n"
printf "Adding key to keychain\n"
ssh-add -k "${private_key}"

printf "Creating working directory on Bastion Host\n"
ssh -A ec2-user@"${bastion_address}" "mkdir -p ~/workdir/usermanagerservice"

printf "Copying jar to Bastion Host\n"
scp ./target/usermanagerservice-"${PROJECT_VERSION}".jar ec2-user@"${bastion_address}":~/workdir/usermanagerservice/

printf "Creating working directory on private instance\n"
ssh -A ec2-user@"${bastion_address}" "ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null ec2-user@${instance_address} 'mkdir -p ~/workdir/usermanagerservice'"

printf "Copying jar to private instance\n"
ssh -A ec2-user@"${bastion_address}" "scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null ~/workdir/usermanagerservice/usermanagerservice-""${PROJECT_VERSION}"".jar ec2-user@${instance_address}:~/workdir/usermanagerservice/"

printf "Stopping process running on port 8080 if exists\n"
ssh -A ec2-user@"${bastion_address}" "ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null ec2-user@${instance_address} 'sudo fuser -k 8080/tcp'"

printf "Running the application\n"
ssh -A ec2-user@"${bastion_address}" "ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null ec2-user@${instance_address} 'export SPRING_PROFILES_ACTIVE=dev && export SPRING_DATASOURCE_URL=${db_address} && export SPRING_DATASOURCE_PASSWORD=${db_password} && java -jar ~/workdir/usermanagerservice/usermanagerservice-${PROJECT_VERSION}.jar'"

printf "Exit"