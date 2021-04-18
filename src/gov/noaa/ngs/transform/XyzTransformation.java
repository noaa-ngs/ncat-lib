package gov.noaa.ngs.transform;

import java.util.Properties;

/**
 * Transforms geodetic coordinates to xyz and vice versa
 *
 * @author Krishna Tadepalli
 * @version 1.0 Date: 06/23/2011
 *
 */
public class XyzTransformation extends CoordinateTransformation {

    private double height;
    private double z;

    /**
     * Constructor lazily instantiated via a parent's factory method
     * @param zoneDef zone definition
     * @param zoneConstants zone constants
     */
    public XyzTransformation(Properties zoneDef, Properties zoneConstants) {
        super(zoneDef, zoneConstants);
    }

    @Override
    public String toProjectedCoordinates(double lat, double lon) {
        return toProjectedCoordinates(lat, lon, height);
    }

    @Override
    public String toProjectedCoordinates(double lat, double lon, double height) {
        lon = CoordinateTransformation.resetLon(lon);
        lat *= degreesToRadians;
        lon *= degreesToRadians;
        double sinLat = Math.sin(lat);
        double g1 = radius / (Math.sqrt(1 - esq * sinLat * sinLat));
        double g2 = g1 * (1 - eflattening) * (1 - eflattening) + height;
        g1 += height;
        double x = g1 * Math.cos(lat);
        double y = x * Math.sin(lon);
        x *= Math.cos(lon);
        double zVal = g2 * sinLat;
        String retStr = String.format(xyzFormat, x);
        retStr += "," + String.format(xyzFormat, y);
        retStr += "," + String.format(xyzFormat, zVal);
        return retStr;
    }

    @Override
    public String toGeodeticCoordinates(double x, double y) {
        return toGeodeticCoordinates(x, y, z);
    }

    @Override
    public String toGeodeticCoordinates(double x, double y, double z) {

        //compute semi-minor axis and set sign to that of z in order to
        //get sign of latitude correct
        double b = radius * (1.0 - eflattening);
        if (z < 0.0) {
            b = -b;
        }

        //compute intermediate values for latitude
        double r = Math.sqrt(x * x + y * y);
        double e1 = (b * z - (radius * radius - b * b)) / (radius * r);
        double f = (b * z + (radius * radius - b * b)) / (radius * r);

        //find solution to t^4 + 2*E*t^3 + 2*F*t -1 = 0
        double p = (4.0 / 3.0) * (e1 * f + 1.0);
        double q = 2.0 * (e1 * e1 - f * f);
        double d = p * p * p + q * q;
        double v;
        if (d >= 0.0) {
            v = Math.pow((Math.sqrt(d) - q), (1.0 / 3.0))
                    - Math.pow((Math.sqrt(d) + q), (1.0 / 3.0));
        } else {
            v = 2.0 * Math.sqrt(-p)
                    * Math.cos(Math.acos(q / (p * Math.sqrt(-p))) / 3.0);
        }

        //improve v(not necessary unless point is near pole)
        if (v * v < Math.abs(p)) {
            v = -(v * v * v + 2.0 * q) / (3.0 * p);
        }

        double g = (Math.sqrt(e1 * e1 + v) + e1) / 2.0;
        double t = Math.sqrt(g * g + (f - v * g) / (2.0 * g - e1)) - g;
        double lat = (Math.atan((radius * (1.0 - t * t)) / (2.0 * b * t)));

        height = (r - radius * t) * Math.cos(lat) + (z - b) * Math.sin(lat);

        double zlong = Math.atan2(y, x);

        double lon = CoordinateTransformation.resetLon(zlong / degreesToRadians);
        String geodeticStr = String.format(latFormat, lat / degreesToRadians);
        geodeticStr += "," + String.format(lonFormat, lon);
        geodeticStr += "," + String.format(heightFormat, height);
        return geodeticStr;

    }
    /**
     * converts to xyz for a given ellipsoid
     * @param lat altitude
     * @param lon longitude
     * @param height ellipsoid height
     * @param radius equatorial radius
     * @param eflattening flattening
     * @return projected coordinate
     */
    @Override
    public String toProjectedCoordinates(double lat, double lon, double height, double radius, double eflattening) {
        lon = CoordinateTransformation.resetLon(lon);
        lat *= degreesToRadians;
        lon *= degreesToRadians;
        double esq = 2 * eflattening - Math.pow(eflattening, 2.0);
        double sinLat = Math.sin(lat);
        double g1 = radius / (Math.sqrt(1 - esq * sinLat * sinLat));
        double g2 = g1 * (1 - eflattening) * (1 - eflattening) + height;
        g1 += height;
        double x = g1 * Math.cos(lat);
        double y = x * Math.sin(lon);
        x *= Math.cos(lon);
        double zVal = g2 * sinLat;
        String retStr = String.format(xyzFormat, x);
        retStr += "," + String.format(xyzFormat, y);
        retStr += "," + String.format(xyzFormat, zVal);
        return retStr;
    }
    
    public String toGeodeticCoordinates(double x, double y, double z,double radius, double eflattening) {

        //compute semi-minor axis and set sign to that of z in order to
        //get sign of latitude correct
        double b = radius * (1.0 - eflattening);
        if (z < 0.0) {
            b = -b;
        }

        //compute intermediate values for latitude
        double r = Math.sqrt(x * x + y * y);
        double e1 = (b * z - (radius * radius - b * b)) / (radius * r);
        double f = (b * z + (radius * radius - b * b)) / (radius * r);

        //find solution to t^4 + 2*E*t^3 + 2*F*t -1 = 0
        double p = (4.0 / 3.0) * (e1 * f + 1.0);
        double q = 2.0 * (e1 * e1 - f * f);
        double d = p * p * p + q * q;
        double v;
        if (d >= 0.0) {
            v = Math.pow((Math.sqrt(d) - q), (1.0 / 3.0))
                    - Math.pow((Math.sqrt(d) + q), (1.0 / 3.0));
        } else {
            v = 2.0 * Math.sqrt(-p)
                    * Math.cos(Math.acos(q / (p * Math.sqrt(-p))) / 3.0);
        }

        //improve v(not necessary unless point is near pole)
        if (v * v < Math.abs(p)) {
            v = -(v * v * v + 2.0 * q) / (3.0 * p);
        }

        double g = (Math.sqrt(e1 * e1 + v) + e1) / 2.0;
        double t = Math.sqrt(g * g + (f - v * g) / (2.0 * g - e1)) - g;
        double lat = (Math.atan((radius * (1.0 - t * t)) / (2.0 * b * t)));

        height = (r - radius * t) * Math.cos(lat) + (z - b) * Math.sin(lat);

        double zlong = Math.atan2(y, x);

        double lon = CoordinateTransformation.resetLon(zlong / degreesToRadians);
        String geodeticStr = String.format(latFormat, lat / degreesToRadians);
        geodeticStr += "," + String.format(lonFormat, lon);
        geodeticStr += "," + String.format(heightFormat, height);
        return geodeticStr;

    }

    /**
     * sets height for ll2xyz conversion
     *
     * @param height ellipsoid height
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * sets z for xy2llh conversion
     *
     * @param z z-coordinate
     */
    public void setZ(double z) {
        this.z = z;
    }

    @Override
    protected void setConstants() {
    }

    @Override
    public String toGeodeticCoordinates(double north, double east, boolean sh) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public String toProjectedCoordinates(double radius, double f, double lat, double lon) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public String toGeodeticCoordinates(double radius, double fl, double north, double east, boolean southernHemisphere) {
        throw new UnsupportedOperationException("Not supported."); 
    }

 
}
