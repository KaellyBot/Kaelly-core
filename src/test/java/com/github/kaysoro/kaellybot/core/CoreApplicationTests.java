package com.github.kaysoro.kaellybot.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CoreApplicationTests {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> CoreApplication.main(new String[]{"--spring.main.web-environment=false"}));
    }
}