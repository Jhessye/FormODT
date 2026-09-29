package Model;

import Model.enums.CampusIfes;
import Model.enums.TipoCargo;

public class CoordenacaoAdjunta {
    private String nomeCoordenadorAdjunto = "";
    private String siape = "";
    private String email = "";
    private TipoCargo cargo = TipoCargo.DOCENTE;
    private String setor = "";
    private CampusIfes campus = CampusIfes.VITORIA;

    public CoordenacaoAdjunta() {}

    public String getNomeCoordenadorAdjunto() {
        return nomeCoordenadorAdjunto;
    }

    public void setNomeCoordenadorAdjunto(String nomeCoordenadorAdjunto) {
        this.nomeCoordenadorAdjunto = nomeCoordenadorAdjunto;
    }

    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoCargo getCargo() {
        return cargo;
    }

    public void setCargo(TipoCargo cargo) {
        this.cargo = cargo;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public CampusIfes getCampus() {
        return campus;
    }

    public void setCampus(CampusIfes campus) {
        this.campus = campus;
    }
}
