package com.gudigundla.UI;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;

import com.gudigundla.jena.RdfGenerator;
import com.gudigundla.jena.Species;
import com.gudigundla.jena.learningSPARQL;
import com.hp.hpl.jena.query.ResultSet;
import com.toedter.calendar.JDateChooser;

//VS4E -- DO NOT REMOVE THIS LINE!
public class SearchUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel jLabel0;
	private JLabel jLabel1;
	private JTextField minLongTextField;
	private JTextField minLatTextField;
	private JTextField maxLatTextField;
	private JTextField maxLongTextField;
	private JLabel jLabel2;
	private JComboBox speciesComboBox;
	private JLabel jLabel3;
	private RdfGenerator rdfGen;
	private JTable jTable0;
	private JScrollPane jScrollPane1;
	private JDateChooser fromDateChooser;
	private JDateChooser toDateChooser;
	
	private ActionListener searchButtonListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Species species = (Species) speciesComboBox.getSelectedItem();
			species.setMinLatitude(Float.parseFloat(minLatTextField.getText()));
			species.setMinLongitude(Float.parseFloat(minLongTextField.getText()));
			species.setMaxLatitude(Float.parseFloat(maxLatTextField.getText()));
			species.setMaxLongitude(Float.parseFloat(maxLongTextField.getText()));
			species.setFrom(fromDateChooser.getCalendar());
			species.setTo(toDateChooser.getCalendar());
		
			ResultSet resultSet = rdfGen.getDrsFromSpecies(species);
			jTable0.setModel(new ResultTableModel(resultSet,species));
		}
	};
	private JButton searchButton;
	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	
	public SearchUI(RdfGenerator rdfGen) throws IOException {
		initComponents();
		this.rdfGen = rdfGen;
	}

	private void initComponents() throws IOException {
		setLayout(new GroupLayout());
		add(getSpeciesComboBox(), new Constraints(new Leading(15, 10, 10), new Leading(53, 10, 10)));
		add(getJLabel3(), new Constraints(new Leading(17, 10, 10), new Leading(32, 12, 12)));
		add(getJLabel2(), new Constraints(new Leading(480, 10, 10), new Leading(37, 12, 12)));
		add(getJLabel0(), new Constraints(new Leading(231, 12, 12), new Leading(37, 12, 12)));
		add(getJLabel1(), new Constraints(new Leading(231, 12, 12), new Leading(59, 12, 12)));
		add(getMinLatTextField(), new Constraints(new Leading(341, 62, 12, 12), new Leading(37, 12, 12)));
		add(getMaxLatTextField(), new Constraints(new Leading(341, 62, 12, 12), new Leading(59, 12, 12)));
		add(getMinLongTextField(), new Constraints(new Leading(409, 64, 12, 12), new Leading(37, 12, 12)));
		add(getMaxLongTextField(), new Constraints(new Leading(410, 62, 12, 12), new Leading(60, 12, 12)));
//		add(getJTextField4(), new Constraints(new Leading(584, 62, 12, 12), new Leading(37, 12, 12)));
		add(getFromDateChooser(), new Constraints(new Leading(550, 110, 12, 12), new Leading(37, 12, 12)));
//		add(getJTextField5(), new Constraints(new Leading(584, 62, 12, 12), new Leading(60, 12, 12)));
		add(getToDateChooser(), new Constraints(new Leading(550, 110, 12, 12), new Leading(60, 12, 12)));
		add(getSearchButton(), new Constraints(new Leading(667, 10, 10), new Leading(37, 37, 10, 10)));
		add(getJScrollPane1(), new Constraints(new Leading(17, 727, 10, 10), new Leading(103, 10, 23)));
		setSize(778, 285);
	}

	private JButton getSearchButton() {
		if (searchButton == null) {
			searchButton = new JButton();
			searchButton.setText("Search");
			searchButton.addActionListener(searchButtonListener);
		}
		return searchButton;
	}
	
	private JDateChooser getFromDateChooser(){
		if(fromDateChooser ==null) {
			fromDateChooser = new JDateChooser();
			Calendar calendar = Calendar.getInstance();
			calendar.set(1880, 0, 0);
			fromDateChooser.setCalendar(calendar);
		}
		return fromDateChooser;
	}

	private JDateChooser getToDateChooser(){
		if(toDateChooser ==null) {
			toDateChooser = new JDateChooser();
			toDateChooser.setDate(new Date());
		}
		return toDateChooser;
	}

	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJTable0());
		}
		return jScrollPane1;
	}

	private JTable getJTable0() {
		if (jTable0 == null) {
			jTable0 = new JTable();
			jTable0.setModel(new DefaultTableModel(new Object[][] { { "", "", }, { "", "", }, }, new String[] { "Default", "Text", }) {
				private static final long serialVersionUID = 1L;
				Class<?>[] types = new Class<?>[] { Object.class, Object.class, };
	
				public Class<?> getColumnClass(int columnIndex) {
					return types[columnIndex];
				}
			});
		}
		return jTable0;
	}

	private JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("Species");
		}
		return jLabel3;
	}

	private JComboBox getSpeciesComboBox() throws IOException {
		if (speciesComboBox == null) {
			speciesComboBox = new JComboBox(new learningSPARQL().getSpeciesList());
			speciesComboBox.setDoubleBuffered(false);
			speciesComboBox.setBorder(null);
		}
		return speciesComboBox;
	}

	private JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("time range");
		}
		return jLabel2;
	}

	private JTextField getMaxLongTextField() {
		if (maxLongTextField == null) {
			maxLongTextField = new JTextField();
			maxLongTextField.setText("-1");
		}
		return maxLongTextField;
	}

	private JTextField getMaxLatTextField() {
		if (maxLatTextField == null) {
			maxLatTextField = new JTextField();
			maxLatTextField.setText("500");
		}
		return maxLatTextField;
	}

	private JTextField getMinLatTextField() {
		if (minLatTextField == null) {
			minLatTextField = new JTextField();
			minLatTextField.setText("1");
		}
		return minLatTextField;
	}

	private JTextField getMinLongTextField() {
		if (minLongTextField == null) {
			minLongTextField = new JTextField();
			minLongTextField.setText("-500");
		}
		return minLongTextField;
	}

	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("max lat, max long");
		}
		return jLabel1;
	}

	private JLabel getJLabel0() {
		if (jLabel0 == null) {
			jLabel0 = new JLabel();
			jLabel0.setText("min lat, min long");
		}
		return jLabel0;
	}

	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
					+ " on this platform:" + e.getMessage());
		}
	}

	/**
	 * Main entry of the class.
	 * Note: This class is only created so that you can easily preview the result at runtime.
	 * It is not expected to be managed by the designer.
	 * You can modify it as you like.
	 */
	public static void main(String[] args) {
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SearchUI frame = null;
				RdfGenerator rdfGen =null;
				try {
					rdfGen = new RdfGenerator();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					frame = new SearchUI(rdfGen);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.setDefaultCloseOperation(SearchUI.EXIT_ON_CLOSE);
				frame.setTitle("SearchUI");
				frame.getContentPane().setPreferredSize(frame.getSize());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			}
		});
	}

	public SearchUI() throws IOException {
		initComponents();
	}

}
