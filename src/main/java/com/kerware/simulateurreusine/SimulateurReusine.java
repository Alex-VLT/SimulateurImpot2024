package com.kerware.simulateurreusine;

import com.kerware.simulateur.SituationFamiliale;

/**
 * Orchestrateur du calcul réusinée: il conserve l'état exposé par les adaptateurs
 * et délègue les règles métier à des classes spécialisées.
 */
public class SimulateurReusine {

    private int revenuNet;
    private int nombreEnfantsACharge;
    private int nombreEnfantsHandicapes;
    private boolean parentIsole;
    private SituationFamiliale situationFamiliale;

    private double revenuFiscalDeReference;
    private double abattement;
    private double partsDeclarant;
    private double partsFoyerFiscal;
    private double montantDecote;
    private double impotAvantDecoteDeclarant;
    private double impotNet;

    public long calculImpot(int revNet, SituationFamiliale sitFam, int nbEnfants,
            int nbEnfantsHandicapes, boolean parentIsol) {
        revenuNet = revNet;
        nombreEnfantsACharge = nbEnfants;
        this.nombreEnfantsHandicapes = nbEnfantsHandicapes;
        parentIsole = parentIsol;
        situationFamiliale = sitFam;

        abattement = new CalculateurAbattement(revenuNet).calculer();
        revenuFiscalDeReference = revenuNet - abattement;

        CalculateurPartsFiscales partsFiscales = new CalculateurPartsFiscales(situationFamiliale,
            nombreEnfantsACharge, nombreEnfantsHandicapes, parentIsole);
        partsFiscales.calculer();
        partsDeclarant = partsFiscales.getPartsDeclarant();
        partsFoyerFiscal = partsFiscales.getPartsFoyerFiscal();

        calculerImpotsAvantEtApresPlafond();
        montantDecote = new CalculateurDecote(partsDeclarant, impotNet,
                ParametresImpot.SEUIL_DECOTE_DECLARANT_SEUL,
                ParametresImpot.SEUIL_DECOTE_DECLARANT_COUPLE,
                ParametresImpot.DECOTE_MAX_DECLARANT_SEUL,
                ParametresImpot.DECOTE_MAX_DECLARANT_COUPLE,
            ParametresImpot.TAUX_DECOTE).calculer();
        impotNet = Math.round(impotNet) - montantDecote;

        return (long) impotNet;
    }

    private void calculerImpotsAvantEtApresPlafond() {
        double revenuImposableParPart = revenuFiscalDeReference / partsDeclarant;
        impotAvantDecoteDeclarant = Math.round(new CalculateurBareme(revenuImposableParPart).calculer()
            * partsDeclarant);

        revenuImposableParPart = revenuFiscalDeReference / partsFoyerFiscal;
        impotNet = Math.round(new CalculateurBareme(revenuImposableParPart).calculer() * partsFoyerFiscal);

        impotNet = new CalculateurPlafondDemiPart(impotAvantDecoteDeclarant, impotNet,
            partsDeclarant, partsFoyerFiscal, ParametresImpot.PLAFOND_DEMI_PART).calculer();
    }

    public void setRevenusNet(int revenusNet) {
        this.revenuNet = revenusNet;
    }

    public void setSituationFamilliale(SituationFamiliale situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    public void setNbEnfantsACharge(int nombreEnfantsACharge) {
        this.nombreEnfantsACharge = nombreEnfantsACharge;
    }

    public void setNbEnfantsEnSituationDeHandicap(int nombreEnfantsEnSituationDeHandicap) {
        this.nombreEnfantsHandicapes = nombreEnfantsEnSituationDeHandicap;
    }

    public void setParentIsole(boolean estParentIsole) {
        this.parentIsole = estParentIsole;
    }

    public void calculImpotSurRevenuNet() {
        calculImpot(revenuNet, situationFamiliale, nombreEnfantsACharge,
                nombreEnfantsHandicapes, parentIsole);
    }

    public int getRevenuFiscalDeReference() {
        return (int) Math.round(revenuFiscalDeReference);
    }

    public int getAbattement() {
        return (int) Math.round(abattement);
    }

    public double getNbPartsFoyerFiscal() {
        return partsFoyerFiscal;
    }

    public int getDecote() {
        return (int) Math.round(montantDecote);
    }

    public int getImpotSurRevenuNet() {
        return (int) impotNet;
    }

    public int getImpotAvantDecote() {
        return (int) Math.round(impotAvantDecoteDeclarant);
    }
}