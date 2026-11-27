package br.upe.savetool;

import br.upe.model.Tarefa;
import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GerenciarSaves {
    private static final String caminho = "notas.json";

    public static String getCaminho(){
        return caminho;
    }

    private static class DadosSalvos {
        List<Tarefa> ativas;
        List<Tarefa> finalizadas;

        DadosSalvos(List<Tarefa> ativas, List<Tarefa> finalizadas) {
            this.ativas = ativas;
            this.finalizadas = finalizadas;
        }
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

    public static void salvarNotas(List<Tarefa> ativas, List<Tarefa> finalizadas) {
        Gson gson = criarGson();
        DadosSalvos dados = new DadosSalvos(ativas, finalizadas);
        String json = gson.toJson(dados);

        try {
            Files.writeString(Paths.get(caminho), json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Erro ao salvar notas: " + e.getMessage());
        }
    }

    public static List<List<Tarefa>> carregarNotas() {
        List<List<Tarefa>> resultado = new ArrayList<>();
        Path path = Paths.get(caminho);

        if (!Files.exists(path)) {
            resultado.add(new ArrayList<>());
            resultado.add(new ArrayList<>());
            return resultado;
        }
        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);
            Gson gson = criarGson();
            DadosSalvos dados = gson.fromJson(json, DadosSalvos.class);

            if (dados != null) {
                resultado.add(dados.ativas != null ? dados.ativas : new ArrayList<>());
                resultado.add(dados.finalizadas != null ? dados.finalizadas : new ArrayList<>());
            } else {
                resultado.add(new ArrayList<>());
                resultado.add(new ArrayList<>());
            }
        }
        catch (IOException | JsonParseException | java.time.DateTimeException e) {
            System.err.println("Erro ao ler Notas: " + e.getMessage());
            resultado.add(new ArrayList<>());
            resultado.add(new ArrayList<>());
        }
        return resultado;
    }
}
