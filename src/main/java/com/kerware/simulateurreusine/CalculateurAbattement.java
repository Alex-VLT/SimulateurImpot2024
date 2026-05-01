package com.kerware.simulateurreusine;

/**
 * Calculator for income tax allowance (abattement).
 * Applies rate and min/max bounds to the net income.
 */
final class CalculateurAbattement implements Calculateur<Double> {

    /** The net revenue. */
    private final int revenuNet;

    /**
     * Constructs a CalculateurAbattement with the given net revenue.
     *
     * @param revenuNet the net revenue
     */
    @SuppressWarnings("hiding")
    CalculateurAbattement(final int revenuNet) {
        this.revenuNet = revenuNet;
    }

    /**
     * Calculates the tax allowance with bounds applied.
     *
     * @return the calculated allowance
     */
    @Override
    public Double calculer() {
        double abattement = revenuNet * ParametresImpot.TAUX_ABATTEMENT;

        if (abattement > ParametresImpot.ABATTEMENT_MAX) {
            abattement = ParametresImpot.ABATTEMENT_MAX;
        }

        if (abattement < ParametresImpot.ABATTEMENT_MIN) {
            abattement = ParametresImpot.ABATTEMENT_MIN;
        }

        return abattement;
    }
}
