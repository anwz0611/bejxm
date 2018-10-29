package com.zyinfo.brj.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class PhoneItemInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Expose
	private String Mobile;
	
	@Expose
	private String sortLetters;
	
	@Expose
	private String Name;
	
	@Expose
	private String OfficeTel;
	
	@Expose
	private String PersonCD;

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getOfficeTel() {
		return OfficeTel;
	}

	public void setOfficeTel(String officeTel) {
		OfficeTel = officeTel;
	}

	public String getPersonCD() {
		return PersonCD;
	}

	public void setPersonCD(String personCD) {
		PersonCD = personCD;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

}
