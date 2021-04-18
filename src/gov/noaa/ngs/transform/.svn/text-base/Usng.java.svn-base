package gov.noaa.ngs.transform;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Transforms geodetic/UTM coordinates to USNG and vice versa
 *
 * @author Krishna Tadepalli
 * @version 1.0 Date: 06/23/2011
 *
 */
public class Usng {

    public static final String[] COLUMN_PATTERN = {"STUVWXYZ", "ABCDEFGH", "JKLMNPQR"};
    public static final String[] ROW_PATTERN = {"FGHJKLMNPQRSTUVABCDE",
        "ABCDEFGHJKLMNPQRSTUV"};
    public static final int UTM_SOUTH_OFFSET = 6000000;
    public static final int UTM_NORTH_OFFSET = 2000000;
    public static final int UTM_GRID_SIZE = 100000;
    public static final String ONE_METER_FORMAT = "%05d";
    public static final String TEN_METER_FORMAT = "%04d";
    public static final String HUNDRED_METER_FORMAT = "%03d";
    public static final int ASCII_M_INDEX = 77;
    public static final int ASCII_C_INDEX = 67;
    public static final int ASCII_X_INDEX = 88;
    public static final int ASCII_I_INDEX = 73;
    public static final int ASCII_O_INDEX = 79;
    private static final Map<Integer, String> GZD_MAP = initGzd();
    private int resolution = 1;
    private String utmFormat;
    private String datum;
    private boolean southernHemisphere;

    public Usng(String datum, boolean southernHemisphere) {
        utmFormat = ONE_METER_FORMAT;
        this.datum = datum;
        this.southernHemisphere = southernHemisphere;
    }

    public Usng(String datum) {
        this(datum, false);
    }

    public Usng() {
        utmFormat = ONE_METER_FORMAT;
    }

    private static Map<Integer, String> initGzd() {
        Map<Integer, String> map = new HashMap<>();
        map.put(-80, "C");
        map.put(-72, "D");
        map.put(-64, "E");
        map.put(-56, "F");
        map.put(-48, "G");
        map.put(-40, "H");
        map.put(-32, "J");
        map.put(-24, "K");
        map.put(-16, "L");
        map.put(-8, "M");
        map.put(0, "N");
        map.put(8, "P");
        map.put(16, "Q");
        map.put(24, "R");
        map.put(32, "S");
        map.put(40, "T");
        map.put(48, "U");
        map.put(56, "V");
        map.put(64, "W");
        map.put(72, "X");

        return Collections.unmodifiableMap(map);
    }

    /**
     * sets USNG resolution (1m,10m,or 100m)
     *
     * @param resolution resolution used for USNG (10m or 100m)
     */
    public void setResolution(int resolution) {
        if (resolution == 1) {
            utmFormat = ONE_METER_FORMAT;
        } else if (resolution == 10) {
            utmFormat = TEN_METER_FORMAT;
        } else {
            utmFormat = HUNDRED_METER_FORMAT;
        }
        if (resolution > 100) {
            resolution = 100;
        }
        this.resolution = resolution;
    }

    /**
     * getter for resolution
     *
     * @return resolution
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * determines grid zone designator for a given latitude
     *
     * @param lat
     * @return a letter denoting grid zone
     */
    private String getGzd(double lat) {
        String gzd = " ";
        if (lat < -80.0 || lat > 84.0) {
            return gzd;
        }
        int i = 0;
        if (lat >= 0) {
            i = ((int) lat / 8) * 8;
        } else {
            int slat = (int) lat;
            i = slat % 8 == 0 ? slat : ((slat / 8) - 1) * 8;
        }
        i = i > 72 ? 72 : i;
        i = i < -80 ? -80 : i;
        return GZD_MAP.get(i);

    }
//    private String getGzd(double lat) {
//        String gzd = " ";
//        if (lat < -80.0 || lat > 84.0) {
//            return gzd;
//        }
//        lat += 90.0;
//        //index of C,X,I,O in ascii table
//        int asciiIndex = lat < 90 ? ((int) lat - 11) / 8 : ((int) lat - 10) / 8;
//        asciiIndex += ASCII_C_INDEX;
//        if (asciiIndex >= ASCII_I_INDEX) {
//            asciiIndex++;
//        }
//        if (asciiIndex >= ASCII_O_INDEX) {
//            asciiIndex++;
//        }
//        asciiIndex = asciiIndex > ASCII_X_INDEX ? ASCII_X_INDEX : asciiIndex;
//        char[] charAr = new char[1];
//        charAr[0] = (char) asciiIndex;
//        return new String(charAr);
//    }

    /**
     * returns a two letter column-row ID into 100k meter grid for a given UTM
     * coordinate and zone.
     *
     * @param north
     * @param east
     * @param zone
     * @return
     */
    private String getSquareId(double north, double east, int zone) {
        String colPattern = COLUMN_PATTERN[zone % 3];
        String rowPattern = ROW_PATTERN[zone % 2];
        int colIndex = ((int) east / UTM_GRID_SIZE) % colPattern.length() - 1;
        if (colIndex < 0) {
            colIndex += 8;
        }
        int rowIndex = ((int) north / UTM_GRID_SIZE) % rowPattern.length();
        String colLetter = colPattern.substring(colIndex, colIndex + 1);
        String rowLetter = rowPattern.substring(rowIndex, rowIndex + 1);
        return colLetter + rowLetter;
    }

    /**
     * converts a given lat-long to USNG per a given resolution
     *
     * @param lat latitude
     * @param lon longitude
     * @return USNG USNG coordinate
     */
    public String toUsng(double lat, double lon) {
        String usngStr = "";
        lon = CoordinateTransformation.resetLon(lon);
        // round lat-longs to 6-decimal places- sufficient for USNG resolutions
        //       lat = (double) Math.round(lat * 1000000) / 1000000;
        //       lon = (double) Math.round(lon * 1000000) / 1000000;
        int utmZone = CoordinateTransformation.findUtmZone(lon);
        CoordinateTransformation ct = CoordinateTransformation.getInstance(utmZone, datum);
        String coordStr = ct.toProjectedCoordinates(lat, lon);
        String[] coordAr = coordStr.split(",");
        utmZone = Integer.valueOf(coordAr[1].trim());
        double north = Double.parseDouble(coordAr[3]);
        double east = Double.parseDouble(coordAr[4]);
        String squareId = getSquareId(north, east, utmZone);
        String gZd = getGzd(lat);
        int northPart = (int) north % UTM_GRID_SIZE;
        northPart /= resolution;
        String northPartStr = String.format(utmFormat, northPart);
        int eastPart = (int) east % UTM_GRID_SIZE;
        eastPart /= resolution;
        String eastPartStr = String.format(utmFormat, eastPart);
        usngStr = Integer.toString(utmZone) + gZd + squareId
                + eastPartStr + northPartStr;

        return usngStr;

    }

    /**
     * converts a given UTM coordinate to USNG per a given resolution
     *
     * @param north northing
     * @param east easting
     * @param utmZone utm zone
     * @return USNG USNG coordinate
     */
    public String toUsng(double north, double east, int utmZone) {
        CoordinateTransformation ct = CoordinateTransformation.getInstance(utmZone, datum);
        String coordStr = southernHemisphere ? ct.toGeodeticCoordinates(north, east, southernHemisphere)
                : ct.toGeodeticCoordinates(north, east);
        String[] coordAr = coordStr.split(",");
        double lat = Double.parseDouble(coordAr[3]);
        return toUsng(north, east, utmZone, lat);
    }

    /**
     * Converts utm to usng at a given latitude per a given resolution
     *
     * @param north northing
     * @param east easting
     * @param utmZone UTM zone
     * @param lat latitude
     * @return USNG coordinate
     */
    public String toUsng(double north, double east, int utmZone, double lat) {
        String squareId = getSquareId(north, east, utmZone);
        String gZd = getGzd(lat);
        int northPart = (int) north % UTM_GRID_SIZE;
        int eastPart = (int) east % UTM_GRID_SIZE;
        northPart /= resolution;
        eastPart /= resolution;
        String northPartStr = String.format(utmFormat, northPart);
        String eastPartStr = String.format(utmFormat, eastPart);
        return Integer.toString(utmZone) + gZd + squareId + eastPartStr + northPartStr;
    }

    /**
     * converts a given USNG string to UTM
     *
     * @param usng USNG coordinate
     * @return UTM coordinate
     */
    public String toUtm(String usng) {
        String utmStr = "";
        int zone;
        if (usng == null || usng.length() == 0) {
            return utmStr;
        }
        int utmCoordLength = Integer.parseInt(utmFormat.substring(2, 3));
        int index = 0;
        if (Character.isDigit(usng.charAt(index)) && Character.isDigit(usng.charAt(index + 1))) {
            zone = Integer.parseInt(usng.substring(index, index + 2));
            index += 2;
        } else if (Character.isDigit(usng.charAt(index))) {
            zone = Integer.parseInt(usng.substring(index, index + 1));
            index += 1;
        } else {
            return utmStr;
        }
        String gzd = usng.substring(index, index + 1);
        index += 1;
        String colLetter = usng.substring(index, index + 1);
        index += 1;
        String rowLetter = usng.substring(index, index + 1);
        index += 1;
        int easting = Integer.parseInt(usng.substring(index, index + utmCoordLength));
        index += utmCoordLength;
        int northing = Integer.parseInt(usng.substring(index, index + utmCoordLength));
        easting *= resolution;
        northing *= resolution;
        int offset = getNorthOffset(gzd, rowLetter, zone);
        String colPattern = COLUMN_PATTERN[zone % 3];
        String rowPattern = ROW_PATTERN[zone % 2];
        int colIndex = colPattern.indexOf(colLetter);
        int rowIndex = rowPattern.indexOf(rowLetter);
        int east = easting + UTM_GRID_SIZE * (colIndex + 1);
        int north = northing + UTM_GRID_SIZE * rowIndex;
        if ((int) gzd.charAt(0) <= ASCII_M_INDEX) {            //southern hemisphere;
            north += UTM_SOUTH_OFFSET - offset;
        } else {
            north += UTM_NORTH_OFFSET + offset;
        }
        String northStr = String.format(utmFormat, north);
        String eastStr = String.format(utmFormat, east);

        utmStr = Integer.toString(zone) + "," + northStr + "," + eastStr;
        return utmStr;
    }

    /**
     * converts a given USNG string to lat-long The string passed should contain
     * utm zone, north, and east delimited by comma
     *
     * @param usng USNG coordinate
     * @return geodetic coordinate
     */
    public String toGeodeticCoordinates(String usng) {
        String[] utmStr = toUtm(usng).split(",");
        int zone = Integer.parseInt(utmStr[0]);
        int north = Integer.parseInt(utmStr[1]);
        int east = Integer.parseInt(utmStr[2]);
        int gzdIndex = zone < 10 ? 1 : 2;
        String gzd = usng.substring(gzdIndex, gzdIndex + 1);
        char gzdChar = gzd.charAt(0);
        int asciiVal = (int) gzdChar;
        CoordinateTransformation ct = CoordinateTransformation.getInstance(zone, datum);
        if (southernHemisphere || (asciiVal <= ASCII_M_INDEX)) {
            return ct.toGeodeticCoordinates(north, east, true);
        }
        return ct.toGeodeticCoordinates(north, east);
    }

    /**
     * computes the offset to be appiled to UTM
     *
     * @param gzd
     * @param rowLetter
     * @param zone
     * @return
     */
    private int getNorthOffset(String gzd, String rowLetter, int zone) {
        String rowPattern1 = "ABCDEFGHJK";
        String rowPattern2 = "FGHJKLMNPQ";
        String rowPattern3 = "ABC";
        String rowPattern4 = "FGH";
        String rowPattern5 = "TUV";
        String rowPattern6 = "CDE";
        boolean oddZone = zone % 2 == 1;
        boolean evenZone = zone % 2 == 0;
        if (gzd.equals("N") || gzd.equals("P")) {
            return -2000000;
        }
        if (gzd.equals("Q")) {
            return ((rowPattern1.contains(rowLetter)) && oddZone)
                    || ((rowPattern2.contains(rowLetter)) && evenZone)
                    ? 0 : -2000000;
        }
        if (gzd.equals("S")) {
            return ((rowPattern1.contains(rowLetter)) && oddZone)
                    || ((rowPattern2.contains(rowLetter)) && evenZone)
                    ? 2000000 : 0;
        }
        if (gzd.equals("T")) {
            return 2000000;
        }
        if (gzd.equals("U")) {
            return ((rowPattern3.contains(rowLetter)) && oddZone)
                    || ((rowPattern4.contains(rowLetter)) && evenZone)
                    ? 4000000 : 2000000;
        }
        if (gzd.equals("V") || gzd.equals("W")) {
            return 4000000;
        }
        if (gzd.equals("X")) {
            return ((rowLetter.equals("V") && oddZone) || (rowLetter.equals("E") && evenZone))
                    ? 4000000 : 6000000;
        }
        if (gzd.equals("M")) {
            return ((rowLetter.equals("A") && oddZone) || (rowLetter.equals("F") && evenZone))
                    ? -4000000 : -2000000;
        }
        if (gzd.equals("L")) {
            return -2000000;
        }
        if (gzd.equals("K")) {
            return ((rowPattern1.contains(rowLetter)) && oddZone)
                    || ((rowPattern2.contains(rowLetter)) && evenZone)
                    ? -2000000 : 0;
        }
        if (gzd.equals("H")) {
            return ((rowPattern1.contains(rowLetter)) && oddZone)
                    || ((rowPattern2.contains(rowLetter)) && evenZone)
                    ? 0 : 2000000;
        }
        if (gzd.equals("G")) {
            return 2000000;
        }
        if (gzd.equals("F")) {
            return ((rowPattern5.contains(rowLetter)) && oddZone)
                    || ((rowPattern6.contains(rowLetter)) && evenZone)
                    ? 4000000 : 2000000;

        }
        if (gzd.equals("E") || gzd.equals("D")) {
            return 4000000;
        }
        if (gzd.equals("C")) {
            return ((rowLetter.equals("A") && oddZone) || (rowLetter.equals("F") && evenZone))
                    ? 4000000 : 6000000;
        }
        return 0;

    }

    public String getUtmStr(String usng) {
        String sh;
        String[] utmStr = toUtm(usng).split(",");
        int zone = Integer.parseInt(utmStr[0]);
        int north = Integer.parseInt(utmStr[1]);
        int east = Integer.parseInt(utmStr[2]);
        int gzdIndex = zone < 10 ? 1 : 2;
        String gzd = usng.substring(gzdIndex, gzdIndex + 1);
        char gzdChar = gzd.charAt(0);
        int asciiVal = (int) gzdChar;
        sh = (asciiVal <= ASCII_M_INDEX) ? "S" : "N";
        return Integer.toString(zone) + "," + Integer.toString(north) + ","
                + Integer.toString(east) + "," + sh;

    }

    public static void main(String[] args) {
        Usng u = new Usng("nad83");
        //       System.out.println("USNG:" + u.toUsng(4316895.472, 456704.008, 18, 39.0));
            System.out.println("USNG:" + u.toGeodeticCoordinates("36MYS2033215192"));
                   System.out.println( " USNG:" + u.toUsng(-7.9997745335,34.9990040037));
    }
}
