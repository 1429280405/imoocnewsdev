package com.imooc.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author liujinqiang
 * @create 2021-08-20 19:54
 */
@Aspect
@Component
@Slf4j
public class ServiceLogAspect {

    @Around("execution(* com.imooc.*.service.impl..*.*(..))")
    public Object recordTimeService(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("=====开始执行 {}.{}==========",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        long takeTime = end - start;
        if (takeTime > 3000) {
            log.error("当前执行耗时：{}", takeTime);
        } else if (takeTime > 2000) {
            log.warn("当前执行耗时：{}", takeTime);
        } else {
            log.info("当前执行耗时：{}", takeTime);
        }

        return result;
    }
}
