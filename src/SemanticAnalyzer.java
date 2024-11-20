import java.util.Map;

import exeptions.Exceptions;
import variable_info.VariableInfo;

public class SemanticAnalyzer {
    private Map<String, VariableInfo> symbolTable;
    private final String msgError;

    public SemanticAnalyzer(Map<String, VariableInfo> symbolTable) {
        this.symbolTable = symbolTable;
        this.msgError = "Error: Type mismatch";
    }

    public boolean semantic() throws Exceptions {
        symbolTable.forEach((identifier, variableInfo) -> {
            if (!variableInfo.isInitialized())
                return;

            if (variableInfo.getValue().length() > 80)
                throw new Exceptions(
                        "Error: La cadena '" + variableInfo.getValue() + "' excede el límite de 80 caracteres.");

            String value = variableInfo.getValue();
            if (symbolTable.containsKey(value)) { // id asignado a otro id
                VariableInfo assignedVariable = symbolTable.get(value);
                if (!checkTypeID(identifier, assignedVariable)) {
                    throw new Exceptions(
                            msgError + " entre " + identifier + " y " + value + ".");
                }
            } else if (containsOperator(value)) { // Si la expresión tiene operadores
                checkExpression(identifier, value);
            } else {
                checkTypeLiteral(identifier, value);
            }
        });
        return true;
    }

    private void checkExpression(String identifier, String expression) {
        String[] tokens = expression.split("\\s*[+\\-*]\\s*");

        for (String token : tokens) {
            if (symbolTable.containsKey(token)) { // Es un identificador
                VariableInfo assignedVariable = symbolTable.get(token);
                if (!checkTypeID(identifier, assignedVariable)) {
                    throw new Exceptions(
                            msgError + " entre " + identifier + " y " + token + " en la expresión.");
                }
            } else {
                checkTypeLiteral(identifier, token);
            }
        }
    }

    private boolean checkTypeID(String identifier, VariableInfo assignedVariable) {
        String type = symbolTable.get(identifier).getType();
        return type.equals(assignedVariable.getType());
    }

    private boolean checkTypeLiteral(String identifier, String value) {
        String type = symbolTable.get(identifier).getType();

        if (type.equals("int")) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new Exceptions(msgError + ". '" + value + "' no es un entero.");
            }
        } else if (type.equals("String") && (!value.startsWith("\"") || !value.endsWith("\""))) {
            throw new Exceptions(msgError + ". '" + value + "' no es una cadena válida.");
        }
        return true;
    }

    private boolean containsOperator(String value) {
        return value.contains("+") || value.contains("-") || value.contains("*");
    }

    public Map<String, VariableInfo> getSymbolTable() {
        return symbolTable;
    }
}
