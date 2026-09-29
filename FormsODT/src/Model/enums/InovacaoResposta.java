package Model.enums;

public enum InovacaoResposta {
    SIM("Sim"),
    NAO("Não"),
    NAO_SEI("Não sei informar");

    private final String descricao;

    InovacaoResposta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
