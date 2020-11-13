package akiko.ruokalasimu.model;

import javax.persistence.*;

/**
 * Asiakas luokka luo ja pitää yllä tietyn palveltavan asiakkaan tietoja.
 * Asiakas luokka sisältää myös Tietokannan ylläpitämiseksi tarvittavat annotaatiot Tietokanta tietueille.
 * 
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
@Entity
@Table(name="asiakas")
public class Asiakas {
	
	/**
	 * Asiakkaan ID numero.
	 */
	@Id
	@Column(name ="id")
	private int id;
	/**
	 * Asiakkaan saapumisaika.
	 */
	@Column(name ="saapumisaika")
	private double saapumisaika;
	/**
	 * Asiakkaan poistumisaika.
	 */
	@Column(name ="poistumisaika")
	private double poistumisaika;
	/**
	 * Asiakkaan läpimenoaika.
	 */
	@Column(name ="lapimenoaika")
	private double ri = -1; //Ri: Palvelupisteen läpimeno aika yhdelle asiakkaalle
	/**
	 * Saapuvien asiakkaiden kokonaismäärä.
	 * Tämä muuttuja päivittyy jokaisen uuden asiakkaan luonnin yhteydessä.
	 * Muuttujaa ei viedä tietokantaan.
	 */
	@Transient
	private static int A = 1; //A: Saapuvien asiakkaiden kokonaismäärä
	/**
	 * Palveltujen asiakkaiden kokonaismäärä.
	 * Tämä muuttuja päivittyy jokaisen asiakkaan palvelun päätyttyä.
	 * Muuttujaa ei viedä tietokantaan.
	 */
	@Transient
	private static int C = 0; //C: Palveltujen asiakkaiden määrä
	/**
	 * Kaikkien asiakkaiden läpimenoaikojen summa.
	 * Tähän muuttujaan lisätään jokaisen asiakkaan yksittäinen läpimenoaika palvelun loputtua.
	 * Muuttujaa ei viedä tietokantaan.
	 */
	@Transient
	private static long W = 0; //W: Kaikkien asiakkaiden läpimeno aikojen summa
	
	/**
	 * Asiakas luokan konstruktori.
	 * ID kasvaa joka kerta kun asiakas luodaan.
	 * Saapumisaika asetetaan nykyisen Kello-luokan ajan mukaan.
	 */
	public Asiakas(){
	    id = A++;
		saapumisaika = Kello.getInstance().getAika();
		Trace.out(Trace.Level.INFO, "Uusi asiakas:" + id + ":"+saapumisaika);
	}

	/**
	 * @return Poistumisaika.
	 */
	public double getPoistumisaika() {
		return poistumisaika;
	}

	/**
	 * @param poistumisaika Poistumisaika.
	 */
	public void setPoistumisaika(double poistumisaika) {
		this.poistumisaika = poistumisaika;
		C ++;
		this.ri = (this.poistumisaika - this.saapumisaika);
		W += this.ri;
	}

	/**
	 * @return Saapumisaika.
	 */
	public double getSaapumisaika() {
		return saapumisaika;
	}

	/**
	 * @param saapumisaika Saapumisaika.
	 */
	public void setSaapumisaika(double saapumisaika) {
		this.saapumisaika = saapumisaika;
	}
	
	/**
	 * Raportti Asikas tapahtumista jota voidaan kutsua jon INFO level on asetettu.
	 */
	public void raportti(){
		Trace.out(Trace.Level.INFO, "Asiakas "+id+ " saapui:" +saapumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " poistui:" +poistumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " viipyi:" +(poistumisaika-saapumisaika));
		
		//System.out.printf("%-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %5d", "As: ", id, " PAika: ", String.format("%.2f", ri), " PAjat: ", W, " SAs: ", A, " PAs: ", C);
		//Loppuja ei tarvita, tee laskut getterien avulla
	}
	
	//Getterit laskimelle

	/**
	 * @return Palvelupisteen läpimenoaika.
	 */
	public double getRi() {
		return ri;
	}

	/**
	 * @return Saapuvien asiakkaiden kokonaismäärä.
	 */
	public int getA() {
		return A;
	}

	/**
	 * @return Palveltujen asiakkaiden kokonaismäärä.
	 */
	public int getC() {
		return C;
	}

	/**
	 * @return Kaikkien asiakkaiden läpimenoaikojen summa.
	 */
	public long getW() {
		return W;
	}
	
	/**
	 * @return Asiakkaan ID
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Nollaa static muuttujat.
	 * Tätä metodia kutsutaan kun halutaan tehdä uusi simulointi ettei kasvateta jo kasvatettuja lukuja.
	 */
	public void nollaa() {
		A = 1;
		C = 0;
		W = 0;
	}
	
	//Tämä vaatisi parannusta, on vähän tökerö asettelu menetelmä HUOM!
	/**
	 *Tällä metodilla rakennetaan Asiakastietokannan tekstinäkymän asettelu helpommin luettavaksi.
	 */
	public String toString() {
		//return String.format("%-10s %-30s %-28s %-1s", this.id, String.format("%.2f",this.saapumisaika), String.format("%.2f",this.poistumisaika), String.format("%.2f",this.ri));
		return this.id + "\t\t\t" + String.format("%.3f",this.saapumisaika) + "\t\t\t" + String.format("%.3f",this.poistumisaika) + "\t\t\t" + String.format("%.3f",this.ri); //ehkä helpompi lukea csv muodossa?
	}

}
