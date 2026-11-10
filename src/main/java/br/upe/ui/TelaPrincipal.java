package br.upe.ui;

import br.upe.controller.TarefaControlador;
import br.upe.model.Tarefa;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaPrincipal {
    private JPanel pnlMain;
    private JTextField txtDescricaoTarefa;
    private JButton btnAdicionarTarefa;
    private JPanel pnlAdicionar;
    private JTable tblTarefas;
    private JCheckBox chkExibirFinalizadas;

    private List<Tarefa> tarefas;

    private TarefaControlador controlador;

    public TelaPrincipal() {
        super();
        tarefas = new ArrayList<>();
        btnAdicionarTarefa.addActionListener(e -> {
            adicionarTarefa(txtDescricaoTarefa.getText());
            txtDescricaoTarefa.setText("");
        });
        chkExibirFinalizadas.addActionListener(e -> {
            boolean selecionado = ((JCheckBox) e.getSource()).isSelected();
            controlador.exibirFinalizadas(selecionado);
        });
        String[] nomes = {"Light", "Dark", "Azul", "Verde", "Vermelho"};
        Color[] coresFundo = {Color.decode("#FFFFFF"), Color.decode("#000000"), Color.decode("#1565C0"), Color.decode("#2E7D32"), Color.decode("#C62828")};
        Color[] coresTexto = {Color.decode("#000000"), Color.decode("#4fe4ba"), Color.decode("#FFFFFF"), Color.decode("#FFFFFF"), Color.decode("#FFFFFF")};

        JComboBox<String> acoesEscolha = new JComboBox<>(nomes);

        JPanel pnlOpcoes = new JPanel();
        pnlOpcoes.add(acoesEscolha);
        pnlOpcoes.setVisible(false);

        acoesEscolha.addActionListener(e -> {
            int i = acoesEscolha.getSelectedIndex();
            aplicarCores(pnlMain, coresFundo[i], coresTexto[i]);
        });

        java.net.URL url = getClass().getResource("/img.png");
        JButton btnTema;
        if (url != null) {
            Image img = new ImageIcon(url).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            btnTema = new JButton(new ImageIcon(img));
        } else {
            btnTema = new JButton("🎨");
        }
        btnTema.setToolTipText("Opções de tema");
        btnTema.setBorderPainted(false);
        btnTema.setContentAreaFilled(true);
        btnTema.setFocusPainted(false);

        btnTema.addActionListener(e -> {
            pnlOpcoes.setVisible(!pnlOpcoes.isVisible());
            pnlMain.revalidate();
            pnlMain.repaint();
        });

        JPanel pnlTema = new JPanel();
        pnlTema.add(btnTema);
        pnlTema.add(pnlOpcoes);
        pnlMain.add(pnlTema, BorderLayout.SOUTH);
    }

    private void aplicarCores(Container container, Color fundo, Color texto) {
        container.setBackground(fundo);
        container.setForeground(texto);
        for (Component c : container.getComponents()) {
            c.setBackground(fundo);
            c.setForeground(texto);
            if (c instanceof JLabel || c instanceof JCheckBox) {
                ((JComponent) c).setOpaque(true);
            }
            if (c instanceof JTextField) {
                ((JTextField) c).setCaretColor(texto);
            }
            if (c instanceof Container) {
                aplicarCores((Container) c, fundo, texto);
            }
        }
    }

    private void adicionarTarefa(String texto) {
        Tarefa tarefa = new Tarefa(texto, tarefas.size());
        controlador.adicionarTarefaAtiva(tarefa);
        tblTarefas.revalidate();
        tblTarefas.repaint();
    }

    public JPanel getPnlMain() {
        return this.pnlMain;
    }

    private void createUIComponents() {
        controlador = new TarefaControlador();
        tblTarefas = new JTable(controlador.getTarefaTableModel());
        tblTarefas.getColumnModel().getColumn(0).setMaxWidth(20);
    }

}
