<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
	navCache("editSurcharge.action");
	setTitle("Edit Surcharge");
</script>
<link href="/JGTDSL_WEB/resources/css/page/meterReading.css"
	rel="stylesheet" type="text/css" />
<style>
input[type="radio"],input[type="checkbox"] {
	margin-top: -3px !important;
}

.alert {
	padding-top: 4px !important;
	padding-bottom: 4px !important;
}

.ui-icon,.ui-widget-content .ui-icon {
	cursor: pointer;
}

.sFont {
	font-size: 12px;
}
</style>
<div class="meter-reading" style="width: 100%;height: 50%;">
	<div class="row-fluid">
		<div class="span9" id="rightSpan">
			<div class="w-box-header">
				<h4 id="rightSpan_caption">Edit Surcharge</h4>
			</div>
			<div class="w-box-content" style="padding: 10px;" id="content_div">
				<div class="row-fluid">
					<div class="row-fluid">
						<div class="span9">
							<label style="width: 40%">Customer ID</label> <input type="text"
								name="customer_id" id="customer_id" style="width: 54.5%" />
						</div>
					</div>
					<div class="row-fluid">
						<div class="span9">
							<label style="width: 40%">Year</label> <input type="text"
								name="year" id="year" style="width: 54.5%" />
						</div>
					</div>
					<div class="row-fluid">
						<div class="span9">
							<label style="width: 40%">Month</label> <select name="month"
								id="month" style="width: 56%;margin-left: 0px;">
								<option value="">Select Month</option>
								<s:iterator value="%{#application.MONTHS}">
									<option value="<s:property value='id'/>">
										<s:property value="label" />
									</option>
								</s:iterator>
							</select>
						</div>
					</div>

				</div>

				<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
					<div id="aDiv" style="height: 0px;"></div>
				</div>
				<div class="formSep sepH_b"
					style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">
					<table width="100%">
						<tr>

							<td style="width: 60%" align="right">
								<button class="btn" id="btn_save"
									onclick="fetchWrongCollectionList()">Search</button>
								<button class="btn btn-danger" type="button" id="btn_cancel"
									onclick="callAction('blankPage.action')">Cancel</button>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div class="row-fluid" >
		<div class="span12" id="rightSpan">
			<div class="w-box-content" id="content_div">
				<form id="formForUpdateSurchargeNM" name="formForUpdateSurchargeNM">
					<table border="10" style="background-color:D1E9EF;">
						<TR>
							<TH rowspan="2" >Bill ID</TH>
							<TH rowspan="2">Description</TH>
							<TH rowspan="2">Billed Amount</TH>
							<TH rowspan="2">Surcharge Amount</TH>
							<TH rowspan="2">Collected Billed Amount</TH>
							<TH rowspan="2">Collected Surcharge</TH>
							<TH rowspan="2">Due Date</TH>
							<TH rowspan="2">Bank, Branch</TH>
							<TH rowspan="2">Collection Date</TH>

						</TR>
						<TR></TR>
						<TR>
							<TD><INPUT TYPE="TEXT" NAME="entry_type" id="entry_type"
								style="width: 90%"></TD>
							<TD><INPUT TYPE="TEXT" NAME="particulars" id="particulars"
								style="width: 90%"></TD>
							<TD><INPUT TYPE="TEXT" NAME="sales_amount" id="sales_amount"
								style="width: 90%"></TD>
							<TD><INPUT TYPE="TEXT" NAME="surcharge" id="surcharge"
								style="width: 90%"></TD>
							<TD><INPUT TYPE="TEXT" NAME="credit_amount" id="credit_amount"
								style="width: 90%"></TD>
							<TD><INPUT TYPE="TEXT" NAME="credit_surcharge" id="credit_surcharge"
								style="width: 90%"></TD>
							<TD><INPUT TYPE="TEXT" NAME="due_date" id="due_date"
								style="width: 90%"></TD>
							<TD><INPUT TYPE="TEXT" NAME="bank_name" id="bank_name"
								style="width: 90%"></TD>
							<TD><INPUT TYPE="TEXT" NAME="issue_paid_date" id="issue_paid_date"
								style="width: 90%"></TD>

						</TR>
						
					</table>
					<P>
						<INPUT  class="btn btn-primary" TYPE="SUBMIT" VALUE="Update" NAME="B1">
					</P>
				</FORM>
			</div>
		</div>
	</div>

</div>


<p style="clear: both;margin-top: 5px;"></p>

<script type="text/javascript">
	function fetchWrongCollectionList() {

		if ($("#customer_id").val() == "") {
			alert("Please enter customer id");
			return;
		}

		if ($("#month").val() == "") {
			alert("Please enter a valid month");
			return;
		}

		if ($("#year").val() == "") {
			alert("Please enter a valid year");
			return;
		}

		$("#detailDiv").html("");
		$("#stat_div").show();
		$("#loading_div")
				.html(
						jsImg.LOADING_MID
								+ "<br/><br/><font style='color:white;font-weight:bold'>Please wait. Searching the Collection list </font>");

		$.ajax({
			type : "POST",
			url : "getNMLedgerByMonthYear",
			dataType : 'text',
			async : false,
			data : {
				customer_id : $("#customer_id").val(),
				year : $("#year").val(),
				month : $("#month").val()
			}
		}).done(function(msg) {
		    var info = JSON.parse(msg);
			alert(info[0]["customer_id"]);
			$("#entry_type").val(info[0]["entry_type"]);
			$("#entry_type").val(info[0]["entry_type"]);
			$("#entry_type").val(info[0]["entry_type"]);
			$("#entry_type").val(info[0]["entry_type"]);
			$("#entry_type").val(info[0]["entry_type"]);
			$("#entry_type").val(info[0]["entry_type"]);
			$("#entry_type").val(info[0]["entry_type"]);
			$("#entry_type").val(info[0]["entry_type"]);
			$("#entry_type").val(info[0]["entry_type"]);
			
			
			
			
			
			$("#stat_div").hide();
			//$("#detailDiv").html(msg);
		}).always(function() {

		}).fail(function(data) {
			if (data.responseCode)
				alert(data.responseCode);
		});

	}
</script>
