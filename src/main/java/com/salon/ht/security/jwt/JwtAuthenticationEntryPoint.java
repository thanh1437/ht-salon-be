package com.salon.ht.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        LOGGER.error("Unauthorized error: {}", e.getMessage());
        if (httpServletRequest.getAttribute("javax.servlet.error.exception") != null) {
            Throwable throwable = (Throwable) httpServletRequest.getAttribute("javax.servlet.error.exception");
            resolver.resolveException(httpServletRequest, httpServletResponse, null, (Exception) throwable);
        }

        if (!httpServletResponse.isCommitted()) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
        }

    }

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
}
