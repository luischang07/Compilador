
import javax.swing.JOptionPane;

public class ParseException extends RuntimeException {
    private final TokenInfo token;

    public ParseException(String message, TokenInfo token) {
        super(message + " at token: " + token.getValue() + " (" + token.getNumber() + ")");
        this.token = token;
    }

    public TokenInfo getToken() {
        return token;
    }

    public void showMessage() {
        JOptionPane.showMessageDialog(null, getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
