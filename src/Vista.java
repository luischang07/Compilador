import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Vista extends JFrame implements ComponentListener {

    AreaComponent areaComponent; // textArea - btnArchivos - btnPhaseCompilador[] - txtAreaTokens

    public Vista(AreaComponent areaComponent) {
        super("Version 1.0 Compiler");
        this.areaComponent = areaComponent;
        Interface();
        Listeners();
    }

    private void Listeners() {
        addComponentListener(this);
    }

    private void Interface() {
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(null);

        add(areaComponent);

        setVisible(true);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        short w = (short) this.getWidth();
        short h = (short) this.getHeight();

        areaComponent.setBounds((short) (w * .02), (short) (h * .05),
                (short) (w * .95), (short) (h * .50));
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
}
