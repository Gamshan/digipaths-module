package org.openmrs.module.digipaths.web.resource;

import org.openmrs.Order;
import org.openmrs.Patient;
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
	
	@RequestMapping(value = "/orders-new", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String,Object> getNewOrders(@RequestParam String patientUuid) {
		DepartmentService departmentService = Context.getService(DepartmentService.class);
		Order order = departmentService.getOrder(patientUuid);
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		Map<String,Object> response = new  HashMap<>();
		if (order != null && order.getDateActivated().before(calendar.getTime())){
			response.put("flagged", true);
			response.put("message", "Hba1c result is old");
			return response;
		}
		response.put("flagged", false);
		response.put("message", "Hba1c result is not old");
		return response;
	}
}
