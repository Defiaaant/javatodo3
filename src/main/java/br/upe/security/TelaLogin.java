package br.upe.security;
import br.upe.ui.TelaPrincipal;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class   TelaLogin extends JFrame implements ActionListener {

    private JLabel lblTitulo = new JLabel("", SwingConstants.CENTER);
    private JTextField txtNome = new JTextField();
    private JPasswordField txtSenha = new JPasswordField();
    private JButton btnEntrar = new JButton();
    private JButton btnApagar = new JButton("Apagar conta");
    private CredenciaisRepositorio arquivo = new CredenciaisRepositorio();

    public TelaLogin() {
        setTitle("Login");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1, 5, 5));

        add(lblTitulo);
        add(new JLabel("Nome:"));
        add(txtNome);
        add(new JLabel("Senha:"));
        add(txtSenha);
        add(btnEntrar);
        add(btnApagar);

        btnEntrar.addActionListener(this);
        btnApagar.addActionListener(this);

        atualizarTela();
    }

    // Muda a tela dependendo se já existe conta ou não
    private void atualizarTela() {
        if (arquivo.existeConta()) {
            lblTitulo.setText("Entrar");
            btnEntrar.setText("Entrar");
            btnApagar.setVisible(true);
        } else {
            lblTitulo.setText("Criar conta");
            btnEntrar.setText("Cadastrar");
            btnApagar.setVisible(false);
        }
        txtNome.setText("");
        txtSenha.setText("");
    }

    // Método chamado quando qualquer botão é clicado
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEntrar) {
            clicouEntrar();
        } else if (e.getSource() == btnApagar) {
            clicouApagar();
        }
    }

    private void clicouEntrar() {
        String nome = txtNome.getText();
        String senha = new String(txtSenha.getPassword());

        if (nome.equals("") || senha.equals("")) {
            JOptionPane.showMessageDialog(this, "Preencha nome e senha!");
            return;
        }

        try {
            if (arquivo.existeConta()) {
                // Já tem conta: confere os dados
                if (arquivo.validar(nome, senha)) {
                    abrirAplicacao();
                } else {
                    JOptionPane.showMessageDialog(this, "Nome ou senha incorretos!");
                }
            } else {
                // Não tem conta: cria uma nova
                arquivo.salvar(nome, senha);
                abrirAplicacao();
            }
        } catch (IOException erro) {
            JOptionPane.showMessageDialog(this, "Erro com o arquivo!");
        }
    }

    private void clicouApagar() {
        int resposta = JOptionPane.showConfirmDialog(this,
                "Deseja apagar a conta?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                arquivo.apagar();
                atualizarTela();
            } catch (IOException erro) {
                JOptionPane.showMessageDialog(this, "Não foi possível apagar a conta!");
            }
        }
    }

    private void abrirAplicacao() {
        dispose(); // fecha a tela de login
        // Aqui você abre a tela principal da sua aplicação:
        // new TelaPrincipal().setVisible(true);
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