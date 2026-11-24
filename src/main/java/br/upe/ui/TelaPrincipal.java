package br.upe.ui;

import br.upe.controller.TarefaControlador;
import br.upe.model.Tarefa;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal {
    private JPanel pnlMain;
    private JTextField txtDescricaoTarefa;
    private JButton btnAdicionarTarefa;
    private JPanel pnlAdicionar;
    private JTable tblTarefas;
    private JCheckBox chkExibirFinalizadas;
    private JButton btnApagarFinalizados;

    private TarefaControlador controlador;

    public TelaPrincipal() {
        super();
        controlador = new TarefaControlador();
        tblTarefas = new JTable(controlador.getTarefaTableModel());
        tblTarefas.setTableHeader(null);
        tblTarefas.setRowHeight(30);
        tblTarefas.getColumnModel().getColumn(0).setMaxWidth(40);
        tblTarefas.setShowGrid(false);
        pnlMain.add(new JScrollPane(tblTarefas), BorderLayout.CENTER);

        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnAdicionarTarefa.setMargin(new Insets(6, 16, 6, 16));
        btnAdicionarTarefa.setFont(btnAdicionarTarefa.getFont().deriveFont(Font.BOLD));
        txtDescricaoTarefa.setMargin(new Insets(4, 8, 4, 8));

        controlador.getTarefaTableModel().addTableModelListener(e -> salvar());

        btnAdicionarTarefa.addActionListener(e -> {
            adicionarTarefa(txtDescricaoTarefa.getText());
            txtDescricaoTarefa.setText("");
        });
        tblTarefas.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE && chkExibirFinalizadas.isSelected()) {
                    removerFinalizados();
                }
            }
        });
        txtDescricaoTarefa.addActionListener(e -> btnAdicionarTarefa.doClick());
        btnApagarFinalizados = new JButton("Apagar Finalizadas");
        btnApagarFinalizados.setVisible(false);
        btnApagarFinalizados.addActionListener(e -> removerFinalizados());
        chkExibirFinalizadas.addActionListener(e -> {
            boolean selecionado = ((JCheckBox) e.getSource()).isSelected();
            controlador.exibirFinalizadas(selecionado);
            btnApagarFinalizados.setVisible(selecionado);
            pnlMain.revalidate();
            pnlMain.repaint();
        });
        String[] nomes = {"Light", "Dark", "Azul", "Verde", "Vermelho"};
        Color[] coresFundo = {Color.decode("#FFFFFF"), Color.decode("#000000"), Color.decode("#93b3cd"), Color.decode("#5cd697"), Color.decode("#ac4343")};
        Color[] coresTexto = {Color.decode("#000000"), Color.decode("#4fe4ba"), Color.decode("#1e1919"), Color.decode("#000000"), Color.decode("#FFFFFF")};

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
            Image img = new ImageIcon(url).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            btnTema = new JButton(new ImageIcon(img));
        } else {
            btnTema = new JButton("🎨");
        }

        btnTema.setToolTipText("Opções de tema");
        btnTema.setBorderPainted(false);
        btnTema.setContentAreaFilled(false);
        btnTema.setFocusPainted(false);

        btnTema.addActionListener(e -> {
            pnlOpcoes.setVisible(!pnlOpcoes.isVisible());
            pnlMain.revalidate();
            pnlMain.repaint();
        });

        JPanel pnlTema = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        pnlTema.add(btnTema);
        pnlTema.add(pnlOpcoes);
        pnlTema.add(btnApagarFinalizados);
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
        if (texto == null || texto.isBlank()) {
            JOptionPane.showMessageDialog(pnlMain, "Digite a descrição da tarefa.");
            return;
        }
        int ordem = controlador.getTarefaTableModel().getTarefasAtivas().size();
        controlador.adicionarTarefaAtiva(new Tarefa(texto.trim(), ordem));
    }

    public void salvar() {
        try {
            controlador.getTarefaTableModel().salvarEstado();
        } catch (Exception ex) {
            System.err.println("Falha ao salvar: " + ex);
        }
    }

    public JPanel getPnlMain() {
        return this.pnlMain;
    }

    private void removerFinalizados() {
        int[] linhasView = tblTarefas.getSelectedRows();
        if (linhasView.length == 0) {
            JOptionPane.showMessageDialog(pnlMain, "Selecione tarefas finalizadas na tabela.");
            return;
        }

        int[] linhasModel = new int[linhasView.length];
        for (int i = 0; i < linhasView.length; i++) {
            linhasModel[i] = tblTarefas.convertRowIndexToModel(linhasView[i]);
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                pnlMain,
                "Deseja apagar a tarefa finalizada selecionada?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            int removidas = controlador.removerFinalizados(linhasModel);
            if (removidas == 0) {
                JOptionPane.showMessageDialog(pnlMain, "Linha selecionada não estao marcada como finalizada");
            }
        }
    }
}
