package ru.tsu.hits.kosterror.messenger.authservice.util;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

public class SecuredEndpoints {

    public static final List<AntPathRequestMatcher> ENDPOINTS = List.of();
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String PATCH = "PATCH";
    private static final String DELETE = "DELETE";

}
