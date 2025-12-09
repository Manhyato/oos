package ru.sspo.oos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Swagger/OpenAPI для документации API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Онлайн-система заказов пиццы")
                        .version("1.0.0")
                        .description("API для управления заказами пиццы. Реализует процессы из DFD: " +
                                "1.0 - Приём и обработка заказа, " +
                                "2.0 - Оплата заказа, " +
                                "3.0 - Организация доставки"));
    }
}

