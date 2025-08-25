package com.ican.cortex.platform.integration.producer;


import com.ican.cortex.platform.integration.kafka.ICanKafkaCamelBaseUtility;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CustomICanKafkaProducer extends ICanKafkaCamelBaseUtility {

    public CustomICanKafkaProducer(CamelContext camelContext) {
        super(camelContext);
    }

    @Override
    protected RouteBuilder configureRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                // Sending messages to Kafka
                from("direct:customProducer")
                    .routeId("CustomKafkaProducerRoute")
                    .setHeader("Content-Type", constant("application/json"))
                    .log("Sending JSON message to Kafka: ${body}")
                    .to("kafka:{{kafka.topic}}");
            }
        };
    }

    // Developer's custom method to send a message
    public void sendCustomMessage(String jsonMessage) throws Exception {
        startCamelContext();
        sendMessageToKafka(jsonMessage);  // Using the method from the base utility class
        stopCamelContext();
    }
}
