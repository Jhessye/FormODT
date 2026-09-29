package Model.enums;

public enum TipoCargo {
    ADMINISTRATIVO("Servidor administrativo"),
    DOCENTE("Servidor docente");

    private final String descricao;

    TipoCargo(String descricao) {
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
