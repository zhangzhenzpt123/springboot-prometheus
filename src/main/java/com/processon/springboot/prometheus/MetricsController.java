package com.processon.springboot.prometheus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletableFuture;

@RestController
public class MetricsController {
    private final Logger logger = LoggerFactory.getLogger(MetricsController.class);
    private final Counter ordersCounter;

    public MetricsController(MeterRegistry registry) {
        ordersCounter = Counter.builder("custom_metric_name")
                .description("Description of custom metric")
                .tags("environment", "development")
                .register(registry);
    }

    @GetMapping("/order")
    public String createOrder(){
        ordersCounter.increment();
        logger.info("thread:" + Thread.currentThread().getName());
        logger.info("ordersCounter count: {}", ordersCounter.count());
        return ordersCounter.count() + " orders created";
    }

    @Async
    @Scheduled(fixedDelay = 60000)
    public void run(){
        ordersCounter.increment();
        logger.info("thread:" + Thread.currentThread().getName());
        logger.info("ordersCounter count: {}", ordersCounter.count());
        System.out.println(ordersCounter.count() + " orders created");
    }

}
