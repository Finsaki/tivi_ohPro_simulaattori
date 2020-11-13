package akiko.ruokalasimu.model;

/**
 * TapahtumanTyyppi luokka on enum luokka joka listaa kullekkin Tapahtumalle mahdolliset tyypit.
 * Tyyppien avulla tapahtumat luokitellaan Moottorin läpikäydessä tapahtumia.
 * 
 * @author Aki Koppinen
 * @version 1.1 (18.10.2020)
 */
public enum TapahtumanTyyppi {
	/** Saapuminen Linjastolle 1. */
	ARR1,
	/** Saapuminen Linjastolle 2. */
	ARR2,
	/** Saapuminen Linjastolle 3. */
	ARR3,
	/** Saapuminen Kahvilaan. */
	ARR4,
	/** Kassalle siirtyminen Linjastolta 1. */
	DEPVALINTA1,
	/** Kassalle siirtyminen Linjastolta 2. */
	DEPVALINTA2,
	/** Kassalle siirtyminen Linjastolta 3. */
	DEPVALINTA3,
	/** Kahvilasta siirtyminen ruokalaan. */
	DEP1,
	/** Kassalta 1 siirtyminen ruokalaan. */
	DEP2,
	/** Kassalta 2 siirtyminen ruokalaan. */
	DEP3,
	/** Ruokalasta poistuminen, eli palvelun päättyminen. */
	DEPFINAL;
}
