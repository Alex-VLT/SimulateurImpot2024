package com.kerware.simulateurreusine;

import com.kerware.simulateur.SituationFamiliale;

/**
 * Calculator for tax household shares (parts fiscales).
 * Computes the declarant shares and total household shares based on family
 * situation and dependents.
 */
@SuppressWarnings("hiding")
final class CalculateurPartsFiscales implements Calculateur<Void> {

    /** Half share value for dependents and isolated parent. */
    private static final double DEMI_PART = 0.5;
    /** Full share value. */
    private static final double PART_PLEINE = 1.0;
    /** Number of shares for married declarants. */
    private static final int PARTS_MARIE = 2;
    /** Number of shares for single declarants. */
    private static final int PARTS_SEUL = 1;
    /** Threshold for child benefit calculation. */
    private static final int SEUIL_ENFANTS = 2;

    /** The family situation. */
    private final SituationFamiliale situationFamiliale;
    /** The number of children in charge. */
    private final int nombreEnfantsACharge;
    /** The number of disabled children. */
    private final int nombreEnfantsHandicapes;
    /** Whether the declarant is a single parent. */
    private final boolean parentIsole;

    /** The calculated declarant shares. */
    private double partsDeclarant;
    /** The calculated total household shares. */
    private double partsFoyerFiscal;

    /**
     * Constructs a CalculateurPartsFiscales with family details.
     *
     * @param situationFamiliale the family situation
     * @param nombreEnfantsACharge the number of dependent children
     * @param nombreEnfantsHandicapes the number of disabled children
     * @param parentIsole whether the filer is a single parent
     */    @SuppressWarnings("hiding")    CalculateurPartsFiscales(final SituationFamiliale situationFamiliale,
            final int nombreEnfantsACharge,
            final int nombreEnfantsHandicapes,
            final boolean parentIsole) {
        this.situationFamiliale = situationFamiliale;
        this.nombreEnfantsACharge = nombreEnfantsACharge;
        this.nombreEnfantsHandicapes = nombreEnfantsHandicapes;
        this.parentIsole = parentIsole;
    }

    /**
     * Calculates the declarant and household shares.
     *
     * @return null (state stored in instance variables)
     */
    @Override
    public Void calculer() {
        partsDeclarant = calculerPartsDeclarant();
        partsFoyerFiscal = calculerPartsFoyerFiscal(partsDeclarant);
        return null;
    }

    /**
     * Gets the total shares of the declarant.
     *
     * @return the declarant shares
     */
    double getPartsDeclarant() {
        return partsDeclarant;
    }

    /**
     * Gets the total shares of the household.
     *
     * @return the household shares
     */
    double getPartsFoyerFiscal() {
        return partsFoyerFiscal;
    }

    /**
     * Calculates the base shares for the declarant.
     *
     * @return 2 for married, 1 for single
     */
    private double calculerPartsDeclarant() {
        if (situationFamiliale == SituationFamiliale.MARIE) {
            return PARTS_MARIE;
        }
        return PARTS_SEUL;
    }

    /**
     * Calculates total household shares including dependents.
     *
     * @param partsDeclarant the declarant shares
     * @return the total household shares
     */
    @SuppressWarnings("hiding")
    private double calculerPartsFoyerFiscal(
            final double partsDeclarant) {
        double partsFoyerFiscal;

        if (nombreEnfantsACharge <= SEUIL_ENFANTS) {
            partsFoyerFiscal = partsDeclarant
                    + nombreEnfantsACharge * DEMI_PART;
        } else {
            partsFoyerFiscal = partsDeclarant + PART_PLEINE
                    + (nombreEnfantsACharge - SEUIL_ENFANTS);
        }

        if (parentIsole && nombreEnfantsACharge > 0) {
            partsFoyerFiscal = partsFoyerFiscal + DEMI_PART;
        }

        return partsFoyerFiscal
                + nombreEnfantsHandicapes * DEMI_PART;
    }
}
