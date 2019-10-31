package kafka.config.provider.aws.ssm;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.config.ConfigData;
import org.apache.kafka.common.config.provider.ConfigProvider;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class AwsSsmConfigProvider implements ConfigProvider {

    private static final Long TTL = Duration.of(1, ChronoUnit.MINUTES).toMillis();
    private final AwsSsmClient ssmClient;

    public AwsSsmConfigProvider() {
        this.ssmClient = new AwsSsmClient();
    }

    @Override
    public ConfigData get(String path, Set<String> keys) {
        log.info("Retrieve values for keys {} from AWS SSM", keys);
        Map<String, String> parameters = ssmClient.getParameters(keys);
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