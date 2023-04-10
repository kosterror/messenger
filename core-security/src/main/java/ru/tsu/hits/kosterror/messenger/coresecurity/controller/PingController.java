package ru.tsu.hits.kosterror.messenger.coresecurity.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @Value("${app.name}")
    private String appName;

    @GetMapping("/ping")
    public String ping() {
        return String.format("Сервис '%s' доступен", appName);
    }

}
