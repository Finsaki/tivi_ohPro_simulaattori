package akiko.ruokalasimu.model;


/**
 * Olio joka kuvaa simulaatiossa tapahtuvia Asiakkaiden saapumisia, siirtymisiä ja poistumisia palvelupisteillä.
 * 
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
public class Tapahtuma implements Comparable<Tapahtuma> {
	
		
	/**
	 * TapahtumanTyyppi enum olio joka kertoo minkälainen tapahtuma on kyseessä.
	 */
	private TapahtumanTyyppi tyyppi;
	/**
	 * Tapahtuman saama tapahtuma-aika.
	 */
	private double aika;
	
	/**
	 * Tapahtuma-olion konstruktori jossa asetetaan sen tyyppi ja tapahtuma-aika.
	 * 
	 * @param tyyppi Tapahtuman tyyppi.
	 * @param aika Tapahtuman tapahtuma-aika.
	 */
	public Tapahtuma(TapahtumanTyyppi tyyppi, double aika){
		this.tyyppi = tyyppi;
		this.aika = aika;
	}
	
	/**
	 * @param tyyppi Haluttu TapahtumanTyyppi tyyppi.
	 */
	public void setTyyppi(TapahtumanTyyppi tyyppi) {
		this.tyyppi = tyyppi;
	}
	/**
	 * @return TapahtumanTyyppi-olio.
	 */
	public TapahtumanTyyppi getTyyppi() {
		return tyyppi;
	}
	/**
	 * @param aika Tapahtuma-aika.
	 */
	public void setAika(double aika) {
		this.aika = aika;
	}
	/**
	 * @return Tapahtuma-aika.
	 */
	public double getAika() {
		return aika;
	}

	/**
	 * Verrataan keskenään kahta Tapahtumaa niitten tapahtuma-ajan perusteella. Tätä tarvitaan Tapahtumalista-olion pitämässä
	 * PriorityQueue listassa joka toimii tärkeysjärjestys periaatteella.
	 */
	@Override
	public int compareTo(Tapahtuma arg) {
		if (this.aika < arg.aika) return -1;
		else if (this.aika > arg.aika) return 1;
		return 0;
	}
	
}
