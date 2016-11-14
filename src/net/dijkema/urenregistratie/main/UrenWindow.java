package net.dijkema.urenregistratie.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;
import net.dijkema.jndbm.NDbm;
import net.dijkema.jndbm.NDbm2;
import net.dijkema.jndbm2.exceptions.NDbmException;
import nl.dykema.jxmlnote.widgets.JRecentlyUsedMenu;
import net.dijkema.urenregistratie.controler.UrenControler;
import net.dijkema.urenregistratie.utils.Swing;
import net.dijkema.urenregistratie.view.UrenView;

public class UrenWindow implements Runnable, ActionListener {
	
	private String			VERSION = "1.2";
	private JFrame 			_frame;
	private JMenuBar 		_menu;
	
	private JRecentlyUsedMenu _recently;

	private UrenControler 	_controler;
	private UrenView		_view;
	private NDbm2			_dbm;
	
	private String			_bestandsnaam;
	
	class MySep extends JSeparator {
		public MySep() {
			super(JSeparator.VERTICAL);
			Dimension size = new Dimension(
				    			super.getPreferredSize().width,
				    			super.getMaximumSize().height
				    		);
			super.setMaximumSize(size);
		}
	}
	
	private void einde() {
		try {
			_dbm.close();
		} catch (NDbmException e) {
			e.printStackTrace();
		}
		JWeeklyTimeSheet.setLastFile(_bestandsnaam);
		JWeeklyTimeSheet.setWindowPosition(_frame.getLocation(),_frame.getSize());
		_frame.setVisible(false);
		System.exit(0);
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd=e.getActionCommand();
		if ("quit".equals(cmd)) {
			einde();
		} else if ("addproject".equals(cmd)) {
			_controler.toevoegenProject(_frame);
		} else if ("chgproject".equals(cmd)) {
			_controler.wijzigProject(_frame);
		} else if ("delproject".equals(cmd)) {
			_controler.verwijderProject(_frame);
		} else if ("weekstaat".equals(cmd)) {
			_controler.reportWeek(_frame);
		} else if ("maandstaat".equals(cmd)) {
			_controler.reportMonth(_frame);
		} else if ("jaarstaat".equals(cmd)) {
			_controler.reportJaar(_frame);
		}
	}
	
	private void setBestandsnaam(String bn) {
		_bestandsnaam=bn;
		if (_frame != null) { _frame.setTitle("Uren Registratie v" + VERSION + " - " + bn); }
	}
	
	public UrenWindow(String bn) {
		setBestandsnaam(bn);
	}
	
	public void openDbm(String file) {
		try {
			_dbm.close();
			File bs=new File(file);
			_dbm=NDbm2.openNDbm( bs, false);
			_controler.setDbm(_dbm);
			_recently.addRecentUse(file);
		} catch (NDbmException e) {
			e.printStackTrace();
		}
	}
	
	public String openFile(Component parent) {
		File lastpath=new File(JWeeklyTimeSheet.getLastPath());
		JFileChooser chooser = new JFileChooser(lastpath);
		chooser.setDialogTitle("Openen of aanmaken uren registratie bestand");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Uren bestanden", "db");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showDialog(parent,"Openen of aanmaken");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			lastpath=chooser.getCurrentDirectory();
			JWeeklyTimeSheet.setLastPath(lastpath.getAbsolutePath());
			File file = chooser.getSelectedFile();
			String fl = file.getAbsolutePath();
			return fl;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("serial")
	public void run() {

		// Look and feel
		
		try {
			Swing.scaleToScreen(UIManager.getSystemLookAndFeelClassName());
			//Swing.scaleToScreen("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	    } catch(Exception e) {
	    	try {
	    		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    	} catch (Exception e1) {
	    		// 	ignore exception
	    	}
	    }

		// Start with a frame
	    _frame=new JFrame("Uren registratie");
	    setBestandsnaam(_bestandsnaam);
	    
	    // menu
	    {
	    	_menu=new JMenuBar();
	    	
	    	JMenu bestand=new JMenu("Bestand");
	    	//bestand.add(new JMenuItem(new AbstractAction("Wijzig jaar") {
			//	public void actionPerformed(ActionEvent e) {
			//		//_controler.wijzigJaar();
			//	}
	    	//}));
	    	
	    	bestand.add(new JMenuItem(new AbstractAction("Nieuw uren Bestand") {
	    		public void actionPerformed(ActionEvent e) {
	    			String bestand=openFile(_frame);
	    			if (bestand!=null) {
	    				openDbm(bestand);
	    				_controler.setDbm(_dbm);
	    			}
	    		}
	    	}));
	    	
	    	_recently=new JRecentlyUsedMenu(
	    			"Recent geopende uren bestanden", 
	    			new JRecentlyUsedMenu.RecentlyUsedProvider() {

				public String clearListText() {
					return "Lijst leegmaken";
				}

				public Vector<String> getList() {
					return JWeeklyTimeSheet.getRecentlyUsed();
				}

				public int getMaxCount() {
					return 5;
				}

				public void putList(Vector<String> v) {
					JWeeklyTimeSheet.setRecentlyUsed(v);
				}
	    		
	    	}, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String cmd=e.getActionCommand();
					if (cmd.startsWith("recent:")) {
						String fl=cmd.substring(7);
						openDbm(fl);
						setBestandsnaam(fl);
						_controler.setDbm(_dbm);
					}
				}
	    	});
	    	
	    	bestand.add(_recently);
	    	
	    	bestand.add(new JSeparator());
	    	bestand.add(JWeeklyTimeSheet.menu("quit","Beëindigen",this));
	    	_menu.add(bestand);
	    	
	    	JMenu project=new JMenu("Project");
	    	project.add(JWeeklyTimeSheet.menu("addproject", "Toevoegen project", this));
	    	project.add(JWeeklyTimeSheet.menu("chgproject", "Wijzigen project", this));
	    	project.add(JWeeklyTimeSheet.menu("delproject", "Verwijder project", this));
	    	_menu.add(project);
	    	
	    	JMenu rapport=new JMenu("Overzichten");
	    	rapport.add(JWeeklyTimeSheet.menu("weekstaat", "Weekstaat", this));
	    	rapport.add(JWeeklyTimeSheet.menu("maandstaat", "Maand overzicht", this));
	    	rapport.add(JWeeklyTimeSheet.menu("jaarstaat", "Jaar overzicht", this));
	    	_menu.add(rapport);
	    	
	    }
	    
	    if (_bestandsnaam==null) {
	    	_bestandsnaam=openFile(null);
	    	if (_bestandsnaam==null) {
	    		JOptionPane.showMessageDialog(
	    				null,
	    				"Geen uren bestand opgegeven. Het programma stopt.",
	    				"Uren Registratie",
	    				JOptionPane.INFORMATION_MESSAGE
	    				);
	    		System.exit(0);
	    	}
	    } 
	    
	    try {
	    	_dbm=NDbm2.openNDbm(new File(_bestandsnaam), false);
	    	_recently.addRecentUse(_bestandsnaam);
	    } catch (NDbmException e) {
	    	e.printStackTrace();
	    	_dbm=null;
	    }
	    _controler=new UrenControler(_dbm);
	    _view=new UrenView(_controler);
	    
	    JToolBar bar=new JToolBar();
	    
	    bar.add(JWeeklyTimeSheet.toolBarAction("quit",this));
	    
	    bar.add(new MySep());
	    bar.add(JWeeklyTimeSheet.toolBarAction("addproject", this));
	    bar.add(JWeeklyTimeSheet.toolBarAction("chgproject", this));
	    bar.add(JWeeklyTimeSheet.toolBarAction("delproject", this));
	    
	    bar.add(new MySep());
	    bar.add(JWeeklyTimeSheet.toolBarAction("weekstaat", this));
	    bar.add(JWeeklyTimeSheet.toolBarAction("maandstaat", this));
	    bar.add(JWeeklyTimeSheet.toolBarAction("jaarstaat", this));
	    
	    bar.setFloatable(false);
	    bar.setFocusable(false);
	    
	    bar.add(Box.createHorizontalGlue());
	    
	    bar.setBorder(BorderFactory.createEtchedBorder());

	    _frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    _frame.addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent e) {
	    		einde();
	    	} 
	    });
	    JWeeklyTimeSheet.setIconImage(_frame);
	    _frame.setJMenuBar(_menu);
	    {
	    	JPanel p=new JPanel(new MigLayout("fill"));;
	    	p.add(bar,"north,growx,wrap");
	    	p.add(_view,"growx,growy");
	    	_frame.add(p);
	    }
	    Point loc=JWeeklyTimeSheet.getPrevWindowLocation();
	    Dimension size=JWeeklyTimeSheet.getPrevWindowSize();
	    Dimension ssize = Toolkit.getDefaultToolkit().getScreenSize(); 
	    if (size!=null) { _frame.setPreferredSize(size); }
	    _frame.pack();
	    if (loc!=null) {
	    	if (loc.x > ssize.width - 50) { loc.x = 100; }
	    	if (loc.y > ssize.height - 50) { loc.y = 100; }  
	    	_frame.setLocation(loc); 
	    }
	    _frame.setVisible(true);
	}
	

}
