
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import exeptions.Exceptions;
import exeptions.ParserException;

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
            component.clear();
            vista.clear();

            return;
        }
        if (evt.getSource() == component.getBtnArchivos()) {
            model.openFile(component.getTxtAreaProgram());
            return;
        }
        MyButton btnAux = (MyButton) evt.getSource();
        if (btnAux.getId() == 0) { // Scanner
            component.setTextAreaTokens(model.scanner(component.getTxtAreaProgram().getText()));
            return;
        }
        if (btnAux.getId() == 1) { // Parser
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
        if (btnAux.getId() == 2) { // Semantic
            component.getBtnCompilador()[2].setEnabled(false);

            try {
                component.showLbl(model.semantic(), component.getLblSemantic(), "Semantic: ");
                component.getBtnCompilador()[3].setEnabled(true);
            } catch (Exceptions e) {
                component.showLbl(false, component.getLblSemantic(), "Semantic: ");
                vista.showMessage(e.getMessage(), "Error: Semantic", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (btnAux.getId() == 3) { // Intermediate Code
            component.getBtnCompilador()[3].setEnabled(false);
            component.getBtnCompilador()[4].setEnabled(true);
            vista.showCodigoIntermedio(model.codigoIntermedio());
            return;
        }
        if (btnAux.getId() == 4) { // Object Code
            component.getBtnCompilador()[4].setEnabled(false);
            vista.showCodigoObjeto(model.codigoObjeto());
            return;
        }

    }

    private void updateParserStatus(boolean status) {
        component.showLbl(status, component.getLblParser(), "Parser: ");
        component.getBtnCompilador()[2].setEnabled(status); // Semantic
    }
}
