package net.dijkema.urenregistratie.controler;


import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import net.dijkema.jndbm.NDbm2;
import net.dijkema.jndbm2.exceptions.NDbmException;
import net.dijkema.splittable.AbstractTwoLevelSplitTableModel;
import net.dijkema.urenregistratie.data.Jaar;
import net.dijkema.urenregistratie.data.Kostensoort;
import net.dijkema.urenregistratie.data.Nil;
import net.dijkema.urenregistratie.data.Project;
import net.dijkema.urenregistratie.dialogs.NieuwProject;
import net.dijkema.urenregistratie.dialogs.VerwijderProject;
import net.dijkema.urenregistratie.dialogs.WijzigProject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;

public class UrenControler extends AbstractTwoLevelSplitTableModel {
	
	private NDbm2 		_dbm;
	private Integer		_year;
	private Jaar		_urenJaar;
	private DateTime	_date;
	private Nil			_nil=new Nil();

	////////////////////////////////////////////////////////////////////
	
	// voor de node zelf
	public int getNodeColumnCount() {
		return 12;
	}

	// voor de rij in de node
	public int getNodeColumnCount(int a) {
		return 12;
	}

	public boolean getNodeExpanded(int node) {
		if (node < _urenJaar.nProjects()) {
			return _urenJaar.project(node).isActive();
		} else {
			return true;
		}
	}
	
	public int getNodeRowCount() {
		if (_urenJaar != null) {
			return _urenJaar.nProjects() + 1; 
		} else {
			return 0;
		}
	}

	public int getNodeRowCount(int i) {
		if (_urenJaar != null) {
			if (i == _urenJaar.nProjects()) { 
				return 1; // Optelling van de totalen over alle rijen. 
			} else {
				return _urenJaar.project(i).nKostensoorten();
			}
		} else {
			return 0;
		}
	}

	public Object getNodeValue(int node, int col) {
		if (col==0) {
			if (node < _urenJaar.nProjects()) {
				return _urenJaar.project(node);
			} else {
				return ""; 
			}
		} else if (col == 10) {
			if (node == 0) {
				return "";
			}
			
			try {
				int i, N;
				Project P = _urenJaar.project(node - 1);
				Float totaal = 0.0f;
				for(i = 0, N = P.nKostensoorten(); i < N; i++) {
					Kostensoort K = P.kostensoort(i);
					DateTime d=_date;
					int id;
					Duration dur=new Duration(24*3600*1000);
					for(id = 0; id < 7; id++) {
						Float uren = K.getUur(d);
						d = d.plus(dur);
						totaal += uren;
					}
				}
				return totaal;
			} catch(Exception e) {
				return "error";
			}
		} else {
			if (col == 11) {
				node -= 1;
				if (node >= 0) {
					int i, n;
					float total = 0.0f;
					for(i = 0, n = _urenJaar.project(node).nKostensoorten(); i < n; i++) {
						try {
							Kostensoort k= _urenJaar.project(node).kostensoort(i);
							total += k.totaalUren();
						} catch (Exception e) {
							// do nothing
						}
					}
					return total;
				} else { 
					return new Nil();
				}
			} else { 
				return new Nil();
			}
		}
	}

	public Object getValueAt(int node, int ksrt, int col) {
		try {
			if (node == _urenJaar.nProjects()) {
				// Totalen
				if (col == 0) {
					return "";
				} else if (col == 1) {
					return "Totaal:";
				} else if (col == 2) {
					return "";
				} else if (col == 11) {
					int j, m, i, n;
					float total = 0.0f;
					for(j = 0, m = _urenJaar.nProjects(); j < m; j++) {
						for(i = 0, n = _urenJaar.project(j).nKostensoorten(); i < n; i++) {
							try {
								Kostensoort k= _urenJaar.project(j).kostensoort(i);
								total += k.totaalUren();
							} catch (Exception e) {
								// do nothing
							}
						}
					}
					return total;
				} else if (col == 10) {
					int c;
					float tot=0.0f;
					for(c = 0; c < 7; c++) {
						DateTime d = _date;
						Duration dur = new Duration(c * 24 * 3600 * 1000);
						d = d.plus(dur);
						int n, k;
						for(n = 0; n < _urenJaar.nProjects(); n++) {
							for(k = 0; k < _urenJaar.project(n).nKostensoorten(); k++) {
								Float _u = _urenJaar.project(n).kostensoort(k).getUur(d); 
								if (_u == null) { _u = 0.0f; }
								tot += _u;
							}
						}
					}
					return tot;
				} else {
					int c = col - 3;
					DateTime d = _date;
					Duration dur = new Duration(c * 24 * 3600 * 1000);
					d = d.plus(dur);
					float tot=0.0f;
					int n, k;
					for(n = 0; n < _urenJaar.nProjects(); n++) {
						for(k = 0; k < _urenJaar.project(n).nKostensoorten(); k++) {
							Float _u = _urenJaar.project(n).kostensoort(k).getUur(d); 
							if (_u == null) { _u = 0.0f; }
							tot += _u;
						}
					}
					return tot;
				}
			} else {
				if (col==0) {
					return _urenJaar.project(node).kostensoort(ksrt);
				} else if (col==1) {
					return _urenJaar.project(node).kostensoort(ksrt).getBudget();
				} else if (col==2) {
					return _urenJaar.project(node).kostensoort(ksrt).getRestand();
				} else if (col==10) {
					float tot=0.0f;
					Kostensoort k=_urenJaar.project(node).kostensoort(ksrt);
					if (k == null) {
						return 0.0f;
					} else {
						for(int c=0;c<7;c++) {
							DateTime d=_date;
							Duration dur=new Duration(c*24*3600*1000);
							d=d.plus(dur);
							Float _u = k.getUur(d);
							float uren = 0.0f;
							if (_u != null) {
								uren = _u;
							}
							tot+=uren;
						}
						return (Float) tot;
					}
				} else if (col==11) {
					Kostensoort k=_urenJaar.project(node).kostensoort(ksrt);
					return k.totaalUren();
				} else {
					col-=3;
					DateTime d=_date;
					Duration dur=new Duration(col*24*3600*1000);
					d=d.plus(dur);
					Float uren=_urenJaar.project(node).kostensoort(ksrt).getUur(d);
					if (uren==null) {
						return _nil;
					} else {
						if (uren==0.0) {
							return _nil;
						} else {
							return uren;
						}
					}
				}
			}
		} catch (NDbmException e) {
			e.printStackTrace();
			return _nil;
		}
	}

	public boolean setNodeExpanded(int node, boolean exp) {
		boolean isActive = false;
		try {
			if (node < _urenJaar.nProjects()) {
				_urenJaar.project(node).setActive(exp);
				isActive = _urenJaar.project(node).isActive();
			} else {
				// do nothing
			}
		} catch (NDbmException e) {
			e.printStackTrace();
		}
		return isActive;
	}

	public int getSplitColumn() {
		return 1;
	}
	
	public String getMaxString(int col) {
		try {
			if (col==0) {
				String s="";
				int m=0;
				for(int i=0;i<_urenJaar.nProjects();i++) {
					Project q=_urenJaar.project(i);
					int l=q.getNaam().length();
					if (l>m) { m=l;s=q.toString(); }
					for(int j=0;j<q.nKostensoorten();j++) {
						Kostensoort k=q.kostensoort(j);
						if (k.getNaam().length()>m) {
							m=k.getNaam().length();
							s=k.getNaam();
						}
					}
				}
				return s;
			} else if (col<10) {
				return "99.9";
			} else {
				return "999.9";
			}
		} catch (NDbmException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public boolean isCellEditable(int node,int row,int col) {
		if (col==0 || col==2 || col>=10) {
			return false;
		} else {
			return true;
		}
	}
	
	private void wrongNumber(Object input,String msg) {
		JOptionPane.showMessageDialog(
				null,
				msg, 
				"Fout in de invoer", 
				JOptionPane.ERROR_MESSAGE
				);
		
	}
	
	public void setValueAt(Object val,int node, int row, int col) {
		try {
			if (col>=3 && col<10) {
				Project p=_urenJaar.project(node);
				Kostensoort ksrt=p.kostensoort(row);
				col-=3;
				Duration d=new Duration(col*24*3600*1000);
				DateTime t=_date.plus(d);
				if (val instanceof String) {
					String v=((String) val).trim();
					try {
						Float fl=(v.isEmpty()) ? 0.0f : Float.parseFloat(v);
						ksrt.setUur(t, fl);
						super.fireTableDataChanged();
					} catch (Exception e) {
						wrongNumber(v,e.getMessage());
					}
				} 
			} else if (col==1) {  // budget
				Project p=_urenJaar.project(node);
				Kostensoort ksrt=p.kostensoort(row);
				if (val instanceof String) {
					String v=((String) val).trim();
					try {
						Float fl=(v.isEmpty()) ? 0.0f : Float.parseFloat((String) val);
						ksrt.setBudget(fl);
					} catch (Exception e) {
						wrongNumber(val,e.getMessage());
					}
				}
			}
		} catch (NDbmException e) {
			e.printStackTrace();
		}
	}
	
	private static String [] _days={"budg.","rest","ma","di","woe","do","vr","za","zo","t/w","t/jr"};
	
	public String getColumnName(int col) {
		if (col==0) {
			return "Project/Kostensoort";
		} else {
			col-=1;
			String label = _days[col];
			if ((col - 2) >= 0 && (col < 9)) {
				col -= 2;
				DateTime d=_date;
				Duration dur=new Duration(col*24*3600*1000);
				DateTime t = d.plus(dur);
				label += " " + t.getDayOfMonth() + "";
			}
			return label; //_days[col] + " (" + t.getDayOfMonth() + ")";
		}
	}

	////////////////////////////////////////////////////////////////////
	
	public AbstractTwoLevelSplitTableModel model() {
		return this;
	}
	
	// precondition: String is verified as a number (floating point)
	public boolean verify(String input,int col) {
		Float fl=Float.valueOf(input);
		if (col==1) { // budget
			return true;
		} else if (col>=3) {
			return (fl>=0 && fl<=24.0);
		} else {
			return false;
		}
	}
	
	public DateTime toMonday(DateTime d) {
		Duration dur=new Duration(24*3600*1000);
		while (d.getDayOfWeek()!=DateTimeConstants.MONDAY) {
			d=d.minus(dur);
		}
		return d;
	}
	
	public void nextWeek() {
		Duration dur=new Duration(7*24*3600*1000);
		DateTime nd=_date.plus(dur);
		int y=nd.getYear();
		if (y>_year) { 
			nd=new DateTime(_year,1,1,0,0,0,0);
		}
		_date=toMonday(nd);
		super.fireTableStructureChanged();
		//super.fireTableDataChanged();
	}
	
	public void prevWeek() {
		Duration dur=new Duration(7*24*3600*1000);
		DateTime nd=_date.minus(dur);
		int y=nd.getYear();
		if (y<_year) {
			nd=new DateTime(_year,12,31,0,0,0,0);
		}
		_date=toMonday(nd);
		super.fireTableStructureChanged();
		//super.fireTableDataChanged();
	}
	
	public void setDate(DateTime d) {
		_date=toMonday(d);
		super.fireTableStructureChanged();
	}
	
	public Date getJavaDate() {
		long tm=_date.getMillis();
		return new Date(tm);
	}
	
	public String jaarAsStr() {
		return _year.toString();
	}
	
	public Integer jaar() {
		return _year;
	}
	
	public String weekAsStr() {
		Integer wknr=_date.getWeekOfWeekyear();
		return wknr.toString();
	}
	
	public String weekDatumAsStr() {
		SimpleDateFormat f=new SimpleDateFormat("dd-MM-yyyy");
		return f.format(_date.toDate());
		//return _date.toString(f);
	}
	
	public void setYear(int year) {
		if (_dbm==null) {
			_urenJaar=null;
			super.fireTableStructureChanged();
		} else {
			try {
				_year=year;
				_urenJaar=new Jaar(_dbm,_year);
				DateTime current=new DateTime();
				if (current.getYear()!=year) {
					_date=new DateTime(year,1,1,0,0,0,0);
				} else {
					_date=new DateTime();
				}
				_date=toMonday(_date);
				super.fireTableStructureChanged();
			} catch (NDbmException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setDbm(NDbm2 dbm) {
		_dbm=dbm;
		setYear(_year);
	}

	public void wijzigJaar(Component parent,int jaar) {
		setYear(jaar);
	}

	public void toevoegenProject(Component parent) {
		NieuwProject np=new NieuwProject(_urenJaar,parent);
		np.run();
		super.fireTableStructureChanged();
	}

	public void wijzigProject(Component parent) {
		WijzigProject p=new WijzigProject(_urenJaar,parent);
		p.run();
		super.fireTableStructureChanged();
	}

	public void verwijderProject(Component parent) {
		try {
			VerwijderProject p=new VerwijderProject(_urenJaar,parent);
			p.run();
			if (p.projectToRemove()!=null) {
				_urenJaar.removeProject(p.projectToRemove());
				super.fireTableStructureChanged();
			}
		} catch (NDbmException e) {
			e.printStackTrace();
		}
	}

	public void reportWeek(Component parent) {
		try {
			UrenReport rep=new UrenReport(parent,_urenJaar,_date,UrenReport.WEEK,_dbm);
			rep.report();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void reportMonth(Component parent) {
		try {
			UrenReport rep=new UrenReport(parent,_urenJaar,_date,UrenReport.MONTH,_dbm);
			rep.report();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reportJaar(Component parent) {
		try {
			UrenReport rep=new UrenReport(parent,_urenJaar,_date,UrenReport.YEAR,_dbm);
			rep.report();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////
	
	public UrenControler(NDbm2 db) {
		_dbm=db;
		setYear(new DateTime().getYear());
	}
}
