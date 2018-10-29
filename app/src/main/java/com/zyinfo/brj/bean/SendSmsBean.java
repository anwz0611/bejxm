package com.zyinfo.brj.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
* @Description:
* @author:杨玉萍
* @date:2016年6月28日上午9:35:58
*/
public class SendSmsBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Expose
	private String PhoneNum;//电话号码
	
	@Expose
	private String Status;//状态

	public String getPhoneNum() {
		return PhoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		PhoneNum = phoneNum;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

}
