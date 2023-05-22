package com.itcatcetc.smarthome.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;


import java.io.IOException;

public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        String json = objectMapper.writeValueAsString("{\"message\":\"Successfully logged out\"}");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
