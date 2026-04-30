package com.kerware.simulateurreusine;

final class CalculateurPlafondDemiPart implements Calculateur<Double> {

    private final double impotAvantDecoteDeclarant;
    private final double impotNet;
    private final double partsDeclarant;
    private final double partsFoyerFiscal;
    private final double plafondDemiPart;

    CalculateurPlafondDemiPart(double impotAvantDecoteDeclarant, double impotNet,
            double partsDeclarant, double partsFoyerFiscal, double plafondDemiPart) {
        this.impotAvantDecoteDeclarant = impotAvantDecoteDeclarant;
        this.impotNet = impotNet;
        this.partsDeclarant = partsDeclarant;
        this.partsFoyerFiscal = partsFoyerFiscal;
        this.plafondDemiPart = plafondDemiPart;
    }

    @Override
    public Double calculer() {
        double baisseImpot = impotAvantDecoteDeclarant - impotNet;
        double ecartPts = partsFoyerFiscal - partsDeclarant;
        double plafond = (ecartPts / 0.5) * plafondDemiPart;

        if (baisseImpot >= plafond) {
            return impotAvantDecoteDeclarant - plafond;
        }

        return impotNet;
    }
}