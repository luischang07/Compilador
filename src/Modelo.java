import javax.swing.JTextPane;
import java.util.ArrayList;

public class Modelo {

    public Modelo() {

    }

    public void openFile(JTextPane textArea) {
        FileImport fileImport = new FileImport(textArea);
        fileImport.openFile();
    }

    public ArrayList<String> Scanner(String text) {
        Scanner scanner = new Scanner(text);
        return scanner.Lexer();
    }

    public void Syntax(String text) {
        System.out.println("Syntax: " + text);
    }
}
