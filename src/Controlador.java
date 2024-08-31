import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

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
            return;
        }

    }

}
