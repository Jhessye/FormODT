package Model;

public class ItemProgramacaoEvento {
    private String atividade = "";
    private String data = "";
    private String horario = "";
    private String local = "";
    private String responsavel = "";

    public ItemProgramacaoEvento() {}

    public ItemProgramacaoEvento(String atividade, String data, String horario, String local, String responsavel) {
        this.atividade = atividade != null ? atividade.trim() : "";
        this.data = data != null ? data.trim() : "";
        this.horario = horario != null ? horario.trim() : "";
        this.local = local != null ? local.trim() : "";
        this.responsavel = responsavel != null ? responsavel.trim() : "";
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade != null ? atividade.trim() : "";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data != null ? data.trim() : "";
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario != null ? horario.trim() : "";
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local != null ? local.trim() : "";
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel != null ? responsavel.trim() : "";
    }

    @Override
    public String toString() {
        return atividade + " | " + data + " " + horario + " | " + local + " | " + responsavel;
    }
}
