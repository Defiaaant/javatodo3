package br.upe.security;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ArquivoUsuario {

    private File arquivo = new File("usuario.txt");

    // Retorna true se já existe um usuário salvo
    public boolean existeConta() {
        return arquivo.exists();
    }

    // Salva nome e senha no arquivo (uma em cada linha)
    public void salvar(String nome, String senha) throws IOException {
        PrintWriter escritor = new PrintWriter(arquivo);
        escritor.println(nome);
        escritor.println(senha);
        escritor.close();
    }

    // Confere se o nome e a senha digitados são iguais aos salvos
    public boolean validar(String nome, String senha) throws IOException {
        Scanner leitor = new Scanner(arquivo);
        String nomeSalvo = leitor.nextLine();
        String senhaSalva = leitor.nextLine();
        leitor.close();

        return nome.equals(nomeSalvo) && senha.equals(senhaSalva);
    }

    // Apaga o arquivo (apaga a conta)
    public void apagar() {
        arquivo.delete();
    }
}