package org.openmrs.module.digipaths;

import org.openmrs.BaseOpenmrsData;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Department extends BaseOpenmrsData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer departmentId;
	
	private String name;
	
	private String description;
	
	public Department() {
		
	}
	
	public Integer getDepartmentId() {
		return departmentId;
	}
	
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	
	@Override
	public Integer getId() {
		return getDepartmentId();
	}
	
	@Override
	public void setId(Integer id) {
		setDepartmentId(id);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
