package exeptions;

import variable_info.TokenInfo;

public class ParserException extends RuntimeException {

    public ParserException(String message, TokenInfo token) {
        super(message + " at token: " + token.getValue() + " (" + token.getNumber() + ")");
    }
}
