/**
 * @author Sokratis Athanasiadis
 * Number Interpretation JAVA Project 2024
 */
package com.conversational.ai.numbersinterpretation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhoneNumberValidator {

	private static final Logger logger = LogManager.getLogger(PhoneNumberValidator.class);

	private static final String GR_2_PREFIX = "2";
	private static final String GR_69_PREFIX = "69";
	private static final String GR_00302_PREFIX = "00302";
	private static final String GR_003069_PREFIX = "003069";

	/**
	 * Validates a phone number based on its length and prefix.
	 * <p>
	 * This method checks if the given phone number is valid according to specific
	 * criteria: - The number should be either 10 or 14 digits long. - If the number
	 * is 10 digits long, it must start with one of the predefined prefixes
	 * {@code GR_2_PREFIX} or {@code GR_69_PREFIX}. - If the number is 14 digits
	 * long, it must start with one of the predefined prefixes
	 * {@code GR_00302_PREFIX} or {@code GR_003069_PREFIX}. The method also trims
	 * any spaces from the input phone number before validation.
	 * </p>
	 *
	 * @param phoneNumber The phone number to be validated. It may include spaces
	 *                    which will be removed before processing.
	 * @return {@code true} if the phone number is valid based on the criteria;
	 *         {@code false} otherwise.
	 * @throws NullPointerException if {@code phoneNumber} is {@code null}.
	 */
	public boolean isValid(String phoneNumber) {

		String number = phoneNumber.replace(" ", "");

		if (number == null || number.isEmpty() || (number.length() != 10 && number.length() != 14)) {
			logger.debug("[phone number: {} INVALID]", number);
			return false;
		}

		if (number.length() == 10) {

			if (number.startsWith(GR_2_PREFIX) || number.startsWith(GR_69_PREFIX)) {
				logger.debug("[phone number: {} VALID]", number);
				return true;
			}
		} else {
			if (number.startsWith(GR_00302_PREFIX) || number.startsWith(GR_003069_PREFIX)) {
				logger.debug("[phone number: {} VALID]", number);
				return true;
			}
		}
		logger.debug("[phone number: {} INVALID]", number);
		return false;
	}

}
