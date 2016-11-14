package net.dijkema.urenregistratie.main;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import net.dijkema.jndbm.NDbm;

public class JWeeklyTimeSheet {
	
	public static String version() {
		return "1.2";
	}
	
	public static void setIconImage(Window w) {
		URL url=JWeeklyTimeSheet.class.getResource("/net/dijkema/urenregistratie/resources/icon_timesheet.png");
		ImageIcon icon = new ImageIcon(url);
		w.setIconImage(icon.getImage());
	}
	
	public static ImageIcon toolBarIcon(String name) {
		URL url=JWeeklyTimeSheet.class.getResource(
					String.format("/net/dijkema/urenregistratie/resources/%s.png",name)
					);
		return new ImageIcon(
					new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)
					);
	}
	
	public static JButton toolBarAction(final String action,final ActionListener l) {
    	JButton b=new JButton(new AbstractAction(action,JWeeklyTimeSheet.toolBarIcon(action)) {
    		public void actionPerformed(ActionEvent e) {
    			ActionEvent E=new ActionEvent(e.getSource(), e.getID(), action);
				l.actionPerformed(E);
    		}
    	});
    	b.setFocusable(false);
    	b.setHideActionText(true);
    	return b;
    }
	
	public static JMenuItem menu(final String action, final String txt,ActionListener l) {
		JMenuItem mnu=new JMenuItem(txt);
		mnu.addActionListener(l);
		mnu.setActionCommand(action);
		return mnu;
	}
	
	public static String getLastFile() {
		Preferences prefs=Preferences.userNodeForPackage(JWeeklyTimeSheet.class);
		String lastfile=prefs.get("lastfile", null);
		return lastfile;
	}

	public static String getLastPath() {
		Preferences prefs=Preferences.userNodeForPackage(JWeeklyTimeSheet.class);
		String lastfile=prefs.get("lastpath", ".");
		return lastfile;
	}
	
	public static void setLastFile(String f) {
		Preferences prefs=Preferences.userNodeForPackage(JWeeklyTimeSheet.class);
		prefs.put("lastfile",f);
	}
	
	public static void setWindowPosition(Point where,Dimension size) {
		Preferences prefs=Preferences.userNodeForPackage(JWeeklyTimeSheet.class);
		prefs.putInt("wx", where.x);
		prefs.putInt("wy", where.y);
		prefs.putInt("width", size.width);
		prefs.putInt("height", size.height);
	}
	
	public static Point getPrevWindowLocation() {
		Preferences prefs=Preferences.userNodeForPackage(JWeeklyTimeSheet.class);
		int x=prefs.getInt("wx", -1);
		int y=prefs.getInt("wy", -1);
		if (x<0 || y<0) {
			return null;
		} else {
			return new Point(x,y);
		}
	}
	
	public static Dimension getPrevWindowSize() {
		Preferences prefs=Preferences.userNodeForPackage(JWeeklyTimeSheet.class);
		int w=prefs.getInt("width", -1);
		int h=prefs.getInt("height", -1);
		if (w<0 || h<0) {
			return null;
		} else {
			return new Dimension(w,h);
		}
	}
	
	public static void setLastPath(String f) {
		Preferences prefs=Preferences.userNodeForPackage(JWeeklyTimeSheet.class);
		prefs.put("lastpath",f);
	}
	
	public static Vector<String> getRecentlyUsed() {
		Preferences prefs=Preferences.userNodeForPackage(JWeeklyTimeSheet.class);
		Integer i;
		Vector<String> r=new Vector<String>();
		for(i=0;i<5;i++) {
			String key="recently_"+i;
			String f=prefs.get(key, null);
			if (f!=null) {
				r.add(f);
			}
		}
		return r;
	}
	
	public static void setRecentlyUsed(Vector<String> v) {
		Preferences prefs=Preferences.userNodeForPackage(JWeeklyTimeSheet.class);
		Integer i;
		int N=Math.max(v.size(), 5);
		for(i=0;i<v.size();i++) {
			String key="recently_"+i;
			prefs.put(key, v.get(i));
		}
		for(;i<5;i++) {
			String key="recently_"+i;
			prefs.remove(key);
		}
	}
	
	public static void main(String argv[]) {
		UrenWindow u=new UrenWindow(getLastFile());
		SwingUtilities.invokeLater(u);
	} 

}
