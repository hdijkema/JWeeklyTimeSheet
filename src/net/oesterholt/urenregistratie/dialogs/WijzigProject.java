package net.oesterholt.urenregistratie.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;
import net.oesterholt.jndbm.NDbm;
import net.oesterholt.jndbm.NDbm2;
import net.oesterholt.urenregistratie.data.Jaar;
import net.oesterholt.urenregistratie.data.Project;
import net.oesterholt.urenregistratie.utils.Swing;

public class WijzigProject extends JDialog {
	private ProjectPanel _pane;
	private Component	 _parent;
	private NDbm2		 _dbm;
	private Jaar		 _jaar;

	public void run() {
		if (_jaar.nProjects()==0) {
			return;
		} else {
			Project [] p=new Project[_jaar.nProjects()];
			for(int i=0;i<_jaar.nProjects();i++) {
				p[i]=_jaar.project(i);
			}
			Project pp=(Project) JOptionPane.showInputDialog(
					_parent, 
					"Kies het te wijzigen project",
					"Wijzigen project",
					JOptionPane.QUESTION_MESSAGE,
					null,
					p,
					p[0]
					  );
			if (pp==null) {
				return;
			} else {
				_pane.setProject(pp);
				this.setModal(true);
				this.pack();
				Swing.centerOnParent(this,_parent);
				this.setVisible(true);
			}
		}
	}
	
	private void close() {
		this.setVisible(false);
	}
	
	public WijzigProject(Jaar jr,Component cmp) {
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
