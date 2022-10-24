package ru.practicum.shareit.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class GlobalLog {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void callPublicControllerMethod() {
    }

    @AfterReturning(pointcut = "callPublicControllerMethod()", returning = "result")
    public void afterCallPublicControllerMethod(JoinPoint joinPoint, Object result) {
        String args = Arrays.toString(joinPoint.getArgs());
        String controller = joinPoint.getSignature().getDeclaringTypeName();
        String method = String.valueOf(joinPoint.getSignature().getName());
        String res = result != null ? result.toString() : "void";
        log.info(String.format(
                "Request in %s | Method: %s | Body: %s | Return: %s", controller, method, args.substring(1, args.length() - 1), res));
    }

    @Pointcut("execution(public * ru.practicum.shareit.config.exception.*.*(..))")
    public void callErrorControllerMethod() {
    }

    @Before("callErrorControllerMethod()")
    public void beforeCallPublicErrorMethod(JoinPoint joinPoint) {
        String args = Arrays.toString(joinPoint.getArgs());
        String controller = joinPoint.getSignature().getDeclaringTypeName();
        log.error(String.format(
                "Error in %s | Exception and message: %s", controller, args.substring(1, args.length() - 1)));
    }
}
