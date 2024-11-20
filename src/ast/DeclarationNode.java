package ast;

public class DeclarationNode extends ASTNode {
    private String variable;
    private String tipo;
    private ASTNode valorInicial;

    public DeclarationNode(String variable, String tipo) {
        this.variable = variable;
        this.tipo = tipo;
    }

    public DeclarationNode(String variable, String tipo, ASTNode valorInicial) {
        this.variable = variable;
        this.tipo = tipo;
        this.valorInicial = valorInicial;
    }

    public String getVariable() {
        return variable;
    }

    public String getTipo() {
        return tipo;
    }

    public ASTNode getValorInicial() {
        return valorInicial;
    }

    public String toString() {
        if (valorInicial != null) {
            return "Declaración(" + variable + ", " + tipo + ", " + valorInicial + ")";
        } else {
            return "Declaración(" + variable + ", " + tipo + ")";
        }
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitDeclarationNode(this);
    }
}
