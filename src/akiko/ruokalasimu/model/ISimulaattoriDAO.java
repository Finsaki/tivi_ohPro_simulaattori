package akiko.ruokalasimu.model;

/**
 * ISimulaattoriDAO luokka tarjoaa Rajapinnan SimulaattoriDAO luokalle.
 * Moottori kutsuu SimulaattoriDAO luokkaa tämän rajapinnan kautta.
 * 
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
public interface ISimulaattoriDAO {
	
	/**
	 * Uuden palvelupisteen luominen tietokantaan.
	 * @param palvelupiste Luotava Palvelupiste-olio.
	 * @return Tieto metodin onnistumisesta.
	 */
	public boolean createPalvelupiste(Palvelupiste palvelupiste);
	/**
	 * Uuden Asiakkaan luominen tietokantaan.
	 * @param asiakas Luotava Asiakas-olio.
	 * @return Tieto metodin onnistumisesta.
	 */
	public boolean createAsiakas(Asiakas asiakas);
	/**
	 * @return Kaikki palvelupisteet taulukko muodossa tietokannasta.
	 */
	public Palvelupiste[] readPalvelupisteet();
	/**
	 * @return Kaikki asiakkaat taulukko muodossa tietokannasta.
	 */
	public Asiakas[] readAsiakkaat();
	/**
	 * Tietyn asiakkaan päivittämiseen tarvittava metodi.
	 * @param asiakas Päivitettävä asiakas.
	 * @return Tieto metodin onnistumisesta.
	 */
	public boolean updateAsiakas(Asiakas asiakas);
	/**
	 * Tietyn asiakkaan päivittämiseen tarvittava metodi.
	 * @param palvelupiste Päivitettävä palvelupiste.
	 * @return Tieto metodin onnistumisesta.
	 */
	public boolean updatePalvelupiste(Palvelupiste palvelupiste);
	/**
	 * @return Tieto metodin onnistumisesta.
	 */
	public boolean deleteAsiakkaat();
	/**
	 * Poistaa kaikki Asiakastiedot tietokannasta.
	 * @return Tieto metodin onnistumisesta.
	 */
	public boolean deletePalvelupisteet();
	/**
	 * Poistaa kaikki Palvelupiste tiedot tietokannasta.
	 * Tee viimeistely tietokanta yhteyden sulkemiseksi.
	 */
	public void finalize();

}
