package ast;

public class LiteralNode extends ASTNode {
    private Object value;

    public LiteralNode(Object value) {
        this.value = value;
    }

    public String getValue() {
        return value.toString();
    }

    public Object getRawValue() {
        return value;
    }

    public String toString() {
        return value + "";
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visitLiteralNode(this);
    }
}
