package gov.noaa.ngs.transform;

import java.util.Properties;

/**
 * Transforms geodetic coordinates to spc and vice versa using Lambert Conic
 * Single Parallel projection. Note: The constants used in this projection are
 * specific to American Samoa-AS62 datum which is similar to nad27.
 *
 * @author Krishna Tadepalli
 * @version 1.0 Date: 07/20/2011
 */
public class LCSTransformation extends CoordinateTransformation {

    private double[] lcsConst = {
        2.062648062471E5, 5.0E5, 6.1200E5, -82.31223465E6, -82.0E6, 0.9999999999,
        -0.2464352205, -0.5110953291E5, 3.82892, -1.16664, 1.012794065E2,
        1.052893882E3, 4.483344E0, 2.352E-2, 2.092583216E7, 9.873675553E-3,
        1.04754671E3, 6.19276E0, 5.0912E-2};

    /**
     * Constructor lazily instantiated via a parent's factory method
     * @param zoneDef zone definitions
     * @param zoneConstants zone constants
     */
    public LCSTransformation(Properties zoneDef, Properties zoneConstants) {
        super(zoneDef, zoneConstants);
    }

    @Override
    public String toProjectedCoordinates(double lat, double lon) {
        setConstants();
        String spcStr = datum + "," + zone + "," + label;
        lat *= degreesToRadians;
        lon *= degreesToRadians;
        double sinLat = Math.sin(lat);
        double cosLat = Math.cos(lat);
        double cosSqLat = cosLat * cosLat;
        double s = lcsConst[10] * (lcsConst[7] - lat * lcsConst[0]
                + (lcsConst[11] - (lcsConst[12]
                - lcsConst[13] * cosSqLat) * cosSqLat) * sinLat * cosLat);
        double s8 = s * 1.0E-8;
        double s8Sq = s8 * s8;
        double r = lcsConst[3] + s * lcsConst[5]
                * (1.0 + s8Sq * (lcsConst[8] - s8 * lcsConst[9]));
        double convergence = lcsConst[6]
                * (lcsConst[2] + lon * lcsConst[0]) / lcsConst[0];
        double east = lcsConst[1] + r * Math.sin(convergence);
        double north = lcsConst[4] - r + 2.0 * r
                * Math.pow(Math.sin(convergence * 0.5), 2.0);
        convergence *= lcsConst[0] / 3600;
        double scaleFactor = lcsConst[6] * r
                * Math.sqrt(1.0 - esq * sinLat * sinLat) / lcsConst[14] / cosLat;
        spcStr += "," + String.format(northFormat, north);
        spcStr += "," + String.format(eastFormat, east);
        spcStr += "," + String.format(convFormat, convergence);
        spcStr += "," + String.format(sfFormat, scaleFactor);
        return spcStr;
    }

    @Override
    public String toGeodeticCoordinates(double north, double east) {
        setConstants();
        String geodeticStr = datum + "," + zone + "," + label;
        double theta = Math.atan((east - lcsConst[1]) / (lcsConst[4] - north));
        double thetaSec = theta * lcsConst[0];
        double esec = lcsConst[2] - thetaSec / lcsConst[6];
        double lon = -(esec / lcsConst[0]) / degreesToRadians;
        double cosTh = Math.cos(theta);
        double shlft = Math.pow(Math.sin(theta / 2.0), 2.0);
        double r = (lcsConst[4] - north) / cosTh;
        double s = (lcsConst[4] - lcsConst[3]
                - north + 2.0 * r * shlft) / lcsConst[5];
        for (int i = 0; i < 3; i++) {
            double far = s * 1.0E-8;
            s = s / (1.0 + far * far * (lcsConst[8] - far * lcsConst[9]));
        }
        double omSec = lcsConst[7] - lcsConst[15] * s;
        double omega = omSec / lcsConst[0];
        double sino = Math.sin(omega);
        double coso = Math.cos(omega);
        double cosoSq = Math.pow(coso, 2.0);
        double psec = omSec + (lcsConst[16] + (lcsConst[17]
                + lcsConst[18] * cosoSq) * cosoSq) * sino * coso;
        double lat = (psec / lcsConst[0]) / degreesToRadians;
        geodeticStr += "," + String.format(latFormat, lat);
        geodeticStr += "," + String.format(lonFormat, lon);
        geodeticStr += "," + String.format(convFormat, theta / degreesToRadians);
        geodeticStr += "," + String.format(sfFormat, 1.0);

        return geodeticStr;
    }

    @Override
    protected void setConstants() {
        datum = zoneDef.getProperty(zone + ".datum", datum);
    }

    @Override
    public String toGeodeticCoordinates(double x, double y, double z) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public String toGeodeticCoordinates(double north, double east, boolean sh) {
        return toGeodeticCoordinates(north, east); 
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
