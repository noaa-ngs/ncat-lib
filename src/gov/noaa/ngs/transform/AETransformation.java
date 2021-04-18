package gov.noaa.ngs.transform;

import java.util.Properties;

/**
 * Transforms geodetic coordinates to spc and vice versa using Azimuthal
 * Equidistant projection. Note: The constants used in this projection are
 * specific to Guam-GU63 datum which is similar to nad27.
 *
 * @author Krishna Tadepalli
 * @version 1.0 Date: 07/12/2011
 */
public class AETransformation extends CoordinateTransformation {

    private double[] aeConst = {5.0E4, 3.092241724E1, 5.2109550254E5, 6.7686580E-3,
        3.087002482E1, 4.82632837578E4, 1.052893943E3, 4.483386E0, 2.3559E-2,
        1.27564128E-1, 4.82632837702E4, 3.239388390E-2, 1.87770E0,
        1.047546691E3, 6.193011E0, 5.0699E-2, 5.2109550254E5, 3.092241724E1};
    private double rhosec = 2.062648062471E05;  //radiansToSec

    /**
     * Constructor lazily instantiated via a parent's factory method
     * @param zoneDef zone definitions
     * @param zoneConstants zone constants
     */
    public AETransformation(Properties zoneDef, Properties zoneConstants) {
        super(zoneDef, zoneConstants);
    }

    @Override
    public String toProjectedCoordinates(double lat, double lon) {
        setConstants();
        lon = CoordinateTransformation.resetLon(lon);
        String spcStr = datum + "," + zone + "," + label;
        lat *= degreesToRadians;
        lon *= degreesToRadians;
        double sinp = Math.sin(lat);
        double cosp = Math.cos(lat);
        double tanp = sinp / cosp;
        double q = Math.sqrt(1.0 - aeConst[3] * sinp * sinp);
        double east = aeConst[0] + aeConst[1] * (lon * rhosec - aeConst[2]) * cosp / q;
        double x1 = Math.pow(east - aeConst[0], 2.0) / 1.0E8;
        double t1 = lat * rhosec - aeConst[5] - (aeConst[6] - aeConst[7] * Math.pow(cosp, 2.0)
                + aeConst[8] * Math.pow(cosp, 4.0)) * sinp * cosp;
        double t2 = x1 * tanp * q / aeConst[9];
        double north = aeConst[0] + aeConst[4] * t1 + t2;
        spcStr += "," + String.format(northFormat, north);
        spcStr += "," + String.format(eastFormat, east);
        spcStr += "," + "N/A";
        spcStr += "," + "N/A";
        return spcStr;
    }

    @Override
    public String toGeodeticCoordinates(double north, double east) {
        setConstants();
        String geodeticStr = datum + "," + zone + "," + label;
        double x1 = Math.pow(east - aeConst[0], 2.0) / 1.0E8;
        double t2 = x1 * aeConst[12];
        double lat = 0.0;
        double cosp = 0.0;
        double q = 0.0;
        for (int i = 0; i < 3; i++) {
            double omega = aeConst[10] + aeConst[11] * (north - aeConst[0] - t2);
            double temp = omega / rhosec;
            double sinm = Math.sin(temp);
            double cosm = Math.cos(temp);
            double psec = omega + (aeConst[13] + aeConst[14] * Math.pow(cosm, 2.0)
                    + aeConst[15] * Math.pow(cosm, 4.0)) * sinm * cosm;
            lat = psec / rhosec;
            double sinp = Math.sin(lat);
            cosp = Math.cos(lat);
            double tanp = sinp / cosp;
            q = Math.sqrt(1.0 - aeConst[3] * sinp * sinp);
            t2 = x1 * tanp * q / aeConst[9];
        }
        cosp = Math.cos(lat);
        double esec = aeConst[16] + (east - aeConst[0]) * q / aeConst[17] / cosp;
        double lon = (esec / rhosec);
        geodeticStr += "," + String.format(latFormat, lat / degreesToRadians);
        geodeticStr += "," + String.format(lonFormat, lon / degreesToRadians);
        geodeticStr += "," + "N/A";
        geodeticStr += "," + "N/A";

        return geodeticStr;

    }

    @Override
    protected void setConstants() {
        datum = zoneDef.getProperty(zone + ".datum", datum);
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
}
