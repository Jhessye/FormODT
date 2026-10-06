package Model;

public class ItemCronograma {
    private String descricao;
    private int mesInicio;
    private int mesFim;

    public ItemCronograma() {
        this.descricao = "";
        this.mesInicio = 1;
        this.mesFim = 1;
    }

    public ItemCronograma(String descricao, int mesInicio, int mesFim) {
        this.descricao = descricao != null ? descricao.trim() : "";
        this.mesInicio = Math.max(1, mesInicio);
        this.mesFim = Math.max(this.mesInicio, mesFim);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao != null ? descricao.trim() : "";
    }

    public int getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(int mesInicio) {
        this.mesInicio = Math.max(1, mesInicio);
        if (this.mesFim < this.mesInicio) {
            this.mesFim = this.mesInicio;
        }
    }

    public int getMesFim() {
        return mesFim;
    }

    public void setMesFim(int mesFim) {
        this.mesFim = Math.max(this.mesInicio, mesFim);
    }

    public boolean isAtivoNoMes(int mes) {
        return mes >= mesInicio && mes <= mesFim;
    }

    public String getPeriodoFormatado() {
        if (mesInicio == mesFim) {
            return "Mês " + mesInicio;
        }
        return "Mês " + mesInicio + " a Mês " + mesFim;
    }

    public int getDuracaoMeses() {
        return (mesFim - mesInicio) + 1;
    }

    @Override
    public String toString() {
        return descricao + " (" + getPeriodoFormatado() + ")";
    }
}
