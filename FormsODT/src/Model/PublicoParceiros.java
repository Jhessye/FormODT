package Model;

public class PublicoParceiros {
    private String caracterizacaoPublicoAlvo = "";
    private int totalPublicoExterno = 0;
    private String organizacoesParceiras = "";
    private String parceiroAportaRecursos = "";

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

    public String getOrganizacoesParceiras() {
        return organizacoesParceiras;
    }

    public void setOrganizacoesParceiras(String organizacoesParceiras) {
        this.organizacoesParceiras = organizacoesParceiras;
    }

    public String getParceiroAportaRecursos() {
        return parceiroAportaRecursos;
    }

    public void setParceiroAportaRecursos(String parceiroAportaRecursos) {
        this.parceiroAportaRecursos = parceiroAportaRecursos;
    }
}
