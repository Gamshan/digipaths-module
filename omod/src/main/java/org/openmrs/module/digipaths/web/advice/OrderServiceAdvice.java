package org.openmrs.module.digipaths.web.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.guice.RequestScoped;
import org.openmrs.Order;

import org.openmrs.module.patientflags.task.*;
import org.springframework.aop.AfterReturningAdvice;

import org.openmrs.api.context.Context;
import java.lang.reflect.Method;

@RequestScoped
public class OrderServiceAdvice implements AfterReturningAdvice {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	public OrderServiceAdvice() {
		System.out.println("✅ OrderServiceAdvice loaded successfully!");
	}
	
	@Override
	public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
		
		if ((method.getName().equals("saveOrder") || method.getName().equals("saveOrUpdate")) && objects.length > 0
		        && objects[0] instanceof Order) {
			Order order = (Order) objects[0];
			new PatientFlagTask().generatePatientFlags(order.getPatient());
		}
		
	}
}
