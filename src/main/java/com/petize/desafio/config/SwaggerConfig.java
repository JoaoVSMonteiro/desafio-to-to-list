package com.petize.desafio.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI todoApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("To-Do-List API")
                        .version("v1")
                        .description("""
                                API REST para gerenciamento de tarefas (To-Do) e subtarefas.
                                Funcionalidades: criar, listar (com filtros), atualizar, excluir e impedir conclusão com subtarefas pendentes.
                                """));
    }

}
