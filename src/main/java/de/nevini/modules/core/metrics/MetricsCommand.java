package de.nevini.modules.core.metrics;

import com.codahale.metrics.*;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.Formatter;
import org.springframework.stereotype.Component;

@Component
public class MetricsCommand extends Command {

    public MetricsCommand() {
        super(CommandDescriptor.builder()
                .keyword("metrics")
                .ownerOnly(true)
                .guildOnly(false)
                .node(Node.CORE_HELP) // dummy node
                .minimumBotPermissions(Permissions.TALK)
                .description("displays an overview of the bot's metrics")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        final StringBuilder builder = new StringBuilder();

        event.getMetricsService().getRegistry().getMetrics().forEach((name, metric) -> {
            builder.append("\n**").append(name).append("**: ");
            if (metric instanceof Gauge) {
                builder.append("value=");
                Object value = ((Gauge) metric).getValue();
                if (value instanceof Double) {
                    builder.append(Formatter.formatDouble((double) value));
                } else if (value instanceof Float) {
                    builder.append(Formatter.formatFloat((float) value));
                } else if (value instanceof Integer) {
                    builder.append(Formatter.formatInteger((int) value));
                } else if (value instanceof Long) {
                    builder.append(Formatter.formatLong((long) value));
                } else {
                    builder.append(value);
                }
            } else if (metric instanceof Counting) {
                builder.append("count=").append(Formatter.formatLong(((Counting) metric).getCount()));
                if (metric instanceof Sampling) {
                    Snapshot snapshot = ((Sampling) metric).getSnapshot();
                    builder.append(", min=").append(Formatter.formatLong(snapshot.getMin()));
                    builder.append(", max=").append(Formatter.formatLong(snapshot.getMax()));
                    builder.append(", mean=").append(Formatter.formatDouble(snapshot.getMean()));
                    builder.append(", std_dev=").append(Formatter.formatDouble(snapshot.getStdDev()));
                    builder.append(", median=").append(Formatter.formatDouble(snapshot.getMedian()));
                    builder.append(", p75=").append(Formatter.formatDouble(snapshot.get75thPercentile()));
                    builder.append(", p95=").append(Formatter.formatDouble(snapshot.get95thPercentile()));
                    builder.append(", p98=").append(Formatter.formatDouble(snapshot.get98thPercentile()));
                    builder.append(", p99=").append(Formatter.formatDouble(snapshot.get99thPercentile()));
                    builder.append(", p999=").append(Formatter.formatDouble(snapshot.get999thPercentile()));
                }
                if (metric instanceof Metered) {
                    Metered metered = (Metered) metric;
                    builder.append(", mean_rate=").append(Formatter.formatDouble(metered.getMeanRate()));
                    builder.append(", m1=").append(Formatter.formatDouble(metered.getOneMinuteRate()));
                    builder.append(", m5=").append(Formatter.formatDouble(metered.getFiveMinuteRate()));
                    builder.append(", m15=").append(Formatter.formatDouble(metered.getFifteenMinuteRate()));
                }
            }
        });

        event.reply(builder.toString(), event::complete);
    }

}
