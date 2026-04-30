package com.kerware.simulateurreusine;

import com.kerware.simulateur.SituationFamiliale;

/**
 * Copie du Simulateur hérité dans un nouveau package pour réusinage progressif.
 * Le contenu est initialement identique au simulateur original afin de préserver
 * le comportement pour les tests de caractérisation.
 */
public class SimulateurReusine {


    private int[] baremeLimites = new int[6];

    private double[] baremeTaux = new double[5];

    private int abattementMaximum = 14171;
    private int abattementMinimum = 495;
    private double tauxAbattement = 0.1;

    private double plafondDemiPart = 1759;

    private double seuilDecoteDeclarantSeul = 1929;
    private double seuilDecoteDeclarantCouple = 3191;

    private double decoteMaxDeclarantSeul = 873;
    private double decoteMaxDeclarantCouple = 1444;
    private double tauxDecote = 0.4525;

    private int revenuNet = 0;
    private int nombreEnfantsACharge = 0;
    private int nombreEnfantsHandicapes = 0;

    private double revenuFiscalDeReference = 0;
    private double revenuImposableParPart = 0;

    private double abattement = 0;

    private double partsDeclarant = 0;
    private double partsFoyerFiscal = 0;
    private double montantDecote = 0;

    private double impotAvantDecoteDeclarant = 0;
    private double impotNet = 0;

    private boolean parentIsole = false;


    // Fonction de calcul de l'impôt sur le revenu net en France en 2024 sur les revenu 2023

    public long calculImpot( int revNet, SituationFamiliale sitFam, int nbEnfants, int nbEnfantsHandicapes, boolean parentIsol) {

        revenuNet = revNet;

        nombreEnfantsACharge = nbEnfants;
        nombreEnfantsHandicapes = nbEnfantsHandicapes;
        parentIsole = parentIsol;

        int[] pLim = ParametresImpot.LIMITES;
        for (int idx = 0; idx < pLim.length && idx < baremeLimites.length; idx++) {
            baremeLimites[idx] = pLim[idx];
        }

        double[] pT = ParametresImpot.TAUX;
        for (int idx = 0; idx < pT.length && idx < baremeTaux.length; idx++) {
            baremeTaux[idx] = pT[idx];
        }

        abattementMaximum = ParametresImpot.ABT_MAX;
        abattementMinimum = ParametresImpot.ABT_MIN;
        tauxAbattement = ParametresImpot.ABT_RATE;

        plafondDemiPart = ParametresImpot.PLAFOND_DEMI_PART;

        seuilDecoteDeclarantSeul = ParametresImpot.SEUIL_DECOTE_DECLARANT_SEUL;
        seuilDecoteDeclarantCouple = ParametresImpot.SEUIL_DECOTE_DECLARANT_COUPLE;

        decoteMaxDeclarantSeul = ParametresImpot.DECOTE_MAX_DECLARANT_SEUL;
        decoteMaxDeclarantCouple = ParametresImpot.DECOTE_MAX_DECLARANT_COUPLE;
        tauxDecote = ParametresImpot.TAUX_DECOTE;

        calculerAbattement();


        revenuFiscalDeReference = revenuNet - abattement;

        calculerPartsFiscales(sitFam);

        revenuImposableParPart = revenuFiscalDeReference / partsDeclarant;

        impotAvantDecoteDeclarant = 0;

        int i = 0;
        do {
            if ( revenuImposableParPart >= baremeLimites[i] && revenuImposableParPart < baremeLimites[i + 1] ) {
                impotAvantDecoteDeclarant += ( revenuImposableParPart - baremeLimites[i] ) * baremeTaux[i];
                break;
            } else {
                impotAvantDecoteDeclarant += ( baremeLimites[i + 1] - baremeLimites[i] ) * baremeTaux[i];
            }
            i++;
        } while (i < 5);

        impotAvantDecoteDeclarant = impotAvantDecoteDeclarant * partsDeclarant;
        impotAvantDecoteDeclarant = Math.round(impotAvantDecoteDeclarant);

        revenuImposableParPart = revenuFiscalDeReference / partsFoyerFiscal;
        impotNet = 0;
        i = 0;

        do {
            if ( revenuImposableParPart >= baremeLimites[i] && revenuImposableParPart < baremeLimites[i + 1] ) {
                impotNet += ( revenuImposableParPart - baremeLimites[i] ) * baremeTaux[i];
                break;
            } else {
                impotNet += ( baremeLimites[i + 1] - baremeLimites[i] ) * baremeTaux[i];
            }
            i++;
        } while (i < 5);

        impotNet = impotNet * partsFoyerFiscal;
        impotNet = Math.round(impotNet);

        double baisseImpot = impotAvantDecoteDeclarant - impotNet;

        double ecartPts = partsFoyerFiscal - partsDeclarant;

        double plafond = (ecartPts / 0.5) * plafondDemiPart;

        if ( baisseImpot >= plafond ) {
            impotNet = impotAvantDecoteDeclarant - plafond;
        }

        calculerDecote();

        impotNet = Math.round(impotNet) - montantDecote;

        return (long) impotNet;
    }

    private void calculerDecote() {
        montantDecote = 0;

        if (partsDeclarant == 1) {
            if (impotNet < seuilDecoteDeclarantSeul) {
                 montantDecote = decoteMaxDeclarantSeul - (impotNet * tauxDecote);
            }
        }

        if (partsDeclarant == 2) {
            if (impotNet < seuilDecoteDeclarantCouple) {
                 montantDecote = decoteMaxDeclarantCouple - (impotNet * tauxDecote);
            }
        }

        montantDecote = Math.round(montantDecote);
        if (impotNet <= montantDecote) {
            montantDecote = impotNet;
        }
    }

    private void calculerAbattement() {
        abattement = revenuNet * tauxAbattement;

        if (abattement > abattementMaximum) {
            abattement = abattementMaximum;
        }

        if (abattement < abattementMinimum) {
            abattement = abattementMinimum;
        }
    }

    private void calculerPartsFiscales(SituationFamiliale sitFam) {
        switch (sitFam) {
            case CELIBATAIRE:
                partsDeclarant = 1;
                break;
            case MARIE:
                partsDeclarant = 2;
                break;
            case DIVORCE:
                partsDeclarant = 1;
                break;
            case VEUF:
                if (nombreEnfantsACharge == 0) {
                    partsDeclarant = 1;
                } else {
                    partsDeclarant = 2;
                }
                partsDeclarant = 1;
                break;
        }

        if (nombreEnfantsACharge <= 2) {
            partsFoyerFiscal = partsDeclarant + nombreEnfantsACharge * 0.5;
        } else if (nombreEnfantsACharge > 2) {
            partsFoyerFiscal = partsDeclarant + 1.0 + (nombreEnfantsACharge - 2);
        }

        if (parentIsole) {
            if (nombreEnfantsACharge > 0) {
                partsFoyerFiscal = partsFoyerFiscal + 0.5;
            }
        }

        partsFoyerFiscal = partsFoyerFiscal + nombreEnfantsHandicapes * 0.5;
    }

    public static void main(String[] args) {
        SimulateurReusine simulateur = new SimulateurReusine();
        long impot = simulateur.calculImpot(65000, SituationFamiliale.MARIE, 3, 0, false);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(65000, SituationFamiliale.MARIE, 3, 1, false);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(35000, SituationFamiliale.DIVORCE, 1, 0, true);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(35000, SituationFamiliale.DIVORCE, 2, 0, true);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(50000, SituationFamiliale.DIVORCE, 3, 0, true);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(50000, SituationFamiliale.DIVORCE, 3, 1, true);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(200000, SituationFamiliale.CELIBATAIRE, 0, 0, true);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(35000, SituationFamiliale.CELIBATAIRE, 0, 0, false);
        System.out.println("Impot sur le revenu net : " + impot);

    }

    // Setters and Getters pour adaptation progressive
    private SituationFamiliale situationFamiliale;

    public void setRevenusNet(int revenusNet ) {
        this.revenuNet = revenusNet;
    }

    public void setSituationFamilliale(SituationFamiliale situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    public void setNbEnfantsACharge(int nombreEnfantsACharge ) {
        this.nombreEnfantsACharge = nombreEnfantsACharge;
    }

    public void setNbEnfantsEnSituationDeHandicap(int nombreEnfantsEnSituationDeHandicap) {
        this.nombreEnfantsHandicapes = nombreEnfantsEnSituationDeHandicap;
    }

    public void setParentIsole(boolean estParentIsole) {
        this.parentIsole = estParentIsole;
    }


    public void calculImpotSurRevenuNet() {
        calculImpot(this.revenuNet, this.situationFamiliale, this.nombreEnfantsACharge,
                this.nombreEnfantsHandicapes, this.parentIsole);
    }


    public int getRevenuFiscalDeReference() {
        return (int) Math.round(this.revenuFiscalDeReference);
    }


    public int getAbattement() {
        return (int) Math.round(this.abattement);
    }


    public double getNbPartsFoyerFiscal() {
        return this.partsFoyerFiscal;
    }

    public int getDecote() {
        return (int) Math.round(this.montantDecote);
    }

    public int getImpotSurRevenuNet() {
        return (int) this.impotNet;
    }
}
