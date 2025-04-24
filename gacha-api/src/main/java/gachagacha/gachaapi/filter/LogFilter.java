package gachagacha.gachaapi.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String traceId = UUID.randomUUID().toString();
        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        try {
            MDC.put("traceId", traceId); // MDC에 저장
            log.info("REQUEST [{}][{} {}]", traceId, method, requestURI);
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{} {}]", traceId, method, requestURI);
            MDC.clear(); // MDC 정리
        }
    }
}
