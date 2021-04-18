package gov.noaa.ngs.grid;

import java.io.Serializable;
import static gov.noaa.ngs.grid.Nutil.MET2USFT;
import static gov.noaa.ngs.grid.Nutil.MET2IFT;

/**
 * A bean that encapsulates coordinate set
 *
 * @author Krishna.Tadepalli
 * @version 1.0 Date: 02/12/2015
 */
public class CoordSet implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ID;
    private String nadconVersion;
    private String vertconVersion;
    private String srcDatum;
    private String destDatum;
    private String srcVertDatum;
    private String destVertDatum;
    private String srcLat;
    private String srcLatDms;
    private String destLat;
    private String destLatDms;
    private String deltaLat;
    private String sigLat;
    private String sigLat_m;
    private String srcLon;
    private String srcLonDms;
    private String destLon;
    private String destLonDms;
    private String deltaLon;
    private String sigLon;
    private String sigLon_m;
    private String srcEht;
    private String destEht;
    private String sigEht;
    private String srcOrthoht;
    private String destOrthoht;
    private String sigOrthoht;
    private String spcZone;
    private String spcNorthing_m;
    private String spcEasting_m;
    private String spcNorthing_usft;
    private String spcEasting_usft;
    private String spcNorthing_ift;
    private String spcEasting_ift;
    private String spcConvergence;
    private String spcScaleFactor;
    private String spcCombinedFactor;
    private String utmZone;
    private String utmNorthing;
    private String utmEasting;
    private String utmConvergence;
    private String utmScaleFactor;
    private String utmCombinedFactor;
    private String x;
    private String y;
    private String z;
    private String usng;

    public CoordSet() {

    }

    public String getID() {
        return ID;
    }

    public String getNadconVersion() {
        return nadconVersion;
    }

    public String getVertconVersion() {
        return vertconVersion;
    }

    public void setVertconVersion(String vertconVersion) {
        this.vertconVersion = vertconVersion;
    }

    public void setNadconVersion(String nadconVersion) {
        this.nadconVersion = nadconVersion;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSrcDatum() {
        return srcDatum;
    }

    public void setSrcDatum(String srcDatum) {
        this.srcDatum = srcDatum;
    }

    public String getDestDatum() {
        return destDatum;
    }

    public void setDestDatum(String destDatum) {
        this.destDatum = destDatum;
    }

    public String getSrcLat() {
        return srcLat;
    }

    public void setSrcLat(String srcLat) {
        this.srcLat = srcLat;
    }

    public String getSrcLatDms() {
        return srcLatDms;
    }

    public void setSrcLatDms(String srcLatDms) {
        this.srcLatDms = srcLatDms;
    }

    public String getSrcLon() {
        return srcLon;
    }

    public void setSrcLon(String srcLon) {
        this.srcLon = srcLon;
    }

    public String getSrcLonDms() {
        return srcLonDms;
    }

    public void setSrcLonDms(String srcLonDms) {
        this.srcLonDms = srcLonDms;
    }

    public String getDestLat() {
        return destLat;
    }

    public void setDestLat(String destLat) {
        this.destLat = destLat;
    }

    public String getDestLatDms() {
        return destLatDms;
    }

    public void setDestLatDms(String destLatDms) {
        this.destLatDms = destLatDms;
    }

    public String getDestLon() {
        return destLon;
    }

    public void setDestLon(String destLon) {
        this.destLon = destLon;
    }

    public String getDestLonDms() {
        return destLonDms;
    }

    public void setDestLonDms(String destLonDms) {
        this.destLonDms = destLonDms;
    }

    public String getSigLat() {
        return sigLat;
    }

    public void setSigLat(String sigLat) {
        this.sigLat = sigLat;
    }

    public String getSigLon() {
        return sigLon;
    }

    public void setSigLon(String sigLon) {
        this.sigLon = sigLon;
    }

    public String getSrcEht() {
        return srcEht;
    }

    public void setSrcEht(String srcEht) {
        this.srcEht = srcEht;
    }

    public String getDestEht() {
        return destEht;
    }

    public void setDestEht(String destEht) {
        this.destEht = destEht;
    }

    public String getSigEht() {
        return sigEht;
    }

    public void setSigEht(String sigEht) {
        this.sigEht = sigEht;
    }

    public String getSpcZone() {
        return spcZone;
    }

    public void setSpcZone(String spcZone) {
        this.spcZone = spcZone;
    }

    public String getSpcNorthing_m() {
        return spcNorthing_m;
    }

    public void setSpcNorthing_m(String spcNorthing_m) {
        this.spcNorthing_m = spcNorthing_m;
    }

    public String getSpcEasting_m() {
        return spcEasting_m;
    }

    public void setSpcEasting_m(String spcEasting_m) {
        this.spcEasting_m = spcEasting_m;
    }

    public String getSpcNorthing_usft() {
        return spcNorthing_usft;
    }

    public void setSpcNorthing_usft(String spcNorthing_usft) {
        this.spcNorthing_usft = spcNorthing_usft;
    }

    public String getSpcEasting_usft() {
        return spcEasting_usft;
    }

    public void setSpcEasting_usft(String spcEasting_usft) {
        this.spcEasting_usft = spcEasting_usft;
    }

    public String getSpcNorthing_ift() {
        return spcNorthing_ift;
    }

    public void setSpcNorthing_ift(String spcNorthing_ift) {
        this.spcNorthing_ift = spcNorthing_ift;
    }

    public String getSpcEasting_ift() {
        return spcEasting_ift;
    }

    public void setSpcEasting_ift(String spcEasting_ift) {
        this.spcEasting_ift = spcEasting_ift;
    }

    public String getSpcScaleFactor() {
        return spcScaleFactor;
    }

    public void setSpcScaleFactor(String spcScaleFactor) {
        this.spcScaleFactor = spcScaleFactor;
    }

    public String getSpcCombinedFactor() {
        return spcCombinedFactor;
    }

    public void setSpcCombinedFactor(String spcCombinedFactor) {
        this.spcCombinedFactor = spcCombinedFactor;
    }

    public String getSpcConvergence() {
        return spcConvergence;
    }

    public void setSpcConvergence(String spcConvergence) {
        this.spcConvergence = spcConvergence;
    }

    public String getUtmZone() {
        return utmZone;
    }

    public void setUtmZone(String utmZone) {
        this.utmZone = utmZone;
    }

    public String getUtmConvergence() {
        return utmConvergence;
    }

    public void setUtmConvergence(String utmConvergence) {
        this.utmConvergence = utmConvergence;
    }

    public String getUtmNorthing() {
        return utmNorthing;
    }

    public void setUtmNorthing(String utmNorthing) {
        this.utmNorthing = utmNorthing;
    }

    public String getUtmEasting() {
        return utmEasting;
    }

    public void setUtmEasting(String utmEasting) {
        this.utmEasting = utmEasting;
    }

    public String getUtmScaleFactor() {
        return utmScaleFactor;
    }

    public void setUtmScaleFactor(String utmScaleFactor) {
        this.utmScaleFactor = utmScaleFactor;
    }

    public String getUtmCombinedFactor() {
        return utmCombinedFactor;
    }

    public void setUtmCombinedFactor(String utmCombinedFactor) {
        this.utmCombinedFactor = utmCombinedFactor;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }



    public String getUsng() {
        return usng;
    }

    public void setUsng(String usng) {
        this.usng = usng;
    }

    public double getMet2Usft() {
        return MET2USFT;
    }

    public double getMet2Ift() {
        return MET2IFT;
    }

    public String getSrcVertDatum() {
        return srcVertDatum;
    }

    public void setSrcVertDatum(String srcVertDatum) {
        this.srcVertDatum = srcVertDatum;
    }

    public String getDestVertDatum() {
        return destVertDatum;
    }

    public void setDestVertDatum(String destVertDatum) {
        this.destVertDatum = destVertDatum;
    }

    public String getSrcOrthoht() {
        return srcOrthoht;
    }

    public void setSrcOrthoht(String srcOrthoht) {
        this.srcOrthoht = srcOrthoht;
    }

    public String getDestOrthoht() {
        return destOrthoht;
    }

    public void setDestOrthoht(String destOrthoht) {
        this.destOrthoht = destOrthoht;
    }

    public String getSigOrthoht() {
        return sigOrthoht;
    }

    public void setSigOrthoht(String sigOrthoht) {
        this.sigOrthoht = sigOrthoht;
    }

    public String getDeltaLat() {
        return deltaLat;
    }

    public void setDeltaLat(String deltaLat) {
        this.deltaLat = deltaLat;
    }

    public String getSigLat_m() {
        return sigLat_m;
    }

    public void setSigLat_m(String sigLat_m) {
        this.sigLat_m = sigLat_m;
    }

    public String getDeltaLon() {
        return deltaLon;
    }

    public void setDeltaLon(String deltaLon) {
        this.deltaLon = deltaLon;
    }

    public String getSigLon_m() {
        return sigLon_m;
    }

    public void setSigLon_m(String sigLon_m) {
        this.sigLon_m = sigLon_m;
    }
    
}
