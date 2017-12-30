package de.ueberdosis.mp3info.id3v2;

//import java.util.HashSet;

import java.util.Hashtable;

/**
 * Contains a list of all available (ISO 639-2) languages and a helper method
 */
public class Languages {
    //private static HashSet languages = null;
    private static Hashtable languages = null;

    /**
     * Checks if a language is defined
     *
     * @param lang 3 characters supposed to denote a language
     * @return true if the language was found
     */
    public static boolean checkLanguage(String lang) {
        String mLang = lang.toUpperCase();
        //if (languages == null) initHashSet ();
        if (languages == null) initHashtable();
        return (languages.contains(mLang));
    }

    //private static void initHashSet () {
    //  languages = new HashSet (500);
    //  for (int i=0; i<ISO_LANGUAGES.length;i++)
    //      languages.add (ISO_LANGUAGES[i]);
    //}

    private static void initHashtable() {
        languages = new Hashtable(500);
        for(int i = 0; i < ISO_LANGUAGES.length; i++)
            languages.put(ISO_LANGUAGES[i], "");
    }

    public static final String[] ISO_LANGUAGES = {
            "AAR", "ABK", "ACE", "ACH", "ADA", "AFA", "AFH", "AFR", "AKA", "AKK", "ALB",
            "ALE", "ALG", "AMH", "ANG", "APA", "ARA", "ARC", "ARM", "ARN", "ARP", "ART",
            "ARW", "ASM", "ATH", "AVA", "AVE", "AWA", "AYM", "AZE", "BAD", "BAI", "BAK",
            "BAL", "BAM", "BAN", "BAQ", "BAS", "BAT", "BEJ", "BEL", "BEM", "BEN", "BER",
            "BHO", "BIH", "BIK", "BIN", "BIS", "BLA", "BNT", "BOD", "BRA", "BRE", "BUA",
            "BUG", "BUL", "BUR", "CAD", "CAI", "CAR", "CAT", "CAU", "CEB", "CEL", "CES",
            "CHA", "CHB", "CHE", "CHG", "CHI", "CHM", "CHN", "CHO", "CHR", "CHU", "CHV",
            "CHY", "COP", "COR", "COS", "CPE", "CPF", "CPP", "CRE", "CRP", "CUS", "CYM",
            "CZE", "DAK", "DAN", "DEL", "DEU", "DIN", "DIV", "DOI", "DRA", "DUA", "DUM",
            "DUT", "DYU", "DZO", "EFI", "EGY", "EKA", "ELL", "ELX", "ENG", "ENM", "EPO",
            "ESK", "ESL", "EST", "EUS", "EWE", "EWO", "FAN", "FAO", "FAS", "FAT", "FIJ",
            "FIN", "FIU", "FON", "FRA", "FRE", "FRM", "FRO", "FRY", "FUL", "GAA", "GAE",
            "GAI", "GAY", "GDH", "GEM", "GEO", "GER", "GEZ", "GIL", "GLG", "GMH", "GOH",
            "GON", "GOT", "GRB", "GRC", "GRE", "GRN", "GUJ", "HAI", "HAU", "HAW", "HEB",
            "HER", "HIL", "HIM", "HIN", "HMO", "HUN", "HUP", "HYE", "IBA", "IBO", "ICE",
            "IJO", "IKU", "ILO", "INA", "INC", "IND", "INE", "INE", "IPK", "IRA", "IRI",
            "IRO", "ISL", "ITA", "JAV", "JAW", "JPN", "JPR", "JRB", "KAA", "KAB", "KAC",
            "KAL", "KAM", "KAN", "KAR", "KAS", "KAT", "KAU", "KAW", "KAZ", "KHA", "KHI",
            "KHM", "KHO", "KIK", "KIN", "KIR", "KOK", "KOM", "KON", "KOR", "KPE", "KRO",
            "KRU", "KUA", "KUM", "KUR", "KUS", "KUT", "LAD", "LAH", "LAM", "LAO", "LAT",
            "LAV", "LEZ", "LIN", "LIT", "LOL", "LOZ", "LTZ", "LUB", "LUG", "LUI", "LUN",
            "LUO", "MAC", "MAD", "MAG", "MAH", "MAI", "MAK", "MAK", "MAL", "MAN", "MAO",
            "MAP", "MAR", "MAS", "MAX", "MAY", "MEN", "MGA", "MIC", "MIN", "MIS", "MKH",
            "MLG", "MLT", "MNI", "MNO", "MOH", "MOL", "MON", "MOS", "MRI", "MSA", "MUL",
            "MUN", "MUS", "MWR", "MYA", "MYN", "NAH", "NAI", "NAU", "NAV", "NBL", "NDE",
            "NDO", "NEP", "NEW", "NIC", "NIU", "NLA", "NNO", "NON", "NOR", "NSO", "NUB",
            "NYA", "NYM", "NYN", "NYO", "NZI", "OCI", "OJI", "ORI", "ORM", "OSA", "OSS",
            "OTA", "OTO", "PAA", "PAG", "PAL", "PAM", "PAN", "PAP", "PAU", "PEO", "PER",
            "PHN", "PLI", "POL", "PON", "POR", "PRA", "PRO", "PUS", "QUE", "RAJ", "RAR",
            "ROA", "ROH", "ROM", "RON", "RUM", "RUN", "RUS", "SAD", "SAG", "SAH", "SAI",
            "SAL", "SAM", "SAN", "SCO", "SCR", "SEL", "SEM", "SGA", "SHN", "SID", "SIN",
            "SIO", "SIT", "SLA", "SLK", "SLO", "SLV", "SMI", "SMO", "SNA", "SND", "SOG",
            "SOM", "SON", "SOT", "SPA", "SQI", "SRD", "SRR", "SSA", "SSW", "SSW", "SUK",
            "SUN", "SUS", "SUX", "SVE", "SWA", "SWE", "SYR", "TAH", "TAM", "TAT", "TEL",
            "TEM", "TER", "TGK", "TGL", "THA", "TIB", "TIG", "TIR", "TIV", "TLI", "TMH",
            "TOG", "TON", "TRU", "TSI", "TSN", "TSO", "TUK", "TUM", "TUR", "TUT", "TWI",
            "TYV", "UGA", "UIG", "UKR", "UMB", "UND", "URD", "UZB", "VAI", "VEN", "VIE",
            "VOL", "VOT", "WAK", "WAL", "WAR", "WAS", "WEL", "WEN", "WOL", "XHO", "YAO",
            "YAP", "YID", "YOR", "ZAP", "ZEN", "ZHA", "ZHO", "ZUL", "ZUN"};
}
