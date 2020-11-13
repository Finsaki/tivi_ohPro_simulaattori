package akiko.ruokalasimu.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
* Visualisointi luokka vastaa simulaation graafisen visualisoinnin toteutuksesta.
*
* @author Aki Koppinen
* @version 1.1 (18.10.2020)
*/
public class Visualisointi extends Canvas{
	
	/**
	 * Graafinen näkymä johon lisätään elementtejä.
	 */
	private GraphicsContext gc;
	
	/**
	 * Graafisen näkymän keskikohta.
	 */
	double keskiKohta;
	/**
	 * Palvelupisteen koko graafisessa näkymässä.
	 */
	double ppKoko;
	/**
	 * Asiakkaan koko graafisessa näkymässä.
	 */
	double asiakasKoko;
	/**
	 * Apumuuttuja graafisessa näkymässä, kertoo palvelupisteiden välisen tyhjän tilan määrän.
	 */
	double vali;
	/**
	 * Apumuuttuja asiakkaan viemiseen paikoilleen graafisessa näkymässä.
	 */
	double asiakasXSijaintiLisa;
	/**
	 * Apumuuttuja viivojen viemiseen paikoilleen graafisessa näkymässä.
	 */
	double viivaXSijaintiLisa;
	
	/**
	 * Palvelupisteen 1 sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp1XSijainti;
	/**
	 * Palvelupisteen 2 sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp2XSijainti;
	/**
	 * Palvelupisteen 3 sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp3XSijainti;
	/**
	 * Palvelupisteen 4 sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp4XSijainti;
	/**
	 * Palvelupisteen 5 sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp5XSijainti;
	/**
	 * Palvelupisteen 6 sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp6XSijainti;
	/**
	 * Palvelupisteen 7 sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp7XSijainti;
	
	/**
	 * Palvelupisteen 1 asiakkaan sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp1asiakasYSijainti;
	/**
	 * Palvelupisteen 2 asiakkaan sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp2asiakasYSijainti;
	/**
	 * Palvelupisteen 3 asiakkaan sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp3asiakasYSijainti;
	/**
	 * Palvelupisteen 4 asiakkaan sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp4asiakasYSijainti;
	/**
	 * Palvelupisteen 5 asiakkaan sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp5asiakasYSijainti;
	/**
	 * Palvelupisteen 6 asiakkaan sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp6asiakasYSijainti;
	/**
	 * Palvelupisteen 7 asiakkaan sijainti graafisen näkymän leveyden mukaan laskettuna.
	 */
	double pp7asiakasYSijainti;
	
	/*-----------------------------------------------------------------------------------------
	 * Luodaan Canvas alusta ja alustetaan sijaintimuuttujat annetun pituuden ja leveyden avulla.
	 * Näin ollen visualisoinnin toiminta pysyy samanlaisena annettiin kooksi mitä hyvänsä.
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * Tämä metodi luo Visualisointi-olion annetulla leveydellä ja korkeudella.
	 * Metodi myös laskee sen muuttujat annetun leveyden mukaan.
	 * Lopuksi metodi kutsuu tyhjennaNaytto metodia.
	 * @param w Visualisoinnin leveys.
	 * @param h Visualisoinnin korkeus.
	 */
	public Visualisointi(int w, int h) {
		super(w, h);
		this.ppKoko = w/10;
		this.vali = w/100;
		this.keskiKohta = w/2 - ppKoko/2;
		this.asiakasKoko = w/33;
		this.asiakasXSijaintiLisa = ppKoko/2 - asiakasKoko/2;
		this.viivaXSijaintiLisa = ppKoko/2 - vali/2;
		
		this.pp1XSijainti = keskiKohta - ppKoko*3 - vali*3;
		this.pp1asiakasYSijainti = ppKoko*3;
		this.pp2XSijainti = keskiKohta - ppKoko*2 - vali*2;
		this.pp2asiakasYSijainti = ppKoko*3;
		this.pp3XSijainti = keskiKohta - ppKoko - vali;
		this.pp3asiakasYSijainti = ppKoko*3;
		this.pp4XSijainti = keskiKohta + ppKoko*3 + vali*3;
		this.pp4asiakasYSijainti = ppKoko*2;
		this.pp5XSijainti = keskiKohta;
		this.pp5asiakasYSijainti = ppKoko*2;
		this.pp6XSijainti = keskiKohta + ppKoko + vali;
		this.pp6asiakasYSijainti = ppKoko*2;
		this.pp7XSijainti = keskiKohta + ppKoko*2 + vali*2;
		this.pp7asiakasYSijainti = ppKoko;
		
		gc = this.getGraphicsContext2D();
		tyhjennaNaytto();
	}
	
	/*-------------------------------------------
	 *	Visualisoinnin tyhjennys ja alustaminen
	 --------------------------------------------*/
	
	/**
	 * Tämä metodi tyhjentää graafisen näkymän ja kutsuu sen jälkeen luoPalvelupisteet() ja luoViivat() metodeja.
	 * Metodi ajetaan aina uuden Visualisoinnin käynnistyessä.
	 */
	public void tyhjennaNaytto() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
		luoPalvelupisteet();
		luoViivat();
	}
	
	/*--------------------------------------------------------------------
	 *	Luodaan neliömäiset palvelupisteet joihin asiakkaat voivat jonottaa
	 ----------------------------------------------------------------------*/
	
	/**
	 * Metodi luo graafiseen esitykseen laatikot jotka edustavat palvelupisteitä.
	 */
	public void luoPalvelupisteet() {
		gc.setFill(Color.ORANGE);
		gc.fillRect(pp5XSijainti, ppKoko, ppKoko, ppKoko);//Palvelupiste 5
		gc.fillRect(pp6XSijainti, ppKoko, ppKoko, ppKoko); //Palvelupiste 6
		gc.setFill(Color.BLACK);
		gc.fillRect(pp7XSijainti, 0, ppKoko, ppKoko); //Palvelupiste 7
		gc.setFill(Color.DARKORCHID);
		gc.fillRect(pp4XSijainti, ppKoko, ppKoko, ppKoko); //Palvelupiste 4
		gc.setFill(Color.SIENNA);
		gc.fillRect(pp3XSijainti, ppKoko*2, ppKoko, ppKoko); //Palvelupiste 3
		gc.fillRect(pp2XSijainti, ppKoko*2, ppKoko, ppKoko); //Palvelupiste 2
		gc.fillRect(pp1XSijainti, ppKoko*2, ppKoko, ppKoko); //Palvelupiste 1
	}
	
	/**
	 * Metodi luo graafiseen esitykseen viivat jotka edustavat asiakkaiden kulkureittejä simulaatiossa.
	 */
	public void luoViivat() {
		//lisätään aloitusnäkymään viivat jotka näyttävät asiakkaitten kulkureitin.
		gc.setFill(Color.BLUE);
		
		gc.fillRect(pp1XSijainti + viivaXSijaintiLisa, ppKoko*1.5, vali, ppKoko/2);
		gc.fillRect(pp2XSijainti + viivaXSijaintiLisa, ppKoko*1.5, vali, ppKoko/2);
		gc.fillRect(pp3XSijainti + viivaXSijaintiLisa, ppKoko*1.5, vali, ppKoko/2);
		gc.fillRect(pp1XSijainti + viivaXSijaintiLisa, ppKoko*1.5, ppKoko*3.5 - vali, vali);
		gc.fillRect(pp5XSijainti + ppKoko/2 + vali, ppKoko*1.5, ppKoko/5, vali);
		gc.fillRect(pp5XSijainti + ppKoko/2 + vali + ppKoko/3, ppKoko*1.5, ppKoko/5, vali);
		gc.fillRect(pp5XSijainti + ppKoko/2 + vali + ppKoko/3 + ppKoko/3, ppKoko*1.5, ppKoko/5, vali);
		
		gc.fillRect(pp5XSijainti + viivaXSijaintiLisa, ppKoko/2, vali, ppKoko);
		gc.fillRect(pp6XSijainti + viivaXSijaintiLisa, ppKoko/2 + vali*1.5, vali, ppKoko/5);
		gc.fillRect(pp6XSijainti + viivaXSijaintiLisa, ppKoko/2 + vali*1.5 + ppKoko/3, vali, ppKoko/5);
		gc.fillRect(pp6XSijainti + viivaXSijaintiLisa, ppKoko/2 + vali*1.5 + ppKoko/3 + ppKoko/3, vali, ppKoko/5);
		gc.fillRect(pp5XSijainti + viivaXSijaintiLisa, ppKoko/2, ppKoko*1.5 + 2.5*vali, vali);
		
		gc.fillRect(pp4XSijainti + viivaXSijaintiLisa, ppKoko/2, vali, ppKoko/2);
		gc.fillRect(pp7XSijainti + ppKoko, ppKoko/2, ppKoko/2 + vali, vali);
	}
	
	/*-------------------------------------------------------------------
	 *  Asiakkaitten visuaalinen lisäys ja poisto Canvas ruudulle.
	 *  Metodit on eriytetty jotta sijaintia saadaan muokattua ja värejä vaihdettua niin halutessa.
	 ------------------------------------------------------------------------*/
	
	/**
	 * Metodi lisää asiakasta kuvaavan pallon graafisessa esityksessä jonottamaan kyseiselle palvelupisteelle.
	 * @param jonoKoko kertoo kuinka monta asiakasta on palvelupisteessä sillä hetkellä jonossa. Asiakas asetetaan jonon viimeiseksi.
	 */
	public void lisaaPp1Asiakas(int jonoKoko) {
		gc.setFill(Color.BLUE);
		gc.fillOval(pp1XSijainti + asiakasXSijaintiLisa, pp1asiakasYSijainti + (jonoKoko*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * Metodi poistaa graafisesta esityksestä asiakasta kuvaava pallon kyseiseltä palvelupisteeltä.
	 * Poistettu asiakas on itse asiassa jonon viimeinen mutta simulaation logiikka toimii edelleen kuten tarkoitus.
	 * @param jonoKoko kertoo kuinka monta asiakasta on palvelupisteessä sillä hetkellä jonossa. Näin voidaan poistaa viimeisin jonossa oleva asiakas.
	 */
	public void poistaPp1Asiakas(int jonoKoko) {
		gc.setFill(Color.WHITE);
		gc.fillRect(pp1XSijainti + asiakasXSijaintiLisa, pp1asiakasYSijainti + ((jonoKoko + 1)*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * @see Visualisointi#lisaaPp1Asiakas(int)
	 */
	public void lisaaPp2Asiakas(int jonoKoko) {
		gc.setFill(Color.BLUE);
		gc.fillOval(pp2XSijainti + asiakasXSijaintiLisa, pp2asiakasYSijainti + (jonoKoko*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * @see Visualisointi#poistaPp1Asiakas(int)
	 */
	public void poistaPp2Asiakas(int jonoKoko) {
		gc.setFill(Color.WHITE);
		gc.fillRect(pp2XSijainti + asiakasXSijaintiLisa, pp2asiakasYSijainti + ((jonoKoko + 1)*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * @see Visualisointi#lisaaPp1Asiakas(int)
	 */
	public void lisaaPp3Asiakas(int jonoKoko) {
		gc.setFill(Color.BLUE);
		gc.fillOval(pp3XSijainti + asiakasXSijaintiLisa, pp3asiakasYSijainti + (jonoKoko*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * @see Visualisointi#poistaPp1Asiakas(int)
	 */
	public void poistaPp3Asiakas(int jonoKoko) {
		gc.setFill(Color.WHITE);
		gc.fillRect(pp3XSijainti + asiakasXSijaintiLisa, pp3asiakasYSijainti + ((jonoKoko + 1)*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * @see Visualisointi#lisaaPp1Asiakas(int)
	 */
	public void lisaaPp4Asiakas(int jonoKoko) {
		gc.setFill(Color.BLUE);
		gc.fillOval(pp4XSijainti + asiakasXSijaintiLisa, pp4asiakasYSijainti + (jonoKoko*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * @see Visualisointi#poistaPp1Asiakas(int)
	 */
	public void poistaPp4Asiakas(int jonoKoko) {
		gc.setFill(Color.WHITE);
		gc.fillRect(pp4XSijainti + asiakasXSijaintiLisa, pp4asiakasYSijainti + ((jonoKoko + 1)*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * @see Visualisointi#lisaaPp1Asiakas(int)
	 */
	public void lisaaPp5Asiakas(int jonoKoko) {
		gc.setFill(Color.BLUE);
		gc.fillOval(pp5XSijainti + asiakasXSijaintiLisa, pp5asiakasYSijainti + (jonoKoko*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * @see Visualisointi#poistaPp1Asiakas(int)
	 */
	public void poistaPp5Asiakas(int jonoKoko) {
		gc.setFill(Color.WHITE);
		gc.fillRect(pp5XSijainti + asiakasXSijaintiLisa, pp5asiakasYSijainti + ((jonoKoko + 1)*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * @see Visualisointi#lisaaPp1Asiakas(int)
	 */
	public void lisaaPp6Asiakas(int jonoKoko) {
		gc.setFill(Color.BLUE);
		gc.fillOval(pp6XSijainti + asiakasXSijaintiLisa, pp6asiakasYSijainti + (jonoKoko*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * @see Visualisointi#poistaPp1Asiakas(int)
	 */
	public void poistaPp6Asiakas(int jonoKoko) {
		gc.setFill(Color.WHITE);
		gc.fillRect(pp6XSijainti + asiakasXSijaintiLisa, pp6asiakasYSijainti + ((jonoKoko + 1)*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * @see Visualisointi#lisaaPp1Asiakas(int)
	 */
	public void lisaaPp7Asiakas(int jonoKoko) {
		gc.setFill(Color.BLUE);
		gc.fillOval(pp7XSijainti + asiakasXSijaintiLisa, pp7asiakasYSijainti + (jonoKoko*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
	/**
	 * @see Visualisointi#poistaPp1Asiakas(int)
	 */
	public void poistaPp7Asiakas(int jonoKoko) {
		gc.setFill(Color.WHITE);
		gc.fillRect(pp7XSijainti + asiakasXSijaintiLisa, pp7asiakasYSijainti + ((jonoKoko + 1)*(asiakasKoko+vali)), asiakasKoko, asiakasKoko);
	}
	
}
