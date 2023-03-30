package ru.tsu.hits.kosterror.messenger.authservice.service.jwt.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ServletResponseService {

    void sendError(HttpServletResponse response, int statusCode, String... errorMessage) throws IOException;

}
