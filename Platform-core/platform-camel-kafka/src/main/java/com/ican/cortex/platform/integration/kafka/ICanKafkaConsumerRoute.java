package com.ican.cortex.platform.integration.kafka;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ICanKafkaConsumerRoute extends ICanKafkaCamelBaseUtility {

    public ICanKafkaConsumerRoute(CamelContext camelContext) {
        super(camelContext);
    }

    @Override
    protected RouteBuilder configureRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("kafka:{{kafka.topic}}")
                    .routeId("KafkaConsumerRoute")
                    .log("Message received from Kafka: ${body}");
            }
        };
    }
}
