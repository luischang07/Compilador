
import javax.swing.JTextPane;
import java.util.List;

public class Modelo {

    public Modelo() {

    }

    public void openFile(JTextPane textArea) {
        FileImport fileImport = new FileImport(textArea);
        fileImport.openFile();
    }

    public List<TokenInfo> scanner(String text) {
        Scanner scanner = new Scanner(text);
        return scanner.lexer();
    }

    public boolean syntax(String text) {
        List<TokenInfo> tokens = scanner(text);
        Parser parser = new Parser(tokens);
        return parser.parseProgram();
    }
}
