package com.devsolutions.medsys.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerConfigTest {

    @Test
    void openAPI_retornaInstanciaNaoNula() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI openAPI = config.openAPI();

        assertThat(openAPI).isNotNull();
    }

    @Test
    void openAPI_temTituloCorreto() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI openAPI = config.openAPI();

        assertThat(openAPI.getInfo()).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("MedSys API");
    }

    @Test
    void openAPI_temVersaoCorreta() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI openAPI = config.openAPI();

        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0.0");
    }

    @Test
    void openAPI_temSecurityScheme_bearerAuth() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI openAPI = config.openAPI();

        assertThat(openAPI.getComponents()).isNotNull();
        assertThat(openAPI.getComponents().getSecuritySchemes()).containsKey("bearerAuth");
    }

    @Test
    void openAPI_temSecurityRequirement() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI openAPI = config.openAPI();

        assertThat(openAPI.getSecurity()).isNotNull().isNotEmpty();
    }
}
