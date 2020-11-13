package akiko.ruokalasimu.controller;

import javafx.application.Platform;
import akiko.ruokalasimu.model.*;
import akiko.ruokalasimu.view.*;

/**
 * Kontrolleri luokka yhdistää käyttöliittymän ja toiminnallisuuden MVC mallin mukaisesti.
 * Käyttöliittymää ja toiminnallisuutta kutsutaan niiden rajapintojen kautta.
 * 
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
public class Kontrolleri implements KontrolleriIf {

	/**
	 * MoottoriIf rajapinnan toteuttava ohjelman toiminnallisuudesta vastaava luokka, 
	 * jonka Kontrolleri yhdistää GuIf rajapinnan kautta käyttöliittymään.
	 */
	private MoottoriIf moottori;
	/**
	 * GuiIf rajapinnan toteuttava ohjelman käyttöliittymästä vastaava luokka, 
	 * jonka Kontrolleri yhdistää MoottoriIf rajapinnan kautta ohjelman toiminnallisuuteen.
	 */
	private GuiIf gui;
	
	/**
	 * Kontrolleri luokan konstruktori.
	 * @param gui käyttöliittymä
	 */
	public Kontrolleri(GuiIf gui) {
		this.gui = gui; //Käyttöliittymä
	}
	
	/*-------------------------------------------------
	 *	Moottorin luominen ja simulaation käynnistäminen
	 --------------------------------------------------*/
	
	/**	{@inheritDoc} */
	@Override
	public void kaynnistaSimulointi() {
		moottori = new Moottori(this); // luodaan uusi moottorisäie jokaista simulointia varten
		moottori.setSimulointiaika(gui.getAika());
		moottori.setViive(gui.getViive());
		moottori.setAsiakasSaapumiset(gui.getAsiakasSaapuminen());
		moottori.setKahvilaStatus(gui.getKahvilaStatus());
		moottori.setKassa2Status(gui.getKassa2Status());
		moottori.setRuokalanKapasiteetti(gui.getRuokalanKapasiteetti());
		moottori.valmistele();
		gui.getVisualisointi().tyhjennaNaytto();
		((Thread)moottori).start(); // Koska gui:n päivitykset tulevat moottorisäikeestä, ne pitää ohjata JavaFX-säikeeseen -- Platform.runLater...
	}
	
	/*-------------------------------
	 * Moottorille vietävät toiminnot
	 -------------------------------*/
	
	/**	{@inheritDoc} */
	@Override
	public void lopeta() {
		Platform.runLater(()->moottori.lopeta()); //keskeyttää simulaation suorituksen
	}
	
	/**	{@inheritDoc} */
	@Override
	public void tallennaTietokantaan() {
		Platform.runLater(()-> {
			moottori.luoTietokannanData();
		});
	}
	
	/**	{@inheritDoc} */
	@Override
	public void poistaTietokannat() {
		Platform.runLater(()-> {
			moottori.poistaAsiakasTietokanta();
			moottori.poistaPalvelupisteTietokanta();
		});
	}
	
	/*-------------------
	 *	Hybridi toiminnot
	 ---------------------*/
	
	/**	{@inheritDoc} */
	@Override
	public void hidasta() { // hidastetaan moottorisäiettä
		Platform.runLater(new Runnable(){ 
			public void run() {
				if (moottori.getViive() != 0) {
					double value = moottori.getViive()*1.10 + 1; //+1 lisäys auttaa ohjelman toimivuudessa kun kyseessä on pienet luvut
					moottori.setViive((long)(value));
					gui.setViiveNakyma(value);
				}
			}
		});
	}
	
	/**	{@inheritDoc} */
	@Override
	public void nopeuta() { // nopeutetaan moottorisäiettä
		Platform.runLater(new Runnable(){
			public void run() {
				double value = moottori.getViive()*0.9 - 1; //-1 lisäys auttaa ohjelman toimivuudessa kun kyseessä on pienet luvut
				if (value < 0) {
					value = 0;
				}
				moottori.setViive((long)(value));
				gui.setViiveNakyma(value);
			}
		});
	}
	
	/**	{@inheritDoc} */
	@Override
	public void naytaAsiakasTietokanta() {
		Platform.runLater(()-> {
			String[] asiakasTiedot = moottori.getAsiakasTietokanta();
			gui.setAsiakasText(asiakasTiedot);
		});
		
		
	}
	
	/**	{@inheritDoc} */
	@Override
	public void naytaPalvelupisteTietokanta() {
		Platform.runLater(()-> {
			String[] palvelupisteTiedot = moottori.getPalvelupisteTietokanta();
			gui.setPalvelupisteText(palvelupisteTiedot);
		});
		
	}
	
	/*--------------------------------------------------
	 *	Käyttöliittymälle vietävät toiminnot
	 ----------------------------------------------------*/
	
	/**	{@inheritDoc} */
	@Override
	public void naytaEdistyminen(double aika) {
		Platform.runLater(()->gui.setProgressBar(aika)); //vie edistysmittarille arvoja joitten avulla nähdään simulaation ajan kuluminen
	}
	
	/**	{@inheritDoc} */
	@Override
	public void naytaLoppuaika(double aika) {
		Platform.runLater(()-> {
			gui.setLoppuaika(aika);
			gui.setSimulointiStatusPaattynyt();
		});
	}
	
	/**	{@inheritDoc} */
	@Override
	public void naytaSaapuneetAsiakkaat(int maara) {
		Platform.runLater(()->gui.setSaapuneetAsiakkaat(maara)); 
		
	}
	
	/**	{@inheritDoc} */
	@Override
	public void naytaPalvellutAsiakkaat(int maara) {
		Platform.runLater(()->gui.setPalvellutAsiakkaat(maara));
		
	}
	
	/**	{@inheritDoc} */
	@Override
	public void naytaSuoritusteho(double teho) {
		Platform.runLater(()->gui.setSuoritusteho(teho));
		
	}
	
	/**	{@inheritDoc} */
	@Override
	public void naytaKeskimLapiMenoAika(double aika) {
		Platform.runLater(()->gui.setKeskimLapiMenoAika(aika));
		
	}
	
	/**	{@inheritDoc} */
	@Override
	public void naytaLapiMenoAjatYht(long ajat) {
		Platform.runLater(()->gui.setLapiMenoAjatYht(ajat));
		
	}
	
	/**	{@inheritDoc} */
	@Override
	public void naytaKeskimJononPituus(double pituus) {
		Platform.runLater(()->gui.setKeskimJononPituus(pituus));
		
	}
	
	/**	{@inheritDoc} */
	@Override
	public void naytaPalvelupisteidenTiedot(String[] pPisteet) { //viedään String muotoiset palvelupisteiden tiedot TabPane luokan tabeille
		Platform.runLater(new Runnable(){
			public void run(){
				for (int i = 0; i < 7; i++) {
					if (pPisteet[i].startsWith("Linjasto 1")) {
						gui.setPP1(pPisteet[i]);
					} else if (pPisteet[i].startsWith("Linjasto 2")) {
						gui.setPP2(pPisteet[i]);
					} else if (pPisteet[i].startsWith("Linjasto 3")) {
						gui.setPP3(pPisteet[i]);
					} else if (pPisteet[i].startsWith("Kahvila")) {
						gui.setPP4(pPisteet[i]);
					} else if (pPisteet[i].startsWith("Kassa 1")) {
						gui.setPP5(pPisteet[i]);
					} else if (pPisteet[i].startsWith("Kassa 2")) {
						gui.setPP6(pPisteet[i]);
					} else if (pPisteet[i].startsWith("Ruokala")) {
						gui.setPP7(pPisteet[i]);
					} else {
						System.out.println("Palvelupisteen tietoja ei löytynyt");
					}
				}
			}
		});
	}
	
	/**	{@inheritDoc} */
	@Override
	public void visualisoiAsiakas(int jononPituus, int ppId) {
		Platform.runLater(new Runnable(){
			public void run(){
				
				switch(ppId) {
				case 1: 
					gui.getVisualisointi().lisaaPp1Asiakas(jononPituus);
					break;
				case 2:
					gui.getVisualisointi().lisaaPp2Asiakas(jononPituus);
					break;
				case 3:
					gui.getVisualisointi().lisaaPp3Asiakas(jononPituus);
					break;
				case 4:
					gui.getVisualisointi().lisaaPp4Asiakas(jononPituus);
					break;
				case 5:
					gui.getVisualisointi().lisaaPp5Asiakas(jononPituus);
					break;
				case 6:
					gui.getVisualisointi().lisaaPp6Asiakas(jononPituus);
					break;
				case 7:
					gui.getVisualisointi().lisaaPp7Asiakas(jononPituus);
					break;
				default:
					System.out.println("Asiakkaan visualisointi ei onnistunut");
				}
				
			}
		});
	}
	
	/**	{@inheritDoc} */
	@Override
	public void visualisoiAsiakasPoisto(int jononPituus, int ppId) {
		Platform.runLater(new Runnable(){
			public void run(){
				
				switch(ppId) {
				case 1: 
					gui.getVisualisointi().poistaPp1Asiakas(jononPituus);
					break;
				case 2:
					gui.getVisualisointi().poistaPp2Asiakas(jononPituus);
					break;
				case 3:
					gui.getVisualisointi().poistaPp3Asiakas(jononPituus);
					break;
				case 4:
					gui.getVisualisointi().poistaPp4Asiakas(jononPituus);
					break;
				case 5:
					gui.getVisualisointi().poistaPp5Asiakas(jononPituus);
					break;
				case 6:
					gui.getVisualisointi().poistaPp6Asiakas(jononPituus);
					break;
				case 7:
					gui.getVisualisointi().poistaPp7Asiakas(jononPituus);
					break;
				default:
					System.out.println("Asiakkaan poiston visualisointi ei onnistunut");
				}
				
			}
		});
	}
	
}
