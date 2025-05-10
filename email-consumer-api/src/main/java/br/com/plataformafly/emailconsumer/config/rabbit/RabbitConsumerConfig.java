package br.com.plataformafly.emailconsumer.config.rabbit;

import br.com.plataformafly.emailconsumer.dto.EmailMessageDTO;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@EnableRabbit
@Configuration
public class RabbitConsumerConfig {

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper());
        return converter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("br.com.plataformafly.usuarioapi.model.dto.in.EmailMessageDTO", EmailMessageDTO.class);
        classMapper.setIdClassMapping(idClassMapping);
        classMapper.setDefaultType(EmailMessageDTO.class);
        return classMapper;
    }
}

/*
   Esta configuração é necessária para que o RabbitMQ consiga serializar e desserializar as mensagens corretamente.
   ERRO que apresentou abaixo:
   __TypeId__ = br.com.plataformafly.usuarioapi.model.dto.in.EmailMessageDTO, trate como br.com.plataformafly.emailconsumer.dto.EmailMessageDTO”.
 */