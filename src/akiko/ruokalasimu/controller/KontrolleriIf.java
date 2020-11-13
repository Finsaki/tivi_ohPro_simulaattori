package akiko.ruokalasimu.controller;


/**
 * Kontrollerin rajapinta jonka Kontrolleri toteuttaa. Kontrolleria voidaan kutsua tämän rajapinnan kautta Moottorissa ja Main-luokassa.
 * 
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
public interface KontrolleriIf {
	
	/*----------------------------------------------
	 * Rajapinta, joka tarjotaan  käyttöliittymälle:
	 -----------------------------------------------*/
	
	/**
	 * Metodi joka välittää simulaation käynnistämiskäskyn käyttöliittymästä moottorille.
	 */
	public void kaynnistaSimulointi();
	/**
	 * Metodi joka välittää simulaation keskeytyskäskyn käyttöliittymästä moottorille.
	 */
	public void lopeta();
	/**
	 * Metodi joka välittää tietokannan tallennus käskyn käyttöliittymästä moottorille.
	 */
	public void tallennaTietokantaan();
	/**
	 * Metodi joka välittää tietokannan tyhjennys käskyn käyttöliittymästä moottorille.
	 */
	public void poistaTietokannat();
	
	/*-------------------------------
	 * Rajapinta jota käyttää sekä moottori että käyttöliittymä
	 -------------------------------*/
	
	/**
	 * Metodi joka välittää simulaation nopeutuskäskyn käyttöliittymästä moottorille.
	 * Metodi myös muuttaa viiven esitystä pienemmäksi käyttöliittymässä.
	 */
	public void nopeuta();
	/**
	 * Metodi joka välittää simulaation hidastuskäskyn käyttöliittymästä moottorille.
	 * Metodi myös muuttaa viiven esitystä suuremmaksi käyttöliittymässä.
	 */
	public void hidasta();
	/**
	 * Metodi joka välittää Tietokannan asiakkaiden tiedonhaku käskyn käyttöliittymästä moottorille.
	 * Moottori palauttaa Tietokannan esityksen tekstimuodossa käyttöliittymään.
	 */
	public void naytaAsiakasTietokanta();
	/**
	 * Metodi joka välittää Tietokannan palvelupisteiden tiedonhaku käskyn käyttöliittymästä moottorille.
	 * Moottori palauttaa Tietokannan esityksen tekstimuodossa käyttöliittymään.
	 */
	public void naytaPalvelupisteTietokanta();
	
	/*---------------------------------------
	 * Rajapinta, joka tarjotaan moottorille:
	 ---------------------------------------*/
	
	/**
	 * Metodi joka välittää simulaation lopetusajan moottorista käyttöliittymälle.
	 * @param aika Simulaation loppumisaika.
	 */
	public void naytaLoppuaika(double aika);
	/**
	 * Metodi joka välittää palvelupisteen ID:n ja jonon silloisen pituuden moottorista käyttöliittymälle.
	 * Käyttöliittymä käyttää näitä tietoja asiakkaan visualiseen lisäämiseen jonossa.
	 * @param jononPituus Jonon silloinen pituus palvelupisteessä.
	 * @param ppId Palvelupisteen ID.
	 */
	public void visualisoiAsiakas(int jononPituus, int ppId);
	/**
	 * Metodi joka välittää palvelupisteen ID:n ja jonon silloisen pituuden moottorista käyttöliittymälle.
	 * Käyttöliittymä käyttää näitä tietoja asiakkaan visuaaliseen poistoon jonosta.
	 * @param jononPituus Jonon silloinen pituus palvelupisteessä.
	 * @param ppId Palvelupisteen ID.
	 */
	public void visualisoiAsiakasPoisto(int jononPituus, int ppId);
	
	/**
	 * Metodi joka välittää simulaation saapuneitten asiakkaiden määrän moottorista käyttöliittymälle.
	 * @param maara Saapuneiden asiakkaiden määrä.
	 */
	public void naytaSaapuneetAsiakkaat(int maara);
	/**
	 * Metodi joka välittää simulaation palveltujen asiakkaiden määrän moottorista käyttöliittymälle.
	 * @param maara Palveltujen asiakkaiden määrä.
	 */
	public void naytaPalvellutAsiakkaat(int maara);
	/**
	 * Metodi joka välittää simulaation suoritustehon moottorista käyttöliittymälle.
	 * @param teho Koko simuloitavan ruokalan suoritusteho.
	 */
	public void naytaSuoritusteho(double teho);
	/**
	 * Metodi joka välittää simulaation läpi kulkeneiden asiakkaiden keskimääräisen läpimenoajan moottorista käyttöliittymälle.
	 * @param aika Asiakkaiden keskimääräinen läpimenoaika.
	 */
	public void naytaKeskimLapiMenoAika(double aika);
	/**
	 * Metodi joka välittää simulaation läpi kulkeneiden asiakkaiden kaikki läpimenoajat yhteensä moottorista käyttöliittymälle.
	 * @param ajat Kaikki asiakkaiden läpimenoajat yhteensä.
	 */
	public void naytaLapiMenoAjatYht(long ajat);
	/**
	 * Metodi joka välittää simulaation kaikkien palvelupisteiden keskimääräisen jononpituuden moottorista käyttöliittymälle.
	 * @param pituus Palvelupisteiden keskimääräinen jononpituus.
	 */
	public void naytaKeskimJononPituus(double pituus);
	
	/**
	 * Metodi joka välittää palvelupisteiden tulostiedot tekstitaulukkona moottorista käyttöliittymälle.
	 * @param ppTiedot Palvelupisteiden tulostiedot tekstitaulukkona.
	 */
	public void naytaPalvelupisteidenTiedot(String[] ppTiedot);
	/**
	 * Metodi joka välittää simulaation edistymisen moottorista käyttöliittymälle.
	 * @param aika Simulaation edistyminen välillä 0-1.0.
	 */
	public void naytaEdistyminen(double aika);

}
