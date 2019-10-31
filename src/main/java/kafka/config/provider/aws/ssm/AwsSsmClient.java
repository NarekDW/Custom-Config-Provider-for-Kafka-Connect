package kafka.config.provider.aws.ssm;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

class AwsSsmClient {

    private final AWSSimpleSystemsManagement ssmClient;

    AwsSsmClient() {
        ssmClient = AWSSimpleSystemsManagementClientBuilder
                .defaultClient();
    }

    Map<String, String> getParameters(Set<String> keys) {
        GetParametersRequest request = new GetParametersRequest()
                .withNames(keys);
        return ssmClient.getParameters(request)
                .getParameters()
                .stream()
                .collect(toMap(Parameter::getName, Parameter::getValue));
    }
}
