package Model.enums;

public enum AcaoVinculadaTipo {
    EXTENSAO("Ação de Extensão"),
    PESQUISA("Programa de pesquisa"),
    ENSINO("Programa de Ensino"),
    POS_GRADUACAO("Programa de Pós-graduação"),
    NAO_VINCULADA("Não está vinculada a ação mais abrangente"),
    OUTRA("Outra");

    private final String descricao;

    AcaoVinculadaTipo(String descricao) {
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
