/**
 * 
 */
package com.fii.homework.QRDecomp.model;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Robert
 */
public class StateManager {
    private static ArrayList<MatrixState> states;
    static {
	states = new ArrayList<MatrixState>();
    }

    public static boolean addState(MatrixState state) {
	try {
	    if (state == null) {
		throw new NullPointerException();
	    }
	    return states.add(state);
	} catch (NullPointerException e) {
	    Logger.getGlobal().log(Level.SEVERE, "cannot add null state");
	    e.printStackTrace();
	    return false;
	}
    }

    /**
     * @return the states
     */
    public static ArrayList<MatrixState> getStates() {
	return states;
    }

    public static void setTime(double timeSeconds) {
	MatrixState.setTimeSeconds(timeSeconds);
    }

    public static void setSingular(boolean singular) {
	MatrixState.setSingular(singular);
    }
}
