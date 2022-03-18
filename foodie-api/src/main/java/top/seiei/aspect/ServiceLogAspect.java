package top.seiei.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Service 耗时时间日志输出切面
 */
@Aspect
@Component
public class ServiceLogAspect {

    final static Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     * * 表示切面返回的类型不作限制
     * top.seiei.service.impl.. 表示 top.seiei.service.impl 下的所有包
     * *.*(..) 表示所有的方法（参数不作限制）
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* top.seiei.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取类名和方法名
        logger.info("====== 正在执行 {}.{} ======", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());

        // 记录当前时间戳
        long begin = System.currentTimeMillis();

        // 执行切面逻辑
        Object result = joinPoint.proceed();

        // 获取执行耗时时间
        long end = System.currentTimeMillis();
        long takeTime = end - begin;

        if (takeTime > 3000) {
            logger.error("====== {}.{} 执行结束，耗时：{}毫秒 ======", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), takeTime);
        } else if (takeTime > 2000) {
            logger.warn("====== {}.{} 执行结束，耗时：{}毫秒 ======", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), takeTime);
        } else {
            logger.info("====== {}.{} 执行结束，耗时：{}毫秒 ======", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), takeTime);
        }

        return result;
    }
}
