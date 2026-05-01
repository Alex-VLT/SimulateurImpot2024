package com.kerware.simulateurreusine;

/**
 * Parameters and tax scale used by the refactored simulator.
 * Centralizes magic numbers to facilitate readability and
 * parameterization (e.g., transition to 2025).
 */
public final class ParametresImpot {

    /**
     * Private constructor to prevent instantiation.
     */
    private ParametresImpot() {
    }

    /**
     * Tax brackets limits for the progressive tax scale.
     */
    public static final int[] BAREME_LIMITES = new int[] {
            0,
            11294,
            28797,
            82341,
            177106,
            Integer.MAX_VALUE
    };

    /**
     * Tax rates for each bracket of the progressive scale.
     */
    public static final double[] BAREME_TAUX = new double[] {
            0.0,
            0.11,
            0.3,
            0.41,
            0.45
    };

    /**
     * Maximum allowance amount.
     */
    public static final int ABATTEMENT_MAX = 14171;
    /**
     * Minimum allowance amount.
     */
    public static final int ABATTEMENT_MIN = 495;
    /**
     * Allowance rate (10% of income).
     */
    public static final double TAUX_ABATTEMENT = 0.1;

    /**
     * Ceiling amount for half-share benefit.
     */
    public static final double PLAFOND_DEMI_PART = 1759.0;

    /**
     * Relief threshold for single filer.
     */
    public static final double SEUIL_DECOTE_DECLARANT_SEUL = 1929.0;
    /**
     * Relief threshold for couple.
     */
    public static final double SEUIL_DECOTE_DECLARANT_COUPLE = 3191.0;

    /**
     * Maximum relief for single filer.
     */
    public static final double DECOTE_MAX_DECLARANT_SEUL = 873.0;
    /**
     * Maximum relief for couple.
     */
    public static final double DECOTE_MAX_DECLARANT_COUPLE = 1444.0;
    /**
     * Relief calculation rate.
     */
    public static final double TAUX_DECOTE = 0.4525;
}

