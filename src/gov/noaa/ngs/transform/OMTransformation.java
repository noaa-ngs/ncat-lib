package gov.noaa.ngs.transform;

import java.util.Properties;

/**
 * Transforms geodetic coordinates to spc and vice versa using Oblique Mercator
 * projection
 *
 * @author Krishna Tadepalli
 * @version 1.0 Date: 06/10/2011
 */
public class OMTransformation extends CoordinateTransformation {

    private double azimuth;
    private double falseEasting;
    private double falseNorthing;
    // intermediate zone constants
    private double b;
    private double c;
    private double d;
    private double sgo;
    private double cgo;
    private double cgc;
    private double sgc;
    private double xi;
    private double lono;
    private double f0;
    private double f2;
    private double f4;
    private double f6;

    /**
     * Constructor lazily instantiated via a parent's factory method
     * @param zoneDef zone definitions
     * @param zoneConstants zone constants
     */
    public OMTransformation(Properties zoneDef, Properties zoneConstants) {
        super(zoneDef, zoneConstants);
    }

    @Override
    public String toProjectedCoordinates(double lat, double lon) {
        if (!zoneDef.getProperty(zone + ".proj", " ").equals("OM") || zone.equals("0000")) {
            return datum + "," + zone + "," + label + ",0.0,0.0,0.0,0.0";
        }
        lon = CoordinateTransformation.resetLon(lon);
        setConstants();
        String spcStr = datum + "," + zone + "," + label;
        lat *= degreesToRadians;
        lon *= degreesToRadians;
        double sinb = Math.sin(lat);
        double cosb = Math.cos(lat);
        double dl = (-lon - lono) * b;
        double sindl = Math.sin(dl);
        double cosdl = Math.cos(dl);
        double q = (Math.log((1 + sinb) / (1 - sinb)) - e * Math.log((1 + e * sinb) / (1 - e * sinb))) / 2.0;
        double r = Math.sinh(b * q + c);
        double s = Math.cosh(b * q + c);
        double u = d * Math.atan((cgo * r - sgo * sindl) / cosdl);
        double v = d * Math.log((s - sgo * r - cgo * sindl) / (s + sgo * r + cgo * sindl)) / 2.0;
        double north = (u * cgc - v * sgc) / unitsCf + falseNorthing;
        double east = (u * sgc + v * cgc) / unitsCf + falseEasting;

        double convergence = Math.atan((sgo - cgo * sindl * r) / (cgo * cosdl * s)) - azimuth;
        double scaleFactor = xi * Math.sqrt(1 - esq * sinb * sinb) * Math.cos(u / d) / cosb / cosdl;
        spcStr += "," + String.format(northFormat, north);
        spcStr += "," + String.format(eastFormat, east);
        spcStr += "," + String.format(convFormat, convergence / degreesToRadians);
        spcStr += "," + String.format(sfFormat, scaleFactor);

        return spcStr;
    }

    @Override
    public String toGeodeticCoordinates(double north, double east) {
        if (!zoneDef.getProperty(zone + ".proj", " ").equals("OM") || zone.equals("0000")) {
            return datum + "," + zone + "," + label + ",0.0,0.0,0.0,0.0";
        }

        setConstants();
        String geodeticStr = datum + "," + zone + "," + label;
        double u = sgc * (east - falseEasting) * unitsCf + cgc * (north - falseNorthing) * unitsCf;
        double v = cgc * (east - falseEasting) * unitsCf - sgc * (north - falseNorthing) * unitsCf;
        double r = Math.sinh(v / d);
        double s = Math.cosh(v / d);
        double sine = Math.sin(u / d);
        double q = (Math.log((s - sgo * r + cgo * sine) / (s + sgo * r - cgo * sine)) / 2.0 - c) / b;
        double ex = Math.exp(q);
        double xr = Math.atan((ex - 1.0) / (ex + 1.0)) * 2.0;
        double cs = Math.cos(xr);
        double cs2 = Math.pow(cs, 2.0);
        double cs4 = Math.pow(cs, 4.0);
        double cs6 = Math.pow(cs, 6.0);
        double lat = xr + (f0 + f2 * cs2 + f4 * cs4 + f6 * cs6) * cs * Math.sin(xr);
        double lon = -lono + Math.atan((sgo * sine + cgo * r) / Math.cos(u / d)) / b;

        double sinb = Math.sin(lat);
        double cosb = Math.cos(lat);
        double dl = (-lon - lono) * b;
        double sindl = Math.sin(dl);
        double cosdl = Math.cos(dl);
        q = (Math.log((1 + sinb) / (1 - sinb)) - e * Math.log((1 + e * sinb) / (1 - e * sinb))) / 2.0;
        r = Math.sinh(b * q + c);
        s = Math.cosh(b * q + c);
        double convergence = Math.atan((sgo - cgo * sindl * r) / (cgo * cosdl * s)) - azimuth;
        double scaleFactor = xi * Math.sqrt(1 - esq * sinb * sinb) * Math.cos(u / d) / cosb / cosdl;
        geodeticStr += "," + String.format(latFormat, lat / degreesToRadians);
        geodeticStr += "," + String.format(lonFormat, CoordinateTransformation.resetLon(lon / degreesToRadians));
        geodeticStr += "," + String.format(convFormat, convergence / degreesToRadians);
        geodeticStr += "," + String.format(sfFormat, scaleFactor);
        return geodeticStr;
    }

    @Override
    protected void setConstants() {
        azimuth = Double.parseDouble(zoneDef.getProperty(zone + ".azimuth", "0.0")) * degreesToRadians;
        falseEasting = Double.parseDouble(zoneDef.getProperty(zone + ".fe", "0.0"));
        falseNorthing = Double.parseDouble(zoneDef.getProperty(zone + ".fn", "0.0"));
        datum = zoneDef.getProperty(zone + ".datum", datum);
        String[] omConst = zoneConstants.getProperty(zone + ".const", "0.0").split(",");
        if (omConst.length > 1) {
            b = Double.parseDouble(omConst[0]);
            c = Double.parseDouble(omConst[1]);
            d = Double.parseDouble(omConst[2]);
            sgo = Double.parseDouble(omConst[3]);
            cgo = Double.parseDouble(omConst[4]);
            cgc = Double.parseDouble(omConst[5]);
            sgc = Double.parseDouble(omConst[6]);
            xi = Double.parseDouble(omConst[7]);
            lono = Double.parseDouble(omConst[8]);
            f0 = Double.parseDouble(omConst[9]);
            f2 = Double.parseDouble(omConst[10]);
            f4 = Double.parseDouble(omConst[11]);
            f6 = Double.parseDouble(omConst[12]);
        }

    }

    @Override
    public String toGeodeticCoordinates(double x, double y, double z) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public String toGeodeticCoordinates(double north, double east, boolean sh) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public String toProjectedCoordinates(double lat, double lon, double height) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public String toProjectedCoordinates(double radius, double f, double lat, double lon) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public String toProjectedCoordinates(double lat, double lon, double height, double radius, double eflattening) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public String toGeodeticCoordinates(double x, double y, double z, double radius, double eflattening) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public String toGeodeticCoordinates(double radius, double fl, double north, double east, boolean southernHemisphere) {
        throw new UnsupportedOperationException("Not supported."); 
    }

}
