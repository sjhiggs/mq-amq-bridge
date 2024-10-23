package com.redhat.support;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.ShutdownStrategy;
import org.springframework.stereotype.Component;

@Component
public class MySpringBootRouter extends RouteBuilder {

    @Override
    public void configure() {

        getCamelContext().setTracing(false);
        getCamelContext().getShutdownStrategy().setTimeout(1);


        from("timer:mq-producer-timer?period=1000&delay=1000")
                .id("01-timer-to-mq")
                .setBody(simple("message sent at: ${date:now:yyMMddHHmmss}"))
                .log("init: ${body}")
                .to("mq:queue:DEV.QUEUE.1");

        from("mq:queue:DEV.QUEUE.1")
                .id("02-mq-to-artemis")
                .log("bridge: ibm-mq to artemis")
                .to("artemis:queue:ARTEMIS.QUEUE.1");

        from("artemis:queue:ARTEMIS.QUEUE.1")
                .id("03-artemis-to-mq")
                .log("bridge 2: artemis to ibm-mq")
                .to("mq:queue:DEV.QUEUE.2");

        from("mq:queue:DEV.QUEUE.2")
                .id("04-mq-to-log")
                .log("completed processing: ${body}");

    }

}
