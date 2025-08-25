package com.ican.cortex.platform.integration.kafka;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ICanKafkaProducerRoute extends ICanKafkaCamelBaseUtility {

    public ICanKafkaProducerRoute(CamelContext camelContext) {
        super(camelContext);
    }

    @Override
    protected RouteBuilder configureRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start")
                    .routeId("KafkaProducerRoute")
                    .to("kafka:{{kafka.topic}}")
                    .log("Message sent to Kafka: ${body}");
            }
        };
    }
}
