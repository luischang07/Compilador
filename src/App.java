import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import javax.swing.UIManager;
import java.awt.Font;

public class App {

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
        }
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        FlatLightLaf.setup();
        // ----------------------------------------------------------------------------------
        Modelo model = new Modelo();
        AreaComponent areaComponent = new AreaComponent();
        Vista vista = new Vista(areaComponent);

        new Controlador(model, vista, areaComponent);
    }
}
