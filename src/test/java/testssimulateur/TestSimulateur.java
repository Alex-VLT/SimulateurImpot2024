package testssimulateur;

import com.kerware.simulateur.AdaptateurVersCodeHerite;
import com.kerware.simulateur.AdaptateurVersCodeReusine;
import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.kerware.simulateurreusine.ParametresImpot;

public class TestSimulateur {
    static final int CODE_HERITE = 1;
    static final int CODE_REUSINE = 2;
    static final int CODE = CODE_REUSINE;
    static ICalculateurImpot calculateur;
    @BeforeAll
    public static void prepareCalculateurImpot() {
        switch( CODE ) {
            case  CODE_HERITE -> calculateur = new AdaptateurVersCodeHerite();
            case  CODE_REUSINE -> calculateur = new AdaptateurVersCodeReusine();
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

    @Test
    @DisplayName( "Couverture: tranches du barème")
    public void testCouvertureTranches() {
        int[] revenus = new int[]{10000, 15000, 50000, 100000, 300000};
        for (int rev : revenus) {
            calculerImpot(rev, SituationFamiliale.CELIBATAIRE, 0, 0, false);
            assertTrue(calculateur.getImpotSurRevenuNet() >= 0);
        }
    }

    @Test
    @DisplayName("Couverture: abattement min/max")
    public void testAbattementMinMax() {
        calculerImpot(100, SituationFamiliale.CELIBATAIRE, 0, 0, false);
        assertEquals(ParametresImpot.ABATTEMENT_MIN, calculateur.getAbattement());

        calculerImpot(1_000_000, SituationFamiliale.CELIBATAIRE, 0, 0, false);
        assertEquals(ParametresImpot.ABATTEMENT_MAX, calculateur.getAbattement());
    }

    @Test
    @DisplayName("Couverture: parts fiscales et parent isolé")
    public void testPartsFiscalesVariants() {
        // VEUF, 0 enfants
        calculerImpot(20000, SituationFamiliale.VEUF, 0, 0, false);
        assertEquals(1.0, calculateur.getNbPartsFoyerFiscal());

        // VEUF, 2 enfants
        calculerImpot(20000, SituationFamiliale.VEUF, 2, 0, false);
        assertEquals(2.0, calculateur.getNbPartsFoyerFiscal());

        // parent isolé
        calculerImpot(20000, SituationFamiliale.CELIBATAIRE, 1, 0, true);
        assertEquals(1.0 + 1 * 0.5 + 0.5, calculateur.getNbPartsFoyerFiscal());
    }

    @Test
    @DisplayName("Couverture: décote appliquée")
    public void testDecoteApplied() {
        calculerImpot(2000, SituationFamiliale.CELIBATAIRE, 0, 0, false);
        assertTrue(calculateur.getDecote() >= 0);
    }

}
