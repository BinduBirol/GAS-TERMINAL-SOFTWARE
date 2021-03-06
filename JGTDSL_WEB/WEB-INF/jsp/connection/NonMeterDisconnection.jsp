<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("nonMeterDisconnectionHome.action");
	setTitle("Disconnection Information(Non-Metered)");
</script>



<div id="customer_meter_div" style="height: 60%;width: 99%;">
	<div id="customer_info" style="float:left; width: 48%;height:100%;">
		<div class="row-fluid" style="height: 52%;">
			<jsp:include page="../common/CustomerInfo.jsp" />
		</div>
   		<div style="height: 40%;width: 100%;margin-top: 10px;">		
   			<form id="disconnInfoForm" name="disconnInfoForm">	
				<jsp:include page="DisconnectionInfo.jsp" />	
			</form>				
		</div>
	</div>

	<div style="width: 51%; height: 99%;float: left;margin-left: 1%;">
		<div class="row-fluid">
			<div class="span12" id="rightSpan">
				
			</div>
		</div>	
	</div>
</div>


<div id="customer_grid_div" style="height: 34%;width: 99%;"> 
<div id="tabbed-nav">
            <ul>
                <li><a>All Customer(Non-Metered)</a></li>
                <li><a>Disconn. History (<font color="green" style="font-weight: bold;">For this customer</font>)</a></li>
                <li><a>Disconn. History (<font color="#E42217" style="font-weight: bold;">All Customer</font>)</a></li>
            </ul>
            <div>
                <div>
					<table id="customer_grid"></table>
					<div id="customer_grid_pager" ></div>
                </div>
                <div>
                    <table id="disconn_history_this_grid"></table>
					<div id="disconn_history_this_grid_pager" ></div>
                </div>
                <div>
                    <table id="disconn_history_all_grid"></table>
					<div id="disconn_history_all_grid_pager" ></div>
                </div>
            </div>

        </div>
</div>


<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/nonMeterDisconnection.js"></script>
