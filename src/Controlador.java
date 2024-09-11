import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

public class Controlador implements ActionListener {

    Modelo model;
    Vista vista;
    AreaComponent component;

    public Controlador(Modelo model, Vista vista, AreaComponent component) {
        this.model = model;
        this.vista = vista;
        this.component = component;
        listeners();
    }

    private void listeners() {
        for (byte i = 0; i < component.getBtnCompilador().length; i++) {
            component.getBtnCompilador()[i].addActionListener(this);
        }
        component.getBtnLLimpiar().addActionListener(this);
        component.getBtnArchivos().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == component.getBtnLLimpiar()) {
            component.getTxtAreaProgram().setText("");
            component.getTxtAreaTokens().setText("");
            component.getLblParser().setVisible(false);
            return;
        }
        if (e.getSource() == component.getBtnArchivos()) {
            model.openFile(component.getTxtAreaProgram());
            return;
        }

        MyButton btnAux = (MyButton) e.getSource();
        if (btnAux.getId() == 0) {
            component.setTextAreaTokens(model.scanner(component.getTxtAreaProgram().getText()));
        } else {
            boolean syntax = model.syntax(component.getTxtAreaProgram().getText());
            System.out.println("Syntax: " + syntax);
            JLabel lbl = component.getLblParser();
            lbl.setVisible(true);
            lbl.setText("Parser: ");
            if (syntax) {
                lbl.setText(lbl.getText() + "Correct");
            } else {
                lbl.setText(lbl.getText() + "Incorrect");
            }
        }

    }

}
