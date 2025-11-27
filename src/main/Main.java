package main;

import view.SistemaMercadoGUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Inicia a interface gráfica na Thread correta do Swing
        SwingUtilities.invokeLater(() -> {
            new SistemaMercadoGUI().setVisible(true);
        });
    }
}