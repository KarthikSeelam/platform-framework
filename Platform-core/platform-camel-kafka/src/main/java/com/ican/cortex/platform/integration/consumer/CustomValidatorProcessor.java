package com.ican.cortex.platform.integration.consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class CustomValidatorProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String message = exchange.getIn().getBody(String.class);

        // Validate the message
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message is invalid: it cannot be empty");
        }
        
        // You can integrate more complex validation logic here
    }
}
