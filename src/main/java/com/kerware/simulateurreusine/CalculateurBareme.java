package com.kerware.simulateurreusine;

final class CalculateurBareme implements Calculateur<Double> {

    private final double revenuImposableParPart;

    CalculateurBareme(double revenuImposableParPart) {
        this.revenuImposableParPart = revenuImposableParPart;
    }

    @Override
    public Double calculer() {
        double montant = 0;
        int[] limites = ParametresImpot.BAREME_LIMITES;
        double[] taux = ParametresImpot.BAREME_TAUX;

        for (int idx = 0; idx < taux.length; idx++) {
            if (revenuImposableParPart >= limites[idx] && revenuImposableParPart < limites[idx + 1]) {
                montant += (revenuImposableParPart - limites[idx]) * taux[idx];
                break;
            }
            montant += (limites[idx + 1] - limites[idx]) * taux[idx];
        }

        return montant;
    }
}