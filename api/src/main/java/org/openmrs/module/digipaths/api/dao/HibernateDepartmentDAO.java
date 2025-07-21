package org.openmrs.module.digipaths.api.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.ConditionClinicalStatus;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.module.digipaths.Department;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

///**
// * The default implementation of {@link DepartmentDAO}.
// */
public class HibernateDepartmentDAO implements DepartmentDAO {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	//    /**
	//     * @param sessionFactory the sessionFactory to set
	//     */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	//    /**
	//     * @return the sessionFactory
	//     */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	//    /**
	//     * @see org.openmrs.module.department.api.db.DepartmentDAO#getAllDepartments()
	//     */
	@Override
	@SuppressWarnings("unchecked")
	public List<Department> getAllDepartments() {
		return (List<Department>) getCurrentSession().createCriteria(Department.class).list();
	}
	
	//    /**
	//     * @see org.openmrs.module.department.api.DepartmentService#getDepartment(java.lang.Integer)
	//     */
	@Override
	public Department getDepartment(Integer departmentId) {
		return (Department) sessionFactory.getCurrentSession().get(Department.class, departmentId);
	}
	
	//    /**
	//     * @see org.openmrs.module.department.api.db.DepartmentDAO#saveDepartment(org.openmrs.module.department.Department)
	//     */
	@Override
	public Department saveDepartment(Department department) {
		getCurrentSession().save(department);
		return department;
	}
	
	//    /**
	//     * @see org.openmrs.module.department.api.db.DepartmentDAO#purgeDepartment(org.openmrs.module.department.Department)
	//     */
	@Override
	public void purgeDepartment(Department department) {
		sessionFactory.getCurrentSession().delete(department);
	}
	
	private Session getCurrentSession() {
		try {
			return sessionFactory.getCurrentSession();
		}
		catch (NoSuchMethodError ex) {
			try {
				Method method = sessionFactory.getClass().getMethod("getCurrentSession", null);
				return (Session) method.invoke(sessionFactory, null);
			}
			catch (Exception e) {
				throw new RuntimeException("Failed to get the current hibernate session from HibernateDepartmentDAO", e);
			}
		}
	}
	
	@Override
	public Order getOrderByPatientUuidAndConceptUuid(String patientUuid, String conceptUuid) {
		return (Order) getCurrentSession()
		        .createQuery(
		            "from Order o where o.patient.uuid = :patientUuid and o.concept.uuid = :conceptUuid and o.fulfillerStatus = :fulfillerStatus order by o.dateActivated desc")
		        .setParameter("patientUuid", patientUuid).setParameter("fulfillerStatus", Order.FulfillerStatus.COMPLETED)
		        .setParameter("conceptUuid", conceptUuid).setMaxResults(1).uniqueResult();
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Obs> getObsByPatientUuidAndConceptUuid(String patientUuid, String conceptUuid, Integer maxResults) {
		return (List<Obs>) getCurrentSession()
		        .createQuery(
		            "from Obs o where o.person.uuid = :personUuid and o.concept.uuid = :conceptUuid order by o.obsDatetime desc")
		        .setParameter("personUuid", patientUuid).setParameter("conceptUuid", conceptUuid)
		        .setMaxResults(maxResults != null ? maxResults : 1).list();
	}
	
	@Override
	public boolean getConditionByPatientUuidAndConceptUuid(String patientUuid, String conceptId) {
		Long count = (Long) getCurrentSession()
		        .createQuery(
		            "select count(o) from Condition o where o.patient.uuid = :patientUuid and o.clinicalStatus = :clinicalStatus and o.condition.coded.uuid = :conceptId order by o.dateCreated desc")
		        .setParameter("patientUuid", patientUuid).setParameter("clinicalStatus", ConditionClinicalStatus.ACTIVE)
		        .setParameter("conceptId", conceptId).uniqueResult();
		return count > 0;
		
	}
	
	@Override
	public boolean existOrderByPatientUuidAndConceptId(String patientUuid, Integer conceptId) {
		Long count = (Long) getCurrentSession()
		        .createQuery(
		            "select count(o) from Order o where o.patient.uuid = :patientUuid and o.concept.id = :conceptId and o.action != :orderAction and o.dateStopped is null order by o.dateActivated desc")
		        .setParameter("patientUuid", patientUuid).setParameter("orderAction", Order.Action.DISCONTINUE)
		        .setParameter("conceptId", conceptId).uniqueResult();
		return count > 0;
		
	}
	
}
