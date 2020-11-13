package akiko.ruokalasimu.model;

/**
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
public class Kello {

	/**
	 * Kellon aika joka kasvaa ohjelman edetessä.
	 */
	private double aika;
	/**
	 * Kellon staattinen instanssi joka on kaikille sitä kutsuville olioille sama.
	 */
	private static Kello instanssi;
	
	/**
	 * Kellon konstruktori jossa kellonaika asetetaan nollaan.
	 */
	private Kello(){
		aika = 0;
	}
	
	/**
	 * Palautetaan Kello instanssi.
	 * Jos instanssia ei ole, luodaan uusi.
	 * @return Kellon staattinen instanssi.
	 */
	public static Kello getInstance(){
		if (instanssi == null){
			instanssi = new Kello();	
		}
		return instanssi;
	}
	
	/**
	 * @param aika Kellon uusi aika.
	 */
	public void setAika(double aika){
		this.aika = aika;
	}

	/**
	 * @return Kellon nykyinen aika.
	 */
	public double getAika(){
		return aika;
	}
}
