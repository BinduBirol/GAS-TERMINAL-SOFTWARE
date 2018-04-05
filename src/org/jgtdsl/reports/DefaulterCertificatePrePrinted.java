package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.jgtdsl.dto.ClearnessDTO;
import org.jgtdsl.dto.CustomerApplianceDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.MBillDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.MeterService;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionSupport;

public class DefaulterCertificatePrePrinted extends ActionSupport implements
		ServletContextAware {

	ClearnessDTO clearnessDTO = new ClearnessDTO();
	// ArrayList<ClearnessDTO> dueMonthList=new ArrayList<ClearnessDTO>();
	ClearnessDTO cto = new ClearnessDTO();

	private static final long serialVersionUID = 8854240739341830184L;
	private String customer_id;
	private String download_type;
	private String area;
	private String collection_month;
	private String from_customer_id;
	private String to_customer_id;
	private String customer_category;
	private String customer_type;
	private String calender_year;
	private String officer_name;
	private String officer_desig;
	private String certification_id;
	private String report_type;
	private ServletContext servlet;
	public HttpServletResponse response = ServletActionContext.getResponse();
	public HttpServletRequest request;
	String yearsb;
	ArrayList<ClearnessDTO> CustomerList = new ArrayList<ClearnessDTO>();
	CustomerDTO customer = new CustomerDTO();
	ClearnessDTO customerInfo;
	MeterService ms = new MeterService();
	ArrayList<CustomerApplianceDTO> applianceList = new ArrayList<CustomerApplianceDTO>();
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
	Date date = new Date();

	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	static DecimalFormat consumption_format = new DecimalFormat(
			"##########0.000");

	UserDTO loggedInUser = (UserDTO) ServletActionContext.getRequest()
			.getSession().getAttribute("user");
	Connection conn = ConnectionManager.getConnection();

	// ////////////////////////////////////////////////////////
	public String clearnessCertificateInfoPrePrinted() {
		return null;
	}

	// ///////////////////////////////////////////////////////
	public String clearnessUnderCertificateInfo() {
		String fileName = "clearnessUnderCertificateInfo.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		document.setMargins(0, 0, 20, 50);

		try {

			ReportFormat Event = new ReportFormat(getServletContext());
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			writer.setPageEvent(Event);
			PdfPCell pcell = null;

			document.open();

			PdfPTable mainTable = new PdfPTable(6);
			Rectangle page = document.getPageSize();
			/*
			 * headerTable.setTotalWidth(page.getWidth()); float
			 * a=((page.getWidth()*15)/100)/2; float
			 * b=((page.getWidth()*30)/100)/2;
			 * 
			 * headerTable.setWidths(new float[] { a,b,a });
			 */

			String realPath = servlet
					.getRealPath("/resources/images/logo/JG.png"); // image path
			Image img = Image.getInstance(realPath);

			// img.scaleToFit(10f, 200f);
			// img.scalePercent(200f);
			img.scaleAbsolute(28f, 31f);
			// img.setAbsolutePosition(145f, 780f);
			img.setAbsolutePosition(145f, 787f); // rotate

			document.add(img);

			PdfPTable mTable = new PdfPTable(1);
			// mTable.setWidths(new float[]{b});
			pcell = new PdfPCell(new Paragraph(
					"JALALABAD GAS T & D SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)",
					ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("REGIONAL OFFICE : ", ReportUtil.f8);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer
					.valueOf(getArea()) - 1]), ReportUtil.f8B);
			Paragraph p = new Paragraph();
			p.add(chunk1);
			p.add(chunk2);
			pcell = new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(
					"Subject: Under Certitificate Posting", ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setPaddingBottom(8);
			pcell.setBorder(Rectangle.BOTTOM);
			mTable.addCell(pcell);

			document.add(mTable);

			// //////////headerTable//////////////

			mainTable.setWidths(new float[] { 10, 20, 41, 7, 7, 25 });

			pcell = new PdfPCell(new Paragraph(" ", ReportUtil.f8B));
			pcell.setColspan(6);
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Sl No.", ReportUtil.f8B));
			//pcell.setRowspan(2);
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Customer ID", ReportUtil.f8B));
			//pcell.setRowspan(2);
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Customer Name", ReportUtil.f8B));
			//pcell.setRowspan(2);
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("S", ReportUtil.f8B));
			pcell.setMinimumHeight(18f);
			//pcell.setRowspan(2);
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("D", ReportUtil.f8B));
			pcell.setMinimumHeight(18f);
			//pcell.setRowspan(2);
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Stamp", ReportUtil.f8B));
			//pcell.setRowspan(2);
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainTable.addCell(pcell);

			// /
			int w = 0;
			CustomerList = getCustomerList(from_customer_id, to_customer_id,
					customer_category, area);
			for (int i = 0; i < CustomerList.size(); i++) {

				w = i;

				pcell = new PdfPCell(new Paragraph(String.valueOf(i + 1),
						ReportUtil.f8));
				// pcell.setRowspan(1);
				pcell.setPadding(5);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				mainTable.addCell(pcell);

				pcell = new PdfPCell(new Paragraph(CustomerList.get(i)
						.getCustomerID(), ReportUtil.f8));
				// pcell.setRowspan(1);
				pcell.setPadding(5);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				mainTable.addCell(pcell);

				pcell = new PdfPCell(new Paragraph(CustomerList.get(i)
						.getCustomerName(), ReportUtil.f8));
				// pcell.setRowspan(1);
				pcell.setPadding(5);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				mainTable.addCell(pcell);

				pcell = new PdfPCell(new Paragraph(String.valueOf((CustomerList
						.get(i).getSingle_burner() == 0) ? "0" : CustomerList
						.get(i).getSingle_burner()), ReportUtil.f8));

				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				mainTable.addCell(pcell);

				pcell = new PdfPCell(new Paragraph(String.valueOf((CustomerList
						.get(i).getDouble_burner() == 0 ? "0" : CustomerList
						.get(i).getDouble_burner())), ReportUtil.f8));

				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				mainTable.addCell(pcell);

				if (w % 3 == 0) {
					pcell = new PdfPCell(new Paragraph("", ReportUtil.f8));

					pcell.setRowspan(3);
					// pcell.setRowspan(3);
					pcell.setPadding(5);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					mainTable.addCell(pcell);
				}

			}
			mainTable.setHeaderRows(2);
			document.add(mainTable);

			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(), fileName);
			document = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}

	public ClearnessDTO getClearnessDTO() {
		return clearnessDTO;
	}

	public void setClearnessDTO(ClearnessDTO clearnessDTO) {
		this.clearnessDTO = clearnessDTO;
	}

	public ClearnessDTO getCto() {
		return cto;
	}

	public void setCto(ClearnessDTO cto) {
		this.cto = cto;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getDownload_type() {
		return download_type;
	}

	public void setDownload_type(String download_type) {
		this.download_type = download_type;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCollection_month() {
		return collection_month;
	}

	public void setCollection_month(String collection_month) {
		this.collection_month = collection_month;
	}

	public String getFrom_customer_id() {
		return from_customer_id;
	}

	public void setFrom_customer_id(String from_customer_id) {
		this.from_customer_id = from_customer_id;
	}

	public String getTo_customer_id() {
		return to_customer_id;
	}

	public void setTo_customer_id(String to_customer_id) {
		this.to_customer_id = to_customer_id;
	}

	public String getCustomer_category() {
		return customer_category;
	}

	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}

	public String getCustomer_type() {
		return customer_type;
	}

	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}

	public String getCalender_year() {
		return calender_year;
	}

	public void setCalender_year(String calender_year) {
		this.calender_year = calender_year;
	}

	public String getOfficer_name() {
		return officer_name;
	}

	public void setOfficer_name(String officer_name) {
		this.officer_name = officer_name;
	}

	public String getOfficer_desig() {
		return officer_desig;
	}

	public void setOfficer_desig(String officer_desig) {
		this.officer_desig = officer_desig;
	}

	public String getCertification_id() {
		return certification_id;
	}

	public void setCertification_id(String certification_id) {
		this.certification_id = certification_id;
	}

	public String getReport_type() {
		return report_type;
	}

	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}

	public ServletContext getServlet() {
		return servlet;
	}

	public void setServlet(ServletContext servlet) {
		this.servlet = servlet;
	}

	public String getYearsb() {
		return yearsb;
	}

	public void setYearsb(String yearsb) {
		this.yearsb = yearsb;
	}

	public ArrayList<ClearnessDTO> getCustomerList() {
		return CustomerList;
	}

	public void setCustomerList(ArrayList<ClearnessDTO> customerList) {
		CustomerList = customerList;
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public ClearnessDTO getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(ClearnessDTO customerInfo) {
		this.customerInfo = customerInfo;
	}

	public MeterService getMs() {
		return ms;
	}

	public void setMs(MeterService ms) {
		this.ms = ms;
	}

	public ArrayList<CustomerApplianceDTO> getApplianceList() {
		return applianceList;
	}

	public void setApplianceList(ArrayList<CustomerApplianceDTO> applianceList) {
		this.applianceList = applianceList;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public static DecimalFormat getTaka_format() {
		return taka_format;
	}

	public static void setTaka_format(DecimalFormat taka_format) {
		DefaulterCertificatePrePrinted.taka_format = taka_format;
	}

	public static DecimalFormat getConsumption_format() {
		return consumption_format;
	}

	public static void setConsumption_format(DecimalFormat consumption_format) {
		DefaulterCertificatePrePrinted.consumption_format = consumption_format;
	}

	public UserDTO getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(UserDTO loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	private ArrayList<ClearnessDTO> getCustomerList(String from_cus_id,
			String to_cus_id, String cust_cat_id, String area) {

		ArrayList<ClearnessDTO> custList = new ArrayList<ClearnessDTO>();
		if (collection_month.length() < 2) {
			collection_month = "0" + collection_month;
		}
		String type = null;
		if (from_cus_id.isEmpty()) {
			type = area + this.customer_category;
		} else {
			type = from_cus_id.substring(0, 4);
		}
		String bill_table;
		if (type.equalsIgnoreCase(area + "01")
				|| type.equalsIgnoreCase(area + "09")) {
			bill_table = "BILL_NON_METERED";
		} else {
			bill_table = "BILL_METERED";
		}
		String whereClause = null;
		if (from_cus_id.isEmpty() && to_cus_id.isEmpty()) {
			whereClause = "      AND BI.CUSTOMER_CATEGORY='"
					+ this.customer_category + "'  ";
		} else {
			whereClause = "         AND BI.CUSTOMER_ID BETWEEN '" + from_cus_id
					+ "' AND '" + to_cus_id + "' ";
		}
		try {
			String transaction_sql = "  SELECT bi.CUSTOMER_ID, getBurner (bi.CUSTOMER_ID) BURNER, BI.CUSTOMER_NAME, COUNT (*) cnt "
					+ "    FROM "
					+ bill_table
					+ " bi, CUSTOMER_CONNECTION cc "
					+ "   WHERE     BI.CUSTOMER_ID = CC.CUSTOMER_ID "
					+ "         AND CC.STATUS = 1 "
					+
					// "         AND bi.STATUS = 1 " +
					"         AND bi.area_id = '"
					+ area
					+ "' "
					+ whereClause
					+ "                 AND BILL_YEAR || LPAD (BILL_MONTH, 2, 0) <= '"
					+ calender_year
					+ collection_month
					+ "'  GROUP BY BI.CUSTOMER_ID, BI.CUSTOMER_NAME, CUSTOMER_CATEGORY, bi.AREA_ID "
					+ "  HAVING COUNT (*) >= 1 ";

			PreparedStatement ps1 = conn.prepareStatement(transaction_sql);
			ResultSet resultSet = ps1.executeQuery();
			while (resultSet.next()) {
				ClearnessDTO ClearnessDTO = new ClearnessDTO();
				ClearnessDTO.setCustomerID(resultSet.getString("CUSTOMER_ID"));
				ClearnessDTO.setCustomerName(resultSet
						.getString("CUSTOMER_NAME"));
				String burner = resultSet.getString("BURNER");
				String[] brnrArray = burner.split("#");
				ClearnessDTO.setSingle_burner(Integer.parseInt(brnrArray[0]));
				ClearnessDTO.setDouble_burner(Integer.parseInt(brnrArray[1]));
				custList.add(ClearnessDTO);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return custList;

	}
}
