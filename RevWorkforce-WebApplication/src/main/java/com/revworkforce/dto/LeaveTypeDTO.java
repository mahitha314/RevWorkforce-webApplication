package com.revworkforce.dto;

public class LeaveTypeDTO {

	private Long id;
	private String typeName;
	private Integer totalDays;

	public LeaveTypeDTO() {
	}

	public LeaveTypeDTO(Long id, String typeName, Integer totalDays) {
		this.id = id;
		this.typeName = typeName;
		this.totalDays = totalDays;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(Integer totalDays) {
		this.totalDays = totalDays;
	}

}