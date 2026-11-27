package br.upe.savetool;

import br.upe.model.Tarefa;
import com.google.gson.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GerenciarSaves {
    private static final String caminho = "notas.json";

    public static String getCaminho() {
        return caminho;
    }

    public static class DadosLista {
        public String nome;
        public List<Tarefa> ativas;
        public List<Tarefa> finalizadas;

        public DadosLista(String nome, List<Tarefa> ativas, List<Tarefa> finalizadas) {
            this.nome = nome;
            this.ativas = ativas;
            this.finalizadas = finalizadas;
        }
    }

    private static class Arquivo {
        List<DadosLista> listas;
    }

    private static Gson criarGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, type, ctx) -> new JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, type, ctx) -> LocalDate.parse(json.getAsString()))
                .create();
    }

    public static void salvarListas(List<DadosLista> listas) {
        Arquivo arquivo = new Arquivo();
        arquivo.listas = listas;
        try {
            Files.writeString(Paths.get(caminho), criarGson().toJson(arquivo), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Erro ao salvar notas: " + e.getMessage());
        }
    }

    public static List<DadosLista> carregarListas() {
        List<DadosLista> resultado = new ArrayList<>();
        Path path = Paths.get(caminho);

        if (Files.exists(path)) {
            try {
                Gson gson = criarGson();
                JsonObject raiz = JsonParser.parseString(
                        Files.readString(path, StandardCharsets.UTF_8)).getAsJsonObject();

                if (raiz.has("listas")) {
                    Arquivo arquivo = gson.fromJson(raiz, Arquivo.class);
                    if (arquivo.listas != null) resultado.addAll(arquivo.listas);
                } else {
                    DadosLista antiga = gson.fromJson(raiz, DadosLista.class);
                    antiga.nome = "Minha lista";
                    resultado.add(antiga);
                }
            } catch (IOException | JsonParseException | IllegalStateException
                     | java.time.DateTimeException e) {
                System.err.println("Erro ao ler Notas: " + e.getMessage());
                resultado.clear();
            }
        }

        if (resultado.isEmpty()) {
            resultado.add(new DadosLista("Minha lista", new ArrayList<>(), new ArrayList<>()));
        }
        for (int i = 0; i < resultado.size(); i++) {
            DadosLista d = resultado.get(i);
            if (d.nome == null || d.nome.isBlank()) d.nome = "Lista " + (i + 1);
            if (d.ativas == null) d.ativas = new ArrayList<>();
            if (d.finalizadas == null) d.finalizadas = new ArrayList<>();
        }
        return resultado;
    }
}