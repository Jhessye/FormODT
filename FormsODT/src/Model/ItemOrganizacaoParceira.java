package Model;

public class ItemOrganizacaoParceira {

    private String nome;
    private String descricaoParticipacao;
    private String aportaRecursos; // "Sim" ou "Não"

    public ItemOrganizacaoParceira() {
        this("", "", "Não");
    }

    public ItemOrganizacaoParceira(String nome, String descricaoParticipacao, String aportaRecursos) {
        this.nome = nome != null ? nome : "";
        this.descricaoParticipacao = descricaoParticipacao != null ? descricaoParticipacao : "";
        this.aportaRecursos = aportaRecursos != null ? aportaRecursos : "Não";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricaoParticipacao() {
        return descricaoParticipacao;
    }

    public void setDescricaoParticipacao(String descricaoParticipacao) {
        this.descricaoParticipacao = descricaoParticipacao;
    }

    public String getAportaRecursos() {
        return aportaRecursos;
    }

    public void setAportaRecursos(String aportaRecursos) {
        this.aportaRecursos = aportaRecursos;
    }
}
