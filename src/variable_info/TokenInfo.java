package variable_info;

import variable_info.Tokens.TokenType;

public class TokenInfo {
    private final String name;
    private final byte number;
    private final String value;

    public TokenInfo(Tokens.TokenType type, byte number, String value) {
        this.name = type.name();
        this.number = number;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public byte getNumber() {
        return number;
    }

    public String getValue() {
        return value;
    }

    public boolean isTokenType(byte expectedType) {
        return number == expectedType;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", value='" + value + '\'' +
                '}';
    }
}