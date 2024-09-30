
public class VariableInfo {
    private String type;
    private boolean initialized;
    private String value;

    public VariableInfo(String type) {
        this.type = type;
        initialized = false;
    }

    public VariableInfo(String type, String value) {
        this.type = type;
        initialized = true;
        this.value = value;
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

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void setValue(String value) {
        initialized = true;
        this.value = value;
    }

}
