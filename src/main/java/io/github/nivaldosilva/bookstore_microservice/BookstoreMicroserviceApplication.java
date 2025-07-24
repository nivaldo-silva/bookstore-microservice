package io.github.nivaldosilva.bookstore_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@OpenAPIDefinition(
    info = @Info(
        title = "Bookstore Microservice API",
        version = "1.0",
        description = "Documentação da API do Microserviço de Livraria, incluindo gerenciamento de autores, livros, clientes e pedidos.",
        contact = @Contact(
            name = "Nivaldo Silva",
            url = "https://www.linkedin.com/in/nivaldo-silva-5a8335289/",
            email = "nivaldosilva.contato@gmail.com"
        )
    ),
    externalDocs = @ExternalDocumentation(
        description = "Repositório GitHub do Projeto",
        url = "https://github.com/nivaldo-silva/springboot-bookstore-microservice"
    ),
    servers = { 
        @Server(url = "http://localhost:8080", description = "Servidor de Desenvolvimento Local")
    }
)
public class BookstoreMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreMicroserviceApplication.class, args);
	}

}
