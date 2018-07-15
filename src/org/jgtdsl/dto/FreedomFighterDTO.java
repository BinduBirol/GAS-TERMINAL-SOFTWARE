package org.jgtdsl.dto;

import com.google.gson.Gson;

public class FreedomFighterDTO {

	private String customer_id;
	private String customer_name;
	private String customer_category;
	private String proprietor_name;
	private String father_name;
	private int singleBurner;
	private int doubleBurner;
	private int serial;

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getCustomer_category() {
		return customer_category;
	}

	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}

	public String getProprietor_name() {
		return proprietor_name;
	}

	public void setProprietor_name(String proprietor_name) {
		this.proprietor_name = proprietor_name;
	}

	public String getFather_name() {
		return father_name;
	}

	public void setFather_name(String father_name) {
		this.father_name = father_name;
	}	

	public int getSingleBurner() {
		return singleBurner;
	}

	public void setSingleBurner(int singleBurner) {
		this.singleBurner = singleBurner;
	}

	public int getDoubleBurner() {
		return doubleBurner;
	}

	public void setDoubleBurner(int doubleBurner) {
		this.doubleBurner = doubleBurner;
	}

	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
