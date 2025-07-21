package org.openmrs.module.digipaths.web.resource;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ConditionService;
import org.openmrs.api.OrderService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.digipaths.Department;
import org.openmrs.module.digipaths.api.DepartmentService;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
@RequestMapping("/rest/v1/digipaths")
public class RestController extends MainResourceController {
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController#getNamespace()
	 */
	@Override
	public String getNamespace() {
		return "v1/digipaths";
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Map<String, String>> getDigipath() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("date", String.valueOf(new Date()));
		hm.put("action", "Hello World");
		Map<String, String> hm2 = new HashMap<String, String>();
		hm2.put("date", String.valueOf(new Date()));
		hm2.put("action", "Second action");
		list.add(hm);
		list.add(hm2);
		return list;
		//		return "HELLO WORLD";
	}
	
	@RequestMapping(value = "/departments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Department> getDepartments() {
		DepartmentService departmentService = Context.getService(DepartmentService.class);
		return departmentService.getAllDepartments();
		
		//		List<Department> newList = new ArrayList<Department>();
		//		return newList;
	}
	
	@RequestMapping(value = "/department", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String createDepartment() {
		DepartmentService departmentService = Context.getService(DepartmentService.class);
		Department department = new Department();
		department.setName("ABC");
		department.setDescription("test");
		departmentService.saveDepartment(department);
		return "Success";
	}
	
	@RequestMapping(value = "/orders-test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getOrders(@RequestParam String patientId) {
		PatientService patientService = Context.getService(PatientService.class);
		OrderService orderService = Context.getService(OrderService.class);
		Patient patient = patientService.getPatientByUuid(patientId);
		List<Order>	orderList =	orderService.getAllOrdersByPatient(patient);
		StringBuilder str = new StringBuilder("Hello ");
		orderList.forEach(order -> {
			str.append(order.getDateActivated().toString());
		});

		
		// Retrieve all patients
		//		List<Patient> patients = patientService.();
		
		return str.toString();
		//		MyOrderService orderService = Context.getService(MyOrderService.class);
		//		return orderService.getAllPatients();
		//		return "HELLO";
	}
	
	//	@RequestMapping(value = "/orders-new", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	//	@ResponseStatus(HttpStatus.OK)
	//	@ResponseBody
	//	public Map<String,Object> getNewOrders(@RequestParam String patientUuid) {
	//		DepartmentService departmentService = Context.getService(DepartmentService.class);
	//		Order order = departmentService.getOrder(patientUuid);
	//
	//		Calendar calendar = Calendar.getInstance();
	//		calendar.add(Calendar.YEAR, -1);
	//		Map<String,Object> response = new  HashMap<>();
	//		if (order != null && order.getDateActivated().before(calendar.getTime())){
	//			response.put("flagged", true);
	//			response.put("message", "Hba1c result is old");
	//			return response;
	//		}
	//		response.put("flagged", false);
	//		response.put("message", "Hba1c result is not old");
	//		return response;
	//	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Map<String,Object>> getDigipathsList(@RequestParam String patientUuid, HttpServletResponse response) {
		DepartmentService departmentService = Context.getService(DepartmentService.class);
		List<Map<String,Object>> list = new ArrayList<>();
		getHemoglobinAction(departmentService,patientUuid,list);
		getAlbuminRatioAction(departmentService,patientUuid,list);
		getSerumCreatinineAction(departmentService,patientUuid,list);
		return list;
	}
	
	public void getHemoglobinAction(DepartmentService departmentService, String patientUuid, List<Map<String,Object>> list) {
		String hemoglobinUUID = "159644AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		Order order = departmentService.getOrderByPatientUuidAndConceptUuid(patientUuid, hemoglobinUUID);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);

		Map<String,Object> map = new  HashMap<>();

		if (order != null && order.getDateActivated().before(calendar.getTime()))
			list.add(getDigipathsRow(order.getDateActivated(),"HbA1c Result is Outdated","<p>The patient’s last <b>HbA1c</b> test was performed over 12 months ago. Consider ordering a new test to assess current glycemic control.</p>"));
	}
	
	public void getAlbuminRatioAction(DepartmentService departmentService, String patientUuid, List<Map<String, Object>> list) {
		String albuminUUID = "164948AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		String TBD2_Name = "Diabetes mellitus, type 2";
		String ACE_Inhibitor_Name = "ACE inhibitors";
		
		ConceptService conceptService = Context.getService(ConceptService.class);
		Concept concept = conceptService.getConceptByName(TBD2_Name);
		Concept aceInhibitorConcept = conceptService.getConceptByName(ACE_Inhibitor_Name);
		
		if (concept != null) {
			boolean existTBD2 = departmentService.getConditionByPatientUuidAndConceptUuid(patientUuid, concept.getUuid());
			boolean existAceInhibitor = aceInhibitorConcept != null
			        && departmentService.getConditionByPatientUuidAndConceptUuid(patientUuid, aceInhibitorConcept.getUuid());
			
			if (existTBD2 && !existAceInhibitor) {
				List<Obs> obsList = departmentService.getObsByPatientUuidAndConceptUuid(patientUuid, albuminUUID, 1);
				if (obsList != null && !obsList.isEmpty() && obsList.get(0) != null && obsList.get(0).getValueNumeric() > 3) {
					list.add(getDigipathsRow(
					    obsList.get(0).getObsDatetime(),
					    "Elevated ACR detected in T2DM patient without ACEI",
					    "The patient with Type 2 Diabetes Mellitus has an elevated urine Albumin-Creatinine Ratio (ACR > 3 mg/mmol), suggesting early signs of diabetic kidney disease.\n"
					            + "Initiating an ACE inhibitor is recommended to reduce progression of renal damage and provide cardiovascular protection."));
				}
			}
		}
	}
	
	public void getSerumCreatinineAction(DepartmentService departmentService, String patientUuid,
	        List<Map<String, Object>> list) {
		String serumCreatinine = "790AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		String ckdUuid = "145438AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		
		boolean existCKD = departmentService.getConditionByPatientUuidAndConceptUuid(patientUuid, ckdUuid);
		
		List<Obs> obsList = departmentService.getObsByPatientUuidAndConceptUuid(patientUuid, serumCreatinine, 5);
		PatientService patientService = Context.getService(PatientService.class);
		Patient patient = patientService.getPatientByUuid(patientUuid);
		handleKidneyDisease(patient, obsList, list, existCKD);
	}
	
	public void handleKidneyDisease(Patient patient, List<Obs> obsList, List<Map<String,Object>> list, boolean existCKD) {
		boolean messageAdded = false;
		outerLoop:
		for (Obs o1 : obsList) {
			for (Obs o2 : obsList) {
				if (o1 == o2)
					continue;
				long diffMillis = Math.abs(o1.getObsDatetime().getTime() - o2.getObsDatetime().getTime());
				long diffDays = diffMillis / (1000 * 60 * 60 * 24);
				if (diffDays < 90 && patient != null && patient.getGender() != null && patient.getAge() != null) {
					Double o1GFR = calculateGFR(patient.getGender(), patient.getAge(), o1.getValueNumeric());
					Double o2GFR = calculateGFR(patient.getGender(), patient.getAge(), o2.getValueNumeric());
					Map<String, Object> map = new HashMap<>();
					String messageLessThan45 = "Reduced kidney function detected (GFR < 45)";
					String messageLessThan60 = "GFR <60 on two occasions three months apart, suspect Chronic Kidney Disease (CKD).";
					String recommendationLessThan45 = "<p>If the GFR is below 45 mL/min/1.73m², the severity warrants timely referral to a nephrologist for specialized evaluation and management to slow disease progression and address complications.</p>";
					String recommendationLessThan60 = "<p>The patient has had two separate serum creatinine tests, spaced at least three months apart, both indicating estimated GFR values below 60 mL/min/1.73m².</br> This persistent reduction in kidney function suggests the presence of Chronic Kidney Disease (CKD), as per clinical guidelines.</p>";

					if(o1GFR < 45) {
						list.add(getDigipathsRow(o1.getObsDatetime(), messageLessThan45, "<h6> GFR: " + String.format("%.1f", o1GFR)  + " mL/min/1.73m² </h6>" + recommendationLessThan45));
						 messageAdded = true;
					} else if(o2GFR < 45) {
						list.add(getDigipathsRow(o2.getObsDatetime(), messageLessThan45, "<h6> GFR: " + String.format("%.1f", o2GFR) + " mL/min/1.73m² </h6>" + recommendationLessThan45));
						messageAdded = true;
					}

					if(!existCKD && o1GFR < 60){
						list.add(getDigipathsRow(o1.getObsDatetime(), messageLessThan60, "<h6> GFR: " + String.format("%.1f",  o1GFR) + " mL/min/1.73m² </h6>" + recommendationLessThan60));
						messageAdded = true;
					} else if(!existCKD && o2GFR < 60) {
						list.add(getDigipathsRow(o2.getObsDatetime(), messageLessThan60,"<h6> GFR: " + String.format("%.1f", o2GFR) + " mL/min/1.73m² </h6>" +  recommendationLessThan60));
						messageAdded = true;
					}
				}
				if(messageAdded) {
					break outerLoop;
				}
			}
		}
	}
	
	public Map<String, Object> getDigipathsRow(Date date, String message, String recommendation){
		Map<String, Object> map = new HashMap<>();
		map.put("date", date.toString());
		map.put("message",message);
		map.put("recommendation",recommendation);

		map.put("description",recommendation);
		map.put("title","message");
		return map;
	}
	
	public Double calculateGFR(String gender, Integer ageYears, Double serumCreatinineValue) {
		
		return (Objects.equals(gender, "F") ?
		
		142 * Math.pow(Math.min(serumCreatinineValue / 0.7, 1), -0.241)
		        * Math.pow(Math.max(serumCreatinineValue / 0.7, 1), -1.200) * Math.pow(0.9938, ageYears) * 1.012 : 142
		        * Math.pow(Math.min(serumCreatinineValue / 0.9, 1), -0.302)
		        * Math.pow(Math.max(serumCreatinineValue / 0.9, 1), -1.200) * Math.pow(0.9938, ageYears));
	}
}
