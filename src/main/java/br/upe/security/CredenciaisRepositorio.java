package br.upe.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;

public class CredenciaisRepositorio {

    // Arquivo salvo na pasta do usuário: ~/.minhaapp/credenciais.dat
    private static final Path ARQUIVO = Paths.get(
            System.getProperty("user.home"), ".minhaapp", "credenciais.dat");

    public boolean existeConta() {
        return Files.exists(ARQUIVO);
    }

    private static final int ITERACOES = 600_000;

    public void salvar(String usuario, String senha) throws IOException {
        String salt = gerarSalt();
        String hash = hashSenha(senha, salt, ITERACOES);

        Files.createDirectories(ARQUIVO.getParent());
        String conteudo = usuario + "\n" + salt + "\n" + ITERACOES + "\n" + hash;
        Files.write(ARQUIVO, conteudo.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validar(String usuario, String senha) {
        try {
            String[] linhas = new String(Files.readAllBytes(ARQUIVO), StandardCharsets.UTF_8).split("\n");
            if (linhas.length < 4) return false;

            String usuarioSalvo = linhas[0];
            String salt = linhas[1];
            int iteracoes = Integer.parseInt(linhas[2]);
            String hashSalvo = linhas[3];

            String hashDigitado = hashSenha(senha, salt, iteracoes);
            return usuarioSalvo.equals(usuario)
                    && MessageDigest.isEqual(
                    hashSalvo.getBytes(StandardCharsets.UTF_8),
                    hashDigitado.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | IllegalArgumentException e) {
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

    private String hashSenha(String senha, String salt, int iteracoes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(), Base64.getDecoder().decode(salt), iteracoes, 256);
            byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
            spec.clearPassword();
            return Base64.getEncoder().encodeToString(hash);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }
}