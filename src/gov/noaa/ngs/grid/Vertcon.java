package gov.noaa.ngs.grid;

import gov.noaa.ngs.endian.EndianParser;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Extends GridManager for Vertcon
 *
 * @author Krishna.Tadepalli
 * @version 1.0 Date: 10/25/2018
 */
public class Vertcon extends GridManager {

    private final static Logger logger = Logger.getLogger(Vertcon.class);
    private double gridTolerance;
    private String region;
    private String srcDatum;
    private String destDatum;
    private String gridParm; //lat,lon,eht
    private String gridType;  //err ot trn
    private final Properties gridDef;

    private int gridHeaderLen;
    private double minlat;
    private double minlon;
    private double maxlat;
    private double maxlon;
    private double dlat;
    private double dlon;
    private int width;
    private int height;
    private int ikind;
    private int cellSize;
    private long fileSize;
    private int drLen;
    private EndianParser parser;
    private double[] intpPoint; //(x,y) coordinate to be used for interpolation

    public Vertcon(String region, String srcDatum, String destDatum,
            String gridParm, String gridType) {
        super();
        this.region = region;
        this.srcDatum = srcDatum;
        this.destDatum = destDatum;
        this.gridParm = gridParm;
        this.gridType = gridType;
        this.gridDef = vertconDef;
    }

    public Vertcon() {
        super();
        this.gridDef = vertconDef;
    }

    /**
     * parses grid header record and initializes header elements
     *
     * @return a reference to grid file
     */
    @Override
    protected RandomAccessFile parseHeader() {
        RandomAccessFile gridfh = null;
        String gridsDefaultPath = gridDef.getProperty("gridfile.path", "");
        String gridsPath = System.getProperty("gpath", gridsDefaultPath).trim();
        // terminate with "/" if it does not have one
        if (!gridsPath.endsWith("/")) {
            gridsPath += "/";
        }
        String gridFile = gridsPath + getGridFile();
        if (!new File(gridFile).exists()) {
            logger.fatal(":Grid file " + gridFile + " does not exist");
            return null;
        }
        try {

            gridfh = new RandomAccessFile(gridFile, "r");

            //
            // read and parse the header; header is HEADER_LEN-bytes
            //
            gridHeaderLen = Integer.valueOf(gridDef.getProperty("grid.headerlen", ""));
            //           System.out.println("grid headerlen:" + gridHeaderLen);
            byte[] header = new byte[gridHeaderLen];
            gridfh.read(header);
            parser = new EndianParser();
            parser = Math.abs(parser.getInt(header, 44)) > 2 ? new EndianParser("le") : parser;

            fileSize = gridfh.length();
            minlat = parser.getDouble(header, 4);
            minlon = parser.getDouble(header, 12);
            dlat = parser.getDouble(header, 20);
            dlon = parser.getDouble(header, 28);
            height = parser.getInt(header, 36);
            width = parser.getInt(header, 40);
            ikind = parser.getInt(header, 44);
            maxlat = minlat + (height - 1) * dlat;
            maxlon = minlon + (width - 1) * dlon;
            cellSize = (ikind == 0 || ikind == 1) ? 4 : 2;
            drLen = (width + 2) * cellSize;

        } catch (IOException ex) {
            logger.fatal("Unable to read grid file " + gridFile, ex);
        }
        return gridfh;
    }

    @Override
    public String getGridFile() {
        String regions = gridDef.getProperty("regions", "");
        if (!regions.contains(region)) {
            return null;
        }
        gridTolerance = Double.valueOf(gridDef.getProperty("grid.tolerance", ""));
        String gridFilePrefix = gridDef.getProperty("gridfile.prefix", "");
        String datum1 = srcDatum.replace("(", "_").replace(")", "").replace("NSRS", "").toLowerCase();
        String datum2 = destDatum.replace("(", "_").replace(")", "").replace("NSRS", "").toLowerCase();
        String gridDate = gridDef.getProperty(region + ".grid.date", "");
        String gridFile = gridFilePrefix + "." + datum1 + "." + datum2 + "."
                + region.toLowerCase() + "." + gridParm + "." + gridType + "." + gridDate + ".b";

//              System.out.println("gridfile"  + gridFile );
        return gridFile;

    }

    /**
     * returns a grid row for a given latitude
     *
     * @param latitude latitude
     * @return grid row#
     */
    @Override
    protected int getGridRow(double latitude) {
        if (latitude < minlat) {
            if (latitude >= minlat - gridTolerance) {
                latitude = minlat;
            } else {
                return -1;
            }
        }
        if (latitude > maxlat) {
            if (latitude <= maxlat + gridTolerance) {
                latitude = maxlat;
            } else {
                return -1;
            }
        }
        double drow = (latitude - minlat) / (dlat / 2.0);
        int row2 = (int) drow + 1;
        int row = row2 % 2 != 0 ? (row2 + 1) / 2 - 1 : row2 / 2;
        row = row < 1 ? 1 : row;
        row = row > (height - 2) ? height - 2 : row;

        return row;
    }

    /**
     * returns a grid column for a given longitude
     *
     * @param longitude longitude
     * @return grid column#
     */
    @Override
    protected int getGridColumn(double longitude) {
        if (longitude < minlon || longitude > maxlon) {
            if (longitude >= minlon - gridTolerance) {
                longitude = minlon;
            } else { // out of bounds
                return -1;
            }
        }
        if (longitude > maxlon) {
            if (longitude <= maxlon + gridTolerance) {
                longitude = maxlon;
            } else { // out of bounds
                return -1;
            }
        }
        double dcol = (longitude - minlon) / (dlon / 2.0);
        int col2 = (int) dcol + 1;
        int col = col2 % 2 != 0 ? (col2 + 1) / 2 - 1 : col2 / 2;
        col = col < 1 ? 1 : col;
        col = col > (width - 2) ? width - 2 : col;
        return col;
    }

    /**
     * returns a block of numRowsXnumCols surrounding a given point
     *
     * @param lat latitude
     * @param lon longitude
     * @param numRows number of rows in a block
     * @param numCols number of columns in a block
     * @return a block of data
     */
    @Override
    public double[] getBlock(double lat, double lon, int numRows, int numCols) {
        RandomAccessFile gridfh = parseHeader();
        if (gridfh == null) { //possibly grid file not found
            return null;
        }
        int row = getGridRow(lat);
        int col = getGridColumn(lon);

        if (row == -1 || col == -1) { // out of bounds
            return null;
        }
        intpPoint = new double[2];
        // point for interpolation (x,y)
        intpPoint[0] = (lon - minlon - dlon * (col - 1)) / dlon;
        intpPoint[1] = (lat - minlat - dlat * (row - 1)) / dlat;
        return getCells(gridfh, row - 1, col - 1, numRows, numCols);

    }

    /**
     * returns a block rowsXcols cells at a given location in the grid
     *
     * @param gridfh a reference to grid file
     * @param heightOffset offset to be applied to grid height
     * @param widthOffset offset to be applied to grid width
     * @param rows number of grid rows to be returned
     * @param cols number of grid columns to be returned
     * @return a block of grid cells defined by rowsXcols
     */
    @Override
    protected double[] getCells(RandomAccessFile gridfh, int heightOffset, int widthOffset, int rows, int cols) {
        double[] grid = new double[cols * rows];
        byte[] gridRec = new byte[cols * cellSize];

        try {
            int gridIdx = 0;
            for (int j = 0; j < rows; j++) {
                int seekPos = gridHeaderLen + (heightOffset + j) * drLen + (widthOffset + 1) * cellSize;
                gridfh.seek(seekPos);
                gridfh.read(gridRec);
                for (int i = 0; i < cols; i++) {
                    double d = cellSize == 4 ? parser.getFloat(gridRec, i * cellSize)
                            : parser.getShort(gridRec, i * cellSize);
                    grid[gridIdx] = d;
                    gridIdx++;
                }
            }

        } catch (IOException ex) {
            logger.fatal("Unable to read grid cells ", ex);
        } finally {
            try {
                gridfh.close();
            } catch (IOException ex) {
                logger.fatal("Unable to close grid file ", ex);
            }
        }
        return grid;

    }

    /**
     * returns the point being interpolated
     *
     * @return point being interpolated
     */
    @Override
    public double[] getIntpPoint() {
        return intpPoint;
    }

    /**
     * returns a 2X2 cell around a point being interpolated
     *
     * @return a 2X2 block of grid cells
     */
    @Override
    public int[] getTwoByTwoCells() {
        double x = intpPoint[0];
        double y = intpPoint[1];
        int quad;
        if (x >= 0 && x <= 1) {
            if (y >= 0 && y <= 1) {
                quad = 1;
            } else {
                quad = 3;
            }
        } else {
            if (y >= 0 && y <= 1) {
                quad = 2;
            } else {
                quad = 4;
            }

        }
        switch (quad) {
            case 1:
                return new int[]{0, 1, 3, 4};
            case 2:
                return new int[]{1, 2, 4, 5};
            case 3:
                return new int[]{3, 4, 6, 7};
            default:
                return new int[]{4, 5, 7, 8};

        }
    }

    /**
     * returns datums for a given region
     *
     * @param region region
     * @return datums for a given region
     */
    @Override
    public String[] getDatums(String region) {
        return gridDef.getProperty(region + ".datum", "").split(",");
    }

    /**
     * returns a list of all regional datums
     *
     * @return a list of datums
     */
    @Override
    public ArrayList<String> getDatums() {
        String[] regions = getRegions();
        ArrayList<String> dlist = new ArrayList<>();
        for (String reg : regions) {
            String[] regDatums = getDatums(reg);
            for (String rDatum : regDatums) {
                if (!dlist.contains(rDatum)) {
                    dlist.add(rDatum);
                }
            }
        }
        return dlist;
    }

    /**
     * returns regional boundaries
     *
     * @return boundaries
     */
    @Override
    public double[] getBounds() {
        String[] boundsStrAr = gridDef.getProperty("bounds", "").split(",");
        double[] bounds = new double[boundsStrAr.length];
        for (int i = 0; i < boundsStrAr.length; i++) {
            bounds[i] = Double.valueOf(boundsStrAr[i]);
        }
        return bounds;
    }

    /**
     * returns regions
     *
     * @return regions
     */
    @Override
    public String[] getRegions() {
        return gridDef.getProperty("regions", "").split(",");
    }

    /**
     * returns parameters for which grids are available
     *
     * @return parameters
     */
    @Override
    public String[] getGridParms() {
        return gridDef.getProperty("grid.parms", "").split(",");
    }

    /**
     * returns the types of grids available for a given parameter
     *
     * @return type of grids
     */
    @Override
    public String[] getGridTypes() {
        return gridDef.getProperty("grid.types", "").split(",");
    }

    /**
     * returns the number of rows used for the interpolation grid
     *
     * @return number of grid rows
     */
    @Override
    public int getIntpGridRows() {
        return Integer.valueOf(gridDef.getProperty("intpGrid.rows", ""));
    }

    /**
     * returns the number of columns used for the interpolation grid
     *
     * @return number of grid columns
     */
    @Override
    public int getIntpGridCols() {
        return Integer.valueOf(gridDef.getProperty("intpGrid.cols", ""));
    }

    @Override
    public String getVersion() {
        return gridDef.getProperty("vertcon.version", "");
    }

    @Override
    public double interpolate(double x, double y, double[] block) {
        Interpolator intp = new Interpolator(x, y, block);
        int gridRank = rankBlock(block);
        double intpVal;
        switch (gridRank) {
            case 3:
                intpVal = intp.biquadratic();
                break;
            case 2:
                int[] twoBytwocells = getTwoByTwoCells();
                double[] blk = new double[4];
                for (int i = 0; i < 4; i++) {
                    blk[i] = block[twoBytwocells[i]];
                }
                intp = new Interpolator(x, y, blk);

                intpVal = intp.bilinear();
                break;
            default:
                intpVal = 999999.0;
                break;
        }
        return intpVal;
    }

}
