
import java.util.ArrayList;
import java.util.List;

public class ScannerAnalyzer {

    String input;
    StringBuilder token;
    ArrayList<TokenInfo> tokensOutput;

    public ScannerAnalyzer(String input) {
        this.input = input;
        token = new StringBuilder();
        tokensOutput = new ArrayList<>();
    }

    public List<TokenInfo> lexer() {
        for (int i = 0; i < input.length(); i++) {
            token.setLength(0);
            char currentChar = input.charAt(i);
            if (Character.isWhitespace(currentChar) || currentChar == '\n') {
                continue;
            }

            if (Character.isLetter(currentChar))
                i = itsIdentifierOrPR(i);
            else if (Character.isDigit(currentChar))
                i = itsNumber(i);
            else if (currentChar == Tokens.EQUAL_CHAR
                    || currentChar == Tokens.GREATER_THAN_CHAR
                    || currentChar == Tokens.LESS_THAN_CHAR)
                i = itsComplexSymbol(currentChar, i);
            else
                itsSimpleSymbol(currentChar);
        }
        return tokensOutput;
    }

    private int itsComplexSymbol(char currentChar, int i) {
        token.append(currentChar);
        i++;
        TokenInfo tokenType;
        if (i < input.length() && input.charAt(i) == Tokens.EQUAL_CHAR) {
            token.append(input.charAt(i));
            String complexSymbol = token.toString();
            tokenType = Tokens.COMPLEX_SYMBOLS.get(complexSymbol); // LESSTHANEQUAL or GREATERTHANEQUAL
        } else {
            i--;
            tokenType = Tokens.SYMBOLS.get(currentChar);
        }
        tokensOutput.add(tokenType);
        return i;
    }

    private int itsNumber(int i) {
        while (i < input.length() && Character.isDigit(input.charAt(i))) {
            token.append(input.charAt(i));
            i++;
        }
        i--;
        tokensOutput.add(new TokenInfo(Tokens.TokenType.NUMBER, Tokens.NUMBER, token.toString()));
        return i;
    }

    private int itsIdentifierOrPR(int i) {
        while (i < input.length() && Character.isLetterOrDigit(input.charAt(i))) {
            token.append(input.charAt(i));
            i++;
        }
        i--;
        String tokenStr = token.toString();
        if (Tokens.KEYWORDS.containsKey(tokenStr)) {
            tokensOutput.add(Tokens.KEYWORDS.get(tokenStr));
        } else {
            tokensOutput.add(new TokenInfo(Tokens.TokenType.IDENTIFIER, Tokens.IDENTIFIER, tokenStr));
        }
        return i;
    }

    private void itsSimpleSymbol(char symbol) {
        if (Tokens.SYMBOLS.containsKey(symbol)) {
            tokensOutput.add(Tokens.SYMBOLS.get(symbol));
        } else {
            tokensOutput
                    .add(new TokenInfo(Tokens.TokenType.INVALID_TOKEN, Tokens.INVALID_TOKEN, String.valueOf(symbol)));
        }
    }
}
