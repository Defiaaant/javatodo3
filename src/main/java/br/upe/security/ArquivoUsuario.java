package br.upe.security;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ArquivoUsuario {

    private File arquivo = new File("usuario.txt");

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

    public boolean validar(String nome, String senha) throws IOException {
        Scanner leitor = new Scanner(arquivo);
        String nomeSalvo = leitor.nextLine();
        String senhaSalva = leitor.nextLine();
        leitor.close();

        return nome.equals(nomeSalvo) && senha.equals(senhaSalva);
    }

    public void apagar() {

        arquivo.delete();
    }
}
