package com.azizi.common.property;

import com.azizi.common.property.factory.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@ConfigurationProperties(prefix = "rabbitmq")
@PropertySource(value = "classpath:application-rabbitmq.yaml", factory = YamlPropertySourceFactory.class)
public class RabbitMqProperties {

    private String username;
    private String password;
    private String host;
    private Integer port;

}
