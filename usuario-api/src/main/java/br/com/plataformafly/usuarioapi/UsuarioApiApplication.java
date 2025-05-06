package br.com.plataformafly.usuarioapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UsuarioApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsuarioApiApplication.class, args);
    }

}
