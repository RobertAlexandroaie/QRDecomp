/**
 * 
 */
package com.fii.homework.QRDecomp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.fii.homework.QRDecomp.model.AutoMatrix;
import com.fii.homework.QRDecomp.model.ManualMatrix;
import com.fii.homework.QRDecomp.model.MatrixState;
import com.fii.homework.QRDecomp.model.MatrixStatesAdapter;
import com.fii.homework.QRDecomp.model.StateManager;
import com.fii.homework.QRDecomp.utils.MatrixUtils;

/**
 * @author Robert
 */
public class MainFrame extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tFdN;
    private JTextField tFdEps;
    private ManualMatrix manMatrix;
    private AutoMatrix autoMatrix;
    private MatrixStatesAdapter mmsa;
    private boolean manualFaster = false;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    MainFrame frame = new MainFrame();
		    frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    private boolean printManInfo(JPanel pnl, JLabel lblMatr, JLabel lblInfo) {
	StringBuilder title = new StringBuilder();
	TitledBorder border = (TitledBorder) pnl.getBorder();

	title.append(MatrixState.getTimeSeconds());
	title.append("---Pas " + mmsa.getStep());
	if (border != null) {
	    Color cl = manualFaster ? Color.green : Color.red;
	    border.setTitleColor(cl);
	    border.setTitle(title.toString());
	}
	lblMatr.setText(mmsa.sysToString());
	lblInfo.setText(mmsa.infoToString());

	pnl.repaint();
	return true;
    }

    private boolean printAutoInfo(JPanel pnl, JLabel lblMatr, JLabel lblInfo) {
	StringBuilder title = new StringBuilder();
	TitledBorder border = (TitledBorder) pnl.getBorder();

	title.append(autoMatrix.getTime());
	if (border != null) {
	    Color cl = manualFaster ? Color.red : Color.green;
	    border.setTitleColor(cl);
	    border.setTitle(title.toString());
	}
	lblMatr.setText(autoMatrix.sysToString());
	lblInfo.setText(autoMatrix.infoToString());

	pnl.repaint();
	return true;
    }

    /**
     * Create the frame.
     */
    public MainFrame() {
	mmsa = new MatrixStatesAdapter();
	setBounds(new Rectangle(0, 0, 0, 30));
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 726, 359);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	contentPane.setLayout(new BorderLayout(0, 0));

	final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	contentPane.add(tabbedPane, BorderLayout.CENTER);

	JPanel pnlInput = new JPanel();
	pnlInput.setBorder(new TitledBorder(null, "Matricea", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
	tabbedPane.addTab("Input", null, pnlInput, null);
	pnlInput.setLayout(new BorderLayout(0, 0));

	JScrollPane scrlPnInput = new JScrollPane();
	pnlInput.add(scrlPnInput);

	final JLabel lblInput = new JLabel("");
	scrlPnInput.setViewportView(lblInput);

	JPanel pnlInputBtn = new JPanel();
	pnlInputBtn.setAlignmentY(Component.TOP_ALIGNMENT);
	pnlInputBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
	pnlInput.add(pnlInputBtn, BorderLayout.SOUTH);
	pnlInputBtn.setLayout(new MigLayout("", "[grow,fill][grow]", "[][grow,fill]"));

	tFdN = new JTextField();
	tFdN.setToolTipText("Numarul de linii si coloane");
	tFdN.setText("5");
	pnlInputBtn.add(tFdN, "cell 0 0,growx");
	tFdN.setColumns(10);

	tFdEps = new JTextField();
	tFdEps.setToolTipText("Modulul puterii numarului epsilon.");
	tFdEps.setText("7");
	pnlInputBtn.add(tFdEps, "cell 1 0,growx");
	tFdEps.setColumns(10);

	JButton btnGenMatrix = new JButton("Genereaza aleator");
	btnGenMatrix.setAlignmentY(Component.TOP_ALIGNMENT);
	btnGenMatrix.setMinimumSize(new Dimension(89, 30));
	btnGenMatrix.setPreferredSize(new Dimension(89, 30));
	pnlInputBtn.add(btnGenMatrix, "cell 0 1 ,grow");

	JButton btnTest = new JButton("Test");
	pnlInputBtn.add(btnTest, "cell 1 1, grow");

	final JPanel pnlManual = new JPanel();
	pnlManual.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Time", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null,
		null));
	pnlManual.setAlignmentY(Component.TOP_ALIGNMENT);
	pnlManual.setAlignmentX(Component.LEFT_ALIGNMENT);
	tabbedPane.addTab("Manual", null, pnlManual, null);
	pnlManual.setLayout(new BorderLayout(0, 0));

	final JSplitPane sPnManualContainer = new JSplitPane();
	sPnManualContainer.setBorder(null);
	sPnManualContainer.setDividerLocation(0.8);
	sPnManualContainer.setDividerSize(2);
	sPnManualContainer.setResizeWeight(0.8);
	pnlManual.add(sPnManualContainer, BorderLayout.CENTER);

	JPanel pnlManualMatrix = new JPanel();
	sPnManualContainer.setLeftComponent(pnlManualMatrix);
	pnlManualMatrix.setLayout(new BorderLayout(0, 0));

	JScrollPane scrlPnManualMatrix = new JScrollPane();
	pnlManualMatrix.add(scrlPnManualMatrix, BorderLayout.CENTER);

	final JLabel lblManualMatrix = new JLabel("");
	scrlPnManualMatrix.setViewportView(lblManualMatrix);

	JPanel pnlManualInfo = new JPanel();
	sPnManualContainer.setRightComponent(pnlManualInfo);
	pnlManualInfo.setLayout(new BorderLayout(0, 0));

	JScrollPane scrlPnManualInfo = new JScrollPane();
	pnlManualInfo.add(scrlPnManualInfo, BorderLayout.CENTER);

	final JLabel lblManualInfo = new JLabel("");
	scrlPnManualInfo.setViewportView(lblManualInfo);

	JPanel pnlManualBtn = new JPanel();
	pnlManualBtn.setAlignmentY(Component.TOP_ALIGNMENT);
	pnlManualBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
	pnlManual.add(pnlManualBtn, BorderLayout.SOUTH);
	pnlManualBtn.setLayout(new MigLayout("", "[grow,fill][grow,fill]", "[grow,fill]"));

	JButton btnPrev = new JButton("Precedent");
	btnPrev.setPreferredSize(new Dimension(89, 30));
	btnPrev.setAlignmentY(Component.TOP_ALIGNMENT);
	pnlManualBtn.add(btnPrev, "cell 0 0,grow");

	JButton btnNext = new JButton("Urmator");
	btnNext.setPreferredSize(new Dimension(89, 30));
	btnNext.setAlignmentY(Component.TOP_ALIGNMENT);
	pnlManualBtn.add(btnNext, "cell 1 0,grow");

	final JPanel pnlAuto = new JPanel();
	pnlAuto.setBorder(new TitledBorder(null, "Time", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
	tabbedPane.addTab("Automated", null, pnlAuto, null);
	pnlAuto.setLayout(new BorderLayout(0, 0));

	JSplitPane splitPane = new JSplitPane();
	splitPane.setResizeWeight(0.8);
	splitPane.setDividerSize(2);
	splitPane.setDividerLocation(0.8);
	splitPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	splitPane.setBorder(null);
	pnlAuto.add(splitPane, BorderLayout.CENTER);

	JPanel pnlAutoMatrix = new JPanel();
	splitPane.setLeftComponent(pnlAutoMatrix);
	pnlAutoMatrix.setLayout(new BorderLayout(0, 0));

	JScrollPane scrlPnAutoMatrix = new JScrollPane();
	pnlAutoMatrix.add(scrlPnAutoMatrix, BorderLayout.CENTER);

	final JLabel lblAutoMatrix = new JLabel("");
	scrlPnAutoMatrix.setViewportView(lblAutoMatrix);

	JPanel pnlAutoInfo = new JPanel();
	splitPane.setRightComponent(pnlAutoInfo);
	pnlAutoInfo.setLayout(new BorderLayout(0, 0));

	JScrollPane scrlPnAutoInfo = new JScrollPane();
	pnlAutoInfo.add(scrlPnAutoInfo, BorderLayout.CENTER);

	final JLabel lblAutoInfo = new JLabel("");
	scrlPnAutoInfo.setViewportView(lblAutoInfo);
	splitPane.setDividerLocation(0.8);

	JPanel pnlEucNorm = new JPanel();
	tabbedPane.addTab("Norme euclidiene", null, pnlEucNorm, null);
	pnlEucNorm.setLayout(new BorderLayout(0, 0));

	JScrollPane scrlPnEucNorm = new JScrollPane();
	pnlEucNorm.add(scrlPnEucNorm, BorderLayout.CENTER);

	final JLabel lblEuNorm = new JLabel("");
	scrlPnEucNorm.setViewportView(lblEuNorm);

	JPanel pnlNorm = new JPanel();
	tabbedPane.addTab("Norma", null, pnlNorm, null);
	pnlNorm.setLayout(new BorderLayout(0, 0));

	JScrollPane scrlPnNorm = new JScrollPane();
	pnlNorm.add(scrlPnNorm, BorderLayout.CENTER);

	final JLabel lblNorm = new JLabel("");
	scrlPnNorm.setViewportView(lblNorm);

	tabbedPane.setEnabledAt(1, false);
	tabbedPane.setEnabledAt(2, false);
	tabbedPane.setEnabledAt(3, false);
	tabbedPane.setEnabledAt(4, false);

	btnGenMatrix.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		try {
		    int n = Integer.parseInt(tFdN.getText());
		    int epsPower = Integer.parseInt(tFdEps.getText());
		    manMatrix = new ManualMatrix(n, epsPower);

		    ArrayList<ArrayList<Double>> clone = MatrixUtils.getClone(manMatrix.getInitialMatrix(), Double.class);
		    autoMatrix = new AutoMatrix(clone);

		    if (manMatrix.HouseholderQRDecomp() && autoMatrix.solve(manMatrix.getbInit())) {
			mmsa.setStates(StateManager.getStates());
			if (MatrixState.getTimeSeconds() < autoMatrix.getTime()) {
			    manualFaster = true;
			}
			tabbedPane.setEnabledAt(1, true);
			tabbedPane.setEnabledAt(2, true);
			tabbedPane.setEnabledAt(3, true);
			tabbedPane.setEnabledAt(4, true);
			lblInput.setText(mmsa.sysToString());
		    } else if (manMatrix.isSingular() || autoMatrix.isSingular()) {
			JOptionPane.showMessageDialog(MainFrame.this, "Matrice singulara!");
		    }
		} catch (NumberFormatException e) {
		    e.printStackTrace();
		    JOptionPane.showMessageDialog(MainFrame.this, "Verificati ca valorile din campuri sa fie numere!");
		}
	    }
	});

	btnTest.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		try {
		    ArrayList<ArrayList<Double>> testMatrix = new ArrayList<ArrayList<Double>>();

		    ArrayList<Double> row = new ArrayList<Double>();
		    row.add(new Double(80));
		    row.add(new Double(96));
		    row.add(new Double(77));
		    testMatrix.add(row);

		    row = new ArrayList<>();
		    row.add(new Double(60));
		    row.add(new Double(-53));
		    row.add(new Double(-36));
		    testMatrix.add(row);

		    row = new ArrayList<>();
		    // row.add(new Double(-75));
		    // row.add(new Double(60));
		    // row.add(new Double(-155));
		    row.add(new Double(60));
		    row.add(new Double(-53));
		    row.add(new Double(-36));
		    testMatrix.add(row);

		    manMatrix = new ManualMatrix(testMatrix);
		    ArrayList<ArrayList<Double>> matrixClone = MatrixUtils.getClone(manMatrix.getInitialMatrix(), Double.class);
		    ArrayList<ArrayList<Double>> bVectClone = MatrixUtils.getClone(manMatrix.getbInit(), Double.class);
		    autoMatrix = new AutoMatrix(matrixClone);

		    if (manMatrix.HouseholderQRDecomp() && autoMatrix.solve(bVectClone)) {
			mmsa.setStates(StateManager.getStates());
			if (MatrixState.getTimeSeconds() < autoMatrix.getTime()) {
			    manualFaster = true;
			}
			tabbedPane.setEnabledAt(1, true);
			tabbedPane.setEnabledAt(2, true);
			tabbedPane.setEnabledAt(3, true);
			tabbedPane.setEnabledAt(4, true);
			lblInput.setText(mmsa.sysToString());
		    } else if (manMatrix.isSingular() || autoMatrix.isSingular()) {
			JOptionPane.showMessageDialog(MainFrame.this, "Matrice singulara!");
		    }
		} catch (NumberFormatException ex) {
		    ex.printStackTrace();
		    JOptionPane.showMessageDialog(MainFrame.this, "Verificati ca valorile din campuri sa fie numere!");
		}

	    }
	});

	tabbedPane.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		int tabIndex = tabbedPane.getSelectedIndex();

		switch (tabIndex) {
		case 0:
		    mmsa.setStep(0);
		    break;
		case 1:
		    if (MatrixState.isSingular()) {
			JOptionPane.showMessageDialog(MainFrame.this, "Matrice singulara!");
		    }
		    if (mmsa.getStep() == 0) {
			mmsa.setStep(1);
		    }
		    printManInfo(pnlManual, lblManualMatrix, lblManualInfo);
		    break;
		case 2:
		    printAutoInfo(pnlAuto, lblAutoMatrix, lblAutoInfo);
		    break;
		case 3:
		    lblEuNorm.setText(mmsa.eucNorms2ToString(manMatrix.getInitialMatrix(), manMatrix.getbInit(), MatrixState.getSolutions(),
			    autoMatrix.getXQR()));
		    break;
		case 4:
		    lblNorm.setText(mmsa.normToString(manMatrix.getInverse(), autoMatrix.getInverse()));
		    break;
		}
		// pnlCoef.repaint();
	    }
	});

	btnPrev.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		mmsa.prev();
		printManInfo(pnlManual, lblManualMatrix, lblManualInfo);
	    }
	});
	btnNext.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		mmsa.next();
		printManInfo(pnlManual, lblManualMatrix, lblManualInfo);
	    }
	});
    }
}
