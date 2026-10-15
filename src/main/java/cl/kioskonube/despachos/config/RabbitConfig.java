package cl.kioskonube.despachos.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Topología RabbitMQ del despachos-service. Solo configuración, sin lógica de negocio.
 */
@Configuration
public class RabbitConfig {

    private final MensajeriaProperties props;

    public RabbitConfig(MensajeriaProperties props) {
        this.props = props;
    }

    // ===== Exchanges de dominio (topic) =====

    @Bean
    public TopicExchange despachosExchange() {
        return new TopicExchange(props.despachos().exchange(), true, false);
    }

    @Bean
    public TopicExchange ordenesExchange() {
        return new TopicExchange(props.ordenes().exchange(), true, false);
    }

    // ===== Caso de uso: orden creada -> crear despacho =====

    @Bean
    public Queue ordenesQueue() {
        String q = props.ordenes().queue();
        return QueueBuilder.durable(q)
                .deadLetterExchange(props.dlxDe(q))
                .deadLetterRoutingKey(q)
                .build();
    }

    @Bean
    public Binding ordenesBinding() {
        return BindingBuilder.bind(ordenesQueue()).to(ordenesExchange()).with(props.ordenes().routingKey());
    }

    @Bean
    public DirectExchange ordenesDlx() {
        return new DirectExchange(props.dlxDe(props.ordenes().queue()), true, false);
    }

    @Bean
    public Queue ordenesDlq() {
        return QueueBuilder.durable(props.dlqDe(props.ordenes().queue())).build();
    }

    @Bean
    public Binding ordenesDlqBinding() {
        return BindingBuilder.bind(ordenesDlq()).to(ordenesDlx()).with(props.ordenes().queue());
    }

    // ===== Envío de mensajes en JSON (solo el productor) =====

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new JacksonJsonMessageConverter());
        return template;
    }
}
