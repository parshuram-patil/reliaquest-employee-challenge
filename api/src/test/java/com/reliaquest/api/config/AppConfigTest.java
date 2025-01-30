package com.reliaquest.api.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class AppConfigTest {

    @InjectMocks
    AppConfig config;

    @Test
    void shouldCreateRestTemplate() {
        RestTemplate restTemplate = config.restTemplate();
        assertNotNull(restTemplate);
        assertEquals("org.springframework.web.client.RestTemplate", restTemplate.getClass().getCanonicalName());
    }

}