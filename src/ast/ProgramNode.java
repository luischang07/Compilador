package ast;

import java.util.List;

public class ProgramNode extends ASTNode {
    private List<ASTNode> statements; // Lista de declaraciones o asignaciones

    public ProgramNode(List<ASTNode> statements) {
        this.statements = statements;
    }

    public List<ASTNode> getStatements() {
        return statements;
    }

    public String toString() {
        return "Program(" + statements + ")";
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitProgramNode(this);
    }
}
