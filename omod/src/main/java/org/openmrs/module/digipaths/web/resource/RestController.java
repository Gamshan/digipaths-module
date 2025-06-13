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
		String hemoglobinUUID = "21AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		Order order = departmentService.getOrderByPatientUuidAndConceptUuid(patientUuid, hemoglobinUUID);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);

		Map<String,Object> map = new  HashMap<>();

		if (order != null && order.getDateActivated().before(calendar.getTime())){
			map.put("date", order.getDateActivated().toString());
			map.put("title", "Hba1c result is old");
			map.put("description","The HBa1C is one year old, Please repeat the test again");
			list.add(map);
		}else if(order != null && order.getDateActivated() != null){
			map.put("date", order.getDateActivated().toString());
			map.put("title", "Valid Hba1c result");
			map.put("description","The HBa1C result is valid");
			list.add(map);
		}

	}
	
	public void getAlbuminRatioAction(DepartmentService departmentService, String patientUuid, List<Map<String,Object>> list) {
		String albuminUUID = "164948AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		String TBD2_Name = "Diabetes mellitus, type 2";

		ConceptService conceptService = Context.getService(ConceptService.class);
		Concept concept = conceptService.getConceptByName(TBD2_Name);

		Map<String, Object> map = new HashMap<>();
		if(concept != null) {
			boolean existTBD2 = departmentService.getConditionByPatientUuidAndConceptId(patientUuid, concept.getConceptId());
			if (existTBD2) {
				List<Obs> obsList = departmentService.getObsByPatientUuidAndConceptUuid(patientUuid, albuminUUID, 1);
				if(obsList != null &&  obsList.get(0) != null && obsList.get(0).getValueNumeric() > 3) {
					map.put("date", obsList.get(0).getObsDatetime().toString());
					map.put("title", "initiate ACEI");
					map.put("description", "initiate an ACE inhibitor.");
					list.add(map);
				}
			}
		}
	}
	
	public void getSerumCreatinineAction(DepartmentService departmentService, String patientUuid, List<Map<String,Object>> list){
		String serumCreatinine = "790AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		List<Obs> obsList = departmentService.getObsByPatientUuidAndConceptUuid(patientUuid, serumCreatinine,5);
		PatientService patientService = Context.getService(PatientService.class);
		Patient patient = patientService.getPatientByUuid(patientUuid);

		Map<String, Object> map = new HashMap<>();
		handleKidneyDisease(patient,obsList,list);

	}
	
	public void handleKidneyDisease(Patient patient, List<Obs> obsList, List<Map<String,Object>> list) {
		for (Obs o1 : obsList) {
			for (Obs o2 : obsList) {
				if (o1 == o2)
					continue;
				long diffMillis = Math.abs(o1.getObsDatetime().getTime() - o2.getObsDatetime().getTime());
				long diffDays = diffMillis / (1000 * 60 * 60 * 24);
				if (diffDays < 90) {
					Double o1GFR = calculateGFR(patient.getGender(), patient.getAge(), o1.getValueNumeric());
					Double o2GFR = calculateGFR(patient.getGender(), patient.getAge(), o2.getValueNumeric());
					Map<String, Object> map = new HashMap<>();
					String alert1 = "Low GFR - consider referral to a kidney specialist";
					String alert2 = "Consider diagnosis of Chronic Kidney disease";
					String title = "Chronic Kidney Disease" + "("+ String.format("%.2f", o1GFR) + ","+  String.format("%.2f", o2GFR) + ")";
					if(o1GFR < 45) {
						list.add(getDigipathsRow(o1.getObsDatetime(), title, alert1));
						return;
					}
					else if(o2GFR < 45) {
						list.add(getDigipathsRow(o2.getObsDatetime(), title, alert1));
						return;
					}
					else if(o1GFR < 60){
						list.add(getDigipathsRow(o1.getObsDatetime(), title, alert2));
						return;
					}
					else if(o2GFR < 60) {
						list.add(getDigipathsRow(o2.getObsDatetime(), title, alert2));
						return;
					}
				}
			}
		}
	}
	
	public Map<String, Object> getDigipathsRow(Date date, String title, String description){
		Map<String, Object> map = new HashMap<>();
		map.put("date", date.toString());
		map.put("title",title);
		map.put("description",description);
		return map;
	}
	
	public Double calculateGFR(String gender, Integer agaYears, Double serumCreatinineValue) {
		
		return (Objects.equals(gender, "F") ?
		
		142 * Math.pow(Math.min(serumCreatinineValue / 0.7, 1), -0.241)
		        * Math.pow(Math.max(serumCreatinineValue / 0.7, 1), -1.200) * Math.pow(0.9938, agaYears) * 1.012 : 142
		        * Math.pow(Math.min(serumCreatinineValue / 0.9, 1), -0.302)
		        * Math.pow(Math.max(serumCreatinineValue / 0.9, 1), -1.200) * Math.pow(0.9938, agaYears));
	}
}
