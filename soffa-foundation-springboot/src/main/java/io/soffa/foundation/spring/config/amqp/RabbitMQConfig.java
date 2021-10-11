package io.soffa.foundation.spring.config.amqp;

import io.soffa.foundation.commons.IDs;
import io.soffa.foundation.commons.JsonUtil;
import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.events.Event;
import io.soffa.foundation.pubsub.PubSubClient;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQConfig {

    private static final Logger LOG = Logger.get(RabbitMQConfig.class);
    static boolean embeddedMode;
    private final RabbitTemplate rabbitTemplate;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${app.amqp.exchange:app}")
    private String exchange;
    @Value("${app.amqp.routing:services}")
    private String routing;

    public RabbitMQConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    Queue queue() {
        return new Queue(applicationName);
    }

    @Bean
    TopicExchange createTopicExchange() {
        return new TopicExchange(exchange + ".topic", true, false);
    }

    @Bean
    public FanoutExchange createFanoutExchange() {
        return new FanoutExchange(exchange + ".fanout", true, false);
    }

    @Bean
    Binding createTopicBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routing + "." + applicationName);
    }

    @Bean
    Binding createFanoutBinding(Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Primary
    @Bean
    PubSubClient createPubSubClient() {
        return new PubSubClient() {

            @Override
            public void send(String target, Event event) {
                if (TextUtil.isEmpty(event.getId())) {
                    event.setId(IDs.secureRandomId("evt_"));
                }
                rabbitTemplate.convertAndSend(exchange + ".topic", routing + "." + target, JsonUtil.serialize(event).getBytes());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("[PUB-SUB] Message sent to %s - @action:%s", target, event.getAction());
                }
            }

            @Override
            public void broadcast(Event event) {
                rabbitTemplate.convertAndSend(exchange + ".fanout", routing + ".*", JsonUtil.serialize(event).getBytes());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("[PUB-SUB] Message broadcasted to %s.* - @action:%s", routing, event.getAction());
                }
            }

            @Override
            public void sendInternal(Event event) {
                send(applicationName, event);
            }
        };
    }


}
