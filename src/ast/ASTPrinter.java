package ast;

import java.util.LinkedHashMap;
import java.util.Map;

import variable_info.VariableInfo;

public class ASTPrinter implements ASTVisitor<Void> {
    private final Map<String, VariableInfo> symbolTable;
    private int stringCounter = 0;

    public ASTPrinter() {
        this.symbolTable = new LinkedHashMap<>();
    }

    public Map<String, VariableInfo> getSymbolTable() {
        return symbolTable;
    }

    @Override
    public Void visitDeclarationNode(DeclarationNode node) {
        String variable = node.getVariable();
        Object initialValue = resolveValue(node.getValorInicial());

        if (initialValue == null) {
            symbolTable.put(variable, new VariableInfo(node.getTipo(), 4));
        } else {
            symbolTable.put(variable, new VariableInfo(node.getTipo(), initialValue.toString(), 4));
        }
        System.out.println("DeclarationNode: " + variable + " = " + (initialValue != null ? initialValue : "null"));

        return null;
    }

    @Override
    public Void visitAssignmentNode(AssignmentNode node) {
        String variable = node.getVariable();
        Object value = resolveValue(node.getExpresion());

        if (symbolTable.containsKey(variable)) {
            symbolTable.get(variable).setValue(value != null ? value.toString() : null);
            System.out.println("AssignmentNode: " + variable + " = " + value);
        } else {
            System.out.println("Variable " + variable + " no declarada");
        }

        return null;
    }

    @Override
    public Void visitBinaryOperationNode(BinaryOperationNode node) {
        Object leftValue = resolveValue(node.getIzquierda());
        Object rightValue = resolveValue(node.getDerecha());
        String operation = leftValue + " " + node.getOperador() + " " + rightValue;

        System.out.println("BinaryOperationNode: " + operation);

        return null;
    }

    @Override
    public Void visitLiteralNode(LiteralNode node) {
        System.out.println("LiteralNode: " + node.getValue());
        return null;
    }

    @Override
    public Void visitVariableNode(VariableNode node) {
        System.out.println("VariableNode: " + node.getName());
        return null;
    }

    @Override
    public Void visitPrintNode(PrintNode node) {
        System.out.println("PrintNode: " + node.getExpresiones());
        for (ASTNode expression : node.getExpresiones()) {
            if (expression instanceof LiteralNode && ((LiteralNode) expression).getValue().startsWith("\"")) {
                stringCounter++;
                symbolTable.put("str" + stringCounter,
                        new VariableInfo("String", expression.toString(), expression.toString().length()));
            }
            expression.accept(this);
        }
        return null;
    }

    @Override
    public Void visitProgramNode(ProgramNode node) {
        System.out.println("ProgramNode");
        for (ASTNode statement : node.getStatements()) {
            statement.accept(this);
        }
        return null;
    }

    // Métodos auxiliares
    private Object resolveValue(ASTNode node) {
        if (node == null) {
            return null;
        } else if (node instanceof LiteralNode) {
            return ((LiteralNode) node).getValue();
        } else if (node instanceof VariableNode) {
            VariableInfo info = symbolTable.get(((VariableNode) node).getName());
            return info != null ? info.getValue() : null;
        } else if (node instanceof BinaryOperationNode) {
            BinaryOperationNode binaryNode = (BinaryOperationNode) node;
            return resolveValue(binaryNode.getIzquierda()) + binaryNode.getOperador()
                    + resolveValue(binaryNode.getDerecha());
        }
        return null; // Otros casos no manejados
    }
}
