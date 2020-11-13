package akiko.ruokalasimu.model;

import java.util.PriorityQueue;

/**
 * Tapahtumalista kerää kaikki Tapahtuma-oliot listalle ja pitää niitä tärkeys järjestyksessä.
 * 
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
public class Tapahtumalista {
	/**
	 * Tärkeysjärjestys periaatteella toimiva PriorityQueue lista.
	 * Tärkeyden määrää Tapahtuma-oliossa annettu aika muuttuja jota vertaillaan Tapahtuma olioiden kesken compareTo metodilla.
	 */
	private PriorityQueue<Tapahtuma> lista = new PriorityQueue<Tapahtuma>();
	
	/**
	 * Tapahtumalista luokan tyhjä konstruktori.
	 */
	public Tapahtumalista(){
	 
	}
	
	/**
	 * Poistetaan tärkeysperiaatteen mukaisesti seuraavana vanhin listalla oleva Tapahtuma-olio.
	 * @return Poistettu Tapahtuma-olio.
	 */
	public Tapahtuma poista(){
		Trace.out(Trace.Level.INFO,"Poisto " + lista.peek());
		return lista.remove();
	}
	
	/**
	 * Lisätään Tapahtumalistalle uusi Tapahtuma-olio.
	 * @param Lisättävä Tapahtuma-olio.
	 */
	public void lisaa(Tapahtuma t){
		lista.add(t);
	}
	
	/**
	 * Haetaan seuraavaksi tärkeydeltään seuraavaksi jonossa olevan Tapahtuman sille asetettu tapahtumisaika.
	 * @return Tapahtuman tapahtuma-aika.
	 */
	public double getSeuraavanAika(){
		return lista.peek().getAika();
	}
	
}
