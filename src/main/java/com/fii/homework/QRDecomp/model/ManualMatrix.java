/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.fii.homework.QRDecomp.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fii.homework.QRDecomp.utils.MatrixUtils;

/**
 * @author Robert
 */
public class ManualMatrix {

    private int size;
    private ArrayList<ArrayList<Double>> initialMatrix;
    private ArrayList<ArrayList<Double>> matrix;

    private ArrayList<ArrayList<Double>> initialB;
    private ArrayList<ArrayList<Double>> b;

    private ArrayList<ArrayList<Double>> Pstep;
    private ArrayList<ArrayList<Double>> qT;
    private ArrayList<ArrayList<Double>> u;

    private ArrayList<ArrayList<Double>> xHouseholder;

    private double eps;
    private boolean singular = true;

    // construction

    public ManualMatrix(ArrayList<ArrayList<Double>> matrix) {
	size = matrix.size();
	this.eps = Math.pow(10, -8);
	this.matrix = MatrixUtils.getClone(matrix, Double.class);
	if (buildBVector(size) && generateBVector(size)) {
	    Logger.getGlobal().log(Level.INFO, "Matrix and b vector succesfully built.");
	    if (storeInitialValues()) {
		Logger.getGlobal().log(Level.INFO, "Storing initial values was successful.");
	    }
	}
    }

    public ManualMatrix(int n, int p) {
	size = n;
	this.eps = Math.pow(10, -p);
	if (buildMatrix(n) && generateRandomMatrix() && buildBVector(n) && generateBVector(n)) {
	    Logger.getGlobal().log(Level.INFO, "Matrix and b vector succesfully built.");
	    if (storeInitialValues()) {
		Logger.getGlobal().log(Level.INFO, "Storing initial values was successful.");
	    }
	}
    }

    private boolean buildMatrix(int n) {
	try {
	    matrix = new ArrayList<ArrayList<Double>>();
	    for (int i = 0; i < size; i++) {
		matrix.add(new ArrayList<Double>());
		for (int j = 0; j < size; j++) {
		    matrix.get(i).add(null);
		}
	    }
	} catch (Exception e) {
	    Logger.getGlobal().log(Level.SEVERE, "something went wrong with building the matrix");
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    private boolean buildBVector(int n) {
	try {
	    b = new ArrayList<ArrayList<Double>>();
	    for (int i = 0; i < n; i++) {
		ArrayList<Double> row = new ArrayList<Double>();
		b.add(row);
	    }
	} catch (Exception e) {
	    Logger.getGlobal().log(Level.SEVERE, "something went wrong with b vector building!");
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    private boolean generateRandomMatrix() {
	try {
	    Random rand = new Random();

	    for (ArrayList<Double> row : matrix) {
		for (int i = 0; i < size; i++) {
		    row.set(i, rand.nextDouble());
		}
	    }
	} catch (Exception e) {
	    Logger.getGlobal().log(Level.SEVERE, "something went wrong with random matrix gen!");
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    private boolean generateBVector(int n) {
	try {
	    Double sum;
	    for (int i = 0; i < n; i++) {
		ArrayList<Double> row = new ArrayList<Double>();
		sum = 0.0;
		for (int j = 0; j < n; j++) {
		    sum += j * getElement(i, j);
		}
		sum /= 3;
		row.add(sum);
		b.set(i, row);
	    }
	} catch (Exception e) {
	    Logger.getGlobal().log(Level.SEVERE, "something went wrong with b vector gen!");
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    private boolean storeInitialValues() {
	try {
	    initialMatrix = new ArrayList<ArrayList<Double>>();
	    initialB = new ArrayList<ArrayList<Double>>();
	    for (int i = 0; i < size; i++) {
		ArrayList<Double> bRow = new ArrayList<>(b.get(i));
		ArrayList<Double> matrixRow = new ArrayList<Double>(matrix.get(i));
		initialMatrix.add(matrixRow);
		initialB.add(bRow);
	    }
	    StateManager.addState(new MatrixState(initialMatrix, initialB));
	} catch (Exception e) {
	    Logger.getGlobal().log(Level.SEVERE, "something went wrong with storring initial values of matrix and b vector!");
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    // QRDecomp

    public boolean HouseholderQRDecomp() {
	matrix = MatrixUtils.getClone(initialMatrix, Double.class);
	b = MatrixUtils.getClone(initialB, Double.class);
	try {
	    long interStop;
	    long interStart;
	    long stop;

	    int step;
	    long start = System.currentTimeMillis();
	    ArrayList<ArrayList<Double>> In = MatrixUtils.getIn(size, Double.class);
	    qT = MatrixUtils.getClone(In, Double.class);
	    for (step = 0; step < size - 1; step++) {
		double sigma = computeSigma(step);
		if (Math.abs(sigma) <= eps) {
		    Logger.getGlobal().log(Level.INFO, "singular matrix");
		    singular = true;
		    return false;
		} else {
		    double k = computeK(step, sigma);
		    double beta = computeBeta(step, sigma, k);

		    if (buildU() && generateU(step, k)) {
			Pstep = computePstep(In, beta);
			matrix = MatrixUtils.mulMatrixes(Pstep, matrix, Double.class);
			b = MatrixUtils.mulMatrixes(Pstep, b, Double.class);
			qT = MatrixUtils.mulMatrixes(Pstep, qT, Double.class);
			interStop = System.currentTimeMillis();

			MatrixState newState = new MatrixState(matrix, b, u, qT);
			StateManager.addState(newState);
			interStart = System.currentTimeMillis();
			start += (interStart - interStop);
		    }
		}
	    }
	    if (MatrixUtils.verifySingularity(matrix, eps)) {
		solveSystem(b);
		singular = false;
	    } else {
		singular = true;
	    }
	    stop = System.currentTimeMillis();
	    StateManager.setSingular(singular);
	    double time = ((double) (stop - start)) / 1000;
	    StateManager.setTime(time);
	    return true;
	} catch (Exception e) {
	    Logger.getGlobal().log(Level.SEVERE, "something went wrong with HQRDecomp!");
	    e.printStackTrace();
	    return false;
	}
    }

    private double computeSigma(int step) {
	double alpha = 0.0;
	for (int i = step; i < size; i++) {
	    double element = getElement(i, step).doubleValue();
	    alpha += element * element;
	}
	return alpha;
    }

    private double computeK(int step, double sigma) {
	double k = 1.0;

	if (getElement(step, step) >= 0) {
	    k *= -1.0;
	}
	k *= Math.sqrt(sigma);
	return k;
    }

    private double computeBeta(int step, double sigma, double k) {
	double beta = sigma;
	beta -= k * getElement(step, step);
	return beta;
    }

    private ArrayList<ArrayList<Double>> computePstep(ArrayList<ArrayList<Double>> In, double beta) {
	ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
	for (int i = 0; i < size; i++) {
	    ArrayList<Double> row = new ArrayList<Double>(In.get(i));
	    result.add(row);
	}

	ArrayList<ArrayList<Double>> uT = MatrixUtils.getTranspose(u, Double.class);
	ArrayList<ArrayList<Double>> uxuT = MatrixUtils.mulMatrixes(u, uT, Double.class);
	ArrayList<ArrayList<Double>> interm = MatrixUtils.multScalarWithMatrix(1.0 / beta, uxuT, Double.class);
	result = MatrixUtils.subMatrixes(In, interm, Double.class);

	return result;
    }

    private boolean buildU() {
	try {
	    u = new ArrayList<ArrayList<Double>>();
	    for (int i = 0; i < size; i++) {
		u.add(new ArrayList<Double>());
	    }
	} catch (Exception e) {
	    Logger.getGlobal().log(Level.SEVERE, "something went wrong building u vector");
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    private boolean generateU(int step, double k) {
	try {
	    for (int i = 0; i < step; i++) {
		ArrayList<Double> row = new ArrayList<Double>();
		row.add(0.0);
		u.set(i, row);
	    }
	    ArrayList<Double> row = new ArrayList<Double>();
	    row.add(getElement(step, step) - k);
	    u.set(step, row);
	    for (int i = step + 1; i < size; i++) {
		row = new ArrayList<Double>();
		row.add(getElement(i, step));
		u.set(i, row);
	    }
	} catch (Exception e) {
	    Logger.getGlobal().log(Level.SEVERE, "something went wrong generating u vector");
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    public Double getElement(int i, int j) {
	return matrix.get(i).get(j);
    }

    // Solving

    public boolean solveSystem(ArrayList<ArrayList<Double>> bVector) {
	xHouseholder = MatrixUtils.solve(matrix, bVector, eps, Double.class);
	if (xHouseholder != null) {
	    MatrixState.setSolutions(xHouseholder);
	    return true;
	} else {
	    return false;
	}
    }

    // Inverse

    public ArrayList<ArrayList<Double>> getInverse() {
	ArrayList<ArrayList<Double>> inverse = MatrixUtils.getInverse(qT, matrix, eps, Double.class);
	return inverse;
    }

    // Getters&Setters

    /**
     * @return the size
     */
    public int getSize() {
	return size;
    }

    /**
     * @return the initialMatrix
     */
    public ArrayList<ArrayList<Double>> getInitialMatrix() {
	return initialMatrix;
    }

    /**
     * @return the matrix
     */
    public ArrayList<ArrayList<Double>> getMatrix() {
	return matrix;
    }

    /**
     * @return the bInit
     */
    public ArrayList<ArrayList<Double>> getbInit() {
	return initialB;
    }

    /**
     * @return the bVector
     */
    public ArrayList<ArrayList<Double>> getbVector() {
	return b;
    }

    /**
     * @return the solutions
     */
    public ArrayList<ArrayList<Double>> getSolutions() {
	return xHouseholder;
    }

    /**
     * @return the eps
     */
    public double getEps() {
	return eps;
    }

    /**
     * @return the singular
     */
    public boolean isSingular() {
	return singular;
    }

}
