import view.SistemaMercadoGUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SistemaMercadoGUI().setVisible(true);
        });
    }
}