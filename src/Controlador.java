
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.components.FlatLabel.LabelType;

public class Controlador implements ActionListener {

    Modelo model;
    Vista vista;
    AreaComponent component;

    public Controlador(Modelo model, Vista vista, AreaComponent component) {
        this.model = model;
        this.vista = vista;
        this.component = component;
        listeners();
        vista.setVisible(true);
    }

    private void listeners() {
        for (byte i = 0; i < component.getBtnCompilador().length; i++) {
            component.getBtnCompilador()[i].addActionListener(this);
        }
        component.getBtnLLimpiar().addActionListener(this);
        component.getBtnArchivos().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == component.getBtnLLimpiar()) {
            vista.symbolTable.setVisible(false);
            vista.txtAreaCodigoIntermedio.setVisible(false);
            component.clear();

            return;
        }
        if (evt.getSource() == component.getBtnArchivos()) {
            model.openFile(component.getTxtAreaProgram());
            return;
        }
        MyButton btnAux = (MyButton) evt.getSource();
        if (btnAux.getId() == 0) {
            component.setTextAreaTokens(model.scanner(component.getTxtAreaProgram().getText()));
            return;
        }
        if (btnAux.getId() == 1) {
            component.getLblSemantic().setVisible(false);
            try {
                boolean syntax = model.syntax(component.getTxtAreaProgram().getText());
                updateParserStatus(syntax);
            } catch (ParserException e) {
                updateParserStatus(false);
                vista.showMessage(e.getMessage(), "Error: Parser", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        if (btnAux.getId() == 2) {
            component.getBtnCompilador()[2].setEnabled(false);
            vista.fillSymbolTable(model.getSymbolTable());
            try {
                component.showLbl(model.semantic(), component.getLblSemantic(), "Semantic: ");
                component.getBtnCompilador()[3].setEnabled(true);
            } catch (SemanticException e) {
                component.showLbl(false, component.getLblSemantic(), "Semantic: ");

                vista.showMessage(e.getMessage(), "Error: Semantic", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (btnAux.getId() == 3) {
            component.getBtnCompilador()[3].setEnabled(false);
            vista.showCodigoIntermedio(model.codigoIntermedio());
        }
    }

    private void updateParserStatus(boolean status) {
        component.showLbl(status, component.getLblParser(), "Parser: ");
        component.getBtnCompilador()[2].setEnabled(status); // Semantic
    }

}
