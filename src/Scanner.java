import java.util.ArrayList;

public class Scanner {

    String input;

    public Scanner(String input) {
        this.input = input;
    }

    public ArrayList<String> Lexer() {
        ArrayList<String> tokensOutput = new ArrayList<>();
        int length = input.length();
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char currentChar = input.charAt(i);
            if (Character.isWhitespace(currentChar) || currentChar == '\n') {
                continue;
            }

            if (Character.isLetter(currentChar)) {
                token.setLength(0);
                while (i < length && Character.isLetterOrDigit(input.charAt(i))) {
                    token.append(input.charAt(i));
                    i++;
                }
                i--; // Ajustar el índice después del bucle
                String tokenStr = token.toString();
                if (Tokens.KEYWORDS.containsKey(tokenStr)) {
                    tokensOutput.add("Keyword, " + tokenStr);
                } else {
                    tokensOutput.add("Identifier, " + tokenStr);
                }

            } else if (Character.isDigit(currentChar)) {
                token.setLength(0);
                while (i < length && Character.isDigit(input.charAt(i))) {
                    token.append(input.charAt(i));
                    i++;
                }
                i--;
                tokensOutput.add("Number, " + token.toString());
            } else {
                switch (currentChar) {
                    case '+':
                        tokensOutput.add("Operator, SUM");
                        break;
                    case '-':
                        tokensOutput.add("Operator, REST");
                        break;
                    case '*':
                        tokensOutput.add("Operator, MULT");
                        break;
                    case '=':
                        tokensOutput.add("Operator, EQUAL");
                        break;
                    case '!':
                        tokensOutput.add("Operator, DIFF");
                        break;
                    case '<':
                        tokensOutput.add("Operator, LESSTHAN");
                        break;
                    case '>':
                        tokensOutput.add("Operator, GREATERTHAN");
                        break;
                    case ',':
                        tokensOutput.add("Operator, COMA");
                        break;
                    case '"':
                        tokensOutput.add("Quote, QUOTE");
                        break;
                    default:
                        tokensOutput.add("Error, " + currentChar);
                        break;
                }
            }
        }
        return tokensOutput;
    }

}
