package com.kerware.simulateurreusine;

import com.kerware.simulateur.SituationFamiliale;

final class CalculateurPartsFiscales implements Calculateur<Void> {

    private final SituationFamiliale situationFamiliale;
    private final int nombreEnfantsACharge;
    private final int nombreEnfantsHandicapes;
    private final boolean parentIsole;

    private double partsDeclarant;
    private double partsFoyerFiscal;

    CalculateurPartsFiscales(SituationFamiliale situationFamiliale, int nombreEnfantsACharge,
            int nombreEnfantsHandicapes, boolean parentIsole) {
        this.situationFamiliale = situationFamiliale;
        this.nombreEnfantsACharge = nombreEnfantsACharge;
        this.nombreEnfantsHandicapes = nombreEnfantsHandicapes;
        this.parentIsole = parentIsole;
    }

    @Override
    public Void calculer() {
        partsDeclarant = calculerPartsDeclarant();
        partsFoyerFiscal = calculerPartsFoyerFiscal(partsDeclarant);
        return null;
    }

    double getPartsDeclarant() {
        return partsDeclarant;
    }

    double getPartsFoyerFiscal() {
        return partsFoyerFiscal;
    }

    private double calculerPartsDeclarant() {
        if (situationFamiliale == SituationFamiliale.MARIE) {
            return 2;
        }
        return 1;
    }

    private double calculerPartsFoyerFiscal(double partsDeclarant) {
        double partsFoyerFiscal;

        if (nombreEnfantsACharge <= 2) {
            partsFoyerFiscal = partsDeclarant + nombreEnfantsACharge * 0.5;
        } else {
            partsFoyerFiscal = partsDeclarant + 1.0 + (nombreEnfantsACharge - 2);
        }

        if (parentIsole && nombreEnfantsACharge > 0) {
            partsFoyerFiscal = partsFoyerFiscal + 0.5;
        }

        return partsFoyerFiscal + nombreEnfantsHandicapes * 0.5;
    }
}