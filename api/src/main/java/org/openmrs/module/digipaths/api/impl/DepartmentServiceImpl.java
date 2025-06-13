package org.openmrs.module.digipaths.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.digipaths.Department;
import org.openmrs.module.digipaths.api.DepartmentService;
import org.openmrs.module.digipaths.api.dao.DepartmentDAO;

import java.util.List;
import java.util.Optional;

///**
// * It is a default implementation of {@link DepartmentService}.
// */
public class DepartmentServiceImpl extends BaseOpenmrsService implements DepartmentService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private DepartmentDAO dao;
	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(DepartmentDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * @return the dao
	 */
	public DepartmentDAO getDao() {
		return dao;
	}
	
	//    /**
	//     * @see org.openmrs.module.department.api.DepartmentService#getAllDepartments()
	//     */
	@Override
	public List<Department> getAllDepartments() {
		return dao.getAllDepartments();
	}
	
	//    /**
	//     * @see org.openmrs.module.department.api.DepartmentService#getDepartment(java.lang.Integer)
	//     */
	@Override
	public Department getDepartment(Integer departmentId) {
		return dao.getDepartment(departmentId);
	}
	
	//    /**
	//     * @see org.openmrs.module.department.api.DepartmentService#saveDepartment(org.openmrs.module.department.Department)
	//     */
	@Override
	public Department saveDepartment(Department department) {
		return dao.saveDepartment(department);
	}
	
	//    /**
	//     * @see org.openmrs.module.department.api.DepartmentService#purgeDepartment(org.openmrs.module.department.Department)
	//     */
	@Override
	public void purgeDepartment(Department department) {
		dao.purgeDepartment(department);
	}
	
	@Override
	public Order getOrderByPatientUuidAndConceptUuid(String patientUuid, String conceptUuid) {
		return dao.getOrderByPatientUuidAndConceptUuid(patientUuid, conceptUuid);
	}
	
	@Override
	public List<Obs> getObsByPatientUuidAndConceptUuid(String patientUuid, String conceptUuid, Integer maxResults) {
		return dao.getObsByPatientUuidAndConceptUuid(patientUuid, conceptUuid, maxResults);
	}
	
	@Override
	public boolean getConditionByPatientUuidAndConceptId(String patientUuid, Integer conceptUuid) {
		return dao.getConditionByPatientUuidAndConceptId(patientUuid, conceptUuid);
	}
}
