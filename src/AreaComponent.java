
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JScrollPane;
import compilerTools.Functions;

import java.util.List;

public class AreaComponent extends JPanel implements ComponentListener {

    private JTextPane txtAreaProgram;
    private JTextPane txtAreaTokens;

    private JScrollPane scrollPane;
    private JScrollPane scrollPane2;

    private JButton btnArchivos;
    private JButton btnLLimpiar;
    private MyButton[] btnCompilador;

    private JLabel lblParser;

    public AreaComponent() {
        initInterface();
        listeners();
        Functions.setLineNumberOnJTextComponent(txtAreaProgram);
        Functions.setLineNumberOnJTextComponent(txtAreaTokens);
    }

    private void listeners() {
        addComponentListener(this);
    }

    private void initInterface() {
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.black));

        txtAreaProgram = new JTextPane();
        txtAreaTokens = new JTextPane();
        txtAreaTokens.setEditable(false);
        txtAreaTokens.setBackground(Color.white);

        scrollPane = new JScrollPane(txtAreaProgram);
        scrollPane2 = new JScrollPane(txtAreaTokens);

        add(scrollPane);
        add(scrollPane2);

        btnArchivos = new JButton();
        btnArchivos.setText("Open File");
        add(btnArchivos);

        btnLLimpiar = new JButton();
        btnLLimpiar.setText("Clear");
        add(btnLLimpiar);

        btnCompilador = new MyButton[2];
        for (byte i = 0; i < btnCompilador.length; i++) {
            btnCompilador[i] = new MyButton(i);
        }

        btnCompilador[0].setName("Scanner");
        btnCompilador[1].setName("Syntax");
        btnCompilador[0].setText(btnCompilador[0].getName());
        btnCompilador[1].setText(btnCompilador[1].getName());

        for (short i = 0; i < btnCompilador.length; i++) {
            add(btnCompilador[i]);
        }

        lblParser = new JLabel();
        add(lblParser);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        short w = (short) this.getWidth();
        short h = (short) this.getHeight();

        btnArchivos.setBounds((short) (w * .01), (short) (h * .02),
                (short) (w * .2), (short) (h * .09));

        btnLLimpiar.setBounds(btnArchivos.getX() + (short) (btnArchivos.getWidth() * 1.1), btnArchivos.getY(),
                (short) (btnArchivos.getWidth() * .7), btnArchivos.getHeight());

        scrollPane.setBounds(btnArchivos.getX(), btnArchivos.getY() + (short) (btnArchivos.getHeight() * 1.4),
                (short) (w * .35),
                (short) (h * .9) - btnArchivos.getHeight());

        // for (short i = 0; i < btnCompilador.length; i++) {
        btnCompilador[0].setBounds((short) (scrollPane.getWidth() * 1.1), scrollPane.getY(),
                (short) (btnArchivos.getWidth() * .8), btnArchivos.getHeight());
        // }

        scrollPane2.setBounds(btnCompilador[0].getX() + (short) (btnCompilador[0].getWidth() * 1.1),
                btnCompilador[0].getY(), (short) (w * .8) - scrollPane.getWidth() - btnArchivos.getWidth(),
                scrollPane.getHeight());

        btnCompilador[1].setBounds(scrollPane2.getX() + (short) (scrollPane2.getWidth() * 1.1), scrollPane2.getY(),
                (short) (w * .9) - scrollPane2.getWidth() - btnCompilador[0].getWidth() - scrollPane.getWidth(),
                (short) (h * .09));

        lblParser.setBounds(btnCompilador[1].getX(),
                btnCompilador[1].getY() + (short) (btnCompilador[1].getHeight() * 1.4),
                btnCompilador[1].getWidth(), btnCompilador[1].getHeight());
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

    public JLabel getLblParser() {
        return lblParser;
    }

    public JButton getBtnLLimpiar() {
        return btnLLimpiar;
    }

    public void setTextAreaTokens(List<TokenInfo> textTokens) {
        txtAreaTokens.setText("");
        for (TokenInfo token : textTokens) {
            txtAreaTokens.setText(
                    txtAreaTokens.getText() + token.getName() + "(" + token.getNumber() + "), " + token.getValue()
                            + "\n");
        }
    }
}
