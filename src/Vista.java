
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import javax.swing.JOptionPane;

import com.formdev.flatlaf.FlatLaf;

import javax.swing.ScrollPaneConstants;

import java.util.Map;
import java.util.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Vista extends JFrame implements ComponentListener {

    AreaComponent areaComponent; // textArea - btnArchivos - btnPhaseCompilador[] - txtAreaTokens
    JTextArea txtAreaCodigoIntermedio;
    JTable symbolTable;
    JScrollPane scrollPaneTabla;
    JScrollPane scrollPaneCodigoIntermedio;
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(null);

        add(areaComponent);

        model = new DefaultTableModel();
        symbolTable = new JTable(model);
        model.addColumn("ID");
        model.addColumn("Type");
        model.addColumn("Value");
        model.addColumn("Initialized");
        model.addColumn("Bytes");

        symbolTable.setDefaultEditor(Object.class, null);

        scrollPaneTabla = new JScrollPane(symbolTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneTabla.setVisible(false);

        txtAreaCodigoIntermedio = new JTextArea();
        txtAreaCodigoIntermedio.setEditable(false);
        txtAreaCodigoIntermedio.setBackground(Color.white);
        txtAreaCodigoIntermedio.setLineWrap(true);

        scrollPaneCodigoIntermedio = new JScrollPane(txtAreaCodigoIntermedio,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneCodigoIntermedio.setVisible(false);

        add(scrollPaneCodigoIntermedio);

        add(scrollPaneTabla);
    }

    public void fillSymbolTable(Map<String, VariableInfo> symbolTable) {
        scrollPaneTabla.setVisible(true);
        model.setRowCount(0);
        symbolTable.forEach((key, value) -> model
                .addRow(new Object[] { key, value.getType(), value.getValue(), value.isInitialized(),
                        value.getBytes() }));
    }

    public void showCodigoIntermedio(List<String> codigoIntermedio) {
        scrollPaneCodigoIntermedio.setVisible(true);
        txtAreaCodigoIntermedio.setText("");
        codigoIntermedio.forEach(line -> txtAreaCodigoIntermedio.append(line + "\n"));
    }

    public void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        short w = (short) this.getWidth();
        short h = (short) this.getHeight();

        areaComponent.setBounds((short) (w * .02), (short) (h * .05),
                (short) (w * .95), (short) (h * .50));

        scrollPaneTabla.setBounds(areaComponent.getWidth() - areaComponent.getWidth() / 3,
                areaComponent.getY() + (short) (areaComponent.getHeight() * 1.05),
                areaComponent.getWidth() / 3,
                (short) (h * .9) - (areaComponent.getY() + (short) (areaComponent.getHeight() * 1.05)));

        scrollPaneCodigoIntermedio.setBounds(areaComponent.getX(),
                areaComponent.getY() + (short) (areaComponent.getHeight() * 1.05),
                (short) (areaComponent.getWidth() * .9 - scrollPaneTabla.getWidth()),
                (short) (h * .9) - (areaComponent.getY() + (short) (areaComponent.getHeight() * 1.05)));

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
