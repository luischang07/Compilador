
import java.util.List;
import java.util.Queue;

import java.util.LinkedList;

public class Parser {
    private Queue<TokenInfo> tokens;
    private TokenInfo token;
    private boolean goToNextTk = true;

    public Parser(List<TokenInfo> tokensList) {
        this.tokens = new LinkedList<>(tokensList);
    }

    public boolean parseProgram() {
        try {
            while (hasNext()) {
                if (goToNextTk) {
                    token = getNextToken();
                    System.out.println("Program:   " + token.getName() + " " + token.getValue());
                    goToNextTk = true;
                }
                if (!parseStatement()) {
                    return false;
                }
            }
            return true;
        } catch (ParseException e) {
            e.showMessage();
            return false;
        }
    }

    private boolean parseStatement() {
        System.out.println("Statement:   " + token.getName() + " " + token.getValue());
        if (isType()) {
            return parseDeclaration();
        } else if (isTokenType(Tokens.IDENTIFIER)) {
            return parseAssignment();
        } else if (isTokenType(Tokens.BDZTOUSER)) {
            return parsePrint();
        } else
            throw new ParseException("Error: Invalid statement", token);
    }

    public boolean parseDeclaration() {
        do {
            System.out.println("Declaration:   " + token.getName() + " " + token.getValue());
            token = advanceToken("Error: Invalid declaration");
            if (!isTokenType(Tokens.IDENTIFIER)) {
                throw new ParseException("Error: Expected identifier in declaration", token);
            }
            if (!hasNext())
                return true;
            token = getNextToken();
            if (!isTokenType(Tokens.COMA)) {
                if (!isTokenType(Tokens.ASSIGN)) {
                    goToNextTk = false;
                    return parseStatement();
                }
                if (!parseTerms()) {
                    throw new ParseException("Error: Invalid declaration term ", token);
                }
                if (hasNext())
                    token = advanceToken("Error: Invalid declaration");
            }
        } while (isTokenType(Tokens.COMA));
        goToNextTk = false;
        return true;
    }

    public boolean parseAssignment() {
        System.out.println("Assignment:   " + token.getName() + " " + token.getValue());
        token = advanceToken("Error: Invalid assignment");
        if (isTokenType(Tokens.ASSIGN) && parseTerms()) {
            return true;
        }
        goToNextTk = false;
        throw new ParseException("Error: Invalid assignment", token);
    }

    private boolean parseString() {
        if (!isTokenType(Tokens.QUOTE)) {
            throw new ParseException("Error: Invalid string expected \"", token);
        }
        token = advanceToken("Error: Invalid string expected Identifier or Number");
        do {
            if (!hasNext()) {
                throw new ParseException("Error: Invalid string expected \"", token);
            }
            if (!(isTokenType(Tokens.IDENTIFIER) || isTokenType(Tokens.NUMBER))) {
                throw new ParseException("Error: Invalid string expected Identifier or Number", token);
            }
            token = getNextToken();
            if (isTokenType(Tokens.QUOTE)) {
                System.out.println("String:   " + token.getName() + " " + token.getValue());
                return true;
            }
        } while (isTokenType(Tokens.IDENTIFIER) || isTokenType(Tokens.NUMBER));

        throw new ParseException("Error: Invalid string", token);
    }

    private boolean isFactor() {
        return isTokenType(Tokens.NUMBER) || isTokenType(Tokens.IDENTIFIER) || parseString();
    }

    public boolean parseTerms() {
        token = advanceToken("Error: Invalid Term");
        System.out.println("Term:   " + token.getName() + " " + token.getValue());
        if (!isFactor()) {
            throw new ParseException("Error: Invalid factor: ", token);
        }
        if (!hasNext()) {
            return true;
        }
        if (isOperator(peekNextToken())) {
            token = getNextToken();
            return parseTerms();
        }
        goToNextTk = true;
        return true;
    }

    private byte peekNextToken() {
        return tokens.peek().getNumber();
    }

    public boolean parsePrint() {
        token = advanceToken("Error: Invalid print statement");
        if (!isTokenType(Tokens.LPAR))
            throw new ParseException("Error: Invalid print Expected '('", token);
        // if (!hasNext())
        // throw new ParseException("Error: Invalid print expected a TERM", token);
        do {
            if (!parseTerms()) {
                throw new ParseException("Error: Invalid Term statement", token);
            }
            token = advanceToken("Error: Invalid print statement");
            if (isTokenType(Tokens.RPAR)) {
                goToNextTk = true;
                return true;
            }
        } while (isTokenType(Tokens.COMA) && hasNext());

        throw new ParseException("Error: Invalid print statement", token);
    }

    // -----------Helper methods--------------------
    private boolean isTokenType(byte expectedType) {
        return token.getNumber() == expectedType;
    }

    private boolean isType() {
        return token.getNumber() == Tokens.INT || token.getNumber() == Tokens.STRING;
    }

    private boolean isOperator(byte number) {
        return number == Tokens.SUM || number == Tokens.REST || number == Tokens.MULT;
    }

    private TokenInfo getNextToken() {
        return tokens.poll();
    }

    private boolean hasNext() {
        return !tokens.isEmpty();
    }

    private TokenInfo advanceToken(String message) {
        if (!hasNext()) {
            throw new ParseException(message, token);
        }
        return getNextToken();
    }
}
