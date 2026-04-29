package com.kerware.simulateur;

import com.kerware.simulateurreusine.SimulateurReusine;
/**
 * Adaptateur pour brancher la nouvelle implémentation réusinée sur les tests existants.
 * Pour l'instant il délègue directement au simulateur copié pour préserver le comportement.
 */
public class AdaptateurVersCodeReusine implements ICalculateurImpot {

    SimulateurReusine simulateurReusine = new SimulateurReusine();

    @Override
    public void setRevenusNet(int rn) {
        simulateurReusine.setRevenusNet( rn );
    }

    @Override
    public void setSituationFamiliale(SituationFamiliale sf) {
        simulateurReusine.setSituationFamilliale( sf );
    }

    @Override
    public void setNbEnfantsACharge(int nbe) {
        simulateurReusine.setNbEnfantsACharge( nbe );
    }

    @Override
    public void setNbEnfantsSituationHandicap(int nbesh) {
        simulateurReusine.setNbEnfantsEnSituationDeHandicap( nbesh );
    }

    @Override
    public void setParentIsole(boolean pi) {
        simulateurReusine.setParentIsole( pi );
    }

    @Override
    public void calculImpotSurRevenuNet() {
        simulateurReusine.calculImpotSurRevenuNet();
    }

    @Override
    public int getRevenuFiscalReference() {
        return simulateurReusine.getRevenuFiscalDeReference();
    }

    @Override
    public int getAbattement() {
        return simulateurReusine.getAbattement();
    }

    @Override
    public double getNbPartsFoyerFiscal() {
        return simulateurReusine.getNbPartsFoyerFiscal();
    }

    @Override
    public int getImpotAvantDecote() {
        return simulateurReusine.getImpotSurRevenuNet() + simulateurReusine.getDecote();
    }

    @Override
    public int getDecote() {
        return simulateurReusine.getDecote();
    }

    @Override
    public int getImpotSurRevenuNet() {
        return simulateurReusine.getImpotSurRevenuNet();
    }
}
