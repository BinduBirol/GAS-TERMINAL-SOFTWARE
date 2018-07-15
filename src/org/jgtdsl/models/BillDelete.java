package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

import com.google.gson.Gson;



public class BillDelete  extends BaseAction{
	
	private static final long serialVersionUID = 3888419044139471548L;	
	private String customer_id;
	private String bill_id;
	private String status;
	private String is_metered;
	

	public String deleteBill() {

		ResponseDTO response = new ResponseDTO();
		response=deleteBillinfo();
		
		setJsonResponse(response);	

		return null;
	}

	public ResponseDTO deleteBillinfo() {
		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();
		PreparedStatement stmt = null;
		
		if(is_metered.equals("1")){
			String mSql1= " Delete BILLING_READING_MAP Where Bill_Id = '"+this.bill_id+"' ";
			String mSql2= " Delete BILL_METERED Where Bill_Id = '"+this.bill_id+"' ";
			String mSql3= " Delete DTL_MARGIN_GOVT Where Bill_Id = '"+this.bill_id+"' ";
			String mSql4= " Delete DTL_MARGIN_PB Where Bill_Id = '"+this.bill_id+"' ";
			String mSql5= " Delete SUMMARY_MARGIN_GOVT Where Bill_Id= '"+this.bill_id+"' ";
			String mSql6= " Delete SUMMARY_MARGIN_PB Where Bill_Id= '"+this.bill_id+"' ";
			String mSql7= " Delete SALES_REPORT Where Bill_Id= '"+this.bill_id+"' ";
			
			try {
				stmt = conn.prepareStatement(mSql1);				
				stmt.executeUpdate();
				
				stmt = conn.prepareStatement(mSql2);				
				stmt.executeUpdate();

				stmt = conn.prepareStatement(mSql3);				
				stmt.executeUpdate();
				
				stmt = conn.prepareStatement(mSql4);				
				stmt.executeUpdate();

				stmt = conn.prepareStatement(mSql5);				
				stmt.executeUpdate();

				stmt = conn.prepareStatement(mSql6);				
				stmt.executeUpdate();

				stmt = conn.prepareStatement(mSql7);				
				stmt.executeUpdate();				

				transactionManager.commit();

				response.setMessasge("Bill-["+bill_id+"] Successfully Deleted");
				response.setResponse(true);

			}

			catch (Exception e) {
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
				try {
					transactionManager.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} finally {
				try {
					stmt.close();
					transactionManager.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				stmt = null;
				conn = null;
			}

			
			
			
		}else{
			String nmSql1= " delete from BILL_NON_METERED where bill_id= '"+this.bill_id+"' ";
			String nmSql2= " delete from BILL_NM_DTL where bill_id '"+this.bill_id+"' ";
			String nmSql3= " delete from SALES_REPORT where bill_id '"+this.bill_id+"' ";
			
			try {
				stmt = conn.prepareStatement(nmSql1);				
				stmt.executeUpdate();
				
				stmt = conn.prepareStatement(nmSql2);				
				stmt.executeUpdate();

				stmt = conn.prepareStatement(nmSql3);				
				stmt.executeUpdate();				
						

				transactionManager.commit();

				response.setMessasge("Bill-["+bill_id+"] Successfully Deleted");
				response.setResponse(true);

			}

			catch (Exception e) {
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
				try {
					transactionManager.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} finally {
				try {
					stmt.close();
					transactionManager.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				stmt = null;
				conn = null;
			}

		}

		return response;
		

	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getBill_id() {
		return bill_id;
	}
		
	public void setBill_id(String bill_id) {
		this.bill_id = bill_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIs_metered() {
		return is_metered;
	}

	public void setIs_metered(String is_metered) {
		this.is_metered = is_metered;
	}
	
	
}
