
public class VariableInfo {
    private String type;
    private boolean initialized;
    private String value;
    private int Bytes;

    public VariableInfo(String type, int Bytes) {
        this.type = type;
        this.Bytes = Bytes;
        initialized = false;
    }

    public VariableInfo(String type, String value, int Bytes) {
        this.type = type;
        initialized = true;
        this.value = value;
        this.Bytes = Bytes;
    }

    public String getType() {
        return type;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public String getValue() {
        return value;
    }

    public int getBytes() {
        return Bytes;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void setValue(String value) {
        initialized = true;
        this.value = value;
    }

}
