package kafka.config.provider.aws.ssm;

import org.apache.kafka.common.config.ConfigData;
import org.apache.kafka.common.config.provider.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AwsSsmConfigProvider implements ConfigProvider {
    private static final Logger log = LoggerFactory.getLogger(AwsSsmConfigProvider.class);

    private static final Long TTL = Duration.of(1, ChronoUnit.MINUTES).toMillis();
    private final AwsSsmClient ssmClient;

    public AwsSsmConfigProvider() {
        this.ssmClient = new AwsSsmClient();
    }

    @Override
    public ConfigData get(String path, Set<String> keys) {
        log.info("SSM PLUGIN: Retrieve values for keys {} from AWS SSM", keys);
        Map<String, String> parameters = ssmClient.getParameters(keys);
        log.info("SSM PLUGIN: Retrieved {} parameters from SSM", parameters.size());
        Set<String> absentKeys = keys.stream()
                .filter(key -> !parameters.containsKey(key))
                .collect(Collectors.toSet());

        if (!absentKeys.isEmpty()) {
            log.error("Some key(s) not found in AWS SSM: {}", absentKeys);
            throw new KeyNotFoundException();
        }

        return new ConfigData(parameters, TTL);
    }


    @Override
    public ConfigData get(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}
