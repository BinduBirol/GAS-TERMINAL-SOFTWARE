package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.FreedomFighterDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
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

public class FreedomFighterInfo extends BaseAction {
	private static final long serialVersionUID = 1L;
	public ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();

	private String area;

	static Font fonth = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
	static Font font1 = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
	static Font font1nb = new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL);
	static Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
	static Font font2 = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	static DecimalFormat consumption_format = new DecimalFormat(
			"##########0.000");
	UserDTO loggedInUser = (UserDTO) ServletActionContext.getRequest()
			.getSession().getAttribute("user");

	public String execute() throws Exception {

		String fileName = "FreedomFightersList.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		document.setMargins(20, 20, 50, 80);
		PdfPCell pcell = null;

		ReportFormat eEvent = new ReportFormat(getServletContext());

		PdfWriter.getInstance(document, baos).setPageEvent(eEvent);

		document.open();
		
		try {
		
		/////////////////header starts/////////////
		
		PdfPTable headerTable = new PdfPTable(3);
		   
		
		headerTable.setWidths(new float[] {
			5,190,5
		});
		
		headerTable.setWidths(new float[] {
				5,190,5
			});

			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			
			
			String realPath = servlet.getRealPath("/resources/images/logo/JG.png"); 	// image path
			Image img = Image.getInstance(realPath);
						
            	//img.scaleToFit(10f, 200f);
            	//img.scalePercent(200f);
            img.scaleAbsolute(28f, 31f);
            img.setAbsolutePosition(123f, 760f);		
            	//img.setAbsolutePosition(290f, 540f);		// rotate
            
	        document.add(img);
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidthPercentage(90);
			mTable.setWidths(new float[]{100});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS T & D SYSTEM LIMITED",fonth));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("REGIONAL OFFICE : ",font2);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),font3);
			Paragraph p = new Paragraph(); 
			p.add(chunk1);
			p.add(chunk2);
			pcell=new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
					
			pcell=new PdfPCell(mTable);
			pcell.setBorder(0);
			headerTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			document.add(headerTable);
			///////////header ends/////////////
		
			PdfPTable dataTable = new PdfPTable(5);
			dataTable.setWidthPercentage(100);
			dataTable.setWidths(new float[]{(float) .25 , (float) .7, (float) 2, (float) .25, (float) .25});
			
			pcell= new PdfPCell(new Paragraph(" "));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(5);
			pcell.setBorder(Rectangle.NO_BORDER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Freedom Fighters Information",font1));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			pcell.setPadding(5);
			dataTable.addCell(pcell);
			
			
			pcell= new PdfPCell(new Paragraph(" "));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(5);
			pcell.setBorder(Rectangle.NO_BORDER);
			dataTable.addCell(pcell);
			
			///datatable header
			pcell= new PdfPCell(new Paragraph("Sl", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			pcell.setRowspan(2);			
			dataTable.addCell(pcell);
			
			pcell= new PdfPCell(new Paragraph("Customer ID", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			pcell.setRowspan(2);			
			dataTable.addCell(pcell);
			
			pcell= new PdfPCell(new Paragraph("Customer Name", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			pcell.setRowspan(2);			
			dataTable.addCell(pcell);
			
			pcell= new PdfPCell(new Paragraph("Burner", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(2);
			pcell.setRowspan(1);			
			dataTable.addCell(pcell);
			
			pcell= new PdfPCell(new Paragraph("S", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			dataTable.addCell(pcell);
			
			pcell= new PdfPCell(new Paragraph("D", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			dataTable.addCell(pcell);
			///////////datatable header ends			
			
			ArrayList<FreedomFighterDTO> freeFighters = getFreedomFighterList();
			
			for(FreedomFighterDTO x:freeFighters ){
			//for(int x=0; x<freeFighters.size(); x++)
				
				pcell= new PdfPCell(new Paragraph(String.valueOf(x.getSerial()), font2));
				pcell.setPadding(5);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);					
				dataTable.addCell(pcell);
				
				pcell= new PdfPCell(new Paragraph(x.getCustomer_id(), font2));
				pcell.setPadding(5);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);					
				dataTable.addCell(pcell);
				
				pcell= new PdfPCell(new Paragraph(x.getCustomer_name(), font2));
				pcell.setPadding(5);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);			
				dataTable.addCell(pcell);
				
				pcell= new PdfPCell(new Paragraph(String.valueOf(x.getSingleBurner()), font2));
				pcell.setPadding(5);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
				dataTable.addCell(pcell);
				
				pcell= new PdfPCell(new Paragraph(String.valueOf(x.getDoubleBurner()), font2));
				pcell.setPadding(5);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
				dataTable.addCell(pcell);
				
			}
			
			document.add(dataTable);
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
		

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public ArrayList<FreedomFighterDTO> getFreedomFighterList() {
		FreedomFighterDTO freedomFighterDTO = null;
		ArrayList<FreedomFighterDTO> freedomFighters = new ArrayList<FreedomFighterDTO>();
		Connection conn = ConnectionManager.getConnection();
		
		String sql= " SELECT customer_id, " +
				"       full_name, " +
				"       SUBSTR (customer_id, 3, 2) AS customer_category, " +
				"       father_name, " +
				"       PROPRIETOR_NAME, " +
				"       getBurner (customer_id) AS burner " +
				"  FROM customer_personal_info " +
				" WHERE FREEDOM_FIGHTER = 'Y' AND SUBSTR (customer_id, 1, 2) =  '"+loggedInUser.getArea_id()+"' ";
		
		Statement stmt = null;
		ResultSet r = null;
		
		try {
			stmt = conn.createStatement();
			r = stmt.executeQuery(sql);
			int i= 1;
			while (r.next()) {
				
				freedomFighterDTO= new FreedomFighterDTO();
				freedomFighterDTO.setCustomer_id(r.getString("CUSTOMER_ID"));
				freedomFighterDTO.setCustomer_category(r.getString("CUSTOMER_CATEGORY"));
				freedomFighterDTO.setCustomer_name(r.getString("FULL_NAME"));
				freedomFighterDTO.setFather_name(r.getString("FATHER_NAME"));
				freedomFighterDTO.setSerial(i++);
				String[] burner= r.getString("BURNER").split("#");				
				
				//for(int b=0; b<burner.)
				
				freedomFighterDTO.setSingleBurner(Integer.parseInt( burner[0]));
				freedomFighterDTO.setDoubleBurner(Integer.parseInt(burner[1]));
				
				freedomFighters.add(freedomFighterDTO);				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return freedomFighters;
	}

	public ServletContext getServlet() {
		return servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

}
