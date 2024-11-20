
import javax.swing.JTextPane;
import javax.xml.crypto.Data;

import ast.ASTNode;
import ast.ASTPrinter;
import ast.BinaryOperationNode;
import ast.PrintNode;
import ast.ProgramNode;
import exeptions.Exceptions;
import variable_info.TokenInfo;
import variable_info.VariableInfo;

import codigo_objeto.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Modelo {

    SemanticAnalyzer semanticAnalyzer;
    Map<String, VariableInfo> symbolTable;
    boolean isNotDeclared;
    boolean isNotInitialized;
    boolean duplicate;
    ASTNode programNode;
    String id;
    String assemblyCode;

    Logger logger = Logger.getLogger(Modelo.class.getName());

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

        programNode = parser.parseProgram();
        ASTPrinter printer = new ASTPrinter();
        programNode.accept(printer);

        // symbolTable = printer.getSymbolTable();
        symbolTable = parser.getSymbolTable();
        System.out.println("Symbol Table: ");
        symbolTable.forEach((key, value) -> System.out
                .println(key + " " + value.getType() + " " + value.getValue() + " " + value.isInitialized() + " "
                        + value.getBytes()));

        if (programNode != null) {
            id = parser.getId();
            isNotDeclared = parser.getIsNotDeclared();
            duplicate = parser.getDuplicated();
            isNotInitialized = parser.getIsNotInitialized();
            return true;
        }
        return false;
    }

    public boolean semantic() throws Exceptions {
        String errVar = "Error: Variable '";
        if (isNotDeclared) // in assignment
            throw new Exceptions(errVar + id + "' is not declared.");
        if (duplicate) // in declaration
            throw new Exceptions(errVar + id + "' is already declared.");
        if (isNotInitialized)
            throw new Exceptions(errVar + id + "' is not initialized.");

        semanticAnalyzer = new SemanticAnalyzer(symbolTable);
        return semanticAnalyzer.semantic();
    }

    public String codigoIntermedio() {
        AssemblyCodeGenerator asemblyCodeGenerator = new AssemblyCodeGenerator(symbolTable);
        assemblyCode = programNode.accept(asemblyCodeGenerator);
        return assemblyCode;
    }

    public String codigoObjeto() {
        CodigoObjeto codigoObjeto = new CodigoObjeto();
        DataSegmentTranslator dataSegmentTranslator = new DataSegmentTranslator();
        StringBuilder binary = new StringBuilder();
        String[] dataCode = assemblyCode.split(".data");
        String[] dataAndCode = dataCode[1].split(".code");
        String[] data = dataAndCode[0].split("\n");
        String[] code = assemblyCode.split("mov ds, ax")[1].split("main endp")[0].split("\n");
        for (String coded : code) {
            System.out.println(coded);
        }

        for (String line : data) {
            binary.append(dataSegmentTranslator.translateDataDeclaration(line));
        }

        CodeSegmentTranslator codeSegmentTranslator = new CodeSegmentTranslator(codigoObjeto.getOpcodeMap(),
                codigoObjeto.getRegisterMap(), dataSegmentTranslator.variableOffsets());

        for (String line : code) {
            binary.append(codeSegmentTranslator.translateInstruction(line));
        }

        return binary.toString();
    }

    public Map<String, VariableInfo> getSymbolTable() {
        return symbolTable;
    }
}
