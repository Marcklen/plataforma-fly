package br.com.plataformafly.emailconsumer.config;

/**
 * Como o RabbitMQEmailListener consome da fila declarada no producer,
 * você precisa recriar apenas a definição mínima da constante da fila no consumer
 * (não é necessário registrar beans do Rabbit aqui, já que ele só ouve).
 */
public class RabbitMQConfig {

    public static final String QUEUE_EMAIL = "email.queue";
}
