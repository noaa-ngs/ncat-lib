package gov.noaa.ngs.grid;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Encapsulates methods for parsing and returning a block of data from a grid
 * that's in ".b" format
 *
 * @author Krishna.Tadepalli
 * @version 1.0 Date: 09/30/2016
 */
public abstract class GridManager {

    private final static Logger logger = Logger.getLogger(GridManager.class);
    public static final String NADCON_PROP_FILE = "/gov/noaa/ngs/nadcon5/resources/nadconDef.properties";
    public static final String VERTCON_PROP_FILE = "/gov/noaa/ngs/vertcon/resources/vertconDef.properties";
    public static final int MISSING_DATA_INDICATOR = -999;
    public static final String NADCON = "nadcon";
    public static final String VERTCON = "vertcon";
    protected static Properties nadconDef;
    protected static Properties vertconDef;

    static {
        loadDatumDefinitions();
    }

    /**
     * load grid properties first time the class is referenced
     */
    private static void loadDatumDefinitions() {
        nadconDef = new Properties();
        try (InputStream stream = GridManager.class.getResourceAsStream(NADCON_PROP_FILE)) {
            nadconDef.load(stream);
        } catch (IOException ex) {
            logger.fatal("Unable to load NADCON property file", ex);
        }
        vertconDef = new Properties();
        try (InputStream stream = GridManager.class.getResourceAsStream(VERTCON_PROP_FILE)) {
            vertconDef.load(stream);
        } catch (IOException ex) {
            logger.fatal("Unable to load VERTCON property file", ex);
        }

    }


    public GridManager() {
    }

    /**
     * returns an instance of GridManager
     *
     * @param gridName type of grid being used
     * @return a reference to GridManager
     */
    public static GridManager getInstance(String gridName) {
        GridManager gm = null;
        if (gridName.equalsIgnoreCase("nadcon")) {
            gm = new Nadcon();
        } else if (gridName.equalsIgnoreCase("vertcon")) {
            gm = new Vertcon();
        }
        return gm;

    }

    /**
     * returns an instance of GridManager
     *
     * @param region a geographic region where grids are available
     * @param srcDatum input datum 
     * @param destDatum output datum
     * @param gridParm  a parameter for which a grid is defined
     * @param gridType  a type that identifies transformation or error grid
     * @param gridName  type of transformation
     * @return a reference to GridManager
     */
    public static GridManager getInstance(String region, String srcDatum, String destDatum,
            String gridParm, String gridType, String gridName) {
        GridManager gm = null;
        if (gridName.equalsIgnoreCase("nadcon")) {
            gm = new Nadcon(region, srcDatum, destDatum, gridParm, gridType);
        } else if (gridName.equalsIgnoreCase("vertcon")) {
            gm = new Vertcon(region, srcDatum, destDatum, gridParm, gridType);
        }
        return gm;

    }

    /**
     * parses grid header record and initializes header elements
     *
     * @return a reference to grid file
     */
    protected abstract RandomAccessFile parseHeader();

    /**
     * returns the point being interpolated
     *
     * @return point being interpolated
     */
    public abstract double[] getIntpPoint();

    /**
     * returns a grid row for a given latitude
     *
     * @param latitude latitude
     * @return grid row#
     */
    protected abstract int getGridRow(double latitude);

    /**
     * returns a grid column for a given longitude
     *
     * @param longitude longitude
     * @return grid column#
     */
    protected abstract int getGridColumn(double longitude);

    /**
     * returns a block of numRowsXnumCols surrounding a given point
     *
     * @param lat latitude
     * @param lon longitude
     * @param numRows number of rows in a block
     * @param numCols number of columns in a block
     * @return a block of data
     */
    public abstract double[] getBlock(double lat, double lon, int numRows, int numCols);

    /**
     * defines a grid file to be used for processing
     *
     * @return grid filename
     */
    public abstract String getGridFile();

    /**
     * returns a block rowsXcols cells at a given location in the grid
     *
     * @param gridfh a reference to grid file
     * @param heightOffset offset to be applied to grid height
     * @param widthOffset  offset to be applied to grid width
     * @param rows number of grid rows to be returned
     * @param cols number of grid columns to be returned
     * @return a block of grid cells defined by rowsXcols
     */
    protected abstract double[] getCells(RandomAccessFile gridfh, int heightOffset, int widthOffset, int rows, int cols);

    /**
     * ranks a given 3X3 block based on missing data, if any
     *
     * @param block a block of cells to be checked
     * @return 3=3X3 block is usable (no missing data), 2=2X2 block is usable(
     * some missing data), 1=block is not usable(many missing data cells)
     */
    public int rankBlock(double[] block) {
        boolean missing = false;
        for (double d : block) {
            if ((int) d == MISSING_DATA_INDICATOR) {
                missing = true;
                break;
            }
        }
        if (!missing) {
            return 3;
        } else {
            int[] twoBytwocells = getTwoByTwoCells();

            // if missing check 2X2 block
            // if any cell in 2X2 has misisng data, the block is not usable
            for (int i = 0; i < twoBytwocells.length; i++) {
                if ((int) block[twoBytwocells[i]] == MISSING_DATA_INDICATOR) {
                    return 1;
                }
            }
        }
        // 2X2 block is usable 
        return 2;
    }

    /**
     * returns a 2X2 cell around a point being interpolated
     *
     * @return a 2X2 block of grid cells
     */
    public abstract int[] getTwoByTwoCells();

    /**
     * returns datums for a given region
     *
     * @param region region
     * @return datums for a given region
     */
    public abstract String[] getDatums(String region);

    /**
     * returns a list of all regional datums
     *
     * @return a list of datums
     */
    public abstract ArrayList<String> getDatums();

    /**
     * returns regional boundaries
     *
     * @return boundaries
     */
    public abstract double[] getBounds();

    /**
     * returns regions
     *
     * @return regions
     */
    public abstract String[] getRegions();

    /**
     * returns parameters for which grids are available
     *
     * @return parameters
     */
    public abstract String[] getGridParms();

    /**
     * returns the types of grids available for a given parameter
     *
     * @return type of grids
     */
    public abstract String[] getGridTypes();

    /**
     * returns the number of rows used for the interpolation grid
     *
     * @return number of grid rows
     */
    public abstract int getIntpGridRows();

    /**
     * returns the number of columns used for the interpolation grid
     *
     * @return number of grid columns
     */
    public abstract int getIntpGridCols();

    /**
     * returns the version of grid being used
     *
     * @return grid version
     */
    public abstract String getVersion();

    /**
     * returns an interpolated value
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param block a block of data used for interpolation
     * @return interpolated value
     */
    public abstract double interpolate(double x, double y, double[] block);

//    public static void main(String[] args) {
//        double[] blk = new double[9];
//        blk[0] = -99.000;
//        blk[1] = -2.7;
//        blk[2] = 1.9;
//        blk[3] = -999.0;
//        blk[4] = 1.9;
//        blk[5] = 1.9;
//        blk[6] = -99.0;
//        blk[7] = 1.9;
//        blk[8] = -99.00;
//        System.out.println("ret:" + GridManager.getInstance(NADCON).rankBlock(blk));
//
//        GridManager g = new GridManager();
//        ArrayList<String> ar = g.getDatums();
//        for (String a : ar) {
//            System.out.println(a);
//        }
    //   GridManager g = GridManager.getInstance("Hawaii", "OHD", "NAD83(1986)", "lat", "trn", "nadcon");
//    }
}
