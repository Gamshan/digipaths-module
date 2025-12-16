package org.openmrs.module.digipaths.web.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.guice.RequestScoped;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

@RequestScoped
public class EncounterServiceAdvice implements AfterReturningAdvice {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	public EncounterServiceAdvice() {
		System.out.println("✅ EncounterServiceAdvice loaded successfully!");
	}
	
	@Override
	public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
		System.out.println("YESSSSSSSSSSS ENC => " + method.getName());
		
	}
}
