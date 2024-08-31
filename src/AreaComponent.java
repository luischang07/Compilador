import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class AreaComponent extends JPanel implements ComponentListener {

    private JTextArea txtAreaProgram, txtAreaTokens;

    private JButton btnArchivos;
    private JButton[] btnCompilador;

    public AreaComponent() {
        Interface();
        Listeners();
    }

    private void Listeners() {
        addComponentListener(this);
    }

    private void Interface() {
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.black));
        txtAreaProgram = new JTextArea();
        add(txtAreaProgram);

        txtAreaTokens = new JTextArea();
        add(txtAreaTokens);

        btnArchivos = new JButton();
        btnArchivos.setText("Open File");
        add(btnArchivos);

        btnCompilador = new JButton[2];
        for (int i = 0; i < btnCompilador.length; i++) {
            btnCompilador[i] = new JButton();
        }
        btnCompilador[0].setText("Scanner");
        btnCompilador[1].setText("Syntax");
        add(btnCompilador[0]);
        add(btnCompilador[1]);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int w = this.getWidth();
        int h = this.getHeight();

        btnArchivos.setBounds((int) (w * .02), (int) (h * .02),
                (int) (w * .2), (int) (h * .09));

        txtAreaProgram.setBounds(btnArchivos.getX(), btnArchivos.getY() + (int) (btnArchivos.getHeight() * 1.4),
                (int) (w * .4),
                (int) (h * .9) - btnArchivos.getHeight());

        for (int i = 0; i < btnCompilador.length; i++) {
            btnCompilador[i].setBounds((int) (txtAreaProgram.getWidth() * 1.1), txtAreaProgram.getY() * (i + 1),
                    btnArchivos.getWidth(), btnArchivos.getHeight());
        }

        txtAreaTokens.setBounds(btnCompilador[0].getX() + (int) (btnCompilador[0].getWidth() * 1.1),
                btnCompilador[0].getY(), (int) (w * .92) - txtAreaProgram.getWidth() - btnArchivos.getWidth(),
                txtAreaProgram.getHeight());

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

    public JTextArea getTxtAreaProgram() {
        return txtAreaProgram;
    }

    public JTextArea getTxtAreaTokens() {
        return txtAreaTokens;
    }

    public JButton getBtnArchivos() {
        return btnArchivos;
    }

    public JButton[] getBtnCompilador() {
        return btnCompilador;
    }
}
