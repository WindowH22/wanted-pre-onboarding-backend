package com.windowh22.wantedpreonboardingbackend.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTraceAop {

    @Around("execution(* com.windowh22.wantedpreonboardingbackend.service.*.*(..))") // 패키지 하위에 모두 적용
    public Object execut(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("Start: " + joinPoint.toString());
        try {

            return joinPoint.proceed(); // 다음 로직으로 넘어간다.
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("End: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}
