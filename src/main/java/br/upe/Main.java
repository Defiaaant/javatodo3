package br.upe;

import br.upe.ui.TelaPrincipal;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
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
    public static void main(String[] args) {
        fontes(16);
        TelaPrincipal pnlMain = new TelaPrincipal();

        JFrame frame = new JFrame("Afazeres");
        frame.setContentPane(pnlMain.getPnlMain());
        frame.pack();
        frame.setMinimumSize(new Dimension(520, 420));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                pnlMain.salvar();
            }
        });

    }
}