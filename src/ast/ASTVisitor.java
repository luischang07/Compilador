package ast;

public interface ASTVisitor<T> {
    T visitDeclarationNode(DeclarationNode node);

    T visitAssignmentNode(AssignmentNode node);

    T visitBinaryOperationNode(BinaryOperationNode node);

    T visitLiteralNode(LiteralNode node);

    T visitVariableNode(VariableNode node);

    T visitPrintNode(PrintNode node);

    T visitProgramNode(ProgramNode node);
}
