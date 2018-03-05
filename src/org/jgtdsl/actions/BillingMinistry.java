package org.jgtdsl.actions;

import java.util.ArrayList;

import org.jgtdsl.dto.BillingNonMeteredDTO;
import org.jgtdsl.dto.BillingParamDTO;
import org.jgtdsl.dto.BurnerQntChangeDTO;

public class BillingMinistry {

	private static final long serialVersionUID = -4026297230147889320L;
	private BillingParamDTO bill_parameter;
	private String bill_id;
	private String customer_category;
	private String area_id;
	private String customer_id;
	private String billing_month;
	private String billing_year;
	private String bill_for;
	private  ArrayList<BillingNonMeteredDTO> duesList;
	private  ArrayList<BurnerQntChangeDTO> eventList;
	
	public String execute(){
		
		
		return "success";
	}
}
