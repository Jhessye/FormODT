package Model;

import java.util.ArrayList;
import java.util.List;

public class Cronograma {
    private List<ItemCronograma> itens = new ArrayList<>();
    private String atividadesCronograma = "";
    private String observacoes = "";

    public Cronograma() {}

    public List<ItemCronograma> getItens() {
        return itens;
    }

    public void setItens(List<ItemCronograma> itens) {
        this.itens = itens != null ? itens : new ArrayList<>();
    }

    public void adicionarItem(ItemCronograma item) {
        if (item != null) {
            this.itens.add(item);
        }
    }

    public void limparItens() {
        this.itens.clear();
    }

    /**
     * Retorna o texto formatado no padrão solicitado na pergunta 57:
     * "Atividade 1 - [Descrição]... Mês X a Mês Y"
     */
    public String getAtividadesCronograma() {
        if (!itens.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int count = 1;
            for (ItemCronograma item : itens) {
                sb.append("Atividade ").append(count++).append(" - ")
                  .append(item.getDescricao()).append(" ... ")
                  .append(item.getPeriodoFormatado()).append("\n");
            }
            return sb.toString().trim();
        }
        return atividadesCronograma;
    }

    public void setAtividadesCronograma(String atividadesCronograma) {
        this.atividadesCronograma = atividadesCronograma != null ? atividadesCronograma : "";
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes != null ? observacoes : "";
    }
}
