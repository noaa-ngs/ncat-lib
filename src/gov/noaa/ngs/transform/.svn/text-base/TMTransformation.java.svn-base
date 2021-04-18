package gov.noaa.ngs.transform;

import java.util.Properties;

/**
 * Transforms geodetic coordinates to spc or utm and vice versa using Transverse
 * Mercator projection
 *
 * @author Krishna Tadepalli
 * @version 1.0 Date: 06/10/2011
 *
 */
public class TMTransformation extends CoordinateTransformation {

    // nad 27 spc constants
    private static final double RHOSEC = 2.062648062471E05;
    private static final double[] NAD27_SPC_TM_CONSTANTS = {
        3.090E5, 109341.00903E0, 0.9999600000E0, 0.3817065E0,
        3.150E5, 107545.53386E0, 0.9999333333E0, 0.3817477E0,
        3.966E5, 111136.62358E0, 0.9999000000E0, 0.3816485E0,
        4.029E5, 111136.62358E0, 0.9999000000E0, 0.3816485E0,
        4.095E5, 111136.62358E0, 0.9999333333E0, 0.3815948E0,
        2.715E5, 136290.53702E0, 0.9999950281E0, 0.3811454E0,
        2.916E5, 87206.09287E0, 0.9999411765E0, 0.3821090E0,
        2.952E5, 87206.09287E0, 0.9999411765E0, 0.3821090E0,
        2.958E5, 107545.53386E0, 0.9999000000E0, 0.3817593E0,
        3.030E5, 107545.53386E0, 0.9999000000E0, 0.3817593E0,
        5.598E5, 67479.52714E0, 0.9999666667E0, 0.3826496E0,
        5.640E5, 72858.21554E0, 0.9999666667E0, 0.3825762E0,
        5.688E5, 75846.77497E0, 0.9999900000E0, 0.3825176E0,
        5.742E5, 78237.83623E0, 0.9999900000E0, 0.3824812E0,
        5.766E5, 77640.05280E0, 0.9999999999E0, 0.3824867E0,
        4.038E5, 149478.35156E0, 0.9999473684E0, 0.3807624E0,
        4.104E5, 149478.35156E0, 0.9999473684E0, 0.3807624E0,
        4.167E5, 149478.35156E0, 0.9999333333E0, 0.3806227E0,
        3.180E5, 131497.04639E0, 0.9999750000E0, 0.3811074E0,
        3.246E5, 131497.04639E0, 0.9999411765E0, 0.3811332E0,
        3.084E5, 134492.84965E0, 0.9999666667E0, 0.3811064E0,
        3.135E5, 134492.84965E0, 0.9999666667E0, 0.3811064E0,
        2.466E5, 157275.15187E0, 0.9999000000E0, 0.3806180E0,
        2.526E5, 153676.25668E0, 0.9999666667E0, 0.3806575E0,
        3.012E5, 148878.72150E0, 0.9999428571E0, 0.3807283E0,
        3.087E5, 148878.72150E0, 0.9999090909E0, 0.3807541E0,
        3.195E5, 148878.72150E0, 0.9999090909E0, 0.3805361E0,
        3.198E5, 106348.62716E0, 0.9999600000E0, 0.3817257E0,
        3.252E5, 109341.00903E0, 0.9999411765E0, 0.3816986E0,
        3.258E5, 128501.66790E0, 0.9999333333E0, 0.3812643E0,
        3.330E5, 128501.66790E0, 0.9999333333E0, 0.3812422E0,
        3.402E5, 129699.76857E0, 0.9999411765E0, 0.3812362E0,
        4.161E5, 124608.30429E0, 0.9999000000E0, 0.3812311E0,
        4.200E5, 124608.30429E0, 0.9999000000E0, 0.3812311E0,
        4.269E5, 124608.30429E0, 0.9999000000E0, 0.3812311E0,
        2.580E5, 152476.76677E0, 0.9999666667E0, 0.3807327E0,
        2.688E5, 139287.02745E0, 0.9999750295E0, 0.3810845E0,
        3.756E5, 111136.62358E0, 0.9999090909E0, 0.3816135E0,
        3.825E5, 111136.62358E0, 0.9999000000E0, 0.3816204E0,
        3.882E5, 111136.62358E0, 0.9999166667E0, 0.3816288E0,
        2.676E5, 143482.84247E0, 0.9999666667E0, 0.3808377E0,
        2.757E5, 143482.84247E0, 0.9999375000E0, 0.3808450E0,
        2.829E5, 143482.84247E0, 0.9999375000E0, 0.3808750E0,
        2.574E5, 147379.72344E0, 0.9999937500E0, 0.3809220E0,
        2.610E5, 152476.76677E0, 0.9999642857E0, 0.3807420E0,
        3.786E5, 145880.83533E0, 0.9999411765E0, 0.3808422E0,
        3.864E5, 145880.83533E0, 0.9999411765E0, 0.3808422E0,
        3.915E5, 145880.83533E0, 0.9999411765E0, 0.3808422E0,
        3.963E5, 145880.83533E0, 0.9999411765E0, 0.3808422E0

    };

    private double a;
    private double b;
    private double c;
    private double r;
    private double v0;
    private double v2;
    private double v4;
    private double v6;
    //zone constants
    private double cm;
    private double falseEasting;
    private double falseNorthing;
    private double sf;
    private double so;
    private boolean southernHemisphere = false;

    /**
     * Constructor lazily instantiated via a parent's factory method
     *
     * @param zoneDef zone definitions
     * @param zoneConstants zone constants
     */
    public TMTransformation(Properties zoneDef, Properties zoneConstants) {
        super(zoneDef, zoneConstants);
    }

    @Override
    public String toProjectedCoordinates(double lat, double lon) {
        lon = CoordinateTransformation.resetLon(lon);
        setConstants();
        if (isUtm()) {
            setConstants(lat, lon, false);
            label = "UTM";
        } else {
            if (datum.equals("nad27")) {   //nad27 SPC conversion
                return toProjectedCoordinates(lat, lon, zone);
            }
        }
        String retStr = datum + "," + zone + "," + label;
        lat *= degreesToRadians;
        lon *= degreesToRadians;

        double scaleFactor = sf > 1.0 ? 1.0 - 1.0 / sf : 1.0;
        double om = lat + a * Math.sin(2.0 * lat) + b * Math.sin(4.0 * lat) + c * Math.sin(6.0 * lat);
        double s = r * om * scaleFactor;
        double sinfi = Math.sin(lat);
        double cosfi = Math.cos(lat);
        double tn = sinfi / cosfi;
        double ts = Math.pow(tn, 2.0);
        double ets = eps * Math.pow(cosfi, 2.0);
        double l = getLonDiff(lon, cm) * cosfi;
        double ls = l * l;
        double rn = scaleFactor * radius / Math.sqrt(1.0 - esq * Math.pow(sinfi, 2.0));
        double a2 = rn * tn / 2.0;
        double a4 = (5.0 - ts + ets * (9.0 + 4. * ets)) / 12.0;
        double a6 = (61.0 + ts * (ts - 58.0) + ets * (270.0 - 330.0 * ts)) / 360.0;
        double a1 = -rn;
        double a3 = (1.0 - ts + ets) / 6.0;
        double a5 = (5.0 + ts * (ts - 18.0) + ets * (14.0 - 58.0 * ts)) / 120.0;
        double a7 = (61.0 - 479.0 * ts + 179.0 * Math.pow(ts, 2.0) - Math.pow(ts, 3.0)) / 5040.0;
        double north = (s - so + a2 * ls * (1.0 + ls * (a4 + a6 * ls))) / unitsCf + falseNorthing;
        double east = falseEasting - (a1 * l * (1.0 + ls * (a3 + ls * (a5 + a7 * ls)))) / unitsCf;
        double c1 = -tn;
        double c3 = (1.0 + 3.0 * ets + 2.0 * Math.pow(ets, 2.0)) / 3.0;
        double c5 = (2.0 - ts) / 15.0;
        double convergence = c1 * l * (1.0 + ls * (c3 + c5 * ls));
        convergence = southernHemisphere ? convergence : -convergence;

        double f2 = (1. + ets) / 2.0;
        double f4 = (5.0 - 4.0 * ts + ets * (9.0 - 24.0 * ts)) / 12.0;
        scaleFactor = scaleFactor * (1.0 + f2 * ls * (1. + f4 * ls));
        retStr += "," + String.format(northFormat, north);
        retStr += "," + String.format(eastFormat, east);
        retStr += "," + String.format(convFormat, convergence / degreesToRadians);
        retStr += "," + String.format(sfFormat, scaleFactor);
        return retStr;
    }

    /**
     * returns projected coordinates for nad27 spc zones
     *
     * @param lat
     * @param lon
     * @param zone
     * @return
     */
    private String toProjectedCoordinates(double lat, double lon, String zone) {
        String retStr = datum + "," + zone + "," + label;
        lon = Math.abs(lon);
//        System.out.println("lat:" + lat + " lon:" + lon);
        double latrad = lat * degreesToRadians;
        double lonrad = lon * degreesToRadians;

        double sinp = Math.sin(latrad);
        double cosp = Math.cos(latrad);

        int index = Integer.valueOf(zoneConstants.getProperty(zone + ".constIndex", "0"));
        if (index == 0) {  //AK zones
            double t = 6.8147849E-3;
            double dlon = (Math.abs(cm * RHOSEC) - lon * 3600) / 1.E4;
            double dlon2 = dlon * dlon;
            double dlon4 = dlon2 * dlon2;
            double cosp2 = cosp * cosp;
            double cosp4 = cosp2 * cosp2;
            double cosp6 = cosp4 * cosp2;

            double s2 = Math.sqrt(1.E0 + t * cosp2);
            double s3 = Math.sqrt(1.E0 - cosp2) * cosp;
            double east = falseEasting + 1.01786215E6 * cosp / s2 * dlon * (1.E0 - 3.91740509E-4 * dlon2
                    * (1.E0 - 2.E0 * cosp2 - t * cosp4) + 4.60382E-8 * dlon4 * (1.E0 - 20.E0 * cosp2
                    + 23.6047E0 * cosp4 + 0.4907E0 * cosp6));
            double north = 101.269278503E0 * (lat * 3600 - 1.9390005442E5 - (1.052893943E3
                    - 4.483386E0 * cosp2 + 2.3599E-2 * cosp4) * s3) + 2.46736748E4 * s3 / s2 * dlon2
                    * (1.E0 + 1.958703E-4 * dlon2 * (-1.E0 + 6.E0 * cosp2 + 6.133306E-2 * cosp4
                    + 1.8577E-4 * cosp6) + 1.5346E-8 * dlon4 * (1.E0 - 60.E0 * cosp2
                    + 117.75E0 * cosp4 + 4.089E0 * cosp6));
            double convergence = s3 / cosp * dlon * (1.E4 + 7.83481E0 * dlon2 * (cosp2 + 2.044E-2 * cosp4
                    + 0.9E-4 * cosp6) + 0.3683E-2 * dlon4 * (3.E0 * cosp4 - cosp2)) / 3600;
            double scaleFactor = 0.9999E0 * (1.E0 + Math.pow(s2, 4) / 8.81572821E2 * Math.pow((east - falseEasting) / 1.E6, 2));

            retStr += "," + String.format(northFormat, north);
            retStr += "," + String.format(eastFormat, east);
            retStr += "," + String.format(convFormat, convergence);
            retStr += "," + String.format(sfFormat, scaleFactor);
        } else {
            // constants vary by TM zone
            // New Jersey zone 2900 has a different constant
            double t1 = zone.equals("2900") ? 2.0E6 : 0.5E06;

            int l = index * 4;
            double t2 = NAD27_SPC_TM_CONSTANTS[l - 4];
            double t34 = NAD27_SPC_TM_CONSTANTS[l - 3];
            double t5 = NAD27_SPC_TM_CONSTANTS[l - 2];
            double t6 = NAD27_SPC_TM_CONSTANTS[l - 1];
            // constanst for all nad27 TM zones
            double c1 = 3.092241724E01;
            double c2 = 3.9174E00;
            double c3 = 4.0831E00;
            double c4 = 3.28083333E00;
            double c5 = 2.552381E-9;
            double c6 = 1.012794065E02;
            double c7 = 1.052893882E03;
            double c8 = 4.483344E0;
            double c9 = 2.352E-2;
            double c10 = 1.9587E-12;
            double c11 = 8.81749162E02;
            double cosp2 = cosp * cosp;
            double sinp2 = sinp * sinp;
            double tanp = Math.tan(latrad);

//            double esq = 6.768658E-3;
//            double eps = 6.8147849E-3;
            double w2 = 1.0 - esq * sinp2;
            double v2x = 1.0 + eps * cosp2;
            double latsec = latrad * RHOSEC;
            double lonsec = lonrad * RHOSEC;
            double s1 = c1 * cosp * (t2 - lonsec - c2 * Math.pow((t2 - lonsec) / 10000.0, 3)) / Math.sqrt(w2);
            double sm = s1 + c3 * Math.pow(s1 * 1.0E-5, 3);
            double east = t1 + c4 * sm * t5 + t6 * Math.pow((c4 * sm * t5 * 1.0E-5), 3);

            double p1sec = latsec + c5 * sm * sm * w2 * w2 * tanp;
            double p1 = p1sec / RHOSEC;
            double sinp1 = Math.sin(p1);
            double tanp1 = Math.tan(p1);
            w2 = 1.0 - esq * sinp1 * sinp1;
            double p2sec = latsec + c5 * sm * sm * w2 * w2 * tanp1;
            double p2 = p2sec / RHOSEC;
            cosp2 = Math.cos(p2);
            double cosp2s = cosp2 * cosp2;
            sinp2 = Math.sin(p2);
            double sinm = Math.sin((latrad + p2) / 2.0);
            double cosm = Math.cos((latrad + p2) / 2.0);
            double tms = t2 - lonsec;
            double north = c6 * t5 * (p2sec - t34 - (c7 - (c8 - c9 * cosp2s) * cosp2s) * sinp2 * cosp2);
            // convergence is decimal degrees
            double convergence = tms * sinm * (1.0 + c10 * tms * tms * cosm * cosm) / 3600.0;
            // scale factor
            double scaleFactor = t5 * (1.0 + (v2x * v2x * ((east - t1) * (east - t1) * 1.E-12)) / (c11 * t5 * t5));
            retStr += "," + String.format(northFormat, north);
            retStr += "," + String.format(eastFormat, east);
            retStr += "," + String.format(convFormat, convergence);
            retStr += "," + String.format(sfFormat, scaleFactor);

        }

        return retStr;
    }

    @Override
    public String toGeodeticCoordinates(double north, double east) {

        setConstants();
        if (isUtm()) {
            setConstants(0.0, 0.0, true);
            label = "UTM";
        } else {
            if (datum.equals("nad27")) {   //nad27 SPC conversion
                return toGeodeticCoordinates(north, east, zone);
            }
        }
        String geodeticStr = datum + "," + zone + "," + label;
        double scaleFactor = sf > 1.0 ? 1.0 - 1.0 / sf : 1.0;
        double om = ((north - falseNorthing) * unitsCf + so) / (r * scaleFactor);
        double cosom = Math.cos(om);
        double cosom2 = Math.pow(cosom, 2.0);
        double cosom4 = Math.pow(cosom, 4.0);
        double cosom6 = Math.pow(cosom, 6.0);
        double f = om + Math.sin(om) * cosom * (v0 + v2 * cosom2 + v4 * cosom4 + v6 * cosom6);
        double sinf = Math.sin(f);
        double cosf = Math.cos(f);
        double tn = sinf / cosf;
        double ts = tn * tn;
        double ets = eps * cosf * cosf;
        double rn = radius * scaleFactor / Math.sqrt(1.0 - esq * sinf * sinf);
        double q = (east - falseEasting) * unitsCf / rn;
        double qs = q * q;
        double b2 = -tn * (1.0 + ets) / 2.0;
        double b4 = -(5.0 + 3.0 * ts + ets * (1.0 - 9.0 * ts) - 4.0 * ets * ets) / 12.0;
        double b6 = (61.0 + 45.0 * ts * (2.0 + ts) + ets * (46.0 - 252.0 * ts - 60.0 * ts * ts)) / 360.0;
        double b1 = 1.0;
        double b3 = -(1.0 + ts + ts + ets) / 6.0;
        double b5 = (5.0 + ts * (28.0 + 24.0 * ts) + ets * (6.0 + 8.0 * ts)) / 120.0;
        double b7 = -(61.0 + 662.0 * ts + 1320.0 * ts * ts + 720.0 * Math.pow(ts, 3.0)) / 5040.0;
        double lat = f + b2 * qs * (1.0 + qs * (b4 + b6 * qs));
        //       System.out.println("r:" + r + " sf:" + sf + " so:" + so + " om:" + om + " f" + f + " qs:" + qs + " b2:" + b2 + " b4:" + b4 + " b6:" + b6);
        double l = b1 * q * (1.0 + qs * (b3 + qs * (b5 + b7 * qs)));
        double lon = l / cosf + cm;

        double sinfi = Math.sin(lat);
        double cosfi = Math.cos(lat);
        tn = sinfi / cosfi;
        ts = Math.pow(tn, 2.0);
        ets = eps * Math.pow(cosfi, 2.0);
        l = getLonDiff(lon, cm) * cosfi;
        double ls = l * l;
        double c1 = -tn;
        double c3 = (1.0 + 3.0 * ets + 2.0 * Math.pow(ets, 2.0)) / 3.0;
        double c5 = (2.0 - ts) / 15.0;
        double convergence = c1 * l * (1.0 + ls * (c3 + c5 * ls));
        convergence = southernHemisphere ? convergence : -convergence;

        double f2 = (1. + ets) / 2.0;
        double f4 = (5.0 - 4.0 * ts + ets * (9.0 - 24.0 * ts)) / 12.0;
        scaleFactor = scaleFactor * (1.0 + f2 * ls * (1. + f4 * ls));
        geodeticStr += "," + String.format(latFormat, lat / degreesToRadians);
        geodeticStr += "," + String.format(lonFormat, CoordinateTransformation.resetLon(lon / degreesToRadians));
        geodeticStr += "," + String.format(convFormat, convergence / degreesToRadians);
        geodeticStr += "," + String.format(sfFormat, scaleFactor);
        return geodeticStr;
    }

    private String toGeodeticCoordinates(double north, double east, String zone) {
        String geodeticStr = datum + "," + zone + "," + label;
        // constants that vary by TM zone
        // New Jersey zone 2900
        int index = Integer.valueOf(zoneConstants.getProperty(zone + ".constIndex", "0"));
        if (index == 0) {  //AK zones
            double t = 6.8147849E-3;
            double o1sec = 1.93900054420E05 + 9.87466302498E-3 * north;
            double coso1 = Math.cos(o1sec / RHOSEC);
            double coso12 = coso1 * coso1;
            double sino1 = Math.sin(o1sec / RHOSEC);

            double p1sec = o1sec + sino1 * coso1 * (1.047546691E3 + coso12 * (6.193011E0
                    + 5.0699E-2 * coso12));
            double cosp1 = Math.cos(p1sec / RHOSEC);
            double cosp12 = cosp1 * cosp1;
            double cosp14 = cosp12 * cosp12;
            double tanp1 = Math.tan(p1sec / RHOSEC);
            double tanp12 = tanp1 * tanp1;
            double w = 1.E0 + t * cosp12;
            double w2 = w * w;
            double far = (east - falseEasting) * 1.E-6;
            double far2 = far * far;
            double far4 = far2 * far2;

            double psec = p1sec - 2.33973645E2 * far2 * w2 * tanp1 * (1.E0 - 1.8905604E-4 * far2
                    * (1.9591113E0 + 3.E0 / cosp12 + 8.1359E-2 * cosp12 + 2.79E-4 * cosp14)
                    + 1.42969E-8 * far4 * w * (15.5E0 + 45.E0 / cosp14 - 0.307E0 / cosp12 + 1.53E0 * cosp12));
            double decLat = psec / 3600.0;
            double esec = Math.abs(cm * RHOSEC) - 9.824513072E3 * Math.sqrt(w) / cosp1 * far * (1.E0 - 3.7811208E-4 * w
                    * far2 * (2.E0 * tanp12 + w) + 4.2890624E-8 * w2 * far4 * (1.054E0 + 24.E0 / cosp14
                    - 20.E0 / cosp12 - 1.36E-2 * cosp12));
            double decLon = esec / 3600.0;
            double latrad = decLat * degreesToRadians;
            double cosp = Math.cos(latrad);
            double cosp2 = Math.pow(cosp, 2);
            double cosp4 = Math.pow(cosp, 4);
            double cosp6 = Math.pow(cosp, 4);
            double dlon = (Math.abs(cm * RHOSEC) - decLon * 3600) / 1.E4;
            double dlon2 = Math.pow(dlon, 2);
            double dlon4 = Math.pow(dlon, 4);
            double s2 = Math.sqrt(1.E0 + t * cosp2);
            double s3 = Math.sqrt(1.E0 - cosp2) * cosp;
            double convergence = s3 / cosp * dlon * (1.E4 + 7.83481E0 * dlon2 * (cosp2 + 2.044E-2 * cosp4
                    + 0.9E-4 * cosp6) + 0.3683E-2 * dlon4 * (3.E0 * cosp4 - cosp2)) / 3600;
            double scaleFactor = 0.9999E0 * (1.E0 + Math.pow(s2, 4) / 8.81572821E2 * Math.pow((east - falseEasting) / 1.E6, 2));
            geodeticStr += "," + String.format(latFormat, decLat);
            geodeticStr += "," + String.format(lonFormat, CoordinateTransformation.resetLon(-decLon));
            geodeticStr += "," + String.format(convFormat, convergence);
            geodeticStr += "," + String.format(sfFormat, scaleFactor);
            return geodeticStr;
        } else {
            // constants vary by TM zone
            // New Jersey zone 2900 has a different constant
            double t1 = zone.equals("2900") ? 2.0E6 : 0.5E06;

            int l = index * 4;
            double t2 = NAD27_SPC_TM_CONSTANTS[l - 4];
            double t34 = NAD27_SPC_TM_CONSTANTS[l - 3];
            double t5 = NAD27_SPC_TM_CONSTANTS[l - 2];
            double t6 = NAD27_SPC_TM_CONSTANTS[l - 1];

            //           double esq = 6.768658E-3;
            double d1 = 0.3048006099E00;
            double d2 = 0.9873675553E-2;
            double d3 = 1.047546710E03;
            double d4 = 6.192760E00;
            double d5 = 5.0912E-2;
            double d6 = 2.552381E01;
            double d7 = 4.0831E00;
            double d8 = 3.092241724E01;
            double d9 = 3.9174E00;
            double c5 = 2.552381E-9;
            double c10 = 1.9587E-12;
            double c11 = 8.81749162E02;

            double sg1 = (east - t1) * (1.0 - t6 * 1.E-15 * (east - t1) * (east - t1));
            double sm = d1 * (east - t1 - t6 * 1.E-15 * Math.pow(sg1, 3)) / t5;
            double omsec = t34 + d2 * north / t5;
            double omega = omsec / RHOSEC;
            double sino = Math.sin(omega);
            double coso = Math.cos(omega);
            double cos2o = coso * coso;
            double p1sec = omsec + (d3 + (d4 + d5 * cos2o) * cos2o) * sino * coso;
            double p1rad = p1sec / RHOSEC;
            double sinp1 = Math.sin(p1rad);
            double sin2p1 = sinp1 * sinp1;
            double tanp1 = Math.tan(p1rad);
            double w = 1.0 - esq * sin2p1;
            double psec = p1sec - d6 * w * w * tanp1 * sm * sm * 1.E-10;
            double p = psec / RHOSEC;
            double sinp = Math.sin(p);
            double cosp = Math.cos(p);
            w = Math.sqrt(1.E0 - esq * sinp * sinp);
            double sa = sm * (1.0 - d7 * sm * sm * 1.E-15);
            double s1 = sm - d7 * 1.E-15 * Math.pow(sa, 3);
            double dl1sec = s1 * w / (d8 * cosp);
            double dlasec = dl1sec * (1.E0 + d9 * dl1sec * dl1sec * 1.E-12);
            double esec = t2 - dl1sec - d9 * 1.E-12 * Math.pow(dlasec, 3);
            double decLat = psec / 3600.0;
            double decLon = -esec / 3600.0;
            double tms = t2 - esec;
            double w2 = 1.0 - esq * sinp1 * sinp1;
            double p2sec = psec + c5 * sm * sm * w2 * w2 * tanp1;
            double p2 = p2sec / RHOSEC;
            double cosm = Math.cos((p + p2) / 2.0);
            double sinm = Math.sin((p + p2) / 2.0);
            double cosp2 = cosp * cosp;

            double v2x = 1.0 + eps * cosp2;

            double convergence = tms * sinm * (1.0 + c10 * tms * tms * cosm * cosm) / 3600.0;
            // scale factor
            double scaleFactor = t5 * (1.0 + (v2x * v2x * ((east - t1) * (east - t1) * 1.E-12)) / (c11 * t5 * t5));

            geodeticStr += "," + String.format(latFormat, decLat);
            geodeticStr += "," + String.format(lonFormat, CoordinateTransformation.resetLon(decLon));
            geodeticStr += "," + String.format(convFormat, convergence);
            geodeticStr += "," + String.format(sfFormat, scaleFactor);
            return geodeticStr;
        }
    }

    @Override
    public String toGeodeticCoordinates(double north, double east, boolean southernHemisphere) {
        this.southernHemisphere = southernHemisphere;
        return toGeodeticCoordinates(north, east);

    }

    @Override
    protected void setConstants() {
        datum = zoneDef.getProperty(zone + ".datum", datum);
        a = Double.parseDouble(zoneConstants.getProperty("tm.a", "0.0"));
        b = Double.parseDouble(zoneConstants.getProperty("tm.b", "0.0"));
        c = Double.parseDouble(zoneConstants.getProperty("tm.c", "0.0"));
        r = Double.parseDouble(zoneConstants.getProperty("tm.r", "0.0"));
        v0 = Double.parseDouble(zoneConstants.getProperty("tm.v0", "0.0"));
        v2 = Double.parseDouble(zoneConstants.getProperty("tm.v2", "0.0"));
        v4 = Double.parseDouble(zoneConstants.getProperty("tm.v4", "0.0"));
        v6 = Double.parseDouble(zoneConstants.getProperty("tm.v6", "0.0"));
        so = Double.parseDouble(zoneConstants.getProperty(zone + ".const", "0.0"));
        cm = Double.parseDouble(zoneDef.getProperty(zone + ".cm", "0.0")) * degreesToRadians;
        sf = Double.parseDouble(zoneDef.getProperty(zone + ".sf", "0.0"));
        falseEasting = Double.parseDouble(zoneDef.getProperty(zone + ".fe", "0.0"));
        falseNorthing = Double.parseDouble(zoneDef.getProperty(zone + ".fn", "0.0"));

    }

    /**
     * overloaded for UTM transformation
     *
     * @param lat
     * @param lon
     * @param toGeodetic true=geodetic transformation
     */
    private void setConstants(double lat, double lon, boolean toGeodetic) {
        falseEasting = Double.parseDouble(zoneDef.getProperty("utm.fe", "0.0"));
        sf = Double.parseDouble(zoneDef.getProperty("utm.sf", "0.0"));
        // false north for southern hemisphere
        double shfn = Double.parseDouble(zoneDef.getProperty("utm.shfn", "0.0"));
        if (toGeodetic) {
            falseNorthing = southernHemisphere ? shfn : 0.0;
            cm = ((Integer.parseInt(zone.trim()) - 1) * 6 - 177) * degreesToRadians;
        } else {
            falseNorthing = lat >= 0.0 ? 0.0 : shfn;
            southernHemisphere = lat < 0.0;
//            int utmZone = CoordinateTransformation.findUtmZone(lon);
            int utmZone = Integer.valueOf(zone);
            utmZone = resetUtmZone(lat, lon, utmZone);
            cm = ((utmZone - 1) * 6 - 177) * degreesToRadians;
            zone = String.format("%4d", utmZone);
        }

    }

    /**
     * resets utm zone in the x and v latitude bands for Norway and Svalbard
     * regions
     *
     * @param lat
     * @param lon
     * @param utmZone
     * @return
     */
    private int resetUtmZone(double lat, double lon, int utmZone) {
        int uzone = utmZone;
        if ((lat > 56.0 && lat <= 64.0) && (lon > 3.0 && lon <= 6)) {
            uzone = 32;
        } else if (lat > 72.0) {
            if (lon > 0.0 && lon <= 9.0) {
                uzone = 31;
            } else if (lon > 9.0 && lon <= 21.0) {
                uzone = 33;
            } else if (lon > 21.0 && lon <= 33.0) {
                uzone = 35;
            } else if (lon > 33.0 && lon < 42.0) {
                uzone = 37;
            }
        }
        return uzone;
    }

    /**
     * returns projected coordinate for a specific ellipsoid; supports
     * international ellipsoids; to be used for UTM transformation only
     *
     * @param radius equatorial radius
     * @param f flattening
     * @param lat latitude
     * @param lon longitude
     * @return projected coordinate
     */
    @Override
    public String toProjectedCoordinates(double radius, double f, double lat, double lon) {
        lon = CoordinateTransformation.resetLon(lon);
        double pr = (1.0 - f) * radius;
        double en = (radius - pr) / (radius + pr);
        double en2 = Math.pow(en, 2.0);
        double en3 = Math.pow(en, 3.0);
        double en4 = Math.pow(en, 4.0);
        double a = -1.5 * en + (9.0 / 16.0) * en3;
        double b = 0.9375 * en2 - (15.0 / 32.0) * en4;
        double c = -(35.0 / 48.0) * en3;
        double r = radius * (1.0 - en) * (1. - en2) * (1. + 2.25 * en2 + (225.0 / 64.0) * en4);
        double c2 = 3.0 * en / 2.0 - 27.0 * en3 / 32.0;
        double c4 = 21.0 * en2 / 16.0 - 55.0 * en4 / 32.0;
        double c6 = 151.0 * en3 / 96.0;
        double c8 = 1097.0 * en4 / 512.0;
        double v0 = 2.0 * (c2 - 2.0 * c4 + 3.0 * c6 - 4.0 * c8);
        double v2 = 8.0 * (c4 - 4.0 * c6 + 10.0 * c8);
        double v4 = 32.0 * (c6 - 6.0 * c8);
        double v6 = 128.0 * c8;
        double so = 0.0;

        double esq = 2 * f - Math.pow(f, 2.0); //eccentricity(e) squared
        double eps = esq / (1.0 - esq);        //eprime squared

        if (isUtm()) {
            setConstants(lat, lon, false);
            label = "UTM";
        }
        String retStr = datum + "," + zone + "," + label;
        lat *= degreesToRadians;
        lon *= degreesToRadians;

        double scaleFactor = sf > 1.0 ? 1.0 - 1.0 / sf : 1.0;
        double om = lat + a * Math.sin(2.0 * lat) + b * Math.sin(4.0 * lat) + c * Math.sin(6.0 * lat);
        double s = r * om * scaleFactor;
        double sinfi = Math.sin(lat);
        double cosfi = Math.cos(lat);
        double tn = sinfi / cosfi;
        double ts = Math.pow(tn, 2.0);
        double ets = eps * Math.pow(cosfi, 2.0);
        double l = getLonDiff(lon, cm) * cosfi;
        double ls = l * l;
        double rn = scaleFactor * radius / Math.sqrt(1.0 - esq * Math.pow(sinfi, 2.0));
        double a2 = rn * tn / 2.0;
        double a4 = (5.0 - ts + ets * (9.0 + 4. * ets)) / 12.0;
        double a6 = (61.0 + ts * (ts - 58.0) + ets * (270.0 - 330.0 * ts)) / 360.0;
        double a1 = -rn;
        double a3 = (1.0 - ts + ets) / 6.0;
        double a5 = (5.0 + ts * (ts - 18.0) + ets * (14.0 - 58.0 * ts)) / 120.0;
        double a7 = (61.0 - 479.0 * ts + 179.0 * Math.pow(ts, 2.0) - Math.pow(ts, 3.0)) / 5040.0;
        double north = (s - so + a2 * ls * (1.0 + ls * (a4 + a6 * ls))) / unitsCf + falseNorthing;
        double east = falseEasting - (a1 * l * (1.0 + ls * (a3 + ls * (a5 + a7 * ls)))) / unitsCf;
        double c1 = -tn;
        double c3 = (1.0 + 3.0 * ets + 2.0 * Math.pow(ets, 2.0)) / 3.0;
        double c5 = (2.0 - ts) / 15.0;
        double convergence = c1 * l * (1.0 + ls * (c3 + c5 * ls));
        convergence = southernHemisphere ? convergence : -convergence;

        double f2 = (1. + ets) / 2.0;
        double f4 = (5.0 - 4.0 * ts + ets * (9.0 - 24.0 * ts)) / 12.0;
        scaleFactor = scaleFactor * (1.0 + f2 * ls * (1. + f4 * ls));
        retStr += "," + String.format(northFormat, north);
        retStr += "," + String.format(eastFormat, east);
        retStr += "," + String.format(convFormat, convergence / degreesToRadians);
        retStr += "," + String.format(sfFormat, scaleFactor);
        return retStr;

    }

    /**
     *
     * returns geodetic coordinate for a specific ellipsoid; supports
     * international ellipsoids; to be used for UTM transformation only
     *
     * @param radius equatorial radius
     * @param fl flattening
     * @param north northing
     * @param east easting
     * @param southernHemisphere true=SH
     * @return geodetic coordinate
     */
    @Override
    public String toGeodeticCoordinates(double radius, double fl, double north, double east, boolean southernHemisphere) {
        this.southernHemisphere = southernHemisphere;
        double pr = (1.0 - fl) * radius;
        double en = (radius - pr) / (radius + pr);
        double en2 = Math.pow(en, 2.0);
        double en3 = Math.pow(en, 3.0);
        double en4 = Math.pow(en, 4.0);
        double a = -1.5 * en + (9.0 / 16.0) * en3;
        double b = 0.9375 * en2 - (15.0 / 32.0) * en4;
        double c = -(35.0 / 48.0) * en3;
        double r = radius * (1.0 - en) * (1. - en2) * (1. + 2.25 * en2 + (225.0 / 64.0) * en4);
        double c2 = 3.0 * en / 2.0 - 27.0 * en3 / 32.0;
        double c4 = 21.0 * en2 / 16.0 - 55.0 * en4 / 32.0;
        double c6 = 151.0 * en3 / 96.0;
        double c8 = 1097.0 * en4 / 512.0;
        double v0 = 2.0 * (c2 - 2.0 * c4 + 3.0 * c6 - 4.0 * c8);
        double v2 = 8.0 * (c4 - 4.0 * c6 + 10.0 * c8);
        double v4 = 32.0 * (c6 - 6.0 * c8);
        double v6 = 128.0 * c8;
        double so = 0.0;

        double esq = 2 * fl - Math.pow(fl, 2.0); //eccentricity(e) squared
        double eps = esq / (1.0 - esq);        //eprime squared

        if (isUtm()) {
            setConstants(0.0, 0.0, true);
            label = "UTM";
        }
        String geodeticStr = datum + "," + zone + "," + label;
        double scaleFactor = sf > 1.0 ? 1.0 - 1.0 / sf : 1.0;
        double om = ((north - falseNorthing) * unitsCf + so) / (r * scaleFactor);
        double cosom = Math.cos(om);
        double cosom2 = Math.pow(cosom, 2.0);
        double cosom4 = Math.pow(cosom, 4.0);
        double cosom6 = Math.pow(cosom, 6.0);
        double f = om + Math.sin(om) * cosom * (v0 + v2 * cosom2 + v4 * cosom4 + v6 * cosom6);
        double sinf = Math.sin(f);
        double cosf = Math.cos(f);
        double tn = sinf / cosf;
        double ts = tn * tn;
        double ets = eps * cosf * cosf;
        double rn = radius * scaleFactor / Math.sqrt(1.0 - esq * sinf * sinf);
        double q = (east - falseEasting) * unitsCf / rn;
        double qs = q * q;
        double b2 = -tn * (1.0 + ets) / 2.0;
        double b4 = -(5.0 + 3.0 * ts + ets * (1.0 - 9.0 * ts) - 4.0 * ets * ets) / 12.0;
        double b6 = (61.0 + 45.0 * ts * (2.0 + ts) + ets * (46.0 - 252.0 * ts - 60.0 * ts * ts)) / 360.0;
        double b1 = 1.0;
        double b3 = -(1.0 + ts + ts + ets) / 6.0;
        double b5 = (5.0 + ts * (28.0 + 24.0 * ts) + ets * (6.0 + 8.0 * ts)) / 120.0;
        double b7 = -(61.0 + 662.0 * ts + 1320.0 * ts * ts + 720.0 * Math.pow(ts, 3.0)) / 5040.0;
        double lat = f + b2 * qs * (1.0 + qs * (b4 + b6 * qs));
        //       System.out.println("r:" + r + " sf:" + sf + " so:" + so + " om:" + om + " f" + f + " qs:" + qs + " b2:" + b2 + " b4:" + b4 + " b6:" + b6);
        double l = b1 * q * (1.0 + qs * (b3 + qs * (b5 + b7 * qs)));
        double lon = l / cosf + cm;

        double sinfi = Math.sin(lat);
        double cosfi = Math.cos(lat);
        tn = sinfi / cosfi;
        ts = Math.pow(tn, 2.0);
        ets = eps * Math.pow(cosfi, 2.0);
        l = getLonDiff(lon, cm) * cosfi;
        double ls = l * l;
        double c1 = -tn;
        double c3 = (1.0 + 3.0 * ets + 2.0 * Math.pow(ets, 2.0)) / 3.0;
        double c5 = (2.0 - ts) / 15.0;
        double convergence = c1 * l * (1.0 + ls * (c3 + c5 * ls));
        convergence = southernHemisphere ? convergence : -convergence;

        double f2 = (1. + ets) / 2.0;
        double f4 = (5.0 - 4.0 * ts + ets * (9.0 - 24.0 * ts)) / 12.0;
        scaleFactor = scaleFactor * (1.0 + f2 * ls * (1. + f4 * ls));
        geodeticStr += "," + String.format(latFormat, lat / degreesToRadians);
        geodeticStr += "," + String.format(lonFormat, CoordinateTransformation.resetLon(lon / degreesToRadians));
        geodeticStr += "," + String.format(convFormat, convergence / degreesToRadians);
        geodeticStr += "," + String.format(sfFormat, scaleFactor);
        return geodeticStr;
    }

    @Override
    public String toGeodeticCoordinates(double x, double y, double z) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String toProjectedCoordinates(double lat, double lon, double height) {
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

    public static void main(String[] args) {
        double lat = 62.0;
        double lon = -152.0;
        String zone = "5006";
        CoordinateTransformation c = CoordinateTransformation.getInstance(zone, "nad27");
        System.out.println(c.toProjectedCoordinates(lat, lon));
//        System.out.println(c.toGeodeticCoordinates(1093083.261420, 171660.728526));
//        c = CoordinateTransformation.getInstance("5002", "nad27");
//        System.out.println(c.toProjectedCoordinates(64.0, -143.0));
//        System.out.println(c.toGeodeticCoordinates(3655605.449270, 339477.590847));

    }
}
