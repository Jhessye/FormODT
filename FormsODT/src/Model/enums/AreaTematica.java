package Model.enums;

public enum AreaTematica {
    COMUNICACAO("Comunicação"),
    CULTURA("Cultura"),
    DIREITOS_HUMANOS("Direitos humanos e justiça"),
    EDUCACAO("Educação"),
    MEIO_AMBIENTE("Meio ambiente"),
    SAUDE("Saúde"),
    TECNOLOGIA_E_PRODUCAO("Tecnologia e produção"),
    TRABALHO("Trabalho");

    private final String descricao;

    AreaTematica(String descricao) {
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
