
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLaf;

public class Vista extends JFrame implements ComponentListener {

    AreaComponent areaComponent; // textArea - btnArchivos - btnPhaseCompilador[] - txtAreaTokens

    JTextArea txtAreaCodigoIntermedio;
    JScrollPane scrollPaneCodigoIntermedio;
    JLabel lblCodigoIntermedio;

    JTextArea txtAreaCodigoObjeto;
    JScrollPane scrollPaneCodigoObjeto;
    JLabel lblCodigoObjeto;

    DefaultTableModel model;

    public Vista(AreaComponent areaComponent) {
        super("Version 1.0 Compiler");
        this.areaComponent = areaComponent;
        initializeUI();
        listeners();
    }

    private void listeners() {
        addComponentListener(this);
    }

    private void initializeUI() {
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(null);

        add(areaComponent);

        lblCodigoIntermedio = new JLabel("Codigo Intermedio");
        add(lblCodigoIntermedio);
        lblCodigoObjeto = new JLabel("Codigo Objeto");
        add(lblCodigoObjeto);

        txtAreaCodigoIntermedio = new JTextArea();
        txtAreaCodigoObjeto = new JTextArea();
        scrollPaneCodigoIntermedio = new JScrollPane(txtAreaCodigoIntermedio);
        scrollPaneCodigoObjeto = new JScrollPane(txtAreaCodigoObjeto);

        initializeScrollPane(scrollPaneCodigoIntermedio, txtAreaCodigoIntermedio);
        initializeScrollPane(scrollPaneCodigoObjeto, txtAreaCodigoObjeto);

    }

    private void initializeScrollPane(JScrollPane scrollPane, JTextArea textArea) {
        textArea.setEditable(false);
        textArea.setBackground(Color.white);
        textArea.setLineWrap(true);

        scrollPane.setViewportView(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVisible(false);

        add(scrollPane);
    }

    public void clear() {
        txtAreaCodigoIntermedio.setText("");
        model.setRowCount(0);
        scrollPaneCodigoIntermedio.setVisible(false);
        scrollPaneCodigoObjeto.setVisible(false);
    }

    public void showCodigoIntermedio(String codigoIntermedio) {
        scrollPaneCodigoIntermedio.setVisible(true);
        txtAreaCodigoIntermedio.setText(codigoIntermedio);
    }

    public void showCodigoObjeto(String codigoObjeto) {
        scrollPaneCodigoObjeto.setVisible(true);
        txtAreaCodigoObjeto.setText(codigoObjeto);
    }

    public void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        short w = (short) this.getWidth();
        short h = (short) this.getHeight();

        areaComponent.setBounds((short) (w * .02), (short) (h * .02),
                (short) (w * .95), (short) (h * .45));

        lblCodigoIntermedio.setBounds(areaComponent.getX(),
                areaComponent.getY() + (short) (areaComponent.getHeight() * 1.02),
                (short) (areaComponent.getWidth() * .4), (short) (h * .05));

        lblCodigoObjeto.setBounds((short) (lblCodigoIntermedio.getX() + lblCodigoIntermedio.getWidth() * 1.05),
                lblCodigoIntermedio.getY(), lblCodigoIntermedio.getWidth(), lblCodigoIntermedio.getHeight());

        scrollPaneCodigoIntermedio.setBounds(areaComponent.getX(),
                lblCodigoIntermedio.getY() + lblCodigoIntermedio.getHeight(),
                lblCodigoIntermedio.getWidth(),
                (short) (h * .92) - (lblCodigoIntermedio.getHeight() + areaComponent.getY()
                        + (short) (areaComponent.getHeight() * 1.05)));

        scrollPaneCodigoObjeto.setBounds(
                lblCodigoObjeto.getX(),
                scrollPaneCodigoIntermedio.getY(),
                scrollPaneCodigoIntermedio.getWidth(),
                scrollPaneCodigoIntermedio.getHeight());

        FlatLaf.updateUI();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // This method is not used
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // This method is not used
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // This method is not used
    }
}
