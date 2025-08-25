package com.ican.cortex.platform.logger;

import com.ican.cortex.platform.logger.base.BaseLogger;
import com.ican.cortex.platform.logger.base.FileLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlatformLoggerApplication {

	private static final Logger logger = LoggerFactory.getLogger(PlatformLoggerApplication.class);
	private static final Marker TELEMETRY_MARKER = MarkerFactory.getMarker("TELEMETRY");


	public static void main(String[] args) {
		SpringApplication.run(PlatformLoggerApplication.class, args);
		FileLogger baseLogger = new FileLogger();
		baseLogger.info("UPLOAD", "File uploaded successfully");
		logger.info(TELEMETRY_MARKER, "Telemetry");



	}

}
