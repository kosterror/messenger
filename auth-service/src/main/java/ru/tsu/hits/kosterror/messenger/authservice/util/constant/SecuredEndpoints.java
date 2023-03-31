package ru.tsu.hits.kosterror.messenger.authservice.util.constant;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

public class SecuredEndpoints {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String PATCH = "PATCH";
    private static final String DELETE = "DELETE";

    public static final List<AntPathRequestMatcher> ENDPOINTS = List.of(
            new AntPathRequestMatcher("/api/account/**")
    );

}
