/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.noaa.ngs.grid;

/**
 * A container for interpolation methods
 *
 * @author Krishna.Tadepalli
 * @version 1.0 Date: 09/30/2016
 */
public class Interpolator {

    private double x;
    private double y;
    private double[] intpGrid;

    public Interpolator(double x, double y, double[] intpGrid) {
        this.x = x;
        this.y = y;
        this.intpGrid = intpGrid;
    }

    /**
     * performs a linear quadratic interpolation from equally spaced values
     * using Newton-Gregory forward polynomial.
     *
     * @param xx - parameter used for interpolation
     * @param f0 - surrounding cell
     * @param f1 - surrounding cell
     * @param f2 - surrounding cell
     * @return - interpolated value
     */
    private double quad(double xx, double f0, double f1, double f2) {
        double df0 = f1 - f0;
        double df1 = f2 - f1;
        double d2f0 = df1 - df0;
        return f0 + xx * df0 + 0.5 * xx * (xx - 1.0) * d2f0;
    }

    /**
     * performs biquadratic interpolation for a given point using a 3X3 grid
     * around it
     *
     * @return interpolated value
     */
    public double biquadratic() {
        double fx0 = quad(x, intpGrid[0], intpGrid[1], intpGrid[2]);
        double fx1 = quad(x, intpGrid[3], intpGrid[4], intpGrid[5]);
        double fx2 = quad(x, intpGrid[6], intpGrid[7], intpGrid[8]);
        double fx = quad(y, fx0, fx1, fx2);
        return fx;

    }

    public double bilinear() {
        double t = x;
        double u = y;
        double w1 = (1 - t) * (1 - u) * intpGrid[0];
        double w2 = (t) * (1 - u) * intpGrid[1];
        double w3 = (t) * (u) * intpGrid[3];
        double w4 = (1 - t) * (u) * intpGrid[2];
 
        return (w1 + w2 + w3 + w4);

    }

}
