package net.dijkema.urenregistratie.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import net.miginfocom.swing.MigLayout;
import net.dijkema.jndbm2.exceptions.NDbmException;
import net.dijkema.urenregistratie.data.Kostensoort;
import net.dijkema.urenregistratie.data.Project;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXTextField;

public class ProjectPanel extends JPanel {
	
	class ProjectComboModel implements MutableComboBoxModel {

		public Object getSelectedItem() {
			return _selected;
		}

		public void setSelectedItem(Object anItem) {
			if (anItem instanceof Kostensoort) {
				_selected=(Kostensoort) anItem;
			} else {
				
			}
		}

		public Object getElementAt(int index) {
			try {
				return _project.kostensoort(index);
			} catch (NDbmException e) {
				// TODO Display a message
				e.printStackTrace();
				return null;
			}
		}

		public int getSize() {
			if (_project==null) {
				return 0;
			} else {
				return _project.nKostensoorten();
			}
		}

		public void addElement(Object obj) {
			try {
				if (obj instanceof String) {
					_project.addKostensoort((String) obj);
					int N=_project.nKostensoorten();
					int index=N-1;
					Iterator<ListDataListener> it=set.iterator();
					while (it.hasNext()) {
						it.next().intervalAdded(
								new ListDataEvent(this,
										ListDataEvent.INTERVAL_ADDED,
										index,
										index+1
								)
						);
					}
				}
			} catch (NDbmException e) {
					// TODO: display message
					e.printStackTrace();
				}
		}
		
		public Kostensoort addKostensoort(String ksrt) {
			addElement(ksrt);
			try {
				return _project.kostensoort(_project.nKostensoorten()-1);
			} catch (NDbmException e) {
				e.printStackTrace();
				return null;
			}
		}

		public void insertElementAt(Object obj, int index) {
			try {
				if (obj instanceof String) {
					_project.insertKostensoort((String) obj,index);
					Iterator<ListDataListener> it=set.iterator();
					while (it.hasNext()) {
						it.next().intervalAdded(
								new ListDataEvent(this,
										ListDataEvent.INTERVAL_ADDED,
										index,
										index
								)
						);
					}
				}
			} catch (Exception E) {
				E.printStackTrace();
			}
		}

		public void removeElement(Object obj) {
			try {
				if (obj instanceof Kostensoort) {
					int removedIndex=_project.delKostensoort((Kostensoort) obj);
					if (removedIndex<0) { return; }
					Iterator<ListDataListener> it=set.iterator();
					while (it.hasNext()) {
						it.next().intervalRemoved(
								new ListDataEvent(
										this,
										ListDataEvent.INTERVAL_REMOVED,
										removedIndex,
										removedIndex
								)
						);
					}
				}
			} catch (Exception E) {
				E.printStackTrace();
			}
		}

		public void removeElementAt(int index) {
		}
		
		private Set<ListDataListener> set=new HashSet<ListDataListener>();

		public void addListDataListener(ListDataListener l) {
			set.add(l);
		}

		public void removeListDataListener(ListDataListener l) {
			set.remove(l);
		}
		
	}
	
	private Project     		_project;
	private Kostensoort 		_selected;
	private JXTextField 		w_project;
	private JXTextField			w_nksrt;
	private JComboBox   		w_kostensoorten;
	private ProjectComboModel   _model;
	
	private void addKsrt(String k) {
		if (k.equals("")) {
			return;
		} else {
			Kostensoort q=_model.addKostensoort(k);
			w_kostensoorten.getEditor().setItem(q);
			w_kostensoorten.setSelectedItem(q);
			w_nksrt.setText("");
		}
	}
	
	private void removeKsrt(String k) {
		_model.removeElement(k);;
	}
	
	private void changeSelectedKsrt(Kostensoort K) {
		try {
			//System.out.println(w_kostensoorten.getEditor());
			//System.out.println(w_kostensoorten.getEditor().getItem());
			Object v=w_kostensoorten.getEditor().getItem();
			if (v!=null) {
				if (v instanceof String) {
					String newvalue=(String) v; 
					newvalue=newvalue.trim();
					if (!newvalue.equals("")) {
						K.setNaam(newvalue);
					}
				}
			}
		} catch (NDbmException e) {
			e.printStackTrace();
		}
	}
	
	private void changeProjectName(String p) {
		try {
			String nprj=w_project.getText().trim();
			if (!nprj.equals("")) {
				_project.setNaam(nprj);
			}
		} catch (NDbmException e) {
			e.printStackTrace();
		}
	}
	
	private void init() {
		_selected=null;
		w_project=new JXTextField();
		_model=new ProjectComboModel();
		w_kostensoorten=new JComboBox(_model);
		w_kostensoorten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println(e);
				//System.out.println(w_kostensoorten.getSelectedItem());
				//System.out.println(w_kostensoorten.getSelectedItem().getClass());
				changeSelectedKsrt((Kostensoort) w_kostensoorten.getSelectedItem());
			}
		});
		w_nksrt=new JXTextField();
		this.setLayout(new MigLayout("fill"));
		this.add(new JXLabel("Project:"));
		this.add(w_project,"wmin 300,growx");
		this.add(new JButton(new AbstractAction("Wijzig naam") {
			public void actionPerformed(ActionEvent e) {
				changeProjectName(w_project.getText().trim());
			}
		}),"growx,span,wrap");
		this.add(new JXLabel("Kostensoorten:"));
		this.add(w_kostensoorten,"growx");
		this.add(new JButton(new AbstractAction("Wijzig") {
			public void actionPerformed(ActionEvent e) {
				changeSelectedKsrt((Kostensoort) w_kostensoorten.getSelectedItem());
			}
		}));
		this.add(new JButton(new AbstractAction("Verwijder"){ 
			public void actionPerformed(ActionEvent e) {
				Kostensoort q=(Kostensoort) w_kostensoorten.getSelectedItem();
				if (q==null) {
					JOptionPane.showMessageDialog(
							ProjectPanel.this, 
							"Om een kostensoort te verwijderen dient u er een te selecteren",
							"Verwijderen kostensoort",
							JOptionPane.ERROR_MESSAGE
							);
				} else {
					_model.removeElement(w_kostensoorten.getSelectedItem());
					_model.setSelectedItem(null);
					w_kostensoorten.getEditor().setItem(null);
				}
			}
		}),"wrap");
		this.add(new JXLabel("Nieuwe kostensoort:"));
		this.add(w_nksrt,"wmin 200,growx");
		this.add(new JButton(new AbstractAction("Aanmaken") {
			public void actionPerformed(ActionEvent arg0) {
				addKsrt(w_nksrt.getText().trim());
			}
		}),"growx,span,wrap");
		w_kostensoorten.setEditable(true);
	}
	
	public void setProject(Project p) {
		_project=p;
		w_project.setText(_project.getNaam());
		_model=new ProjectComboModel();
		w_kostensoorten.setModel(_model);
		if (_project.nKostensoorten()>0) {
			w_kostensoorten.setSelectedIndex(0);
			w_kostensoorten.getEditor().setItem(w_kostensoorten.getItemAt(0));
		}
	}
	
	public ProjectPanel() {
		_project=null;
		init();
	}
	
	public ProjectPanel(Project p) {
		_project=p;
		init();
	}
	

}
