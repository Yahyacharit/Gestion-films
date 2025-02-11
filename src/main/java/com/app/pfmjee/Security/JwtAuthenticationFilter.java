package com.app.pfmjee.Security;

import jakarta.servlet.Filter;
import org.springframework.stereotype.Component;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter implements Filter {

    private JwtUtil jwtUtil = new JwtUtil();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        if (requestURI.equals("/auth/login") || requestURI.equals("/auth/register") || requestURI.equals("/auth/logout")) {
            chain.doFilter(request, response);
            return;
        }

        String token = (String) httpRequest.getSession().getAttribute("token");

        if (token == null) {
            String authorizationHeader = httpRequest.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            }
        }

        if (token != null && jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            String role = jwtUtil.extractClaim(token, claims -> claims.get("role", String.class));

            if ("USER".equals(role)) {
                if (requestURI.equals("/films") || requestURI.matches(".*/films/\\d+$")) {
                    chain.doFilter(request, response);
                    return;
                } else {
                    httpResponse.sendRedirect("/films");
                    return;
                }
            } else if ("ADMIN".equals(role)) {
                chain.doFilter(request, response);
                return;
            }
        }

        httpResponse.sendRedirect("/auth/login");
    }



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
