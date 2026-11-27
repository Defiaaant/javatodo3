package br.upe.controller;

import br.upe.model.Tarefa;
import br.upe.model.TarefaTableModel;

import java.util.List;

public class TarefaControlador {

    // Atributos
    private TarefaTableModel tarefaTableModel;

    //Construtor -> mudei ó kkk
    public TarefaControlador(List<Tarefa> ativas, List<Tarefa> finalizadas) {
        tarefaTableModel = new TarefaTableModel();
        tarefaTableModel.setTarefasAtivas(ativas);
        tarefaTableModel.setTarefasFinalizadas(finalizadas);
    }

    public int removerFinalizados(int[] linhasSelecionadas) {
        return tarefaTableModel.removerFinalizados(linhasSelecionadas);
    }

    //Metodos de negocio
    public void adicionarTarefaAtiva(Tarefa tarefa) {
        this.tarefaTableModel.getTarefasAtivas().add(tarefa);
        this.tarefaTableModel.fireTableDataChanged();
    }

    public void exibirFinalizadas(boolean exibir) {
        tarefaTableModel.setExibirFinalizadas(exibir);
    }


    //Getter e Setters

    public TarefaTableModel getTarefaTableModel() {
        return tarefaTableModel;
    }

    public void setTarefaTableModel(TarefaTableModel tarefaTableModel) {
        this.tarefaTableModel = tarefaTableModel;
    }

}
