package akiko.ruokalasimu.model;

/**
 * Testaukseen ja konsolitulosteiden luontiin tehty luokka.
 * Tulosteita tulostuu vain jos ohjelman alussa asetettu Trace level on sama tai suurempi kuin ohjelmassa tulosteille asetetut Trace tasot.
 * 
 * @author Aki koppinen
 * @version 1.1 (18.10.2020)
 */
public class Trace {

	/**
	 * Trace tasot enum luokassa.
	 * INFO on pienin.
	 * WAR on keskimmäinen.
	 * ERR on suurin.
	 * 
	 * @author Aki Koppinen
	 * @version 1.1 (18.10.2020)
	 */
	public enum Level{
		/**
		 * Information.
		 */
		INFO,
		/**
		 * Warning.
		 */
		WAR,
		/**
		 * Error.
		 */
		ERR
	}
	
	/**
	 * Trace tason muuttuja.
	 */
	private static Level traceLevel;
	
	/**
	 * Globaalin Trace tason asettaminen halutuksi.
	 * @param lvl Haluttu Trace taso.
	 */
	public static void setTraceLevel(Level lvl){
		traceLevel = lvl;
	}
	/**
	 * Annetun tekstin tulostaminen tulosteen trace taso vastaa asetettua globaalia trace tasoa.
	 * 
	 * @param lvl verrattava trace taso.
	 * @param txt tulostettava teksti.
	 */
	public static void out(Level lvl, String txt){
		if (lvl.ordinal() >= traceLevel.ordinal()){
			System.out.println(txt);
		}
	}
	
}