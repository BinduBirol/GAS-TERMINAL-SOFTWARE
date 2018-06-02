<%@ taglib prefix="s" uri="/struts-tags"%>
	
<script  type="text/javascript">
	setTitle("Ministry Collection Report");
</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div class="row-fluid" style="width: 80%;height: 50%;">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
					<h4 id="rightSpan_caption">Ministry Collection Details</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
					<form action="ministryCollectionDetails.action">
					
					
						
						
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Region/Area</label>
								<select id="area_id"  style="width: 56%;"  name="area_id">
									<option value="" selected="selected">Select Area</option>
									<s:iterator value="%{#session.USER_AREA_LIST}" id="areaList">
										<option selected="selected" value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
								</s:iterator>
								</select>									      
							</div>
							<div class="span6">
								<label style="width: 40%">Ministry<m class='man'/></label>
								<select name="ministry_id" id="MINISTRY_ID" style="width: 56%;">
							       	<option value="all">All Ministry</option>
							       	<s:iterator  value="%{#application.MINISTRY_CUSTOMER_CATEGORY}" id="MINISTRY_CAT">
							            <option value="<s:property value="Ministry_id"/>"><s:property value="Ministry_name" /></option>
									</s:iterator>
						       </select>     
							</div>  
						</div>
						
						
						
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Billing Month<m class='man'/></label>
								<select name="bill_month" id="billing_month" style="width: 56%;margin-left: 0px;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span6">
								<label style="width: 40%">Billing Year<m class='man'/></label>
								<select name="bill_year" id="billing_year" style="width: 56%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>  
						</div>
						
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">		
						   <table width="100%">
						   	<tr>
						   		
						   		<td style="width: 70%" align="right">			   			  
						   			     
						   			 <button class="btn" type="submit">Generate Report</button>	
									 <button class="btn btn-danger"  type="button" id="btn_cancel" onclick="callAction('blankPage.action')">Cancel</button>
						   		</td>
						   	</tr>
						   </table>								    
						   									
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
<script  type="text/javascript">
if($("#area_wise").attr('checked', 'checked')){
	//$("#MINISTRY_ID").attr("disabled","disabled");
}else if($("#by_category").attr('checked', 'checked')){
	$("#MINISTRY_ID").removeAttr("disabled");
}
;
</script>
</body>
</html>