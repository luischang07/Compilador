import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controlador implements ActionListener {

    Modelo model;
    Vista vista;
    AreaComponent component;

    public Controlador(Modelo model, Vista vista, AreaComponent component) {
        this.model = model;
        this.vista = vista;
        this.component = component;
        Listeners();
    }

    private void Listeners() {
        for (int i = 0; i < component.getBtnCompilador().length; i++) {
            component.getBtnCompilador()[i].addActionListener(this);

        }
        component.getBtnArchivos().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == component.getBtnArchivos()) {
            model.openFile(component.getTxtAreaProgram());
            return;
        }

        MyButton btnAux = (MyButton) e.getSource();
        if (btnAux.getId() == 0) {
            component.setTextAreaTokens(model.Scanner(component.getTxtAreaProgram().getText()));
        } else {
            model.Syntax(component.getTxtAreaProgram().getText());
        }

    }

}
