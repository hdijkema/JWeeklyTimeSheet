package net.dijkema.urenregistratie.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;
import net.dijkema.jndbm.NDbm;
import net.dijkema.jndbm.NDbm2;
import net.dijkema.jndbm2.exceptions.NDbmException;
import net.dijkema.urenregistratie.data.Jaar;
import net.dijkema.urenregistratie.data.Project;
import net.dijkema.urenregistratie.utils.Swing;

public class NieuwProject extends JDialog {
	
	private ProjectPanel _pane;
	private Component	 _parent;
	private NDbm2		 _dbm;
	private Jaar		 _jaar;

	public void run() { 
		String prj=JOptionPane.showInputDialog(_parent,"Geef een projectnaam:","Nieuw Project",JOptionPane.QUESTION_MESSAGE);
		if (prj!=null) {
			String num=JOptionPane.showInputDialog(_parent,"Geef een projectnummer:","Nieuw Project",JOptionPane.QUESTION_MESSAGE);
			if (num!=null) {
				try {
					Project p=new Project(_dbm,num,prj);
					_pane.setProject(p);
					this.setModal(true);
					this.pack();
					Swing.centerOnParent(this,_parent);
					this.setVisible(true);
					_jaar.addProject(p);
				} catch (NDbmException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(
							_parent, 
							e.getMessage(), 
							"Problemen in de datastore", 
							JOptionPane.ERROR_MESSAGE
							);
				}
			}
		}
	}
	
	private void close() {
		this.setVisible(false);
	}
	
	public NieuwProject(Jaar jr,Component cmp) {
		_jaar=jr;
		_dbm=_jaar.db();
		_pane=new ProjectPanel();
		JPanel dlgp=new JPanel();
		dlgp.setLayout(new MigLayout("fill"));
		dlgp.add(_pane,"growx,wrap");
		dlgp.add(new JSeparator(),"growx,span,wrap");
		dlgp.add(new JButton(new AbstractAction("Ok") {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		}),"span,right,wrap");
		this.add(dlgp);
		_parent=cmp;
	}

}
