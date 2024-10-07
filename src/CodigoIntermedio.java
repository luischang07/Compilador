
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CodigoIntermedio {

    private Map<String, VariableInfo> symbolTable;

    public CodigoIntermedio(Map<String, VariableInfo> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public List<String> getCodigoIntermedio() {
        List<String> codigoIntermedio = new ArrayList<>();
        symbolTable.forEach((key, value) -> {
            if (!value.isInitialized()) {
                if (value.getType().equals("int")) {
                    codigoIntermedio.add(key + "\tdd\t?");
                } else if (value.getType().equals("String")) {
                    codigoIntermedio.add(key + "\tdb\t" + value.getBytes() + " dup('$')");
                }
                return;
            }
            if (value.getType().equals("int")) {
                codigoIntermedio.add(key + "\tdd\t" + value.getValue());
            } else if (value.getType().equals("String")) {
                String string = value.getValue().replace("\"", "");
                codigoIntermedio.add(key + "\tdb\t '" + string + "$'");
            }
        });
        return codigoIntermedio;
    }

}
// Codigo intermedio:
// ABCD db "Na$" ; String inicial
// Day db "20$" ; String Day
// Bye db 80 dup('$’) ;variable sin inicializar
// msg1 db "TW$" ; String constante
// Four dw ? ; Variable Four inicializada a 3
// Leaf dw ? ; Variable Leaf inicializada a 40
