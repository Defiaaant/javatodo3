package br.upe;

import br.upe.ui.TelaPrincipal;
import br.upe.security.TelaLogin;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;

public class Main {
    private static void fontes(int tamanho){
        for (Object chave : Collections.list(UIManager.getDefaults().keys())) {
            Object valor = UIManager.get(chave);
            if (valor instanceof FontUIResource) {
                FontUIResource f = (FontUIResource) valor;
                UIManager.put(chave, new FontUIResource(f.deriveFont((float) tamanho)));
            }
        }
    }
    public static void abrirAplicacao(){
        TelaPrincipal pnlMain = new TelaPrincipal();

        JFrame frame = new JFrame("Afazeres");
        frame.setContentPane(pnlMain.getPnlMain());
        frame.pack();
        frame.setMinimumSize(new Dimension(520, 420));
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pnlMain.salvar();
            }
        });

        frame.setVisible(true);
    }
    public static void main(String[] args) {
        fontes(16);
        SwingUtilities.invokeLater(() -> new TelaLogin(Main::abrirAplicacao).setVisible(true));    }
}