package br.upe;

import br.upe.ui.TelaPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        TelaPrincipal pnlMain = new TelaPrincipal();

        JFrame frame = new JFrame("Afazeres");
        frame.setContentPane(pnlMain.getPnlMain());
        frame.pack();
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