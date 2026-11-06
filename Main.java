package app;

import controller.Controlador;
import javax.swing.SwingUtilities;

/**
 * Ponto de entrada da versão com interface gráfica (Swing).
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Controlador().iniciar());
    }
}
