package ast;

public class BinaryOperationNode extends ASTNode {
    private String operador;
    private ASTNode izquierda;
    private ASTNode derecha;

    public BinaryOperationNode(String operador, ASTNode izquierda, ASTNode derecha) {
        this.operador = operador;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    public String getOperador() {
        return operador;
    }

    public ASTNode getIzquierda() {
        return izquierda;
    }

    public ASTNode getDerecha() {
        return derecha;
    }

    public String toString() {
        return "BinaryOperationNode(" + operador + ", " + izquierda + ", " + derecha + ")";
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitBinaryOperationNode(this);
    }
}
