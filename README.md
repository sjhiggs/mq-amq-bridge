# Description

Sends messages with the following flow:

camel-timer-mq-route --> ibm-mq --> camel-mq-amq-route --> amq7 --> camel-amq-mq-route --> camel-mq-log-route

# Sources

## Skeleton Project
```
mvn archetype:generate \
-DarchetypeGroupId=org.apache.camel.archetypes \
-DarchetypeArtifactId=camel-archetype-spring-boot \
-DarchetypeVersion=4.4.0.redhat-00033 \
-DgroupId=com.redhat \
-DartifactId=$PROJECT_NAME \
-Dversion=1.0-SNAPSHOT \
-DinteractiveMode=false
```

## Incorporated Sources

https://github.com/apache/camel-spring-boot-examples/tree/main/amqp

# Add messaging environments

## IBM MQ

```
podman run --env LICENSE=accept --env MQ_QMGR_NAME=QM1 --volume qm1data:/mnt/mqm --publish 1414:1414 --publish 9443:9443 --detach --env MQ_APP_PASSWORD=foobar --name QM1 localhost/myibmmq:latest
```

## AMQ 7

From install directory
```
./bin/artemis create --port-offset 0 --relax-jolokia --host 127.0.0.1  --user admin --password admin --require-login --no-hornetq-acceptor --no-mqtt-acceptor --no-stomp-acceptor instance
./instance/bin/artemis run
```

# Run Project

```
mvn clean spring-boot:run
```
