package Model;

import java.util.ArrayList;
import java.util.List;

public class PublicoParceiros {
    private String caracterizacaoPublicoAlvo = "";
    private int totalPublicoExterno = 0;
    private List<ItemOrganizacaoParceira> organizacoes = new ArrayList<>();

    public PublicoParceiros() {}

    public String getCaracterizacaoPublicoAlvo() {
        return caracterizacaoPublicoAlvo;
    }

    public void setCaracterizacaoPublicoAlvo(String caracterizacaoPublicoAlvo) {
        this.caracterizacaoPublicoAlvo = caracterizacaoPublicoAlvo;
    }

    public int getTotalPublicoExterno() {
        return totalPublicoExterno;
    }

    public void setTotalPublicoExterno(int totalPublicoExterno) {
        this.totalPublicoExterno = totalPublicoExterno;
    }

    public List<ItemOrganizacaoParceira> getOrganizacoes() {
        return organizacoes;
    }

    public void setOrganizacoes(List<ItemOrganizacaoParceira> organizacoes) {
        this.organizacoes = organizacoes != null ? organizacoes : new ArrayList<>();
    }

    // Métodos para compatibilidade e texto resumo
    public String getOrganizacoesParceiras() {
        if (organizacoes == null || organizacoes.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (ItemOrganizacaoParceira org : organizacoes) {
            if (sb.length() > 0) sb.append("\n");
            sb.append("• ").append(org.getNome());
            if (org.getDescricaoParticipacao() != null && !org.getDescricaoParticipacao().isEmpty() && !"-".equals(org.getDescricaoParticipacao())) {
                sb.append(" - ").append(org.getDescricaoParticipacao());
            }
            sb.append(" (Aporta recursos: ").append(org.getAportaRecursos()).append(")");
        }
        return sb.toString();
    }

    public void setOrganizacoesParceiras(String organizacoesParceiras) {
        // mantido para compatibilidade caso texto bruto seja informado
    }

    public String getParceiroAportaRecursos() {
        if (organizacoes == null || organizacoes.isEmpty()) return "Não se aplica";
        for (ItemOrganizacaoParceira org : organizacoes) {
            if ("Sim".equalsIgnoreCase(org.getAportaRecursos())) {
                return "Sim";
            }
        }
        return "Não";
    }

    public void setParceiroAportaRecursos(String parceiroAportaRecursos) {
        // mantido para compatibilidade
    }
}
