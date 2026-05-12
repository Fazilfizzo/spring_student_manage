package com.fazil.learn_spring.learnspring_jpa.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class LoggingRequestFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LoggingRequestFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
       log.info("[StudentFilter] - Inside doFilter method");
       log.info("local port : " + servletRequest.getLocalPort());
       log.info("Server Name: " + servletRequest.getServerName());

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        log.info("Method name : " + httpServletRequest.getMethod());
        log.info("Request URI : " + httpServletRequest.getRequestURI());
        log.info("from IP : " + httpServletRequest.getRemoteAddr());
        log.info("Servlet Path : " + httpServletRequest.getServletPath());

        filterChain.doFilter(servletRequest, servletResponse);

        System.out.println("Outgoing response:" + servletResponse.getContentType());
    }

}
