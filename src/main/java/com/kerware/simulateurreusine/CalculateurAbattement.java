package com.kerware.simulateurreusine;

final class CalculateurAbattement implements Calculateur<Double> {

    private final int revenuNet;

    CalculateurAbattement(int revenuNet) {
        this.revenuNet = revenuNet;
    }

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