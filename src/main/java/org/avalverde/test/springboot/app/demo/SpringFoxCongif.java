package org.avalverde.test.springboot.app.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxCongif {

    //Le indicamos a SpringFox que queremos seleccionar solo api en el controller y que el path
    //de respuesta debe de ser indicando api/cuentas, donde tenemos los metodos para asi no mostrar
    //llamadas internas
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                .apis(RequestHandlerSelectors.basePackage("org.avalverde.test.springboot.app.demo.controllers"))
                .paths(PathSelectors.ant("/api/cuentas/*")).build();
    }
}
