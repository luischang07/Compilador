import javax.swing.JButton;

public class MyButton extends JButton {
    private byte id;
    private String name;

    public MyButton(byte id) {
        this.id = id;
    }

    public MyButton(byte id, String name) {
        this.id = id;
        this.name = name;
    }

    public byte getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}