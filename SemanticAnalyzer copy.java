
import java.util.HashMap;
import java.util.Map;

public class SemanticAnalyzer {
    private Map<String, VariableInfo> symbolTable;
    private final String msgError;

    public SemanticAnalyzer() {
        symbolTable = new HashMap<>();
        msgError = "Error: Variable '";
    }

    public void declareVariable(String identifier, String type) {
        if (symbolTable.containsKey(identifier)) {
            throw new SemanticException(msgError + identifier + "' already declared.");
        }
        symbolTable.put(identifier, new VariableInfo(type));
    }

    public boolean isDeclared(String identifier) {
        return symbolTable.containsKey(identifier);
    }

    public VariableInfo lookupVariable(String identifier) {
        if (!isDeclared(identifier))
            throw new SemanticException(msgError + identifier + "' is undeclared.");
        else if (!symbolTable.get(identifier).isInitialized())
            throw new SemanticException(msgError + identifier + "' may not initialized.");

        return symbolTable.get(identifier);
    }

    public void initializeVariable(String identifier, String value) {
        VariableInfo id = symbolTable.get(identifier);
        if (id.isInitialized()) {
            throw new SemanticException(msgError + identifier + "' already initialized.");
        }

        id.setInitialized(true);
        id.setValue(value);
        symbolTable.put(identifier, id);
    }

    public void initializeVariable(String identifier, VariableInfo variable) {
        if (!variable.isInitialized()) {
            throw new SemanticException(msgError + variable + "' may not initialized.");
        }
        VariableInfo id = symbolTable.get(identifier);
        id.setInitialized(true);
        symbolTable.put(identifier, variable);
    }

    public void checkType(String identifier, String valueAssig) {
        if (!symbolTable.containsKey(identifier)) {
            throw new SemanticException(msgError + identifier + "' is undeclared.");
        }
        String type = symbolTable.get(identifier).getType();
        if (symbolTable.containsKey(valueAssig)) { // id assigned to another variable
            String typeAssig = symbolTable.get(valueAssig).getType();
            if (!type.equals(typeAssig)) {
                throw new SemanticException(
                        "Error: Type mismatch" + identifier + " and " + valueAssig + " are not the same type");
            }
            symbolTable.get(identifier).setValue(valueAssig);
            return;
        }

        if (type.equals("int")) {
            try {
                Integer.parseInt(valueAssig);
            } catch (NumberFormatException e) {
                throw new SemanticException("Error: Type mismatch " + valueAssig + " is not an integer");
            }
        }

        symbolTable.get(identifier).setValue(valueAssig);
    }

    public Map<String, VariableInfo> getSymbolTable() {
        return symbolTable;
    }
}
