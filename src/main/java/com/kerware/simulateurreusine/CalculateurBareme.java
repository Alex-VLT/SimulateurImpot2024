package com.kerware.simulateurreusine;

/**
 * Calculator for tax scale (barème).
 * Applies progressive tax rates based on income brackets.
 */
final class CalculateurBareme implements Calculateur<Double> {

    /** The taxable income per tax household share. */
    private final double revenuImposableParPart;

    /**
     * Constructs a CalculateurBareme with the given taxable income.
     *
     * @param revenuImposableParPart the taxable income per share
     */
    @SuppressWarnings("hiding")
    CalculateurBareme(final double revenuImposableParPart) {
        this.revenuImposableParPart = revenuImposableParPart;
    }

    /**
     * Calculates tax amount based on tax brackets and rates.
     *
     * @return the calculated tax amount
     */
    @Override
    public Double calculer() {
        double montant = 0;
        int[] limites = ParametresImpot.BAREME_LIMITES;
        double[] taux = ParametresImpot.BAREME_TAUX;

        for (int idx = 0; idx < taux.length; idx++) {
            if (revenuImposableParPart >= limites[idx]
                    && revenuImposableParPart < limites[idx + 1]) {
                montant += (revenuImposableParPart - limites[idx])
                        * taux[idx];
                break;
            }
            montant += (limites[idx + 1] - limites[idx]) * taux[idx];
        }

        return montant;
    }
}
