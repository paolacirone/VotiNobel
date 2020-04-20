package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> esami; 
	private double bestMedia= 0.0; 
	private Set<Esame> bestSoluzione= null; 
	
	public Model() {
		EsameDAO dao = new EsameDAO(); 
		this.esami = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		Set<Esame> parziale = new HashSet<>();
		this.cerca(parziale, 0, numeroCrediti);
		
		return this.bestSoluzione;
	}
	
	//Il set va bene perchè non ci interessa l'ordine, per l'ordine Lista
	private void cerca(Set<Esame> parziale, int livello, int m) {
		
		// casi terminali
		
		int crediti = sommaCrediti(parziale); 
		if(crediti>m)
			return; 
		
		if(crediti==m) {
			double media = calcolaMedia(parziale);
			if(media> bestMedia) {
				bestSoluzione =  new HashSet<>(parziale);
				bestMedia= media;
			}
		}
		
		//sicuramente crediti <m
		if(livello==esami.size())
			return; 
		//generare sottoproblemi 
		// esame(L) è da aggiungere o no? Provo entrambe le cose 
		
		//provo ad aggiungerlo
		parziale.add(esami.get(livello)); 
		cerca(parziale, livello+1, m); 
		parziale.remove(esami.get(livello)); //backtracking
		
		//provo a non aggingerlo 
		cerca(parziale, livello+1, m);
	}

	public double calcolaMedia(Set<Esame> parziale) {
		int crediti = 0; 
		int somma=0; 
		
		for (Esame e: parziale) {
			crediti += e.getCrediti(); 
			somma+= (e.getVoto()*e.getCrediti());
			
		}
		return somma/crediti;
	}

	private int sommaCrediti(Set<Esame> parziale) {
		int somma = 0; 
		for (Esame e: parziale) {
			somma+= e.getCrediti();
		}
		return somma;
	}
	
	
	//APPROCCIO 2---TROPPO LENTO

private void cerca2(Set<Esame> parziale, int livello, int m) {
		
		// casi terminali
		
		int crediti = sommaCrediti(parziale); 
		if(crediti>m)
			return; 
		
		if(crediti==m) {
			double media = calcolaMedia(parziale);
			if(media> bestMedia) {
				bestSoluzione =  new HashSet<>(parziale);
				bestMedia= m;
			}
		}
		
		//sicuramente crediti <m
		if(livello==esami.size())
			return; 
		//generare sottoproblemi 
		
		for(Esame e: esami) {
			if(!parziale.contains(e)) {
			parziale.add(e);
			cerca2(parziale, livello+1, m);
			parziale.remove(e);
			}
		}
	}
}
