package akiko.ruokalasimu.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;


/**
 * SimulaattoriDAO luokka sisältää kaikki tietokannan lukemiseen, kirjoittamiseen, poistamiseen ja päivittämiseen tarvittavat metodit.
 * Päivittämis metodit eivät ole käytössä ohjelmassa.
 * SimulaattoriDAO käyttää toteutuksessaan hyväksi Hibernate ORM-työkalua.
 * 
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
public class SimulaattoriDAO implements ISimulaattoriDAO {
	
	/**
	 * Tietokannan käsittelyyn tarvittava istuntotehdas.
	 */
	private SessionFactory istuntotehdas = null;
	
	/**
	 * SimulaattoriDAO luokan konstruktori jossa yritetään luoda tietokantayhteys.
	 * Metodi käyttää tietoja hibernate.cfx.xml dokumentista joka löytyy src kansiosta.
	 */
	public SimulaattoriDAO() {
		try {
			istuntotehdas = new Configuration().configure().buildSessionFactory();
		} catch (Exception e) {
			System.err.println("Istuntotehtaan luonti ei onnistunut: " + e.getMessage());
			//System.exit(-1); poista kommentti kun tiedot lisatty hibernate.cfg.xml tiedostoon!!!
		}
	}
	
	/**	{@inheritDoc} */
	@Override
	public void finalize() {
		try {
			if (istuntotehdas != null) {
				istuntotehdas.close();
			}
		} catch (Exception e) {
			System.err.println("Istuntotehtaan sulkeminen epäonnistui: " + e.getMessage());
		}
	}
	
	/**	{@inheritDoc} */
	@Override
	public boolean createPalvelupiste(Palvelupiste uusiPalvelupiste) {
		Transaction transaktio = null;
		try (Session istunto = istuntotehdas.openSession()) {
			transaktio = istunto.beginTransaction();
			Palvelupiste tallennettavaPalvelupiste = uusiPalvelupiste;
			istunto.saveOrUpdate(tallennettavaPalvelupiste);
			transaktio.commit();
		} catch(Exception e) {
			if (transaktio != null) {
				transaktio.rollback();
			}
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**	{@inheritDoc} */
	@Override
	public boolean createAsiakas(Asiakas uusiAsiakas) {
		Transaction transaktio = null;
		try (Session istunto = istuntotehdas.openSession()) {
			transaktio = istunto.beginTransaction();
			Asiakas tallennettavaAsiakas = uusiAsiakas;
			istunto.saveOrUpdate(tallennettavaAsiakas);
			transaktio.commit();
		} catch(Exception e) {
			if (transaktio != null) {
				transaktio.rollback();
			}
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**	{@inheritDoc} */
	@Override
	public Palvelupiste[] readPalvelupisteet() {
		ArrayList<Palvelupiste> palvelupisteet = new ArrayList<>();
		
		try (Session istunto = istuntotehdas.openSession()) {
			String hql = "from Palvelupiste";
			@SuppressWarnings("rawtypes")
			Query kysely = istunto.createQuery(hql);
			@SuppressWarnings("unchecked")
			List<Palvelupiste> result = kysely.list(); //List<Valuutta> result = istunto.createQuery(hql).getResultList(); 
			
			for (Palvelupiste pp: result) {
				palvelupisteet.add(pp);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Palvelupiste[] pPisteetArray = new Palvelupiste[palvelupisteet.size()];
		
		return (Palvelupiste[])palvelupisteet.toArray(pPisteetArray);
	}

	/**	{@inheritDoc} */
	@Override
	public Asiakas[] readAsiakkaat() {
		ArrayList<Asiakas> asiakkaat = new ArrayList<>();
		
		try (Session istunto = istuntotehdas.openSession()) {
			String hql = "from Asiakas";
			@SuppressWarnings("rawtypes")
			Query kysely = istunto.createQuery(hql);
			@SuppressWarnings("unchecked")
			List<Asiakas> result = kysely.list(); //List<Valuutta> result = istunto.createQuery(hql).getResultList(); 
			
			for (Asiakas a: result) {
				asiakkaat.add(a);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Asiakas[] asiakkaatArray = new Asiakas[asiakkaat.size()];
		
		return (Asiakas[])asiakkaat.toArray(asiakkaatArray);
	}

	/**	{@inheritDoc} */
	@Override
	public boolean deleteAsiakkaat() { //tämä metodi poistaa kaikki kerätyt Asiakas tiedot
		Transaction transaktio = null;
		try (Session istunto = istuntotehdas.openSession()) {
			transaktio = istunto.beginTransaction();
			String hql = "delete from Asiakas";
			@SuppressWarnings("rawtypes")
			Query kysely = istunto.createQuery(hql);
			int rivit = kysely.executeUpdate();
			Trace.out(Trace.Level.INFO, "Sarakkeita tyhjennetty: " + rivit);
			transaktio.commit();
		} catch (Exception e) {
			if (transaktio != null) {
				transaktio.rollback();
			}
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**	{@inheritDoc} */
	@Override
	public boolean deletePalvelupisteet() { //tämä metodi poistaa kaikki kerätyt Palvelupiste tiedot
		Transaction transaktio = null;
		try (Session istunto = istuntotehdas.openSession()) {
			transaktio = istunto.beginTransaction();
			String hql = "delete from Palvelupiste";
			@SuppressWarnings("rawtypes")
			Query kysely = istunto.createQuery(hql);
			int rivit = kysely.executeUpdate();
			Trace.out(Trace.Level.INFO, "Sarakkeita tyhjennetty: " + rivit);
			transaktio.commit();
		} catch (Exception e) {
			if (transaktio != null) {
				transaktio.rollback();
			}
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**	{@inheritDoc} */
	@Override
	public boolean updateAsiakas(Asiakas uusiAsiakas) { //tämä metodi ei ole käytössä
		Transaction transaktio = null;
		try (Session istunto = istuntotehdas.openSession()) {
			transaktio = istunto.beginTransaction();
			Asiakas paivitettavaAsiakas = uusiAsiakas;
			istunto.update(paivitettavaAsiakas);
			transaktio.commit();
		} catch(Exception e) {
			if (transaktio != null) {
				transaktio.rollback();
			}
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**	{@inheritDoc} */
	@Override
	public boolean updatePalvelupiste(Palvelupiste uusiPalvelupiste) { //tämä metodi ei ole käytössä
		Transaction transaktio = null;
		try (Session istunto = istuntotehdas.openSession()) {
			transaktio = istunto.beginTransaction();
			Palvelupiste paivitettavaPalvelupiste = uusiPalvelupiste;
			istunto.update(paivitettavaPalvelupiste);
			transaktio.commit();
		} catch(Exception e) {
			if (transaktio != null) {
				transaktio.rollback();
			}
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
