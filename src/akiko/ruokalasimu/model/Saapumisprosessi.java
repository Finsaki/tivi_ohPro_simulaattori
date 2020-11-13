package akiko.ruokalasimu.model;

import java.util.Random;


import eduni.distributions.*;
/**
 * Saapumisprosessi generoi uusia Tapahtuma-olioita joitten tyypit ovat saapumisia aloituspalvelupisteille.
 * Tapahtuma saa satunnaisen TapahtumanTyypin riippuen luoSatunnainenSaapuminen luokan prosenttimäärittelystä.
 * Nämä uudet Tapahtumat asetetaan Moottorissa Tapahtumalistalle.
 * 
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
public class Saapumisprosessi {
	/**
	 * Ohjelman toiminnallisuudesta vastaava moottori luokka.
	 */
	private Moottori moottori;
	/**
	 * eduni.distributions pakkauksesta haettu olio.
	 * Olio palauttaa sample() komennolla satunnaisen double luvun.
	 */
	private ContinuousGenerator generaattori;
	/**
	 * Random olio jota käytetään satunnaisen aloituspisteen valintaan.
	 */
	private Random rand;
	
	/**
	 * Saapumisprosessi-olion konstruktori.
	 * @param m Moottori.
	 * @param g ContinuousGenerator.
	 */
	public Saapumisprosessi(Moottori m, ContinuousGenerator g){
		this.moottori = m;
		this.generaattori = g;
		this.rand = new Random();
	}

	/**
	 * Luo uuden Tapahtuma-olion jolle se asettaa satunnaisen alkamisajan nykyisen kellon ajan mukaan.
	 */
	public void generoiSeuraava(){
		TapahtumanTyyppi tyyppi = luoSatunnainenSaapuminen();
		Tapahtuma t = new Tapahtuma(tyyppi, Kello.getInstance().getAika()+generaattori.sample());
		moottori.uusiTapahtuma(t);
	}
	
	/**
	 * Metodia kutsutaan generoiSeuraava() metodin sisältä.
	 * Tämän metodin avulla lasketaan mistä palvelupisteestä asiakas aloittaa palvelunsa.
	 * Jokaiselle aloituspalvelupisteelle on laskettu tässä metodissa oma todennäköisyys jota valintatilanne noudattaa.
	 * @return valittu TapahtumanTyyppi.
	 */
	private TapahtumanTyyppi luoSatunnainenSaapuminen() {
		//Generoidaan satunnainen tapahtumaTyyppi käyttäen ennalta asetettuja saapumistodennakoisyyksia
		int tapahtuma = rand.nextInt(10);
		if (tapahtuma < 4) { 
			return TapahtumanTyyppi.ARR1; //40% perusruoka
		} else if (tapahtuma < 7) { 
			return TapahtumanTyyppi.ARR2; //30% kasvisruoka
		} else if (tapahtuma < 8) { 
			return TapahtumanTyyppi.ARR3; //10% erikoisruoka
		} else {
			return TapahtumanTyyppi.ARR4; //20% kahvila
		}
	}
}
