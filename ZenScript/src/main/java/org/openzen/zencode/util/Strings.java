/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.util;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;

/**
 *
 * @author Stanneke
 */
public class Strings
{
	private Strings()
	{
	}

	public static String join(String[] values, String separator)
	{
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (String value : values) {
			if (first)
				first = false;
			else
				result.append(separator);
			result.append(value);
		}

		return result.toString();
	}
	
	public static String join(Object[] values, String separator)
	{
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Object value : values) {
			if (first)
				first = false;
			else
				result.append(separator);
			result.append(value);
		}

		return result.toString();
	}

	public static String join(Iterable<String> values, String separator)
	{
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (String value : values) {
			if (first)
				first = false;
			else
				result.append(separator);
			result.append(value);
		}
		return result.toString();
	}

	/**
	 * If a set of methods is available and none matches, this method creates a
	 * suitable message.
	 *
	 * @param <E>
	 * @param methods matching methods
	 * @param arguments calling arguments
	 * @return return value
	 */
	public static <E extends IPartialExpression<E>>
		 String methodMatchingError(List<IMethod<E>> methods, E... arguments)
	{
		if (methods.isEmpty())
			return "no method with that name available";
		else {
			StringBuilder message = new StringBuilder();
			if (methods.size() == 1)
				message.append("a method ");
			else
				message.append(methods.size()).append(" methods ");
			message.append("available but none matches the parameters (");
			boolean first = true;
			
			for (E value : arguments) {
				if (first)
					first = false;
				else
					message.append(", ");
				message.append(value.getType().toString());
			}
			message.append(")");
			return message.toString();
		}
	}

	/**
	 * Splits a string in parts, given a specified delimiter.
	 *
	 * @param value string to be split
	 * @param delimiter delimiter
	 * @return
	 */
	public static String[] split(String value, char delimiter)
	{
		if (value == null)
			return null;

		ArrayList<String> result = new ArrayList<String>();
		int start = 0;
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == delimiter) {
				result.add(value.substring(start, i));
				start = i + 1;
			}
		}
		result.add(value.substring(start));
		return result.toArray(new String[result.size()]);
	}

	/**
	 * Processes a doc comment into paragraphs.
	 *
	 * @param value input value
	 * @return output paragraphs
	 */
	public static String[] splitParagraphs(String value)
	{
		String[] lines = split(value, '\n');

		for (int i = 0; i < lines.length; i++) {
			lines[i] = lines[i].trim();
		}

		ArrayList<String> output = new ArrayList<String>();
		StringBuilder current = new StringBuilder();
		for (String line : lines) {
			if (line.isEmpty()) {
				if (current.length() > 0) {
					output.add(current.toString());
					current = new StringBuilder();
				}
			} else {
				if (current.length() > 0)
					current.append(' ');
				
				current.append(line);
			}
		}
		
		if (current.length() > 0)
			output.add(current.toString());

		return output.toArray(new String[output.size()]);
	}

	// ########################
	// ### String utilities ###
	// ########################
	
	public static class DistanceCosts {
		public final double replace;
		public final double insert;
		public final double delete;
		public final double transpose;
		
		public DistanceCosts(double replace, double insert, double delete, double transpose)
		{
			this.replace = replace;
			this.insert = insert;
			this.delete = delete;
			this.transpose = transpose;
		}
	}
	
	public static final DistanceCosts COSTS_1 = new DistanceCosts(1, 1, 1, 1);
	public static final DistanceCosts COSTS_TYPO = new DistanceCosts(1, 1, 1, 1);
	
	/**
	 * 
	 * 
	 * @author("X Wang")
	 * @param word1
	 * @param word2
	 * @param costs
	 * @return 
	 * @source(http://www.programcreek.com/2013/12/edit-distance-in-java/)
	 */
	public static double minDistance(String word1, String word2, DistanceCosts costs)
	{
		int len1 = word1.length();
		int len2 = word2.length();
		
		// len1+1, len2+1, because finally return dp[len1][len2]
		double[][] dp = new double[len1 + 1][len2 + 1];

		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}

		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}

		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);

				//if last two chars equal
				if (c1 == c2) {
					//update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					double replace = dp[i][j] + costs.replace;
					double insert = dp[i][j + 1] + costs.insert;
					double delete = dp[i + 1][j] + costs.delete;
					
					double min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					
					if (i > 0 && j > 0 && word1.charAt(i) == word2.charAt(i - 1)
							&& word1.charAt(i - 1) == word2.charAt(j)
							&& word1.charAt(i) != word2.charAt(j))
				        min = Math.min(min, dp[i - 1][j - 1] + costs.transpose);
					
					dp[i + 1][j + 1] = min;
				}
			}
		}

		return dp[len1][len2];
	}

	public static String unescape(String escapedString)
	{
		if (escapedString.length() < 2)
			throw new IllegalArgumentException("String is not quoted");
		
		boolean isLiteral = escapedString.charAt(0) == '@';
		if (isLiteral)
			escapedString = escapedString.substring(1);
		
		if (escapedString.charAt(0) != '"' && escapedString.charAt(0) != '\'')
			throw new IllegalArgumentException("String is not quoted");
		
		char quoteCharacter = escapedString.charAt(0);
		if (escapedString.charAt(escapedString.length() - 1) != quoteCharacter)
			throw new IllegalArgumentException("Unbalanced quotes");
		
		if (isLiteral)
			return escapedString.substring(1, escapedString.length() - 1);
		
		StringBuilder result = new StringBuilder(escapedString.length() - 2);
		
		for (int i = 1; i < escapedString.length() - 1; i++) {
			if (!isLiteral && escapedString.charAt(i) == '\\')
			{
				if (i >= escapedString.length() - 1)
					throw new IllegalArgumentException("Unfinished escape sequence");
				
				switch (escapedString.charAt(i + 1)) {
					case '\\': i++; result.append('\\'); break;
					case '&': i++; result.append('&'); break;
					case 't': i++; result.append('\t'); break;
					case 'r': i++; result.append('\r'); break;
					case 'n': i++; result.append('\n'); break;
					case 'b': i++; result.append('\b'); break;
					case 'f': i++; result.append('\f'); break;
					case '"': i++; result.append('\"'); break;
					case '\'': i++; result.append('\''); break;
					case 'u':
						if (i >= escapedString.length() - 5)
							throw new IllegalArgumentException("Unfinished escape sequence");
						int hex0 = readHex(escapedString.charAt(i + 2));
						int hex1 = readHex(escapedString.charAt(i + 3));
						int hex2 = readHex(escapedString.charAt(i + 4));
						int hex3 = readHex(escapedString.charAt(i + 5));
						i += 5;
						result.append((hex0 << 12) | (hex1 << 8) | (hex2 << 4) | hex3);
					default:
						throw new IllegalArgumentException("Illegal escape sequence");
				}
			}
			else
				result.append(escapedString.charAt(i));
		}
		
		return result.toString();
	}
	
	private static int readHex(char hex)
	{
		if (hex >= '0' && hex <= '9')
			return hex - '0';
		
		if (hex >= 'A' && hex <= 'F')
			return hex - 'A' + 10;
		
		if (hex >= 'a' && hex <= 'f')
			return hex - 'a' + 10;
		
		throw new IllegalArgumentException("Illegal hex character: " + hex);
	}
}
