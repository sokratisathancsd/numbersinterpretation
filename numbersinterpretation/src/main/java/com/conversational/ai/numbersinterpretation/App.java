/**
 * @author Sokratis Athanasiadis
 * Number Interpretation JAVA Project 2024
 * 
 * Run executable with 
 * $java -Dlog4j.configurationFile=/home/athan/eclipse-workspace/numbersinterpretation/src/main/resources/log4j2.xml -jar NaturalNumbersInterpretation.jar
 */
package com.conversational.ai.numbersinterpretation;

import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

	private static final Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args) {

		AmbiguitiesGenerator generator = new AmbiguitiesGenerator();
		PhoneNumberValidator phoneValidator = new PhoneNumberValidator();

		Scanner scanner = new Scanner(System.in);
		String input;

		while (true) {
			logger.info("Enter phone number (or type 'exit' to quit): ");
			input = scanner.nextLine();

			// Remove leading spaces
			input = input.replaceAll("^\\s+", "");
			// Remove extra spaces
			input = input.replaceAll("\\s{2,}", " ");

			if (input.equalsIgnoreCase("exit")) {
				break;
			}

			// Check if the input contains only digits
			if (!input.matches("[\\d\\s]+")) {
				logger.info("Invalid input. Please enter only digits and spaces.");
				continue;
			}

			List<String> ambiguities = generator.generateAmbiguities(input);

			for (int i = 0; i < ambiguities.size(); i++) {
				logger.info("Interpretation: {}: {}  {}", i + 1, ambiguities.get(i),
						phoneValidator.isValid(ambiguities.get(i)) ? "[phone number: VALID]"
								: "[phone number: INVALID]");
			}
		}

		scanner.close();

	}

}
