package champollion;
import java.util.Date;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class ChampollionJUnitTest {
	Enseignant untel;
	UE uml, java;
		
	@BeforeEach
	public void setUp() {
		untel = new Enseignant("untel", "untel@gmail.com");
		uml = new UE("UML");
		java = new UE("Programmation en java");		
	}
	

	@Test
	public void testNouvelEnseignantSansService() {
		assertEquals(0, untel.heuresPrevues(),
                        "Un nouvel enseignant doit avoir 0 heures prévues.");
	}

	@Test
	void nouvelEnseignantSansUE() {
		assertTrue( untel.UEPrevues().isEmpty() );
	}

	@Test
	void nouvelEnseignantSousService() {
		assertTrue( untel.enSousService() );
	}

	@Test
	void heuresPlanifiees() {
		assertEquals(0, untel.heuresPlanifiee());
		untel.ajouteEnseignement(uml, 0, 10, 0);
		Intervention inter = new Intervention(1, new Date(), untel, uml, TypeIntervention.TD, null);
		untel.ajoutIntervention(inter);
		assertEquals(1, untel.heuresPlanifiee());
	}

	@Test
	public void testAjoutH() {
		untel.ajouteEnseignement(uml, 0, 10, 0);
		assertEquals(10, untel.heuresPrevuesPourUE(uml),
                        "L'enseignant doit avoir 10 heures prévues pour l'UE");
                untel.ajouteEnseignement(uml, 0, 20, 0);
		assertEquals(10 + 20, untel.heuresPrevuesPourUE(uml),
                         "L'enseignant doit avoir 30 heures prévues pour l'UE");
		
	}

	@Test
	void ajoutHNegativesImpossible() {
		try {
			untel.ajouteEnseignement(uml, 0, -1, 0);
			fail("La valeur négative doit être refusée");
		} catch (IllegalArgumentException ex) {
		}
	}


	@Test
	void ajoutIntervResteAPlanif() {
		untel.ajouteEnseignement(uml, 0, 10, 0);
		assertEquals(10, untel.resteAPlanifier(uml, TypeIntervention.TD));
		Intervention inter = new Intervention(1, new Date(), untel, uml, TypeIntervention.TD, null);
		untel.ajoutIntervention(inter);
		assertEquals(9, untel.resteAPlanifier(uml, TypeIntervention.TD));
	}
	@Test
	void ajoutIntervNecessiteServicePrevuUE() {
		untel.ajouteEnseignement(uml, 0, 10, 0);
		Intervention inter = new Intervention(1, new Date(), untel, java, TypeIntervention.TD, null);
		try {
			untel.ajoutIntervention(inter);
			fail("L'enseignant n'a pas de service prévu dans cette UE");
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	void calculCorrectementService() {
		untel.ajouteEnseignement(uml, 0, 10, 0);
		untel.ajouteEnseignement(uml, 20, 0, 0);
		untel.ajouteEnseignement(java, 0, 0, 20);

		int expected = 10 + 30 + 15;
		assertEquals(expected, untel.heuresPrevues());
	}

}
