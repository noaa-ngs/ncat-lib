package gov.noaa.ngs.transform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A base class for coordinate transformation
 *
 * @author Krishna Tadepalli
 * @version 1.0 Date: 06/10/2011
 *
 */
public abstract class CoordinateTransformation {

    public static final double TWOPI = 2 * Math.PI;
    public static final String NAD83_CONSTANTS_PROP = "/gov/noaa/ngs/transform/resources/nad83ZoneConstants.properties";
    public static final String NAD83_ZONE_DEF_PROP = "/gov/noaa/ngs/transform/resources/nad83ZoneDefinitions.properties";
    public static final String NAD27_CONSTANTS_PROP = "/gov/noaa/ngs/transform/resources/nad27ZoneConstants.properties";
    public static final String NAD27_ZONE_DEF_PROP = "/gov/noaa/ngs/transform/resources/nad27ZoneDefinitions.properties";
    public static final double UTM_TOLERANCE = 0.0; //used to adjust UTM zone
    public static final int SPC_TRANSFORMATION = 1;
    public static final int UTM_TRANSFORMATION = 2;
    protected Properties zoneConstants;  //zone-specific constants
    protected Properties zoneDef;       //zone definitions
    private static Properties nad83ZoneDef;
    private static Properties nad27ZoneDef;
    private static Properties nad83ZoneConstants;
    private static Properties nad27ZoneConstants;
    protected double degreesToRadians;
    protected double radius;      //equitorial radius
    protected double e;           //eccentricity
    protected double esq;         //eccentricity squared
    protected double eps;         //eprime squared
    protected double eflattening;  //earth flattening
    protected String zone;     //numeric ID of a zone
    protected String label;    //alphanumeric label of a zone
    protected int transformation; //transformation being used (spc or utm)
    protected String datum; //datum tag
    //String format used for coordinates
    protected String northFormat;
    protected String eastFormat;
    protected String latFormat;
    protected String lonFormat;
    protected String heightFormat;
    protected String xyzFormat;
    protected String sfFormat;
    protected String convFormat;
    protected String secondsFormat;
    protected double metersToUsfeet;
    protected double metersToIntlfeet;
    //factor used for converting units of projected coordinates
    //defaults to 1.0 for meters. Set to metersToUsfeet for
    //nad27 SPC coordinates
    protected double unitsCf;

    static {
        try {
            loadZoneDefinitions();
            loadZoneConstants();
        } catch (CTException ex) {
            //To do: send the logger to a file
            Logger.getLogger(CoordinateTransformation.class.getName()).log(Level.SEVERE, ex.getMessage());
        }

    }

    /**
     * initializes constants common to all transformations
     *
     * @param zoneDef zone definitions
     * @param zoneConstants zone constants
     */
    protected CoordinateTransformation(Properties zoneDef, Properties zoneConstants) {
        this.zoneDef = zoneDef;
        this.zoneConstants = zoneConstants;
        datum = zoneDef.getProperty("datum", "");
        degreesToRadians = Double.parseDouble(zoneConstants.getProperty("deg2rad", "0.0"));
        radius = Double.parseDouble(zoneConstants.getProperty("radius", "0.0"));
        e = Double.parseDouble(zoneConstants.getProperty("e", "0.0"));
        esq = Double.parseDouble(zoneConstants.getProperty("esq", "0.0"));
        eps = Double.parseDouble(zoneConstants.getProperty("eps", "0.0"));
        eflattening = Double.parseDouble(zoneConstants.getProperty("eflattening", "0.0"));
        northFormat = zoneDef.getProperty("north.format", "");
        eastFormat = zoneDef.getProperty("east.format", "");
        latFormat = zoneDef.getProperty("lat.format", "");
        lonFormat = zoneDef.getProperty("lon.format", "");
        heightFormat = zoneDef.getProperty("height.format", "");
        xyzFormat = zoneDef.getProperty("xyz.format", "");
        convFormat = zoneDef.getProperty("conv.format", "");
        sfFormat = zoneDef.getProperty("sf.format", "");
        secondsFormat = zoneDef.getProperty("seconds.format", "");
        metersToUsfeet = Double.parseDouble(zoneDef.getProperty("meters2usfeet", "0.0"));
        metersToIntlfeet = Double.parseDouble(zoneDef.getProperty("meters2intlfeet", "0.0"));
    }

    /**
     * loads zone definitions from a property file
     *
     * @return reference to property file
     * @throws CTException
     */
    private static void loadZoneDefinitions() throws CTException {
        nad83ZoneDef = new Properties();
        nad27ZoneDef = new Properties();
        InputStream nad83Stream = CoordinateTransformation.class.getResourceAsStream(NAD83_ZONE_DEF_PROP);
        InputStream nad27Stream = CoordinateTransformation.class.getResourceAsStream(NAD27_ZONE_DEF_PROP);
        if (nad83Stream == null || nad27Stream == null) {
            throw new CTException("Unable to find zone definition property file");
        } else {
            try {
                nad83ZoneDef.load(nad83Stream);
                nad27ZoneDef.load(nad27Stream);
            } catch (IOException ex) {
                throw new CTException("Unable to load zone definition property file. Exception:" + ex.getMessage());
            }
        }
    }

    /**
     * loads zone constants from a property file
     *
     * @throws CTException
     */
    private static void loadZoneConstants() throws CTException {
        InputStream nad83Stream = CoordinateTransformation.class.getResourceAsStream(NAD83_CONSTANTS_PROP);
        InputStream nad27Stream = CoordinateTransformation.class.getResourceAsStream(NAD27_CONSTANTS_PROP);
        nad83ZoneConstants = new Properties();
        nad27ZoneConstants = new Properties();
        if (nad83Stream == null || nad27Stream == null) {
            throw new CTException("Unable to find zone constants property file");
        } else {
            try {
                nad83ZoneConstants.load(nad83Stream);
                nad27ZoneConstants.load(nad27Stream);
            } catch (IOException ex) {
                throw new CTException("Unable to load zone constants property file. Exception:" + ex.getMessage());
            }
        }
    }

    /**
     * converts DMS to decimal degrees using -ve west longitude convention
     *
     * @param degrees degrees
     * @param minutes minutes
     * @param seconds seconds
     * @return decimal degrees
     */
    public static double toDecimalDeg(double degrees, double minutes,
            double seconds) {
        return  (degrees + minutes / 60.0 + seconds / 3600.0);
                
    }

    /**
     * resets longitude to be within +/- 180.0
     *
     * @param lon longitude
     * @return longitude
     */
    public static double resetLon(double lon) {
        if (lon > 180.0 || lon <= -180.0) {
            return lon > 180.0 ? lon - 360.0 : lon + 360.0;
        }
        return lon;
    }

    /**
     * converts a DMS string to decimal degrees
     *
     * @param dmsStr - expected format N/SDDMMSS.*s or E/WDDDMMSS.*s
     * @return decimal degrees
     */
    public static double toDecimalDeg(String dmsStr) {
        if (dmsStr == null || dmsStr.length() < 7) {
            return 0.0;
        }
        boolean validDms = dmsStr.charAt(0) == 'N' || dmsStr.charAt(0) == 'S'
                || dmsStr.charAt(0) == 'E' || dmsStr.charAt(0) == 'W';
        if (!validDms) {
            return 0.0;
        }
        boolean lon = dmsStr.charAt(0) == 'E' || dmsStr.charAt(0) == 'W';
        int indexOff = lon ? 1 : 0;
        int deg = Integer.parseInt(dmsStr.substring(1, 3 + indexOff));
        int min = Integer.parseInt(dmsStr.substring(3 + indexOff, 5 + indexOff));
        double sec = Double.parseDouble(dmsStr.substring(5 + indexOff));
        double decimalDeg = toDecimalDeg(deg, min, sec);
        return dmsStr.charAt(0) == 'W' || dmsStr.charAt(0) == 'S' ? -decimalDeg : decimalDeg;
        
    }

    /**
     * converts decimal degrees to DMS
     *
     * @param decimalDegree decimal degrees
     * @return a white space delimited DMS. Seconds formatted per seconds format
     */
    public static String toDMS(double decimalDegree) {
        decimalDegree = resetLon(decimalDegree);
        double[] dms = new double[3];
        int degrees = (int) decimalDegree;
        double seconds = Math.abs((decimalDegree - degrees) * 3600);
        int minutes = (int) (seconds / 60.0);
        seconds -= minutes * 60;
        dms[0] = decimalDegree < 0 ? -(double) degrees : (double) degrees;
        dms[1] = (double) minutes;
        dms[2] = seconds;
        return dms[0] + " " + dms[1] + " " + String.format("%8.5f", dms[2]);
    }

    /**
     * converts decimal degrees to formatted DMS caller should validate decimal
     * degrees
     *
     * @param decimalDegree decimal degrees
     * @param isLat true=latitude used for conversion
     * @return DMS with a hemisphere designator
     */
    public static String toDMS(double decimalDegree, boolean isLat) {
        decimalDegree = isLat ? decimalDegree : resetLon(decimalDegree);
        int degrees = (int) decimalDegree;
        double seconds = Math.abs((decimalDegree - degrees) * 3600);
        degrees = Math.abs(degrees);
        int minutes = (int) (seconds / 60.0);
        seconds -= minutes * 60;
        seconds = Math.round(seconds * 100000) / 100000.0;
        if (seconds >= 60){
            seconds -= 60;
            minutes++;
            if (minutes >= 60){
                minutes -= 60;
                degrees ++;
            }
        }
        String degStr;
        String minStr = String.format("%02d", minutes);
        String secStr = String.format("%08.5f", seconds);
        if (isLat) {
            degStr = decimalDegree < 0 ? "S" + String.format("%02d", degrees)
                    : "N" + String.format("%02d", degrees);
        } else {
            degStr = decimalDegree < 0 ? "W" + String.format("%03d", degrees)
                    : "E" + String.format("%03d", degrees);
        }
        return degStr + minStr + secStr;

    }

    /**
     * verifies whether lat-long string is in DMS format based on a prefix
     *
     * @param coordStr pattern being tested
     * @return true=DMS format
     */
    public static boolean isDmsFormat(String coordStr) {
        return (coordStr.charAt(0) == 'N' || coordStr.charAt(0) == 'S'
                || coordStr.charAt(0) == 'E' || coordStr.charAt(0) == 'W');
    }

    /**
     * converts meters to US Survey Feet
     *
     * @param meters meters being converted
     * @return US Survet feet
     */
    public double toUSSurveyFeet(double meters) {
        return meters / metersToUsfeet;

    }

    public double toMeters(double value, String units) {
        return (units.equalsIgnoreCase("usft") ? value * metersToUsfeet : value * metersToIntlfeet);
    }

    /**
     * converts meters to International Feet
     *
     * @param meters meters being converted
     * @return international survey feet
     */
    public double toInternationalFeet(double meters) {
        return meters / metersToIntlfeet;

    }

    /**
     * returns difference between two longitudes and adjusts it to be within PI
     * radians
     *
     * @param lon1 longitude1 in radians
     * @param lon2 longitude2 in radians
     * @return difference between longitude1 and longitude2
     */
    public double getLonDiff(double lon1, double lon2) {
        double lonDiff = lon1 - lon2;
        if (lonDiff > Math.PI) {
            lonDiff -= TWOPI;
        } else if (lonDiff < -Math.PI) {
            lonDiff += TWOPI;
        }
        return lonDiff;

    }

    /**
     * computes elevation factor as a function of latitude and ellipsoid height
     *
     * @param lat latitude
     * @param ellipHeight ellipsoid height
     * @return elevation factor
     */
    public double getElevationFactor(double lat, double ellipHeight) {
        lat *= degreesToRadians;
        double tmp = Math.sqrt(1.0 - esq * Math.sin(lat) * Math.sin(lat));
        double tmp3 = Math.pow(tmp, 3);
        double n = radius / tmp;
        double m = radius * (1.0 - esq) / tmp3;
        double ra = 2 * m * n / (m + n);
        return ra / (ra + ellipHeight);
    }

    /**
     * allows the caller to override auto selection or specify a zone for
     * inverse transformation
     *
     * @param zone SPC/UTM zone#
     */
    public void setZone(String zone) {
        this.zone = zone;
        this.label = zoneDef.getProperty(zone + ".label", "");

    }

    public void setUnitsCf(double unitsCf) {
        this.unitsCf = unitsCf;
    }

    /**
     * finds a UTM zone for a given longitude (west -ve convention used)
     *
     * @param lon longitude
     * @return UTM zone
     */
    public static int findUtmZone(double lon) {
        lon = resetLon(lon);
        lon += 180.0;
        int uzone = ((int) lon) / 6 + 1;
        uzone = uzone > 60 ? uzone - 60 : uzone;
        int lowerBound = (uzone - 1) * 6;
        int upperBound = uzone * 6;
        if (Math.abs(lon - lowerBound) <= UTM_TOLERANCE) {
            uzone -= 1;
            uzone = uzone == 0 ? 60 : uzone;

        } else if (Math.abs(lon - upperBound) <= UTM_TOLERANCE) {
            uzone += 1;
            uzone = uzone > 60 ? uzone -= 60 : uzone;
        }
        return uzone;
    }

    /**
     * finds an SPC zone for a given lat/lon (west -ve convention used for lon)
     * not implemented (see spcUtil)
     *
     * @param lat latitude
     * @param lon longitude
     * @return SPC zone
     */
    public static int findSpcZone(double lat, double lon) {
        int spcZone = 0;
        return spcZone;
    }

    /**
     * sets transformation to SPC or UTM
     *
     * @param transformation transformation code
     */
    public void setTransformation(int transformation) {
        this.transformation = transformation;

    }

    /**
     * determines whether utm transformation is being used
     *
     * @return true if UTM transformation is being used
     */
    public boolean isUtm() {
        return transformation == UTM_TRANSFORMATION;
    }

    /**
     * abstract method to support llh transformation
     *
     * @param lat latitude
     * @param lon longitude
     * @param height ellipsoid height
     * @return projected coordinate
     */
    public abstract String toProjectedCoordinates(double lat, double lon, double height);

    /**
     * converts lat-longs to projected coordinates (SPC/UTM)
     *
     * @param lat latitude
     * @param lon longitude
     * @return projected coordinate
     */
    public abstract String toProjectedCoordinates(double lat, double lon);

    /**
     * converts lat-long-height to xyz for a given ellipsoid defined by radius
     * and flattening
     *
     * @param lat latitude
     * @param lon longitude
     * @param height ellipsoid height
     * @param radius equatorial radius
     * @param eflattening flattening
     * @return projected coordinate
     */
    public abstract String toProjectedCoordinates(double lat, double lon, double height, double radius, double eflattening);

    /**
     * converts to an UTM coordinate for a given ellipsoid defined by radius and
     * flattening
     *
     * @param radius equatorial radius
     * @param f flattening
     * @param lat latitude
     * @param lon longitude
     * @return projected coordinate
     */
    public abstract String toProjectedCoordinates(double radius, double f, double lat, double lon);

    /**
     * abstract method for xyz transformation
     *
     * @param x x
     * @param y y
     * @param z z
     * @return geodetic coordinate
     */
    public abstract String toGeodeticCoordinates(double x, double y, double z);

    /**
     * abstract method for spc/utm transformation
     *
     * @param north northing
     * @param east easting
     * @param sh true=southern hemisphere
     * @return geodetic coordinate
     */
    public abstract String toGeodeticCoordinates(double north, double east, boolean sh);

    /**
     * converts xyz to llh for a given ellipsoid defined by radius and
     * flattening
     *
     * @param x x
     * @param y y
     * @param z z
     * @param radius equatorial radius
     * @param eflattening flattening
     * @return geodetic coordinate
     */
    public abstract String toGeodeticCoordinates(double x, double y, double z, double radius, double eflattening);

    /**
     * converts a projected coordinate (spc/utm) to lat-long
     *
     * @param north northing
     * @param east easting
     * @return geodetic coordinate
     */
    public abstract String toGeodeticCoordinates(double north, double east);

    /**
     * converts a utm coordiante to lat-long for a given ellipsoid defined y
     * radius and flattening
     *
     * @param radius equatorial radius
     * @param fl flattening
     * @param north northing
     * @param east easting
     * @param southernHemisphere true=SH
     * @return geodetic coordinate
     */
    public abstract String toGeodeticCoordinates(double radius, double fl, double north, double east, boolean southernHemisphere);

    /**
     * sets the constants required for a zone
     */
    protected abstract void setConstants();

    public static Properties getZones(String datum) {
        Properties prop = null;
        if (datum != null) {
            prop = datum.equalsIgnoreCase("nad83") ? nad83ZoneDef : nad27ZoneDef;
        }
        return prop;
    }

    public static Properties getConstants(String datum) {
        Properties prop = null;
        if (datum != null) {
            prop = datum.equalsIgnoreCase("nad83") ? nad83ZoneConstants : nad27ZoneConstants;
        }
        return prop;
    }

    /**
     * Factory method to return an instance of CoordinateTransformation
     *
     * @param zone zone# of SPC/UTM or null if zone is not applicable
     * @param datum datum being used for transformation
     * @return an instance of CoordinateTransformation
     */
    public static CoordinateTransformation getInstance(String zone, String datum) {
        CoordinateTransformation ct = null;
        Properties zoneConstants;
        Properties zoneDef;
        double unitsCf;
        if (datum == null) {
            return null;
        }
        boolean nad27 = false;
        if (datum.equalsIgnoreCase("nad83")) {
            zoneDef = nad83ZoneDef;
            zoneConstants = nad83ZoneConstants;
        } else {
            nad27 = true;
            zoneDef = nad27ZoneDef;
            zoneConstants = nad27ZoneConstants;
        }
        if (zone == null) {
            return new XyzTransformation(zoneDef, zoneConstants);
        }
        zone = zone.trim();
        //add a leading zero if zone length is 1(UTM) or zone length is 3 (SPC)
        zone = zone.length() == 1 || zone.length() == 3 ? "0" + zone : zone;
        int transformation = zone.length() > 2 ? SPC_TRANSFORMATION : UTM_TRANSFORMATION;
        // nad27 spc coordinates are computed in feet
        if (nad27 && transformation == SPC_TRANSFORMATION) {
            unitsCf = Double.parseDouble(zoneDef.getProperty("meters2usfeet", "1.0"));
        } else {
            unitsCf = 1.0;
        }
        String projection = zoneDef.getProperty(zone + ".proj", " ");
        if (projection.equals("TM") || transformation == UTM_TRANSFORMATION) {
            ct = new TMTransformation(zoneDef, zoneConstants);
        } else if (projection.equals("LC")) {
            ct = new LambertTransformation(zoneDef, zoneConstants);
        } else if (projection.equals("AE")) {
            ct = new AETransformation(zoneDef, zoneConstants);
        } else if (projection.equals("LCS")) {
            ct = new LCSTransformation(zoneDef, zoneConstants);
        } else {
            // default instance. OMTransformation validates the instance
            ct = new OMTransformation(zoneDef, zoneConstants);
        }

        ct.setZone(zone);
        ct.setTransformation(transformation);
        ct.setUnitsCf(unitsCf);
        return ct;
    }

    /**
     * overloaded factory method
     *
     * @param zone zone# of SPC/UTM or null if zone is not applicable
     * @param datum datum being used for transformation
     * @return an instance of CoordinateTransformation
     */
    public static CoordinateTransformation getInstance(int zone, String datum) {
        return getInstance(Integer.valueOf(zone).toString(), datum);
    }

    /**
     * overloaded factory method
     *
     * @param datum datum being used for transformation
     * @return an instance of CoordinateTransformation
     */
    public static CoordinateTransformation getInstance(String datum) {
        return getInstance(null, datum);
    }

}
