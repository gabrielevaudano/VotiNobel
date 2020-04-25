package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	private List<Esame> esami;
	private Double miglioreMedia;
	private Set<Esame> miglioreSoluzione;
	
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
		
	}

	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		this.miglioreMedia = 0.0;
		this.miglioreSoluzione = null;
		Set<Esame> parziale = new HashSet<>();
		
		cerca(parziale, 0, numeroCrediti);
		
		return miglioreSoluzione;
	}

	private void cerca(Set<Esame> parziale, Integer livello, Integer m) {
		// casi terminali
		Integer crediti = sommaCrediti(parziale);
		if (crediti > m)
			return;
		
		if (crediti == m) {
			Double media = calcolaMedia(parziale);
			
			if (media > miglioreMedia) {
				miglioreSoluzione = new HashSet<Esame>(parziale);
				miglioreMedia = media;
			}
		}
		
		// sicuramente crediti < m
		// considero caso ultimo, sicuramente non valido perchè crediti < m
		if (livello == esami.size())
			return;
		
		
		
		// generazione dei sotto problemi
		// Esami [L] è da aggiungere o no? Provo entrambe le cose.
		
		// La lista iniziale di esami deve essere lista -> per modello ordine degli esami
		
		// provo ad aggiungerlo
		parziale.add(esami.get(livello));
		cerca(parziale,(livello+1), m);
		
		parziale.remove(esami.get(livello));
		
		//provo a non aggiungerlo
		cerca(parziale, livello + 1, m);
	}
	

	private double calcolaMedia(Set<Esame> parziale) {
		int crediti = 0;
		int somma = 0;
		
		for (Esame e : parziale) {
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma / crediti;
	}

	private Integer sommaCrediti(Set<Esame> parziale) {
		int somma = 0;
		
		for (Esame e : parziale)
			somma += e.getCrediti();
		return somma;	
	}
}
