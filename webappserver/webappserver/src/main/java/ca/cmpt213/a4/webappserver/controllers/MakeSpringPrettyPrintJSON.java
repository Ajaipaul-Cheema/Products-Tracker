package ca.cmpt213.a4.webappserver.controllers;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * Make the application return pretty-printed JSON (easier to debug)
 * COPIED FROM: https://stackoverflow.com/questions/36119852/spring-boot-actuator-pretty-print-json
 */
@Configuration
public class MakeSpringPrettyPrintJSON extends WebMvcConfigurationSupport {

    /**
     * This method helps make the json more readable
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) {
                jacksonConverter.setPrettyPrint(true);
            }
        }
    }
}
