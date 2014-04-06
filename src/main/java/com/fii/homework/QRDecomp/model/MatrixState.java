/**
 * 
 */
package com.fii.homework.QRDecomp.model;

import java.util.ArrayList;

import com.fii.homework.QRDecomp.utils.MatrixUtils;

/**
 * @author Robert
 */
public class MatrixState {
    private ArrayList<ArrayList<Double>> matrix;
    private ArrayList<ArrayList<Double>> b;
    private ArrayList<ArrayList<Double>> u;
    private ArrayList<ArrayList<Double>> qT;
    private static ArrayList<ArrayList<Double>> solutions;
    private static double timeSeconds;
    private static boolean singular;

    public MatrixState(ArrayList<ArrayList<Double>> matrix, ArrayList<ArrayList<Double>> b) {
	this.matrix = MatrixUtils.getClone(matrix, Double.class);
	this.b = MatrixUtils.getClone(b, Double.class);
    }

    public MatrixState(ArrayList<ArrayList<Double>> matrix, ArrayList<ArrayList<Double>> b, ArrayList<ArrayList<Double>> u,
	    ArrayList<ArrayList<Double>> qT) {
	this(matrix, b);
	this.u = MatrixUtils.getClone(u, Double.class);
	this.qT = MatrixUtils.getClone(qT, Double.class);
    }

    /**
     * @return the matrix
     */
    public ArrayList<ArrayList<Double>> getMatrix() {
	return matrix;
    }

    /**
     * @param matrix
     *            the matrix to set
     */
    public void setMatrix(ArrayList<ArrayList<Double>> matrix) {
	this.matrix = matrix;
    }

    /**
     * @return the b
     */
    public ArrayList<ArrayList<Double>> getB() {
	return b;
    }

    /**
     * @param b
     *            the b to set
     */
    public void setB(ArrayList<ArrayList<Double>> b) {
	this.b = b;
    }

    /**
     * @return the u
     */
    public ArrayList<ArrayList<Double>> getU() {
	return u;
    }

    /**
     * @param u
     *            the u to set
     */
    public void setU(ArrayList<ArrayList<Double>> u) {
	this.u = u;
    }

    /**
     * @return the qT
     */
    public ArrayList<ArrayList<Double>> getqT() {
	return qT;
    }

    /**
     * @param qT
     *            the qT to set
     */
    public void setqT(ArrayList<ArrayList<Double>> qT) {
	this.qT = qT;
    }

    /**
     * @param solutions
     *            the solutions to set
     */
    public static void setSolutions(ArrayList<ArrayList<Double>> solutions) {
	MatrixState.solutions = MatrixUtils.getClone(solutions, Double.class);
    }

    /**
     * @return the solutions
     */
    public static ArrayList<ArrayList<Double>> getSolutions() {
	return solutions;
    }

    /**
     * @return the timeSeconds
     */
    public static double getTimeSeconds() {
	return timeSeconds;
    }

    /**
     * @param timeSeconds
     *            the timeSeconds to set
     */
    public static void setTimeSeconds(double timeSeconds) {
	MatrixState.timeSeconds = timeSeconds;
    }

    /**
     * @return the singular
     */
    public static boolean isSingular() {
	return singular;
    }

    /**
     * @param singular
     *            the singular to set
     */
    public static void setSingular(boolean singular) {
	MatrixState.singular = singular;
    }

}
