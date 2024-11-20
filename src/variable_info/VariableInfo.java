package variable_info;

public class VariableInfo {
    private String type;
    private boolean initialized;
    private String value;
    private int bytes;

    public VariableInfo(String type, int bytes) {
        this.type = type;
        this.bytes = bytes;
        initialized = false;
    }

    public VariableInfo(String type, String value, int bytes) {
        this.type = type;
        initialized = true;
        this.value = value;
        this.bytes = bytes;
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
        return bytes;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public void setValue(String value) {
        initialized = true;
        this.value = value;
    }

}
