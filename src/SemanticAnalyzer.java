
import java.util.Map;

public class SemanticAnalyzer {
    private Map<String, VariableInfo> symbolTable;
    private final String msgError;

    public SemanticAnalyzer(Map<String, VariableInfo> symbolTable) {
        this.symbolTable = symbolTable;
        this.msgError = "Error: Type mismatch";
    }

    public boolean semantic() throws SemanticException {
        symbolTable.forEach((identifier, variableInfo) -> {
            if (!variableInfo.isInitialized())
                return;

            String value = variableInfo.getValue();
            if (symbolTable.containsKey(value)) { // id assigned to another id
                VariableInfo assignedVariable = symbolTable.get(value);
                if (!checkTypeID(identifier, assignedVariable)) {
                    throw new SemanticException(
                            msgError + " between " + identifier + " and " + value + ".");
                }
            } else {
                checkTypeLiteral(identifier, value);
            }
        });
        return true;
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
                throw new SemanticException(msgError + ". '" + value + "' is not an integer.");
            }
        } else if (type.equals("String") && (!value.startsWith("\"") || !value.endsWith("\""))) {
            throw new SemanticException(msgError + ". '" + value + "' is not a string.");
        }
        return false;
    }

    public Map<String, VariableInfo> getSymbolTable() {
        return symbolTable;
    }
}
