package com.unnamed.matchmaking.cs16_matchmaking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Counter Striker - MatchMaking")
                        .version("1.0.0")
                        .description("""
                                Essa API está em processo de desenvolvimento.
                                A sua principal funcionalidade é transformar partidas
                                de CS 1.6 mais organizadas e competitivas.
                                """)
                        .contact(new Contact()
                                .name("Luiz Gabriel")
                                .email("luizgabrielmartins2005@gmail.com")
                                .url("""
                                        Github: https://github.com/Luiztins1
                                        Linkedin: https://linkedin.com/in/luiz-gabriel-martins-dev
                                        """)));
    }
}
