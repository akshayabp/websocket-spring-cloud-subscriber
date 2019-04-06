#!/bin/sh
echo "********************************************************"
echo "Starting the Eureka Server"
while ! `nc -z eurekaserver 8761`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Waiting for the Rabbitmq server to start on port 5672"
echo "********************************************************"
while ! `nc -z my-rabbitmq 5672`; do sleep 3; done
echo "******** Rabbitmq Server has started "

echo "********************************************************"
echo "Waiting for the Zipkin server to start on port 9411"
echo "********************************************************"
while ! `nc -z zipkinserver 9411`; do sleep 3; done
echo "******** Zipkin Server has started "

java \
	-Dspring.profiles.active=docker \
	-jar /usr/local/websocket_spring_cloud_subscriber/@project.build.finalName@.jar
