package Model;

import Model.enums.CampusIfes;
import Model.enums.InovacaoResposta;
import Model.enums.TipoCargo;

public class DadosCadastrais {
    private String tituloAcao = "";
    private String nomeCoordenador = "";
    private String siape = "";
    private String email = "";
    private TipoCargo cargo = TipoCargo.DOCENTE;
    private String setor = "";
    private CampusIfes campus = CampusIfes.VITORIA;
    private String emailSetorExtensao = "";
    private String inicioVigencia = "";
    private String fimVigencia = "";
    private InovacaoResposta propostaInovadora = InovacaoResposta.NAO;

    public DadosCadastrais() {}

    public String getTituloAcao() {
        return tituloAcao;
    }

    public void setTituloAcao(String tituloAcao) {
        this.tituloAcao = tituloAcao;
    }

    public String getNomeCoordenador() {
        return nomeCoordenador;
    }

    public void setNomeCoordenador(String nomeCoordenador) {
        this.nomeCoordenador = nomeCoordenador;
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

    public String getEmailSetorExtensao() {
        return emailSetorExtensao;
    }

    public void setEmailSetorExtensao(String emailSetorExtensao) {
        this.emailSetorExtensao = emailSetorExtensao;
    }

    public String getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(String inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public String getFimVigencia() {
        return fimVigencia;
    }

    public void setFimVigencia(String fimVigencia) {
        this.fimVigencia = fimVigencia;
    }

    public InovacaoResposta getPropostaInovadora() {
        return propostaInovadora;
    }

    public void setPropostaInovadora(InovacaoResposta propostaInovadora) {
        this.propostaInovadora = propostaInovadora;
    }
}
