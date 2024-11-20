package ast;

import java.util.List;

public class PrintNode extends ASTNode {
    private List<ASTNode> expresiones; // Cambiado a una lista de expresiones

    public PrintNode(List<ASTNode> expresiones) {
        this.expresiones = expresiones; // Almacena la lista de expresiones
    }

    public List<ASTNode> getExpresiones() {
        return expresiones; // Método para obtener la lista de expresiones
    }

    public String toString() {
        return "Print(" + expresiones + ")"; // Cambiado a Print(expresiones)
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitPrintNode(this); // Llama al visitante para manejar PrintNode
    }
}
