/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.noaa.ngs.transform;

/**
 * A utility to determine the SPC zone spatially for a given lat-long and datum
 *
 * @author Krishna Tadepalli
 * @version 1.0 Date: 08/12/2011
 *
 */
public class SpcUtil {


    /**
     *
     */
    public SpcUtil() {
    }


    /**
     * determines an SPC zone based an a given lat-long and datum
     * references to internal data sources removed
     * returns defaults
     *
     * @param lat latitude
     * @param lon longitude
     * @param datum reference datum
     * @param zoneOnly true=returns 4-character spc zone only; false=
     * zone+metadata
     * @return 4-character spc zone or "0000" if unable to connect
     * @throws gov.noaa.ngs.transform.CTException if unable to retrieve a zone
     *
     */
    public String getZone(double lat, double lon, String datum, boolean zoneOnly) throws CTException {
        String spcZone = zoneOnly ? "0000" : "0000,XX,XXXXX";
        return spcZone;
    }

    public String getZone(double lat, double lon, String datum) throws CTException {
        return getZone(lat, lon, datum, true);
    }
    public void close(){
        
    }


}
