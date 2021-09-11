/*
 * arg license
 *
 */

package com.arg.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;

import com.arg.common.exception.ArgException;
import com.arg.common.utils.MapperUtil;

import lombok.extern.slf4j.Slf4j;

@EnableAspectJAutoProxy
@Slf4j
@Aspect
@Configuration
public class LogAspect {

	@SuppressWarnings({ "unchecked" })
	@Around("@annotation(Log)")
	public Object doAround(ProceedingJoinPoint joinPoint) {
		log.debug("Start of {}.{} method with request data {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), MapperUtil.toJson(joinPoint.getArgs()));
		Object obj = null;
		try {
			obj =  joinPoint.proceed();
		} catch (Throwable throwable) {
			log.error("Error in {}.{} method ", joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName(), throwable);
			if(throwable instanceof ArgException) {
				ArgException e = (ArgException)throwable;
				throw new ArgException(e.getMessage(), e.getStatus());
			}
			throw new ArgException(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.debug("End of {}.{} method with data {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), MapperUtil.toJson(obj));
		return obj;
	}
}
