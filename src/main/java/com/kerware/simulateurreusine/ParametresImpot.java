package com.kerware.simulateurreusine;

/**
 * Paramètres et barème utilisés par la version réusinée du simulateur.
 * Centralise les nombres "magiques" pour faciliter la lecture et la
 * paramétrisation (p.ex. passage à 2025).
 */
public final class ParametresImpot {

    private ParametresImpot() { }

    public static final int[] LIMITES = new int[] {
            0,
            11294,
            28797,
            82341,
            177106,
            Integer.MAX_VALUE
    };

    public static final double[] TAUX = new double[] {
            0.0,
            0.11,
            0.3,
            0.41,
            0.45
    };

    public static final int ABT_MAX = 14171;
    public static final int ABT_MIN = 495;
    public static final double ABT_RATE = 0.1;

    public static final double PLAFOND_DEMI_PART = 1759.0;

    public static final double SEUIL_DECOTE_DECLARANT_SEUL = 1929.0;
    public static final double SEUIL_DECOTE_DECLARANT_COUPLE = 3191.0;

    public static final double DECOTE_MAX_DECLARANT_SEUL = 873.0;
    public static final double DECOTE_MAX_DECLARANT_COUPLE = 1444.0;
    public static final double TAUX_DECOTE = 0.4525;
}
