import javax.swing.JButton;

public class MyButton extends JButton {
    private int id;
    private String name;

    public MyButton(int id) {
        this.id = id;
    }

    public MyButton(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}