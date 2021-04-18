package gov.noaa.ngs.nadcon5.test;

import gov.noaa.ngs.grid.GridManager;
import gov.noaa.ngs.grid.Transformer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Driver for Nadcon tests
 *
 * @author Krishna.Tadepalli
 * @version 1.0 Date: 02/12/2015
 */
public class TestDriver {

    public static final String NADCON_TEST_FILE = "/gov/noaa/ngs/nadcon5/test/nadcon_in.txt";
    private static final String NADCON_EXPECTED_RESULTS = "/gov/noaa/ngs/nadcon5/test/nadcon_expected.txt";
    private static final String NADCON_ACTUAL_RESULTS = "nadcon_actual.txt";
    private ArrayList<String> testData;
    private PrintStream ps;

    public TestDriver() {
        try {
            ps = new PrintStream(new File(NADCON_ACTUAL_RESULTS));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadTestData() {
        testData = new ArrayList<>();
        try (InputStream stream = TestDriver.class.getResourceAsStream(NADCON_TEST_FILE)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) {
                    testData.add(line);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GridManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void runTests() {
        loadTestData();
        for (String testRow : testData) {
            ps.print(testRow + ",");
            String[] testFlds = testRow.split(",");
            double lat = Double.valueOf(testFlds[1].trim());
            double lon = Double.valueOf(testFlds[2].trim());
            double eht = Double.valueOf(testFlds[3].trim());
            String srcDatum = testFlds[4].trim().toUpperCase();
            if (srcDatum.contains("_")) {
                srcDatum = srcDatum.replace("_", "(").concat(")");
            }
            String destDatum = testFlds[5].trim().toUpperCase();
            if (destDatum.contains("_")) {
                destDatum = destDatum.replace("_", "(").concat(")");
            }

            Transformer t = null;
            t = new Transformer(lat, lon, eht, srcDatum, destDatum,GridManager.NADCON);
            ps.println(t.transform(true));

        }
    }

    private boolean compareFiles() throws FileNotFoundException, IOException {
        InputStream ios = gov.noaa.ngs.transform.test.TestDriver.class.getResourceAsStream(NADCON_EXPECTED_RESULTS);
        BufferedReader expected = new BufferedReader(new InputStreamReader(
                ios));
        BufferedReader actual = new BufferedReader(new FileReader(
                new File(NADCON_ACTUAL_RESULTS)));
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

    public static void main(String[] args) throws IOException {
        TestDriver t = new TestDriver();
        t.runTests();
        if (!t.compareFiles()) {
            System.out.println("Nadcon Tests Failed. Actual results differ from expected results");
        } else {
            System.out.println("All Nadcon Tests Passed. Actual results match expected results");

        }

    }

}
