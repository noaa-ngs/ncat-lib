/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.noaa.ngs.grid;

import gov.noaa.ngs.transform.CoordinateTransformation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

/**
 * performs datum transformation
 *
 * @author Krishna.Tadepalli
 * @version 1.0 Date: 09/30/2016
 */
public class Transformer {

    private final static Logger logger = Logger.getLogger(Transformer.class);
    private String srcDatum;
    private String destDatum;
    private double lat;
    private double lon;
    private double eht;
    private boolean hasEht;
    private String region;
    private String[] regionDatums;
    private double[] regionBounds;
    private String[] gridParms;
    private String[] gridTypes;
    private String errMsg;
    private int intpGridRows;
    private int intpGridCols;
    private boolean noEhtGrid;
    private String transGrid;
    private double elevation;
    private boolean transDir;
    private ConcurrentHashMap<String, Double> map;

    /**
     *
     * @param lat latitude
     * @param lon longitude
     * @param srcDatum input Datum
     * @param destDatum output Datum
     * @param transGrid type of transformation
     */
    public Transformer(double lat, double lon, String srcDatum, String destDatum, String transGrid) {
        this.lat = lat;
        this.lon = lon;
        setEastLon();
        this.srcDatum = srcDatum;
        this.destDatum = destDatum;
        this.transGrid = transGrid;
        hasEht = false;
        transDir = true; //forward transmission
        setGridParms();
    }

    /**
     *
     * @param lat latitude
     * @param lon longitude
     * @param height ellipsoid height or elevation
     * @param srcDatum input Datum
     * @param destDatum output Datum
     * @param transGrid grid used for transformation (nadcon or vertcon)
     */
    public Transformer(double lat, double lon, double height, String srcDatum, String destDatum, String transGrid) {
        this.lat = lat;
        this.lon = lon;
        setEastLon();
        this.transGrid = transGrid;
        if (isNadcon()) {
            this.eht = height;
            hasEht = true;
        } else {
            this.elevation = height;
            hasEht = false;
        }
        this.srcDatum = srcDatum;
        this.destDatum = destDatum;
        transDir = true;
        setGridParms();
    }

    /**
     *
     * @param lat latitude
     * @param lon longitude
     * @param transGrid type of transformation
     */
    public Transformer(double lat, double lon, String transGrid) {
        this.lat = lat;
        this.lon = lon;
        this.transGrid = transGrid;
        transDir = true;
        setEastLon();
    }

    public Transformer(String transGrid) {
        this.transGrid = transGrid;
        transDir = true;
    }

    private boolean isNadcon() {
        return transGrid.equalsIgnoreCase(GridManager.NADCON);
    }

    /**
     * determines whether transformation is to a more recent datum
     *
     * @return true=more recent
     */
    public boolean isTransDir() {
        return transDir;
    }

    /**
     * returns a nadcon region for a given lat-long
     *
     * @return nadcon region
     */
    public String getRegion() {
        findRegion();
        return region;
    }

    /**
     * sets +ve East longitude
     */
    private void setEastLon() {
        if (lon < 0) {
            lon += 360.0;
        }
    }

    /**
     * initializes grid parameters
     */
    private void setGridParms() {
        GridManager g = GridManager.getInstance(transGrid);
        gridParms = g.getGridParms();
        gridTypes = g.getGridTypes();
        intpGridRows = g.getIntpGridRows();
        intpGridCols = g.getIntpGridCols();

    }

    /**
     * locates a nadcon region for a given lat-long
     *
     * @return
     */
    private void findRegion() {
        GridManager g = GridManager.getInstance(transGrid);
        String[] regions = g.getRegions();
        double[] bounds = g.getBounds();
        boolean found = false;
        int i;
        for (i = 0; i < regions.length; i++) {
            int indx = i * 4;
            double minlat = bounds[indx];
            double maxlat = bounds[indx + 1];
            double minlon = bounds[indx + 2];
            double maxlon = bounds[indx + 3];
            if (lat >= minlat && lat <= maxlat
                    && lon >= minlon && lon <= maxlon) {
                found = true;
                regionBounds = new double[4];
                regionBounds[0] = minlat;
                regionBounds[1] = maxlat;
                regionBounds[2] = minlon;
                regionBounds[3] = maxlon;
                break;
            }
        }
        region = found ? regions[i] : null;
        //      System.out.println("found reg:" + region);
    }

    /**
     * initializes datums for a given region
     */
    private void setDatums() {
        GridManager g = GridManager.getInstance(transGrid);
        regionDatums = g.getDatums(region);

    }

    /**
     * returns regions datums based on lat-long
     *
     * @return region datums
     */
    public String[] getDatums() {
        findRegion();
        if (region != null) {
            setDatums();
            return regionDatums;
        }
        return null;
    }

    /**
     * returns region datums based on a given spc zone
     *
     * @param spcZone spc zone
     * @return region region datums
     */
    public String[] getDatums(String spcZone) {
        int zone = Integer.valueOf(spcZone);
        String region = null;
        if (zone < 5000) {
            region = "Conus";
        } else if (zone >= 5001 && zone <= 5010) {
            region = "Alaska";
        } else if (zone >= 5101 && zone <= 5105) {
            region = "Hawaii";
        } else if (zone >= 5200 && zone <= 5202) {
            region = "PRVI";
        } else if (zone == 5300) {
            region = "AS";
        } else if (zone == 5400) {
            region = "GuamCNMI";
        }
        if (region != null) {
            GridManager g = GridManager.getInstance(transGrid);
            String[] regDatums = g.getDatums(region);

            return regDatums;
        }
        return null;
    }

    /**
     * returns the index of source and destination datums in the ordered datums
     * array for a given region
     *
     * @return index into datum array
     */
    private int[] getDatumIndex() {
        GridManager g = GridManager.getInstance(transGrid);
        ArrayList<String> a = new ArrayList<>(Arrays.asList(regionDatums));
        int[] datumIndex = new int[2];
        datumIndex[0] = a.indexOf(srcDatum);
        datumIndex[1] = a.indexOf(destDatum);
        return datumIndex;
    }

    /**
     * verifies whether a point is inbounds of a grid
     *
     * @param lat latitude
     * @param lon longitude
     * @return true=inbounds
     */
    private boolean isInBounds(double lat, double lon) {

        if ((lat < regionBounds[0] || lat > regionBounds[1])
                || (lon < regionBounds[2] || lon > regionBounds[3])) {
            return false;
        }
        return true;

    }

    /**
     * returns an error message
     *
     * @return error message
     */
    public String getErrMsg() {
        return errMsg;
    }

    /**
     * returns a transformed coordinate
     *
     * @param addDMS true=add DMS format for lat-longs
     * @return transformed coordinate
     */
    public String transform(boolean addDMS) {

        // ****reset lat-lons for multiple iterations
        //
        double[] result = isNadcon() ? new double[6] : new double[2];
        //      double[] result = hasEht ? new double[6] : new double[4];
        errMsg = null;
        findRegion();
        if (region == null) {
            logger.info("No suitable region found for datum transformation");
            return "**No suitable region found for datum transformation**";
        }
        setDatums();
        int[] idx = getDatumIndex();
        int startIdx = idx[0];
        int endIdx = idx[1];
        if (startIdx == -1 || endIdx == -1) {
            logger.error("Source or destination datum not found for " + region);
            return "***Source or destination datum not found for " + region + " ***";
        }
        // initialize result 
        if (isNadcon()) {
            result[0] = lat;
            result[1] = 0.0; //laterr
            result[2] = lon;
            result[3] = 0.0; //lonerr
            if (hasEht) {
                result[4] = eht;
                result[5] = 0.0; //ehterr
                noEhtGrid = false;
            } else {
                result[4] = 999999.0;
                result[5] = 999999.0;
            }
        } else {
            result[0] = elevation;
            result[1] = 0.0; //elevation error
        }
        if (endIdx > startIdx) { //forward transformation to newer datums
            for (int i = startIdx; i < endIdx; i++) {
                String fromDatum = regionDatums[i];
                String toDatum = regionDatums[i + 1];
                map = new ConcurrentHashMap<>();
//                System.out.println("\nlat:" + result[0] + " lon:" + result[2] + " d1:" + fromDatum + " d2:" + toDatum);
                if (isNadcon()) {
                    transform(result[0], result[2], fromDatum, toDatum);
                    if (map.get("gridError") != null) {
                        logger.fatal("Transformation failure;no grids found");
                        return "***Transformation failure;no grids found***";
                    }
                    result[0] += map.get("lattrn") / 3600.0;
                    result[1] += Math.pow(map.get("laterr"), 2.0);
                    result[2] += map.get("lontrn") / 3600.0;
                    result[3] += Math.pow(map.get("lonerr"), 2.0);
                    if (!isInBounds(result[0], result[2])) {
                        logger.error("Transformation failure;coordinate is out of bounds");
                        return "***Transformation failure;coordinate is out of bounds***";

                    }
                    if (hasEht) {
                        //                                       System.out.println("No EHT GRID:"+noEhtGrid);
                        if (!noEhtGrid) {
                            result[4] += map.get("ehttrn");
                            result[5] += Math.pow(map.get("ehterr"), 2.0);
                        }
                    }
                } else {
                    transform(lat, lon, fromDatum, toDatum);
                    if (map.get("gridError") != null) {
                        logger.fatal("Transformation failure;no grids found");
                        return "***Transformation failure;no grids found***";
                    }
                    result[0] += map.get("ohttrn");
                    result[1] += map.get("ohterr");

                }

            }
        } else { //backward transformation to older datums
            transDir = false;
            for (int i = startIdx; i > endIdx; i--) {
                //from and to datums used only to define grid file
                String fromDatum = regionDatums[i - 1];
                String toDatum = regionDatums[i];
                map = new ConcurrentHashMap<>();
                if (isNadcon()) {
                    transform(result[0], result[2], fromDatum, toDatum);
                    if (map.get("gridError") != null) {
                        logger.fatal("Transformation failure;no grids found");
                        return "***Transformation failure;no grids found***";
                    }
                    result[0] -= map.get("lattrn") / 3600.0;
                    result[1] += Math.pow(map.get("laterr"), 2.0);
                    result[2] -= map.get("lontrn") / 3600.0;
                    result[3] += Math.pow(map.get("lonerr"), 2.0);
                    if (!isInBounds(result[0], result[2])) {
                        logger.error("Transformation failure;coordinate is out of bounds");
                        return "***Transformation failure;coordinate is out of bounds***";
                    }
                    if (hasEht) {
                        if (!noEhtGrid) {
                            result[4] -= map.get("ehttrn");
                            result[5] += Math.pow(map.get("ehterr"), 2.0);
                        }
                    }
                } else {
                    transform(lat, lon, fromDatum, toDatum);
                    if (map.get("gridError") != null) {
                        logger.fatal("Transformation failure;no grids found");
                        return "***Transformation failure;no grids found***";
                    }
                    result[0] -= map.get("ohttrn");
                    result[1] += map.get("ohterr");

                }

            }

        }
        if (isNadcon()) {
            result[1] = Math.sqrt(result[1]);
            result[3] = Math.sqrt(result[3]);
            if (hasEht) {
                if (noEhtGrid) {
                    result[4] = 999999.0;
                    result[5] = 999999.0;
                } else {
                    result[5] = Math.sqrt(result[5]);
                }
            }
        }
        return formatResult(result, addDMS);
    }

    /**
     * formats transformed coordinate
     *
     * @param result transformed set
     * @param addDMS true=add DMS for lat-longs
     * @return formatted result
     */
    private String formatResult(double[] result, boolean addDMS) {
        if (isNadcon()) {
            double resetLon = CoordinateTransformation.resetLon(result[2]);
            if ((int) result[4] == 999999 && (result[4] == result[5])) {
                return addDMS ? String.format("%.10f", result[0]) + ","
                        + CoordinateTransformation.toDMS(result[0], true) + ","
                        + String.format("%.6f", result[1]) + ","
                        + String.format("%.10f", resetLon) + ","
                        + CoordinateTransformation.toDMS(resetLon, false) + ","
                        + String.format("%.6f", result[3])
                        + "," + "N/A" + "," + "N/A"
                        : String.format("%.10f", result[0]) + "," + String.format("%.6f", result[1]) + ","
                        + String.format("%.10f", result[2]) + "," + String.format("%.6f", result[3])
                        + "," + "N/A" + "," + "N/A";

            } else {
                return addDMS ? String.format("%.10f", result[0]) + ","
                        + CoordinateTransformation.toDMS(result[0], true) + ","
                        + String.format("%.6f", result[1]) + ","
                        + String.format("%.10f", resetLon) + ","
                        + CoordinateTransformation.toDMS(resetLon, false) + ","
                        + String.format("%.6f", result[3]) + ","
                        + String.format("%.3f", result[4]) + "," + String.format("%.3f", result[5])
                        : String.format("%.10f", result[0]) + "," + String.format("%.6f", result[1]) + ","
                        + String.format("%.10f", result[2]) + "," + String.format("%.6f", result[3])
                        + "," + String.format("%.3f", result[4]) + "," + String.format("%.3f", result[5]);
            }
        } else {
            double resetLon = CoordinateTransformation.resetLon(lon);
            return addDMS ? String.format("%.10f", lat) + ","
                    + CoordinateTransformation.toDMS(lat, true) + ","
                    + String.format("%.10f", resetLon) + ","
                    + CoordinateTransformation.toDMS(resetLon, false) + ","
                    + String.format("%.3f", elevation) + "," + String.format("%.3f", result[0])
                    + "," + String.format("%.3f", result[1])
                    : String.format("%.10f", lat) + ","
                    + String.format("%.10f", resetLon) + ","
                    + String.format("%.3f", elevation) + "," + String.format("%.3f", result[0])
                    + "," + String.format("%.3f", result[1]);

        }
    }

    /**
     * transforms a given coordinate from one datum to the other
     *
     * @param tlat latitude
     * @param tlon longitude
     * @param fromDatum source datum
     * @param toDatum destination datum
     */
    private void transform(double tlat, double tlon, String fromDatum, String toDatum) {
        map = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(6);

        for (int j = 0; j < gridParms.length; j++) {
            // skip if there is no grid for eht
            if (gridParms[j].equalsIgnoreCase("eht") && (!hasEht)) {
                continue;
            }
            for (int k = 0; k < gridTypes.length; k++) {
                Runnable w = new Transformer.Gthread(tlat, tlon, fromDatum, toDatum, gridParms[j], gridTypes[k]);
                executor.submit(w);

            }
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }

    }

    /**
     * An inner class to support multithreading
     */
    private class Gthread implements Runnable {

        private String gParm;
        private String gType;
        private String fromDatum;
        private String toDatum;
        private double tlat;
        private double tlon;

        /**
         *
         * @param tlat latitude
         * @param tlon longitude
         * @param fromDatum source datum
         * @param toDatum destination datum
         * @param gParm parameter being transformed
         * @param gType parameter type being transformed
         */
        public Gthread(double tlat, double tlon, String fromDatum, String toDatum, String gParm, String gType) {
            this.tlat = tlat;
            this.tlon = tlon;
            this.gParm = gParm;
            this.gType = gType;
            this.fromDatum = fromDatum;
            this.toDatum = toDatum;
        }

        @Override
        public void run() {
            GridManager g = GridManager.getInstance(region, fromDatum, toDatum, gParm, gType, transGrid);
            double[] block = g.getBlock(tlat, tlon, intpGridRows, intpGridCols);
            if (block != null) {
                double[] coord = g.getIntpPoint(); // (x,y) coordinate used for interploation
                double intpVal = g.interpolate(coord[0], coord[1], block);
                if ((gParm + gType).equals("ohterr") && intpVal < 0) {
                    int[] blkidx = g.getTwoByTwoCells();
                    double[] blk = new double[4];
                    for (int i=0;i<4;i++){
                        blk[i] = block[blkidx[i]];
                    }
                    Interpolator intp = new Interpolator(coord[0], coord[1], blk);
                    intpVal = Math.abs(intp.bilinear());
                }
                map.put(gParm + gType, intpVal);
            } else {
                if (gParm.equals("eht")) {
                    noEhtGrid = true;
                } else {
                    map.put("gridError", 1.0);
                }
            }
        }

    }

    public static void main(String[] args) {
        double lat = 39.2240867222;
        double lon = CoordinateTransformation.resetLon(-98.5421515000);
        for (int i=0;i<10000;i++){
        Transformer t = new Transformer(lat, lon, 123.0, "NAD83(NSRS2007)", "NAD83(2011)", GridManager.NADCON);
        String result = t.transform(true);
        System.out.println(result);
        }
//        t = new Transformer(-14.2897883722, CoordinateTransformation.resetLon(189.3363353000), GridManager.VERTCON);
//        String[] datums = t.getDatums("4900");
//        for (String datum : datums) {
//            System.out.println("datum:" + datum);
    }
    //   }

//    }
}
