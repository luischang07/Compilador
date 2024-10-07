
import java.util.List;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;

import java.util.LinkedList;

public class ParserAnalyzer {
    private Queue<TokenInfo> tokens;
    private TokenInfo token;
    private boolean goToNextTk;

    private String type;
    private StringBuilder str;
    private String identifier;

    private Map<String, VariableInfo> symbolTable;
    private String id;
    private int bytes;
    private boolean isNotDeclared;
    private boolean duplicated;

    public ParserAnalyzer(List<TokenInfo> tokensList) {
        this.tokens = new LinkedList<>(tokensList);
        symbolTable = new HashMap<>();
        isNotDeclared = false;
        goToNextTk = true;
    }

    public boolean parseProgram() throws ParserException {
        while (hasNext()) {
            if (goToNextTk) {
                token = getNextToken();
                System.out.println("Program:   " + token.getName() + " " + token.getValue());
                // goToNextTk = true;
            }
            if (!parseStatement()) {
                return false;
            }
        }
        return true;
    }

    private boolean parseStatement() {
        System.out.println("Statement:   " + token.getName() + " " + token.getValue());
        if (isType()) {
            type = token.getValue();
            if (token.isTokenType(Tokens.INT))
                bytes = 4;
            else if (token.isTokenType(Tokens.STRING))
                bytes = 80;
            return parseDeclaration();
        } else if (token.isTokenType(Tokens.IDENTIFIER)) {
            identifier = token.getValue();
            return parseAssignment();
        } else if (token.isTokenType(Tokens.BDZTOUSER)) {
            return parsePrint();
        } else
            throw new ParserException("Error: Invalid statement", token);
    }

    public boolean parseDeclaration() {
        do {
            System.out.println("Declaration:   " + token.getName() + " " + token.getValue());
            token = advanceToken("Error: Invalid declaration");
            if (!token.isTokenType(Tokens.IDENTIFIER)) {
                throw new ParserException("Error: Expected identifier in declaration", token);
            }

            identifier = token.getValue();
            if (symbolTable.containsKey(identifier) && symbolTable.get(identifier).getType() != null) {
                id = identifier;
                duplicated = true;
            }

            symbolTable.put(identifier, new VariableInfo(type, bytes));
            // semanticAnalyzer.declareVariable(identifier, type);

            if (!hasNext())
                return true;
            token = getNextToken();
            if (!token.isTokenType(Tokens.COMA)) {
                if (!token.isTokenType(Tokens.ASSIGN)) {
                    goToNextTk = false;
                    return parseStatement();
                }
                if (!parseTerms()) {
                    throw new ParserException("Error: Invalid declaration term ", token);
                }

                if (token.isTokenType(Tokens.IDENTIFIER) && !symbolTable.containsKey(token.getValue())) {
                    id = token.getValue();
                    isNotDeclared = true;
                }

                symbolTable.put(identifier, new VariableInfo(type, str.toString(), bytes));
                // if (token.isTokenType(Tokens.IDENTIFIER)) {
                // VariableInfo matchedVar = semanticAnalyzer.lookupVariable(token.getValue());
                // semanticAnalyzer.initializeVariable(identifier, matchedVar);
                // }
                // semanticAnalyzer.initializeVariable(identifier, str.toString());

                if (hasNext())
                    token = advanceToken("Error: Invalid declaration");
            }
            bytes = 80;
        } while (token.isTokenType(Tokens.COMA));
        goToNextTk = false;
        return true;
    }

    public boolean parseAssignment() {
        System.out.println("Assignment:   " + token.getName() + " " + token.getValue());
        token = advanceToken("Error: Invalid assignment");
        if (token.isTokenType(Tokens.ASSIGN) && parseTerms()) {
            if (!symbolTable.containsKey(identifier)) {
                id = identifier;
                isNotDeclared = true;
            }
            if (token.isTokenType(Tokens.IDENTIFIER) && !symbolTable.containsKey(token.getValue())) {
                id = token.getValue();
                isNotDeclared = true;
            }

            VariableInfo variableInfo = symbolTable.get(identifier);

            if (variableInfo != null && variableInfo.getType() != null) {
                type = variableInfo.getType();
                // bytes = variableInfo.getBytes();
                symbolTable.put(identifier, new VariableInfo(type, str.toString(), bytes));
            } else {
                symbolTable.put(identifier, new VariableInfo(null, str.toString(), bytes));
            }
            // semanticAnalyzer.checkType(identifier, str.toString());
            return true;
        }

        goToNextTk = false;
        throw new ParserException("Error: Invalid assignment", token);
    }

    private boolean parseString() {
        if (!token.isTokenType(Tokens.QUOTE)) {
            throw new ParserException("Error: Invalid string expected \"", token);
        }
        token = advanceToken("Error: Invalid string expected Identifier or Number");
        str.append(token.getValue());
        do {
            if (!hasNext()) {
                throw new ParserException("Error: Invalid string expected \"", token);
            }
            if (!(token.isTokenType(Tokens.IDENTIFIER) || token.isTokenType(Tokens.NUMBER))) {
                throw new ParserException("Error: Invalid string expected Identifier or Number", token);
            }
            token = getNextToken();
            str.append(" " + token.getValue());
            if (token.isTokenType(Tokens.QUOTE)) {
                str.replace(str.length() - 2, str.length(), token.getValue());
                System.out.println("String:   " + token.getName() + " " + token.getValue());
                return true;
            }
        } while (token.isTokenType(Tokens.IDENTIFIER) || token.isTokenType(Tokens.NUMBER));

        throw new ParserException("Error: Invalid string", token);
    }

    private boolean isFactor() {
        if (token.isTokenType(Tokens.NUMBER)) {
            bytes = 4;
            return true;
        } else if (token.isTokenType(Tokens.IDENTIFIER)) {
            if (symbolTable.containsKey(token.getValue())) {
                VariableInfo variableInfo = symbolTable.get(token.getValue());
                if (variableInfo.getType() != null) {
                    bytes = variableInfo.getBytes();
                    return true;
                }
            }
        } else if (parseString()) {
            bytes = str.length() - 2 + 1;
            System.out.println(str.toString() + " " + bytes);
            return true;
        }
        return false;
    }

    public boolean parseTerms() {
        token = advanceToken("Error: Invalid Term");
        str = new StringBuilder();
        str.append(token.getValue());

        System.out.println("Term:   " + token.getName() + " " + token.getValue());
        if (!isFactor()) {
            throw new ParserException("Error: Invalid factor: ", token);
        }

        if (!hasNext()) {
            return true;
        }
        if (isOperator(peekNextToken())) {
            System.out.println("Operator:   " + token.getName() + " " + token.getValue() + "-----");
            token = getNextToken();
            str.append(" " + token.getValue());
            return parseTerms();
        }
        goToNextTk = true;
        return true;
    }

    public boolean parsePrint() {
        token = advanceToken("Error: Invalid print statement");
        if (!token.isTokenType(Tokens.LPAR))
            throw new ParserException("Error: Invalid print Expected '('", token);
        // if (!hasNext())
        // throw new ParseException("Error: Invalid print expected a TERM", token);
        do {
            if (!parseTerms()) {
                throw new ParserException("Error: Invalid Term statement", token);
            }
            token = advanceToken("Error: Invalid print statement");
            if (token.isTokenType(Tokens.RPAR)) {
                goToNextTk = true;
                return true;
            }
        } while (token.isTokenType(Tokens.COMA) && hasNext());

        throw new ParserException("Error: Invalid print statement", token);
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

    private byte peekNextToken() {
        return tokens.peek().getNumber();
    }

    private TokenInfo advanceToken(String message) {
        if (!hasNext()) {
            throw new ParserException(message, token);
        }
        return getNextToken();
    }

    public Map<String, VariableInfo> getSymbolTable() {
        return symbolTable;
    }

    public boolean getIsNotDeclared() {
        return isNotDeclared;
    }

    public boolean getDuplicated() {
        return duplicated;
    }

    public String getId() {
        return id;
    }
}
