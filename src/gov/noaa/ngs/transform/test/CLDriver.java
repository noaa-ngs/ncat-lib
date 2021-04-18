package gov.noaa.ngs.transform.test;

import gov.noaa.ngs.grid.CoordSet;
import gov.noaa.ngs.grid.Nutil;
import gov.noaa.ngs.transform.CTException;
import gov.noaa.ngs.transform.CoordinateTransformation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Krishna.Tadepalli
 * @version 1.0 Date: 06/01/2017 Driver for command line interface of the
 * conversion tool
 */
public class CLDriver {

    /**
     * converts and returns coordinate set as a JSON
     *
     * @param inDatum input Datum
     * @param outDatum output Datum
     * @param llh lat-long-height
     * @param radius equatorial radius, if used
     * @param fparm earth flattening, if used
     * @param spcZone SPC zone to be used
     * @return
     */
    private static String getJson(String inDatum, String outDatum, double[] llh, double radius, double fparm, String spcZone, String utmZone,
            String inVertDatum, String outVertDatum, double orthoHt) {
        CoordSet transCoord = null;
        // map output datum to a reference datum to be used for projection
        String pOutDatum = Nutil.getPdatum(outDatum);
        transCoord = Nutil.transform(inDatum, outDatum,
                llh[0], llh[1], llh[2], inVertDatum, outVertDatum, orthoHt);
        CoordSet c = null;
        try {
            //Step2: convert LLH to SPC,UTM,XYZ,and USNG
            c = Nutil.getCoordSet(transCoord, outDatum, pOutDatum, radius, fparm, spcZone, utmZone);
            c.setID(Long.toString(System.currentTimeMillis()));

        } catch (CTException ex) {
            Logger.getLogger(CLDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toString(c);
    }

    /**
     * returns the coordinate set as a JSON string This step is optional; needed
     * if the output is desired in JSON
     *
     * @param c CoordSet
     * @return CoordSet as a JSON
     */
    private static String toString(CoordSet c) {
        StringBuilder s = new StringBuilder();
        String q = "\"";
        s.append("{\n");
        s.append(q).append("ID").append(q).append(":").append(q).append(c.getID()).append(q).append(",\n");
        s.append(q).append("nadconVersion").append(q).append(":").append(q).append(c.getNadconVersion()).append(q).append(",\n");
        s.append(q).append("vertconVersion").append(q).append(":").append(q).append(c.getVertconVersion()).append(q).append(",\n");
        s.append(q).append("srcDatum").append(q).append(":").append(q).append(c.getSrcDatum()).append(q).append(",\n");
        s.append(q).append("destDatum").append(q).append(":").append(q).append(c.getDestDatum()).append(q).append(",\n");
        s.append(q).append("srcVertDatum").append(q).append(":").append(q).append(c.getSrcVertDatum()).append(q).append(",\n");
        s.append(q).append("destVertDatum").append(q).append(":").append(q).append(c.getDestVertDatum()).append(q).append(",\n");
        s.append(q).append("srcLat").append(q).append(":").append(q).append(c.getSrcLat()).append(q).append(",\n");
        s.append(q).append("srcLatDms").append(q).append(":").append(q).append(c.getSrcLatDms()).append(q).append(",\n");
        s.append(q).append("destLat").append(q).append(":").append(q).append(c.getDestLat()).append(q).append(",\n");
        s.append(q).append("destLatDms").append(q).append(":").append(q).append(c.getDestLatDms()).append(q).append(",\n");
        s.append(q).append("sigLat").append(q).append(":").append(q).append(c.getSigLat()).append(q).append(",\n");
        s.append(q).append("srcLon").append(q).append(":").append(q).append(c.getSrcLon()).append(q).append(",\n");
        s.append(q).append("srcLonDms").append(q).append(":").append(q).append(c.getSrcLonDms()).append(q).append(",\n");
        s.append(q).append("destLon").append(q).append(":").append(q).append(c.getDestLon()).append(q).append(",\n");
        s.append(q).append("destLonDms").append(q).append(":").append(q).append(c.getDestLonDms()).append(q).append(",\n");
        s.append(q).append("sigLon").append(q).append(":").append(q).append(c.getSigLon()).append(q).append(",\n");
        s.append(q).append("srcEht").append(q).append(":").append(q).append(c.getSrcEht()).append(q).append(",\n");
        s.append(q).append("destEht").append(q).append(":").append(q).append(c.getDestEht()).append(q).append(",\n");
        s.append(q).append("sigEht").append(q).append(":").append(q).append(c.getSigEht()).append(q).append(",\n");
        s.append(q).append("srcOrthoht").append(q).append(":").append(q).append(c.getSrcOrthoht()).append(q).append(",\n");
        s.append(q).append("destOrthoht").append(q).append(":").append(q).append(c.getDestOrthoht()).append(q).append(",\n");
        s.append(q).append("sigOrthoht").append(q).append(":").append(q).append(c.getSigOrthoht()).append(q).append(",\n");
        s.append(q).append("spcZone").append(q).append(":").append(q).append(c.getSpcZone()).append(q).append(",\n");
        s.append(q).append("spcNorthing_m").append(q).append(":").append(q).append(c.getSpcNorthing_m()).append(q).append(",\n");
        s.append(q).append("spcEasting_m").append(q).append(":").append(q).append(c.getSpcEasting_m()).append(q).append(",\n");
        s.append(q).append("spcNorthing_usft").append(q).append(":").append(q).append(c.getSpcNorthing_usft()).append(q).append(",\n");
        s.append(q).append("spcEasting_usft").append(q).append(":").append(q).append(c.getSpcEasting_usft()).append(q).append(",\n");
        s.append(q).append("spcNorthing_ift").append(q).append(":").append(q).append(c.getSpcNorthing_ift()).append(q).append(",\n");
        s.append(q).append("spcEasting_ift").append(q).append(":").append(q).append(c.getSpcEasting_ift()).append(q).append(",\n");
        s.append(q).append("spcConvergence").append(q).append(":").append(q).append(c.getSpcConvergence()).append(q).append(",\n");
        s.append(q).append("spcScaleFactor").append(q).append(":").append(q).append(c.getSpcScaleFactor()).append(q).append(",\n");
        s.append(q).append("spcCombinedFactor").append(q).append(":").append(q).append(c.getSpcCombinedFactor()).append(q).append(",\n");

        s.append(q).append("utmZone").append(q).append(":").append(q).append(c.getUtmZone()).append(q).append(",\n");
        s.append(q).append("utmNorthing").append(q).append(":").append(q).append(c.getUtmNorthing()).append(q).append(",\n");
        s.append(q).append("utmEasting").append(q).append(":").append(q).append(c.getUtmEasting()).append(q).append(",\n");
        s.append(q).append("utmConvergence").append(q).append(":").append(q).append(c.getUtmConvergence()).append(q).append(",\n");
        s.append(q).append("utmScaleFactor").append(q).append(":").append(q).append(c.getUtmScaleFactor()).append(q).append(",\n");
        s.append(q).append("utmCombinedFactor").append(q).append(":").append(q).append(c.getUtmCombinedFactor()).append(q).append(",\n");
        s.append(q).append("x").append(q).append(":").append(q).append(c.getX()).append(q).append(",\n");
        s.append(q).append("y").append(q).append(":").append(q).append(c.getY()).append(q).append(",\n");
        s.append(q).append("z").append(q).append(":").append(q).append(c.getZ()).append(q).append(",\n");
        s.append(q).append("usng").append(q).append(":").append(q).append(c.getUsng()).append(q).append("\n");
        s.append("}");
        return s.toString();

    }

    private static boolean isValidLLH(String latStr, String lonStr, String heightStr) {
        if (!(Nutil.isValid("latdms", latStr) || Nutil.isValid("declat", latStr))) {
            System.err.println("Invalid latitude");
            return false;
        }
        if (!(Nutil.isValid("londms", lonStr) || Nutil.isValid("declon", lonStr))) {
            System.err.println("Invalid longitude");
            return false;
        }

        return isValidHeight(heightStr);
    }

    private static boolean isValidHeight(String heightStr) {
        if (heightStr.equalsIgnoreCase("N/A")) {
            return true;
        }
        if (!Nutil.isValid("height", heightStr)) {
            System.err.println("Invalid Ellipsoid height");
            return false;
        }
        return true;
    }

    /**
     * parses and validates input parameters converts input data to LLH, where
     * applicable
     *
     * @param inData input data provided as a comma delimited string
     */
    private static void convert(String[] inData) {
        String convType = inData[0].trim();
        double[] llh = new double[3];
        double radius = 0.0;
        double fparm = 0.0;
        String spcZone = "0000";
        String utmZone = "";
        String destZone = "auto"; // used for converting between zones
        String inDatum = "N/A";
        String outDatum = "N/A";
        String inVertDatum = "N/A";
        String outVertDatum = "N/A";
        double orthoHt = Double.NaN;
        String northing = "";
        String easting = "";
        double[] llCoord;
        switch (convType) {
            case "llh":
            case "llH":
                // No conversions here, validate input data and set LLH 
                String latStr = inData[1];
                String lonStr = inData[2];
                String heightStr = inData[3];
                if (!isValidLLH(latStr, lonStr, heightStr)) {
                    return;
                }
                double lat = Character.isLetter(latStr.charAt(0))
                        ? CoordinateTransformation.toDecimalDeg(latStr)
                        : Double.valueOf(latStr);
                double lon = Character.isLetter(lonStr.charAt(0))
                        ? CoordinateTransformation.toDecimalDeg(lonStr)
                        : Double.valueOf(lonStr);
                if (Math.abs(lat) > 90.0) {
                    System.err.println("Invalid latitude");
                    return;
                }
                if (Math.abs(lon) > 360.0) {
                    System.err.println("Invalid longitude");
                    return;
                }
                // if the field starts with a letter, input and ouput datums
                // are provided
                utmZone = inData.length >= 8 ? inData[7] : inData[6];
                utmZone = utmZone.equalsIgnoreCase("auto") ? "auto" : utmZone;
                if (Character.isLetter(inData[4].charAt(0))) {
                    inDatum = inData[4].trim().toUpperCase();
                    if (!Nutil.isValidDatum(inDatum, "nadcon")) {
                        System.err.println("Invalid input datum");
                        return;
                    }
                    outDatum = inData[5].trim().toUpperCase();
                    if (!Nutil.isValidDatum(outDatum, "nadcon")) {
                        System.err.println("Invalid output datum");
                        return;
                    }

                    spcZone = inData[6].trim();
                    if (!Nutil.isValid("spczone", spcZone)) {
                        System.err.println("Invalid SPC Zone");
                        return;
                    }
                    if (!utmZone.equalsIgnoreCase("auto")) {
                        if (!Nutil.isValid("utmzone", utmZone)) {
                            System.err.println("Invalid UTM zone");
                            return;
                        }

                    }

                } else {
                    // radius and flattening parameters are provided
                    if (!Nutil.isValid("aparm", inData[4])) {
                        System.err.println("Invalid radius");
                        return;
                    }
                    if (!Nutil.isValid("invf", inData[5])) {
                        System.err.println("Invalid inverse of flattening");
                        return;
                    }
                    radius = Double.valueOf(inData[4]);
                    double invfd = Double.valueOf(inData[5]);
                    if ((radius > 0 && invfd > 0) && (!Nutil.isValidEllipsoid(radius, invfd))) {
                        System.err.println("Invalid radius or inverse of flattening");
                        return;
                    }
                    fparm = invfd > 0 ? 1 / invfd : 0.0;
                }
                llh[0] = lat;
                llh[1] = lon;
                if (convType.equals("llh")) {
                    llh[2] = heightStr.equalsIgnoreCase("N/A") ? Double.NaN : Double.valueOf(heightStr);
                } else {
                    llh[2] = Double.NaN;
                    orthoHt = heightStr.equalsIgnoreCase("N/A") ? Double.NaN : Double.valueOf(heightStr);
                    inVertDatum = inData[8].trim().toUpperCase();
                    if (!Nutil.isValidDatum(inVertDatum, "vertcon")) {
                        System.err.println("Invalid input geopotential datum");
                        return;
                    }
                    outVertDatum = inData[9].trim().toUpperCase();
                    if (!Nutil.isValidDatum(outVertDatum, "vertcon")) {
                        System.err.println("Invalid output geopotential datum");
                        return;
                    }

                }

                break;
            case "spch":
            case "spcH":
                // parse, validate, and convert SPC coordinate to LLH
                spcZone = inData[1].trim();
                if (!Nutil.isValid("spczone", spcZone)) {
                    System.err.println("Invalid SPC zone");
                    return;
                }
                northing = inData[2];
                easting = inData[3];
                if (!Nutil.isValid("northing", northing)) {
                    System.err.println("Invalid northing coordinate");
                    return;
                }
                if (!Nutil.isValid("easting", easting)) {
                    System.err.println("Invalid easting coordinate");
                    return;
                }

                String units = inData[4].trim();
                if (!Nutil.isValid("units", units)) {
                    System.err.println("Invalid units");
                    return;

                }

                inDatum = inData[5].trim().toUpperCase();
                if (!Nutil.isValidDatum(inDatum, "nadcon")) {
                    System.err.println("Invalid input datum");
                    return;
                }
                outDatum = inData[6].trim().toUpperCase();
                if (!Nutil.isValidDatum(outDatum, "nadcon")) {
                    System.err.println("Invalid output datum");
                    return;
                }
                utmZone = utmZone = inData[7];
                utmZone = utmZone.equalsIgnoreCase("auto") ? "auto" : utmZone;
                if (!utmZone.equalsIgnoreCase("auto")) {
                    if (!Nutil.isValid("utmzone", utmZone)) {
                        System.err.println("Invalid UTM zone");
                        return;
                    }
                }
                heightStr = inData[8];
                if (!isValidHeight(heightStr)) {
                    return;
                }

                // map input datum to a reference datum used for projection
                String pInDatum = Nutil.getPdatum(inDatum);
                // convert SPC to llh
                llCoord = Nutil.getCoordSet(pInDatum, Double.valueOf(northing),
                        Double.valueOf(easting), units, spcZone);
                llh[0] = llCoord[0];
                llh[1] = llCoord[1];
                if (convType.equals("spch")) {
                    llh[2] = heightStr.equalsIgnoreCase("N/A") ? Double.NaN : Double.valueOf(heightStr);
                    llh[2] = Nutil.toMeters(llh[2], units);
                    if (inData.length > 9) {
                        destZone = inData[9];
                        if (!Nutil.isValid("spczone", destZone)) {
                            System.err.println("Invalid Destination SPC zone");
                            return;
                        }

                    }

                } else {
                    llh[2] = Double.NaN;
                    orthoHt = heightStr.equalsIgnoreCase("N/A") ? Double.NaN : Double.valueOf(heightStr);
                    inVertDatum = inData[9].trim().toUpperCase();
                    if (!Nutil.isValidDatum(inVertDatum, "vertcon")) {
                        System.err.println("Invalid input geopotential datum");
                        return;
                    }
                    outVertDatum = inData[10].trim().toUpperCase();
                    if (!Nutil.isValidDatum(outVertDatum, "vertcon")) {
                        System.err.println("Invalid output geopotential datum");
                        return;
                    }
                    if (inData.length > 11) {
                        destZone = inData[11];
                        if (!Nutil.isValid("spczone", destZone)) {
                            System.err.println("Invalid Destination SPC zone");
                            return;
                        }

                    }

                }

                break;
            case "utmh":
            case "utmH":
                // parse, validate, and convert UTMM coordinate to LLH
                utmZone = inData[1].trim();
                if (!Nutil.isValid("utmzone", utmZone)) {
                    System.err.println("Invalid UTM zone");
                    return;
                }
                northing = inData[2];
                easting = inData[3];
                if (!Nutil.isValid("northing", northing)) {
                    System.err.println("Invalid northing coordinate");
                    return;
                }
                if (!Nutil.isValid("easting", easting)) {
                    System.err.println("Invalid easting coordinate");
                    return;
                }
                String hemi = inData[4];
                if (!Nutil.isValid("hemi", hemi)) {
                    System.err.println("Invalid hemisphere");
                    return;
                }
                // if the field starts with a letter, , input and ouput datums
                // are provided
                if (Character.isLetter(inData[5].charAt(0))) {
                    inDatum = inData[5].trim().toUpperCase();
                    if (!Nutil.isValidDatum(inDatum, "nadcon")) {
                        System.err.println("Invalid input datum");
                        return;
                    }
                    outDatum = inData[6].trim().toUpperCase();
                    if (!Nutil.isValidDatum(outDatum, "nadcon")) {
                        System.err.println("Invalid output datum");
                        return;
                    }
                    spcZone = inData[7].trim();
                    if (!Nutil.isValid("spczone", spcZone)) {
                        System.err.println("Invalid SPC Zone");
                        return;
                    }
                    heightStr = inData[8];
                } else {
                    // radius and flattening parameters are provided
                    if (!Nutil.isValid("aparm", inData[5])) {
                        System.err.println("Invalid radius");
                        return;
                    }
                    if (!Nutil.isValid("invf", inData[6])) {
                        System.err.println("Invalid inverse of flattening");
                        return;
                    }
                    radius = Double.valueOf(inData[5]);
                    double invfd = Double.valueOf(inData[6]);
                    if ((radius > 0 && invfd > 0) && (!Nutil.isValidEllipsoid(radius, invfd))) {
                        System.err.println("Invalid radius or inverse of flattening");
                        return;
                    }
                    fparm = invfd > 0 ? 1 / invfd : 0.0;
                    heightStr = inData[7];
                }
                if (!isValidHeight(heightStr)) {
                    return;
                }

                // map input datum to a reference datum used for projection
                pInDatum = Nutil.getPdatum(inDatum);
                // associate a reference ellipsoid for reference datum
                String ellipsoid = Nutil.getEllipsoid(pInDatum);
                // convert UTM to LLH
                llCoord = Nutil.getCoordSet(Double.valueOf(northing), Double.valueOf(easting), utmZone, hemi, ellipsoid, radius, fparm);
                llh[0] = llCoord[0];
                llh[1] = llCoord[1];
                if (convType.equals("utmh")) {
                    llh[2] = heightStr.equalsIgnoreCase("N/A") ? Double.NaN : Double.valueOf(heightStr);
                    if (inData.length == 10) {
                        destZone = inData[9];
                        if (!Nutil.isValid("utmzone", destZone)) {
                            System.err.println("Invalid Destination UTM zone");
                            return;
                        }

                    }
                } else {
                    llh[2] = Double.NaN;
                    orthoHt = heightStr.equalsIgnoreCase("N/A") ? Double.NaN : Double.valueOf(heightStr);
                    inVertDatum = inData[9].trim().toUpperCase();
                    if (!Nutil.isValidDatum(inVertDatum, "vertcon")) {
                        System.err.println("Invalid input geopotential datum");
                        return;
                    }
                    outVertDatum = inData[10].trim().toUpperCase();
                    if (!Nutil.isValidDatum(outVertDatum, "vertcon")) {
                        System.err.println("Invalid output geopotential datum");
                        return;
                    }

                    // UTM is being converted between zone
                    if (inData.length == 12) {
                        destZone = inData[11];
                        if (!Nutil.isValid("utmzone", destZone)) {
                            System.err.println("Invalid Destination UTM zone");
                            return;
                        }

                    }

                }
                break;
            case "usngh":
            case "usngH":
                // parse, validate, and convert USNG coordinate to LLH
                String usng = inData[1].trim().toUpperCase();
                if (!Nutil.isValid("usng", usng)) {
                    System.err.println("Invalid USNG");
                    return;
                }
                heightStr = inData.length >= 6 ? inData[5] : inData[4];
                if (!isValidHeight(heightStr)) {
                    return;
                }

                // if the field starts with a letter, , input and ouput datums
                // are provided
                if (Character.isLetter(inData[2].charAt(0))) {
                    inDatum = inData[2].trim().toUpperCase();
                    if (!Nutil.isValidDatum(inDatum, "nadcon")) {
                        System.err.println("Invalid input datum");
                        return;
                    }
                    outDatum = inData[3].trim().toUpperCase();
                    if (!Nutil.isValidDatum(outDatum, "nadcon")) {
                        System.err.println("Invalid output datum");
                        return;
                    }
                    spcZone = inData[4].trim();
                    if (!Nutil.isValid("spczone", spcZone)) {
                        System.err.println("Invalid SPC Zone");
                        return;
                    }

                } else {
                    // radius and flattening parameters are provided
                    if (!Nutil.isValid("aparm", inData[2])) {
                        System.err.println("Invalid radius");
                        return;
                    }
                    if (!Nutil.isValid("invf", inData[3])) {
                        System.err.println("Invalid inverse of flattening");
                        return;
                    }
                    radius = Double.valueOf(inData[2]);
                    double invfd = Double.valueOf(inData[3]);
                    if ((radius > 0 && invfd > 0) && (!Nutil.isValidEllipsoid(radius, invfd))) {
                        System.err.println("Invalid radius or inverse of flattening");
                        return;
                    }
                    fparm = invfd > 0 ? 1 / invfd : 0.0;
                }
                // map input datum to a reference datum used for projection
                pInDatum = Nutil.getPdatum(inDatum);
                // associate a reference ellipsoid for reference datum
                ellipsoid = Nutil.getEllipsoid(pInDatum);
                String[] utmStr = Nutil.getUtmStr(usng).split(",");
                utmZone = utmStr[0];
                // convert USNG to LLH
                llCoord = Nutil.getCoordSet(Double.valueOf(utmStr[1]), Double.valueOf(utmStr[2]), utmStr[0], utmStr[3], ellipsoid, radius, fparm);
                llh[0] = llCoord[0];
                llh[1] = llCoord[1];
                if (convType.equals("usngh")) {
                    llh[2] = heightStr.equalsIgnoreCase("N/A") ? Double.NaN : Double.valueOf(heightStr);
                } else {
                    llh[2] = Double.NaN;
                    orthoHt = heightStr.equalsIgnoreCase("N/A") ? Double.NaN : Double.valueOf(heightStr);
                    inVertDatum = inData[6].trim().toUpperCase();
                    if (!Nutil.isValidDatum(inVertDatum, "vertcon")) {
                        System.err.println("Invalid input geopotential datum");
                        return;
                    }
                    outVertDatum = inData[7].trim().toUpperCase();
                    if (!Nutil.isValidDatum(outVertDatum, "vertcon")) {
                        System.err.println("Invalid output geopotential datum");
                        return;
                    }
                }
                break;
            case "xyz":
                // parse, validate, and convert USNG coordinate to LLH
                String x = inData[1];
                String y = inData[2];
                String z = inData[3];
                if (!Nutil.isValid("x", x)) {
                    System.err.println("Invalid X-coordinate");
                    return;
                }
                if (!Nutil.isValid("y", y)) {
                    System.err.println("Invalid Y-coordinate");
                    return;
                }
                if (!Nutil.isValid("z", z)) {
                    System.err.println("Invalid Z-coordinate");
                    return;
                }
                utmZone = inData.length == 8 ? inData[7] : inData[6];
                utmZone = utmZone.equalsIgnoreCase("auto") ? "auto" : utmZone;
                if (!utmZone.equalsIgnoreCase("auto")) {
                    if (!Nutil.isValid("utmzone", utmZone)) {
                        System.err.println("Invalid UTM zone");
                        return;
                    }

                }

                // if the field starts with a letter, , input and ouput datums
                // are provided
                if (Character.isLetter(inData[4].charAt(0))) {
                    inDatum = inData[4].trim().toUpperCase();
                    if (!Nutil.isValidDatum(inDatum, "nadcon")) {
                        System.err.println("Invalid input datum");
                        return;
                    }
                    outDatum = inData[5].trim().toUpperCase();
                    if (!Nutil.isValidDatum(outDatum, "nadcon")) {
                        System.err.println("Invalid output datum");
                        return;
                    }
                    spcZone = inData[6].trim();
                    if (!Nutil.isValid("spczone", spcZone)) {
                        System.err.println("Invalid SPC Zone");
                        return;
                    }

                } else {
                    // radius and flattening parameters are provided
                    if (!Nutil.isValid("aparm", inData[4])) {
                        System.err.println("Invalid radius");
                        return;
                    }
                    if (!Nutil.isValid("invf", inData[5])) {
                        System.err.println("Invalid inverse of flattening");
                        return;
                    }
                    radius = Double.valueOf(inData[4]);
                    double invfd = Double.valueOf(inData[5]);
                    if ((radius > 0 && invfd > 0) && (!Nutil.isValidEllipsoid(radius, invfd))) {
                        System.err.println("Invalid radius or inverse of flattening");
                        return;
                    }
                    fparm = invfd > 0 ? 1 / invfd : 0.0;
                }
                // map input datum to a reference datum used for projection
                pInDatum = Nutil.getPdatum(inDatum);
                // associate a reference ellipsoid for reference datum
                ellipsoid = Nutil.getEllipsoid(pInDatum);
                // convert XYZ to LLH
                llCoord = Nutil.getCoordSet(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z), ellipsoid, radius, fparm);
                llh[0] = llCoord[0];
                llh[1] = llCoord[1];
                llh[2] = llCoord[2];

                break;
            default:
                System.err.println("convType is not a valid conversion type");
        }
        // we now have LLH for input datum. Transform LLH to ouput datum 
        // and complete SPC, UTM, USNG, and XYZ conversions
        if (!destZone.equals("auto")) {
            if (destZone.length() > 2) {
                spcZone = destZone;
            } else {
                utmZone = destZone;
            }
        }

        System.out.println(getJson(inDatum, outDatum, llh, radius, fparm, spcZone, utmZone,
                inVertDatum, outVertDatum, orthoHt));

    }

    public static void main(String[] args) {
        String[] inData = System.getProperty("parms", "").split(",");
 //              String[] inData = "spch,1702,682098.274,3369338.675,usft,NAD83(2011),NAD83(2011),auto,6.367284734569469".split(",");
        if (!(inData.length > 1)) {
            System.err.println("No input parameters provided, terminating the program");
            return;
        }
        convert(inData);

    }

}
