
import javax.swing.JTextPane;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Modelo {

    SemanticAnalyzer semanticAnalyzer;
    Map<String, VariableInfo> symbolTable;
    boolean isNotDeclared, duplicate;
    String id;

    Logger logger = Logger.getLogger(Modelo.class.getName());

    public Modelo() {
    }

    public void openFile(JTextPane textArea) {
        FileImport fileImport = new FileImport(textArea);
        fileImport.openFile();
    }

    public List<TokenInfo> scanner(String text) {
        ScannerAnalyzer scanner = new ScannerAnalyzer(text);
        return scanner.lexer();
    }

    public boolean syntax(String text) {
        List<TokenInfo> tokens = scanner(text);
        ParserAnalyzer parser = new ParserAnalyzer(tokens);
        symbolTable = parser.getSymbolTable();
        if (parser.parseProgram()) {
            id = parser.getId();
            isNotDeclared = parser.getIsNotDeclared();
            duplicate = parser.getDuplicated();
            return true;
        }
        return false;
    }

    public boolean semantic() throws SemanticException {
        if (isNotDeclared) // in assignment
            throw new SemanticException("Error: Variable '" + id + "' is not declared.");
        if (duplicate) // in declaration
            throw new SemanticException("Error: Variable '" + id + "' is already declared.");

        semanticAnalyzer = new SemanticAnalyzer(symbolTable);
        return semanticAnalyzer.semantic();
    }

    public Map<String, VariableInfo> getSymbolTable() {
        return symbolTable;
    }
}
