import java.util.Map;

public class Tokens {

    // Lista de Tokens disponibles en la gramatica
    public static final String[] TOKENS = {
            // Palabras reservadas
            "PR",
            // Operadores Aritmeticos
            "SUM",
            "REST",
            "MULT",
            // Operadores de comparacion
            "EQUAL",
            "DIFF",
            "LESSTHAN",
            "GREATERTHAN",
            "LESSTHANEQUAL",
            "GREATERTHANEQUAL",
            // Operador de asignacion
            "ASSIGN",
            // Operador de concatenacion
            "COMA",

            "QUOTE",

            "NUM",
            "IDENTIFIER"
    };

    // Lista de Tokens disponibles en la gramatica
    public static final Map<String, String> KEYWORDS = Map.of(
            "int", "PR",
            "String", "PR",
            "BDZtoUser", "PR");
}
