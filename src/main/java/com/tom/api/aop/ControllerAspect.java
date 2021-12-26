package com.tom.api.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
@Order(3)
public class ControllerAspect {

    private static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    /**
     * 該package 下所有class,method都被攔截
     */
    @Pointcut("execution(* com.tom.api.controller..*(..))")
    public void pointcut() { }

    /**
     * 指定当前执行方法在logPointCut之前执行
     * */
    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        String clazzName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        // 接收到请求，记录请求内容
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        if (response.getStatus() >= 400) {
            //logger.info("[{}] - [{}] (1) 上一步的檢查沒通過, 不继續執行後面的 AOP 動作.", clazzName, methodName);
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String oToken = request.getHeader("oToken");

            StringBuilder sb = new StringBuilder();
            sb.append("[" + clazzName + "] - [" + methodName + "] (1) 收到請求");
            sb.append(", ");
            sb.append("請求地址:" + request.getRequestURL().toString());
            sb.append(", ");
            sb.append("請求方法:" + request.getMethod());
            sb.append(", ");
            if(oToken != null && oToken.length() > 0){
                sb.append("oToken:" + oToken);
                sb.append(", ");
            }
//                sb.append("請求參數(入參):" + Arrays.toString(joinPoint.getArgs()));
            sb.append("請求參數(入參):" + args2String(joinPoint) );

            logger.info(sb.toString());
        }
    }


    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        String clazzName = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        if(response.getStatus() >= 400){
            //logger.info("[{}] - [{}] (2) 上一步的檢查沒通過, 不继續執行後面的 AOP 動作.", clazzName, methodName);
            return null; // it will not call doBefore again
        }else {
            Object ob = pjp.proceed();// ob 为方法的返回值
            logger.info("[" + clazzName + "] - [" + methodName + "] (4) 耗時:" + (System.currentTimeMillis() - startTime) + "豪秒");
            return ob;
        }
    }

    @After("pointcut()")
    public void doAfter(JoinPoint joinPoint) {
        String clazzName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        if(response.getStatus() >= 400){
            //logger.info("[{}] - [{}] (3) 上一步的檢查沒通過, 不继續執行後面的 AOP 動作.", clazzName, methodName);
        }else {
            logger.info("[" + clazzName + "] - [" + methodName + "] (3) 結束請求");
        }
    }


    /**
     * 指定在方法之后返回
     * */
    @AfterReturning(returning = "obj", pointcut = "pointcut()")// returning的值和doAfterReturning的参数名一致
    public void doAfterReturning(JoinPoint joinPoint, Object obj) throws Throwable {
        // 处理完请求，返回内容(返回值太复杂时，打印的是物理存储空间的地址)
        String clazzName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        if(response.getStatus() >= 400){
            //logger.info("[{}] - [{}] (4) 上一步的檢查沒通過, 不继續執行後面的 AOP 動作.", clazzName, methodName);
        }else {
            if (obj != null) {
                if( obj instanceof java.util.AbstractCollection ){
                    logger.info("[" + clazzName + "] - [" + methodName + "] (2) 執行成功, 返回(" + obj.getClass() + "), " + ((java.util.AbstractCollection)obj).size() + "筆記錄.");
                }else{
                    logger.info("[" + clazzName + "] - [" + methodName + "] (2) 執行成功, 返回(" + obj.getClass() + ")");
                }
            } else {
                logger.info("[" + clazzName + "] - [" + methodName + "] (2) 執行成功");
            }
        }
    }

    /**
     * AfterThrowing: 異常通知
     */
    @AfterThrowing(value="pointcut()",throwing="e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e){
        String clazzName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        logger.error("[" + clazzName + "] - [" + methodName + "] (2) 執行失敗, 執行" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + " <-- 執行過程中拋出 excepton: " + e.getMessage());
    }


    protected String args2String(JoinPoint joinPoint ) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        if(codeSignature.getParameterNames().length == 0){
            return "";
        }else{
            StringBuffer sb = new StringBuffer("[");
            String[] names = codeSignature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            for(int i = 0; i < names.length; i++ ){
                sb.append("" +  names[i] + "-->" + args[i]);
                if(i != names.length -1 ){
                    sb.append(", ");
                }else{
                    sb.append("]");
                }
            }
            return sb.toString();
        }
    }
}
