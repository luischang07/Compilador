
import java.util.Map;

import variable_info.VariableInfo;
import ast.*;

//intel 8086 assembly code
public class AssemblyCodeGenerator implements ASTVisitor<String> {
    private Map<String, VariableInfo> symbolTable;
    private int stringCounter;
    private String int21h;
    boolean helperProcedures = false;

    public AssemblyCodeGenerator(Map<String, VariableInfo> symbolTable) {
        this.symbolTable = symbolTable;
        this.stringCounter = 0;
        int21h = "    int 21h\n";
    }

    @Override
    public String visitProgramNode(ProgramNode node) {
        StringBuilder code = new StringBuilder();

        code.append(".model small\n");
        code.append(".stack 100h\n");
        code.append(".data\n");

        for (Map.Entry<String, VariableInfo> entry : symbolTable.entrySet()) {
            String varName = entry.getKey().replace("\"", "");
            VariableInfo info = entry.getValue();

            if (!entry.getValue().isInitialized() || containsOperator(entry.getValue().getValue())) {
                if (info.getType().equals("int")) {
                    code.append(varName).append("\t dw \t ?\n");
                } else if (info.getType().equals("String")) {
                    code.append(varName).append("\t db \t 80 dup($)\n");
                }
            } else {
                if (info.getType().equals("int")) {
                    code.append(varName).append("\t dw  \t" + info.getValue() + "\n");
                } else if (info.getType().equals("String")) {
                    code.append(varName).append("\t db  \t'" + info.getValue().replace("\"", "")
                            + "$'\n"); // + (80 - info.getBytes()) + " dup('$')\n");
                }
            }
        }

        code.append("temp \t dw \t ?\n");
        code.append("buffer \t db \t 80 dup('$')\n\n");

        code.append(".code\n");
        code.append("main proc\n");
        code.append("    mov ax, @data\n");
        code.append("    mov ds, ax\n\n");

        for (ASTNode statement : node.getStatements()) {
            code.append(statement.accept(this));
        }

        code.append("\n    mov ah, 4Ch\n");
        code.append(int21h);
        code.append("main endp\n");
        if (helperProcedures) {
            code.append(getHelperProcedures());
        }
        code.append("end main\n");

        return code.toString();
    }

    private boolean containsOperator(String value) {
        return value.contains("+") || value.contains("-") || value.contains("*");
    }

    @Override
    public String visitDeclarationNode(DeclarationNode node) {
        if (node.getValorInicial() instanceof BinaryOperationNode) {
            return node.getValorInicial().accept(this);
        }
        return "";
    }

    @Override
    public String visitAssignmentNode(AssignmentNode node) {
        return generateAssignment(node.getVariable(), node.getExpresion());
    }

    private String generateAssignment(String variable, ASTNode expression) {
        StringBuilder code = new StringBuilder();
        code.append("\n; Asignación a ").append(variable).append("\n");

        // Comprobamos si la variable es de tipo String y la expresión es un LiteralNode
        if (symbolTable.get(variable).getType().equals("String") && expression instanceof LiteralNode) {
            // Obtener la cadena literal
            String value = ((LiteralNode) expression).getValue();

            // Comenzamos a generar el código para copiar la cadena
            for (Map.Entry<String, VariableInfo> entry : symbolTable.entrySet()) {
                if (entry.getValue().isInitialized() && entry.getValue().getValue().equals(value)) {
                    code.append("    lea si, ").append(entry.getKey().replace("\"", "")).append("\n");
                    break;
                }
            }
            code.append("    lea di, ").append(variable + "\n");
            code.append("    mov cx, ").append(value.length() + 1);
            code.append("\ncopy_loop:\n");
            code.append("    mov al, [si]   \n");
            code.append("    mov [di], al   \n");
            code.append("    inc si         \n");
            code.append("    inc di         \n");
            code.append("    loop copy_loop \n");

        } else {
            // Si no es una cadena literal, se sigue el comportamiento original
            code.append(expression.accept(this));
            code.append("    mov ").append(variable).append(", ax\n");
        }

        return code.toString();
    }

    @Override
    public String visitPrintNode(PrintNode node) {
        StringBuilder code = new StringBuilder();
        code.append("\n; Print statement\n");

        for (ASTNode expr : node.getExpresiones()) {
            if (expr instanceof VariableNode) {
                String varName = ((VariableNode) expr).getName();
                VariableInfo info = symbolTable.get(varName);

                if (info.getType().equals("String")) {
                    code.append("    lea dx, ").append(varName).append("\n");
                    code.append("    mov ah, 09h\n");
                    code.append(int21h);
                } else {
                    // Imprimir número
                    code.append("    mov ax, ").append(varName).append("\n");
                    code.append("    call ConvertirMostrarNum\n");
                    helperProcedures = true;

                }
            } else if (expr instanceof LiteralNode) {
                String value = ((LiteralNode) expr).getValue();
                if (value.startsWith("\"")) {
                    String stringLabel = null;
                    for (Map.Entry<String, VariableInfo> entry : symbolTable.entrySet()) {
                        if (entry.getValue().isInitialized() && entry.getValue().getValue().equals(value)) {
                            stringLabel = entry.getKey();
                            break;
                        }
                    }

                    // Imprimir
                    code.append("    lea dx, ").append(stringLabel).append("\n");
                    code.append("    mov ah, 09h\n");
                    code.append(int21h);
                } else {
                    code.append("    mov ax, '").append(value).append("'\n");
                    code.append("    call ConvertirMostrarNum\n");
                    helperProcedures = true;
                }
            }
        }

        return code.toString();
    }

    @Override
    public String visitBinaryOperationNode(BinaryOperationNode node) {
        StringBuilder code = new StringBuilder();

        code.append(";Operación binaria\n");
        code.append(node.getIzquierda().accept(this));
        code.append("    push ax\n");

        code.append(node.getDerecha().accept(this));
        code.append("    mov bx, ax\n");
        code.append("    pop ax\n");

        switch (node.getOperador()) {
            case "+":
                code.append("    add ax, bx\n");
                break;
            case "-":
                code.append("    sub ax, bx\n");
                break;
            case "*":
                code.append("    mul bx\n");
                break;
            default:
                break;
        }

        return code.toString();
    }

    @Override
    public String visitLiteralNode(LiteralNode node) {
        return "    mov ax, " + node.getValue() + "\n";
    }

    @Override
    public String visitVariableNode(VariableNode node) {
        return "    mov ax, " + node.getName() + "\n";
    }

    private String getHelperProcedures() {
        StringBuilder procs = new StringBuilder();

        procs.append("\nConvertirMostrarNum proc\n");
        procs.append("    push bx\n");
        procs.append("    push si\n");
        procs.append("    push di\n");

        procs.append("    mov si, offset buffer\n");
        procs.append("    mov bx, 10    \n");
        procs.append("convertir:\n");
        procs.append("    mov dx, 0          \n");
        procs.append("    div bx             \n");
        procs.append("    add dl, '0'        \n");
        procs.append("    mov [si], dl       \n");
        procs.append("    inc si             \n");
        procs.append("    cmp ax, 0          \n");
        procs.append("    jne convertir\n");

        procs.append("    mov di, offset buffer  \n");
        procs.append("    dec si                 \n");
        procs.append("invertir:\n");
        procs.append("    cmp di, si\n");
        procs.append("    jae mostrar_num\n");
        procs.append("    mov al, [di]\n");
        procs.append("    mov ah, [si]\n");
        procs.append("    mov [di], ah\n");
        procs.append("    mov [si], al\n");
        procs.append("    inc di\n");
        procs.append("    dec si\n");
        procs.append("    jmp invertir\n");

        procs.append("mostrar_num:\n");
        procs.append("    mov dx, offset buffer\n");
        procs.append("    mov ah, 9\n");
        procs.append("    int 21h\n");

        procs.append("    ; Limpiar buffer para siguiente uso\n");
        procs.append("    mov cx, 6\n");
        procs.append("    mov si, offset buffer\n");
        procs.append("    mov al, '$'\n");
        procs.append("limpiar:\n");
        procs.append("    mov [si], al\n");
        procs.append("    inc si\n");
        procs.append("    loop limpiar\n");

        procs.append("    pop di\n");
        procs.append("    pop si\n");
        procs.append("    pop bx\n");
        procs.append("    ret\n");
        procs.append("ConvertirMostrarNum endp\n");

        return procs.toString();
    }

}