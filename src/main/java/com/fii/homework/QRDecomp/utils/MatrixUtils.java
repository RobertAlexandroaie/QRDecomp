/**
 * 
 */
package com.fii.homework.QRDecomp.utils;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Robert
 */
public class MatrixUtils {

    // neutrals
    @SuppressWarnings("unchecked")
    private static <T extends Number> T getNeutralSumElem(Class<T> type) {
	switch (type.getSimpleName()) {
	case "Double":
	    return (T) (new Double(0.0));
	case "Float":
	    return (T) (new Float(0.0));
	case "Byte":
	    return (T) (new Byte((byte) 0));
	case "Short":
	    return (T) (new Short((short) 0));
	case "Integer":
	    return (T) (new Integer(0));
	case "Long":
	    return (T) (new Long(0));
	}
	return null;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number> T getNeutralMulElem(Class<T> type) {
	switch (type.getSimpleName()) {
	case "Double":
	    return (T) (new Double(1.0));
	case "Float":
	    return (T) (new Float(1.0));
	case "Byte":
	    return (T) (new Byte((byte) 1));
	case "Short":
	    return (T) (new Short((short) 1));
	case "Integer":
	    return (T) (new Integer(1));
	case "Long":
	    return (T) (new Long(1));
	}
	return null;
    }

    public static <T extends Number> ArrayList<ArrayList<T>> getIn(int n, Class<T> type) {
	ArrayList<ArrayList<T>> In = new ArrayList<ArrayList<T>>();
	for (int i = 0; i < n; i++) {
	    ArrayList<T> row = new ArrayList<T>();
	    for (int j = 0; j < n; j++) {
		if (i != j) {
		    row.add(getNeutralSumElem(type));
		} else {
		    row.add(getNeutralMulElem(type));
		}
	    }
	    In.add(row);
	}
	return In;
    }

    // element operations
    @SuppressWarnings("unchecked")
    private static <T extends Number> T sumElements(T e1, T e2, Class<T> type) {
	T result = getNeutralMulElem(type);
	switch (type.getSimpleName()) {
	case "Byte":
	    return (T) (Byte) (byte) ((Byte) e1 + (Byte) e2);
	case "Short":
	    return (T) (Short) (short) ((Short) e1 + (Short) e2);
	case "Integer":
	    return (T) (Integer) ((Integer) e1 + (Integer) e2);
	case "Long":
	    return (T) (Long) ((Long) e1 + (Long) e2);
	case "Float":
	    return (T) (Float) ((Float) e1 + (Float) e2);
	case "Double":
	    return (T) (Double) ((Double) e1 + (Double) e2);
	default:
	    return result;
	}
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number> T subsElements(T e1, T e2, Class<T> type) {
	T result = getNeutralMulElem(type);
	switch (type.getSimpleName()) {
	case "Byte":
	    return (T) (Byte) (byte) ((Byte) e1 - (Byte) e2);
	case "Short":
	    return (T) (Short) (short) ((Short) e1 - (Short) e2);
	case "Integer":
	    return (T) (Integer) ((Integer) e1 - (Integer) e2);
	case "Long":
	    return (T) (Long) ((Long) e1 - (Long) e2);
	case "Float":
	    return (T) (Float) ((Float) e1 - (Float) e2);
	case "Double":
	    return (T) (Double) ((Double) e1 - (Double) e2);
	default:
	    return result;
	}
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number> T mulElements(T e1, T e2, Class<T> type) {
	T result = getNeutralMulElem(type);
	switch (type.getSimpleName()) {
	case "Byte":
	    return (T) (Byte) (byte) ((Byte) e1 * (Byte) e2);
	case "Short":
	    return (T) (Short) (short) ((Short) e1 * (Short) e2);
	case "Integer":
	    return (T) (Integer) ((Integer) e1 * (Integer) e2);
	case "Long":
	    return (T) (Long) ((Long) e1 * (Long) e2);
	case "Float":
	    return (T) (Float) ((Float) e1 * (Float) e2);
	case "Double":
	    return (T) (Double) ((Double) e1 * (Double) e2);
	default:
	    return result;
	}
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number> T divElements(T e1, T e2, Class<T> type, double eps) {
	if (Math.abs((Double) e2) > eps) {
	    T result = getNeutralMulElem(type);
	    switch (type.getSimpleName()) {
	    case "Byte":
		return (T) (Byte) (byte) ((Byte) e1 / (Byte) e2);
	    case "Short":
		return (T) (Short) (short) ((Short) e1 / (Short) e2);
	    case "Integer":
		return (T) (Integer) ((Integer) e1 / (Integer) e2);
	    case "Long":
		return (T) (Long) ((Long) e1 / (Long) e2);
	    case "Float":
		return (T) (Float) ((Float) e1 / (Float) e2);
	    case "Double":
		return (T) (Double) ((Double) e1 / (Double) e2);
	    default:
		return result;
	    }
	} else {
	    return getNeutralMulElem(type);
	}
    }

    // matrix operations

    public static <T extends Number> ArrayList<ArrayList<T>> multScalarWithMatrix(T scalar, ArrayList<ArrayList<T>> a, Class<T> type) {
	ArrayList<ArrayList<T>> result = new ArrayList<ArrayList<T>>();
	try {
	    if (a == null) {
		throw new NullPointerException();
	    }
	    int rows = a.size();
	    int columns = a.get(0).size();

	    for (int i = 0; i < rows; i++) {
		ArrayList<T> row = new ArrayList<>();
		for (int j = 0; j < columns; j++) {
		    T elem = mulElements(scalar, a.get(i).get(j), type);
		    row.add(elem);
		}
		result.add(row);
	    }
	    return result;
	} catch (NullPointerException e) {
	    Logger.getGlobal().log(Level.SEVERE, "cannot multiply null matrixes");
	    e.printStackTrace();
	    return null;
	} catch (IndexOutOfBoundsException e) {
	    Logger.getGlobal().log(Level.SEVERE, "check your indexes when multiplying matrixes");
	    e.printStackTrace();
	    return null;
	}
    }

    public static <T extends Number> ArrayList<ArrayList<T>> mulMatrixes(ArrayList<ArrayList<T>> a, ArrayList<ArrayList<T>> b, Class<T> type) {
	ArrayList<ArrayList<T>> result = new ArrayList<ArrayList<T>>();
	try {
	    if (a == null || b == null) {
		throw new NullPointerException();
	    }
	    int rows = a.size();
	    int columns = b.get(0).size();
	    int common = a.get(0).size();

	    for (int i = 0; i < rows; i++) {
		ArrayList<T> row = new ArrayList<>();
		for (int j = 0; j < columns; j++) {
		    T elem = getNeutralSumElem(type);
		    for (int k = 0; k < common; k++) {
			elem = sumElements(elem, mulElements(a.get(i).get(k), b.get(k).get(j), type), type);
		    }
		    row.add(elem);
		}
		result.add(row);
	    }
	} catch (NullPointerException e) {
	    Logger.getGlobal().log(Level.SEVERE, "cannot multiply null matrixes");
	    e.printStackTrace();
	    return null;
	} catch (IndexOutOfBoundsException e) {
	    Logger.getGlobal().log(Level.SEVERE, "check your indexes when multiplying matrixes");
	    e.printStackTrace();
	    return null;
	}
	return result;
    }

    public static <T extends Number> ArrayList<ArrayList<T>> subMatrixes(ArrayList<ArrayList<T>> a, ArrayList<ArrayList<T>> b, Class<T> type) {
	try {
	    ArrayList<ArrayList<T>> result = new ArrayList<ArrayList<T>>();
	    if (a.size() != b.size() && a.get(0).size() != b.get(0).size()) {
		throw new IllegalArgumentException();
	    }

	    for (int i = 0; i < a.size(); i++) {
		ArrayList<T> resultRow = new ArrayList<T>();
		ArrayList<T> aRow = a.get(i);
		ArrayList<T> bRow = b.get(i);
		for (int j = 0; j < a.get(0).size(); j++) {
		    resultRow.add(subsElements(aRow.get(j), bRow.get(j), type));
		}
		result.add(resultRow);
	    }
	    return result;
	} catch (NullPointerException e) {
	    Logger.getGlobal().log(Level.SEVERE, "cannot extract from null matrix!");
	    e.printStackTrace();
	    return null;
	} catch (IllegalArgumentException e) {
	    Logger.getGlobal().log(Level.SEVERE, "matrixes have different sizes!");
	    e.printStackTrace();
	    return null;
	} catch (IndexOutOfBoundsException e) {
	    Logger.getGlobal().log(Level.SEVERE, "check indexes!");
	    e.printStackTrace();
	    return null;
	}
    }

    // utils

    public static <T extends Number> ArrayList<ArrayList<T>> getTranspose(ArrayList<ArrayList<T>> a, Class<T> type) {
	try {
	    int rows = a.size();
	    int columns = a.get(0).size();

	    ArrayList<ArrayList<T>> transpose = new ArrayList<ArrayList<T>>();
	    for (int i = 0; i < columns; i++) {
		ArrayList<T> row = new ArrayList<>();
		for (int j = 0; j < rows; j++) {
		    row.add(a.get(j).get(i));
		}
		transpose.add(row);
	    }
	    return transpose;
	} catch (NullPointerException e) {
	    Logger.getGlobal().log(Level.SEVERE, "cannot transpose null matrix!");
	    e.printStackTrace();
	    return null;
	}
    }

    public static <T extends Number> ArrayList<ArrayList<T>> solve(ArrayList<ArrayList<T>> a, ArrayList<ArrayList<T>> bVector, double eps,
	    Class<T> type) {
	try {
	    if (verifySingularity(a, eps)) {
		int size = a.size();
		ArrayList<ArrayList<T>> solution = new ArrayList<ArrayList<T>>();
		for (int i = 0; i < size; i++) {
		    solution.add(new ArrayList<T>());
		}
		for (int i = size - 1; i >= 0; i--) {
		    T sum = bVector.get(i).get(0);
		    for (int j = i + 1; j < size; j++) {
			T value = a.get(i).get(j);
			T x = solution.get(j).get(0);
			sum = subsElements(sum, mulElements(value, x, type), type);
		    }
		    solution.get(i).add(divElements(sum, a.get(i).get(i), type, eps));
		}

		// this.checkSolution();
		return solution;
	    } else {
		return null;
	    }
	} catch (NullPointerException e) {
	    Logger.getGlobal().log(Level.SEVERE, "something went wrong solving the sys!");
	    e.printStackTrace();
	    return null;
	} catch (IndexOutOfBoundsException e) {
	    Logger.getGlobal().log(Level.SEVERE, "check your indexes!");
	    e.printStackTrace();
	    return null;
	}
    }

    public static <T extends Number> boolean verifySingularity(ArrayList<ArrayList<T>> matrix, double eps) {
	boolean isSingular = true;
	int rows = matrix.size();
	for (int i = 0; i < rows; i++) {
	    if (Math.abs((Double) matrix.get(i).get(i)) <= eps) {
		isSingular = false;
	    }
	}
	return isSingular;
    }

    public static <T extends Number> ArrayList<ArrayList<T>> getInverse(ArrayList<ArrayList<T>> QT, ArrayList<ArrayList<T>> R, double eps,
	    Class<T> type) {
	try {
	    ArrayList<ArrayList<T>> inverse = new ArrayList<ArrayList<T>>();
	    ArrayList<ArrayList<T>> solCol;
	    int size = QT.size();

	    for (int i = 0; i < size; i++) {
		ArrayList<T> row = new ArrayList<T>();
		inverse.add(row);
	    }

	    for (int col = 0; col < size; col++) {
		ArrayList<ArrayList<T>> bAsMatrix = new ArrayList<ArrayList<T>>();
		for (int row = 0; row < size; row++) {
		    ArrayList<T> rowInB = new ArrayList<T>();
		    rowInB.add(QT.get(row).get(col));
		    bAsMatrix.add(rowInB);
		}
		solCol = solve(R, bAsMatrix, eps, type);

		for (int i = 0; i < size; i++) {
		    inverse.get(i).add(solCol.get(i).get(0));
		}
	    }

	    return inverse;
	} catch (NullPointerException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static <T extends Number> ArrayList<ArrayList<T>> getClone(ArrayList<ArrayList<T>> matrix, Class<T> type) {
	ArrayList<ArrayList<T>> clone = new ArrayList<ArrayList<T>>();
	for (ArrayList<T> row : matrix) {
	    clone.add(new ArrayList<T>(row));
	}
	return clone;
    }
}
