package com.gudigundla.UI;

import java.awt.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;
import com.gudigundla.jena.*;

//VS4E -- DO NOT REMOVE THIS LINE!
public class UIFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JComboBox speciesLeftCB;
	private JComboBox speciesRightCB;
	private JButton addButton;
	private JLabel jLabel1;
	private JTextArea speciesTextArea;
	private JButton jButton1;
	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	private RdfGenerator rdfGen;
	
	public UIFrame() throws Exception {
		initComponents();
	}

	private void initComponents() throws Exception {
		setLayout(new GroupLayout());
		add(getJComboBox0(), new Constraints(new Leading(18, 10, 10), new Leading(24, 10, 10)));
		add(getJLabel1(), new Constraints(new Leading(221, 10, 10), new Leading(29, 12, 10, 10)));
		add(getJComboBox1(), new Constraints(new Leading(297, 10, 10), new Leading(24, 12, 12)));
		add(getJButton0(), new Constraints(new Trailing(12, 500, 500), new Leading(23, 12, 12)));
		add(getJTextArea0(), new Constraints(new Bilateral(19, 12, 22), new Leading(66, 202, 10, 10)));
		add(getJButton1(), new Constraints(new Trailing(12, 12, 12), new Trailing(12, 100, 280)));
		setSize(587, 321);
		rdfGen = new RdfGenerator();
	}

	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("DONE");
			ActionListener clickListener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					getFrames()[0].dispose();
					SearchUI frame = null ;
					try {
						rdfGen.saveFile();
						rdfGen.createInferredModel();
						frame = new SearchUI(rdfGen);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					frame.getContentPane().setPreferredSize(frame.getSize());
					frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				}
			};
			jButton1.addActionListener(clickListener );
		}
		return jButton1;
	}

	private JTextArea getJTextArea0() {
		if (speciesTextArea == null) {
			speciesTextArea = new JTextArea();
			//speciesTextArea.setText("species Hierarchy");
			speciesTextArea.setEditable(false);
			//speciesTextArea.disable()
		}
		return speciesTextArea;
	}

	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("is parent of");
		}
		return jLabel1;
	}

	private JButton getJButton0() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.setText("ADD");
			ActionListener addButtonListener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Species leftCB = (Species)speciesLeftCB.getSelectedItem();
					Species rightCB = (Species)speciesRightCB.getSelectedItem();
					speciesTextArea.append("\n"+leftCB.getSname()+" is parent of "+rightCB.getSname());
					rdfGen.updateModel((Species)speciesLeftCB.getSelectedItem(), (Species)speciesRightCB.getSelectedItem());
				}
			};
			addButton.addActionListener(addButtonListener );
		}
		return addButton;
	}

	private JComboBox getJComboBox1() throws IOException {
		if (speciesRightCB == null) {
			//speciesRightCB = new JComboBox();
			//speciesRightCB = new JComboBox(new PopulateUIComboBoxes().populateCBs());
			speciesRightCB = new JComboBox(new learningSPARQL().getSpeciesList());
			//speciesRightCB.setModel(new DefaultComboBoxModel(new Object[] { "item0", "item1", "item2", "item3" }));
			speciesRightCB.setDoubleBuffered(false);
			speciesRightCB.setBorder(null);
		}
		return speciesRightCB;
	}

	private JComboBox getJComboBox0() throws Exception {
		if (speciesLeftCB == null) {
		//	speciesLeftCB = new JComboBox();
			//speciesLeftCB = new JComboBox(new PopulateUIComboBoxes().populateCBs());
			speciesLeftCB = new JComboBox(new learningSPARQL().getSpeciesList());
		//	speciesLeftCB.setModel(new DefaultComboBoxModel(new Object[] { "item0", "item1", "item2", "item3" }));
			//speciesLeftCB.setModel()
			speciesLeftCB.setDoubleBuffered(false);
			speciesLeftCB.setBorder(null);
		}
		return speciesLeftCB;
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
				UIFrame frame = null;
				try {
					frame = new UIFrame();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.setDefaultCloseOperation(UIFrame.EXIT_ON_CLOSE);
				frame.setTitle("UIFrame");
				frame.getContentPane().setPreferredSize(frame.getSize());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			}
		});
	}

}
