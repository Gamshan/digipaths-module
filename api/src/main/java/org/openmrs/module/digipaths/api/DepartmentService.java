package org.openmrs.module.digipaths.api;

import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.digipaths.Department;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The service for managing departments.
 */
@Transactional
public interface DepartmentService extends OpenmrsService {
	
	/**
	 * Gets a list of departments.
	 * 
	 * @return the department list.
	 */
	@Transactional(readOnly = true)
	List<Department> getAllDepartments();
	
	/**
	 * Gets a department for a given id.
	 * 
	 * @param departmentId the department id
	 * @return the department with the given id
	 */
	@Transactional(readOnly = true)
	Department getDepartment(Integer departmentId);
	
	/**
	 * Saves a new or existing department.
	 * 
	 * @param department the department to save.
	 * @return the saved department.
	 */
	Department saveDepartment(Department department);
	
	/**
	 * Deletes a department from the database.
	 * 
	 * @param department the department to delete.
	 */
	void purgeDepartment(Department department);
	
	Order getOrderByPatientUuidAndConceptUuid(String patientUuid, String conceptUuid);
	
	List<Obs> getObsByPatientUuidAndConceptUuid(String patientUuid, String conceptUuid, Integer maxResults);
	
	boolean getConditionByPatientUuidAndConceptId(String patientUuid, Integer conceptUuid);
}
