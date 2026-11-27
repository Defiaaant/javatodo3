package br.upe.security;

import br.upe.savetool.GerenciarSaves;
import br.upe.ui.TelaAbas;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class   TelaLogin extends JFrame implements ActionListener {

    private ArquivoUsuario arquivo = new ArquivoUsuario();

    private JLabel lblTitulo = new JLabel("", SwingConstants.CENTER);
    private JTextField txtNome = new JTextField();
    private JPasswordField txtSenha = new JPasswordField();
    private JButton btnEntrar = new JButton();
    private JButton btnApagar = new JButton("Apagar conta");

    public TelaLogin() {
        fontes(16);
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel painel = new JPanel(new GridLayout(7,1,5,10));
        painel.setBorder(BorderFactory.createEmptyBorder(10,15,2,15));
        setContentPane(painel);

        painel.add(lblTitulo);
        painel.add(new JLabel("Nome:"));
        painel.add(txtNome);
        painel.add(new JLabel("Senha:"));
        painel.add(txtSenha);
        painel.add(btnEntrar);
        painel.add(btnApagar);

        Border padding = BorderFactory.createEmptyBorder(4,8,4,8);
        txtNome.setBorder(BorderFactory.createCompoundBorder(txtNome.getBorder(),padding));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(txtSenha.getBorder(),padding));

        btnEntrar.addActionListener(this);
        btnApagar.addActionListener(this);

        atualizarTela();
        setMinimumSize(new Dimension(340, 340));
        setLocationRelativeTo(null);
    }

    private void atualizarTela() {
        lblTitulo.setFont(new Font("JetBrains Mono", Font.BOLD, 18));
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

    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
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
                if (arquivo.validar(nome, senha)) {
                    abrirAplicacao();
                } else {
                    JOptionPane.showMessageDialog(this, "Nome ou senha incorretos!");
                }
            } else {
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
            arquivo.apagar();
            try {
                Files.deleteIfExists(Paths.get(GerenciarSaves.getCaminho()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            atualizarTela();
        }
    }
    private void fontes(int tamanho){
        for (Object chave : Collections.list(UIManager.getDefaults().keys())) {
            Object valor = UIManager.get(chave);
            if (valor instanceof FontUIResource) {
                FontUIResource f = (FontUIResource) valor;
                UIManager.put(chave, new FontUIResource(f.deriveFont((float) tamanho)));
            }
        }
    }
    private void abrirAplicacao() {
        dispose();
        fontes(16);
        TelaAbas abas = new TelaAbas();

        JFrame frame = new JFrame("Afazeres");
        frame.setContentPane(abas.getPainel());
        frame.pack();
        frame.setMinimumSize(new Dimension(520, 420));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    abas.salvar();
                }
            });
    }
}
