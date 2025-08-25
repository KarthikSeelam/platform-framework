package com.ican.cortex.platform.integration.kafka;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public abstract class ICanKafkaCamelBaseUtility {

    private static final Logger logger = LoggerFactory.getLogger(ICanKafkaCamelBaseUtility.class);

    private final ProducerTemplate producerTemplate;
    private final CamelContext camelContext;

    @Value("${kafka.topic}")
    private String kafkaTopic;

    public ICanKafkaCamelBaseUtility(CamelContext camelContext) {
        this.camelContext = camelContext;
        this.producerTemplate = camelContext.createProducerTemplate();
    }

    // Send a message to Kafka with error handling
    public void sendMessageToKafka(String message) throws KafkaCamelException {
        try {
            producerTemplate.sendBody("kafka:" + kafkaTopic, message);
            logger.info("Message successfully sent to Kafka topic '{}': {}", kafkaTopic, message);
        } catch (Exception e) {
            logger.error("Error sending message to Kafka topic '{}': {}", kafkaTopic, e.getMessage(), e);
            throw new KafkaCamelException("Failed to send message to Kafka topic '" + kafkaTopic + "'", e, KafkaCamelException.ErrorCode.MESSAGE_SEND_ERROR);
        }
    }

    // Define custom routes
    protected abstract RouteBuilder configureRoute();

    // Start the Camel context with error handling and logging
    public void startCamelContext() throws KafkaCamelException {
        try {
            RouteBuilder customRoute = configureRoute();
            camelContext.addRoutes(customRoute);
            camelContext.start();
            logger.info("Camel context started successfully with custom routes.");
        } catch (Exception e) {
            logger.error("Error starting Camel context: {}", e.getMessage(), e);
            throw new KafkaCamelException("Failed to start Camel context", e, KafkaCamelException.ErrorCode.CAMEL_CONTEXT_START_ERROR);
        }
    }

    // Stop the Camel context with error handling and logging
    public void stopCamelContext() throws KafkaCamelException {
        try {
            camelContext.stop();
            logger.info("Camel context stopped successfully.");
        } catch (Exception e) {
            logger.error("Error stopping Camel context: {}", e.getMessage(), e);
            throw new KafkaCamelException("Failed to stop Camel context", e, KafkaCamelException.ErrorCode.CAMEL_CONTEXT_STOP_ERROR);
        }
    }
}
