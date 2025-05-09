package com.Rezeptmanager.Rezeptmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpoonacularConfig {

    @Value("${spoonacular.api.key}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}