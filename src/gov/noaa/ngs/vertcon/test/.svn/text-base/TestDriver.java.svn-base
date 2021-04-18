/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.noaa.ngs.vertcon.test;

import gov.noaa.ngs.grid.GridManager;
import gov.noaa.ngs.grid.Transformer;
import gov.noaa.ngs.transform.CoordinateTransformation;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Driver for Vetcon tests
 *
 * @author Krishna.Tadepalli
 * @version 1.0 Date: 03/08/2019
 */
public class TestDriver {

    public static final Map<String, String> map = new HashMap<String, String>() {
        {
            put("conus", "ngvd29.navd88.conus.chk.out");
            put("alaska", "ngvd29.navd88.alaska.chk.out");
            put("vi", "lt.vivd09.vi.chk.out");
            put("pr", "lt.prvd02.pr.chk.out");
            put("cnmi", "lt.nmvd03.cnmi.chk.out");
            put("as", "lt.asvd02.as.chk.out");
            put("guam", "guvd63.guvd04.guam.chk.out");
        }
    };

    public boolean runTests(File testFile) throws FileNotFoundException {
        Scanner s = new Scanner(testFile);
        int count = 0;
        while (s.hasNextLine()) {
            String st = s.nextLine();
            String[] str = st.split("\\s+");
            double lat = Double.valueOf(str[1]);
            double lon = CoordinateTransformation.resetLon(Double.valueOf(str[2]));
            Transformer t = new Transformer(lat, lon, Double.valueOf(str[3]), str[4].toUpperCase(), str[5].toUpperCase(), GridManager.VERTCON);
            String result = t.transform(true);
            String[] resultAr = result.split(",");
            if (!str[6].equals(resultAr[5]) && str[8].equals(resultAr[6])) {
                double d1 = Double.valueOf(str[6]);
                double d2 = Double.valueOf(resultAr[5]);
                int diff = (int) (Math.abs(d1 - d2) * 1000);
                if (diff > 1) {
                    System.out.println(st + " NOT MATCHED");
                    System.out.println("result:" + result);
                    count++;
                }
            }

        }
        return count == 0;
    }

    public static void main(String[] args) throws FileNotFoundException {
        TestDriver td = new TestDriver();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("Running tests for "+entry.getKey().toUpperCase());
            String testFile = "/junk/vertcon3/test/"+entry.getValue();
            if (td.runTests(new File(testFile))) {
            System.out.println("All Tests Passed");
        }
//            System.out.printf("Key : %s and Value: %s %n", entry.getKey(), entry.getValue());
        }


        
    }

}
