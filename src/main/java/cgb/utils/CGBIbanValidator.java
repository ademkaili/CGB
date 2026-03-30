package cgb.utils;

import org.apache.commons.validator.routines.IBANValidator;

public class CGBIbanValidator {

	private static CGBIbanValidator instance;

	private CGBIbanValidator() {

	}

	/**
	 * Singleton de CGBIbanValidator
	 * 
	 * @return instance unique
	 */
	public static CGBIbanValidator getInstanceValidator() {
		if (instance == null) {
			instance = new CGBIbanValidator();
		}
		return instance;
	}

	// • Code du pays : Deux lettres représentant le pays (par exemple, "FR" pour la
	// France).
	// • Chiffres de contrôle : Deux chiffres utilisés pour la validation de l'IBAN.
	// • BBAN (Basic Bank Account Number) : Le numéro de compte bancaire de base,
	// dont la structure
	// varie selon le pays. Il peut inclure le code de la banque, le code de
	// l'agence, et le numéro de
	// compte.
	/**
	 * @param iban
	 * 
	 * @return Si la structure de l'iban est valide: true
	 */
	public boolean isIbanStructureValid(String iban) {
		if (iban.matches("^FR[0-9]{25}$")) {
			return true;
		}
		return false;
	}

	/**
	 * @param iban
	 * 
	 * @return Si la structure et les données de l'iban sont valide (verification du
	 *         CRC): true
	 */
	public boolean isIbanValid(String iban) {
		IBANValidator validator = IBANValidator.getInstance();
		if (validator.isValid(iban)) {
			return true;
		}
		return false;
	}

	/**
	 * @param iban
	 * 
	 * @return Le pays de l'iban
	 */
	public String getCountryCode(String iban) {
		return iban.substring(0, 2);
	}

	/**
	 * @param iban
	 * 
	 * @return Le CRC d'un iban 
	 */
	public String getCheckDigits(String iban) {
		return iban.substring(2, 4);
	}

	/**
	 * @param iban
	 * 
	 * @return Le BBAN d'un iban
	 */
	public String getBBAN(String iban) {
		return iban.substring(4);
	}

}
