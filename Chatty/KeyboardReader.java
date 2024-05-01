//package csci1140;

import java.io.*;

/**
 * <p>Title: CSCI 1140 Basic data entry class.</p>
 * <p>Description: This class provides a set of methods which read from the
 * keyboard and return specific values.<br>It is intended for use by CSCI 1140
 * students as part of the regular class, lab, and homework programming
 * assignments.</p>
 * <p>Copyright Benedictine College(c) 2021</p>
 * @author Nicolaas tenBroek
 * @version 1.0
 */

public final class KeyboardReader {
	private static BufferedReader in;

	static {
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * Reads the words &quot;true&quot; and &quot;false&quot; from the keyboard.
	 * Re-prompts for data on any invalid entry.
	 * @param prompt The String to display to the user describing the requested data.
	 * @return boolean
	 */
	public static final boolean readBoolean(String prompt) {
		while(true) {
			// try {
			// 	return Boolean.parseBoolean(readLine(prompt));
			// } catch(Exception e) {
			// 	System.out.println();
			// 	System.out.println("Please enter either true or false.");
			// }
			String input = readLine(prompt);
			if(input.equalsIgnoreCase("true")) {
				return true;
			} else if(input.equalsIgnoreCase("false")) {
				return false;
			} else {
				System.out.println("Please enter either \"true\" or \"false\".");
			}
		}
	}

	/**
	 * Reads the words &quot;true&quot; and &quot;false&quot; from the keyboard.
	 * Prompts for data on any invalid entry.
	 * @return boolean
	 */
	public static final boolean readBoolean() {
		while(true) {
			String input = readLine();
			if(input.equalsIgnoreCase("true")) {
				return true;
			} else if(input.equalsIgnoreCase("false")) {
				return false;
			} else {
				System.out.println("Please enter either \"true\" or \"false\".");
			}
		}
	}

	/**
	 * Reads a byte value (between -128 and 127) from the keyboard.
	 * Re-prompts for data on any invalid entry.
	 * @param prompt The String to display to the user describing the requested data.
	 * @return byte
	 */
	public static final byte readByte(String prompt) {
		while(true) {
			try {
				return Byte.parseByte(readLine(prompt));
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter an integer value between -128 and 127.");
			}
		}
	}
	
	/**
	 * Reads a byte value (between -128 and 127) from the keyboard.
	 * Prompts for data on any invalid entry.
	 * @return byte
	 */
	public static final byte readByte() {
		while(true) {
			try {
				return Byte.parseByte(readLine());
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter an integer value between -128 and 127.");
			}
		}
	}

	/**
	 * Reads a single character from the keyboard.
	 * Re-prompts for data on any invalid entry.
	 * @param prompt The String to display to the user describing the requested data.
	 * @return char
	 */
	public static final char readChar(String prompt) {
		while(true) {
			try {
				return readLine(prompt).charAt(0);
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter a single character.");
			}
		}
	}
	
	/**
	 * Reads a single character from the keyboard.
	 * Prompts for data on any invalid entry.
	 * @return char
	 */
	public static final char readChar() {
		while(true) {
			try {
				return readLine().charAt(0);
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter a single character.");
			}
		}
	}

	/**
	 * Reads a double value from the keyboard.
	 * Re-prompts for data on any invalid entry.
	 * @param prompt The String to display to the user describing the requested data.
	 * @return double
	 */
	public static final double readDouble(String prompt) {
		while(true) {
			try {
				return Double.parseDouble(readLine(prompt));
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter a real number.");
			}
		}
	}

	/**
	 * Reads a double value from the keyboard.
	 * Prompts for data on any invalid entry.
	 * @return double
	 */
	public static final double readDouble() {
		while(true) {
			try {
				return Double.parseDouble(readLine());
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter a real number.");
			}
		}
	}

	/**
	 * Reads a floating point value from the keyboard.
	 * Re-prompts for data on any invalid entry.
	 * @param prompt The String to display to the user describing the requested data.
	 * @return float
	 */
	public static final float readFloat(String prompt) {
		while(true) {
			try {
				return Float.parseFloat(readLine(prompt));
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter a real number.");
			}
		}
	}

	/**
	 * Reads a floating point value from the keyboard.
	 * Prompts for data on any invalid entry.
	 * @return float
	 */
	public static final float readFloat() {
		while(true) {
			try {
				return Float.parseFloat(readLine());
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter a real number.");
			}
		}
	}

	/**
	 * Reads an integer value from the keyboard.
	 * Re-prompts for data on any invalid entry.
	 * @param prompt The String to display to the user describing the requested data.
	 * @return int
	 */
	public static final int readInt(String prompt) {
		while(true) {
			try {
				return Integer.parseInt(readLine(prompt));
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter an integer value between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE + ".");
			}
		}
	}

	/**
	 * Reads an integer value from the keyboard.
	 * Prompts for data on any invalid entry.
	 * @return int
	 */
	public static final int readInt() {
		while(true) {
			try {
				return Integer.parseInt(readLine());
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter an integer value between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE + ".");
			}
		}
	}

	/**
	 * Reads a string of text from the keyboard.
	 * Re-prompts for data on any invalid entry.
	 * @param prompt The String to display to the user describing the requested data.
	 * @return String
	 */
	public static final String readLine(String prompt) {
		while(true) {
			try {
				System.out.println(prompt);
				String input = in.readLine();
				if(input.length() <= 0) {
					System.out.println();
					System.out.println("Please enter some text.");
				} else {
					return input;
				}
			} catch(Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}
	/**
	 * Reads a string of text from the keyboard.
	 * Prompts for data on any invalid entry.
	 * @return String
	 */
	public static final String readLine() {
		// while(true) {
			try {
				return in.readLine();
			} catch(Exception e) {
				System.out.println(e.getLocalizedMessage());
				System.out.println("Please enter something before pressing return.");
			}
		// }
		return "";
	}

	/**
	 * Reads a long integer value from the keyboard.
	 * Re-prompts for data on any invalid entry.
	 * @param prompt The String to display to the user describing the requested data.
	 * @return long
	 */
	public static final long readLong(String prompt) {
		while(true) {
			try {
				return Long.parseLong(readLine(prompt));
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter an integer value between " + Long.MIN_VALUE + " and " + Long.MAX_VALUE + ".");
			}
		}
	}

	/**
	 * Reads a long integer value from the keyboard.
	 * Prompts for data on any invalid entry.
	 * @return long
	 */
	public static final long readLong() {
		while(true) {
			try {
				return Long.parseLong(readLine());
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter an integer value between " + Long.MIN_VALUE + " and " + Long.MAX_VALUE + ".");
			}
		}
	}

	/**
	 * Reads a short integer value from the keyboard.
	 * Re-prompts for data on any invalid entry.
	 * @param prompt The String to display to the user describing the requested data.
	 * @return short
	 */
	public static final short readShort(String prompt) {
		while(true) {
			try {
				return Short.parseShort(readLine(prompt));
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter an integer value between " + Short.MIN_VALUE + " and " + Short.MAX_VALUE + ".");
			}
		}
	}

	/**
	 * Reads a short integer value from the keyboard.
	 * Prompts for data on any invalid entry.
	 * @return short
	 */
	public static final short readShort() {
		while(true) {
			try {
				return Short.parseShort(readLine());
			} catch(Exception e) {
				System.out.println();
				System.out.println("Please enter an integer value between " + Short.MIN_VALUE + " and " + Short.MAX_VALUE + ".");
			}
		}
	}

	/**
	 * This main is only provided to test the input methods of the class.
	 * @param args - not used.
	 */
	public static void main(String[] args) {
		System.out.println("You entered: " + readBoolean("Enter a boolean value"));
		System.out.println("You entered: " + readByte("Enter a byte value"));
		System.out.println("You entered: " + readChar("Enter a char value"));
		System.out.println("You entered: " + readDouble("Enter a double value"));
		System.out.println("You entered: " + readFloat("Enter a float value"));
		System.out.println("You entered: " + readInt("Enter an int value"));
		System.out.println("You entered: " + readLine("Enter a string"));
		System.out.println("You entered: " + readLong("Enter a long value"));
		System.out.println("You entered: " + readShort("Enter a short value"));
	}

	private KeyboardReader() {
	}
}