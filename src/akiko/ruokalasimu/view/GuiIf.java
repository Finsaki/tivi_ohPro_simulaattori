package akiko.ruokalasimu.view;

/**
* GuiIf rajapintaa käytetään osana MVC-ohjelmistoarkkitehtuurin toteutusta. Main luokka toteuttaa GuIf rajapinnan ja Main luokkaa
* voidaan kutsua GuiIf rajapinnan kautta.
*
* @author Aki Koppinen
* @version 1.1 (18.10.2020)
*/
public interface GuiIf {
	
	// Kontrolleri tarvitsee syötteitä, jotka se välittää Moottorille
	
	/**
	 * Metodi hakee simulointiaika syötteen käyttöliittymästä.
	 * @return simulaatiolle syötteenä annettu loppumisaika.
	 */
	public double getAika();
	/**
	 * Metodi hakee viive syötteen käyttöliittymästä.
	 * @return simulaatiolle syötteenä annettu viive.
	 */
	public long getViive();
	
	/**
	 * Metodi hakee asiakassaapumis määrän syötteen käyttöliittymästä.
	 * @return simulaatiolle syötteenä annettu asiakassaapumis määrä.
	 */
	public double getAsiakasSaapuminen();
	/**
	 * Metodi hakee tiedon onko Kahvila käytössä käyttöliittymän syötteestä.
	 * @return simulaatiolle syötteenä annettu tieto Kahvilan tilasta.
	 */
	public boolean getKahvilaStatus();
	/**
	 * Metodi hakee tiedon onko Kassa 2 käytössä käyttöliittymän syötteestä.
	 * @return simulaatiolle syötteenä annettu tieto Kassa 2 tilasta.
	 */
	public boolean getKassa2Status();
	/**
	 * Metodi hakee Ruokalan kapasiteetin syötteen käyttöliittymästä.
	 * @return simulaatiolle syötteenä Ruokalan kapasiteetti.
	 */
	public int getRuokalanKapasiteetti();
	
	//Kontrolleri antaa käyttöliittymälle tuloksia, joita Moottori tuottaa
	
	/**
	 * @param aika Simulaation loppumisaika.
	 */
	public void setLoppuaika(double aika);
	/**
	 * @return Visualisointi luokan olion joka toimii graafisena simulaation esityksenä.
	 */
	public Visualisointi getVisualisointi();
	
	/**
	 * @param maara Saapuneiden asiakkaiden määrä.
	 */
	public void setSaapuneetAsiakkaat(int maara);
	
	/**
	 * @param maara Palveltujen asiakkaiden määrä.
	 */
	public void setPalvellutAsiakkaat(int maara);
	/**
	 * @param teho Suoritusteho.
	 */
	public void setSuoritusteho(double teho);
	/**
	 * @param aika Keskimääräinen läpimenoaika asiakkaille.
	 */
	public void setKeskimLapiMenoAika(double aika);
	/**
	 * @param ajat Asiakkaiden läpimenoajat yhteensä.
	 */
	public void setLapiMenoAjatYht(long ajat);
	/**
	 * @param pituus Kaikille palvelupisteille yhteinen keskimääräinen jonon pituus.
	 */
	public void setKeskimJononPituus(double pituus);
	
	/**
	 * @param teksti Linjaston 1 tulostiedot tekstimuodossa.
	 */
	public void setPP1(String teksti);
	/**
	 * @param teksti Linjaston 2 tulostiedot tekstimuodossa.
	 */
	public void setPP2(String teksti);
	/**
	 * @param teksti Linjaston 3 tulostiedot tekstimuodossa.
	 */
	public void setPP3(String teksti);
	/**
	 * @param teksti Kahvilan tulostiedot tekstimuodossa.
	 */
	public void setPP4(String teksti);
	/**
	 * @param teksti Kassa 1 tulostiedot tekstimuodossa.
	 */
	public void setPP5(String teksti);
	/**
	 * @param teksti Kassa 2 tulostiedot tekstimuodossa.
	 */
	public void setPP6(String teksti);
	/**
	 * @param teksti Ruokalan tulostiedot tekstimuodossa.
	 */
	public void setPP7(String teksti);
	
	/**
	 * @param value Simulaation edistymispalkin edistyminen.
	 */
	public void setProgressBar(double value);
	
	/**
	 * @param value Simulaation viiveen määrä.
	 */
	public void setViiveNakyma(double value);
	
	/**
	 * Tieto siitä että simulaatio on päättynyt.
	 */
	public void setSimulointiStatusPaattynyt();
	
	/**
	 * @param asiakkaat Tietokannan asiakastiedot tekstimuodossa.
	 */
	public void setAsiakasText(String[] asiakkaat);
	/**
	 * @param palvelupisteet Tietokannan palvelupisteiden tiedot tekstimuodossa.
	 */
	public void setPalvelupisteText(String[] palvelupisteet);

}
