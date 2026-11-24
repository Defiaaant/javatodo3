package br.upe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class CredenciaisRepositorio {

    // Arquivo salvo na pasta do usuário: ~/.minhaapp/credenciais.dat
    private static final Path ARQUIVO = Paths.get(
            System.getProperty("user.home"), ".minhaapp", "credenciais.dat");

    public boolean existeConta() {
        return Files.exists(ARQUIVO);
    }

    public void salvar(String usuario, String senha) throws IOException {
        String salt = gerarSalt();
        String hash = hashSenha(senha, salt);

        Files.createDirectories(ARQUIVO.getParent());
        String conteudo = usuario + "\n" + salt + "\n" + hash;
        Files.write(ARQUIVO, conteudo.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validar(String usuario, String senha) {
        try {
            String[] linhas = new String(Files.readAllBytes(ARQUIVO), StandardCharsets.UTF_8)
                    .split("\n");
            if (linhas.length < 3) return false;

            String usuarioSalvo = linhas[0];
            String salt = linhas[1];
            String hashSalvo = linhas[2];

            return usuarioSalvo.equals(usuario)
                    && hashSalvo.equals(hashSenha(senha, salt));
        } catch (IOException e) {
            return false;
        }
    }

    public void apagar() throws IOException {
        Files.deleteIfExists(ARQUIVO);
    }

    private String gerarSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashSenha(String senha, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hash = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}