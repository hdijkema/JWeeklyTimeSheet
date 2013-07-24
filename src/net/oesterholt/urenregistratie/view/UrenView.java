package net.oesterholt.urenregistratie.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

import net.miginfocom.swing.MigLayout;
import net.oesterholt.JXTwoLevelSplitTable;
import net.oesterholt.splittable.AbstractSplitTableModel.ColumnWidthListener;
import net.oesterholt.urenregistratie.controler.UrenControler;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.decorator.AlignmentHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.FontHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.joda.time.DateTime;

public class UrenView extends JPanel {
	
	JXTwoLevelSplitTable 	_table;
	JButton				 	_volgendeWeek;
	JButton		 			_vorigeWeek;
	JXDatePicker 			_calendar;
	JSpinner		 			_jaar;
	JXLabel		 			_weekNr;
	JXLabel					_weekDatum;
	
	UrenControler 			_controler;
	
	class CellEditor implements TableCellEditor {
		
		TableCellEditor _previous;
		InputVerifier   _verifier;
		Component 		_currentComponent=null;
		int             _column=-1;
		
		public void setCol(int c) {
			_column=c;
		}
		
		public CellEditor() {
			_previous=_table.rightTable().getDefaultEditor(Object.class);
			_verifier=new InputVerifier() {
				public boolean verify(JComponent input) {
					if (input instanceof JTextField) {
						String txt=((JTextField) input).getText().trim();
						if (txt.isEmpty()) {
							return true;
						} else {
							boolean ok=txt.matches("^[0-9]*([.][0-9]+)?$");
							if (ok) {
								return _controler.verify(txt,_column+1);  // correct column to model. 
							} else {
								return false;
							}
						}
					} else {
						return false;
					}
				}
			};
		}
		
		public Component getTableCellEditorComponent(JTable t,Object v,boolean isSel,int row, int col) {
			Component c=_previous.getTableCellEditorComponent(t,v,isSel,row,col);
			if (c instanceof JTextField) {
				JTextField field=(JTextField) c;
				InputVerifier vv=field.getInputVerifier(); 
				if (vv!=_verifier) {
					field.setInputVerifier(_verifier);
				};
				setCol(col);
				field.selectAll();
			}
			_currentComponent=c;
			return c;
		}
		
		public void addCellEditorListener(CellEditorListener l) {
			_previous.addCellEditorListener(l);
		}

		public void cancelCellEditing() {
			_currentComponent=null;
			_previous.cancelCellEditing();
		}

		public Object getCellEditorValue() {
			return _previous.getCellEditorValue();
		}

		public boolean isCellEditable(EventObject e) {
			return _previous.isCellEditable(e);
		}

		public void removeCellEditorListener(CellEditorListener l) {
			_previous.removeCellEditorListener(l);
		}

		public boolean shouldSelectCell(EventObject e) {
			return _previous.shouldSelectCell(e);
		}

		public boolean stopCellEditing() {
			if (_currentComponent!=null) {
				if (_currentComponent instanceof JComponent) {
					if (_verifier.verify((JComponent) _currentComponent)) {
						return _previous.stopCellEditing();
					} else {
						return false;
					}
				} else {
					return _previous.stopCellEditing();
				}
			} else {
				return _previous.stopCellEditing();
			}
		}

	}

	public UrenView(UrenControler c) {
		_controler=c;
		_table=new JXTwoLevelSplitTable(
								"uren",
								c.model(),
								JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
								JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
								);
		_controler.model().addColumnWidthListener(new ColumnWidthListener() {
			public void prefferedWidthForColumn(int col, int width) {
				if (col==0) {
					_table.leftTable().getColumnExt(col).setPreferredWidth(width+60);
				} else {
					_table.rightTable().getColumnExt(col-1).setPreferredWidth(width);
				}
			}
		});
		_table.setEditable(true);
		_table.rightTable().setDefaultEditor(Object.class, new CellEditor());
		_table.addHighlighter(new ColorHighlighter(new HighlightPredicate() {
			public boolean isHighlighted(Component c, ComponentAdapter a) {
				return (a.column>=2 && a.column<9) && !_controler.isNodeIndex(a.row);
			}
			
		},Color.white,null),false);
		
		
		_table.addHighlighter(new AlignmentHighlighter(SwingConstants.RIGHT), false);
		_table.addHighlighter(new ColorHighlighter(new HighlightPredicate() {
			public boolean isHighlighted(Component c, ComponentAdapter a) {
				return (a.column==0 && !_controler.isNodeIndex(a.row));
			}
		},new Color(255,255,153),null), false);
		_table.addHighlighter(new ColorHighlighter(new HighlightPredicate() {
			public boolean isHighlighted(Component c,ComponentAdapter a) {
				if ((a.column==1) && !_controler.isNodeIndex(a.row)) {
					float val=(Float) _controler.getValueAt(a.row, a.column+1);
					if (val<0.0f) { return true; }
					else { return false; }
				} else {
					return false;
				}
			}
		},new Color(255,114,38),null), false);
		_table.addHighlighter(new ColorHighlighter(new HighlightPredicate() {
			public boolean isHighlighted(Component c,ComponentAdapter a) {
				if ((a.column==1) && !_controler.isNodeIndex(a.row)) {
					float val=(Float) _controler.getValueAt(a.row, a.column+1);
					if (val>=0.0f) { return true; }
					else { return false; }
				} else {
					return false;
				}
			}
		},new Color(109,242,122),null), false);
		_table.addHighlighter(new ColorHighlighter(new HighlightPredicate() {
			public boolean isHighlighted(Component c,ComponentAdapter a) {
				return a.column==10 && !_controler.isNodeIndex(a.row);
			}
		},new Color(134,132,138),Color.white),false);
		Font f=_table.getFont().deriveFont(Font.BOLD);
		_table.addHighlighter(new FontHighlighter(new HighlightPredicate() {
			public boolean isHighlighted(Component c,ComponentAdapter a) {
				return a.column==10 && !_controler.isNodeIndex(a.row);
			}
		},f),false);
		
		
		

		_volgendeWeek=new JButton("Week +");
		_volgendeWeek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_controler.nextWeek();
				_weekNr.setText(_controler.weekAsStr());
				_calendar.setDate(_controler.getJavaDate());
				_weekDatum.setText(_controler.weekDatumAsStr());
			}
		});
		_vorigeWeek=new JButton("Week -");
		_vorigeWeek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_controler.prevWeek();
				_weekNr.setText(_controler.weekAsStr());
				_calendar.setDate(_controler.getJavaDate());
				_weekDatum.setText(_controler.weekDatumAsStr());
			}
		});
		_calendar=new JXDatePicker();
		_calendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateTime dt=new DateTime(_calendar.getDate().getTime());
				_controler.setDate(dt);
				_weekNr.setText(_controler.weekAsStr());
				_weekDatum.setText(_controler.weekDatumAsStr());
			}
			
		});
		_calendar.setDate(_controler.getJavaDate());
		_jaar=new JSpinner();
		_jaar.setValue(_controler.jaar());
		_jaar.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (_jaar.getValue() instanceof Integer) {
					_controler.wijzigJaar(UrenView.this,(Integer) _jaar.getValue());
					_weekNr.setText(_controler.weekAsStr());
					_weekDatum.setText(_controler.weekDatumAsStr());
					_calendar.setDate(_controler.getJavaDate());
				}
			}
		});
		//_jaar.setText(_controler.jaarAsStr());
		_weekNr=new JXLabel();
		_weekNr.setText(_controler.weekAsStr());
		_weekDatum=new JXLabel();
		_weekDatum.setText(_controler.weekDatumAsStr());
		
		super.setLayout(new MigLayout("insets 1,fill"));
		
		JPanel navigate=new JPanel(new MigLayout("insets 0,fill"));
		{
			JPanel ff=new JPanel();
			ff.add(_calendar);
			ff.add(_vorigeWeek);
			ff.add(_volgendeWeek);
			navigate.add(ff,"left");
		}
		
		{
			JPanel ff=new JPanel();
			ff.add(new JXLabel("Jaar:"));
			ff.add(_jaar);
			ff.add(new JSeparator(JSeparator.VERTICAL));
			ff.add(new JXLabel("Week nummer:"));
			ff.add(_weekNr);
			ff.add(new JSeparator(JSeparator.VERTICAL));
			ff.add(new JXLabel("Week datum:"));
			ff.add(_weekDatum);
			navigate.add(ff,"right,wrap");
		}
		navigate.setBorder(BorderFactory.createEtchedBorder());
		
		JPanel hours=new JPanel();
		hours.setLayout(new MigLayout("insets 0,fill"));
		hours.add(_table,"growx,growy");
		
		this.add(navigate,"growx,wrap");
		this.add(_table,"growx,growy");
	}
}
