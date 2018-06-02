package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.MinistryCollectionDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class MinistryCollectionDetailsReport extends BaseAction{
	private static final long serialVersionUID = 1L;
	private String bill_month;
	private String bill_year;
	private String area_id;
	private String ministry_id;

	public ArrayList<MinistryCollectionDTO> getMinistryCollection() {
		MinistryCollectionDTO ministry = null;
		ArrayList<MinistryCollectionDTO> ministryList = new ArrayList<MinistryCollectionDTO>();
		Connection conn = ConnectionManager.getConnection();

		String whereClause = null;
		if (ministry_id.equals("all")) {
			whereClause = " cc.ministry_id IS NOT NULL ";
		} else {
			whereClause = "cc.ministry_id = " + ministry_id;
		}

		String sql = " SELECT * from( " 
				+ "SELECT cc.customer_id, "
				+ "       BM.CUSTOMER_CATEGORY_NAME, "
				+ "       cc.MINISTRY_ID, "
				+ "       MM.MINISTRY_NAME, "
				+ "       BM.PAYABLE_AMOUNT, "
				+ "       (BM.COLLECTED_AMOUNT + BM.COLLECTED_SURCHARGE) AS collected_amount "
				+ "  FROM customer_connection cc, mst_ministry mm, bill_metered bm "
				+ " WHERE "
				+ whereClause
				+ "       AND CC.MINISTRY_ID = MM.MINISTRY_ID "
				+ "       AND BM.CUSTOMER_ID = CC.CUSTOMER_ID "
				+ "       AND BM.BILL_MONTH =  "
				+ bill_month
				+ "       AND BM.BILL_YEAR =  "
				+ bill_year
				+ " and area_id = '"
				+ area_id
				+ "' "
				+ "UNION "
				+ "SELECT cc.customer_id, "
				+ "       BM.CUSTOMER_CATEGORY_NAME, "
				+ "       cc.MINISTRY_ID, "
				+ "       MM.MINISTRY_NAME, "
				+ "       BM.ACTUAL_PAYABLE_AMOUNT, "
				+ "       (BM.COLLECTED_PAYABLE_AMOUNT + BM.COLLECTED_SURCHARGE) "
				+ "          AS collected_amount "
				+ "  FROM customer_connection cc, mst_ministry mm, bill_non_metered bm "
				+ " WHERE "
				+ whereClause
				+ "       AND CC.MINISTRY_ID = MM.MINISTRY_ID "
				+ "       AND BM.CUSTOMER_ID = CC.CUSTOMER_ID "
				+ "       AND BM.BILL_MONTH =  "
				+ bill_month
				+ "       AND BM.BILL_YEAR =  "
				+ bill_year
				+ " and area_id = '" + area_id + "' "
				+") order by MINISTRY_ID " ;

		Statement stmt = null;
		ResultSet r = null;

		try {
			stmt = conn.createStatement();
			r = stmt.executeQuery(sql);

			while (r.next()) {
				ministry = new MinistryCollectionDTO();

				ministry.setCustomer_id(r.getString("CUSTOMER_ID"));
				ministry.setCustomer_category_name(r
						.getString("CUSTOMER_CATEGORY_NAME"));
				ministry.setMinistry_id(r.getString("MINISTRY_ID"));
				ministry.setMinistry_name(r.getString("MINISTRY_NAME"));
				ministry.setPayable_amount(r.getString("PAYABLE_AMOUNT"));
				ministry.setCollected_amount(r.getString("COLLECTED_AMOUNT"));

				ministryList.add(ministry);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ministryList;
	}

	// private static final long serialVersionUID = 1L;
	public ServletContext servlet;
	public HttpServletResponse response = ServletActionContext.getResponse();
	public HttpServletRequest request;

	static Font fonth = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	static Font font1 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
	static Font font1nb = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
	static Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
	static Font font2 = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	static DecimalFormat consumption_format = new DecimalFormat(
			"##########0.000");
	UserDTO loggedInUser = (UserDTO) ServletActionContext.getRequest()
			.getSession().getAttribute("user");

	public String execute() throws Exception {

		String fileName = "Ministry_Collection_Details.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		//document.setMargins(20, 20, 50, 80);
		PdfPCell pcell = null;

		try {

			ReportFormat eEvent = new ReportFormat(getServletContext());

			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);

			document.open();

			PdfPTable headerTable = new PdfPTable(3);

			headerTable.setWidths(new float[] { 5, 190, 5 });

			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);

			String realPath = servlet
					.getRealPath("/resources/images/logo/JG.png"); // image path
			Image img = Image.getInstance(realPath);

			// img.scaleToFit(10f, 200f);
			// img.scalePercent(200f);
			img.scaleAbsolute(28f, 31f);
			img.setAbsolutePosition(125f, 773f);
			// img.setAbsolutePosition(290f, 540f); // rotate

			document.add(img);

			PdfPTable mTable = new PdfPTable(1);
			mTable.setWidthPercentage(90);
			mTable.setWidths(new float[] { 100 });
			pcell = new PdfPCell(new Paragraph("JALALABAD GAS T & D SYSTEM LIMITED", fonth));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("REGIONAL OFFICE : ", font2);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer
					.valueOf(loggedInUser.getArea_id()) - 1]), font3);
			Paragraph p = new Paragraph();
			p.add(chunk1);
			p.add(chunk2);
			pcell = new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			pcell = new PdfPCell(mTable);
			pcell.setBorder(0);
			headerTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			document.add(headerTable);

			PdfPTable dataTable = new PdfPTable(3);
			dataTable.setWidthPercentage(70);
			dataTable.setWidths(new float[] { (float) 0.15, (float) 1,
					(float) 0.5 });

			pcell = new PdfPCell(new Paragraph(" "));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(3);
			pcell.setBorder(Rectangle.NO_BORDER);
			dataTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Ministry Customer Collection Details", font1));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.BOX);
			pcell.setPadding(5);
			dataTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(" "));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(3);
			pcell.setBorder(Rectangle.NO_BORDER);
			dataTable.addCell(pcell);

			
			
			
			ArrayList<MinistryCollectionDTO> ministryCollectionList= getMinistryCollection();
			
			PdfPTable mainTable= new PdfPTable(5); 
			mainTable.setWidthPercentage(100);
			mainTable.setWidths(new float[]{15,30,31,12,12});
			
			pcell = new PdfPCell(new Paragraph("Collection Month: "
					+ Month.values()[Integer.valueOf(bill_month) - 1] + ", "
					+ bill_year, ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(5);
			pcell.setPadding(5);
			pcell.setBorder(Rectangle.NO_BORDER);
			mainTable.addCell(pcell);
			
			
			
			
			
			String prev_category = "";
			String cur_category = "";
			
			
			for(MinistryCollectionDTO x: ministryCollectionList){
				
				cur_category = x.getMinistry_id();
				
				if (!cur_category.equals(prev_category)) {
					
					pcell = new PdfPCell(new Paragraph(" ", ReportUtil.f8B));
					pcell.setColspan(5);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setBorder(Rectangle.NO_BORDER);
					mainTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(x.getMinistry_name(), ReportUtil.f8B));
					pcell.setColspan(5);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setPaddingBottom(10);
					pcell.setBorder(Rectangle.NO_BORDER);
					mainTable.addCell(pcell);		
										
					pcell = new PdfPCell(new Paragraph("Customer ID", ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setPaddingBottom(3);
					mainTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Category", ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setPaddingBottom(3);
					mainTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Ministry Name", ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setPaddingBottom(3);
					mainTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Payable amount", ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setPaddingBottom(3);
					mainTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Collected Amount", ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setPaddingBottom(3);
					mainTable.addCell(pcell);
				}
				
					pcell = new PdfPCell(new Paragraph(x.getCustomer_id(), ReportUtil.f8));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setPadding(5);
					mainTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(x.getCustomer_category_name(), ReportUtil.f8));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setPadding(5);
					mainTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(x.getMinistry_name(), ReportUtil.f8));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setPadding(5);
					mainTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(x.getPayable_amount(), ReportUtil.f8));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setPadding(5);
					mainTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(x.getCollected_amount(), ReportUtil.f8));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setPadding(5);
					mainTable.addCell(pcell);				
				
				prev_category = cur_category;
				cur_category = x.getMinistry_id();
			}				

			document.add(dataTable);
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

	public String getBill_month() {
		return bill_month;
	}

	public void setBill_month(String bill_month) {
		this.bill_month = bill_month;
	}

	public String getBill_year() {
		return bill_year;
	}

	public void setBill_year(String bill_year) {
		this.bill_year = bill_year;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getMinistry_id() {
		return ministry_id;
	}

	public void setMinistry_id(String ministry_id) {
		this.ministry_id = ministry_id;
	}

	public ServletContext getServlet() {
		return servlet;
	}

	public void setServlet(ServletContext servlet) {
		this.servlet = servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
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
	

}
