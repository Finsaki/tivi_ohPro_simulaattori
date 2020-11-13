package akiko.ruokalasimu.view;
	
import java.text.DecimalFormat;

import akiko.ruokalasimu.controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import akiko.ruokalasimu.model.Trace;
import akiko.ruokalasimu.model.Trace.Level;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

/**
* Main luokka vastaa käyttöliittymän toteutuksesta ja ohjelman käynnistämisestä.
*
* @author Aki Koppinen
* @version 1.1 (18.10.2020). Edellinen versio 1.0 (8.10.2020), ei sisältänyt JavaDoc dokumentointia
*/
public class Main extends Application implements GuiIf {
	
	//Kontrollerin esittely (tarvitaan käyttöliittymässä)
	
	/**
	 * Kontrolleri jonka kautta tietoja siirretään Moottorille ja Moottorilta käyttöliittymään.
	 */
	private KontrolleriIf kontrolleri;
	
	//Muut luokkamuuttujat
	/**
	 * Desimaali asettelu jota käytetään käyttöliittymän double arvoille.
	 */
	private DecimalFormat formatter;
	/**
	 * Fontti jota käytetään useimmille käyttöliittymän teksteille.
	 */
	private String fontFamily;

	// Käyttöliittymäkomponentit:
	
	/** Syötenäkymä simuloinnin kestolle. */
	private TextField aika;
	/** Syötenäkymä viiveelle. */
	private TextField viive;
	/** Valintatyökalu asiakassaapumisten aikavälille. */
	private Slider asiakasSaapumiset;
	/** Asetetun asiakassaapumisten aikavälin graafinen esitys. */
	private TextField asiakasSaapuminen;
	/** Valintatyökalu asiakassaapumisten määrälle. */
	private Slider asiakasMaarat;
	/** Asetetun asiakassaapumisten määrän graafinen esitys. */
	private TextField asiakasMaara;
	/** Valintatyökalu kahvilan käytössä ololle. */
	private ToggleButton kahvilaKaytossa;
	/** Valintatyökalu Kassa 2 käytössä ololle. */
	private ToggleButton kassa2Kaytossa;
	/** Syötenäkymä Ruokalan kapasiteetille. */
	private TextField ruokalanKapasiteetti;
	
	/** Tulokseksi saadun lopetusajan graafinen esitys. */
	private TextField lopetusAika;
	/** Tulokseksi saadun saapuneitten asiakkaitten määrän graafinen esitys. */
	private TextField saapuneetAsiakkaat;
	/** Tulokseksi saadun palveltujen asiakkaitten määrän graafinen esitys. */
	private TextField palvellutAsiakkaat;
	/** Tulokseksi saadun suoritustehon graafinen esitys. */
	private TextField suoritusteho;
	/** Tulokseksi saadun asiakkaiden läpimenoaikojen graafinen esitys. */
	private TextField lapimenoAjat;
	/** Tulokseksi saadun asiakkaiden läpimenoaikojen keskiarvon graafinen esitys. */
	private TextField lapimenoAjatKA;
	/** Tulokseksi saadun palvelupisteiden keskimääräisen jonon pituuden graafinen esitys. */
	private TextField jononPituusKA;
	
	/** Tulokseksi saadut Linjaston 1 tiedot. */
	private TextArea pp1;
	/** Tulokseksi saadut Linjaston 2 tiedot. */
	private TextArea pp2;
	/** Tulokseksi saadut Linjaston 3 tiedot. */
	private TextArea pp3;
	/** Tulokseksi saadut Kahvilan tiedot. */
	private TextArea pp4;
	/** Tulokseksi saadut Kassa 1 tiedot. */
	private TextArea pp5;
	/** Tulokseksi saadut Kassa 2 tiedot. */
	private TextArea pp6;
	/** Tulokseksi saadut Ruokalan tiedot. */
	private TextArea pp7;
	
	/** Ohjelman lopetus painike. */
	private Button exitButton;
	/** Aloitusnäkymän info painike. */
	private Button infoButton;
	/** Simuloinnin aloitus painike. */
	private Button simuloiButton;
	/** Simuloinnin hidastus painike. */
	private Button hidastaButton;
	/** Simuloinnin nopeutus painike. */
	private Button nopeutaButton;
	/** Simuloinnin keskeytys painike. */
	private Button lopetaButton;
	/** Simulointinäkymästä takaisin aloitusnäkymään painike. */
	private Button takaisinButton;

	/** Simulaation edistymisnäkymä. */
	private ProgressBar progressBar;
	/** Nykyisen viiveen esitys. */
	private Label viiveNakyma;
	
	/** Simuloinnin käynnissä olosta tai loppumisesta kertova teksti. */
	private Text simulointiStatus;
	
	/** Aloitusnäkymän vasemman puolen kansikuva jossa on mustavalkoinen ruokalinjasto. */
	private Image kansiKuva;

	/** Tietokannan asiakastiedot tekstinä. */
	private TextArea asiakkaatText;
	/** Tietokannan palvelupiste tiedot tekstinä. */
	private TextArea palvelupisteetText;
	/** Avaa tietokantanäkymän ja näyttää tietokannan sisällön tekstimudossa. */
	private Button naytaTietokantaButton;
	/** poistaa tietokannan datan. */
	private Button tyhjennaTietokantaButton;
	/** tallentaa saadut tulokset tietokantaan. */
	private Button tallennaTietokantaButton;
	/** Vaihtaa näkymän takaisin aloitusnäkymään tietokannasta. */
	private Button takaisin2Button;
	/** Näyttää tietokannan info tekstin. */
	private Button tietokantaInfoButton;
	
	/** Simulaation graafinen esitys muuttujana. */
	private Visualisointi naytto;
	
	/*------------------------
	 *	Alkutietojen asettaminen
	 --------------------------*/
	
	/**
	 * Alkutietojen asettaminen ohjelmalle.
	 */
	@Override
	public void init(){
		Trace.setTraceLevel(Level.WAR); //Kertoo halutun tietomäärän tulostamisesta konsoliin
		kontrolleri = new Kontrolleri(this);
		formatter = new DecimalFormat("#0.00"); //asetetaan etukäteen haluttu desimaalinakyma double luvuille
		fontFamily = "Helvetica";
	}
	
	/*-----------------------------------------------------
	 *	JavaFX-sovelluksen (käyttöliittymän) käynnistäminen
	 ------------------------------------------------------*/

	/**
	 * @param args käynnistys argumentit.
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/*-------------------------------
	 *	Käyttöliittymän rakentaminen
	 --------------------------------*/

	/**
	 * Käyttöliittymän käynnistäminen ja käyttöliittymäkomponenttien yhdistäminen.
	 */
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent t) {
			        Platform.exit();
			        System.exit(0);
			    }
			});
						
			primaryStage.setTitle("Simulaattori");

			//Aloitusnäkymän luominen
			SplitPane alkuNakyma = new SplitPane();
			alkuNakyma.setPrefSize(1000, 500);
			BorderPane aloitusNakymaVasen = alkuNakymaVasen(); //Alkunäkymän vasen puoli
			BorderPane aloitusNakymaOikea = alkuNakymaOikea(); //Alkunäkymän oikea puoli
			alkuNakyma.getItems().addAll(aloitusNakymaVasen, aloitusNakymaOikea); //Alkunäkymän asettaminen

			//Simulaatio näkymän luominen
			SplitPane simuNakyma = new SplitPane();
			simuNakyma.setPrefSize(1000, 500);
			naytto = new Visualisointi(400,400);
			
			BorderPane simulaatioNakymaVasen = simuNakymaVasen(); //Simulaationakyman vasen puoli
			BorderPane simulaatioNakymaOikea = simuNakymaOikea(); //Simulaationäkyman oikea puoli
			simuNakyma.getItems().addAll(simulaatioNakymaVasen, simulaatioNakymaOikea); //Simulaationäkymän asettaminen
			
			//Tietokanta näkymän luominen
			SplitPane tietokantaNakyma = new SplitPane();
			tietokantaNakyma.setPrefSize(1000, 500);
			BorderPane tkNakymaVasen = tietokantaNakymaVasen();
			BorderPane tkNakymaOikea = tietokantaNakymaOikea();
			tietokantaNakyma.getItems().addAll(tkNakymaVasen, tkNakymaOikea);
			
			//Scene näkymien luominen
	        Scene scene1 = new Scene(alkuNakyma); //Aloitussivu
	        Scene scene2 = new Scene(simuNakyma); //Simulointisivu
	        Scene scene3 = new Scene(tietokantaNakyma); //Tietokantasivu (Palvelupisteet + Asiakkaat)

			//Action tapahtumien luominen
	        luoGuiToiminnot(primaryStage, scene1, scene2, scene3);
	        
	        //Ensimmäisen näkymän asettaminen
	        primaryStage.setScene(scene1);
	        primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*-----------
	 * 	GUI osat
	 -------------*/
	
	/**
	 * @return Aloitusnäkymän vasemman puolen (Otsikko ja Kuva) komponentit ja asettelu.
	 */
	private BorderPane alkuNakymaVasen() {
		BorderPane alkuVasenNakyma = new BorderPane();
		alkuVasenNakyma.setPadding(new Insets(5,5,5,5));
		HBox otsikkoAsettelu = new HBox();
		Text ruokalaSimuOtsikko = new Text("Ruokalasimulaattori");
		ruokalaSimuOtsikko.setFont(Font.font(fontFamily, FontWeight.BOLD, 24));
		ruokalaSimuOtsikko.setFill(Color.ROYALBLUE);
		otsikkoAsettelu.getChildren().addAll(ruokalaSimuOtsikko);
		otsikkoAsettelu.setAlignment(Pos.CENTER);
		otsikkoAsettelu.setPadding(new Insets(25,0,0,0));
		alkuVasenNakyma.setTop(otsikkoAsettelu);
		try {
			kansiKuva = new Image("file:images/RuokalaKansiKuvaMV.jpg");
			ImageView kuvaNakyma = new ImageView(kansiKuva);
			kuvaNakyma.setFitWidth(450);
			kuvaNakyma.setPreserveRatio(true);
			alkuVasenNakyma.setCenter(kuvaNakyma);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		BorderPane alaVasenNapit = new BorderPane();
		exitButton = new Button("Lopeta ohjelma");
		exitButton.setMinSize(100, 25);
		exitButton.setFont(Font.font(fontFamily, FontWeight.BOLD, 12));
		exitButton.setTextFill(Color.RED);
		infoButton = new Button("?");
		infoButton.setMinSize(25, 25);
		infoButton.setFont(Font.font(fontFamily, FontWeight.BOLD, 12));
		infoButton.setTextFill(Color.ROYALBLUE);
		alaVasenNapit.setLeft(exitButton);
		alaVasenNapit.setRight(infoButton);
		alaVasenNapit.setPadding(new Insets(5, 5, 5, 5));
		alkuVasenNakyma.setBottom(alaVasenNapit);
		
		return alkuVasenNakyma;
	}
	
	/**
	 * @return Aloitusnäkymän oikean puolen (Asetukset) komponentit ja asettelu.
	 */
	private BorderPane alkuNakymaOikea() {
		GridPane valintaNakyma = new GridPane();
		valintaNakyma.setVgap(5);
		valintaNakyma.setHgap(10);
		
		aika = new TextField("5000");
		viive = new TextField("0");
		ruokalanKapasiteetti = new TextField("100");
		kahvilaKaytossa = new ToggleButton("ON");
		kassa2Kaytossa = new ToggleButton("ON");
		
		asiakasSaapuminen = new TextField("10.00");
		asiakasSaapumiset = new Slider(1.0, 20.0, 10.0);
		asiakasSaapumiset.setMajorTickUnit(5);
		asiakasSaapumiset.setShowTickLabels(true);
		
		asiakasMaara = new TextField("1.00");
		
		asiakasMaarat = new Slider(1.0, 10.0, 1.0);
		asiakasMaarat.setMajorTickUnit(3);
		asiakasMaarat.setShowTickLabels(true);
		
		valintaNakyma.add(new Label("Simulointiaika"), 0, 0);
		valintaNakyma.add(aika, 1, 0);
		valintaNakyma.add(new Label("Simuloinnin viive"), 0, 1);
		valintaNakyma.add(viive, 1, 1);
		valintaNakyma.add(new Label("Asiakkaiden saapumisväli"), 0, 2);
		valintaNakyma.add(asiakasSaapumiset, 1, 2);
		valintaNakyma.add(asiakasSaapuminen, 2, 2);
		valintaNakyma.add(new Label("Asiakkaiden saapumismäärä"), 0, 3);
		valintaNakyma.add(asiakasMaarat, 1, 3);
		valintaNakyma.add(asiakasMaara, 2, 3);
		valintaNakyma.add(new Label("Kahvila käytössä"), 0, 4);
		valintaNakyma.add(kahvilaKaytossa, 1, 4);
		valintaNakyma.add(new Label("Kassa 2 käytössä"), 0, 5);
		valintaNakyma.add(kassa2Kaytossa, 1, 5);
		valintaNakyma.add(new Label("Ruokalan kapasiteetti"), 0, 6);
		valintaNakyma.add(ruokalanKapasiteetti, 1, 6);
		
		BorderPane alkuOikeaNakyma = new BorderPane();
		alkuOikeaNakyma.setPadding(new Insets(5,5,5,5));
		
		Label asetuksetOtsikko = new Label("Asetukset");
		HBox asetusOtsikkoAsettelu = otsikkoAsettelu(asetuksetOtsikko, 14);
		
		alkuOikeaNakyma.setTop(asetusOtsikkoAsettelu);
		alkuOikeaNakyma.setCenter(valintaNakyma);
		
		VBox nappulaAsettelu = new VBox();
		simuloiButton = new Button("Simuloi");
		simuloiButton.setMinSize(200, 50);
		simuloiButton.setFont(Font.font(fontFamily, FontWeight.BOLD, 14));
		simuloiButton.setTextFill(Color.GREEN);
		naytaTietokantaButton = new Button("Näytä tietokanta");
		tyhjennaTietokantaButton = new Button("Tyhjennä tietokanta");
		nappulaAsettelu.getChildren().addAll(simuloiButton, naytaTietokantaButton, tyhjennaTietokantaButton);
		nappulaAsettelu.setAlignment(Pos.CENTER);
		nappulaAsettelu.setSpacing(15);
		nappulaAsettelu.setPadding(new Insets(0,0,35,0));
		alkuOikeaNakyma.setBottom(nappulaAsettelu);
		
		return alkuOikeaNakyma;
	}
	
	/**
	 * @return Simulaationäkymän vasemman puolen (Graafinen esitys) komponentit ja asettelu.
	 */
	private BorderPane simuNakymaVasen() {
		BorderPane simuVasenNakyma = new BorderPane();
		simuVasenNakyma.setPadding(new Insets(5,5,5,5));

		Label simulaatioOtsikko = new Label("Simulaatio");
		HBox simulaatioOtsikkoAsettelu = otsikkoAsettelu(simulaatioOtsikko, 14);
		
		//Palvelupisteiden nimeäminen naytössä. HUOM! tämän voisi tehdä hienomminkin, CSS?
		double tekstinKoko = 9;
		TextFlow selitys = new TextFlow();
		selitys.setTextAlignment(TextAlignment.CENTER);
		selitys.setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		Text text1 = new Text("Linjasto 1 - ");
		text1.setFill(Color.SIENNA);
		text1.setFont(Font.font(fontFamily, FontWeight.BOLD, tekstinKoko));
		Text text2 = new Text("Linjasto 2 - ");
		text2.setFill(Color.SIENNA);
		text2.setFont(Font.font(fontFamily, FontWeight.BOLD, tekstinKoko));
		Text text3 = new Text("Linjasto 3 - ");
		text3.setFill(Color.SIENNA);
		text3.setFont(Font.font(fontFamily, FontWeight.BOLD, tekstinKoko));
		Text text4 = new Text("Kassa 1 - ");
		text4.setFill(Color.ORANGE);
		text4.setFont(Font.font(fontFamily, FontWeight.BOLD, tekstinKoko));
		Text text5 = new Text("Kassa 2 - ");
		text5.setFill(Color.ORANGE);
		text5.setFont(Font.font(fontFamily, FontWeight.BOLD, tekstinKoko));
		Text text6 = new Text("Ruokala - ");
		text6.setFill(Color.BLACK);
		text6.setFont(Font.font(fontFamily, FontWeight.BOLD, tekstinKoko));
		Text text7 = new Text("Kahvila");
		text7.setFill(Color.DARKORCHID);
		text7.setFont(Font.font(fontFamily, FontWeight.BOLD, tekstinKoko));
		selitys.getChildren().addAll(text1, text2, text3, text4, text5, text6, text7);
		
		VBox nayttoJaSelitys = new VBox();
		nayttoJaSelitys.setAlignment(Pos.CENTER);
		nayttoJaSelitys.setFillWidth(true);
		nayttoJaSelitys.setSpacing(5);
		nayttoJaSelitys.getChildren().addAll(selitys, naytto);
		
		GridPane simulaationStatus = new GridPane();
		simulaationStatus.setPadding(new Insets(5,0,5,0));
		simulaationStatus.setVgap(10);
		simulaationStatus.setHgap(10);
		progressBar = new ProgressBar(0);
		viiveNakyma = new Label(viive.getText());
		viiveNakyma.setPrefWidth(100);
		simulaationStatus.add(new Label("Simulaation viive"), 0, 0);
		simulaationStatus.add(viiveNakyma, 1, 0);
		simulaationStatus.add(new Label("Simulaation edistyminen"), 0, 1);
		simulaationStatus.add(progressBar, 1, 1);
		
		simuVasenNakyma.setTop(simulaatioOtsikkoAsettelu);
		simuVasenNakyma.setCenter(nayttoJaSelitys);
		simuVasenNakyma.setBottom(simulaationStatus);
		
		return simuVasenNakyma;
	}
	
	/**
	 * @return Simulaationäkymän oikean puolen (Tulokset) komponentit ja asettelu.
	 */
	private BorderPane simuNakymaOikea() {
		HBox tulosNapit = new HBox();
		tulosNapit.setSpacing(10);
		tulosNapit.setPadding(new Insets(10,5,5,5));
		hidastaButton = new Button("Hidasta");
		nopeutaButton = new Button("Nopeuta");
		lopetaButton = new Button("Keskeytä");
		tallennaTietokantaButton = new Button("Tallenna tietokantaan");
		takaisinButton = new Button("Takaisin");
		tulosNapit.getChildren().addAll(hidastaButton, nopeutaButton, lopetaButton, tallennaTietokantaButton, takaisinButton);

		GridPane tulosNakyma1 = new GridPane();
		tulosNakyma1.setVgap(5);
		tulosNakyma1.setHgap(10);
		lopetusAika = new TextField();
		saapuneetAsiakkaat = new TextField();
		palvellutAsiakkaat = new TextField();
		suoritusteho = new TextField();
		lapimenoAjat = new TextField();
		lapimenoAjatKA = new TextField();
		jononPituusKA = new TextField();
		tulosNakyma1.add(new Label("Simuloinnin kesto"), 0, 0);
		tulosNakyma1.add(lopetusAika, 1, 0);
		tulosNakyma1.add(new Label("Saapuneet asiakkaat"), 0, 1);
		tulosNakyma1.add(saapuneetAsiakkaat, 1, 1);
		tulosNakyma1.add(new Label("Palvellut asiakkaat"), 0, 2);
		tulosNakyma1.add(palvellutAsiakkaat, 1, 2);
		tulosNakyma1.add(new Label("Suoritusteho"), 0, 3);
		tulosNakyma1.add(suoritusteho, 1, 3);
		tulosNakyma1.add(new Label("Keskimääräinen läpimenoaika"), 0, 4);
		tulosNakyma1.add(lapimenoAjatKA, 1, 4);
		tulosNakyma1.add(new Label("Läpimenoajat yhteensä"), 0, 5);
		tulosNakyma1.add(lapimenoAjat, 1, 5);
		tulosNakyma1.add(new Label("Keskimääräinen jonon pituus"), 0, 6);
		tulosNakyma1.add(jononPituusKA, 1, 6);
		
		pp1 = new TextArea();
		pp2 = new TextArea();
		pp3 = new TextArea();
		pp4 = new TextArea();
		pp5 = new TextArea();
		pp6 = new TextArea();
		pp7 = new TextArea();
		TabPane tulosNakyma2 = new TabPane();
		tulosNakyma2.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		Tab tab1 = new Tab("Linjasto 1", pp1);
		Tab tab2 = new Tab("Linjasto 2", pp2);
		Tab tab3 = new Tab("Linjasto 3", pp3);
		Tab tab4 = new Tab("Kahvila", pp4);
		Tab tab5 = new Tab("Kassa 1", pp5);
		Tab tab6 = new Tab("Kassa 2", pp6);
		Tab tab7 = new Tab("Ruokala", pp7);
		tulosNakyma2.getTabs().addAll(tab1, tab2, tab3, tab4, tab5, tab6, tab7);
		
		VBox tulosNakymat = new VBox();
		tulosNakymat.setSpacing(10);
		
		Label ppOtsikko = new Label("Palvelupisteiden tiedot");
		ppOtsikko.setFont(Font.font(fontFamily, FontWeight.BOLD, 12));

		tulosNakymat.getChildren().addAll(tulosNakyma1, ppOtsikko, tulosNakyma2);

		Label tuloksetOtsikko = new Label("Tulokset");
		HBox tuloksetOtsikkoAsettelu = otsikkoAsettelu(tuloksetOtsikko, 14);
		
		simulointiStatus = new Text();

		VBox ylaTekstit = new VBox();
		ylaTekstit.getChildren().addAll(tuloksetOtsikkoAsettelu, simulointiStatus);
		
		BorderPane simuOikeaNakyma = new BorderPane();
		simuOikeaNakyma.setPadding(new Insets(5,5,5,5));
		simuOikeaNakyma.setTop(ylaTekstit);
		simuOikeaNakyma.setCenter(tulosNakymat);
		simuOikeaNakyma.setBottom(tulosNapit);
		
		return simuOikeaNakyma;
	}
	
	/**
	 * @return Tietokantanäkymän vasemman puolen (Palvelupisteet) komponentit ja asettelu.
	 */
	private BorderPane tietokantaNakymaVasen() {
		BorderPane tkVasen = new BorderPane();
		Label otsikko = new Label("Palvelupiste tiedot");
		HBox otsikkoMuutos = otsikkoAsettelu(otsikko, 14);
		palvelupisteetText = new TextArea();
		BorderPane nappulaAsettelu = new BorderPane();
		tietokantaInfoButton = new Button("?");
		nappulaAsettelu.setLeft(tietokantaInfoButton);
		nappulaAsettelu.setPadding(new Insets(10,10,10,10));
		tkVasen.setTop(otsikkoMuutos);
		tkVasen.setCenter(palvelupisteetText);
		tkVasen.setBottom(nappulaAsettelu);
		return tkVasen;
	}
	
	/**
	 * @return Tietokantanäkymän oikean puolen (Asiakkaat) komponentit ja asettelu.
	 */
	private BorderPane tietokantaNakymaOikea() {
		BorderPane tkOikea = new BorderPane();
		Label otsikko = new Label("Asiakas tiedot");
		HBox otsikkoMuutos = otsikkoAsettelu(otsikko, 14);
		asiakkaatText = new TextArea();
		BorderPane nappulaAsettelu = new BorderPane();
		takaisin2Button = new Button("Takaisin");
		nappulaAsettelu.setRight(takaisin2Button);
		nappulaAsettelu.setPadding(new Insets(10,10,10,10));
		tkOikea.setTop(otsikkoMuutos);
		tkOikea.setCenter(asiakkaatText);
		tkOikea.setBottom(nappulaAsettelu);
		return tkOikea;
	}
	
	/* ------------------
	 * 	GUI toiminnot
	 --------------------*/
	
	/**
	 * Luodaan nappien painalluksien toiminnot.
	 * 
	 * @param primaryStage käyttöliittymän nykyinen näkymä.
	 * @param scene1 Aloitusnäkymä.
	 * @param scene2 Simulaationäkymä.
	 * @param scene3 Tietokantanäkymä.
	 */
	private void luoGuiToiminnot(Stage primaryStage, Scene scene1, Scene scene2, Scene scene3) {
		
		asiakasSaapumiset.valueProperty().addListener(new ChangeListener<Number>() { //vaihdetaan asiakasSaapuminen muuttujan arvo Sliderin muutoksia vastaaviksi
        	public void changed(ObservableValue <? extends Number >  
            observable, Number oldValue, Number newValue)
        	{
        		asiakasSaapuminen.setText(formatter.format(newValue));
        	}
        });
        
        asiakasMaarat.valueProperty().addListener(new ChangeListener<Number>() { //vaihdetaan asiakasMaara muuttujan arvo Sliderin muutoksia vastaaviksi
        	public void changed(ObservableValue <? extends Number >  
            observable, Number oldValue, Number newValue)
        	{
        		asiakasMaara.setText(formatter.format(newValue));
        	}
        });
        
        simuloiButton.setOnAction(e -> { //vaihdetaan näkymä simulointinäkymään ja aloitetaan simulointi
        	if (tarkista()) { //varmistetaan että syötteet ovat kunnollisia
        		tyhjenna();
        		primaryStage.setScene(scene2);
	        	viiveNakyma.setText(viive.getText()); //viedään asetettu viiveen arvoon valmiiksi simulointinäkymään
	        	simulointiStatus.setText("Simulointi käynnissä - Odotetaan tuloksia...");
	        	simulointiStatus.setFont(Font.font(fontFamily, FontWeight.NORMAL, 10));
	    		simulointiStatus.setFill(Color.RED);
	        	kontrolleri.kaynnistaSimulointi(); //käynnistetään simulaatio
        	} else { //jos syötteissä on vikaa, annetaan se tiedoksi käyttäjälle
        		naytaSyoteVaroitus(); //Näytetään varoitusviesti
        	}
        	
        });
        
        takaisinButton.setOnAction(e -> { //vaihdetaan näkymä takaisin alkunäkymään
        	kontrolleri.lopeta(); //varmistetaan että simulaation suoritus on lakannut
        	 //tyhjennetaan tuloskenttaan saadut luvut seuraavaa suoritusta varten
        	primaryStage.setScene(scene1);
        });
        
        naytaTietokantaButton.setOnAction(e -> {
        	primaryStage.setScene(scene3);
        	if (progressBar.getProgress() != 0) { //jos simulaatiota on ajettu ainakin jonkun aikaa HUOM! Ohjelman avaamisen jälkeen tietojen tarkastelu ei heti mahdollista
        		asiakkaatText.setText("");
        		palvelupisteetText.setText("");
        		kontrolleri.naytaAsiakasTietokanta();
            	kontrolleri.naytaPalvelupisteTietokanta();
        	}
        });
        
        takaisin2Button.setOnAction(e -> primaryStage.setScene(scene1));
        
        kahvilaKaytossa.setOnAction(e -> {
        	if (kahvilaKaytossa.isSelected()) { //Togglebuttonin boolean arvoa hyödyntämällä vaihdetaan napin teksti halutunlaiseksi
        		kahvilaKaytossa.setText("OFF");
        	} else {
        		kahvilaKaytossa.setText("ON");
        	}
        });
        
        kassa2Kaytossa.setOnAction(e -> {
        	if (kassa2Kaytossa.isSelected()) { //Togglebuttonin boolean arvoa hyödyntämällä vaihdetaan napin teksti halutunlaiseksi
        		kassa2Kaytossa.setText("OFF");
        	} else {
        		kassa2Kaytossa.setText("ON");
        	}
        });
        
        lopetaButton.setOnAction(e -> {
        	kontrolleri.lopeta(); //keskeyttää simulaation suorituksen
        });
        
        nopeutaButton.setOnAction(e -> kontrolleri.nopeuta()); //nopeuta simulaatiota
        
        hidastaButton.setOnAction(e -> kontrolleri.hidasta()); //hidasta simulaatiota
        
        exitButton.setOnAction(e -> Platform.exit());
        
        infoButton.setOnAction(e -> naytaInfoViesti());
        
        tietokantaInfoButton.setOnAction(e -> naytaTietokantaInfoViesti());
        
        tallennaTietokantaButton.setOnAction(e -> kontrolleri.tallennaTietokantaan());
        
        tyhjennaTietokantaButton.setOnAction(e -> { //HUOM! Ohjelman avaamisen jälkeen tietojen poisto ei heti mahdollista, vaatii ainakin yhden ajokerran
        	if (progressBar.getProgress() != 0) {
        		kontrolleri.poistaTietokannat();
        	}
        });
	}
	
	/*-----------------
	 *	Private metodit
	 ------------------*/
	
	/**
	 * Tyhjennetään kaikki tulosnäkymän tulokset.
	 */
	private void tyhjenna() { //tyhjennetaan tuloskenttaan saadut luvut
		this.lopetusAika.clear();
		this.saapuneetAsiakkaat.setText("");
		this.palvellutAsiakkaat.setText("");
		this.suoritusteho.clear();
		this.lapimenoAjat.clear();
		this.lapimenoAjatKA.clear();
		this.jononPituusKA.clear();
		this.viiveNakyma.setText("");
		this.pp1.clear();
		this.pp2.clear();
		this.pp3.clear();
		this.pp4.clear();
		this.pp5.clear();
		this.pp6.clear();
		this.pp7.clear();
	}
	
	/**
	 * Näytetään varoitus jos syötteet eivät ole kelvolliset.
	 */
	private void naytaSyoteVaroitus() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Varoitus");
		alert.setHeaderText("Varoitus - syötetty data ei kelpaa");
		alert.setContentText("Varmista että simulaation asetukset ovat kelvolliset\n\n"
				+ "Simulaation aika ei saa olla pienempi kuin 1\n"
				+ "Viive ei saa olla pienempi kuin 0\n"
				+ "Ruokalan kapasiteetti ei saa olla pienempi kuin 1\n\n"
				+ "Näiden syötteiden on myös oltava kokonaislukuja");
		alert.showAndWait();
	}
	
	/**
	 * Näytetään Aloitusnäkymän infoviesti.
	 */
	private void naytaInfoViesti() {
		Alert info = new Alert(AlertType.INFORMATION);
		info.setTitle("Ruokalasimulaattori - info");
		info.setHeaderText("Tietoa ohjelmasta");
		info.setContentText("Tämä simulaattori tarjoaa mahdollisuuden simuloida ruokalan toimintaa.\n\n"
				+ "Voit asettaa haluamasi asetukset ja painaa sitten |Simuloi| painiketta.\n"
				+ "Ohjelma simuloi toimintaa tietyn ajan verran, riippuen annetusta viiveestä.\n"
				+ "Näet simulaatiosta visuaalisen esityksen jossa Laatikot edustavat palvelupisteitä ja pallot edustavat jonossa olevia asiakkaita.\n"
				+ "Voit myös halutessasi tallentaa saadut tulokset mySQL tietokantaan painamalla |Tallenna tietokantaan|.\n"
				+ "Voit suorittaa simuloinnin uudelleen palaamalla takaisin etusivulle |Takaisin| painikkeella.\n\n"
				+ "Tekijä: Aki Koppinen, Tieto- ja viestintätekniikan opiskelija Metropolia Ammattikorkeakoulussa.\n"
				+ "Simulaatio on tehty osana Ohjelmointiprojekti TX00CD79-3012 kurssia.\n"
				+ "08.10.2020");
		info.showAndWait();
	}
	
	/**
	 * Näytetään Tietokantanäkymän infoviesti.
	 */
	private void naytaTietokantaInfoViesti() {
		Alert info = new Alert(AlertType.INFORMATION);
		info.setTitle("Tietokanta - info");
		info.setHeaderText("Tietoa tietokannasta");
		info.setContentText("Tämä ohjelma kerää aineistonsa mySQL tietokantaan kun käyttäjä painaa Tallenna tietokantaan -nappia Simulointi sivulla.\n\n"
				+ "Tämä esitysikkuna käy hakemassa tietokannan sisällön ja näyttää sen käyttäjälle tekstimuodossa.\n"
				+ "Tietokannan tekemiseen on käytetty hyväksi hibernate -ORM työkalua.\n\n"
				+ "Sarakkeiden selitykset: \n"
				+ "AKTIIVIAIKA = Aika jolloin palvelupisteessä on ollut asiakkaita.\n"
				+ "S.ASIAKKAAT = Palvelupisteelle saapuneet asiakkaat.\n"
				+ "P.ASIAKKAAT = Palvelupisteellä palvelun loppuun vieneet asiakkaat.\n"
				+ "KAPASITEETTI = Palvelupisteellä yhtä aikaa palveltavien asiakkaiden maksimimäärä.\n"
				+ "LÄPIMENOAIKA = Asiakkaan simulaatiossa viettämä kokonaisaika.");
		info.showAndWait();
	}
	
	/**
	 * @return Tieto siitä ovatko syötteet olleet kelvolliset.
	 */
	private boolean tarkista() { //tarkistetaan Asetuksiin asetetut luvut
		boolean kaikkiOk = true;
		try {
			//lukujen on oltava kokonaislukuja
			int tarkistus1 = Integer.parseInt(aika.getText()); 
			int tarkistus2 = Integer.parseInt(viive.getText());
			int tarkistus3 = Integer.parseInt(ruokalanKapasiteetti.getText());
			//lukujen on oltava double tyyppisiä
			double tarkistus4 = Double.parseDouble(asiakasSaapuminen.getText());
			double tarkistus5 = Double.parseDouble(asiakasMaara.getText());
			
			//luvuille on annettu vähimmäis ja tarpeen mukaan enimmäismäärä
			if (tarkistus1 < 1 || tarkistus2 < 0 || tarkistus3 < 1 || tarkistus4 < 1 || tarkistus5 < 1 || tarkistus4 > 20 || tarkistus5 > 10) { 
				kaikkiOk = false;
			}
		} catch (Exception e) {
			kaikkiOk = false;
		}
		return kaikkiOk;
	}
	
	/**
	 * Metodia käytetään antamaan useille otsikoille sama asettelu.
	 * @param otsikko Otsikkoteksti.
	 * @param fonttiKoko haluttu fonttikoko.
	 * @return aseteltu otsikko.
	 */
	private HBox otsikkoAsettelu(Label otsikko, int fonttiKoko) {
		otsikko.setFont(Font.font(fontFamily, FontWeight.BOLD, fonttiKoko));
		HBox otsikkoAsettelu = new HBox();
		otsikkoAsettelu.getChildren().add(otsikko);
		otsikkoAsettelu.setPadding(new Insets(5,0,5,0));
		return otsikkoAsettelu;
	}
	
	/*-------------------------------------------------------------
	 *	Käyttöliittymän rajapintametodit (kutsutaan kontrollerista)
	 -------------------------------------------------------------*/
	
	/**	{@inheritDoc} */
	@Override
	public double getAika(){
		return Double.parseDouble(aika.getText());
	}

	/**	{@inheritDoc} */
	@Override
	public long getViive(){
		return Long.parseLong(viive.getText());
	}

	/**	{@inheritDoc} */
	@Override
	public double getAsiakasSaapuminen() {
		return Double.parseDouble(asiakasSaapuminen.getText()) / Double.parseDouble(asiakasMaara.getText());
	}

	/**	{@inheritDoc} */
	@Override
	public boolean getKahvilaStatus() {
		return kahvilaKaytossa.isSelected();
	}

	/**	{@inheritDoc} */
	@Override
	public boolean getKassa2Status() {
		return kassa2Kaytossa.isSelected();
	}

	/**	{@inheritDoc} */
	@Override
	public int getRuokalanKapasiteetti() {
		return Integer.parseInt(ruokalanKapasiteetti.getText());
	}

	/**	{@inheritDoc} */
	@Override
	public void setLoppuaika(double aika){
		 this.lopetusAika.setText(formatter.format(aika));
	}

	/**	{@inheritDoc} */
	@Override
	public Visualisointi getVisualisointi() {
		 return naytto;
	}

	/**	{@inheritDoc} */
	@Override
	public void setSaapuneetAsiakkaat(int maara) {
		this.saapuneetAsiakkaat.setText(String.valueOf(maara));
		
	}

	/**	{@inheritDoc} */
	@Override
	public void setPalvellutAsiakkaat(int maara) {
		this.palvellutAsiakkaat.setText(String.valueOf(maara));
		
	}

	/**	{@inheritDoc} */
	@Override
	public void setSuoritusteho(double teho) {
		this.suoritusteho.setText(formatter.format(teho));
		
	}

	/**	{@inheritDoc} */
	@Override
	public void setKeskimLapiMenoAika(double aika) {
		this.lapimenoAjatKA.setText(formatter.format(aika));
		
	}

	/**	{@inheritDoc} */
	@Override
	public void setLapiMenoAjatYht(long ajat) {
		this.lapimenoAjat.setText(String.valueOf(ajat));
		
	}

	/**	{@inheritDoc} */
	@Override
	public void setKeskimJononPituus(double pituus) {
		this.jononPituusKA.setText(formatter.format(pituus));
	}

	/**	{@inheritDoc} */
	@Override
	public void setPP1(String teksti) {
		this.pp1.setText(teksti);
		
	}

	/**	{@inheritDoc} */
	@Override
	public void setPP2(String teksti) {
		this.pp2.setText(teksti);
		
	}

	/**	{@inheritDoc} */
	@Override
	public void setPP3(String teksti) {
		this.pp3.setText(teksti);
		
	}

	/**	{@inheritDoc} */
	@Override
	public void setPP4(String teksti) {
		this.pp4.setText(teksti);
		
	}

	/**	{@inheritDoc} */
	@Override
	public void setPP5(String teksti) {
		this.pp5.setText(teksti);
		
	}

	/**	{@inheritDoc} */
	@Override
	public void setPP6(String teksti) {
		this.pp6.setText(teksti);
		
	}

	/**	{@inheritDoc} */
	@Override
	public void setPP7(String teksti) {
		this.pp7.setText(teksti);
		
	}
	
	/**	{@inheritDoc} */
	@Override
	public void setProgressBar(double value) {
		this.progressBar.setProgress(value);
	}
	
	/**	{@inheritDoc} */
	public void setSimulointiStatusPaattynyt() {
		this.simulointiStatus.setText("Simulointi on päättynyt - tulokset alla");
		this.simulointiStatus.setFont(Font.font(fontFamily, FontWeight.NORMAL, 10));
		this.simulointiStatus.setFill(Color.GREEN);
	}
	
	/**	{@inheritDoc} */
	public void setViiveNakyma(double value) {
		this.viiveNakyma.setText(formatter.format(value));
	}
	
	/**	{@inheritDoc} */
	public void setAsiakasText(String[] asiakkaat) {
		//Tämä vaatisi parannusta, on vähän tökerö asettelu menetelmä HUOM!
		this.asiakkaatText.appendText(String.format("%-21s %-18s %-17s %-1s", "ID", "SAAPUMISAIKA", "POISTUMISAIKA", "LÄPIMENOAIKA"));
		this.asiakkaatText.appendText("\n\n");
		for (String asiakasRivi: asiakkaat) {
			this.asiakkaatText.appendText(asiakasRivi + "\n");
		}
	}
	
	/**	{@inheritDoc} */
	public void setPalvelupisteText(String[] palvelupisteet) {
		//Tämä vaatisi parannusta, on vähän tökerö asettelu menetelmä HUOM!
		this.palvelupisteetText.appendText(String.format("%-21s %-20s %-18s %-12s %-12s %-1s", "ID","NIMI","AKTIIVIAIKA", "S.ASIAKKAAT", "P.ASIAKKAAT", "KAPASITEETTI"));
		this.palvelupisteetText.appendText("\n\n");
		for (String palvelupisteRivi: palvelupisteet) {
			this.palvelupisteetText.appendText(palvelupisteRivi + "\n");
		}
	}

}
