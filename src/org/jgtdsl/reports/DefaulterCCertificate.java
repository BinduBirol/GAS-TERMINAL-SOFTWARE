package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.jgtdsl.dto.ClearnessDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionSupport;

public class DefaulterCCertificate extends ActionSupport implements
		ServletContextAware {
	ClearnessDTO clearnessDTO = new ClearnessDTO();
	// ArrayList<ClearnessDTO> dueMonthList=new ArrayList<ClearnessDTO>();
	ClearnessDTO cto = new ClearnessDTO();

	private static final long serialVersionUID = 8854240739341830184L;
	private String customer_id;
	private String download_type;
	private String area;
	private String from_date;
	private String from_customer_id;
	private String to_customer_id;
	private String customer_category;
	private String customer_type;
	private String calender_year;
	private String officer_name;
	private String officer_desig;
	private ServletContext servlet;

	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	static DecimalFormat consumption_format = new DecimalFormat(
			"##########0.000");

	UserDTO loggedInUser = (UserDTO) ServletActionContext.getRequest()
			.getSession().getAttribute("user");
	Connection conn = ConnectionManager.getConnection();

	public String execute() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();

		PdfReader reader = null;
		ByteArrayOutputStream certificate = null;
		List<PdfReader> readers = null;
		String realPathC = "";
		String realPathD = "";
		String picPath = "";
		Document document = new Document();
		ByteArrayOutputStream out = null;
		document.setPageSize(PageSize.A4);
		document.setMargins(10, 10, 10, 10);
		// left,right,top,bottom
		String fileName = "";
		readers = new ArrayList<PdfReader>();

		try {

			picPath = servlet.getRealPath("/resources/images/logo.png");
			realPathC = servlet
					.getRealPath("/resources/staticPdf/CertificateC.pdf");
			realPathD = servlet
					.getRealPath("/resources/staticPdf/CertificateD.pdf");

			document = new Document();
			out = new ByteArrayOutputStream();
			// left,right,top,bottom
			fileName = "ClearnessCertificate.pdf";
			ClearnessDTO customerInfo = getCustomerInfo(customer_id, area);
			if (customerInfo.getDueAmount() == 0
					&& customerInfo.getDueMonth().equals("")) {
				reader = new PdfReader(new FileInputStream(realPathC));
			} else {
				reader = new PdfReader(new FileInputStream(realPathD));
			}

			certificate = new ByteArrayOutputStream();
			PdfStamper stamp = new PdfStamper(reader, certificate);
			PdfContentByte over;
			over = stamp.getOverContent(1);

			over.beginText();
			over.endText();

			if (readers.size() > 0) {
				PdfWriter writer = PdfWriter.getInstance(document, out);

				document.open();

				PdfContentByte cb = writer.getDirectContent();
				PdfReader pdfReader = null;
				PdfImportedPage page;

				for (int k = 0; k < readers.size(); k++) {
					document.newPage();
					pdfReader = readers.get(k);
					page = writer.getImportedPage(pdfReader, 1);
					cb.addTemplate(page, 0, 0);
				}

				document.close();
				ReportUtil rptUtil = new ReportUtil();
				rptUtil.downloadPdf(out, response, fileName);
				document = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private ClearnessDTO getCustomerInfo(String customer_id, String area_id) {
		ClearnessDTO ctrInfo = new ClearnessDTO();

		try {

			String customer_info_sql = "SELECT * "
					+ "  FROM (  SELECT bi.CUSTOMER_ID, "
					+ "                 CUSTOMER_CATEGORY, "
					+ "                 bi.AREA_ID, "
					+ "                 LISTAGG ( "
					+ "                       TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON') "
					+ "                    || ' ' "
					+ "                    || SUBSTR (BILL_YEAR, 3), "
					+ "                    ',') "
					+ "                 WITHIN GROUP (ORDER BY BILL_YEAR ASC, BILL_MONTH ASC) "
					+ "                    AS DUEMONTH, "
					+ "                 SUM ( "
					+ "                      BILLED_AMOUNT "
					+ "                    + CALCUALTESURCHARGE (BILL_ID, "
					+ "                                          TO_CHAR (SYSDATE, 'dd-mm-YYYY'))) "
					+ "                    totalamount, "
					+ "                 COUNT (*) cnt "
					+ "            FROM BILL_METERED bi, CUSTOMER_CONNECTION cc "
					+ "           WHERE     BI.CUSTOMER_ID = CC.CUSTOMER_ID "
					+ "                 AND CC.STATUS = 1 "
					+ "                 AND bi.STATUS = 1 "
					+ "                 AND bi.area_id = ? "
					+ "                 AND BI.CUSTOMER_ID = ? "
					+
					// "                 And bi.CUSTOMER_CATEGORY= " +
					"                -- AND BILL_YEAR || LPAD (BILL_MONTH, 2, 0) <= 2017030 "
					+ "        GROUP BY BI.CUSTOMER_ID, CUSTOMER_CATEGORY, bi.AREA_ID "
					+ "          HAVING COUNT (*) > 1) tmp1, "
					+ "       (SELECT AA.CUSTOMER_ID, "
					+ "               BB.FULL_NAME, "
					+ "               BB.MOBILE, "
					+ "               AA.ADDRESS_LINE1, "
					+ "               AA.ADDRESS_LINE2 "
					+ "          FROM CUSTOMER_ADDRESS aa, CUSTOMER_PERSONAL_INFO bb "
					+ "         WHERE AA.CUSTOMER_ID = BB.CUSTOMER_ID) tmp2 "
					+ " WHERE tmp1.CUSTOMER_ID = tmp2.CUSTOMER_ID ";

			PreparedStatement ps1 = conn.prepareStatement(customer_info_sql);
			ps1.setString(1, customer_id);
			ps1.setString(2, area_id);

			ResultSet resultSet = ps1.executeQuery();

			while (resultSet.next()) {

				ctrInfo.setCustomerID(resultSet.getString("CUSTOMER_ID"));
				ctrInfo.setCustomerName(resultSet.getString("FULL_NAME"));
				ctrInfo.setCustomerAddress(resultSet.getString("ADDRESS_LINE1"));
				ctrInfo.setDueMonth(resultSet.getString("DUEMONTH"));
				ctrInfo.setDueAmount(Double.parseDouble(resultSet
						.getString("TOTALAMOUNT")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ctrInfo;
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

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
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

	public ServletContext getServlet() {
		return servlet;
	}

	public void setServlet(ServletContext servlet) {
		this.servlet = servlet;
	}

	public static DecimalFormat getTaka_format() {
		return taka_format;
	}

	public static void setTaka_format(DecimalFormat taka_format) {
		DefaulterCCertificate.taka_format = taka_format;
	}

	public static DecimalFormat getConsumption_format() {
		return consumption_format;
	}

	public static void setConsumption_format(DecimalFormat consumption_format) {
		DefaulterCCertificate.consumption_format = consumption_format;
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

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}

}
