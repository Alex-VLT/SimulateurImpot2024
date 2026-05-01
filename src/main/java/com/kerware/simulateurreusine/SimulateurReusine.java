package com.kerware.simulateurreusine;

import com.kerware.simulateur.SituationFamiliale;

/**
 * Orchestrator of the refactored tax calculation.
 * Maintains state provided by adapters and delegates business rules
 * to specialized classes.
 */
@SuppressWarnings("hiding")
public class SimulateurReusine {

    /** The net revenue. */
    private int revenuNet;
    /** The number of dependent children. */
    private int nombreEnfantsACharge;
    /** The number of disabled children. */
    private int nombreEnfantsHandicapes;
    /** Whether the declarant is a single parent. */
    private boolean parentIsole;
    /** The family situation of the declarant. */
    private SituationFamiliale situationFamiliale;

    /** The tax reference revenue (after allowance). */
    private double revenuFiscalDeReference;
    /** The calculated allowance. */
    private double abattement;
    /** The declarant's tax household shares. */
    private double partsDeclarant;
    /** The total household shares. */
    private double partsFoyerFiscal;
    /** The calculated relief amount. */
    private double montantDecote;
    /** The tax before relief. */
    private double impotAvantDecoteDeclarant;
    /** The net tax after ceiling application. */
    private double impotNet;

    /**
     * Calculates the income tax based on the provided parameters.
     *
     * @param revNet the net revenue
     * @param sitFam the family situation
     * @param nbEnfants the number of dependent children
     * @param nbEnfantsHandicapes the number of disabled children
     * @param parentIsol whether the filer is a single parent
     * @return the calculated net tax
     */
    public long calculImpot(final int revNet,
            final SituationFamiliale sitFam,
            final int nbEnfants,
            final int nbEnfantsHandicapes,
            final boolean parentIsol) {
        revenuNet = revNet;
        nombreEnfantsACharge = nbEnfants;
        this.nombreEnfantsHandicapes = nbEnfantsHandicapes;
        parentIsole = parentIsol;
        situationFamiliale = sitFam;

        abattement = new CalculateurAbattement(revenuNet).calculer();
        revenuFiscalDeReference = revenuNet - abattement;

        CalculateurPartsFiscales partsFiscales =
                new CalculateurPartsFiscales(situationFamiliale,
                        nombreEnfantsACharge,
                        nombreEnfantsHandicapes,
                        parentIsole);
        partsFiscales.calculer();
        partsDeclarant = partsFiscales.getPartsDeclarant();
        partsFoyerFiscal = partsFiscales.getPartsFoyerFiscal();

        calculerImpotsAvantEtApresPlafond();
        montantDecote = new CalculateurDecote(partsDeclarant,
                impotNet,
                ParametresImpot.SEUIL_DECOTE_DECLARANT_SEUL,
                ParametresImpot.SEUIL_DECOTE_DECLARANT_COUPLE,
                ParametresImpot.DECOTE_MAX_DECLARANT_SEUL,
                ParametresImpot.DECOTE_MAX_DECLARANT_COUPLE,
                ParametresImpot.TAUX_DECOTE).calculer();
        impotNet = Math.round(impotNet) - montantDecote;

        return (long) impotNet;
    }

    /**
     * Calculates taxes before and after ceiling application.
     */
    private void calculerImpotsAvantEtApresPlafond() {
        double revenuImposableParPart =
                revenuFiscalDeReference / partsDeclarant;
        impotAvantDecoteDeclarant = Math.round(
                new CalculateurBareme(revenuImposableParPart)
                        .calculer() * partsDeclarant);

        revenuImposableParPart =
                revenuFiscalDeReference / partsFoyerFiscal;
        impotNet = Math.round(
                new CalculateurBareme(revenuImposableParPart)
                        .calculer() * partsFoyerFiscal);

        impotNet = new CalculateurPlafondDemiPart(
                impotAvantDecoteDeclarant,
                impotNet,
                partsDeclarant,
                partsFoyerFiscal,
                ParametresImpot.PLAFOND_DEMI_PART).calculer();
    }

    /**
     * Sets the net revenue.
     *
     * @param revenusNet the net revenue
     */
    public void setRevenusNet(final int revenusNet) {
        this.revenuNet = revenusNet;
    }

    /**
     * Sets the family situation.
     *
     * @param situationFamiliale the family situation
     */
    @SuppressWarnings("hiding")
    public void setSituationFamilliale(
            final SituationFamiliale situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    /**
     * Sets the number of dependent children.
     *
     * @param nombreEnfantsACharge the number of dependents
     */
    @SuppressWarnings("hiding")
    public void setNbEnfantsACharge(final int nombreEnfantsACharge) {
        this.nombreEnfantsACharge = nombreEnfantsACharge;
    }

    /**
     * Sets the number of disabled children.
     *
     * @param nombreEnfantsEnSituationDeHandicap
     *        the number of disabled children
     */
    public void setNbEnfantsEnSituationDeHandicap(
            final int nombreEnfantsEnSituationDeHandicap) {
        this.nombreEnfantsHandicapes =
                nombreEnfantsEnSituationDeHandicap;
    }

    /**
     * Sets whether the declarant is a single parent.
     *
     * @param estParentIsole true if single parent
     */
    public void setParentIsole(final boolean estParentIsole) {
        this.parentIsole = estParentIsole;
    }

    /**
     * Calculates tax using the stored revenue.
     */
    public void calculImpotSurRevenuNet() {
        calculImpot(revenuNet, situationFamiliale,
                nombreEnfantsACharge,
                nombreEnfantsHandicapes,
                parentIsole);
    }

    /**
     * Gets the tax reference revenue.
     *
     * @return the tax reference revenue
     */
    public int getRevenuFiscalDeReference() {
        return (int) Math.round(revenuFiscalDeReference);
    }

    /**
     * Gets the allowance amount.
     *
     * @return the allowance
     */
    public int getAbattement() {
        return (int) Math.round(abattement);
    }

    /**
     * Gets the total household shares.
     *
     * @return the household shares
     */
    public double getNbPartsFoyerFiscal() {
        return partsFoyerFiscal;
    }

    /**
     * Gets the relief amount.
     *
     * @return the relief
     */
    public int getDecote() {
        return (int) Math.round(montantDecote);
    }

    /**
     * Gets the net tax on income.
     *
     * @return the net tax
     */
    public int getImpotSurRevenuNet() {
        return (int) impotNet;
    }

    /**
     * Gets the tax before relief.
     *
     * @return the tax before relief
     */
    public int getImpotAvantDecote() {
        return (int) Math.round(impotAvantDecoteDeclarant);
    }
}
