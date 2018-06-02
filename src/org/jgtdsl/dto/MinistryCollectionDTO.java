package org.jgtdsl.dto;

import com.google.gson.Gson;

public class MinistryCollectionDTO {

	private String customer_id;
	private String customer_category_name;
	private String ministry_id;
	private String ministry_name;
	private String payable_amount;
	private String collected_amount;
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_category_name() {
		return customer_category_name;
	}
	public void setCustomer_category_name(String customer_category_name) {
		this.customer_category_name = customer_category_name;
	}
	public String getMinistry_id() {
		return ministry_id;
	}
	public void setMinistry_id(String ministry_id) {
		this.ministry_id = ministry_id;
	}
	public String getMinistry_name() {
		return ministry_name;
	}
	public void setMinistry_name(String ministry_name) {
		this.ministry_name = ministry_name;
	}
	public String getPayable_amount() {
		return payable_amount;
	}
	public void setPayable_amount(String payable_amount) {
		this.payable_amount = payable_amount;
	}
	public String getCollected_amount() {
		return collected_amount;
	}
	public void setCollected_amount(String collected_amount) {
		this.collected_amount = collected_amount;
	}
	
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
