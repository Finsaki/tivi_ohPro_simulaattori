package akiko.ruokalasimu.model;

/**
 * MoottoriIf luokka on rajapinta jonka Moottori luokka toteuttaa.
 * Kontrolleri kutsuu Moottori luokkaa tämän rajapinnan kautta.
 * 
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
public interface MoottoriIf {
		
	// Kontrolleri käyttää tätä rajapintaa
	
	/**
	 * Asetetaan käyttöliittymästä haettu simulointiaika Moottorin omaan muuttujaan.
	 * @param aika Simulaation kesto
	 */
	public void setSimulointiaika(double aika);
	/**
	 * Asetetaan käyttöliittymästä haettu viive Moottorin omaan muuttujaan.
	 * @param aika Simulaation viive
	 */
	public void setViive(long aika);
	/**
	 * Haetaan moottorista nykyinen viive Kontrolleriin.
	 * Kontrollerissa sitä käyttävät hidasta ja nopeuta metodit jotka välittävät laskutoimituksen käyttöliittymälle.
	 * @return Simulaation viive.
	 */
	public long getViive();
	
	/**
	 * Tämän metodin kutsu saa simuloinnin keskeytymään.
	 */
	public void lopeta();
	/**
	 * Valmistele metodilla tehdään ne Moottorin valmistelumetodit joihin tarvitaan jo olemassa oleva ja luotu Moottori olio.
	 * Valmistele metodissa alustetaan vaaditut palvelupisteet, tapahtumalista sekä ensimmäinen saapumisprosessi.
	 * Vasta konstruktorin kutsun ja valmistele() metodin jälkeen kutsutaan start komentoa Moottorin säikeelle.
	 */
	public void valmistele();
	
	/**
	 * Asetetaan käyttäjän käyttöliittymässä syöttämä arvo muuttujaan Moottorissa.
	 * @param asiakasSaapumiset luku joka on laskettu käyttäjän antamasta saapuvien asiakkaiden määrästä ja saapumisväliajasta.
	 */
	public void setAsiakasSaapumiset(double asiakasSaapumiset);
	/**
	 * Asetetaan käyttäjän käyttöliittymässä syöttämä arvo muuttujaan Moottorissa.
	 * @param kahvilaKaytossa arvo joka kertoo onko kahvila käytössä.
	 */
	public void setKahvilaStatus(boolean kahvilaKaytossa);
	/**
	 * Asetetaan käyttäjän käyttöliittymässä syöttämä arvo muuttujaan Moottorissa.
	 * @param kassa2Kaytossa arvo joka kertoo onko Kassa 2 käytössä.
	 */
	public void setKassa2Status(boolean kassa2Kaytossa);
	/**
	 * Asetetaan käyttäjän käyttöliittymässä syöttämä arvo muuttujaan Moottorissa.
	 * @param ruokalanKapasiteetti Ruokalan kapasiteetti.
	 */
	public void setRuokalanKapasiteetti(int ruokalanKapasiteetti);
	
	/**
	 * Luodaan simuloinnin jälkeen saaduista tuloksista Asiakas ja Palvelupiste tietokanta data tietokantaan.
	 */
	public void luoTietokannanData();
	/**
	 * @return Haetaan tietokannasta Asiakas tietokanta tiedot Tekstitaulukkona.
	 */
	public String[] getAsiakasTietokanta();
	/**
	 * @return Haetaan tietokannasta Palvelupiste tietokanta tiedot Tekstitaulukkona.
	 */
	public String[] getPalvelupisteTietokanta();
	/**
	 * Tyhjennetään kaikki Asiakastiedot tietokannasta.
	 */
	public void poistaAsiakasTietokanta();
	/**
	 * Tyhjennetään kaikki Palvelupistetiedot tietokannasta.
	 */
	public void poistaPalvelupisteTietokanta();
	
}
