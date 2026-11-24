package br.upe;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TelaLogin extends JFrame {

    private final CredenciaisRepositorio repo = new CredenciaisRepositorio();

    private final JLabel lblTitulo = new JLabel("", SwingConstants.CENTER);
    private final JTextField txtUsuario = new JTextField(15);
    private final JPasswordField txtSenha = new JPasswordField(15);
    private final JButton btnAcao = new JButton();
    private final JButton btnApagar = new JButton("Apagar conta");

    public TelaLogin() {
        setTitle("Acesso");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        montarTela();
        atualizarModo();
        pack();
        setLocationRelativeTo(null);
    }

    private void montarTela() {
        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 16f));
        form.add(lblTitulo);
        form.add(new JLabel("Usuário:"));
        form.add(txtUsuario);
        form.add(new JLabel("Senha:"));
        form.add(txtSenha);
        form.add(btnAcao);
        form.add(btnApagar);

        add(form);

        btnAcao.addActionListener(e -> aoClicarAcao());
        btnApagar.addActionListener(e -> aoClicarApagar());
        getRootPane().setDefaultButton(btnAcao); // Enter aciona o botão
    }

    /** Alterna entre modo cadastro e modo entrada. */
    private void atualizarModo() {
        boolean jaTemConta = repo.existeConta();
        lblTitulo.setText(jaTemConta ? "Entrar" : "Criar conta");
        btnAcao.setText(jaTemConta ? "Entrar" : "Cadastrar");
        btnApagar.setVisible(jaTemConta);
        txtUsuario.setText("");
        txtSenha.setText("");
        pack();
    }

    private void aoClicarAcao() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha usuário e senha.");
            return;
        }

        if (repo.existeConta()) {
            if (repo.validar(usuario, senha)) {
                abrirAplicacao();
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try {
                repo.salvar(usuario, senha);
                abrirAplicacao();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void aoClicarApagar() {
        int opcao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja apagar esta conta?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                repo.apagar();
                atualizarModo(); // volta para a tela de cadastro
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao apagar: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirAplicacao() {
        dispose();
        // TODO: troque pela tela principal da sua aplicação
        // new TelaPrincipal().setVisible(true);
        JOptionPane.showMessageDialog(null, "Login realizado!");
    }
}