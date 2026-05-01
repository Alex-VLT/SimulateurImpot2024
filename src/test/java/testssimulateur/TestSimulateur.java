package testssimulateur;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kerware.simulateur.AdaptateurVersCodeHerite;
import com.kerware.simulateur.AdaptateurVersCodeReusine;
import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;
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

    // ============================================================
    // Tests du code legacy (AdaptateurVersCodeHerite)
    // Changez la variable CODE au début pour basculer entre versions
    // ============================================================

    @Test
    @DisplayName("Legacy: Célibataire sans enfant")
    public void testLegacyCelibataireSansEnfant() {
        ICalculateurImpot legacy = new AdaptateurVersCodeHerite();
        legacy.setRevenusNet(35000);
        legacy.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
        legacy.setNbEnfantsACharge(0);
        legacy.setNbEnfantsSituationHandicap(0);
        legacy.setParentIsole(false);
        legacy.calculImpotSurRevenuNet();

        assertEquals(2736, legacy.getImpotSurRevenuNet());
        assertEquals(1, legacy.getNbPartsFoyerFiscal());
        assertEquals(31500, legacy.getRevenuFiscalReference());
        assertEquals(3500, legacy.getAbattement());
    }

    @Test
    @DisplayName("Legacy: Marié avec 3 enfants")
    public void testLegacyMarieAvec3Enfants() {
        ICalculateurImpot legacy = new AdaptateurVersCodeHerite();
        legacy.setRevenusNet(65000);
        legacy.setSituationFamiliale(SituationFamiliale.MARIE);
        legacy.setNbEnfantsACharge(3);
        legacy.setNbEnfantsSituationHandicap(0);
        legacy.setParentIsole(false);
        legacy.calculImpotSurRevenuNet();

        assertEquals(685, legacy.getImpotSurRevenuNet());
        assertEquals(4.0, legacy.getNbPartsFoyerFiscal());
        assertEquals(1466, legacy.getImpotAvantDecote());
    }

    @Test
    @DisplayName("Legacy: Divorcé parent isolé avec 1 enfant")
    public void testLegacyDivorcParentIsoleAvec1Enfant() {
        ICalculateurImpot legacy = new AdaptateurVersCodeHerite();
        legacy.setRevenusNet(35000);
        legacy.setSituationFamiliale(SituationFamiliale.DIVORCE);
        legacy.setNbEnfantsACharge(1);
        legacy.setNbEnfantsSituationHandicap(0);
        legacy.setParentIsole(true);
        legacy.calculImpotSurRevenuNet();

        assertEquals(550, legacy.getImpotSurRevenuNet());
        assertEquals(2.0, legacy.getNbPartsFoyerFiscal());
        assertEquals(430, legacy.getDecote());
    }

    @Test
    @DisplayName("Legacy: Divorcé avec enfants et handicap")
    public void testLegacyDivorcAvecEnfantsEtHandicap() {
        ICalculateurImpot legacy = new AdaptateurVersCodeHerite();
        legacy.setRevenusNet(50000);
        legacy.setSituationFamiliale(SituationFamiliale.DIVORCE);
        legacy.setNbEnfantsACharge(3);
        legacy.setNbEnfantsSituationHandicap(1);
        legacy.setParentIsole(true);
        legacy.calculImpotSurRevenuNet();

        assertEquals(0, legacy.getImpotSurRevenuNet());
        assertEquals(4.0, legacy.getNbPartsFoyerFiscal());
    }

    @Test
    @DisplayName("Legacy: Couverture tranches du barème")
    public void testLegacyCouvertureTranches() {
        ICalculateurImpot legacy = new AdaptateurVersCodeHerite();
        int[] revenus = new int[]{10000, 15000, 50000, 100000, 300000};
        for (int rev : revenus) {
            legacy.setRevenusNet(rev);
            legacy.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            legacy.setNbEnfantsACharge(0);
            legacy.setNbEnfantsSituationHandicap(0);
            legacy.setParentIsole(false);
            legacy.calculImpotSurRevenuNet();
            assertTrue(legacy.getImpotSurRevenuNet() >= 0);
        }
    }

    @Test
    @DisplayName("Legacy: Abattement min/max")
    public void testLegacyAbattementMinMax() {
        ICalculateurImpot legacy = new AdaptateurVersCodeHerite();
        
        legacy.setRevenusNet(100);
        legacy.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
        legacy.setNbEnfantsACharge(0);
        legacy.setNbEnfantsSituationHandicap(0);
        legacy.setParentIsole(false);
        legacy.calculImpotSurRevenuNet();
        assertEquals(ParametresImpot.ABATTEMENT_MIN, legacy.getAbattement());

        legacy.setRevenusNet(1_000_000);
        legacy.calculImpotSurRevenuNet();
        assertEquals(ParametresImpot.ABATTEMENT_MAX, legacy.getAbattement());
    }

    @Test
    @DisplayName("Legacy: Parts fiscales variants")
    public void testLegacyPartsFiscalesVariants() {
        ICalculateurImpot legacy = new AdaptateurVersCodeHerite();
        
        // VEUF, 0 enfants
        legacy.setRevenusNet(20000);
        legacy.setSituationFamiliale(SituationFamiliale.VEUF);
        legacy.setNbEnfantsACharge(0);
        legacy.setNbEnfantsSituationHandicap(0);
        legacy.setParentIsole(false);
        legacy.calculImpotSurRevenuNet();
        assertEquals(1.0, legacy.getNbPartsFoyerFiscal());

        // VEUF, 2 enfants
        legacy.setNbEnfantsACharge(2);
        legacy.calculImpotSurRevenuNet();
        assertEquals(2.0, legacy.getNbPartsFoyerFiscal());
    }

    @Test
    @DisplayName("Legacy: Décote appliquée")
    public void testLegacyDecoteApplied() {
        ICalculateurImpot legacy = new AdaptateurVersCodeHerite();
        legacy.setRevenusNet(2000);
        legacy.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
        legacy.setNbEnfantsACharge(0);
        legacy.setNbEnfantsSituationHandicap(0);
        legacy.setParentIsole(false);
        legacy.calculImpotSurRevenuNet();
        assertTrue(legacy.getDecote() >= 0);
    }

}
