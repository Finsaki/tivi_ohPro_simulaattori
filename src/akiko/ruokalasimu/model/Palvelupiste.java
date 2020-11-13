package akiko.ruokalasimu.model;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.persistence.*;

import eduni.distributions.ContinuousGenerator;

/**
 * Palvelupiste luokka luo ja pitää yllä tietyn palvelupisteen tietoja.
 * Palvelupiste luokka sisältää myös Tietokannan ylläpitämiseksi tarvittavat annotaatiot Tietokanta tietueille.
 * 
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
@Entity
@Table(name="palvelupiste")
public class Palvelupiste {
	
	/**
	 * Palvelupisteen ID.
	 */
	@Id
	@Column(name ="id")
	private int id;
	
	/**
	 * Palvelupisteen nimi.
	 */
	@Column(name ="nimi")
	private String nimi;

	/**
	 * Palvelupisteen aktiivinen aika.
	 */
	@Column(name ="aktiivinenAika")
	private double b = 0; //B: palvelupisteen aktiivinen aika //HUOM! B Muutettava vastaamaan kapasiteettia
	/**
	 * Palvelupisteelle saapuneitten asiakkaiden määrä.
	 */
	@Column(name ="saapuneetAsiakkaat")
	private int ai = 0; //palvelupisteen omakohtainen saapuneitten asiakkaitten määrä
	/**
	 * Palvelupisteellä palveltujen asiakkaiden määrä.
	 */
	@Column(name ="palvellutAsiakkaat")
	private int ci = 0; //palvelupisteen omakohtainen palveltujen asiakkaitten määrä
	/**
	 * Palvelupisteelle asetettu kapasiteetti, eli kuinka monta asiakasta voidaan palvella samanaikaisesti.
	 */
	@Column(name ="kapasiteetti")
	private int KAPASITEETTIMAX; //hibernate ei anna olla final?
	
	/**
	 * Tätä muuttujaa tarvittan vain palvelupisteen keskimääräisen palveluajan laskemiseen.
	 */
	@Transient
	private double b2 = 0;
	/**
	 * Moottori olio jolla generoidaan uusia Tapahtuma-olioita Palvelupisteelle.
	 */
	@Transient
	private Moottori moottori;
	/**
	 * Asiakasjono johon asiakkaat asetetaan palvelupisteelle saapuessa.
	 */
	@Transient
	private LinkedList<Asiakas> jono = new LinkedList<Asiakas>();
	/**
	 * eduni.distributions pakkauksesta haettu olio.
	 * Olio palauttaa sample() komennolla satunnaisen double luvun.
	 */
	@Transient
	private ContinuousGenerator generator;
	/**
	 * Jokaisella palvelupisteellä on oma TapahtumanTyyppi jonka se antaa siitä poistuville asikkaille.
	 */
	@Transient
	private TapahtumanTyyppi skeduloitavanTapahtumanTyyppi;
	/**
	 * palveltavat muuttuja pitää listaa kullakin hetkellä palveltavana olevista asiakkaista.
	 * Listan koko määräytyy kapasiteetin mukaan.
	 */
	@Transient
	private ArrayList<Asiakas> palveltavat = new ArrayList<>();
	
	/**
	 * tätä muuttujaa käytetään hyväksi vain oikean B-arvon laskemiseksi kapasiteetin kanssa.
	 */
	@Transient
	double muuttuvaLopetusaika = 0;
	/**
	 * tätä muuttujaa käytetään hyväksi vain oikean B-arvon laskemiseksi kapasiteetin kanssa.
	 */
	@Transient
	double lopetusaikaMaksimi = 0;
	
	/**
	 * Tämä parametriton konstruktori tarvitaan Hibernate ORM työkalua varten.
	 */
	public Palvelupiste() {
		super();
	}

	/**
	 * Palvelupiste olion konstruktori.
	 * 
	 * @param generator Satunnaisluku generaattori.
	 * @param moottori Moottori.
	 * @param nimi Nimi.
	 * @param id ID.
	 * @param tyyppi TapahtumanTyyppi.
	 * @param kapasiteetti Kapasiteetti.
	 */
	public Palvelupiste(ContinuousGenerator generator, Moottori moottori, String nimi, int id, TapahtumanTyyppi tyyppi, int kapasiteetti){
		super();
		this.moottori = moottori;
		this.generator = generator;
		this.nimi = nimi;
		this.id = id;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;
		this.KAPASITEETTIMAX = kapasiteetti;
	}

	/**
	 * Asiakas lisätään jonoon.
	 * @param a Jonoon lisättävä asiakas.
	 */
	public void lisaaJonoon(Asiakas a){   // Jonon 1. asiakas aina palvelussa
		jono.add(a);
		this.ai ++;
	}

	/**
	 * Asiakas poistetaan jonosta.
	 * @return Jonosta poistettava asiakas.
	 */
	public Asiakas otaJonosta(){  // Poistetaan palvelussa ollut
		Asiakas poistettavaAsiakas =  jono.poll();
		Trace.out(Trace.Level.INFO, "palveltavat ennen poistoa" + palveltavat);
		palveltavat.remove(0); //poistaa listalta aina indeksissä 0 olevan tiedon HUOM! katso add kohta aloitaPalvelu() metodissa
		Trace.out(Trace.Level.INFO, "palveltavat jälkeen poiston" + palveltavat);
		this.ci ++;
		return poistettavaAsiakas;
	}

	/**
	 * Aloitetaan palvelu seuraavaksi jonossa olevalle asiakkaalle.
	 * Asiakas saa satunnaisen palveluajan Moottorissa generaattorille annetuilla parametreillä.
	 * Moottorin kautta luodaan uusi tapahtuma jonka lopetusaika on nykyinen Kello-luokan aika + saatu satunnainen palveluaika.
	 * Moottori lisää tämän jälkeen itse Tapahtuman tapahtumalistalle.
	 */
	public void aloitaPalvelu(){  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
		Asiakas palveltava = this.jono.peek();
		double aikaNyt = Kello.getInstance().getAika();
		double palveluaika = generator.sample();
		b2 += palveluaika;
		double lopetusaika = aikaNyt + palveluaika;
		palveltavat.add(palveltava); //HUOM! tännehhän voisi lisätä oikeastaan mitä vain, koodin suoritus ei siitä muutu
		Trace.out(Trace.Level.INFO, "palveltaviin lisätty " + palveltava);
		
		/* HUOM! Tätä koodin osaa sopisi parantaa jos aikaa riittää, toimii kutakuinkin oletetusti mutta melkoisilla kommervenkeillä
		 * 
		 * Jos palveltavia on palvelupisteessä vain yksi, toimitaan normaalisti ja lisätään vain palveluaika b-muuttujaan eli palveluajat-yhteensä.
		 * Jos kuitenkin palveltavia on enemmän, joudutaan tekemään alla oleva laskuoperaatio jotta päällekkäisiä aikoja ei tallentuisi b-muuttujaan.
		 * Laskuoperaatio käyttää lopetusaika- ja muuttuvaLopetusaika muuttujia. lopetusaika on nykyisen asiakkaan palvelun lopetusaika,
		 * muuttuvaLopetusaika on edellisen asiakkaan lopetusaika. Kun näistä kahdesta otetaan erotus, saadaan selville kuinka paljon pitempään uusi asiakas
		 * viettää palveltavana kuin vanha. Tämä ylimenevä aika lisätäään b-muuttujaan, eikä päällekkäisiä aikoja pääse muodostumaan.
		 * 
		 * Jos lause varmistaa palveltavien määrän lisäksi myös ettei edellisten asiakkaiden lopetusajoista suurin olisi suurempi kuin nykyinen lopetusaika, jos näin olisi
		 * ei b-muuttujaan tule lisätä yhtään aikaa.
		 */
		
		if (lopetusaika > lopetusaikaMaksimi) {
			lopetusaikaMaksimi = lopetusaika;
		}
		
		if (this.palveltavat.size() > 1 && lopetusaika == lopetusaikaMaksimi) {
			double lopetusaikojenErotus = lopetusaika - muuttuvaLopetusaika;
			muuttuvaLopetusaika = lopetusaika;
			this.b += lopetusaikojenErotus;
		} else if (lopetusaika == lopetusaikaMaksimi) {
			muuttuvaLopetusaika = lopetusaika;
			this.b += palveluaika;
		} else {
			Trace.out(Trace.Level.INFO, "Lopetusaika uudelle tapahtumalle oli ajallisesti ennen nykyistä maksimi lopetusaikaa, b-arvoa ei koroteta");
		}
		
		moottori.uusiTapahtuma(new Tapahtuma(skeduloitavanTapahtumanTyyppi, aikaNyt + palveluaika));
	}

	/**
	 * @return Onko jonossa asiakkaita.
	 */
	public boolean onJonossa(){
		return jono.size() != 0;
	}
	
	/**
	 * @return Jonossa olevien asiakkaiden määrä.
	 */
	public int getJononPituus() {
		return this.jono.size(); //palautetaan jonon pituus, kaytossa Moottorin suoritaKassanValinta() -metodissa
	}
	
	/**
	 * @return Palvelupisteen aktiivinen aika.
	 */
	public double getB() {
		return this.b; //palautetaan B arvo
	}
	
	/**
	 * @return Palvelupisteen nimi
	 */
	public String getNimi() {
		return this.nimi;
	}
	
	/**
	 * @return Palvelupisteen ID
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * @return tarkistus onko asetettu kapasiteetti tayttynyt.
	 */
	public boolean kapasiteettiTaynna() { //tarkistetaan onko asetettu kapasiteetti tayttynyt
		return KAPASITEETTIMAX == this.palveltavat.size();
	}
	
	/**
	 * @return tarkistus ovatko kaikki palvelupisteella jonottavat jo palveltavana.
	 */
	public boolean kaikkiJonottajatPalveltavana() { //tarkistetaan ovatko kaikki palvelupisteella jonottavat jo palveltavana
		return this.palveltavat.size() == this.jono.size();
	}
	
	/**
	 * @return Saapuneiden asiakkaiden määrä palvelupisteelle.
	 */
	public int getAi() {
		return this.ai;
	}
	
	/**
	 * @return Palveltujen asiakkaiden määrä palvelupisteellä.
	 */
	public int getCi() {
		return this.ci;
	}
	
	/**
	 * @return Asetettu kapasiteetti palvelupisteelle.
	 */
	public int getKapasiteetti() {
		return this.KAPASITEETTIMAX;
	}
	
	/**
	 * @return Palveltavien määrä palvelupisteellä.
	 */
	public int getPalveltavienMaara() {
		return this.palveltavat.size();
	}
	
	/**
	 * @return Jonossa olevien määrä palvelupisteellä.
	 */
	public int getJonottajienMaara() {
		return this.jono.size() - this.palveltavat.size();
	}
	
	/**
	 * @return Kokonaispalveluaika, ei riipu kapasiteetista.
	 */
	public double getB2() {
		return this.b2;
	}
	
	//Tämä vaatisi parannusta, on vähän tökerö asettelu menetelmä HUOM!
	/**
	 *Tällä metodilla rakennetaan Palvelupiste tietokannan tekstinäkymän asettelu helpommin luettavaksi.
	 */
	public String toString() {
		//return String.format("%-10s %-14s %-30s %-22s %-22s %-1s", this.id, this.nimi, String.format("%.2f",this.b), this.ai, this.ci, this.KAPASITEETTIMAX);
		return this.id + "\t\t\t" + this.nimi + "\t\t\t" + String.format("%.3f",this.b) + "\t\t\t" + this.ai + "\t\t\t" + this.ci + "\t\t\t" + this.KAPASITEETTIMAX; //ehkä helpompi lukea csv muodossa?
	}

}
