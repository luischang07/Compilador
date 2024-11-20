
public class Operacion {
    private String operador;
    private String operando1;
    private String operando2;
    private String resultado;

    public Operacion(String operador, String operando1, String operando2, String resultado) {
        this.operador = operador;
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.resultado = resultado;
    }

    public String getOperador() {
        return operador;
    }

    public String getOperando1() {
        return operando1;
    }

    public String getOperando2() {
        return operando2;
    }

    public String getResultado() {
        return resultado;
    }
}
