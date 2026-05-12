package com.fazil.learn_spring.learnspring_jpa;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeMonitorAspect {

    @Around("@annotation(com.fazil.learn_spring.learnspring_jpa)")
    public Object logTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = ((System.currentTimeMillis()) - start);
        System.out.println(joinPoint.getSignature() + "takes: " + executionTime + " ms");

        return proceed;
    }
}
