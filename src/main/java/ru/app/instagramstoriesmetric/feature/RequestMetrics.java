package ru.app.instagramstoriesmetric.feature;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RequestMetrics {

    private final Counter requestCounter;
    private final Counter errorCounter;

    public RequestMetrics(MeterRegistry registry) {
        this.requestCounter = Counter.builder("http.requests.total")
                .description("Total number of HTTP requests")
                .register(registry);

        this.errorCounter = Counter.builder("http.requests.errors")
                .description("Total number of HTTP request errors")
                .register(registry);
    }

    public void incrementRequestCount() {
        requestCounter.increment();
    }

    public void incrementErrorCount() {
        errorCounter.increment();
    }
}
