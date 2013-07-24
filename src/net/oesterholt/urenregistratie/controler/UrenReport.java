package net.oesterholt.urenregistratie.controler;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import net.oesterholt.jndbm.NDbm2;
import net.oesterholt.jxmlnote.report.Report;
import net.oesterholt.jxmlnote.report.ReportException;
import net.oesterholt.jxmlnote.report.ReportProgressBar;
import net.oesterholt.jxmlnote.report.elements.Cell;
import net.oesterholt.jxmlnote.report.elements.Chunk;
import net.oesterholt.jxmlnote.report.elements.Paragraph;
import net.oesterholt.jxmlnote.report.elements.Rectangle;
import net.oesterholt.jxmlnote.report.elements.Table;
import net.oesterholt.jxmlnote.report.pdf.PdfReport;
import net.oesterholt.jxmlnote.report.viewers.PdfViewer;
import net.oesterholt.jxmlnote.styles.StyleContainedException;
import net.oesterholt.jxmlnote.styles.XMLNoteParStyle;
import net.oesterholt.jxmlnote.styles.XMLNoteStyles;
import net.oesterholt.jxmlnote.utils.DefaultXMLNotePreferences;
import net.oesterholt.urenregistratie.data.Jaar;
import net.oesterholt.urenregistratie.data.Kostensoort;
import net.oesterholt.urenregistratie.data.Project;
import net.oesterholt.urenregistratie.utils.Swing;

public class UrenReport {
	
	static final int MONTH=1;
	static final int YEAR=2;
	static final int WEEK=3;
	
	private Component 		_parent;
	private Jaar      		_jaar; 
	private int		  		_type;
	private DateTime  		_forDate;
	private XMLNoteStyles   _styles;
	private NDbm2			_dbm;

	public void report() throws Exception {
		if (_type==WEEK) {
			reportWeek();
		} else {
			reportTotals(_type);
		}
	}
	
	public void reportTotals(int type) throws Exception {
		_styles=new XMLNoteStyles();
		final File f=File.createTempFile("UrenRegistratie","pdf");

		InputStream in=_dbm.getBlob("fontCache");
		ByteArrayOutputStream bout=new ByteArrayOutputStream();
		final PdfReport pdfReport=new PdfReport(in,bout);

		pdfReport.beginReport(f);
		pdfReport.setPageSize(Report.PageSize.A4);
		float left=(72f/2.54f)*1f;
		float right=(72f/2.54f)*1f;
		float top=(72f/2.54f)*2f;
		float bottom=(72f/2.54f)*2f;
		pdfReport.setMargins(new Rectangle(left,top,right,bottom));
		
		final Preferences _prefs=Preferences.userNodeForPackage(UrenReport.class);

		ReportProgressBar bar=ReportProgressBar.runJob(pdfReport,_parent, "Genereren rapport...", new ReportProgressBar.Job() {
			public void job(ReportProgressBar.Progress p) {
				try {
					if (_type==MONTH) {
						createMonthReport(pdfReport,p);
					} else {
						createYearReport(pdfReport,p);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			public void cancelled() {
				
			}
		}, new DefaultXMLNotePreferences(_prefs)); 
		
		
		bout.close();
		in.close();
		if (!bar.cancelled()) {
			
			ByteArrayInputStream bin=new ByteArrayInputStream(bout.toByteArray());
			_dbm.putBlob("fontCache", bin);
			bin.close();
			
			showViewer(_parent,String.format("Uren %s rapport",(_type==MONTH) ? "maand" : "jaar"),f);
		}
	
	}
	
	public void reportWeek() throws Exception {
		_styles=new XMLNoteStyles();
		final File f=File.createTempFile("UrenRegistratie", "pdf");

		InputStream in=_dbm.getBlob("fontCache");
		ByteArrayOutputStream bout=new ByteArrayOutputStream();
		final PdfReport pdfReport=new PdfReport(in,bout);
		
		pdfReport.beginReport(f);
		pdfReport.setPageSize(Report.PageSize.A4);
		pdfReport.setOrientation(Report.Orientation.LANDSCAPE);
		float left=(72f/2.54f)*1f;
		float right=(72f/2.54f)*1f;
		float top=(72f/2.54f)*2f;
		float bottom=(72f/2.54f)*2f;
		pdfReport.setMargins(new Rectangle(left,top,right,bottom));
		
		final Preferences _prefs=Preferences.userNodeForPackage(UrenReport.class);

		ReportProgressBar bar=ReportProgressBar.runJob(pdfReport,_parent, "Genereren rapport...", new ReportProgressBar.Job() {
			public void job(ReportProgressBar.Progress p) {
				try {
					if (_type==WEEK) {
						createWeekReport(pdfReport,p);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			public void cancelled() {
				
			}
		}, new DefaultXMLNotePreferences(_prefs)); 

		bout.close();
		if (in!=null) {
			in.close();
		}
		
		if (!bar.cancelled()) {
			
			ByteArrayInputStream bin=new ByteArrayInputStream(bout.toByteArray());
			_dbm.putBlob("fontCache", bin);
			bin.close();
			
			showViewer(_parent,"Uren rapport",f);
		}
		
	}
	
	private void showViewer(Component p,String title,File pdfFile) {
		try {
			Window frame=Swing.getWindow(p);
			Preferences prefs=Preferences.userNodeForPackage(UrenReport.class);
			PdfViewer.showPdfViewer(frame, title, pdfFile,new DefaultXMLNotePreferences(prefs));
		} catch (IOException e) {	// FIXME: Add JoptionPanel with problem
			e.printStackTrace();
		}				
	}
	
	private XMLNoteParStyle header3() throws Exception {
		return _styles.h3Style();
	}
	
	private XMLNoteParStyle tableBoldStyle() throws Exception {
		if (_styles.parStyle("tableBold")==null) {
			XMLNoteParStyle tbl=new XMLNoteParStyle(_styles,tableStyle().toString());
			tbl.id("table");
			tbl.name("Table sized text (bold)");
			tbl.bold(true);
			_styles.addParStyle("tableBold",tbl);
		}
		XMLNoteParStyle tbl=_styles.parStyle("tableBold");
		return (tbl==null) ? tableStyle() : tbl;
	}
	
	private XMLNoteParStyle tableStyle() throws Exception {
		if (_styles.parStyle("table")==null) {
			XMLNoteParStyle tbl=new XMLNoteParStyle(_styles,paragraphStyle().toString());
			tbl.id("table");
			tbl.name("Table sized text");
			_styles.addParStyle("table", tbl);
		} 
		XMLNoteParStyle tbl=_styles.parStyle("table");
		tbl.pointSize(8);
		tbl.bottomSkip(6);
		tbl.topSkip(6);
		return (tbl==null) ? paragraphStyle() : tbl;
	}

	private XMLNoteParStyle paragraphStyle() {
		return _styles.paragraphStyle();
	}

	
	private Cell headerCell(Report rep,String txt) throws Exception {
		Chunk ctxt=rep.createChunk(txt);
		Paragraph p=rep.createParagraph(tableBoldStyle());
		p.setAlignment(Report.Align.CENTER);
		p.add(ctxt);
		Cell c=rep.createCell(p);
		Color col=new Color(0xcc,0xcc,0xcc);
		c.setBackground(col);
		c.setBorder(col.darker(),1.0f);
		return c;
	}
	
	private Cell projectCell(Report rep,String txt) throws Exception {
		Chunk ctxt=rep.createChunk(txt);
		Paragraph p=rep.createParagraph(tableBoldStyle());
		p.add(ctxt);
		Cell c=rep.createCell(p);
		c.setBackground(Color.lightGray);
		return c;
	}
	
	private void createWeekReport(Report rep,ReportProgressBar.Progress p) throws Exception {
		
		{
			int week=_forDate.getWeekOfWeekyear();
			String dt=_forDate.toString("d-M-Y");
			Chunk c=rep.createChunk(String.format("Weekstaat voor week nummer:  %d, datum: %s",week,dt)); 
			Paragraph pr=rep.createParagraph(header3());
			pr.add(c);
			rep.add(pr);
		}
		
		Table table=rep.createTable(
				Report.Align.CENTER,
				100.0f,
				new float[]{0.20f,0.07f,0.07f,0.07f,0.07f,0.07f,0.07f,0.07f,0.07f,0.07f,0.07f}
		);
		table.setVSpace(10.0f,10.f);

		int i,n;
		boolean reuse=false;
		table.add(headerCell(rep,"project/ksrt"));
		table.add(headerCell(rep,"budget"));
		table.add(headerCell(rep,"rest."));
		table.add(headerCell(rep,"ma"));
		table.add(headerCell(rep,"di"));
		table.add(headerCell(rep,"woe"));
		table.add(headerCell(rep,"do"));
		table.add(headerCell(rep,"vr"));
		table.add(headerCell(rep,"za"));
		table.add(headerCell(rep,"zo"));
		table.add(headerCell(rep,"tot."));

		Project proj;
		for(i=0;i<_jaar.nProjects();i++) {
			proj=_jaar.project(i);
			
			String name=proj.getNaam();
			Cell naam=projectCell(rep,name);
			//Paragraph scr=rep.createParagraph(tableStyle());
			//scr.add(rep.createChunk(name));
			//Cell naam=rep.createCell(scr);
			table.add(naam);
			
			//scr=rep.createParagraph(tableStyle());
			Cell ff=projectCell(rep,""); 
			for(int j=0;j<10;j++) { table.add(ff); }
			
			Paragraph scr;
			for(int k=0;k<proj.nKostensoorten();k++) {
				Kostensoort K=proj.kostensoort(k);
				String ksrt=K.getNaam();
				scr=rep.createParagraph(tableStyle());
				scr.add(rep.createChunk(ksrt));
				table.add(rep.createCell(scr));
				
				scr=rep.createParagraph(tableStyle());
				scr.add(rep.createChunk(String.format("%.1f", K.getBudget())));
				scr.setAlignment(Report.Align.RIGHT);
				table.add(rep.createCell(scr));
				
				scr=rep.createParagraph(tableStyle());
				scr.add(rep.createChunk(String.format("%.1f", K.getRestand())));
				scr.setAlignment(Report.Align.RIGHT);
				table.add(rep.createCell(scr));
				
				float tot=0.0f;
				for(int w=0;w<7;w++) {
					DateTime dt=new DateTime(_forDate);
					Duration dur=new Duration(w*24*3600*1000);
					dt=dt.plus(dur);
					Float uren=K.getUur(dt);
					tot+=uren;
					
					if (uren==0.0f) {
						scr=rep.createParagraph(tableStyle());
						scr.add(rep.createChunk(""));
					} else {
						scr=rep.createParagraph(tableStyle());
						scr.add(rep.createChunk(String.format("%.1f", uren)));
						scr.setAlignment(Report.Align.RIGHT);
					}
					table.add(rep.createCell(scr));
				}
				
				scr=rep.createParagraph(tableStyle());
				scr.add(rep.createChunk(String.format("%.1f", tot)));
				scr.setAlignment(Report.Align.RIGHT);
				table.add(rep.createCell(scr));
			}
		}
		
		rep.add(table);
		rep.endReport();
	}

	public void createMonthReport(Report rep,ReportProgressBar.Progress p) throws Exception {
		{
			String dt=_forDate.toString("M-Y");
			Chunk c=rep.createChunk(String.format("Maandstaat voor maand %s",dt)); 
			Paragraph pr=rep.createParagraph(header3());
			pr.add(c);
			rep.add(pr);
		}
		
		Table table=rep.createTable(
				Report.Align.CENTER,
				80.0f,
				new float[]{0.70f,0.10f,0.10f,0.10f}
		);
		table.setVSpace(10.0f,10.f);
		table.add(headerCell(rep,"project/ksrt"));
		table.add(headerCell(rep,"budget"));
		table.add(headerCell(rep,"rest."));
		table.add(headerCell(rep,"totaal"));
		
		Project proj;
		int i;
		for(i=0;i<_jaar.nProjects();i++) {
			proj=_jaar.project(i);
			
			String name=proj.getNaam();
			Cell naam=projectCell(rep,name);
			//Paragraph scr=rep.createParagraph(tableStyle());
			//scr.add(rep.createChunk(name));
			//Cell naam=rep.createCell(scr);
			table.add(naam);
			
			//scr=rep.createParagraph(tableStyle());
			Cell ff=projectCell(rep,""); 
			for(int j=0;j<3;j++) { table.add(ff); }
			
			Paragraph scr;
			for(int k=0;k<proj.nKostensoorten();k++) {
				Kostensoort K=proj.kostensoort(k);
				String ksrt=K.getNaam();
				scr=rep.createParagraph(tableStyle());
				scr.add(rep.createChunk(ksrt));
				table.add(rep.createCell(scr));
				
				scr=rep.createParagraph(tableStyle());
				scr.add(rep.createChunk(String.format("%.1f", K.getBudget())));
				scr.setAlignment(Report.Align.RIGHT);
				table.add(rep.createCell(scr));
				
				scr=rep.createParagraph(tableStyle());
				scr.add(rep.createChunk(String.format("%.1f", K.getRestand())));
				scr.setAlignment(Report.Align.RIGHT);
				table.add(rep.createCell(scr));
				
				int dom=_forDate.getDayOfMonth();
				Duration dur=new Duration(dom*24*3600*1000);
				DateTime dt=new DateTime(_forDate);
				dt=dt.minus(dur);
				float tot=0.0f;
				int [] days={31,(dt.yearOfEra().isLeap()) ? 29 : 28,31,30,31,30,31,31,30,31,30,31};
				int month=_forDate.getMonthOfYear();
				int ndays=days[month];
				for(int w=0;w<ndays;w++) {
					DateTime dt1=dt;
					dur=new Duration(w*24*3600*1000);
					dt1=dt1.plus(dur);
					Float uren=K.getUur(dt1);
					tot+=uren;
				}
				
				scr=rep.createParagraph(tableStyle());
				scr.add(rep.createChunk(String.format("%.1f", tot)));
				scr.setAlignment(Report.Align.RIGHT);
				table.add(rep.createCell(scr));
			}
		}
		
		rep.add(table);
		rep.endReport();
	}

	public void createYearReport(Report rep,ReportProgressBar.Progress p) throws Exception {
		
	}
	
	public UrenReport(Component parent,Jaar jr,NDbm2 dbm) {
		this(parent,jr,new DateTime(new Date()),YEAR,dbm);
	}
	
	public UrenReport(Component parent,Jaar jr,DateTime forDate,int type,NDbm2 dbm) {
		_parent=parent;
		_jaar=jr;
		_forDate=forDate;
		_type=type;
		_dbm=dbm;
		
	}
}
