package com.sh.thermostat.actuator.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.util.StringUtils;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression("${aws.dynamodb.enabled:false} == true")
@EnableDynamoDBRepositories("com.sh.thermostat.actuator.domain.log")
public class DynamoDBConfig {

    @Value("${aws.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;
    @Value("${aws.dynamodb.region}")
    private String region;

    @Bean
    @ConditionalOnProperty("aws.dynamodb.region")
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDBClientBuilder clientBuilder = AmazonDynamoDBClientBuilder.standard();

        if (!StringUtils.isNullOrEmpty(amazonDynamoDBEndpoint)) {
            clientBuilder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, region));
        } else {
            clientBuilder.withRegion(Regions.EU_CENTRAL_1);
        }
        return clientBuilder.build();
    }

    @Bean
    public DynamoDBTemplate dynamoDBOperations(AmazonDynamoDB dynamoDB) {
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder().build();
        return new DynamoDBTemplate(dynamoDB, new DynamoDBMapper(dynamoDB), config);
    }

}
