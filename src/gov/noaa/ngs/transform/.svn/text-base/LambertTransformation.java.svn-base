package gov.noaa.ngs.transform;

import java.util.HashMap;
import java.util.Properties;

/**
 * Transforms geodetic coordinates to spc and vice versa using Lambert Conic
 * projection
 *
 * @author Krishna Tadepalli
 * @version 1.0 Date: 06/10/2011
 */
public class LambertTransformation extends CoordinateTransformation {

    private double falseEasting;
    private double falseNorthing;
    private double cm;
    private double sinfo;
    private double mapRad;
    private double mapRadOrig;
    private double sfRadius;
    private static final double MI_NAD27_RADIUS = 6378450.04748448;
    private static final String[] MI_NAD27_ZONES = {"2111", "2112", "2113"};
    private static final double RHOSEC = 2.062648062471E05;
    private static final double[] NAD27_SPC_LC_CONSTANTS = {
        6.3360E5, 15.89395036E6, 16.56462877E6, .9998480641E0, .7969223940E0,
        1.8970787068E5, 3.79919E0, 5.91550E0,
        3.3120E5, 29.27759361E6, 29.73288287E6, .9999359370E0, .5818991407E0,
        1.2760635656E5, 3.81452E0, 3.26432E0,
        3.3120E5, 31.01403923E6, 31.51172420E6, .9999184698E0, .5596906871E0,
        1.2203694711E5, 3.81550E0, 3.08256E0,
        4.3920E5, 24.24535805E6, 24.79243623E6, .9998946358E0, .6538843192E0,
        1.4648675847E5, 3.80992E0, 3.93575E0,
        4.3920E5, 25.79585031E6, 26.31225765E6, .9999146793E0, .6304679732E0,
        1.4019081964E5, 3.81147E0, 3.70114E0,
        4.3380E5, 27.05747585E6, 27.51299204E6, .9999291792E0, .6122320427E0,
        1.3539552018E5, 3.81265E0, 3.52998E0,
        4.2840E5, 28.18240533E6, 28.65293196E6, .9999407628E0, .5965871443E0,
        1.3135035494E5, 3.81362E0, 3.39020E0,
        4.2480E5, 30.19414554E6, 30.64942427E6, .9999221277E0, .5700119219E0,
        1.2461210305E5, 3.81523E0, 3.16593E0,
        4.1850E5, 31.84657092E6, 32.27126772E6, .9999541438E0, .5495175982E0,
        1.1952016335E5, 3.81642E0, 3.00292E0,
        4.2600E5, 30.89138210E6, 35.05539631E6, .9999885350E0, .5612432071E0,
        1.2242288096E5, 3.81572E0, 3.09520E0,
        3.7980E5, 24.75189768E6, 25.08606820E6, .9999568475E0, .6461334829E0,
        1.4438462308E5, 3.81044E0, 3.85610E0,
        3.7980E5, 25.78137691E6, 26.24305274E6, .9999359117E0, .6306895773E0,
        1.4024965162E5, 3.81146E0, 3.70326E0,
        3.7980E5, 26.97713389E6, 27.40223182E6, .9999453995E0, .6133780528E0,
        1.3569426662E5, 3.81257E0, 3.54046E0,
        2.6190E5, 23.65923356E6, 23.91438902E6, .9999831405E0, .6630594147E0,
        1.4899967980E5, 3.80929E0, 4.03278E0,
        3.0420E5, 36.03044305E6, 36.45492453E6, .9999484343E0, .5025259000E0,
        1.0814611701E5, 3.81898E0, 2.65643E0,
        3.3660E5, 22.73695034E6, 23.16246159E6, .9999453686E0, .6777445518E0,
        1.5308002265E5, 3.80827E0, 4.19479E0,
        3.3660E5, 23.93658511E6, 24.37409667E6, .9999483705E0, .6587010213E0,
        1.4780259905E5, 3.80959E0, 3.98630E0,
        3.5280E5, 25.64495912E6, 25.97906857E6, .9999568556E0, .6327148646E0,
        1.4078797215E5, 3.81133E0, 3.72376E0,
        3.5460E5, 26.89602448E6, 27.35152150E6, .9999359200E0, .6145281068E0,
        1.3599441020E5, 3.81250E0, 3.55102E0,
        3.0330E5, 26.37182068E6, 26.72405182E6, .9999620817E0, .6220672671E0,
        1.3797063364E5, 3.81202E0, 3.62113E0,
        3.0870E5, 27.46786075E6, 27.83223564E6, .9999453808E0, .6064623718E0,
        1.3389657874E5, 3.81301E0, 3.47771E0,
        3.3300E5, 33.62456836E6, 34.07962933E6, .9999147417E0, .5287006734E0,
        1.1443268515E5, 3.81758E0, 2.84511E0,
        3.2880E5, 36.27138935E6, 36.75655345E6, .9999257458E0, .5000126971E0,
        1.0754855026E5, 3.81911E0, 2.63885E0,
        3.2880E5, 41.09174954E6, 41.57676239E6, .9998947956E0, .4540068519E0,
        .9677930342E5, 3.82138E0, 2.27436E0,
        2.7720E5, 25.98947499E6, 26.36911276E6, .9999498485E0, .6276341196E0,
        1.3943969369E5, 3.81166E0, 3.67392E0,
        2.5740E5, 23.11197514E6, 23.54947732E6, .9999645506E0, .6717286561E0,
        1.5139953138E5, 3.80870E0, 4.12738E0,
        2.5380E5, 23.78467844E6, 23.92439802E6, .9999984844E0, .6610953994E0,
        1.4845947463E5, 3.80943E0, 4.01174E0,
        3.1320E5, 20.04171618E6, 20.58942009E6, .9999410344E0, .7227899381E0,
        1.6610225085E5, 3.80501E0, 4.68430E0,
        3.0360E5, 21.00171522E6, 21.59476840E6, .9999509058E0, .7064074100E0,
        1.6127076661E5, 3.80622E0, 4.46875E0,
        3.0360E5, 22.56484851E6, 23.06959722E6, .9999450783E0, .6805292633E0,
        1.5386223938E5, 3.80808E0, 4.15706E0,
        3.3516E5, 18.98431962E6, 19.47139875E6, .9999028166E0, .7412196637E0,
        1.7168463011E5, 3.80362E0, 5.01609E0,
        3.3930E5, 20.00667972E6, 20.49345715E6, .9999220223E0, .7233880702E0,
        1.6628089747E5, 3.80497E0, 4.76197E0,
        3.3840E5, 21.32700606E6, 21.87434914E6, .9999220448E0, .7009277824E0,
        1.5968012517E5, 3.80662E0, 4.46959E0,
        3.9420E5, 18.68949840E6, 19.15787426E6, .9999714855E0, .7464518080E0,
        1.7330021285E5, 3.80322E0, 5.09490E0,
        3.9420E5, 19.43293976E6, 19.91980636E6, .9999220151E0, .7333538278E0,
        1.6928196779E5, 3.80422E0, 4.90135E0,
        3.9420E5, 20.50065051E6, 21.09682093E6, .9999107701E0, .7149012442E0,
        1.6376115820E5, 3.80560E0, 4.64814E0,
        3.6000E5, 23.00434629E6, 23.36897746E6, .9999645501E0, .6734507906E0,
        1.5187930504E5, 3.80858E0, 4.14653E0,
        3.5820E5, 24.10456106E6, 24.59078186E6, .9999220725E0, .6560764003E0,
        1.4708468139E5, 3.80977E0, 3.95865E0,
        2.6640E5, 24.23500080E6, 24.46254530E6, .9999949000E0, .6540820950E0,
        1.4654064240E5, 3.80990E0, 3.93780E0,
        2.8440E5, 29.63705947E6, 30.18361125E6, .9998725510E0, .5771707700E0,
        1.2641160353E5, 3.81480E0, 3.22483E0,
        3.6180E5, 18.81984905E6, 19.21551601E6, .9999358426E0, .7441333961E0,
        1.7258257950E5, 3.80339E0, 5.05972E0,
        3.6180E5, 19.66102779E6, 20.08697718E6, .9999358523E0, .7293826040E0,
        1.6808045445E5, 3.80452E0, 4.84504E0,
        2.9700E5, 24.04873851E6, 24.55915847E6, .9999391411E0, .6569503193E0,
        1.4732348125E5, 3.80971E0, 3.96783E0,
        2.9700E5, 25.52287581E6, 26.02707112E6, .9999359346E0, .6345195439E0,
        1.4126863705E5, 3.81121E0, 3.74048E0,
        3.5280E5, 28.65787166E6, 29.08283170E6, .9999454101E0, .5901470744E0,
        1.2970256887E5, 3.81402E0, 3.33440E0,
        3.5280E5, 30.38283106E6, 30.83803296E6, .9999359432E0, .5676166827E0,
        1.2401248935E5, 3.81537E0, 3.14645E0,
        4.3380E5, 20.83625094E6, 21.38385248E6, .9998945810E0, .7091860222E0,
        1.6208208858E5, 3.80602E0, 4.57382E0,
        4.3380E5, 22.34130943E6, 22.88866715E6, .9998946058E0, .6841473833E0,
        1.5488274104E5, 3.80782E0, 4.26823E0,
        2.7990E5, 23.75535127E6, 24.21105037E6, .9999568410E0, .6615397363E0,
        1.4858157953E5, 3.80940E0, 4.01753E0,
        2.7990E5, 24.57780067E6, 24.98482643E6, .9999595012E0, .6487931668E0,
        1.4510387979E5, 3.81026E0, 3.88319E0,
        2.9160E5, 30.63012553E6, 31.12772475E6, .9999454207E0, .5644973800E0,
        1.2323344099E5, 3.81555E0, 3.12127E0,
        2.9160E5, 32.25212630E6, 32.67688765E6, .9999326284E0, .5446515700E0,
        1.1832357839E5, 3.81669E0, 2.94381E0,
        3.6000E5, 20.92270409E6, 21.36669703E6, .9999391116E0, .7077381841E0,
        1.6165893392E5, 3.80612E0, 4.55529E0,
        3.6120E5, 21.99357561E6, 22.46193705E6, .9999068931E0, .6898519579E0,
        1.5650154370E5, 3.80742E0, 4.33519E0,
        3.0960E5, 29.01023109E6, 29.53514991E6, .9999484030E0, .5854397296E0,
        1.2850428313E5, 3.81431E0, 3.29422E0,
        3.6540E5, 29.45690729E6, 29.97295994E6, .9999108771E0, .5795358654E0,
        1.2700858548E5, 3.81466E0, 3.24452E0,
        3.5100E5, 32.18780958E6, 32.69165454E6, .9998726224E0, .5453944146E0,
        1.1850595074E5, 3.81665E0, 2.97107E0,
        3.6120E5, 34.85170346E6, 35.33712123E6, .9998817443E0, .5150588857E0,
        1.1114162181E5, 3.81832E0, 2.74550E0,
        3.5640E5, 37.26150920E6, 37.80744038E6, .9998632433E0, .4899126408E0,
        1.0515719059E5, 3.81962E0, 2.56899E0,
        3.5460E5, 41.09174954E6, 41.57676239E6, .9998947956E0, .4540068519E0,
        .9677930342E5, 3.82138E0, 2.33094E0,
        4.0140E5, 23.89487245E6, 24.22911029E6, .9999568422E0, .6593554910E0,
        1.4798196231E5, 3.80955E0, 3.99323E0,
        4.0140E5, 25.11717675E6, 25.66411442E6, .9998988207E0, .6405785926E0,
        1.4288930066E5, 3.81081E0, 3.80024E0,
        4.0140E5, 27.02595535E6, 27.43281288E6, .9999512939E0, .6126873424E0,
        1.3551416878E5, 3.81262E0, 3.53414E0,
        2.8260E5, 26.23020009E6, 26.57644445E6, .9999483859E0, .6241178597E0,
        1.3851078682E5, 3.81189E0, 3.64047E0,
        2.8260E5, 27.43480006E6, 27.81131271E6, .9999454027E0, .6069248249E0,
        1.3401641072E5, 3.81298E0, 3.48187E0,
        4.3500E5, 18.79808167E6, 19.20586343E6, .9999422551E0, .7445203390E0,
        1.7270215711E5, 3.80336E0, 5.06556E0,
        4.3380E5, 19.83265352E6, 20.28911960E6, .9999145875E0, .7263957947E0,
        1.6718172121E5, 3.80474E0, 4.80336E0,
        2.8620E5, 25.30502912E6, 25.71512655E6, .9999407460E0, .6377729696E0,
        1.4213752979E5, 3.81099E0, 3.77244E0,
        2.9160E5, 26.63932345E6, 27.07062078E6, .9999256928E0, .6181953936E0,
        1.3695382207E5, 3.81227E0, 3.58491E0,
        3.2400E5, 20.12413305E6, 20.48917967E6, .9999453461E0, .7213707913E0,
        1.6567904034E5, 3.80511E0, 4.73451E0,
        3.2400E5, 21.05074699E6, 21.43091391E6, .9999407059E0, .7055766312E0,
        1.6102881363E5, 3.80628E0, 4.52782E0,
        3.2400E5, 22.16143225E6, 22.67213466E6, .9999325474E0, .6871032423E0,
        1.5572001691E5, 3.80761E0, 4.30274E0,
        2.3916E5, 63.54222166E6, 63.68747944E6, .9999939449E0, .3128882281E0,
        .6532844933E5, 3.82699E0, 1.51030E0,
        2.3916E5, 63.54222166E6, 63.78747944E6, .9999939449E0, .3128882281E0,
        .6532844933E5, 3.82699E0, 1.51030E0,
        6.1200E5, -82.31223465E6, -82.0E6, .9999999999E0, -.2464352205E0,
        -.5110953291E5, 3.82892E0, -1.16664E0

    };

    /**
     * Constructor lazily instantiated via a parent's factory method
     *
     * @param zoneDef zone definitions
     * @param zoneConstants zone constants
     */
    public LambertTransformation(Properties zoneDef, Properties zoneConstants) {
        super(zoneDef, zoneConstants);
    }

    @Override
    public String toProjectedCoordinates(double lat, double lon) {
        lon = CoordinateTransformation.resetLon(lon);
        setConstants();
        if (datum.equals("nad27")) {   //nad27 SPC conversion
            return toProjectedCoordinates(lat, lon, zone);
        }
        String spcStr = datum + "," + zone + "," + label;
        lat *= degreesToRadians;
        lon *= degreesToRadians;
        double sinlat = Math.sin(lat);
        double coslat = Math.cos(lat);
        double convergence = getLonDiff(lon, cm) * sinfo;
        double q = (Math.log((1 + sinlat) / (1 - sinlat)) - e * Math.log((1 + e * sinlat) / (1 - e * sinlat))) / 2.0;
        double rpt = mapRad / Math.exp(sinfo * q);
        double north = falseNorthing + (mapRadOrig - rpt * Math.cos(convergence)) / unitsCf;
        double east = falseEasting + (rpt * Math.sin(convergence)) / unitsCf;
        double wp = Math.sqrt(1.0 - esq * Math.pow(sinlat, 2.0));
        double scaleFactor = wp * sinfo * rpt / (sfRadius * coslat);
        spcStr += "," + String.format(northFormat, north);
        spcStr += "," + String.format(eastFormat, east);
        spcStr += "," + String.format(convFormat, convergence / degreesToRadians);
        spcStr += "," + String.format(sfFormat, scaleFactor);
        return spcStr;
    }

    private String toProjectedCoordinates(double lat, double lon, String zone) {
        String spcStr = datum + "," + zone + "," + label;
        lon = Math.abs(lon);
        double latrad = lat * degreesToRadians;
        double lonrad = lon * degreesToRadians;
        int index = Integer.valueOf(zoneConstants.getProperty(zone + ".lcConstIndex", "0"));
        double l1 = Double.valueOf(zoneConstants.getProperty(zone + ".l1", "0.0"));
        double l11 = Double.valueOf(zoneConstants.getProperty(zone + ".l11", "0.0"));
        double c1 = Double.valueOf(zoneConstants.getProperty("lc.c1", "0.0"));
        double c2 = Double.valueOf(zoneConstants.getProperty("lc.c2", "0.0"));
        double c3 = Double.valueOf(zoneConstants.getProperty("lc.c3", "0.0"));
        double c4 = Double.valueOf(zoneConstants.getProperty("lc.c4", "0.0"));
        double c5 = Double.valueOf(zoneConstants.getProperty("lc.c5", "0.0"));

        int l = index * 8 - 1;
        double l2 = NAD27_SPC_LC_CONSTANTS[l - 7];
        double l3 = NAD27_SPC_LC_CONSTANTS[l - 6];
        double l4 = NAD27_SPC_LC_CONSTANTS[l - 5];
        double l5 = NAD27_SPC_LC_CONSTANTS[l - 4];
        double l6 = NAD27_SPC_LC_CONSTANTS[l - 3];
        double l78 = NAD27_SPC_LC_CONSTANTS[l - 2];
        double l9 = NAD27_SPC_LC_CONSTANTS[l - 1];
        double l10 = NAD27_SPC_LC_CONSTANTS[l];
        double sinp = Math.sin(latrad);
        double cosp = Math.cos(latrad);
        double cosp2 = cosp * cosp;
        double ss = c1 * (l78 - latrad * RHOSEC + (c2 - (c3 - c4 * cosp2) * cosp2) * sinp * cosp);
        double s8 = ss * 1.0E-8;
        double s82 = s8 * s8;
        double r = l3 + ss * l5 * (1.E0 + s82 * (l9 - s8 * l10 + s82 * l11));
        double convergence = l6 * (l2 - lonrad * RHOSEC) / RHOSEC;
        double east = l1 + r * Math.sin(convergence);
        double north = l4 - r + 2.E0 * r * Math.pow(Math.sin(convergence * 0.5E0), 2);
        double scaleFactor = l6 * r * Math.sqrt(1.E0 - esq * sinp * sinp) / c5 / cosp;
        if (zone.equals("2111") || zone.equals("2112") || zone.equals("2113")) {
            scaleFactor = scaleFactor / 1.0000382E0;
        }
        spcStr += "," + String.format(northFormat, north);
        spcStr += "," + String.format(eastFormat, east);
        spcStr += "," + String.format(convFormat, convergence / degreesToRadians);
        spcStr += "," + String.format(sfFormat, scaleFactor);

        return spcStr;
    }

    @Override
    public String toGeodeticCoordinates(double north, double east) {
        String geodeticStr = datum + "," + zone + "," + label;
        setConstants();
        if (datum.equals("nad27")) {   //nad27 SPC conversion
            return toGeodeticCoordinates(north, east, zone);
        }
        double npr = mapRadOrig - (north - falseNorthing) * unitsCf;
        double epr = (east - falseEasting) * unitsCf;
        double gam = Math.atan(epr / npr);
        double lon = getLonDiff(cm + (gam / sinfo), 0.0);
        double rpt = Math.sqrt(npr * npr + epr * epr);
        double q = Math.log(mapRad / rpt) / sinfo;
        double tmp = Math.exp(2 * q);
        double sine = (tmp - 1.0) / (tmp + 1);
//        System.out.println("sine: " + sine);
        for (int i = 0; i < 3; i++) {
            double f1 = (Math.log((1.0 + sine) / (1.0 - sine))
                    - e * Math.log((1.0 + e * sine) / (1.0 - e * sine))) / 2.0 - q;
            double f2 = 1.0 / (1.0 - sine * sine) - esq / (1.0 - esq * sine * sine);
            sine -= f1 / f2;
        }
        double lat = Math.asin(sine);
        double sinlat = Math.sin(lat);
        double coslat = Math.cos(lat);
        double convergence = getLonDiff(lon, cm) * sinfo;
        q = (Math.log((1 + sinlat) / (1 - sinlat)) - e * Math.log((1 + e * sinlat) / (1 - e * sinlat))) / 2.0;
        rpt = mapRad / Math.exp(sinfo * q);
        double wp = Math.sqrt(1 - esq * sinlat * sinlat);
        double scaleFactor = wp * sinfo * rpt / (sfRadius * coslat);
        geodeticStr += "," + String.format(latFormat, lat / degreesToRadians);
        geodeticStr += "," + String.format(lonFormat, CoordinateTransformation.resetLon(lon / degreesToRadians));
        geodeticStr += "," + String.format(convFormat, convergence / degreesToRadians);
        geodeticStr += "," + String.format(sfFormat, scaleFactor);

        return geodeticStr;
    }

    private String toGeodeticCoordinates(double north, double east, String zone) {
        String geodeticStr = datum + "," + zone + "," + label;
        setConstants();
        int index = Integer.valueOf(zoneConstants.getProperty(zone + ".lcConstIndex", "0"));
        double l1 = Double.valueOf(zoneConstants.getProperty(zone + ".l1", "0.0"));
        double l11 = Double.valueOf(zoneConstants.getProperty(zone + ".l11", "0.0"));
        int l = index * 8 - 1;
        double l2 = NAD27_SPC_LC_CONSTANTS[l - 7];
        double l3 = NAD27_SPC_LC_CONSTANTS[l - 6];
        double l4 = NAD27_SPC_LC_CONSTANTS[l - 5];
        double l5 = NAD27_SPC_LC_CONSTANTS[l - 4];
        double l6 = NAD27_SPC_LC_CONSTANTS[l - 3];
        double l78 = NAD27_SPC_LC_CONSTANTS[l - 2];
        double l9 = NAD27_SPC_LC_CONSTANTS[l - 1];
        double l10 = NAD27_SPC_LC_CONSTANTS[l];

        double d1 = Double.valueOf(zoneConstants.getProperty("lc.d1", "0.0"));
        double d2 = Double.valueOf(zoneConstants.getProperty("lc.d2", "0.0"));
        double d3 = Double.valueOf(zoneConstants.getProperty("lc.d3", "0.0"));
        double d4 = Double.valueOf(zoneConstants.getProperty("lc.d4", "0.0"));
        double[] s = new double[4];
        double theta = Math.atan((east - l1) / (l4 - north));
        double thsec = theta * RHOSEC;
        double esec = l2 - thsec / l6;
        double lon = esec / 3600.0;
        double costh = Math.cos(theta);
        double shlft = Math.pow((Math.sin(theta / 2.E0)), 2);
        double r = (l4 - north) / costh;
        double s1 = (l4 - l3 - north + 2.E0 * r * shlft) / l5;
        s[0] = s1;
        for (int i = 0; i < 3; i++) {
            double far = s[i] * 1.0E-8;
            double far2 = far * far;
            s[i + 1] = s1 / (1.E0 + far2 * (l9 - far * l10 + far2 * l11));
        }
        double omsec = l78 - d1 * s[3];
        double omega = omsec / RHOSEC;
        double sino = Math.sin(omega);
        double coso = Math.cos(omega);
        double cos2o = coso * coso;
        double lat = (omsec + (d2 + (d3 + d4 * cos2o) * cos2o) * sino * coso) / 3600.0;
        double convergence = l6 * (l2 - lon * degreesToRadians * RHOSEC) / RHOSEC;
        double sinp = Math.sin(lat * degreesToRadians);
        double cosp = Math.cos(lat * degreesToRadians);
        double c5 = Double.valueOf(zoneConstants.getProperty("lc.c5", "0.0"));
        double scaleFactor = l6 * r * Math.sqrt(1.E0 - esq * sinp * sinp) / c5 / cosp;
        if (zone.equals("2111") || zone.equals("2112") || zone.equals("2113")) {
            scaleFactor = scaleFactor / 1.0000382E0;
        }
        geodeticStr += "," + String.format(latFormat, lat);
        geodeticStr += "," + String.format(lonFormat, CoordinateTransformation.resetLon(-lon));
        geodeticStr += "," + String.format(convFormat, convergence / degreesToRadians);
        geodeticStr += "," + String.format(sfFormat, scaleFactor);

        return geodeticStr;
    }

    @Override
    protected void setConstants() {
        falseEasting = Double.parseDouble(zoneDef.getProperty(zone + ".fe", "0.0"));
        falseNorthing = Double.parseDouble(zoneDef.getProperty(zone + ".fn", "0.0"));
        cm = Double.parseDouble(zoneDef.getProperty(zone + ".cm", "0.0")) * degreesToRadians;
        datum = zoneDef.getProperty(zone + ".datum", datum);
        sfRadius = radius;
        String[] lcConst = zoneConstants.getProperty(zone + ".const", "0.0").split(",");
        if (lcConst.length > 1) {
            sinfo = Double.parseDouble(lcConst[0]);
            mapRad = Double.parseDouble(lcConst[1]);
            mapRadOrig = Double.parseDouble(lcConst[2]);
        }
        if (isMichiganNad27Zone()) {
            mapRad *= MI_NAD27_RADIUS / radius;
            mapRadOrig *= MI_NAD27_RADIUS / radius;
            sfRadius = MI_NAD27_RADIUS;
        }

    }

    private boolean isMichiganNad27Zone() {
        return (datum.equals("nad27") && (zone.equals(MI_NAD27_ZONES[0])
                || zone.equals(MI_NAD27_ZONES[1]) || zone.equals(MI_NAD27_ZONES[2])));
    }

    @Override
    public String toGeodeticCoordinates(double x, double y, double z) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String toGeodeticCoordinates(double north, double east, boolean sh) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String toProjectedCoordinates(double lat, double lon, double height) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String toProjectedCoordinates(double radius, double f, double lat, double lon) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String toProjectedCoordinates(double lat, double lon, double height, double radius, double eflattening) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String toGeodeticCoordinates(double x, double y, double z, double radius, double eflattening) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String toGeodeticCoordinates(double radius, double fl, double north, double east, boolean southernHemisphere) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public static void main(String[] args) {
        CoordinateTransformation c = CoordinateTransformation.getInstance("1702", "nad27");
        System.out.println(c.toProjectedCoordinates(31.0000000000, -91.5000000000));
//        System.out.println(c.toGeodeticCoordinates(1093083.261420, 171660.728526));
//        c = CoordinateTransformation.getInstance("5002", "nad27");
//        System.out.println(c.toProjectedCoordinates(64.0, -143.0));
   //     System.out.println(c.toGeodeticCoordinates(808743.720518, 2124768.906005));

    }

}
