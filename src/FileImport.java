import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JTextPane;

import javax.swing.JFileChooser;

public class FileImport {

    private JTextPane textArea;

    public FileImport(JTextPane textArea) {
        this.textArea = textArea;
    }

    private void loadFile(String filePath) {
        System.out.println(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            textArea.read(reader, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            loadFile(filePath);
        }
    }

}
