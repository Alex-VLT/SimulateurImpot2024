package com.kerware.simulateurreusine;

final class CalculateurDecote implements Calculateur<Double> {

    private final double partsDeclarant;
    private final double impotNet;
    private final double seuilDecoteDeclarantSeul;
    private final double seuilDecoteDeclarantCouple;
    private final double decoteMaxDeclarantSeul;
    private final double decoteMaxDeclarantCouple;
    private final double tauxDecote;

    CalculateurDecote(double partsDeclarant, double impotNet,
            double seuilDecoteDeclarantSeul, double seuilDecoteDeclarantCouple,
            double decoteMaxDeclarantSeul, double decoteMaxDeclarantCouple,
            double tauxDecote) {
        this.partsDeclarant = partsDeclarant;
        this.impotNet = impotNet;
        this.seuilDecoteDeclarantSeul = seuilDecoteDeclarantSeul;
        this.seuilDecoteDeclarantCouple = seuilDecoteDeclarantCouple;
        this.decoteMaxDeclarantSeul = decoteMaxDeclarantSeul;
        this.decoteMaxDeclarantCouple = decoteMaxDeclarantCouple;
        this.tauxDecote = tauxDecote;
    }

    @Override
    public Double calculer() {
        double montantDecote = 0;

        if (partsDeclarant == 1 && impotNet < seuilDecoteDeclarantSeul) {
            montantDecote = decoteMaxDeclarantSeul - (impotNet * tauxDecote);
        }

        if (partsDeclarant == 2 && impotNet < seuilDecoteDeclarantCouple) {
            montantDecote = decoteMaxDeclarantCouple - (impotNet * tauxDecote);
        }

        montantDecote = Math.round(montantDecote);
        if (impotNet <= montantDecote) {
            montantDecote = impotNet;
        }

        return montantDecote;
    }
}