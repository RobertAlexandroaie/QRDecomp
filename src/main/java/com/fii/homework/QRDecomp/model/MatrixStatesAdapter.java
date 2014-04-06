/**
 * 
 */
package com.fii.homework.QRDecomp.model;

import java.util.ArrayList;

import com.fii.homework.QRDecomp.utils.MatrixUtils;

/**
 * @author Robert
 */
public class MatrixStatesAdapter {
    private ArrayList<MatrixState> manualMatrixStates;
    private int step;

    public MatrixStatesAdapter() {
	step = 0;
    }

    /**
     * @return the states
     */
    public ArrayList<MatrixState> getManualMatrixStates() {
	return manualMatrixStates;
    }

    /**
     * @param manualMatrixStates
     *            the states to set
     */
    public void setStates(ArrayList<MatrixState> manualMatrixStates) {
	this.manualMatrixStates = manualMatrixStates;
    }

    /**
     * @return the step
     */
    public int getStep() {
	return step;
    }

    public void next() {
	if (step == manualMatrixStates.size() - 1) {
	    step = 1;
	} else {
	    step++;
	}
    }

    public void prev() {
	if (step == 1) {
	    step = manualMatrixStates.size() - 1;
	} else {
	    step--;
	}
    }

    public String sysToString() {
	MatrixState currentState = manualMatrixStates.get(step);
	ArrayList<ArrayList<Double>> bVector = currentState.getB();

	StringBuilder sysToString = new StringBuilder("<html><table>");

	int size = bVector.size();
	for (int ec = 0; ec < size; ec++) {
	    sysToString.append(ecToString(ec, size, currentState));
	}

	sysToString.append("</table></html>");
	return sysToString.toString();
    }

    public String sysToString(MatrixState state) {
	MatrixState currentState = state;
	ArrayList<ArrayList<Double>> bVector = currentState.getB();

	StringBuilder sysToString = new StringBuilder("<html><table>");

	int size = bVector.size();
	for (int ec = 0; ec < size; ec++) {
	    sysToString.append(ecToString(ec, size, currentState));
	}

	sysToString.append("</table></html>");
	return sysToString.toString();
    }

    private String ecToString(int ec, int size, MatrixState state) {
	ArrayList<Double> row = state.getMatrix().get(ec);
	Double bi = state.getB().get(ec).get(0);

	StringBuilder ecToString = new StringBuilder("<tr>");

	// ai*xi
	int index = 1;
	for (Double coef : row) {
	    if (coef == 0.0) {
		ecToString.append("<td>");
		ecToString.append("");
		ecToString.append("</td>");
		ecToString.append("<td>");
		ecToString.append("");
		ecToString.append("</td>");
	    } else {
		// (coef
		ecToString.append("<td><b>(");
		ecToString.append(coef);

		// xi)
		ecToString.append("</b> * x" + index + "<b>)</b>");
		ecToString.append("</td>");
		// +
		ecToString.append("<td>");
		ecToString.append((index < size) ? "+ " : "");
		ecToString.append("</td>");
	    }
	    index++;
	}

	// =b
	ecToString.append("<td>");
	ecToString.append("= <b>" + bi);
	ecToString.append("</b></td>");

	ecToString.append("</tr>");
	return ecToString.toString();
    }

    public String infoToString() {
	StringBuilder infoToString = new StringBuilder("<html><table>");

	if (!MatrixState.isSingular()) {
	    infoToString.append(solToString());
	}

	infoToString.append(uToString());
	infoToString.append(qTToString());

	infoToString.append("</table></html>");
	return infoToString.toString();
    }

    public String solToString() {
	ArrayList<ArrayList<Double>> solutions = MatrixState.getSolutions();
	StringBuilder solToString = new StringBuilder("<tr><table border=\"1\">");
	// Solution
	solToString.append("<tr>");
	solToString.append("<th colspan=\"2\">");
	solToString.append("Solutii");
	solToString.append("</th>");
	solToString.append("</tr>");

	// xi=value
	for (int i = 0; i < solutions.size(); i++) {
	    solToString.append("<tr>");
	    // xi
	    solToString.append("<td><b>x" + (i + 1) + "</b></td>");

	    // value
	    solToString.append("<td>" + solutions.get(i).get(0) + "</td>");
	    solToString.append("</tr>");
	}

	solToString.append("</table></td>");
	return solToString.toString();
    }

    public String uToString() {
	ArrayList<ArrayList<Double>> u = manualMatrixStates.get(step).getU();
	StringBuilder uToString = new StringBuilder("<tr><table border=\"1\">");

	int size = u.size();

	// u
	uToString.append("<tr>");
	uToString.append("<th colspan=\"2\">");
	uToString.append("u" + step);
	uToString.append("</th>");
	uToString.append("</tr>");

	for (int i = 0; i < size; i++) {
	    // values
	    uToString.append("<tr>");
	    uToString.append(u.get(i).get(0));
	    uToString.append("</tr>");
	}

	uToString.append("</table></tr>");
	return uToString.toString();
    }

    public String qTToString() {
	ArrayList<ArrayList<Double>> qT = manualMatrixStates.get(step).getqT();
	StringBuilder qTToString = new StringBuilder("<tr><table  border=\"1\">");

	int rows = qT.size();
	int columns = qT.get(0).size();

	qTToString.append("<tr>");
	qTToString.append("<th colspan=\"" + columns + "\">");
	qTToString.append("QT " + step);
	qTToString.append("</th>");
	qTToString.append("</tr>");

	// values
	for (int i = 0; i < rows; i++) {
	    qTToString.append("<tr>");
	    for (int j = 0; j < columns; j++) {
		qTToString.append("<td><b>" + qT.get(i).get(j) + "</b></td>");
	    }
	    qTToString.append("</tr>");
	}

	qTToString.append("</table></tr>");
	return qTToString.toString();
    }

    public String eucNorms2ToString(ArrayList<ArrayList<Double>> a, ArrayList<ArrayList<Double>> b, ArrayList<ArrayList<Double>> xHh,
	    ArrayList<ArrayList<Double>> xQR) {
	StringBuilder eucNormToString = new StringBuilder("<html><table>");

	eucNormToString.append("<tr>");
	eucNormToString.append("<td>");
	eucNormToString.append("||Aini*xHh - binit||2");
	eucNormToString.append("</td>");
	eucNormToString.append(eucNorm2aToString(a, b, xHh));
	eucNormToString.append("</tr>");

	eucNormToString.append("<tr>");
	eucNormToString.append("<td>");
	eucNormToString.append("||Aini*xQR - binit||2");
	eucNormToString.append("</td>");
	eucNormToString.append(eucNorm2aToString(a, b, xQR));
	eucNormToString.append("</tr>");

	ArrayList<ArrayList<Double>> s = new ArrayList<ArrayList<Double>>();
	int size = xQR.size();
	for (int i = 0; i < size; i++) {
	    ArrayList<Double> row = new ArrayList<>();
	    row.add(1.0 * (i + 1) / 3.0);
	    s.add(row);
	}

	eucNormToString.append("<tr>");
	eucNormToString.append("<td>");
	eucNormToString.append("||xHh - s||2/||s||2");
	eucNormToString.append("</td>");
	eucNormToString.append(eucNorm2bToString(xHh, s));
	eucNormToString.append("</tr>");

	eucNormToString.append("<tr>");
	eucNormToString.append("<td>");
	eucNormToString.append("||xQR - s||2/||s||2");
	eucNormToString.append("</td>");
	eucNormToString.append(eucNorm2bToString(xQR, s));
	eucNormToString.append("</tr>");

	eucNormToString.append("</table></html>");
	return eucNormToString.toString();

    }

    /**
     * @param a
     * @param b
     * @param xHh
     * @return
     */
    private String eucNorm2aToString(ArrayList<ArrayList<Double>> a, ArrayList<ArrayList<Double>> b, ArrayList<ArrayList<Double>> sol) {
	StringBuilder normToString = new StringBuilder("<td>");
	ArrayList<ArrayList<Double>> matrix = MatrixUtils.subMatrixes(MatrixUtils.mulMatrixes(a, sol, Double.class), b, Double.class);
	Double norm = getNorm2(matrix);
	normToString.append(norm);
	normToString.append("</td>");
	return normToString.toString();
    }

    /**
     * @param xHh
     * @param s
     * @return
     */
    private String eucNorm2bToString(ArrayList<ArrayList<Double>> sol, ArrayList<ArrayList<Double>> s) {
	StringBuilder normToString = new StringBuilder("<td>");
	ArrayList<ArrayList<Double>> matrix = MatrixUtils.subMatrixes(sol, s, Double.class);
	Double nom = 0.0;
	Double denom = 0.0;

	nom = getNorm2(matrix);
	denom = getNorm2(s);

	normToString.append(nom / denom);
	normToString.append("</td>");
	return normToString.toString();
    }

    private Double getNorm2(ArrayList<ArrayList<Double>> matr) {
	Double norm = 0.0;
	int size = matr.size();
	for (int i = 0; i < size; i++) {
	    Double elem = Math.abs(matr.get(i).get(0));
	    norm += elem * elem;
	}
	norm = Math.sqrt(norm);
	return norm;
    }

    public String normToString(ArrayList<ArrayList<Double>> manInv, ArrayList<ArrayList<Double>> autoInv) {
	StringBuilder normToString = new StringBuilder("<html><table>");

	normToString.append("<tr>");
	normToString.append("<td>");
	normToString.append("||Ainv - AinvLib||2");
	normToString.append("</td>");
	normToString.append(normaToString(manInv, autoInv));
	normToString.append("</tr>");
	normToString.append("</table></html>");
	return normToString.toString();

    }

    /**
     * @param manInv
     * @param autoInv
     * @return
     */
    private String normaToString(ArrayList<ArrayList<Double>> manInv, ArrayList<ArrayList<Double>> autoInv) {
	StringBuilder normaToString = new StringBuilder("<td>");
	ArrayList<ArrayList<Double>> matrix = MatrixUtils.subMatrixes(manInv, autoInv, Double.class);

	Double norm = getNorm(matrix);
	normaToString.append(norm);
	normaToString.append("</td>");
	return normaToString.toString();

    }

    private Double getNorm(ArrayList<ArrayList<Double>> matr) {
	Double norm = Double.MIN_VALUE;
	Double sum;
	int rows = matr.size();
	int columns = matr.get(0).size();
	for (int i = 0; i < rows; i++) {
	    sum = 0.0;
	    for (int j = 0; j < columns; j++) {
		sum += matr.get(i).get(j);
	    }
	    if (sum > norm) {
		norm = sum;
	    }
	}
	norm = Math.sqrt(norm);
	return norm;
    }

    /**
     * @param step
     *            the step to set
     */
    public void setStep(int step) {
	this.step = step;
    }
}
