package akiko.ruokalasimu.model;

import java.util.ArrayList;
import java.util.Random;

import akiko.ruokalasimu.controller.KontrolleriIf;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;

/**
 * Moottori luokka sisältää kaiken ohjelmiston laskutoimituksiin ja toiminnallisuuteen liittyvän koodin MVC mallin muokaisesti.
 * Moottori toteuttaa MoottoriIf rajapinnan jonka kautta Moottoria voi kutsua.
 * Moottori käyttää toiminnallisuuksien hoitamisessa hyväksi muita model pakkauksen olio luokkia.
 * Moottori myös käyttää Tietokanta toimintoja ISimulaattoriDAO rajapinnan toteuttavan SimulaattoriDAO luokan kautta.
 * 
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
public class Moottori extends Thread implements MoottoriIf{
	
	/**
	 * Rajapinta jonka avulla hoidetaan yhteys käyttöliittymään.
	 */
	private KontrolleriIf kontrolleri;
	/**
	 * Rajapinta jonka avulla hoidetaan yhteys tietokantaan.
	 */
	private ISimulaattoriDAO simuDAO;
	
	/**
	 * Käyttöliittymästä haettu simuloinnin kesto.
	 * Asetetaan tässä defaulttina nollaksi.
	 */
	private double simulointiaika = 0;
	/**
	 * Simuloinnin viive.
	 */
	private long viive = 0;
	
	/**
	 * Simulaation palvelupisteet taulukkona.
	 */
	private Palvelupiste[] palvelupisteet;
	/**
	 * Kello-olio joka pitää yllä nykyistä aikaa simulaation edetessä.
	 */
	private Kello kello;

	/**
	 * Olio joka luo uusia Tapahtumia
	 */
	private Saapumisprosessi saapumisprosessi;
	/**
	 * Olio jonne uusia Tapahtumia talletetaan ja käydään läpi simulaation edetessä.
	 * Läpikäydyt Tapahtumat poistetaan listalta.
	 */
	private Tapahtumalista tapahtumalista;
	
	/**
	 * Viimeisin palvelun loppuun vienyt asiakas.
	 * Käytetään muuttujana josta saadaan simuloinnin päätteeksi haettua kaikki Asiakas-olioissa inkrementoituneet muuttujat.
	 */
	private Asiakas viimeisinAsiakas;
	/**
	 * Lista palvelun loppuun vieneistä asiakkaista.
	 * Listan avulla luodaan Asikastietokanta simulaation päätyttyä jos käyttäjä näin valitsee.
	 */
	private ArrayList<Asiakas> asiakkaatTK;
	/**
	 * Random muuttuja jota käytetään satunnaisen kassan valitsemiseen tilanteessa jolloin kassojen jonot ovat yhtä pitkät.
	 */
	private Random rand = new Random();
	
	/**
	 * Muuttuja joka kertoo true ilmaisulla että käyttäjä on halunnut keskeyttää simuloinnin.
	 */
	private volatile boolean exit = false;
	
	/**
	 * Käyttöliittymästä haettu arvo joka kertoo asiakasmäärän suhteessa aikaan.
	 */
	private double asiakasSaapumiset;
	/**
	 * Käyttöliittymästä haettu arvo joka kertoo onko Kahvila käytössä vai ei.
	 */
	private boolean kahvilaStatus;
	/**
	 * Käyttöliittymästä haettu arvo joka kertoo onko Kassa 2 käytössä vai ei.
	 */
	private boolean kassa2Status;
	/**
	 * Käyttöliittymästä haettu arvo joka kertoo ruokalan kapasiteetin.
	 */
	private int ruokalanKapasiteetti;
	
	/*--------------------
	 *	Simulaatio moottorin luominen
	 ----------------------*/
	
	/**
	 * Moottorin konstruktori jossa annetaan arvo useimmille olio-muuttujille.
	 * Loppujen muuttujien arvo asetetaan valmistele() metodissa.
	 * 
	 * @param kontrolleri Kontrolleri yhdistää käyttöliittymän ja Moottorin MVC arkkitehtuurin mukaisesti.
	 */
	public Moottori(KontrolleriIf kontrolleri){
		this.kontrolleri = kontrolleri;
		this.simuDAO = new SimulaattoriDAO();
		this.asiakkaatTK = new ArrayList<>();
		this.palvelupisteet = new Palvelupiste[7];
		this.kello = Kello.getInstance(); // Otetaan kello muuttujaan yksinkertaistamaan koodia
		this.viimeisinAsiakas = new Asiakas();
		this.viimeisinAsiakas.nollaa(); //palautetaan asiakas arvot nollaan
	}
	
	/*-----------------------------
	 * 	Simulaatio moottorin valmisteleminen
	 ------------------------------*/
	
	/**	{@inheritDoc} */
	@Override	
	public void valmistele() {
		/*
		 * viimeinen parametri kertoo kapasiteetin.
		 * Jos antaa 0 kapasiteetin, poistuu palvelupiste kaytosta
		 */
		int kahvila = 1;
		int kassa2 = 1;
		if (this.kahvilaStatus) {
			kahvila = 0;
		}
		if (this.kassa2Status) {
			kassa2 = 0;
		}

		palvelupisteet[0]=new Palvelupiste(new Normal(5,4), this, "Linjasto 1", 1, TapahtumanTyyppi.DEPVALINTA1, 1); //ruokalinjasto1	
		palvelupisteet[1]=new Palvelupiste(new Normal(5,4), this, "Linjasto 2", 2, TapahtumanTyyppi.DEPVALINTA2, 1); //ruokalinjasto2
		palvelupisteet[2]=new Palvelupiste(new Normal(12,6), this, "Linjasto 3", 3, TapahtumanTyyppi.DEPVALINTA3, 1); //ruokalinjasto3
		palvelupisteet[3]=new Palvelupiste(new Normal(4,3), this, "Kahvila", 4, TapahtumanTyyppi.DEP1, kahvila); //kahvila	
		palvelupisteet[4]=new Palvelupiste(new Normal(3,2), this, "Kassa 1", 5, TapahtumanTyyppi.DEP2, 1); //kassa1
		palvelupisteet[5]=new Palvelupiste(new Normal(3,2), this, "Kassa 2", 6, TapahtumanTyyppi.DEP3, kassa2); //kassa2
		palvelupisteet[6]=new Palvelupiste(new Normal(20,10), this, "Ruokala", 7, TapahtumanTyyppi.DEPFINAL, this.ruokalanKapasiteetti); //ruokala
		
		kello.setAika(0);
		saapumisprosessi = new Saapumisprosessi(this, new Negexp(this.asiakasSaapumiset,5)); //Tata muuttamalla voitaisiin vaikuttaa asiakkaiden saapumismaaraan, HUOM! Ei vielä implementoitu
		tapahtumalista = new Tapahtumalista();	
		saapumisprosessi.generoiSeuraava(); // Ensimmainen saapuminen!!
	}
	
	/*---------------------------
	 * 	Simulaation suorittaminen
	 ----------------------------*/
	
	/**
	 * Simulaation suorittamisen aloittaminen ja loppuun vienti.
	 * Simulaatiota ajetaan säikeessä joka mahdollistaa että käyttäjä voi antaa muita komentoja simulaation suorituksen aikana, 
	 * ja että tietoja saadaan vietyä käyttöliittymään reaaliajassa.
	 * 
	 * Toiminnallisesti Moottori toistaa A, B ja C vaiheita, kunnes listatut tapahtumat loppuvat, tai ohjelma kohtaa ennalta määrätyn lopetusajan.
	 */
	@Override
	public void run(){  // ent. aja
		while (simuloidaan() && !exit){
			viive();
			kello.setAika(nykyaika());
			suoritaBTapahtumat();
			yritaCTapahtumat();
			try {
				kontrolleri.naytaEdistyminen(nykyaika()/simulointiaika); //HUOM! tata voisi ehka pistaa toimimaan portaittain ettei kuluttaisi niin paljon resursseja
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		tulokset();
		setViive(0);
	}
	
	/**
	 * Metodia kutsutaan run metodin sisältä.
	 * Metodissa verrataan seuraavaksi tapahtumalistalla olevan tapahtuman kellonaikaa nykyiseen kellonaikaan.
	 * Jos aika on sama, suoritetaan kyseinen tapahtuma.
	 * Tätä jatketaan kunnes aika ei enää täsmää.
	 */
	private void suoritaBTapahtumat(){
		while (tapahtumalista.getSeuraavanAika() == kello.getAika()){
			suoritaTapahtuma(tapahtumalista.poista());
		}
	}

	/**
	 * Tämä metodi aloittaa palvelun niille palvelupisteille joille se on mahdollista sillä hetkellä.
	 * 
	 * Metodin while silmukkaa toteuttaa kapasiteetin toiminnan seuraavan laisesti:
	 * 
	 * palvelun annetaan alkaa vain jos:
	 * 	1. kapasiteettia on vapaana.
	 * 	2. kaikki jonottajat eivat ole jo palveltavana (simulaatiossa palveltavat katsotaan osaksi jonoa ja poistetaan jonosta vasta palvelun paatteeksi).
	 * 	3. joku on jonossa.
	 */
	private void yritaCTapahtumat(){
		for (Palvelupiste p: palvelupisteet){
			while (!p.kapasiteettiTaynna() && !p.kaikkiJonottajatPalveltavana() && p.onJonossa()){
				p.aloitaPalvelu();
			}
		}
	}

	/**
	 * Keskitetty algoritmi tapahtumien kasittelyyn ja asiakkaiden siirtymisten hallintaan.
	 * 
	 * Metodi suorittaa sille annetun tapahtuman kyseisen tapahtuman tyypin mukaan.
	 * Saapumistapahtumat luovat uuden asiakkaan palvelupisteelle ja generoivat uuden tapahtuman tapahtumalistalle.
	 * Poistumistapahtumat poistavat asiakkaan palvelupisteeltä ja siirtävät seuraavalle palvelupisteelle.
	 * Jos palvelupiste josta poistuttiin oli viimeinen, palvelu päättyy kyseiselle asiakkaalle.
	 * @param t Suoritettava Tapahtuma.
	 */
	private void suoritaTapahtuma(Tapahtuma t){
		
		switch (t.getTyyppi()){
			
			case ARR1: //ruokalinjastolle 1 saapuminen ja uuden satunnaisen saapumisen luominen
				saapuminen(0, new Asiakas());
				saapumisprosessi.generoiSeuraava();
				break;
			case ARR2: //ruokalinjastolle 2 saapuminen ja uuden satunnaisen saapumisen luominen
				saapuminen(1, new Asiakas());
				saapumisprosessi.generoiSeuraava();
		       	break;
			case ARR3: //ruokalinjastolle 3 saapuminen ja uuden satunnaisen saapumisen luominen
				saapuminen(2, new Asiakas());
				saapumisprosessi.generoiSeuraava();
				break;
			case ARR4: //kahvilaan saapuminen ja uuden satunnaisen saapumisen luominen
				saapuminen(3, new Asiakas());
				saapumisprosessi.generoiSeuraava();
				break;
			case DEPVALINTA1: //ruokalinjastolta 1 poistuminen ja siirtyminen valitulle kassalle.
				Asiakas a0 = poistuminen(0);
				Palvelupiste uusiPiste1 = suoritaKassanValinta(a0);
				saapuminen(uusiPiste1.getId()-1, a0);
				break;
			case DEPVALINTA2: //ruokalinjastolta 2 poistuminen ja siirtyminen valitulle kassalle.
				Asiakas a1 = poistuminen(1);
				Palvelupiste uusiPiste2 = suoritaKassanValinta(a1);
				saapuminen(uusiPiste2.getId()-1, a1);
				break;
			case DEPVALINTA3:	//ruokalinjastolta 3 poistuminen ja siirtyminen valitulle kassalle.
				Asiakas a2 = poistuminen(2);
				Palvelupiste uusiPiste3 = suoritaKassanValinta(a2);
				saapuminen(uusiPiste3.getId()-1, a2);
				break;
			case DEP1:	//kahvilasta poistuminen ja siirtyminen ruokalaan
				Asiakas a3 = poistuminen(3);
				saapuminen(6, a3);
				break;
			case DEP2: //kassalta 1 poistuminen ja siirtyminen ruokalaan
				Asiakas a4 = poistuminen(4);
				saapuminen(6, a4);
	   	   		break;
			case DEP3: //kassalta 2 poistuminen ja siirtyminen ruokalaan
				Asiakas a5 = poistuminen(5);
				saapuminen(6, a5);
				break;
			case DEPFINAL:	//ruokalasta poistuminen ja asiakkaan palvelun päättyminen
				Asiakas a6 = poistuminen(6);
				a6.setPoistumisaika(kello.getAika());
				viimeisinAsiakas = a6;
				asiakkaatTK.add(viimeisinAsiakas); //yksinkertaistan ja talletan vain palvelun loppuun päässeet asiakkaat
				a6.raportti(); //naytetaan vain jos INFO-Trace paalla
		}
	}

	/*----------------
	 * 	Public metodit
	 -----------------*/
	
	/**
	 * @return Viimeisin palvelun loppuunvienyt asiakas.
	 */
	public Asiakas getViimeisinAsiakas() {
		return this.viimeisinAsiakas;
	}

	/**
	 * Lisätään uusi tapahtuma tapahtumalistalle.
	 * Kutsutaan Palvelupiste luokan aloitaPalvelu metodista.
	 * @param uusi Tapahtuma.
	 */
	public void uusiTapahtuma(Tapahtuma t){
		tapahtumalista.lisaa(t);
	}
	
	/*-----------------
	 * 	Private metodit
	 ------------------*/

	/**
	 * Metodia kutsutaan jotta voidaan siirtyä suoraan seuraavaan tapahtumaan ilman odottelua.
	 * @return Seuraavan suoritettavan tapahtuman aika.
	 */
	private double nykyaika(){
		return tapahtumalista.getSeuraavanAika();
	}
	
	/**
	 * @return Tieto siitä onko simulointi vielä käynnissä.
	 */
	private boolean simuloidaan(){
		Trace.out(Trace.Level.INFO, "Kello on: " + kello.getAika());
		return kello.getAika() < simulointiaika;
	}
	
	/**
	 * Metodi suoritetaan simulaation päätyttyä.
	 * Lasketaan saadun datan avulla tulosteet.
	 * Saadut tulosteet viedään kontrollerin kautta käyttöliittymään käyttäjän nähtäväksi.
	 */
	private void tulokset(){
		Trace.out(Trace.Level.INFO, "Simulointi paattyi kello " + kello.getAika());
		
		//muuttujien laskenta
		double aika = 1.0*kello.getAika();
		int sMaara = viimeisinAsiakas.getA();
		int pMaara = viimeisinAsiakas.getC();
		double teho = 1.0*pMaara/aika;
		long lpajat = viimeisinAsiakas.getW();
		double lpkaika = 1.0*lpajat/pMaara;
		double pituus = 1.0*lpajat/aika;
		
		//palvelupisteiden omakohtaisten tietojen tallettaminen String muodossa taulukkoon
		String[] ppTiedot = luoPalvelupisteidenTiedot();
		
		//muuttujien vienti käyttöliittymään
		kontrolleri.naytaLoppuaika(aika);
		kontrolleri.naytaSaapuneetAsiakkaat(sMaara);
		kontrolleri.naytaPalvellutAsiakkaat(pMaara);
		kontrolleri.naytaSuoritusteho(teho);
		kontrolleri.naytaKeskimLapiMenoAika(lpkaika);
		kontrolleri.naytaLapiMenoAjatYht(lpajat);
		kontrolleri.naytaKeskimJononPituus(pituus);
		
		//taulukko viedään kontrollerille, kontrolleri jakaa sen osiin ja asettaa arvot käyttöliittymässä
		kontrolleri.naytaPalvelupisteidenTiedot(ppTiedot);
	}
	
	/**
	 * Tämä metodi luo tekstitaulukon joka sisältää erikseen jokaisen palvelupisteen tulostetiedot.
	 * Metodi myös laskee kyseiset tulostiedot Moottorin muuttujien avulla.
	 * @return Palvelupisteiden tulostiedot tekstitaulukossa.
	 */
	private String[] luoPalvelupisteidenTiedot() {
		String[] ppInfoLista = new String[7];
		String ppInfo;
		int i = 0;
		for (Palvelupiste pPiste: this.palvelupisteet) {
			int ci = pPiste.getCi(); //palvelupisteen omakohtainen palveltujen asiakkaitten määrä
			double b = pPiste.getB(); //tässä b arvossa kapasiteetti on huomioitu
			double u = b/kello.getAika(); // vanha laskutoimitus: b/(pPiste.getKapasiteetti() * kello.getAika());
			double s = pPiste.getB2()/ci; //tähän tarvitaan b2-arvoa jossa kapasiteettia ei ole huomioitu
			ppInfo = (pPiste.getNimi() + "\n\nPalvelupisten aktiivinen aika: " +
					String.format("%.2f", b) + "\nPalvelupisteen käyttöaste: " + String.format("%.2f", u) +
					"\nPalvelupisteen keskimääräinen palveluaika: " + String.format("%.2f", s)) + "\n";
			ppInfoLista[i] = ppInfo;
			i++;
		}
		return ppInfoLista;
	}
	
	/**
	 * Metodia kutsutaan Moottori säikeen run metodista.
	 * Metodin kutsuminen saa säikeen odottamaan viive muuttujan silloisen arvon verran.
	 * Näin voidaan seurata hidastettuna ohjelman suoritusta.
	 */
	private void viive() {
		Trace.out(Trace.Level.INFO, "Viive " + viive);
		try {
			sleep(viive);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodia kutsutaan suoritaTapahtuma() metodista.
	 * Metodilla viedään palvelupisteelle saapuva asiakas palveltavaksi kyseiselle palvelupisteelle.
	 * Jos palvelupisteen kapasiteetti on täyttynyt, viedään myös asiakkaan visuaalinen esitys käyttöliittymän Visualisoinnille.
	 * Visualisoinnissa jonottava asiakas ilmestyy ruudulle sinisenä pallona.
	 * Metodissa myös varmistetaan ettei saapuminen tapahdu Kahvilaan jos se ei ole käytössä.
	 * @param saapumisNro Palvelupisteen indeksi Palvelupiste-taulukossa.
	 * @param a Asiakas joka saapuu.
	 */
	private void saapuminen(int saapumisNro, Asiakas a) {
		if (saapumisNro == 3 && kahvilaStatus) {
			Trace.out(Trace.Level.INFO, "Kahvila pois käytöstä, asiakas ei saanut palvelua");
		} else {
			if (palvelupisteet[saapumisNro].kapasiteettiTaynna()) { //jos kapasiteetti on täyttynyt lisätään asiakas visuaalisesti palvelupisteen jonoon, palveltavana olevia ei visualisoida
				kontrolleri.visualisoiAsiakas(palvelupisteet[saapumisNro].getJonottajienMaara(), palvelupisteet[saapumisNro].getId());
			}
			palvelupisteet[saapumisNro].lisaaJonoon(a); //lisätään asiakas käytännössä uudelle palvelupisteelle
		}
	}
	
	/**
	 * Metodia kutsutaan suoritaTapahtuma() metodista.
	 * Metodilla poistetaan palvelupisteellä palvelun päättänyt asiakas palvelupisteeltä.
	 * Jos palvelupisteellä on jonoa, poistetaan myös yksi asiakas käyttöliittymän visuaalisesta jonosta.
	 * Näin saadaan visualisoinnissa näyttämään siltä että seuraava asiakas on siirtynyt palveltavaksi.
	 * @param poistumisNro Palvelupisteen indeksi Palvelupiste-taulukossa.
	 * @return Asiakas joka poistui.
	 */
	private Asiakas poistuminen(int poistumisNro) {
		if (!palvelupisteet[poistumisNro].kaikkiJonottajatPalveltavana()) { //jos jonoa on olemassa poistetaan jonossa ensimmäisenä (mutta todellisuudessa viimeisenä) oleva asiakas
			kontrolleri.visualisoiAsiakasPoisto(palvelupisteet[poistumisNro].getJonottajienMaara(), palvelupisteet[poistumisNro].getId());
		}
		Asiakas a = palvelupisteet[poistumisNro].otaJonosta(); //poistetaan asiakas palvelupisteeltä käytännössä
		return a;
	}
	
	/**
	 * Metodia kutsutaan suoritaTapahtuma() metodista jos asiakas poistuu Linjastolta 1, 2 tai 3.
	 * Asiakas joutuu tällöin valitsemaan kassan.
	 * Asiakas valitsee aina kassan jossa on lyhyempi jono, paitsi jos kassa 2 ei ole käytössä.
	 * jos Kassa 2 ei ole käytössä, asiakas valitsee aina Kassan 1.
	 * @param a Valinnan suorittava asiakas.
	 * @return palvelupiste (eli kassa) johon asiakas siirtyy.
	 */
	private Palvelupiste suoritaKassanValinta(Asiakas a) { //parametri taitaa olla turha, poista
		//asiakas valitsee aina lyhyemman jonon, paitsi jos kassa 2 ei ole käytössä
		if (kassa2Status) {
			Trace.out(Trace.Level.INFO, "Kassa 2 pois käytöstä, asiakas ohjattu kassalle 1");
			return palvelupisteet[4];
		} else if (palvelupisteet[5].getJonottajienMaara() < palvelupisteet[4].getJonottajienMaara()) {
			return palvelupisteet[5];
		} else if (palvelupisteet[5].getJonottajienMaara() > palvelupisteet[4].getJonottajienMaara()) {
			return palvelupisteet[4];
		} else { //jos palvelupisteiden jonot ovat yhta pitkat, valitse toinen randomilla
			if (rand.nextInt(2) < 1) {
				return palvelupisteet[4];
			} else {
				return palvelupisteet[5];
			}
		}
	}
	
	/*------------------------------------------------------
	 * 	Moottorin rajapintametodit (kutsutaan kontrollerista)
	 --------------------------------------------------------*/
	
	/**	{@inheritDoc} */
	@Override
	public void lopeta() {
		exit = true;
	}
	
	/**	{@inheritDoc} */
	@Override
	public void setAsiakasSaapumiset(double asiakasSaapumiset) {
		this.asiakasSaapumiset = asiakasSaapumiset;
	}
	
	/**	{@inheritDoc} */
	@Override
	public void setKahvilaStatus(boolean kahvilaKaytossa) {
		this.kahvilaStatus = kahvilaKaytossa;
	}
	
	/**	{@inheritDoc} */
	@Override
	public void setKassa2Status(boolean kassa2Kaytossa) {
		this.kassa2Status = kassa2Kaytossa;
	}

	/**	{@inheritDoc} */
	@Override
	public void setRuokalanKapasiteetti(int ruokalanKapasiteetti) {
		this.ruokalanKapasiteetti = ruokalanKapasiteetti;
	}

	/**	{@inheritDoc} */
	@Override
	public void setSimulointiaika(double aika) {
		simulointiaika = aika;
	}

	/**	{@inheritDoc} */
	@Override
	public void setViive(long viive) {
		this.viive = viive;
	}
	
	/**	{@inheritDoc} */
	@Override
	public long getViive() {
		return viive;
	}
	
	/**	{@inheritDoc} */
	@Override
	public void luoTietokannanData() {
		for (int i = 0; i < palvelupisteet.length; i++) {
			simuDAO.createPalvelupiste(palvelupisteet[i]);
		}
		for (int i = 0; i < asiakkaatTK.size(); i++) {
			simuDAO.createAsiakas(asiakkaatTK.get(i));
		}
	}
	
	/**	{@inheritDoc} */
	@Override
	public String[] getAsiakasTietokanta() {
		Asiakas[] asiakkaat =  simuDAO.readAsiakkaat();
		String[] vaihtoehdot = new String[asiakkaat.length];
		for (int i = 0; i < vaihtoehdot.length; i++) {
			vaihtoehdot[i] = asiakkaat[i].toString();
		}
		return vaihtoehdot;
	}
	
	/**	{@inheritDoc} */
	@Override
	public String[] getPalvelupisteTietokanta() {
		Palvelupiste[] palvelupisteet = simuDAO.readPalvelupisteet();
		String[] vaihtoehdot = new String[palvelupisteet.length];
		for (int i = 0; i < vaihtoehdot.length; i++) {
			vaihtoehdot[i] = palvelupisteet[i].toString();
		}
		return vaihtoehdot;
	}
	
	/**	{@inheritDoc} */
	@Override
	public void poistaAsiakasTietokanta() {
		simuDAO.deleteAsiakkaat();
	}
	
	/**	{@inheritDoc} */
	@Override
	public void poistaPalvelupisteTietokanta() {
		simuDAO.deletePalvelupisteet();
	}
}