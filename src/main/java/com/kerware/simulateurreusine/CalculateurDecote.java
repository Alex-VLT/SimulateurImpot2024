package com.kerware.simulateurreusine;

/**
 * Calculator for tax relief (décote).
 * Applies relief to eligible taxpayers based on their characteristics and
 * income level.
 */@SuppressWarnings("hiding")final class CalculateurDecote implements Calculateur<Double> {

    /** The number of tax household shares of the declarant. */
    private final double partsDeclarant;
    /** The net tax before relief. */
    private final double impotNet;
    /** The relief threshold for single filer. */
    private final double seuilDecoteDeclarantSeul;
    /** The relief threshold for couple. */
    private final double seuilDecoteDeclarantCouple;
    /** The maximum relief for single filer. */
    private final double decoteMaxDeclarantSeul;
    /** The maximum relief for couple. */
    private final double decoteMaxDeclarantCouple;
    /** The rate of relief calculation. */
    private final double tauxDecote;

    /**
     * Constructs a CalculateurDecote with relief parameters.
     *
     * @param partsDeclarant the declarant's shares
     * @param impotNet the net tax amount
     * @param seuilDecoteDeclarantSeul the threshold for single filer
     * @param seuilDecoteDeclarantCouple the threshold for couple
     * @param decoteMaxDeclarantSeul the max relief for single filer
     * @param decoteMaxDeclarantCouple the max relief for couple
     * @param tauxDecote the relief rate
     */
    @SuppressWarnings("hiding")
    CalculateurDecote(final double partsDeclarant, final double impotNet,
            final double seuilDecoteDeclarantSeul,
            final double seuilDecoteDeclarantCouple,
            final double decoteMaxDeclarantSeul,
            final double decoteMaxDeclarantCouple,
            final double tauxDecote) {
        this.partsDeclarant = partsDeclarant;
        this.impotNet = impotNet;
        this.seuilDecoteDeclarantSeul = seuilDecoteDeclarantSeul;
        this.seuilDecoteDeclarantCouple = seuilDecoteDeclarantCouple;
        this.decoteMaxDeclarantSeul = decoteMaxDeclarantSeul;
        this.decoteMaxDeclarantCouple = decoteMaxDeclarantCouple;
        this.tauxDecote = tauxDecote;
    }

    /**
     * Calculates the tax relief amount.
     *
     * @return the calculated relief amount
     */
    @Override
    public Double calculer() {
        double montantDecote = 0;

        if (partsDeclarant == 1
                && impotNet < seuilDecoteDeclarantSeul) {
            montantDecote = decoteMaxDeclarantSeul
                    - (impotNet * tauxDecote);
        }

        if (partsDeclarant == 2
                && impotNet < seuilDecoteDeclarantCouple) {
            montantDecote = decoteMaxDeclarantCouple
                    - (impotNet * tauxDecote);
        }

        montantDecote = Math.round(montantDecote);
        if (impotNet <= montantDecote) {
            montantDecote = impotNet;
        }

        return montantDecote;
    }
}
