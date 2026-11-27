package br.upe.ui;

import br.upe.savetool.GerenciarSaves;
import br.upe.savetool.GerenciarSaves.DadosLista;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaAbas {
    private final JPanel painel = new JPanel(new BorderLayout());
    private final JTabbedPane abas = new JTabbedPane();
    private final List<TelaPrincipal> telas = new ArrayList<>(); // mesma ordem das abas

    public TelaAbas() {
        for (DadosLista dados : GerenciarSaves.carregarListas()) {
            adicionarAba(dados);
        }

        JButton btnNova = new JButton("+ Nova lista");
        JButton btnRenomear = new JButton("Renomear");
        JButton btnApagar = new JButton("Apagar lista");

        btnNova.addActionListener(e -> novaLista());
        btnRenomear.addActionListener(e -> renomearLista());
        btnApagar.addActionListener(e -> apagarLista());

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        barra.add(btnNova);
        barra.add(btnRenomear);
        barra.add(btnApagar);

        painel.add(barra, BorderLayout.NORTH);
        painel.add(abas, BorderLayout.CENTER);
    }

    private void adicionarAba(DadosLista dados) {
        TelaPrincipal tela = new TelaPrincipal(dados, this::salvar);
        telas.add(tela);
        abas.addTab(dados.nome, tela.getPnlMain());
    }

    private String pedirNome(String mensagem, String inicial) {
        String nome = JOptionPane.showInputDialog(painel, mensagem, inicial);
        if (nome == null) return null;               // clicou em cancelar
        nome = nome.trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(painel, "O nome não pode ficar vazio.");
            return null;
        }
        return nome;
    }

    private void novaLista() {
        String nome = pedirNome("Nome da nova lista:", "");
        if (nome == null) return;
        adicionarAba(new DadosLista(nome, new ArrayList<>(), new ArrayList<>()));
        abas.setSelectedIndex(abas.getTabCount() - 1);
        salvar();
    }

    private void renomearLista() {
        int i = abas.getSelectedIndex();
        if (i < 0) return;
        String nome = pedirNome("Novo nome:", abas.getTitleAt(i));
        if (nome == null) return;
        abas.setTitleAt(i, nome);
        salvar();
    }

    private void apagarLista() {
        int i = abas.getSelectedIndex();
        if (i < 0) return;
        if (abas.getTabCount() == 1) {
            JOptionPane.showMessageDialog(painel, "Precisa existir pelo menos uma lista.");
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(painel,
                "Apagar a lista \"" + abas.getTitleAt(i) + "\" e todas as suas tarefas?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            abas.removeTabAt(i);
            telas.remove(i);
            salvar();
        }
    }

    public void salvar() {
        List<DadosLista> listas = new ArrayList<>();
        for (int i = 0; i < telas.size(); i++) {
            TelaPrincipal t = telas.get(i);
            listas.add(new DadosLista(abas.getTitleAt(i),
                    t.getAtivasParaSalvar(), t.getTarefasFinalizadas()));
        }
        GerenciarSaves.salvarListas(listas);
    }

    public JPanel getPainel() {
        return painel;
    }
}