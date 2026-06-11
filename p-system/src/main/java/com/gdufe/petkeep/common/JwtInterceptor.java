package com.gdufe.petkeep.common;

import com.gdufe.petkeep.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            send401(response, "未携带认证令牌");
            return false;
        }

        String token = authHeader.substring(7);
        try {
            Long userId = jwtUtils.parseUserId(token);
            Integer role = jwtUtils.parseRole(token);
            UserContext.set(userId, role);
            log.debug("JWT 校验通过 -> userId={}, role={}, uri={}", userId, role, request.getRequestURI());
            return true;
        } catch (Exception e) {
            log.warn("JWT 校验失败 -> {}", e.getMessage());
            send401(response, "令牌无效或已过期");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        UserContext.clear();
    }

    private void send401(HttpServletResponse response, String message) throws Exception {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        String json = String.format("{\"code\":401,\"message\":\"%s\",\"data\":null}", message);
        response.getWriter().write(json);
    }
}
