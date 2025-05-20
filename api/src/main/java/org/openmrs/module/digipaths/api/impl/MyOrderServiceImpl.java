package org.openmrs.module.digipaths.api.impl;

import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.module.digipaths.api.MyOrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

///**
// * It is a default implementation of {@link DepartmentService}.
// */
public class MyOrderServiceImpl implements MyOrderService {
	
	@Autowired
	private PatientService patientService; // OpenMRS core service
	
	@Override
	public List<Patient> getAllPatients() {
		return patientService.getAllPatients(); // you can also filter here
	}
}
