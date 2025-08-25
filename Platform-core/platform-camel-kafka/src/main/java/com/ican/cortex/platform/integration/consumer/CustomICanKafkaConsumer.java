package com.ican.cortex.platform.integration.consumer;
import com.ican.cortex.platform.integration.kafka.ICanKafkaCamelBaseUtility;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Component
public class CustomICanKafkaConsumer extends ICanKafkaCamelBaseUtility {

    public CustomICanKafkaConsumer(CamelContext camelContext) {
        super(camelContext);
    }

    @Override
    protected RouteBuilder configureRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                // Consuming messages from Kafka and processing them
                from("kafka:{{kafka.topic}}")
                    .routeId("CustomKafkaConsumerRoute")
                    .log("Received message from Kafka: ${body}")
                    .process(new CustomValidatorProcessor())  // Add validation
                            .onException(Exception.class)  // Catch all exceptions
                            .log("Error processing message: ${exception.message}")  // Log the exception
                            .to("kafka:{{kafka.dlq.topic}}")  // Send the failed message to DLQ topic
                            .handled(true)  // Mark the exception as handled so it doesn't propagate
                            .end()

                            .onException(TimeoutException.class)
                            .log("Timeout error occurred, retrying...")
                            .maximumRedeliveries(3)  // Retry up to 3 times
                            .redeliveryDelay(1000)  // Wait 1 second before retrying
                            .to("kafka:{{kafka.retry.topic}}")  // Send to retry topic
                            .handled(true)
                            .end()
                    .process(exchange -> {
                        // Business logic for processing the message
                        String message = exchange.getIn().getBody(String.class);
                       // saveToDatabase(message);  // Call the method below
                    });
            }
        };
    }



    // Start consuming messages
    public void startConsuming() throws Exception {
        startCamelContext();
    }
}
