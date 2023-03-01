package com.salon.ht.config;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Type;

@Configuration
public class GsonConfig {
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Json.class, new SwaggerJsonTypeAdapter())
                .create();
    }

    public static class SwaggerJsonTypeAdapter implements JsonSerializer<Json> {

        @Override
        public JsonElement serialize(Json json, Type type, JsonSerializationContext context) {
            return JsonParser.parseString(json.value());
        }
    }
}
