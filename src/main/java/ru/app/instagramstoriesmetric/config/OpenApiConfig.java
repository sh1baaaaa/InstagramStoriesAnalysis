package ru.app.instagramstoriesmetric.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Микросервис для анализа Instagram Stories",
                description = "Веб-приложение позволяющее получать метрики сторисов из Instagram",
                version = "1.0"
        )
)
public class OpenApiConfig {
}
