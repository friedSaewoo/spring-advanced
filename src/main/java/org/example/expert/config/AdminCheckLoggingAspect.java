package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminCheckLoggingAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(org.example.expert.domain.common.annotation.CheckAdmin)")
    private void adminLoggingPoint(){
    };

    @Around("adminLoggingPoint()")
    public Object adminLogging(ProceedingJoinPoint joinPoint) throws Throwable{
        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Long userId = (Long)servletRequest.getAttribute("userId");
        long requestTime = System.currentTimeMillis();
        String requestUrl = servletRequest.getMethod();
        String requestBody = objectMapper.writeValueAsString(joinPoint.getArgs());
        // 실행 전
        log.info("[AOP API 요청 ID] userId = {}",userId);
        log.info("[AOP API 요청 시각] requestTime = {}",requestTime);
        log.info("[AOP API 요청 URL] requestUrl = {}", requestUrl);
        log.info("[AOP API RequestBody] requestBody = {}", requestBody);
        Object result = joinPoint.proceed();

        // 실행 후
        long responseTime = System.currentTimeMillis();
        String responseBody = objectMapper.writeValueAsString(result);
        log.info("[AOP API 응답 시간] responseTime = {}",responseTime);
        log.info("[AOP API 소요 시간] elapsedTime = {}ms",responseTime-requestTime);
        log.info("[AOP API 요청 URL] responseBody = {}", responseBody);
        return result;
    }

}
