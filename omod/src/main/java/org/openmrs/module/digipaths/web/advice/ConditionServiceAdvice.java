package org.openmrs.module.digipaths.web.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.guice.RequestScoped;
import org.openmrs.Condition;
import org.openmrs.Patient;
import org.openmrs.api.ConditionService;
import org.openmrs.api.PatientService;
import org.openmrs.module.patientflags.task.PatientFlagTask;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

@RequestScoped
public class ConditionServiceAdvice implements AfterReturningAdvice {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
		
		if ((method.getName().equals("update") || method.getName().equals("delete")) && objects.length > 0
		        && objects[0] instanceof String) {
			
			ConditionService conditionService = org.openmrs.api.context.Context.getService(ConditionService.class);
			Condition condition = conditionService.getConditionByUuid(objects[0].toString());
			if (condition != null && condition.getPatient() != null)
				new PatientFlagTask().generatePatientFlags(condition.getPatient());
		}
		
		if (method.getName().equals("create") && objects.length > 0) {
			org.hl7.fhir.r4.model.Condition fhirCondition = (org.hl7.fhir.r4.model.Condition) objects[0];
			org.hl7.fhir.r4.model.Reference patientRef = fhirCondition.getSubject();
			
			if (patientRef != null && patientRef.getReferenceElement() != null
			        && patientRef.getReferenceElement().getIdPart() != null) {
				PatientService patientService = org.openmrs.api.context.Context.getService(PatientService.class);
				Patient patient = patientService.getPatientByUuid(patientRef.getReferenceElement().getIdPart());
				new PatientFlagTask().generatePatientFlags(patient);
			}
		}
		
	}
}
