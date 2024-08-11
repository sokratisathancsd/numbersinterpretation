/**
 * @author Sokratis Athanasiadis
 * Number Interpretation JAVA Project 2024
 */
package com.conversational.ai.numbersinterpretation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AmbiguitiesGenerator {

	private static final Logger logger = LogManager.getLogger(AmbiguitiesGenerator.class);

	public List<String> generateAmbiguities(String input) {

		List<String> ambiguities = new ArrayList<>();

		// identify collide ambiguities
		List<String> inputAsList = new ArrayList<>();

		for (String number : Arrays.asList(input.split(" "))) {
			if (number.matches("[2-9]0+")) {
				inputAsList.add("/" + number);
			} else {
				inputAsList.add(number);
			}
		}

		ambiguities.addAll(generateDivideAmbiguities(inputAsList.get(0)));
		logger.debug("generateAmbiguities | ambiguities before the loop: {}", ambiguities);

		for (int i = 1; i < inputAsList.size(); i++) {
			combineLists(ambiguities, generateDivideAmbiguities(inputAsList.get(i)));
		}
		for (String ambiguity : ambiguities) {
			logger.debug("generateAmbiguities | ambiguity: {}", ambiguity);
		}

		// collide ambiguities
		return generateCollideAmbiguities(ambiguities).stream().map(amb -> amb.replace("lead", ""))
				.map(amb -> amb.replace(" ", "")).collect(Collectors.toList());
	}

	private List<String> generateCollideAmbiguities(List<String> ambiguities) {
		List<String> resultList = new ArrayList<>();
		for (String ambiguity : ambiguities) {
			List<String> ambiguityAsList = Arrays.asList(ambiguity.split(" "));
			List<String> resultListInput = new ArrayList<>();
			resultListInput.add("lead");
			for (int i = 0; i < ambiguityAsList.size();) {
				if (ambiguityAsList.get(i).contains("/")) {
					List<String> temp = new ArrayList<>();

					identifySequenceRecursive(temp, ambiguityAsList.subList(i, ambiguityAsList.size()),
							countZeroes(ambiguityAsList.get(i)));

					List<String> collideAmbiguities = generateCollideAmbiguities(
							String.join(" ", temp).replace("/", ""));
					combineLists(resultListInput, collideAmbiguities);
					i += temp.size();
				} else {
					combineLists(resultListInput, List.of(ambiguityAsList.get(i)));
					i++;
				}

			}
			resultList.addAll(resultListInput);
		}
		return resultList.stream().map(amb -> amb.replace("lead", "")).map(amb -> amb.replace(" ", ""))
				.collect(Collectors.toList());
	}

	/**
	 * Recursively identifies and builds a sequence of strings from an input list
	 * based on the presence of zeroes and their length.
	 * 
	 *
	 * Consists of 2 terminal conditions
	 * <ol>
	 * <li>If the zeroes == 0. The remaining character (if any) is part of the
	 * sequence only if it's length is less than the previously added
	 * character.</li>
	 * <li>If the input does not contain a "0". The remaining character (if any) is
	 * part of the sequence only if it's length is less than the previously added
	 * character.</li>
	 * </ol>
	 * 
	 * @param result The list to which matching strings will be added. It is updated
	 *               in place.
	 * @param input  The list of strings to be processed.
	 * @param zeroes The number of zeroes to be searched for in the strings. It
	 *               decrements with each recursive call.
	 * @return The updated {@code result} list containing the strings that meet the
	 *         specified conditions.
	 */
	private List<String> identifySequenceRecursive(List<String> result, List<String> input, Integer zeroes) {

		String input0 = input.get(0);

		if (zeroes == 0) {
			if (input0.replace("/", "").length() < result.get(result.size() - 1).replace("/", "").length()) {
				result.add(input0);
			}
			return result; // TERMINAL CONDITION 1
		}
		if (!input0.contains("0")) {
			if (input0.replace("/", "").length() < result.get(result.size() - 1).replace("/", "").length()) {
				result.add(input0);
			}
			return result; // TERMINAL CONDITION 2
		}

		if (input0.contains("0".repeat(zeroes))) {
			result.add(input0);
			if (zeroes - 1 >= 0) {
				identifySequenceRecursive(result, input.subList(1, input.size()), zeroes - 1);
			}
		}
		return result;
	}

	/**
	 * This method takes as input a number and finds all ambiguities that could be
	 * divided from that number.
	 * 
	 * For example 487 can be interpreted as [400 80 7, 480 7, 400 87, 487].
	 * 
	 * We call this 'divide ambiguities'.
	 * 
	 * The process to calculate the 'divide ambiguities' is the following:
	 * <ul>
	 * <li>Reverse the input number (487-->784)</li>
	 * <li>Store the first digit of the reversed number in divideAmbiguities List
	 * (example: 7)</li>
	 * <li>For each digit in the reversed number repeat:</li>
	 * <ul>
	 * <li>Create an incomingAmbiguities list that consists of 2 elements:</li>
	 * <ul>
	 * <li>[digit] (example: 8)</li>
	 * <li>[digit][i times 0], where i is the index of the digit in the reversed
	 * number (example: 80)</li>
	 * </ul>
	 * <li>Combine divideAmbiguities and incomingAmbiguities to calculate the new
	 * list of divideAmbiguities. (example 807, 87)</li>
	 * </ul>
	 * 
	 * </ul>
	 * 
	 * @param number The input number to find the 'divide ambiguities' for.
	 * @return a List that contains all the 'divide ambiguities' for the input
	 *         number.
	 */
	private List<String> generateDivideAmbiguities(String number) {

		List<String> divideAmbiguities = new ArrayList<>();

		// avoid collide ambiguities
		if (number.contains("/")) {
			return List.of(number);
		}

		// If number is [0,19] or any digit number that has only 0s there are no divide
		// ambiguities
		Integer numIntF = Integer.parseInt(number);
		if (numIntF >= 0 && numIntF <= 19 || (numIntF % 10 == 0)) {
			return List.of(number);
		}

		// take the input number and reverse it
		List<String> inputNumDigitsRev = Arrays.asList(number.split(""));
		Collections.reverse(inputNumDigitsRev);

		divideAmbiguities.add(inputNumDigitsRev.get(0));

		// generate ambiguities
		for (int i = 1; i < inputNumDigitsRev.size(); i++) {
			String zeroes = "0".repeat(i);
			List<String> incomingAmbiguities = List.of(inputNumDigitsRev.get(i) + zeroes, inputNumDigitsRev.get(i));
			combineDivideAmbiguities(divideAmbiguities, incomingAmbiguities);
		}
		logger.debug("generateSingleAmbiguities | singleAmbiguities: {}", divideAmbiguities);

		return divideAmbiguities;
	}

	/**
	 * Combines elements from two lists into a single list and updates the first
	 * list with the combined results.
	 * <p>
	 * This method takes two lists of strings, {@code list1} and {@code list2}. It
	 * creates a new list where each element of {@code list1} is combined with each
	 * element of {@code list2} in a specific manner. Specifically, each element in
	 * {@code list1} is prefixed by the first element of {@code list2} followed by a
	 * space, each element in {@code list1} is prefixed by the second element of
	 * {@code list2}. The original {@code list1} is then replaced with this newly
	 * created combined list.
	 * </p>
	 *
	 * @param list1 The first list to be updated with the combined results. This
	 *              list is cleared and replaced with the new combined list.
	 * @param list2 The second list used to generate the combined results. It must
	 *              contain exactly two elements.
	 */
	private void combineDivideAmbiguities(List<String> list1, List<String> list2) {

		List<String> combinedList = new ArrayList<>();

		for (String l1 : list1) {
			combinedList.add(list2.get(0) + " " + l1);
			combinedList.add(list2.get(1) + l1);
		}

		// Replace List1 with the new combined list
		list1.clear();
		list1.addAll(combinedList);
	}

	/**
	 * Combines each element of the first list with each element of the second list,
	 * and updates the first list with the combined results.
	 * <p>
	 * This method takes two lists of strings, {@code list1} and {@code list2}. It
	 * generates a new list where each string from {@code list1} is concatenated
	 * with each string from {@code list2}, separated by a space. The original
	 * {@code list1} is then cleared and replaced with this newly created combined
	 * list.
	 * </p>
	 *
	 * @param list1 The first list to be updated with the combined results. This
	 *              list is cleared and populated with the new combined list.
	 * @param list2 The second list used to generate the combined results. Each
	 *              element in {@code list1} is combined with every element in
	 *              {@code list2}.
	 */
	private void combineLists(List<String> list1, List<String> list2) {

		List<String> combinedList = new ArrayList<>();

		for (String l1 : list1) {
			for (String l2 : list2) {
				combinedList.add(l1 + " " + l2);
			}
		}

		// Replace List1 with the new combined list
		list1.clear();
		list1.addAll(combinedList);
	}

	private List<String> generateCollideAmbiguities(String input) {

		List<String> collideAmbiguities = new ArrayList<>();

		List<String> inputNumbers = Arrays.asList(input.split(" "));

		collideAmbiguities.add(inputNumbers.get(0));

		for (int i = 1; i < inputNumbers.size(); i++) {
			List<String> tempCollideAmbiguities = new ArrayList<>();
			for (String ambiguity : collideAmbiguities) {
				// sum
				Integer temp = Integer.parseInt(ambiguity) + Integer.parseInt(inputNumbers.get(i));
				tempCollideAmbiguities.add(temp.toString());
				// collide
				tempCollideAmbiguities.add(ambiguity + inputNumbers.get(i));
			}
			collideAmbiguities.clear();
			collideAmbiguities.addAll(tempCollideAmbiguities);
		}

		return collideAmbiguities;
	}

	/**
	 * Counts the number of occurrences of the character '0' in a given string.
	 * <p>
	 * This method calculates the number of '0' characters in the input string by
	 * comparing the length of the original string with the length of the string
	 * after all '0' characters have been removed.
	 * </p>
	 *
	 * @param str The input string in which to count occurrences of '0'.
	 * @return The number of occurrences of '0' in the input string.
	 * @throws NullPointerException if {@code str} is {@code null}.
	 */
	private int countZeroes(String str) {
		return str.length() - str.replace("0", "").length();
	}

}
