package com.kerware.simulateurreusine;

/**
 * Calculator for half-share ceiling (plafond demi-part).
 * Applies a ceiling on tax benefits from additional household shares.
 */
@SuppressWarnings("hiding")
final class CalculateurPlafondDemiPart implements Calculateur<Double> {

    /** Half share value for ceiling calculation. */
    private static final double DEMI_PART = 0.5;

    /** The tax before relief. */
    private final double impotAvantDecoteDeclarant;
    /** The net tax. */
    private final double impotNet;
    /** The declarant shares. */
    private final double partsDeclarant;
    /** The total household shares. */
    private final double partsFoyerFiscal;
    /** The half-share ceiling amount. */
    private final double plafondDemiPart;

    /**
     * Constructs a CalculateurPlafondDemiPart with tax parameters.
     *
     * @param impotAvantDecoteDeclarant tax before relief
     * @param impotNet the net tax
     * @param partsDeclarant the declarant shares
     * @param partsFoyerFiscal the household shares
     * @param plafondDemiPart the ceiling amount
     */
    @SuppressWarnings("hiding")
    CalculateurPlafondDemiPart(final double impotAvantDecoteDeclarant,
            final double impotNet,
            final double partsDeclarant,
            final double partsFoyerFiscal,
            final double plafondDemiPart) {
        this.impotAvantDecoteDeclarant = impotAvantDecoteDeclarant;
        this.impotNet = impotNet;
        this.partsDeclarant = partsDeclarant;
        this.partsFoyerFiscal = partsFoyerFiscal;
        this.plafondDemiPart = plafondDemiPart;
    }

    /**
     * Calculates tax applying the half-share ceiling.
     *
     * @return the adjusted tax amount
     */
    @Override
    public Double calculer() {
        double baisseImpot = impotAvantDecoteDeclarant - impotNet;
        double ecartPts = partsFoyerFiscal - partsDeclarant;
        double plafond = (ecartPts / DEMI_PART) * plafondDemiPart;

        if (baisseImpot >= plafond) {
            return impotAvantDecoteDeclarant - plafond;
        }

        return impotNet;
    }
}
