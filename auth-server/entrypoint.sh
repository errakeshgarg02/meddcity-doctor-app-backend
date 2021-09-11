#! /bin/sh

export ECS_INSTANCE_IP_ADDRESS=$(curl — retry 5 — connect-timeout 3 -s 169.254.169.254/latest/meta-data/local-ipv4)

#export ECS_TASK_NON_SECURE_INSTANCE_PORT=$(cat ${ECS_CONTAINER_METADATA_FILE} | grep HostPort | awk -F ":" '{print $2}' | sed 's/,$//g')
java -jar app.jar