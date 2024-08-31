public class App {
    public static void main(String[] args) throws Exception {
        Modelo model = new Modelo();
        AreaComponent areaComponent = new AreaComponent();
        Vista vista = new Vista(areaComponent);

        Controlador c = new Controlador(model, vista, areaComponent);
    }
}
