package gachagacha.gachagacha.api;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ApiLoggingAspect {

    @Pointcut("execution(* gachagacha.gachagacha.api..*.*(..))")
    private void cut(){}

    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        Object[] args = joinPoint.getArgs();
        log.info("Receive request on [{}.{}], Arguments: {}", className, methodName, Arrays.toString(args));
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        if (returnObj != null) {
            log.info("Response from [{}.{}], Response data: {}", className, methodName, returnObj.toString());
        } else {
            log.info("Response from [{}.{}], Response data: {}", className, methodName, null);
        }
    }
}
