
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

import ast.*;
import exeptions.ParserException;
import variable_info.TokenInfo;
import variable_info.Tokens;
import variable_info.VariableInfo;

public class ParserAnalyzer {
    private List<TokenInfo> tokens;
    private int currentTokenIndex;
    private TokenInfo currentToken;
    private Map<String, VariableInfo> symbolTable;
    private boolean isNotDeclared;
    private boolean isNotInitialized;
    private boolean duplicate;
    private String id;
    private int stringCounter;

    public ParserAnalyzer(List<TokenInfo> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.stringCounter = 0;
        this.symbolTable = new LinkedHashMap<>();
        this.isNotDeclared = false;
        this.isNotInitialized = false;
        this.duplicate = false;
        this.id = null;
        if (!tokens.isEmpty())
            this.currentToken = tokens.get(currentTokenIndex);
    }

    public ASTNode parseProgram() throws ParserException {
        List<ASTNode> statements = new ArrayList<>();

        while (hasNext()) {
            if (isType()) {
                statements.addAll(parseDeclaration());
            } else if (currentToken.getNumber() == Tokens.IDENTIFIER) {
                statements.add(parseAssignment());
            } else if (currentToken.getNumber() == Tokens.BDZTOUSER) {
                statements.add(parsePrint());
            } else {
                throw new ParserException("Error de sintaxis en el token: ", currentToken);
            }
        }

        if (statements.isEmpty())
            throw new ParserException("Error: No se encontraron declaraciones o asignaciones.", currentToken);

        return new ProgramNode(statements);
    }

    private boolean isType() {
        return currentToken.getNumber() == Tokens.INT || currentToken.getNumber() == Tokens.STRING;
    }

    private List<ASTNode> parseDeclaration() throws ParserException {
        String tipo = currentToken.getValue();
        List<ASTNode> declarations = new ArrayList<>();

        do {
            currentToken = getNextToken(); // id

            String variable = currentToken.getValue();
            if (symbolTable.containsKey(variable)) {
                duplicate = true;
                id = variable;
            } else {
                int bytes = tipo.equals("int") ? 2 : tipo.equals("String") ? 80 : 0;
                symbolTable.put(variable, new VariableInfo(tipo, bytes));
            }

            if (hasNext()) {
                currentToken = getNextToken();
            }

            if (currentToken.getNumber() == Tokens.ASSIGN) {
                currentToken = getNextToken();
                ASTNode expression = parseExpression();

                String expresionStr = expressionToString(expression);
                symbolTable.get(variable).setValue(expresionStr);
                if (tipo.equals("String")) {
                    symbolTable.get(variable).setBytes(expresionStr.length() - 2);
                }

                declarations.add(new DeclarationNode(variable, tipo, expression));
            } else {
                declarations.add(new DeclarationNode(variable, tipo));
            }

        } while (currentToken.getNumber() == Tokens.COMA);

        return declarations;
    }

    private AssignmentNode parseAssignment() throws ParserException {
        String variable = currentToken.getValue();
        currentToken = getNextToken();

        if (!symbolTable.containsKey(variable)) {
            isNotDeclared = true;
            id = variable;
        }
        if (currentToken.getNumber() != Tokens.ASSIGN) {
            throw new ParserException("Se esperaba el operador de asignación '='", currentToken);
        }

        currentToken = getNextToken();
        ASTNode expression = parseExpression();

        if (symbolTable.containsKey(variable)) {
            String expresionStr = expressionToString(expression);
            if (!symbolTable.get(variable).isInitialized())
                symbolTable.get(variable).setValue(expresionStr);
            else if (symbolTable.get(variable).getType().equals("String")) {
                String temp = expresionStr + "_str";
                symbolTable.put(temp, new VariableInfo("String", expresionStr, expresionStr.length() - 2));
            }

        }

        return new AssignmentNode(variable, expression);
    }

    private PrintNode parsePrint() throws ParserException {
        currentToken = getNextToken(); // parentesis izquierdo

        if (currentToken.getNumber() != Tokens.LPAR) {
            throw new ParserException("Se esperaba un paréntesis izquierdo '('.", currentToken);
        }

        List<ASTNode> expressions = new ArrayList<>();
        do {
            currentToken = getNextToken(); // expresión
            ASTNode exp = parseExpression();
            if (exp instanceof LiteralNode && ((LiteralNode) exp).getValue().startsWith("\"")) {
                stringCounter++;
                symbolTable.put("str" + stringCounter,
                        new VariableInfo("String", exp.toString(), exp.toString().length() - 2));
            }
            expressions.add(exp);

        } while (currentToken.getNumber() == Tokens.COMA);

        if (currentToken.getNumber() != Tokens.RPAR) {
            throw new ParserException("Se esperaba un paréntesis derecho ')'.", currentToken);
        }

        if (hasNext())
            currentToken = getNextToken();

        return new PrintNode(expressions);
    }

    private ASTNode parseExpression() throws ParserException {
        ASTNode term = parseTerm();

        while (currentToken.getNumber() == Tokens.SUM || currentToken.getNumber() == Tokens.REST) {
            String operador = currentToken.getValue();
            currentToken = getNextToken();
            ASTNode rightTerm = parseTerm();
            term = new BinaryOperationNode(operador, term, rightTerm);
        }
        return term;
    }

    private ASTNode parseTerm() throws ParserException {
        ASTNode factor = parseFactor();

        while (currentToken.getNumber() == Tokens.MULT || currentToken.getNumber() == Tokens.ASSIGN) {
            String operador = currentToken.getValue();
            currentToken = getNextToken();
            ASTNode rightFactor = parseFactor();
            factor = new BinaryOperationNode(operador, factor, rightFactor);
        }

        return factor;
    }

    private ASTNode parseFactor() throws ParserException {
        if (currentToken.getNumber() == Tokens.NUMBER) {
            String value = currentToken.getValue();
            if (hasNext()) {
                currentToken = getNextToken();
            }
            return new LiteralNode(value);
        } else if (currentToken.getNumber() == Tokens.IDENTIFIER) {
            String variable = currentToken.getValue();
            if (!symbolTable.get(variable).isInitialized()) {
                isNotInitialized = true;
                id = variable;
            }
            if (hasNext()) {
                currentToken = getNextToken();
            }
            return new VariableNode(variable);
        } else if (currentToken.getNumber() == Tokens.LPAR) {
            currentToken = getNextToken();
            ASTNode expression = parseExpression();
            if (currentToken.getNumber() != Tokens.RPAR) {
                throw new ParserException("Se esperaba un paréntesis derecho ')'", currentToken);
            }
            if (hasNext())
                currentToken = getNextToken();

            return expression;
        } else if (currentToken.getNumber() == Tokens.QUOTE) {
            StringBuilder stringLiteral = new StringBuilder();
            stringLiteral.append(currentToken.getValue());
            currentToken = getNextToken();

            while (currentToken.getNumber() != Tokens.QUOTE) {
                stringLiteral.append(currentToken.getValue());
                currentToken = getNextToken();
                stringLiteral.append(currentToken.getValue());
            }
            if (hasNext())
                currentToken = getNextToken();

            return new LiteralNode(stringLiteral.toString());
        } else {
            throw new ParserException("Error de sintaxis en el factor: ", currentToken);
        }
    }

    private String expressionToString(ASTNode expression) {
        if (expression instanceof LiteralNode) {
            return ((LiteralNode) expression).getValue();
        } else if (expression instanceof VariableNode) {
            return ((VariableNode) expression).getName();
        } else if (expression instanceof BinaryOperationNode) {
            BinaryOperationNode binOp = (BinaryOperationNode) expression;
            return expressionToString(binOp.getIzquierda()) + " " +
                    binOp.getOperador() + " " +
                    expressionToString(binOp.getDerecha());
        }
        return "";
    }

    private TokenInfo getNextToken() {
        if (currentTokenIndex < tokens.size() - 1) {
            currentTokenIndex++;
            return tokens.get(currentTokenIndex);
        } else {
            return null;
        }
    }

    public Map<String, VariableInfo> getSymbolTable() {
        return symbolTable;
    }

    public boolean getIsNotDeclared() {
        return isNotDeclared;
    }

    public boolean getDuplicated() {
        return duplicate;
    }

    public String getId() {
        return id;
    }

    public boolean getIsNotInitialized() {
        return isNotInitialized;
    }

    private boolean hasNext() {
        return currentTokenIndex < tokens.size() - 1;
    }
}
