package gachagacha.gachagacha.interceptor;

import gachagacha.gachagacha.auth.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("AuthInterceptor start");
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        jwtUtils.validateAccessToken(request);
        return true;
    }
}
