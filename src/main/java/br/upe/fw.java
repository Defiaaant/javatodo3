import br.upe.TelaLogin;

import javax.swing.*;

public class fw {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}