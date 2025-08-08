package com.ejada.taskmanagementsystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {

    @AfterThrowing(pointcut = "execution(* com.ejada.taskmanagementsystem.service..*(..))", throwing = "ex")

    public void logException(JoinPoint joinPoint, Throwable ex) {
        log.error("Exception in {}.{}() with cause = {} and exception = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                ex.getCause() != null ? ex.getCause() : "NULL",
                ex.getMessage(), ex);
    }
}
