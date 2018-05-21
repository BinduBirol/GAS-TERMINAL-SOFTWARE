package org.jgtdsl.dto.ipg;

import java.util.ArrayList;

import com.google.gson.Gson;

public class PreviewTotalDTO {
	private Double total_consumption;
	private String total_gas_bill;
	private String total_total_bill;
	private String total_billing_category;
	private String total_customer_count;
	
	
	public String getTotal_customer_count() {
		return total_customer_count;
	}
	public void setTotal_customer_count(String total_customer_count) {
		this.total_customer_count = total_customer_count;
	}
	public Double getTotal_consumption() {
		return total_consumption;
	}
	public void setTotal_consumption(Double total_consumption) {
		this.total_consumption = total_consumption;
	}
	public String getTotal_gas_bill() {
		return total_gas_bill;
	}
	public void setTotal_gas_bill(String total_gas_bill) {
		this.total_gas_bill = total_gas_bill;
	}
	public String getTotal_total_bill() {
		return total_total_bill;
	}
	public void setTotal_total_bill(String total_total_bill) {
		this.total_total_bill = total_total_bill;
	}
	public String getTotal_billing_category() {
		return total_billing_category;
	}
	public void setTotal_billing_category(String total_billing_category) {
		this.total_billing_category = total_billing_category;
	}
	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}
