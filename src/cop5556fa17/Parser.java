package cop5556fa17;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;
import static cop5556fa17.Scanner.Kind.*;

public class Parser {

	@SuppressWarnings("serial")
	public class SyntaxException extends Exception {
		Token t;

		public SyntaxException(Token t, String message) {
			super(message);
			this.t = t;
		}

	}

	Scanner scanner;
	Token t;

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * Main method called by compiler to parser input. Checks for EOF
	 * 
	 * @throws SyntaxException
	 */
	public void parse() throws SyntaxException {
		program();
		matchEOF();
	}

	/**
	 * Program ::= IDENTIFIER ( Declaration SEMI | Statement SEMI )*
	 * 
	 * Program is start symbol of our grammar.
	 * 
	 * @throws SyntaxException
	 */
	void program() throws SyntaxException {
		// TODO implement this
		matchToken(IDENTIFIER);

		if (t.kind == KW_int || t.kind == KW_boolean) {
			variableDeclaration();
		} else if (t.kind == KW_image) {
			imageDeclaration();
		} else if (t.kind == KW_url || t.kind == KW_file) {
			sourceSinkDeclaration();
		}
		throw new UnsupportedOperationException();
	}
	
	void variableDeclaration() throws SyntaxException {
		varType();
		matchToken(IDENTIFIER);
		if (t.kind == OP_ASSIGN) {
			matchToken(OP_ASSIGN);
			expression();
		}
	}

	void varType() throws SyntaxException {
		switch (t.kind) {
		case KW_boolean:
			matchToken(KW_boolean);
			break;
		case KW_int:
			matchToken(KW_int);
			break;
		default:
			throw new SyntaxException(t, "Illegal varType");
		}
	}
	
	void imageDeclaration() throws SyntaxException {
		matchToken(KW_image);
		if (t.kind == LSQUARE) {
			matchToken(LSQUARE);
			expression();
			matchToken(COMMA);
			expression();
			matchToken(RSQUARE);
		} else {
			matchToken(IDENTIFIER);
			if (t.kind == OP_LARROW) {
				matchToken(OP_LARROW);
				source();
			}
		}
	}
	
	void sourceSinkDeclaration() throws SyntaxException {
		
	}
	
	void source() throws SyntaxException {
		switch (t.kind) {
		case STRING_LITERAL:
			matchToken(STRING_LITERAL);
			break;
		case OP_AT:
			matchToken(OP_AT);
			expression();
			break;
		case IDENTIFIER:
			matchToken(IDENTIFIER);
			break;
		default:
			throw new SyntaxException(t, "Illegal Source");
		}
	}

	/**
	 * Expression ::= OrExpression OP_Q Expression OP_COLON Expression |
	 * OrExpression
	 * 
	 * Our test cases may invoke this routine directly to support incremental
	 * development.
	 * 
	 * @throws SyntaxException
	 */
	void expression() throws SyntaxException {
		// TODO implement this.
		throw new UnsupportedOperationException();
	}

	/**
	 * Only for check at end of program. Does not "consume" EOF so no attempt to get
	 * nonexistent next Token.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (t.kind == EOF) {
			return t;
		}
		String message = "Expected EOL at " + t.line + ":" + t.pos_in_line;
		throw new SyntaxException(t, message);
	}

	private Token matchToken(Kind kind) throws SyntaxException {
		if (t.kind == kind) {
			return consume();
		}
		throw new SyntaxException(t, "Got token of kind " + t.kind + " instead of " + kind);
	}

	private Token matchToken(Kind... kinds) throws SyntaxException {
		for (Kind k : kinds) {
			if (k == t.kind) {
				return consume();
			}
		}

		throw new SyntaxException(t, "Invalid Tokens");
	}

	private Token consume() throws SyntaxException {
		Token tempToken = t;
		t = scanner.nextToken();
		return tempToken;
	}
}
