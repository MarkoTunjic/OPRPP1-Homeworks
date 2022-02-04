package hr.fer.oprpp1.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class LexerTest {

	@Test
	void testLexerFromFile1() {
		Lexer lexer = new Lexer(readExample(1));
		assertNotNull(lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getType());
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testLexerFromFile2() {
		Lexer lexer = new Lexer(readExample(2));
		assertNotNull(lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getType());
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testLexerFromFile3() {
		Lexer lexer = new Lexer(readExample(3));
		assertNotNull(lexer.nextToken());
		assertEquals(TokenType.EOF, lexer.nextToken().getType());
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testLexerFromFile4() {
		Lexer lexer = new Lexer(readExample(4));
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testLexerFromFile5() {
		Lexer lexer = new Lexer(readExample(5));
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testLexerFromFile6() {
		Lexer lexer = new Lexer(readExample(6));
		assertEquals(TokenType.TEXT, lexer.nextToken().getType());
		assertEquals(TokenType.OPEN_TAG, lexer.nextToken().getType());
		lexer.setState(LexerState.TAG);
		assertEquals(TokenType.TAG_NAME, lexer.nextToken().getType());
		lexer.setState(LexerState.INSIDE_TAG);
		assertEquals(TokenType.STRING, lexer.nextToken().getType());
		assertEquals(TokenType.CLOSE_TAG, lexer.nextToken().getType());
		lexer.setState(LexerState.TEXT);
		assertEquals(TokenType.EOF, lexer.nextToken().getType());
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testLexerFromFile7() {
		Lexer lexer = new Lexer(readExample(7));
		assertEquals(TokenType.TEXT, lexer.nextToken().getType());
		assertEquals(TokenType.OPEN_TAG, lexer.nextToken().getType());
		lexer.setState(LexerState.TAG);
		assertEquals(TokenType.TAG_NAME, lexer.nextToken().getType());
		lexer.setState(LexerState.INSIDE_TAG);
		assertEquals(TokenType.STRING, lexer.nextToken().getType());
		assertEquals(TokenType.CLOSE_TAG, lexer.nextToken().getType());
		lexer.setState(LexerState.TEXT);
		assertEquals(TokenType.EOF, lexer.nextToken().getType());
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testLexerFromFile8() {
		Lexer lexer = new Lexer(readExample(8));
		assertNotNull(lexer.nextToken());
		assertNotNull(lexer.nextToken());
		lexer.setState(LexerState.TAG);
		assertNotNull(lexer.nextToken());
		lexer.setState(LexerState.INSIDE_TAG);
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	@Test
	void testLexerFromFile9() {
		Lexer lexer = new Lexer(readExample(9));
		assertNotNull(lexer.nextToken());
		assertNotNull(lexer.nextToken());
		lexer.setState(LexerState.TAG);
		assertNotNull(lexer.nextToken());
		lexer.setState(LexerState.INSIDE_TAG);
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	/**
	 * A method that reads test cases from a txt file
	 * 
	 * @param the index of the file to be read
	 * @return the String that represents the whole file
	 * @throws RuntimeException if there is no file or error while reading
	 */
	private String readExample(int n) {
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer" + n + ".txt")) {
			if (is == null)
				throw new RuntimeException("Datoteka extra/primjer" + n + ".txt je nedostupna.");
			byte[] data = is.readAllBytes();
			String text = new String(data, StandardCharsets.UTF_8);
			return text;
		} catch (IOException ex) {
			throw new RuntimeException("Greška pri čitanju datoteke.", ex);
		}
	}

}
