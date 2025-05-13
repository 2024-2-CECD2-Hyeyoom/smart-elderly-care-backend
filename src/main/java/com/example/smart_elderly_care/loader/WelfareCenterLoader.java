package com.example.smart_elderly_care.loader;

import com.example.smart_elderly_care.service.WelfareCenterApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WelfareCenterLoader implements CommandLineRunner {

    private final WelfareCenterApiService apiService;

    @Override
    public void run(String... args) {
        apiService.syncWelfareCenters();
    }
}
