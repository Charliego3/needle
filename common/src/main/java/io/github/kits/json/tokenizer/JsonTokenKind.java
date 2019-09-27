package io.github.kits.json.tokenizer;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public interface JsonTokenKind {

	/**
	 * Left Big Parantheses '{'
	 */
	byte LEFT_BIG_PARANTHESES = 123;

	/**
	 * Right Big Parantheses '}'
	 */
	byte RIGHT_BIG_PARANTHESES = 125;

	/**
	 * Left Bracket '['
	 */
	byte LEFT_BRACKET = 91;

	/**
	 * Right Bracket ']'
	 */
	byte RIGHT_BRACKET = 93;

	/**
	 * Colon ':'
	 */
	byte COLON = 58;

	/**
	 * Comma ','
	 */
	byte COMMA = 44;

	/**
	 * Double quote '"'
	 */
	byte DOUBLE_QUOTE = 34;

	/**
	 * Line feed character
	 */
	byte LF = 10;

	/**
	 * Carriage return
	 */
	byte CR = 13;

	/**
	 * Horizontal space
	 */
	byte SP = 32;

	/**
	 * Slash '/'
	 */
	byte SLASH = 47;

	/**
	 * STAR '*'
	 */
	byte STAR = 42;

	/**
	 * Null string first 'n'
	 */
	byte N = 110;

	/**
	 * False string first 'f'
	 */
	byte F = 102;

	/**
	 * True string first 't'
	 */
	byte T = 116;

	/**
	 * Negative '-'
	 */
	byte NEGATIVE = 45;

	/**
	 * Zero '0'
	 */
	byte ZERO = 48;

	/**
	 * One '1'
	 */
	byte ONE = 49;

	/**
	 * Two '2'
	 */
	byte TWO = 50;

	/**
	 * Three '3'
	 */
	byte THREE = 51;

	/**
	 * Four '4'
	 */
	byte FOUR = 52;

	/**
	 * Five '5'
	 */
	byte FIVE = 53;

	/**
	 * Six '6'
	 */
	byte SIX = 54;

	/**
	 * Seven '7'
	 */
	byte SEVEN = 55;

	/**
	 * Eight '8'
	 */
	byte EIGHT = 56;


	/**
	 * Nine '9'
	 */
	byte NINE = 57;

}
