/**
 * 
 */
package com.fii.homework.QRDecomp.model;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import Jama.Matrix;
import Jama.QRDecomposition;

/**
 * @author Robert
 */
public class AutoMatrix {
    private Matrix matrix;
    private QRDecomposition qrDec;

    private ArrayList<ArrayList<Double>> qT;
    private ArrayList<ArrayList<Double>> R;
    private ArrayList<ArrayList<Double>> xQR;
    private ArrayList<ArrayList<Double>> bVector;

    private boolean singular;
    private double time;

    public AutoMatrix(ArrayList<ArrayList<Double>> matrix) {
	try {
	    this.matrix = new Matrix(getArrayFromCollection(matrix));
	} catch (NullPointerException e) {
	    Logger.getGlobal().log(Level.SEVERE, "cannot build matrix from null");
	    e.printStackTrace();
	}
    }

    public boolean solve(ArrayList<ArrayList<Double>> matrix) {
	try {
	    bVector = matrix;

	    Matrix jamaBMatrix = new Matrix(getArrayFromCollection(matrix));

	    long start = System.currentTimeMillis();

	    qrDec = this.matrix.qr();
	    Matrix sol = qrDec.solve(jamaBMatrix);

	    long stop = System.currentTimeMillis();

	    qT = getCollectionFromArray(qrDec.getQ().getArray());
	    R = getCollectionFromArray(qrDec.getR().getArray());
	    xQR = getCollectionFromArray(sol.getArray());

	    time = ((double) (stop - start)) / 1000;

	    return true;
	} catch (NullPointerException e) {
	    Logger.getGlobal().log(Level.SEVERE, "cannot build matrix from null");
	    e.printStackTrace();
	    return false;
	} catch (IllegalArgumentException e) {
	    Logger.getGlobal().log(Level.SEVERE, "matrixes with different sizes");
	    e.printStackTrace();
	    return false;
	} catch (RuntimeException e) {
	    Logger.getGlobal().log(Level.SEVERE, "matrix is singular");
	    e.printStackTrace();
	    singular = true;
	    return false;
	}
    }

    public ArrayList<ArrayList<Double>> getInverse() {
	double[][] inverse = matrix.inverse().getArray();
	return getCollectionFromArray(inverse);
    }

    public String sysToString() {
	StringBuilder sysToString = new StringBuilder("<html><table>");

	ArrayList<ArrayList<Double>> a = getCollectionFromArray(matrix.getArray());
	;
	int size = bVector.size();
	for (int ecInd = 0; ecInd < size; ecInd++) {
	    sysToString.append(ecToString(ecInd, size, a, bVector));
	}

	sysToString.append("</table></html>");
	return sysToString.toString();
    }

    public String ecToString(int ec, int size, ArrayList<ArrayList<Double>> matrix, ArrayList<ArrayList<Double>> bVector) {
	ArrayList<Double> row = matrix.get(ec);
	Double bi = bVector.get(ec).get(0);

	StringBuilder ecToString = new StringBuilder("<tr>");

	// ai*xi
	int index = 1;
	for (Double coef : row) {
	    if (coef == 0.0) {
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

	infoToString.append(solToString());
	infoToString.append(qTToString());
	infoToString.append(rToString());

	infoToString.append("</table></html>");
	return infoToString.toString();
    }

    private String solToString() {
	ArrayList<ArrayList<Double>> solutions = xQR;
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

	solToString.append("</table></tr>");
	return solToString.toString();
    }

    private String qTToString() {
	StringBuilder qTToString = new StringBuilder("<tr><table  border=\"1\">");
	// Solution
	int rows = qT.size();
	int columns = qT.get(0).size();

	qTToString.append("<tr>");
	qTToString.append("<th colspan=\"" + columns + "\">");
	qTToString.append("Q");
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

    private String rToString() {
	StringBuilder rToString = new StringBuilder("<tr><table  border=\"1\">");
	// Solution
	int rows = R.size();
	int columns = R.get(0).size();

	rToString.append("<tr>");
	rToString.append("<th colspan=\"" + columns + "\">");
	rToString.append("R");
	rToString.append("</th>");
	rToString.append("</tr>");

	// values
	for (int i = 0; i < rows; i++) {
	    rToString.append("<tr>");
	    for (int j = 0; j < columns; j++) {
		rToString.append("<td><b>" + R.get(i).get(j) + "</b></td>");
	    }
	    rToString.append("</tr>");
	}

	rToString.append("</table></tr>");
	return rToString.toString();
    }

    private ArrayList<ArrayList<Double>> getCollectionFromArray(double[][] array) {
	ArrayList<ArrayList<Double>> collection = new ArrayList<ArrayList<Double>>();

	int rows = array.length;
	int columns = array[0].length;

	for (int rowIt = 0; rowIt < rows; rowIt++) {
	    ArrayList<Double> row = new ArrayList<Double>();
	    for (int colIt = 0; colIt < columns; colIt++) {
		row.add(array[rowIt][colIt]);
	    }
	    collection.add(row);
	}
	return collection;
    }

    private double[][] getArrayFromCollection(ArrayList<ArrayList<Double>> collection) {
	int rows = collection.size();
	int columns = collection.get(0).size();
	double[][] array = new double[rows][columns];

	for (int i = 0; i < rows; i++) {
	    for (int j = 0; j < columns; j++) {
		array[i][j] = collection.get(i).get(j);
	    }
	}

	return array;
    }

    /**
     * @return the matrix
     */
    public Matrix getMatrix() {
	return matrix;
    }

    /**
     * @return the time
     */
    public double getTime() {
	return time;
    }

    /**
     * @return the solutions
     */
    public ArrayList<ArrayList<Double>> getXQR() {
	return xQR;
    }

    /**
     * @return the singular
     */
    public boolean isSingular() {
	return singular;
    }

}
