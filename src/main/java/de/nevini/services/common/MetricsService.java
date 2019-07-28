package de.nevini.services.common;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MetricsService {

    @Getter
    private final MetricRegistry registry = new MetricRegistry();

    public MetricsService() {
        Slf4jReporter.forRegistry(registry)
                .convertDurationsTo(TimeUnit.SECONDS)
                .convertRatesTo(TimeUnit.MINUTES)
                .outputTo(LoggerFactory.getLogger(getClass().getName()))
                .withLoggingLevel(Slf4jReporter.LoggingLevel.INFO)
                .build().start(15, TimeUnit.MINUTES);
    }

    /**
     * Marks the occurrence of an event for the meter identified by {@code name}.
     *
     * @throws NullPointerException if {@code name} is {@code null}
     * @see com.codahale.metrics.Meter#mark()
     */
    public void mark(@NonNull String name) {
        getRegistry().meter(name).mark();
    }

}
