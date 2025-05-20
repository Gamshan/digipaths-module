package org.openmrs.module.digipaths.api.dao;

import org.openmrs.Order;
import org.openmrs.module.digipaths.Department;

import java.util.List;

///**
// * Database methods for {@link DepartmentService}.
// */
public interface DepartmentDAO {
	
	//	/**
	//	 * @see org.openmrs.module.department.api.DepartmentService#getAllDepartments()
	//	 */
	List<Department> getAllDepartments();
	
	//	/**
	//	 * @see org.openmrs.module.department.api.DepartmentService#getDepartment(java.lang.Integer)
	//	 */
	Department getDepartment(Integer departmentId);
	
	//	/**
	//	 * @see org.openmrs.module.department.api.DepartmentService#saveDepartment(org.openmrs.module.department.Department)
	//	 */
	Department saveDepartment(Department department);
	
	//	/**
	//	 * @see org.openmrs.module.department.api.DepartmentService#purgeDepartment(org.openmrs.module.department.Department)
	//	 */
	void purgeDepartment(Department department);
	
	Order getOrder(String patientUuid);
}
