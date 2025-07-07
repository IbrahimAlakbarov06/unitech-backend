package org.unitech.msauth.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Bean
    public Queue userRegistrationQueue() {
        return new Queue("user.registration", true);
    }

    @Bean
    public Queue userLoginQueue() {
        return new Queue("user.login", true);
    }

    @Bean
    public Queue userLogoutQueue() {
        return new Queue("user.logout", true);
    }

    @Bean
    public Queue userUpdateQueue() {
        return new Queue("user.update", true);
    }

    @Bean
    public Queue userDeleteQueue() {
        return new Queue("user.delete", true);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}