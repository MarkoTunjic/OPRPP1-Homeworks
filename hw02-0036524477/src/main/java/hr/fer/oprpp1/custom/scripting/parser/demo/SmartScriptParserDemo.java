package hr.fer.oprpp1.custom.scripting.parser.demo;

import hr.fer.oprpp1.custom.scripting.nodes.*;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

public class SmartScriptParserDemo {
	public static void main(String[] args) {
		String docBody = "This is sample text.\n" + "{$ FOR i 1 10 1 $}\n"
				+ "	This is {$= i $}-th time this message is generated.\n" + "{$END$}\n" + "{$FOR i 0 10 $}\n"
				+ "	sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\n" + "{$END$}";
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = document.toString();
		System.out.println(originalDocumentBody); // should write something like original
	}
}
