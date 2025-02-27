package org.example.expert.domain.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.annotation.CheckAdmin;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod method){
            CheckAdmin checkAdmin = method.getMethodAnnotation(CheckAdmin.class);
            if(checkAdmin != null){
                UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));
                log.info("userRole = {}",userRole);
                if(userRole == null || !userRole.equals(UserRole.ADMIN)){
                    throw new AuthException("관리자 권한이 필요한 기능입니다.");
                }
                log.info("admin 인증 요청 시각 = {}, 요청 URL = {}",System.currentTimeMillis(),request.getRequestURI());
            }
        }
        return true;
    }
}
