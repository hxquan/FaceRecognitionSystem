package com.whut.aop.aspect;

import com.whut.dao.UserRepository;
import com.whut.dao.entity.User;
import com.whut.domain.Response;
import com.whut.util.CookieUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class CheckPermissionAspect {

    private static final Logger LOGGER = Logger.getLogger(CheckPermissionAspect.class);

    @Autowired
    private UserRepository userRepository;



    /**
     * 添加了RecordLog注释的方法，在执行时，会记录日志
     * 日志格式：ID,操作人,方法名,开始时间,结束时间,是否成功,失败原因
     */
    @Around("@annotation(com.whut.aop.interfaces.CheckPermission)")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            String username = CookieUtil.getUsernameFromRequest(request);
            if (username == null) {
                return new Response(Response.Code.UnLoginError).toString();
            }
            User user = userRepository.findByUsername(username);
            if (user == null){
                return new Response(Response.Code.UserNotExistError).toString();
            }
        }
        Object result;
        try {
            //让代理方法执行
            result = pjp.proceed();
        } catch (Exception e) {
            return new Response(Response.Code.SystemError).toString();
        }
        return result;
    }
}
