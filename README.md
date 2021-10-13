# AWS Systems Manager Config Provider for Kafka Connect

This is an example how to retrieve configuration parameters for Kafka Connectors from External System.
You can find some additional information in details here: [Kafka Connect Security](https://docs.confluent.io/current/connect/security.html#externalizing-secrets)

Current implementation uses **AWS Systems Manager** as an External System whcich stores configuration parameters.

## Usage
Build: (assuming you are building from project root)
```
./gradlew fatJar
```

Add a reference to the jar file in the Kafka Connect Worker Config file:
```
plugin.path=.../folder_with_the_jar_file
```

Add additional properties for the worker configuration: 
```
config.providers=ssm   # multiple comma-separated provider types can be specified here
config.providers.ssm.class=kafka.config.provider.aws.ssm.AwsSsmConfigProvider
```

Then you can reference the configuration variables in the connector configuration as follow,
for example here is configuration for Snowflake Connector for Kafka Connect:
```
...
# These parameters will be retrieved from AWS SSM
snowflake.private.key=${ssm:SNOWFLAKE_PRIVATE_KEY}
snowflake.private.key.passphrase=${ssm:SNOWFLAKE_PRIVATE_KEY_PASSPHRASE}
...
```
