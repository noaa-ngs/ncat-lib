package gov.noaa.ngs.transform.test;

import gov.noaa.ngs.transform.CTException;
import gov.noaa.ngs.transform.CoordinateTransformation;
import gov.noaa.ngs.transform.SpcUtil;
import gov.noaa.ngs.transform.Usng;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * Test Driver for coordinate transformation
 *
 * @author Krishna Tadepalli
 * @version 1.0 Date: 08/01/2011
 *
 */
public class TestDriver {

    private static final String TRANSFORM_TEST_DATA = "/gov/noaa/ngs/transform/test/testdata.properties";
    private static final String TRANSFORM_EXPECTED_RESULTS = "/gov/noaa/ngs/transform/test/expected_trans.txt";
    private static final String TRANSFORM_ACTUAL_RESULTS = "actual_trans.txt";
    private static final int SPC_SAMPLE = 40;
    private Properties testdata = null;
    private PrintStream ps;

    /**
     * loads test data property file
     *
     * @throws gov.noaa.ngs.transform.CTException unable to find test data
     * @throws java.io.FileNotFoundException - unable to load test data
     */
    public TestDriver() throws CTException, FileNotFoundException {
        ps = new PrintStream(new File(TRANSFORM_ACTUAL_RESULTS));
        testdata = new Properties();
        InputStream istream = null;
        istream = TestDriver.class.getResourceAsStream(TRANSFORM_TEST_DATA);
        if (istream == null) {
            throw new CTException("Unable to find test data");
        } else {
            try {
                testdata.load(istream);
            } catch (IOException ex) {
                throw new CTException("Unable to load test data. Exception:" + ex.getMessage());
            }
        }
    }

    /**
     * verifies whether a DMS format is used for the coordinate
     *
     * @param coordStr
     * @return true=DMS format
     */
    private boolean isDmsFormat(String coordStr) {
        return (coordStr.charAt(0) == 'N' || coordStr.charAt(0) == 'S'
                || coordStr.charAt(0) == 'E' || coordStr.charAt(0) == 'W');
    }

    private boolean compareFiles() throws FileNotFoundException, IOException {
        InputStream ios = TestDriver.class.getResourceAsStream(TRANSFORM_EXPECTED_RESULTS);
        BufferedReader expected = new BufferedReader(new InputStreamReader(
                ios));
        BufferedReader actual = new BufferedReader(new FileReader(
                new File(TRANSFORM_ACTUAL_RESULTS)));
        String line1 = expected.readLine();
        String line2 = actual.readLine();
        while (line1 != null && line2 != null) {
            
            // trim embedded spaces
            
            line1 = line1.replaceAll("\\s+", "");
            line2 = line2.replaceAll("\\s+", "");
            if (!line1.equals(line2)) {
                return false;
            }
            line1 = expected.readLine();
            line2 = actual.readLine();

        }
        return (line1 == null && line2 == null);
    }

    /**
     * steps through test data, runs test cases, and performs forward and
     * inverse transformations for spc, utm, and usng for a given datum
     *
     * @throws CTException
     */
    private void transform() throws CTException {
        System.out.println("Performing all Transformations against testdata.......");
        CoordinateTransformation ct = null;
        String[] keys
                = (testdata.keySet().toArray(new String[testdata.size()]));
        Arrays.sort(keys);
        String datum = null;
        for (int i = 0; i < keys.length; i++) {
            String coordArStr = testdata.getProperty(keys[i]);
            String[] coordAr = coordArStr.split(",");
            if (coordAr.length < 2) {
                continue;
            }
            ps.println("******TEST:" + i + "******");
            double lat = isDmsFormat(coordAr[0]) ? CoordinateTransformation.toDecimalDeg(coordAr[0])
                    : Double.parseDouble(coordAr[0]);
            double lon = isDmsFormat(coordAr[1]) ? CoordinateTransformation.toDecimalDeg(coordAr[1])
                    : Double.parseDouble(coordAr[1]);
            if (keys[i].startsWith("xyz") || keys[i].startsWith("llh")) {
                datum = keys[i].substring(4);
                ct = CoordinateTransformation.getInstance(null, datum);
                String retStr = null;
                if (keys[i].startsWith("xyz")) {
                    retStr = ct.toGeodeticCoordinates(Double.parseDouble(coordAr[0]), Double.parseDouble(coordAr[1]), Double.parseDouble(coordAr[2]));
                    ps.println("xyz2llh conversion:datum:" + datum + "," + " xyz:" + coordArStr + " llh:" + retStr);
                } else {
                    retStr = ct.toProjectedCoordinates(lat, lon, Double.parseDouble(coordAr[2]));
                    ps.println("llh2xyz conversion:datum:" + datum + "," + " llh:" + coordArStr + " xyz:" + retStr);
                }
                continue;
            } else {
                datum = keys[i].substring(8);
            }
            String zone = null;
            String label = null;
            for (int j = 0; j < 3; j++) {
                if (j == 0) {
                    zone = keys[i].substring(3, 7);
                    label = "spc";
                } else if (j == 1) {
                    zone = Integer.toString(CoordinateTransformation.findUtmZone(lon));
                    label = "utm";
                } else {
                    Usng usng = new Usng(datum);
                    String usngStr = usng.toUsng(lat, lon);
                    ps.println("\nlat-lon:" + lat + " " + lon + " usng:" + datum + "," + usngStr);
                    ps.println("USNG Inverse:" + " datum:" + datum + " lat-longs:" + usng.toGeodeticCoordinates(usngStr));
                    continue;
                }

                ct = CoordinateTransformation.getInstance(zone, datum);
                String spc = ct.toProjectedCoordinates(lat, lon);
                ps.println("\nlat-long:" + lat + " " + lon + " " + label + ":" + spc);
                String[] spcEle = spc.split(",");
                String inverse = lat < 0.0 ? ct.toGeodeticCoordinates(Double.parseDouble(spcEle[3]), Double.parseDouble(spcEle[4]), true)
                        : ct.toGeodeticCoordinates(Double.parseDouble(spcEle[3]), Double.parseDouble(spcEle[4]));
                String str = spcEle[5].equals("N/A")? "N/A":CoordinateTransformation.toDMS(Double.valueOf(spcEle[5]));
                ps.println("Inverse:" + inverse + " " + " convergence in DMS:" + str);
            }
        }

    }

    private boolean checkSpc() throws CTException {
        System.out.println("Performing randomized tests on SPC zones.....");
        CoordinateTransformation ct = null;
        String[] keys
                = (testdata.keySet().toArray(new String[testdata.size()]));
        Arrays.sort(keys);
        String datum = null;
        SpcUtil spcUtil = new SpcUtil();
        for (int i = 0; i < keys.length; i += SPC_SAMPLE) {
            String coordArStr = testdata.getProperty(keys[i]);
            String[] coordAr = coordArStr.split(",");
            if (coordAr.length < 2) {
                continue;
            }
            double lat = isDmsFormat(coordAr[0]) ? CoordinateTransformation.toDecimalDeg(coordAr[0])
                    : Double.parseDouble(coordAr[0]);
            double lon = isDmsFormat(coordAr[1]) ? CoordinateTransformation.toDecimalDeg(coordAr[1])
                    : Double.parseDouble(coordAr[1]);
            if (keys[i].startsWith("xyz") || keys[i].startsWith("llh")) {
                continue;
            }
            datum = keys[i].substring(8);
            String state = keys[i].substring(0, 2);
            if (state.equals("KY") || state.equals("LA")) {
                continue;
            }
            String expectedZone = keys[i].substring(3, 7);
            String actualZone = spcUtil.getZone(lat, lon, datum, true);

            if (!actualZone.equals(expectedZone)) {
                System.out.println("Test " + i + " Failed");
                return false;
            }
        }
        spcUtil.close();
        return true;

    }

    public static void main(String[] args) {
        try {
            TestDriver driver = new TestDriver();
            String spcStr = System.getProperty("spctest", "none");
            boolean spcTest = !(spcStr.equals("none"));
            if (spcTest) {
                System.out.println("doing spc tests");
                if (!driver.checkSpc()) {
                    System.out.println("Tests Failed. Actual results differ from expected results");
                } else {
                    System.out.println("All Tests Passed. Actual results match expected results");

                }

            } else {
                driver.transform();
                if (!driver.compareFiles()) {
                    System.out.println("Tests Failed. Actual results differ from expected results");
                } else {
                    System.out.println("All Tests Passed. Actual results match expected results");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
