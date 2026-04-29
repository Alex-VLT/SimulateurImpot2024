package testssimulateur;

import com.kerware.simulateur.AdaptateurVersCodeHerite;
import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSimulateur {
    static final int CODE_HERITE = 1;
    static final int CODE_REUSINE = 2;
    static final int CODE = CODE_HERITE;
    static ICalculateurImpot calculateur;
    @BeforeAll
    public static void prepareCalculateurImpot() {
        switch( CODE ) {
            case  CODE_HERITE -> calculateur = new AdaptateurVersCodeHerite();
            case  CODE_REUSINE -> calculateur = null;
        }
    }

    private void calculerImpot(int revenusNet, SituationFamiliale situationFamiliale,
            int nombreEnfantsACharge, int nombreEnfantsEnSituationDeHandicap,
            boolean estParentIsole) {
        calculateur.setRevenusNet(revenusNet);
        calculateur.setSituationFamiliale(situationFamiliale);
        calculateur.setNbEnfantsACharge(nombreEnfantsACharge);
        calculateur.setNbEnfantsSituationHandicap(nombreEnfantsEnSituationDeHandicap);
        calculateur.setParentIsole(estParentIsole);
        calculateur.calculImpotSurRevenuNet();
    }

    @Test
    @DisplayName( "Test du calcul de l'impot pour un célibataire sans enfant")
    public void testImpotSurRevenuNetPourUnCelibataireSansEnfant() {
        calculerImpot(35000, SituationFamiliale.CELIBATAIRE, 0, 0, false);

        assertEquals(2736, calculateur.getImpotSurRevenuNet());
        assertEquals(1, calculateur.getNbPartsFoyerFiscal());
        assertEquals(31500, calculateur.getRevenuFiscalReference());
        assertEquals(3500, calculateur.getAbattement());
    }

    @Test
    @DisplayName("Test du calcul de l'impot pour un couple marié avec trois enfants")
    public void testImpotSurRevenuNetPourUnMarieAvecTroisEnfants() {
        calculerImpot(65000, SituationFamiliale.MARIE, 3, 0, false);

        assertEquals(685, calculateur.getImpotSurRevenuNet());
        assertEquals(4.0, calculateur.getNbPartsFoyerFiscal());
        assertEquals(1466, calculateur.getImpotAvantDecote());
    }

    @Test
    @DisplayName("Test du calcul de l'impot pour un divorcé parent isolé avec un enfant")
    public void testImpotSurRevenuNetPourUnDivorceParentIsoleAvecUnEnfant() {
        calculerImpot(35000, SituationFamiliale.DIVORCE, 1, 0, true);

        assertEquals(550, calculateur.getImpotSurRevenuNet());
        assertEquals(2.0, calculateur.getNbPartsFoyerFiscal());
        assertEquals(430, calculateur.getDecote());
    }

    @Test
    @DisplayName("Test du calcul de l'impot pour un divorcé avec enfants et handicap")
    public void testImpotSurRevenuNetPourUnDivorceAvecEnfantsEtHandicap() {
        calculerImpot(50000, SituationFamiliale.DIVORCE, 3, 1, true);

        assertEquals(0, calculateur.getImpotSurRevenuNet());
        assertEquals(4.0, calculateur.getNbPartsFoyerFiscal());

    }

}
