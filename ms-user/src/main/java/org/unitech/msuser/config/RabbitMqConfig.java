package org.unitech.msuser.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqConfig {

    private final RabbitTemplate rabbitTemplate;


}
