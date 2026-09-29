package Model.enums;

public enum ModalidadeAcao {
    SELECIONE("Selecione a modalidade..."),
    PROGRAMA_REDE("Programa em Rede"),
    PROGRAMA_MULTICAMPI("Programa multicampi"),
    PROGRAMA("Programa"),
    PROJETO("Projeto"),
    EVENTO("Evento"),
    PRESTACAO_SERVICOS("Prestação de serviços");

    private final String descricao;

    ModalidadeAcao(String descricao) {
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
