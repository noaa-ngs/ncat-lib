package gov.noaa.ngs.transform;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Computes and stores ellipsoid and zone specific constants to a property file
 *
 * @author Krishna Tadepalli
 * @version 1.0 Date: 06/10/2011
 */
public class Util {

    public static final String NAD83_ZONE_DEF_PROP = "/gov/noaa/ngs/transform/resources/nad83ZoneDefinitions.properties";
    public static final String NAD27_ZONE_DEF_PROP = "/gov/noaa/ngs/transform/resources/nad27ZoneDefinitions.properties";
    public static final double NAD83_RF = 298.257222101; //reciprocal of earth flattening
    public static final double NAD83_RADIUS = 6378137.000000; //earth eq. radius
    public static final double NAD27_RF = 294.978698214; //reciprocal of earth flattening
    public static final double NAD27_RADIUS = 6378206.4; //earth eq. radius
    public static final double DEG_TO_RADIANS = Math.PI / 180.0;
    private static double radius;

    /**
     * computes and loads spc constants to a property file
     *
     * @param propFile property file
     * @param datum reference datum
     * @throws IOException if unable to retrieve constants
     */
    public static void loadConstants(String propFile, String datum) throws IOException {
        Properties prop = new Properties();
        InputStream inStream = null;
        double rf;
        if (datum.equals("nad83")) {
            rf = NAD83_RF;
            radius = NAD83_RADIUS;
            inStream = Util.class.getResourceAsStream(NAD83_ZONE_DEF_PROP);
        } else {
            rf = NAD27_RF;
            radius = NAD27_RADIUS;
            inStream = Util.class.getResourceAsStream(NAD27_ZONE_DEF_PROP);
        }
        double f = 1.0 / rf; //earth flattening
        double esq = 2 * f - Math.pow(f, 2.0); //eccentricity(e) squared
        double eps = esq / (1.0 - esq);        //eprime squared
        double e = Math.sqrt(esq);             //eccentricity

        // ellipsoid constants
        prop.setProperty("radius", Double.toString(radius));
        prop.setProperty("e", Double.toString(e));
        prop.setProperty("esq", Double.toString(esq));
        prop.setProperty("eps", Double.toString(eps));
        prop.setProperty("eflattening", Double.toString(f));
        prop.setProperty("deg2rad", Double.toString(DEG_TO_RADIANS));
        // load spc zone definitions
        if (inStream == null) {
            throw new IOException("Unable to access zone definitions for " + datum);
        } else {
            Properties zoneDef = new Properties();
            zoneDef.load(inStream);
            String zoneStr = zoneDef.getProperty("zones");
            String[] zones = zoneStr.split(",");            //
            loadTmConstants(f, zones, prop, zoneDef, datum);
            loadOmConstants(e, esq, eps, zones, prop, zoneDef);
            loadLcConstants(e, esq, zones, prop, zoneDef, datum);
            prop.store(new FileOutputStream(propFile), null);

        }
    }

    /**
     * computes and sets constants for Lambert Conic
     *
     * @param e eccentricity
     * @param esq eccentricity squared
     * @param zones spc zones
     * @param prop properties object
     * @param zoneDef zone definition property object
     */
    private static void loadLcConstants(double e, double esq, String[] zones, Properties prop, Properties zoneDef, String datum) {
        int idx = 0;
        HashMap<String, Double> l1map = new HashMap<>();
        l1map.put("5010", 3.0E6);
        l1map.put("0407", 4.18669258E6);
        l1map.put("0600", 6.0E5);
        l1map.put("2001", 6.0E5);
        l1map.put("1900", 8.0E5);
        l1map.put("2002", 2.0E5);
        l1map.put("5201", 5.0E5);
        l1map.put("5202", 5.0E5);
        HashMap<String, Double> l11map = new HashMap<>();
        l11map.put("5010", 44.0E0);
        l11map.put("1703", 25.0E0);
        l11map.put("2111", 36.0E0);
        l11map.put("2112", 35.0E0);
        l11map.put("2113", 33.0E0);
        prop.setProperty("lc.c1", Double.toString(1.012794065E2));
        prop.setProperty("lc.c2", Double.toString(1.052893882E3));
        prop.setProperty("lc.c3", Double.toString(4.483344E0));
        prop.setProperty("lc.c4", Double.toString(2.352E-2));
        prop.setProperty("lc.c5", Double.toString(2.092583216E7));
        prop.setProperty("lc.d1", Double.toString(9.873675553E-3));
        prop.setProperty("lc.d2", Double.toString(1.04754671E3));
        prop.setProperty("lc.d3", Double.toString(6.19276E0));
        prop.setProperty("lc.d4", Double.toString(5.0912E-2));

        for (int i = 0; i < zones.length; i++) {
            String proj = zoneDef.getProperty(zones[i] + ".proj", "");
            if (proj.equals("LC")) {
                double southSp = Double.parseDouble(zoneDef.getProperty(zones[i] + ".southLat", "0.0")) * DEG_TO_RADIANS;
                double northSp = Double.parseDouble(zoneDef.getProperty(zones[i] + ".northLat", "0.0")) * DEG_TO_RADIANS;
                double originLat = Double.parseDouble(zoneDef.getProperty(zones[i] + ".originLat", "0.0")) * DEG_TO_RADIANS;
                double sinfs = Math.sin(southSp);
                double cosfs = Math.cos(southSp);
                double sinfn = Math.sin(northSp);
                double cosfn = Math.cos(northSp);
                double sinfb = Math.sin(originLat);
                double qs = qFactor(e, sinfs);
                double qn = qFactor(e, sinfn);
                double qb = qFactor(e, sinfb);
                double w1 = Math.sqrt(1.0 - esq * Math.pow(sinfs, 2.0));
                double w2 = Math.sqrt(1.0 - esq * Math.pow(sinfn, 2.0));
                double sinfo = Math.log(w2 * cosfs / (w1 * cosfn)) / (qn - qs);
                double tmp = Math.exp(qs * sinfo) / (w1 * sinfo);
                double mappingRadius = radius * cosfs * tmp;
                // mapping radius at origin Lat
                double mappingRadiusOrig = mappingRadius / Math.exp(qb * sinfo);
                String lcConstStr = Double.toString(sinfo) + ","
                        + Double.toString(mappingRadius) + ","
                        + Double.toString(mappingRadiusOrig);
                prop.setProperty(zones[i] + ".const", lcConstStr);
                if (datum.equals("nad27")) {
                    idx++;
                    prop.setProperty(zones[i] + ".lcConstIndex", Integer.toString(idx));
                    prop.setProperty(zones[i] + ".l1", Double.toString(l1map.getOrDefault(zones[i], 2.0E6)));
                    prop.setProperty(zones[i] + ".l11", Double.toString(l11map.getOrDefault(zones[i], 0.0)));
                }

            }
        }
    }

    /**
     * computes and sets constants for Transverse Mercator
     *
     * @param f earth flattening
     * @param zones spc zones
     * @param prop properties object
     * @param zoneDef zone definition property object
     */
    private static void loadTmConstants(double f, String[] zones, Properties prop, Properties zoneDef, String datum) {
        double pr = (1.0 - f) * radius;
        double en = (radius - pr) / (radius + pr);
        double en2 = Math.pow(en, 2.0);
        double en3 = Math.pow(en, 3.0);
        double en4 = Math.pow(en, 4.0);
        double a = -1.5 * en + (9.0 / 16.0) * en3;
        double b = 0.9375 * en2 - (15.0 / 32.0) * en4;
        double c = -(35.0 / 48.0) * en3;
        double r = radius * (1.0 - en) * (1. - en2) * (1. + 2.25 * en2 + (225.0 / 64.0) * en4);
        prop.setProperty("tm.a", Double.toString(a));
        prop.setProperty("tm.b", Double.toString(b));
        prop.setProperty("tm.c", Double.toString(c));
        prop.setProperty("tm.r", Double.toString(r));
        double c2 = 3.0 * en / 2.0 - 27.0 * en3 / 32.0;
        double c4 = 21.0 * en2 / 16.0 - 55.0 * en4 / 32.0;
        double c6 = 151.0 * en3 / 96.0;
        double c8 = 1097.0 * en4 / 512.0;
        double v0 = 2.0 * (c2 - 2.0 * c4 + 3.0 * c6 - 4.0 * c8);
        double v2 = 8.0 * (c4 - 4.0 * c6 + 10.0 * c8);
        double v4 = 32.0 * (c6 - 6.0 * c8);
        double v6 = 128.0 * c8;
        prop.setProperty("tm.v0", Double.toString(v0));
        prop.setProperty("tm.v2", Double.toString(v2));
        prop.setProperty("tm.v4", Double.toString(v4));
        prop.setProperty("tm.v6", Double.toString(v6));
        // set constants for each zone where TM is used
        int idx = 0;
        for (int i = 0; i < zones.length; i++) {
            String proj = zoneDef.getProperty(zones[i] + ".proj", "");
            if (proj.equals("TM")) {
                double sf = Double.parseDouble(zoneDef.getProperty(zones[i] + ".sf", "0.0"));
                double scaleFactor = sf > 1.0 ? 1.0 - 1.0 / sf : 1.0;
                double southSp = Double.parseDouble(zoneDef.getProperty(zones[i] + ".originLat", "0.0"));
                southSp *= DEG_TO_RADIANS;
                double omo = southSp + a * Math.sin(2.0 * southSp) + b * Math.sin(4.0 * southSp) + c * Math.sin(6.0 * southSp);
                double so = scaleFactor * r * omo;
                prop.setProperty(zones[i] + ".const", Double.toString(so));
                int zone = Integer.valueOf(zones[i]);
                // set up index into constants for nad27 TM zones
                if (datum.equals("nad27")) {
                    if (!(zone >= 5000 && zone <= 5010)) {
                        idx++;
                        prop.setProperty(zones[i] + ".constIndex", Integer.toString(idx));
                    }
                }
            }
        }

    }

    /**
     * computes and sets constants for Oblique Mercator
     *
     * @param e eccentricity
     * @param esq eccentricity squared
     * @param eps ePrime squared
     * @param zones spc zones
     * @param prop properties object
     * @param zoneDef zone definition property object
     */
    private static void loadOmConstants(double e, double esq, double eps, String[] zones, Properties prop, Properties zoneDef) {
        for (int i = 0; i < zones.length; i++) {
            String proj = zoneDef.getProperty(zones[i] + ".proj", "");
            if (proj.equals("OM")) {

                double originLat = Double.parseDouble(zoneDef.getProperty(zones[i] + ".originLat", "0.0")) * DEG_TO_RADIANS;
                double cm = Double.parseDouble(zoneDef.getProperty(zones[i] + ".cm", "0.0")) * DEG_TO_RADIANS;
                double azimuth = Double.parseDouble(zoneDef.getProperty(zones[i] + ".azimuth", "0.0")) * DEG_TO_RADIANS;
                double sf = Double.parseDouble(zoneDef.getProperty(zones[i] + ".sf", "0.0"));

                double kc = sf > 1.0 ? 1.0 - 1.0 / sf : 1.0;
                double e2 = esq;
                double e4 = e2 * e2;
                double e6 = Math.pow(e2, 3.0);
                double e8 = Math.pow(e2, 4.0);
                double c2 = e2 / 2.0 + 5.0 * e4 / 24.0 + e6 / 12.0 + 13.0 * e8 / 360.0;
                double c4 = 7.0 * e4 / 48.0 + 29.0 * e6 / 240.0 + 811.0 * e8 / 11520.0;
                double c6 = 7.0 * e6 / 120.0 + 81.0 * e8 / 1120.0;
                double c8 = 4279.0 * e8 / 161280.0;
                double f0 = 2.0 * c2 - 4.0 * c4 + 6.0 * c6 - 8.0 * c8;
                double f2 = 8.0 * c4 - 32.0 * c6 + 80.0 * c8;
                double f4 = 32.0 * c6 - 192.0 * c8;
                double f6 = 128.0 * c8;

                double sinb = Math.sin(originLat);
                double cosb = Math.cos(originLat);
                double b = Math.sqrt(1.0 + eps * Math.pow(cosb, 4.0));
                double w = Math.sqrt(1.0 - esq * sinb * sinb);
                double a = b * radius * Math.sqrt(1.0 - esq) / (w * w);

                double qc = qFactor(sinb, e, 0);
                double c = coshx(b * Math.sqrt(1.0 - esq) / w / cosb) - b * qc;
                double d = a * kc / b;
                double sgc = Math.sin(azimuth);
                double cgc = Math.cos(azimuth);
                double sgo = sgc * cosb * radius / (a * w);
                double cgo = Math.sqrt(1.0 - sgo * sgo);
                double lono = -cm + Math.asin(sgo * Math.sinh(b * qc + c) / cgo) / b;
                double xi = a * kc / radius;
                String omConstStr = Double.toString(b) + ","
                        + Double.toString(c) + ","
                        + Double.toString(d) + ","
                        + Double.toString(sgo) + ","
                        + Double.toString(cgo) + ","
                        + Double.toString(cgc) + ","
                        + Double.toString(sgc) + ","
                        + Double.toString(xi) + ","
                        + Double.toString(lono) + ","
                        + Double.toString(f0) + ","
                        + Double.toString(f2) + ","
                        + Double.toString(f4) + ","
                        + Double.toString(f6);
                prop.setProperty(zones[i] + ".const", omConstStr);
            }
        }

    }

    /**
     * returns the inverse of a cosine hyperbolic function
     *
     * @param arg
     * @return
     */
    private static double coshx(double arg) {
        return Math.log(arg + Math.sqrt(arg * arg - 1));

    }

    /**
     * computes intermediate constant for LC
     *
     * @param arg1
     * @param arg2
     * @return
     */
    private static double qFactor(double arg1, double arg2) {
        return ((Math.log((1 + arg2) / (1 - arg2)) - arg1 * Math.log((1 + arg1 * arg2) / (1 - arg1 * arg2))) / 2.0);
    }

    /**
     * computes intermediate constant for OM
     *
     * @param arg1
     * @param arg2
     * @param dummy
     * @return
     */
    private static double qFactor(double arg1, double arg2, int dummy) {
        return (Math.log((1.0 + arg1) / (1.0 - arg1)) - arg2 * Math.log((1.0 + arg2 * arg1)
                / (1.0 - arg2 * arg1))) / 2.0;
    }

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.err.println("Insufficient arguments. Specify a filename for spcConstants property file");
            } else {
                String nad83PropFile = args[0];
                String nad27PropFile = args[1];
                if (!nad83PropFile.contains(".properties")) {
                    nad83PropFile += ".properties";
                }
                if (!nad27PropFile.contains(".properties")) {
                    nad27PropFile += ".properties";
                }
                loadConstants(nad83PropFile, "nad83");
                loadConstants(nad27PropFile, "nad27");
            }
        } catch (IOException e) {
            System.err.println("Failed to retrieve/store spc properties. Exception:" + e.getMessage());
            System.exit(1);
        }
    }
}
