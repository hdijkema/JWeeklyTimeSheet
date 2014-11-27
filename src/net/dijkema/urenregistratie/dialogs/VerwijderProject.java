package net.dijkema.urenregistratie.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import net.dijkema.urenregistratie.data.Jaar;
import net.dijkema.urenregistratie.data.Project;

public class VerwijderProject {
	
	Jaar		_jaar;
	Project		_toRemove;
	Component   _parent;

	public Project projectToRemove() {
		return _toRemove;
	}
	
	public void run() {
		_toRemove=null;
		Project [] p=new Project[_jaar.nProjects()];
		for(int i=0;i<_jaar.nProjects();i++) {
			p[i]=_jaar.project(i);
		}
		Project pp=(Project) JOptionPane.showInputDialog(
				_parent, 
				"Kies het te verwijderen project",
				"Verwijderen project",
				JOptionPane.QUESTION_MESSAGE,
				null,
				p,
				p[0]
				  );
		if (pp==null) {
			cancel();
		} else {
			verwijderProject(pp);
		}
	}
	
	private void verwijderProject(Project p) {
		if (p!=null) {
			if (JOptionPane.showConfirmDialog(
					_parent, 
					"Weet u zeker dat u dit project wilt verwijderen?\n" +
					"Alle bijbehorende kostensoorten en uren zullen worden verwijderd!", 
					"Verwijderen project", 
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE
			)==JOptionPane.YES_OPTION) {
				_toRemove=p;
			}
		} else {
			JOptionPane.showMessageDialog(
					_parent, 
					"U dient een project te selecteren om te verwijderen",
					"Verwijderen project",
					JOptionPane.ERROR_MESSAGE
					);
			_toRemove=null;
		}
	}
	
	private void cancel() {
		_toRemove=null;
	}
	
	public VerwijderProject(Jaar jr,Component parent) {
		_jaar=jr;
		_parent=parent;
	}
	
}
