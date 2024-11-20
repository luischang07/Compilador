package ast;

public class AssignmentNode extends ASTNode {
    private String variable;
    private ASTNode expresion;

    public AssignmentNode(String variable, ASTNode expresion) {
        this.variable = variable;
        this.expresion = expresion;
    }

    public String getVariable() {
        return variable;
    }

    public ASTNode getExpresion() {
        return expresion;
    }

    public String toString() {
        return "Asignación(" + variable + ", " + expresion + ")";
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitAssignmentNode(this);
    }
}
