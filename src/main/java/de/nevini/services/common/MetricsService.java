package de.nevini.services.common;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MetricsService {

    @Getter
    private final MetricRegistry registry = new MetricRegistry();

    public MetricsService() {
        Slf4jReporter.forRegistry(registry)
                .convertDurationsTo(TimeUnit.SECONDS)
                .convertRatesTo(TimeUnit.MINUTES)
                .outputTo(log).withLoggingLevel(Slf4jReporter.LoggingLevel.DEBUG)
                .build().start(15, TimeUnit.MINUTES);
    }

    /**
     * Marks the occurrence of an event for the meter identified by {@code name}.
     * Also marks the occurrence of an event for the meter identified by {@code name} and {@code discriminator}
     * if {@code discriminator} is not {@code null}.
     *
     * @throws NullPointerException if {@code name} is {@code null}
     * @see com.codahale.metrics.Meter#mark()
     */
    public void mark(@NonNull String name, String discriminator) {
        getRegistry().meter(name).mark();
        if (discriminator != null) {
            getRegistry().meter(name + " (" + discriminator + ")").mark();
        }
    }

}
