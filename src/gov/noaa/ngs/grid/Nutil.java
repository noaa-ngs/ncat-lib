package gov.noaa.ngs.grid;

import gov.noaa.ngs.transform.CTException;
import gov.noaa.ngs.transform.CoordinateTransformation;
import gov.noaa.ngs.transform.SpcUtil;
import gov.noaa.ngs.transform.Usng;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Utility methods for datum transformation and coordinate conversion
 *
 * @author Krishna.Tadepalli
 * @version 1.1 Date: 03/12/2019
 */
public class Nutil {

    private static final Map<String, String> REGEX_MAP = initRegexMap();
    private static final String[] EXCLUDED_SPC_DATUMS = {"USSD", "SP1897", "SP1952", "SG1897", "SG1952", "SL1952"};
    private static final String[] NAD27_DATUMS = {"NAD27", "OHD", "GU63", "AS62", "PR40"};
    public static final String CF_FORMAT = "%.8f"; //combined factor format
    public static final double NAD83_RF = 298.257222101; //reciprocal of earth flattening
    public static final double NAD83_RADIUS = 6378137.000000; //earth eq. radius
    public static final double NAD27_RF = 294.978698214; //reciprocal of earth flattening
    public static final double NAD27_RADIUS = 6378206.4; //earth eq. radius
    public static final double MET2USFT = 0.304800609601219241;
    public static final double MET2IFT = 0.3048;
    private static final int RAD_MIN = 6300000;
    private static final int RAD_MAX = 6400000;
    private static final int INVF_MIN = 180;
    private static final int INVF_MAX = 310;
    private static final String META_CONFIG = "/gov/noaa/ngs/grid/resources/meta.properties";

    private static Map<String, String> initRegexMap() {
        Map<String, String> map = new HashMap<>();
        map.put("hemi", "[NnSsEeWw]");
        map.put("latdms", "[NnSn]?[0-8][0-9][0-5][0-9][0-5][0-9](\\.\\d+)?");
        map.put("londms", "[EeWw]?([0][0-9][0-9][0-5][0-9][0-5][0-9]|[1][0-7][0-9][0-5][0-9][0-5][0-9])(\\.\\d+)?");
        map.put("declat", "[+-]?\\d+(\\.\\d+)?");
        map.put("declon", "[+-]?\\d+(\\.\\d+)?");
        map.put("height", "[+-]?\\d+(\\.\\d+)?");
        map.put("aparm", "[+-]?\\d*+(\\.\\d+)?");
        map.put("invf", "[+-]?\\d*+(\\.\\d+)?");
        map.put("northing", "\\d+(\\.\\d+)?");
        map.put("easting", "\\d+(\\.\\d+)?");
        map.put("spczone", "[0-9][0-9][0-9][0-9]");
        map.put("utmzone", "([0-5][0-9]|(60)|[0-9])");
        map.put("units", "(m)|(usft)|(ift)");
        map.put("usng", "^\\d{1,2}[a-zA-Z]{3}((\\d{6})|(\\d{8})|(\\d{10}))$");
        map.put("x", "[+-]?\\d+(\\.\\d+)?");
        map.put("y", "[+-]?\\d+(\\.\\d+)?");
        map.put("z", "[+-]?\\d+(\\.\\d+)?");

        return Collections.unmodifiableMap(map);
    }

    /**
     * transforms horizontal and vertical datums
     *
     * @param inDatum input horizontal datum
     * @param outDatum output horizontal datum
     * @param lat latitude
     * @param lon longitude
     * @param height ellipsoid height
     * @param inVertDatum input vertical datum
     * @param outVertDatum output vertical datum
     * @param orthoHt orthometric height
     * @return transformed coordinate
     */
    public static CoordSet transform(String inDatum, String outDatum,
            double lat, double lon, double height, String inVertDatum, String outVertDatum, double orthoHt) {
        CoordSet p = new CoordSet();
        Transformer t = null;
        Transformer t2 = null;
        boolean hasHeight = !Double.isNaN(height);
        boolean hasOrthoHeight = !Double.isNaN(orthoHt);
        boolean isForward = true; // transformation to a newer datum
        //
        //** transformation of both ellipsoid and orthometric height is not currently
        //** allowed as such transformation is likely to result in misiterpretation
        //** of results. Return null for now
        //
//        if (hasHeight && hasOrthoHeight) {
//            return null;
//        }
        p.setSrcDatum(inDatum);
        p.setDestDatum(outDatum);
        if (inVertDatum != null && inVertDatum.length() > 1) {
            p.setSrcVertDatum(inVertDatum);
        } else {
            p.setSrcVertDatum("N/A");
        }
        if (outVertDatum != null && outVertDatum.length() > 1) {
            p.setDestVertDatum(outVertDatum);
        } else {
            p.setDestVertDatum("N/A");
        }
        p.setSrcLat(String.format("%.10f", lat));
        p.setSrcLatDms(CoordinateTransformation.toDMS(lat, true));
        p.setSrcLon(String.format("%.10f", lon));
        p.setSrcLonDms(CoordinateTransformation.toDMS(lon, false));
        if (hasHeight) {
            p.setSrcEht(String.format("%.3f", height));
        } else {
            p.setSrcEht("N/A");
        }
        // horizontal transformation
        if (inDatum.equalsIgnoreCase(outDatum)) {
            // no horizontal transformation is needed
            // for an international coordinate, set destination to N/A
            if (inDatum.equalsIgnoreCase("N/A") || inDatum.length() == 0) {
                p.setDestLat("N/A");
                p.setDestLatDms("N/A");
                p.setSigLat("N/A");
                p.setSigLat_m("N/A");
                p.setDeltaLat("N/A");
                p.setDestLon("N/A");
                p.setDestLonDms("N/A");
                p.setDeltaLon("N/A");
                p.setSigLon("N/A");
                p.setSigLon_m("N/A");
                p.setDestEht("N/A");
                p.setSigEht("N/A");
            } else {
                // for nadcon regions, copy source set to destination
                p.setDestLat(p.getSrcLat());
                p.setDestLatDms(p.getSrcLatDms());
                p.setDeltaLat("0.000");
                p.setSigLat("0.000000");
                p.setSigLat_m("0.0000");
                p.setDestLon(p.getSrcLon());
                p.setDestLonDms(p.getSrcLonDms());
                p.setDeltaLon("0.000");
                p.setSigLon("0.000000");
                p.setSigLon_m("0.0000");
                p.setDestEht(p.getSrcEht());
                if (hasHeight) {
                    p.setSigEht("0.000");
                } else {
                    p.setSigEht("N/A");
                }
            }
        } else {
            // transform horizontal datums
            t = hasHeight ? new Transformer(lat, lon, height, inDatum, outDatum, GridManager.NADCON)
                    : new Transformer(lat, lon, inDatum, outDatum, GridManager.NADCON);
            String result = t.transform(true);
            isForward = t.isTransDir();
            String[] resultAr = result.split(",");
            if (resultAr.length > 1) {

                p.setDestLat(resultAr[0]);
                p.setDestLatDms(resultAr[1]);
                p.setSigLat(resultAr[2]);
                p.setDestLon(resultAr[3]);
                p.setDestLonDms(resultAr[4]);
                p.setSigLon(resultAr[5]);
                p.setDestEht(resultAr[6]);
                p.setSigEht(resultAr[7]);
                p = toLinear(p);

            } else {
                // no transformation available or errored out
                p.setDestLat("N/A");
                p.setDestLatDms("N/A");
                p.setDeltaLat("N/A");
                p.setSigLat("N/A");
                p.setSigLat_m("N/A");
                p.setDestLon("N/A");
                p.setDestLonDms("N/A");
                p.setDeltaLon("N/A");
                p.setSigLon("N/A");
                p.setSigLon_m("N/A");
                p.setDestEht("N/A");
                p.setSigEht("N/A");
            }
        }
        // vertical transformation
        if (hasOrthoHeight) {
            p.setSrcOrthoht(String.format("%.3f", orthoHt));
            if (inVertDatum.equalsIgnoreCase(outVertDatum)) {
                // no vertical transformation is needed
                // for an international coordinate, set destination to N/A
                if (inVertDatum.equalsIgnoreCase("N/A") || inVertDatum.length() == 0) {
                    p.setDestOrthoht("N/A");
                    p.setSigOrthoht("N/A");
                } else {
                    p.setDestOrthoht(p.getSrcOrthoht());
                    p.setSigOrthoht("0.000");
                }
            } else {
                // use a more recent coordinate for ortho transformation
                double xlat;
                double xlon;
                if (isForward) { //destination coordinate is more recent
                    double[] llh = getDestCoord(p, false);
                    xlat = llh[0];
                    xlon = llh[1];
                } else { // source coordinate is more recent
                    xlat = lat;
                    xlon = lon;
                }
                t2 = new Transformer(xlat, xlon, orthoHt, inVertDatum, outVertDatum, GridManager.VERTCON);
                String result = t2.transform(true);
                String[] resultAr = result.split(",");
                if (resultAr.length > 1) {
                    p.setDestOrthoht(resultAr[5]);
                    p.setSigOrthoht(resultAr[6]);
                } else {
                    // no tarnsformation available or errored out
                    p.setDestOrthoht("N/A");
                    p.setSigOrthoht("N/A");
                }
            }
        } else {
            // no ortho height provided
            p.setSrcOrthoht("N/A");
            p.setDestOrthoht("N/A");
            p.setSigOrthoht("N/A");

        }
        return p;
    }

    /**
     * transforms horizontal datums
     *
     * @param inDatum input datum being transformed
     * @param outDatum output datum
     * @param lat latitude being transformed
     * @param lon longitude being transformed
     * @param height height being transformed
     * @return coordinate set that contains transformed coordinate
     */
    public static CoordSet transform(String inDatum, String outDatum,
            double lat, double lon, double height) {
        return transform(inDatum, outDatum,
                lat, lon, height, "N/A", "N/A", Double.NaN);
    }

    /**
     * Converts transformed geodetic coordinate to SPC,UTM,XYZ,USNG
     *
     * @param p container for transformed geodetic coordinate
     * @param outDatum destination datum
     * @param pDatum reference datum
     * @param aParm equatorial radius
     * @param fParm earth flattening
     * @param spcZone spc zone for override
     * @param utmZone utml zone for override
     * @param coOptions coordinate options
     * @return coordinate set with transformed and converted coordinates
     * @throws CTException if unable to find an SPC zone
     */
    public static CoordSet getCoordSet(CoordSet p, String outDatum, String pDatum,
            double aParm, double fParm, String spcZone, String utmZone, String[] coOptions) throws CTException {

        boolean specialCase = pDatum.equalsIgnoreCase("other");
        double[] destCoord = getDestCoord(p, specialCase);
        double dlat = destCoord[0];
        double dlon = destCoord[1];
        double dheight = destCoord[2];
        boolean hasHeight = !Double.isNaN(dheight);
        CoordinateTransformation ct = null;
        double elevFactor = 0.0;
        List<String> optionsList = Arrays.asList(coOptions);
        // SPC conversion
        if (optionsList.contains("SPC")) {
            spcZone = getSpcZone(spcZone, outDatum, dlat, dlon, pDatum);
            if (spcZone.equals("0000") || spcZone.equalsIgnoreCase("N/A")) {
                p = initSpc(p);
            } else {
                ct = CoordinateTransformation.getInstance(spcZone, pDatum);
                String spc = ct.toProjectedCoordinates(dlat, dlon);
                elevFactor = hasHeight ? ct.getElevationFactor(dlat, dheight) : 0.0;
                String[] spcParm = spc.split(",");
                String spcNorthing = spcParm[3];
                String spcEasting = spcParm[4];
                String spcConvergence = spcParm[5];
                String spcScaleFactor = spcParm[6];
                String spcCombinedFactor = hasHeight
                        ? (spcScaleFactor.equals("N/A") ? "N/A" : String.format(CF_FORMAT, Double.valueOf(spcScaleFactor) * elevFactor)) : "N/A";
                p.setSpcZone(getSpcZoneLabel(pDatum, spcZone));
                // Northing and easting coordinates for Guam nad27 zone are in meters
                if (pDatum.equalsIgnoreCase("nad83") || spcZone.equals("5400")) {
                    p.setSpcNorthing_m(String.format("%,.3f", Double.valueOf(spcNorthing)));
                    p.setSpcNorthing_usft(String.format("%,.3f", Double.valueOf(spcNorthing) / MET2USFT));
                    p.setSpcNorthing_ift(String.format("%,.3f", Double.valueOf(spcNorthing) / MET2IFT));
                    p.setSpcEasting_m(String.format("%,.3f", Double.valueOf(spcEasting)));
                    p.setSpcEasting_usft(String.format("%,.3f", Double.valueOf(spcEasting) / MET2USFT));
                    p.setSpcEasting_ift(String.format("%,.3f", Double.valueOf(spcEasting) / MET2IFT));
                } else {
                    // spc coordinates are in usfeet for nad27
                    p.setSpcNorthing_m(String.format("%,.3f", Double.valueOf(spcNorthing) * MET2USFT));
                    p.setSpcNorthing_usft(String.format("%,.3f", Double.valueOf(spcNorthing)));
                    p.setSpcNorthing_ift(String.format("%,.3f", Double.valueOf(spcNorthing) * MET2USFT / MET2IFT));
                    p.setSpcEasting_m(String.format("%,.3f", Double.valueOf(spcEasting) * MET2USFT));
                    p.setSpcEasting_usft(String.format("%,.3f", Double.valueOf(spcEasting)));
                    p.setSpcEasting_ift(String.format("%,.3f", Double.valueOf(spcEasting) * MET2USFT / MET2IFT));

                }
                spcConvergence = spcConvergence.equals("N/A") ? "N/A" : toDMS(Double.valueOf(spcConvergence));
                p.setSpcConvergence(spcConvergence);
                p.setSpcScaleFactor(spcScaleFactor);
                p.setSpcCombinedFactor(spcCombinedFactor);
            }
        } else {
            p = initSpc(p);
        }

        //utm conversion
        String utmNorthing = null;
        String utmEasting = null;
        if (optionsList.contains("UTM") || optionsList.contains("USNG")) {
            utmZone = utmZone.equals("auto")
                    ? Integer.toString(CoordinateTransformation.findUtmZone(dlon)) : utmZone.trim();
            String utm = "";
            //      double fparm = 0.0;
            if (specialCase) {

                // a datum other than nad83 or nad27 is in play; convert to lat-long 
                // using (a,f) parms. CoordinateTransformation object is 
                // instantiated using nad83 just to fetch constants that are common
                // to all datums.
                ct = CoordinateTransformation.getInstance(utmZone, "nad83");
                utm = ct.toProjectedCoordinates(aParm, fParm, dlat, dlon);

            } else {
                ct = CoordinateTransformation.getInstance(utmZone, pDatum);
                utm = ct.toProjectedCoordinates(dlat, dlon);
            }
            String[] utmParm = utm.split(",");
            utmZone = utmParm[1].trim();
            String utmScaleFactor = utmParm[6];
            // if elevation factor is not computed before, compute it here
            if ((int) elevFactor == 0) {
                elevFactor = hasHeight ? ct.getElevationFactor(dlat, dheight) : 0.0;
            }
            String utmCombinedFactor = hasHeight
                    ? (utmScaleFactor.equals("N/A") ? "N/A" : String.format(CF_FORMAT, Double.valueOf(utmScaleFactor) * elevFactor)) : "N/A";
            utmNorthing = utmParm[3];
            utmEasting = utmParm[4];
            String utmConvergence = utmParm[5];
            p.setUtmZone(getUtmZoneLabel(utmZone));
            p.setUtmNorthing(String.format("%,.3f", Double.valueOf(utmNorthing)));
            p.setUtmEasting(String.format("%,.3f", Double.valueOf(utmEasting)));
            p.setUtmConvergence(toDMS(Double.valueOf(utmConvergence)));
            p.setUtmScaleFactor(utmScaleFactor);
            p.setUtmCombinedFactor(utmCombinedFactor);
        } else {
            p.setUtmZone("N/A");
            p.setUtmNorthing("N/A");
            p.setUtmEasting("N/A");
            p.setUtmConvergence("N/A");
            p.setUtmScaleFactor("N/A");
            p.setUtmCombinedFactor("N/A");

        }
        // xyz conversion
        if (optionsList.contains("XYZ")) {
            String xyzStr = "N/A,N/A,N/A";
            if (specialCase) {
                ct = CoordinateTransformation.getInstance(null, pDatum);
                if (hasHeight) {
                    xyzStr = ct.toProjectedCoordinates(dlat, dlon, dheight, aParm, fParm);
                }

            } else {
                if (hasHeight) {
                    ct = CoordinateTransformation.getInstance(null, pDatum);
                    xyzStr = ct.toProjectedCoordinates(dlat, dlon, dheight);
                }
//            System.out.println("pDatum:" + pDatum + " lat:" + dlat + " lon:" + dlon + " height:" + dheight + " xyz:" + xyzStr);
            }
            String[] xyzParm = xyzStr.split(",");
            if (hasHeight) {
                p.setX(String.format("%,.3f", Double.valueOf(xyzParm[0])));
                p.setY(String.format("%,.3f", Double.valueOf(xyzParm[1])));
                p.setZ(String.format("%,.3f", Double.valueOf(xyzParm[2])));
            } else {
                p.setX("N/A");
                p.setY("N/A");
                p.setZ("N/A");

            }
        } else {
            p.setX("N/A");
            p.setY("N/A");
            p.setZ("N/A");

        }
        if (optionsList.contains("USNG")) {
            // usng conversion
            Usng u = new Usng(pDatum);
            String usng = u.toUsng(Double.valueOf(utmNorthing), Double.valueOf(utmEasting), Integer.valueOf(utmZone), dlat);
            p.setUsng(usng);
        } else {
            p.setUsng("N/A");
        }
        p.setNadconVersion(GridManager.getInstance("nadcon").getVersion());
        p.setVertconVersion(GridManager.getInstance("vertcon").getVersion());

        return p;

    }

    public static CoordSet getCoordSet(CoordSet p, String outDatum, String pDatum,
            double aParm, double fParm, String spcZone, String utmZone) throws CTException {
        String[] options = new String[4];
        options[0] = "SPC";
        options[1] = "UTM";
        options[2] = "USNG";
        options[3] = "XYZ";
        return getCoordSet(p, outDatum, pDatum, aParm, fParm, spcZone, utmZone, options);
    }

    /**
     * returns destination coordinate from coordinate set. If no datum
     * transformation occurred, source coordinate is returned
     *
     * @param p transformed coordinate
     * @param specialCase international coordinate
     * @return returns source LLh if no transformation occurred
     */
    public static double[] getDestCoord(CoordSet p, boolean specialCase) {
        double[] coord = new double[3];
        String destLat = p.getDestLat();
        coord[0] = destLat.equalsIgnoreCase("N/A") ? Double.valueOf(p.getSrcLat())
                : Double.valueOf(destLat);
        String destLon = p.getDestLon();
        coord[1] = destLon.equalsIgnoreCase("N/A") ? Double.valueOf(p.getSrcLon())
                : Double.valueOf(destLon);
        String destEht = p.getDestEht();
        String srcEht = p.getSrcEht();
        // *** fix the bug here  ***
        if (specialCase) {
            // international coordinate, if destination height is not
            // available, return source height
            coord[2] = destEht.equalsIgnoreCase("N/A")
                    ? (srcEht.equalsIgnoreCase("N/A") ? Double.NaN
                    : Double.valueOf(srcEht)) : Double.valueOf(destEht);
        } else {
            coord[2] = destEht.equalsIgnoreCase("N/A") ? Double.NaN : Double.valueOf(destEht);
        }

        return coord;
    }

    /**
     * converts an SPC coordinate to geodetic
     *
     * @param datum datum
     * @param northing northing
     * @param easting easting
     * @param units units
     * @param zone spc zone
     * @return lat-long
     */
    public static double[] getCoordSet(String datum, double northing, double easting, String units, String zone) {
        CoordinateTransformation ct = null;
//        System.out.println("spc conv args:" + datum + "," + northing + "," + easting + "," + units + "," + zone);
        // convert SPC northing and easting to meters if datum is nad83
        // Northing and easting coordinates for Guam nad27 zone are in meters

        if (datum.equalsIgnoreCase("nad83") || zone.equals("5400")) {
            if (units.equalsIgnoreCase("usft")) {
                northing *= MET2USFT;
                easting *= MET2USFT;
            } else if (units.equalsIgnoreCase("ift")) {
                northing *= MET2IFT;
                easting *= MET2IFT;
            }
        } else {
            // convert SPC northing and easting to usft
            if (units.equalsIgnoreCase("m")) {
                northing /= MET2USFT;
                easting /= MET2USFT;
            } else if (units.equalsIgnoreCase("ift")) {
                northing *= MET2IFT / MET2USFT;
                easting *= MET2IFT / MET2USFT;
            }
        }
        ct = CoordinateTransformation.getInstance(zone, datum);
        String resultStr = ct.toGeodeticCoordinates(northing, easting);
        //       System.out.println("northing:"+northing+" easting:"+easting+" zone:"+zone+"datum:"+datum+" result:"+resultStr);
        String[] result = resultStr.split(",");
        double[] gCoord = new double[2];
        gCoord[0] = Double.valueOf(result[3]);
        gCoord[1] = Double.valueOf(result[4]);
        return gCoord;

    }

    /**
     * converts an utm coordinate to geodetic
     *
     * @param northing northing
     * @param easting easting
     * @param zone utm zone
     * @param hemi hemisphere(N/S)
     * @param ellipsoid ellispoid
     * @param aParm Equatorial radius
     * @param fParm Earth flattening
     * @return lat-long
     */
    public static double[] getCoordSet(double northing, double easting, String zone, String hemi,
            String ellipsoid, double aParm, double fParm) {
        CoordinateTransformation ct = null;
        boolean southernHemisphere = hemi.equalsIgnoreCase("S");
        double[] gCoord = new double[2];
        String resultStr = "";
        if (ellipsoid.equalsIgnoreCase("other")) {
            // a datum other than nad83 or nad27 is in play; convert to lat-long 
            // using (a,f) parms. CoordinateTransformation object is 
            // instantiated using nad83 just to fetch constants that are common
            // to all datums.
            ct = CoordinateTransformation.getInstance(zone, "nad83");
            resultStr = ct.toGeodeticCoordinates(aParm, fParm, northing, easting, southernHemisphere);
        } else {
            String datum = ellipsoid.equalsIgnoreCase("GRS80") ? "nad83" : "nad27";
            ct = CoordinateTransformation.getInstance(zone, datum);
            resultStr = southernHemisphere ? ct.toGeodeticCoordinates(northing, easting, true)
                    : ct.toGeodeticCoordinates(northing, easting);
            //           System.out.println("Util:resultStr:" + resultStr);
        }
        String[] result = resultStr.split(",");
        if (resultStr.length() > 4) {
            gCoord[0] = Double.valueOf(result[3]);
            gCoord[1] = Double.valueOf(result[4]);
        }
        return gCoord;
    }

    /**
     * returns an ellipsoid for a given datum
     *
     * @param datum datum
     * @return ellipsoid
     */
    public static String getEllipsoid(String datum) {
        if (datum.equalsIgnoreCase("other")) {
            return datum;
        }
        return datum.equalsIgnoreCase("nad83") ? "GRS80" : "CLARK1866";
    }

    /**
     * converts an xyz coordinate to geodetic
     *
     * @param x x
     * @param y y
     * @param z z
     * @param ellipsoid reference ellipsoid
     * @param aParm Equatorial radius
     * @param fParm Earth flattening
     * @return lat-long-height
     */
    public static double[] getCoordSet(double x, double y, double z,
            String ellipsoid, double aParm, double fParm) {
        CoordinateTransformation ct = null;
        double[] gCoord = new double[3];
        String resultStr = "";
        if (ellipsoid.equalsIgnoreCase("other")) {
            // a datum other than nad83 or nad27 is in play; convert to lat-long 
            // using (a,f) parms. CoordinateTransformation object is 
            // instantiated using nad83 just to fetch constants that are common
            // to all datums.
            ct = CoordinateTransformation.getInstance(null, "nad83");
            resultStr = ct.toGeodeticCoordinates(x, y, z, aParm, fParm);
        } else {
            String datum = ellipsoid.equalsIgnoreCase("GRS80") ? "nad83" : "nad27";
            ct = CoordinateTransformation.getInstance(null, datum);
            resultStr = ct.toGeodeticCoordinates(x, y, z);
            //           System.out.println("Nutil:resultStr:" + resultStr);
        }
        String[] result = resultStr.split(",");
        if (resultStr.length() > 2) {
            gCoord[0] = Double.valueOf(result[0]);
            gCoord[1] = Double.valueOf(result[1]);
            gCoord[2] = Double.valueOf(result[2]);
        }
        return gCoord;
    }

    /**
     * updates and returns coordinate set for a given spc zone
     *
     * @param c coordinate set
     * @param spcZone SPC zone
     * @return updated coordinate set
     */
    public static CoordSet getCoordSet(CoordSet c, String spcZone) {
        if (spcZone.equals("0000") || spcZone.equalsIgnoreCase("N/A")) {
            return c;
        }
        int zonePos = spcZone.indexOf("-") + 1;
        if (zonePos == 0) {
            return c;
        }
        String zone = spcZone.substring(zonePos, zonePos + 4);
        String destDatum = c.getDestDatum();
        String pDatum = getPdatum(destDatum);
        boolean specialCase = pDatum.equalsIgnoreCase("other");
        if (specialCase) {
            return c;
        }
        double[] destCoord = getDestCoord(c, specialCase);  //LLh coord
        boolean hasHeight = !Double.isNaN(destCoord[2]);
        CoordinateTransformation ct = CoordinateTransformation.getInstance(zone, pDatum);
        String spc = ct.toProjectedCoordinates(destCoord[0], destCoord[1]);
        double elevFactor = hasHeight ? ct.getElevationFactor(destCoord[0], destCoord[2]) : 0.0;
        String[] spcParm = spc.split(",");
        String spcNorthing = spcParm[3];
        String spcEasting = spcParm[4];
        String spcConvergence = spcParm[5];
        String spcScaleFactor = spcParm[6];
        String spcCombinedFactor = hasHeight
                ? (spcScaleFactor.equals("N/A") ? "N/A" : String.format(CF_FORMAT, Double.valueOf(spcScaleFactor) * elevFactor)) : "N/A";
        c.setSpcZone(getSpcZoneLabel(pDatum, zone));
        // Northing and easting coordinates for Guam nad27 zone are in meters
        if (pDatum.equalsIgnoreCase("nad83") || spcZone.equals("5400")) {
            c.setSpcNorthing_m(String.format("%,.3f", Double.valueOf(spcNorthing)));
            c.setSpcNorthing_usft(String.format("%,.3f", Double.valueOf(spcNorthing) / MET2USFT));
            c.setSpcNorthing_ift(String.format("%,.3f", Double.valueOf(spcNorthing) / MET2IFT));
            c.setSpcEasting_m(String.format("%,.3f", Double.valueOf(spcEasting)));
            c.setSpcEasting_usft(String.format("%,.3f", Double.valueOf(spcEasting) / MET2USFT));
            c.setSpcEasting_ift(String.format("%,.3f", Double.valueOf(spcEasting) / MET2IFT));
        } else {
            // spc coordinates are in usfeet for nad27
            c.setSpcNorthing_m(String.format("%,.3f", Double.valueOf(spcNorthing) * MET2USFT));
            c.setSpcNorthing_usft(String.format("%,.3f", Double.valueOf(spcNorthing)));
            c.setSpcNorthing_ift(String.format("%,.3f", Double.valueOf(spcNorthing) * MET2USFT / MET2IFT));
            c.setSpcEasting_m(String.format("%,.3f", Double.valueOf(spcEasting) * MET2USFT));
            c.setSpcEasting_usft(String.format("%,.3f", Double.valueOf(spcEasting)));
            c.setSpcEasting_ift(String.format("%,.3f", Double.valueOf(spcEasting) * MET2USFT / MET2IFT));

        }

        spcConvergence = spcConvergence.equals("N/A") ? "N/A" : toDMS(Double.valueOf(spcConvergence));
        c.setSpcConvergence(spcConvergence);
        c.setSpcScaleFactor(spcScaleFactor);
        c.setSpcCombinedFactor(spcCombinedFactor);

        return c;
    }

    /**
     * updates and returns coordinate set for a given utm zone
     *
     * @param c coordinate to be converted
     * @param pDatum reference datum used for projection
     * @param aParm equatorial radius
     * @param fParm inverse of flattening
     * @param utmZone zone to be used for conversion
     * @return updated coordinate set
     */
    public static CoordSet getCoordSet(CoordSet c, String pDatum, double aParm,
            double fParm, String utmZone) {
        boolean specialCase = pDatum.equalsIgnoreCase("other");
        double[] destCoord = getDestCoord(c, specialCase); //LLh
        double dlat = destCoord[0];
        double dlon = destCoord[1];
        boolean hasHeight = !Double.isNaN(destCoord[2]);
        String utm = "";
        CoordinateTransformation ct = null;
        if (specialCase) {

            // a datum other than nad83 or nad27 is in play; convert to lat-long 
            // using (a,f) parms. CoordinateTransformation object is 
            // instantiated using nad83 just to fetch constants that are common
            // to all datums.
            ct = CoordinateTransformation.getInstance(utmZone, "nad83");
            utm = ct.toProjectedCoordinates(aParm, fParm, dlat, dlon);

        } else {
            ct = CoordinateTransformation.getInstance(utmZone, pDatum);
            utm = ct.toProjectedCoordinates(dlat, dlon);
        }
        double elevFactor = hasHeight ? ct.getElevationFactor(destCoord[0], destCoord[2]) : 0.0;

        String[] utmParm = utm.split(",");
        utmZone = utmParm[1].trim();
        String utmScaleFactor = utmParm[6];
        String utmCombinedFactor = hasHeight
                ? (utmScaleFactor.equals("N/A") ? "N/A" : String.format(CF_FORMAT, Double.valueOf(utmScaleFactor) * elevFactor)) : "N/A";
        String utmNorthing = utmParm[3];
        String utmEasting = utmParm[4];
        String utmConvergence = utmParm[5];
        c.setUtmZone(getUtmZoneLabel(utmZone));
        c.setUtmNorthing(String.format("%,.3f", Double.valueOf(utmNorthing)));
        c.setUtmEasting(String.format("%,.3f", Double.valueOf(utmEasting)));
        c.setUtmConvergence(toDMS(Double.valueOf(utmConvergence)));
        c.setUtmScaleFactor(utmScaleFactor);
        c.setUtmCombinedFactor(utmCombinedFactor);

        Usng u = new Usng(pDatum);
        String usng = u.toUsng(Double.valueOf(utmNorthing), Double.valueOf(utmEasting), Integer.valueOf(utmZone), dlat);
        c.setUsng(usng);
        return c;

    }

    /**
     * converts DMS to decimal degrees
     *
     * @param str DMS string
     * @return decimal degrees
     */
    public static double toDecimalDeg(String str) {
        if (isValid("latdms", str) || isValid("londms", str)) {
            return CoordinateTransformation.toDecimalDeg(str.toUpperCase());
        }
        if (isValid("declat", str)) {
            return Double.valueOf(str);
        }
        return Double.NaN;

    }

    /**
     * checks whether DMS format is used for lat-longs
     *
     * @param coordStr
     * @return
     */
    private static boolean isDmsFormat(String coordStr) {
        return (coordStr.charAt(0) == 'N' || coordStr.charAt(0) == 'S'
                || coordStr.charAt(0) == 'E' || coordStr.charAt(0) == 'W');
    }

    /**
     * validates a given parameter
     *
     * @param parm parameter being validated
     * @param val value being validated
     * @return true=valid
     */
    public static boolean isValid(String parm, String val) {
        return Pattern.matches(REGEX_MAP.get(parm), val);
    }

    /**
     * validates a given datum
     *
     * @param datum datum being validated
     * @param gridType type of grid (nadcon or vertcon)
     * @return true=valid
     */
    public static boolean isValidDatum(String datum, String gridType) {
        if (gridType.equalsIgnoreCase("nadcon")
                || gridType.equalsIgnoreCase("vertcon")) {
            GridManager g = GridManager.getInstance(gridType);
            ArrayList<String> list = g.getDatums();
            return list.contains(datum.toUpperCase());
        }
        return false;
    }

    /**
     * validates radius and flattening parameters
     *
     * @param radius radius
     * @param invf inverse of flattening
     * @return true=parameters are valid
     */
    public static boolean isValidEllipsoid(double radius, double invf) {
        return ((radius >= RAD_MIN && radius <= RAD_MAX)
                && (invf >= INVF_MIN && invf <= INVF_MAX));
    }

    /**
     * converts decimal degrees to DMS
     *
     * @param decimalDeg parameter in decimal degree
     * @return parameter in DMS format
     */
    public static String toDMS(double decimalDeg) {
        String dms = CoordinateTransformation.toDMS(decimalDeg, true);
        String deg = decimalDeg < 0 ? "-" + dms.substring(1, 3) : dms.substring(1, 3);
        return deg + " " + dms.substring(3, 5) + " " + String.format("%05.2f", Double.valueOf(dms.substring(5)));
    }

    /**
     * checks whether a given datum is valid for SPC conversion
     *
     * @param inDatum input datum
     * @return true=no SPC for this datum
     */
    public static boolean isExcludedSpcDatum(String inDatum) {
        for (String edatum : EXCLUDED_SPC_DATUMS) {
            if (inDatum.equalsIgnoreCase(edatum)) {
                return true;
            }
        }
        return false;

    }

    /**
     * returns spc Zone
     *
     * @param datum nadcon5 datum
     * @param lat latitude
     * @param lon longitude
     * @param pDatum reference datum used for projection
     * @param zoneOnly true=returns zone only, false=zone+metadata
     * @return spc zone
     * @throws CTException if unable to retrieve a zone
     */
    public static String getSpcZone(String datum, double lat, double lon, String pDatum, boolean zoneOnly) throws CTException {
        if (isExcludedSpcDatum(datum) || pDatum.equalsIgnoreCase("other")) {
            return zoneOnly ? "0000" : "0000,XX,XXXXX";
        }
        SpcUtil spcUtil = new SpcUtil();

        String spcZone = spcUtil.getZone(lat, lon, pDatum, zoneOnly);
        if (spcZone != null && spcZone.length() == 0) {
            spcZone = zoneOnly ? "0000" : "0000,XX,XXXXX";
        }
        spcUtil.close();

        return spcZone;
    }

    /**
     * returns spc Zone- overloaded to use shared resource
     *
     * @param datum nadcon5 datum
     * @param lat latitude
     * @param lon longitude
     * @param pDatum reference datum used for projection
     * @param spcUtil object used for zone determination
     * @return spc zone
     * @throws CTException if unable to retrieve a zone
     */
    public static String getSpcZone(String datum, double lat, double lon, String pDatum, SpcUtil spcUtil) throws CTException {
        if (isExcludedSpcDatum(datum) || pDatum.equalsIgnoreCase("other")) {
            return "0000";
        }

        String spcZone = spcUtil.getZone(lat, lon, pDatum, true);
        if (spcZone != null && spcZone.length() == 0) {
            spcZone = "0000";
        }

        return spcZone;
    }

    /**
     * returns spc zone for a given lat-long
     *
     * @param spcZone SPC zone
     * @param outDatum output datum
     * @param lat latitude
     * @param lon longitude
     * @param pDatum reference datum
     * @return SPC zone
     * @throws CTException if unable to find an SPC zone
     */
    public static String getSpcZone(String spcZone, String outDatum, double lat, double lon, String pDatum) throws CTException {
        // auto pick one or return one provided by the caller
        return spcZone.equals("auto") ? Nutil.getSpcZone(outDatum, lat, lon, pDatum, true) : spcZone.trim();

    }

    /**
     * generates a label for a given spc zone
     *
     * @param datum reference datum
     * @param zone SPC zone
     * @return label for SPC zone
     */
    public static String getSpcZoneLabel(String datum, String zone) {
        if (zone.equals("0000")) {
            return "N/A";
        }
        Properties p = CoordinateTransformation.getZones(datum);
        return p == null ? null : p.getProperty(zone + ".label", "") + "-" + zone;

    }

    /**
     * generates a label for a given utm zone
     *
     * @param zone UTM zone
     * @return label for UTM zone
     */
    public static String getUtmZoneLabel(String zone) {
        return "UTM Zone " + zone;
    }

    /**
     * returns nad27 or nad83 reference datum to be used for projection
     *
     * @param inDatum nadcon5 datum
     * @return reference datum
     */
    public static String getPdatum(String inDatum) {
        // a condition that is releavant for international datums
        if (inDatum.length() == 0 || inDatum.equalsIgnoreCase("N/A")) {
            return "other";
        }
        // relevant for utm,usng,xyz conversion
        for (String pdatum : EXCLUDED_SPC_DATUMS) {
            if (inDatum.equalsIgnoreCase(pdatum)) {
                return "nad27";
            }
        }
        for (String pdatum : NAD27_DATUMS) {
            if (inDatum.equalsIgnoreCase(pdatum)) {
                return "nad27";
            }
        }
        return "nad83";

    }

    /**
     * returns a utm string for a given usng
     *
     * @param usng USNG
     * @return UTM parsed from USNG
     */
    public static String getUtmStr(String usng) {
        return new Usng().getUtmStr(usng);
    }

    /**
     * initializes spc coordinates
     *
     * @param p coordinate set
     * @return initialized coordinate set
     */
    public static CoordSet initSpc(CoordSet p) {
        p.setSpcZone("N/A");
        p.setSpcNorthing_m("N/A");
        p.setSpcNorthing_usft("N/A");
        p.setSpcNorthing_ift("N/A");
        p.setSpcEasting_m("N/A");
        p.setSpcEasting_usft("N/A");
        p.setSpcEasting_ift("N/A");
        p.setSpcConvergence("N/A");
        p.setSpcScaleFactor("N/A");
        p.setSpcCombinedFactor("N/A");
        return p;
    }

    /**
     * converts US Survey Feet or International Feet to meters
     *
     * @param value value being converted
     * @param units units
     * @return converted value
     */
    public static double toMeters(double value, String units) {
        if (units == null || Double.isNaN(value) || units.equalsIgnoreCase("m")) {
            return value;
        } else if (units.equalsIgnoreCase("usft")) {
            return value * MET2USFT;
        } else if (units.equalsIgnoreCase("ift")) {
            return value * MET2IFT;
        }
        return value;
    }


    /**
     * converts nadcon error estimates and shifts to meters
     *
     * @param c transformed coordinate for which error estimates apply
     * @return a coordinate set that contains linear units
     */
    public static CoordSet toLinear(CoordSet c) {
        double a = 6378137.0;
        double e2 = 0.006694380022900787;
        double lat1 = Double.valueOf(c.getSrcLat());
        double lat2 = Double.valueOf(c.getDestLat());
        double lon1 = Double.valueOf(c.getSrcLon());
        double lon2 = Double.valueOf(c.getDestLon());
        double siglat = Double.valueOf(c.getSigLat());
        double siglon = Double.valueOf(c.getSigLon());
        double latMean = Math.toRadians((lat1 + lat2) / 2);
        double cosLat = Math.cos(latMean);
        double e2Lat2 = 1 - e2 * Math.pow(Math.sin(latMean), 2);
        double denom1 = (Math.pow(e2Lat2, 1.5) * 648000);
        double denom2 = (Math.sqrt(e2Lat2) * 648000);

        double latShiftSec = (lat2 - lat1) * 3600;
        double lonShiftSec = (lon2 - lon1) * 3600;

        c.setDeltaLat(String.format("%.3f", (a * (1 - e2) * latShiftSec * Math.PI) / denom1));
        c.setDeltaLon(String.format("%.3f", (a * cosLat * lonShiftSec * Math.PI) / denom2));
        c.setSigLat_m(String.format("%.4f", (a * (1 - e2) * siglat * Math.PI) / denom1));
        c.setSigLon_m(String.format("%.4f", (a * cosLat * siglon * Math.PI) / denom2));

        return c;
    }

    public static void main(String[] args) throws CTException {
        //    CoordSet c = getCoordSet(Nutil.transform("", "", -33.8688, 151.2093, 0.0),
        //          "", "other", 6378160, 298.25, "auto", "auto");
        CoordSet c = transform("NAD83(2011)", "NAD27",
                36.0, CoordinateTransformation.resetLon(-112.0), Double.NaN, "NGVD29", "NAVD88", Double.NaN);
//        String optionStr = "SPC";
//        c = getCoordSet(c, "NAD83(2011)", "nad83",
//                0.0, 0.0, "5300", "auto", optionStr.split(","));
//CoordSet c = new CoordSet();
//c.setSrcLat("46.0");
//c.setDestLat("45.9999999865");
//c.setSrcLon("-95.0");
//c.setDestLon("-94.9999997414");
//c.setSigLat("0.000004");
//c.setSigLon("0.000008");
//c = toLinear(c);
//System.out.println(c.getDeltaLat() + " " + c.getDeltaLon() + " " + c.getSigLat_m() + " " + c.getSigLon_m());

    }
}
