import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JScrollPane;
import compilerTools.Functions;

import java.util.ArrayList;

public class AreaComponent extends JPanel implements ComponentListener {

    private JTextPane txtAreaProgram, txtAreaTokens;
    private JScrollPane scrollPane, scrollPane2;

    private JButton btnArchivos;
    private MyButton[] btnCompilador;

    public AreaComponent() {
        Interface();
        Listeners();
        Functions.setLineNumberOnJTextComponent(txtAreaProgram);
        Functions.setLineNumberOnJTextComponent(txtAreaTokens);
    }

    private void Listeners() {
        addComponentListener(this);
    }

    private void Interface() {
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.black));

        txtAreaProgram = new JTextPane();
        txtAreaTokens = new JTextPane();

        scrollPane = new JScrollPane(txtAreaProgram);
        scrollPane2 = new JScrollPane(txtAreaTokens);

        add(scrollPane);
        add(scrollPane2);

        btnArchivos = new JButton();
        btnArchivos.setText("Open File");
        add(btnArchivos);

        btnCompilador = new MyButton[2];
        for (int i = 0; i < btnCompilador.length; i++) {
            btnCompilador[i] = new MyButton(i);
        }

        btnCompilador[0].setName("Scanner");
        btnCompilador[1].setName("Syntax");
        btnCompilador[0].setText(btnCompilador[0].getName());
        btnCompilador[1].setText(btnCompilador[1].getName());

        for (int i = 0; i < btnCompilador.length; i++) {
            add(btnCompilador[i]);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int w = this.getWidth();
        int h = this.getHeight();

        btnArchivos.setBounds((int) (w * .02), (int) (h * .02),
                (int) (w * .2), (int) (h * .09));

        scrollPane.setBounds(btnArchivos.getX(), btnArchivos.getY() + (int) (btnArchivos.getHeight() * 1.4),
                (int) (w * .4),
                (int) (h * .9) - btnArchivos.getHeight());

        for (int i = 0; i < btnCompilador.length; i++) {
            btnCompilador[i].setBounds((int) (scrollPane.getWidth() * 1.1), scrollPane.getY() * (i + 1),
                    btnArchivos.getWidth(), btnArchivos.getHeight());
        }

        scrollPane2.setBounds(btnCompilador[0].getX() + (int) (btnCompilador[0].getWidth() * 1.1),
                btnCompilador[0].getY(), (int) (w * .92) - scrollPane.getWidth() - btnArchivos.getWidth(),
                scrollPane.getHeight());

    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    public JTextPane getTxtAreaProgram() {
        return txtAreaProgram;
    }

    public JTextPane getTxtAreaTokens() {
        return txtAreaTokens;
    }

    public JButton getBtnArchivos() {
        return btnArchivos;
    }

    public JButton[] getBtnCompilador() {
        return btnCompilador;
    }

    public void setTextAreaTokens(ArrayList<String> textTokens) {
        txtAreaTokens.setText("");
        for (String token : textTokens) {
            txtAreaTokens.setText(txtAreaTokens.getText() + token + "\n");
        }
    }
}
