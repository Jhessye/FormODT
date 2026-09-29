package Model;

import java.util.ArrayList;
import java.util.List;
import Model.enums.AcaoVinculadaTipo;

public class Caracterizacao {
    // Pergunta 21: Atividades curriculares de extensão
    private boolean naoPossuiCurricular = false;
    private List<String> cursosCurriculares = new ArrayList<>();

    // Pergunta 22: Fomento da ação
    private List<String> fomentoSelecionados = new ArrayList<>();
    private String fomentoOutro = "";

    // Pergunta 23: Ação institucional mais abrangente
    private AcaoVinculadaTipo acaoMaisAbrangente = AcaoVinculadaTipo.NAO_VINCULADA;
    private String acaoMaisAbrangenteOutro = "";

    // Pergunta 24: Processo SIPAC (somente se vinculado à Ação de Extensão)
    private String numeroProcessoSipac = "";

    public Caracterizacao() {}

    public boolean isNaoPossuiCurricular() {
        return naoPossuiCurricular;
    }

    public void setNaoPossuiCurricular(boolean naoPossuiCurricular) {
        this.naoPossuiCurricular = naoPossuiCurricular;
    }

    public List<String> getCursosCurriculares() {
        return cursosCurriculares;
    }

    public void setCursosCurriculares(List<String> cursosCurriculares) {
        this.cursosCurriculares = cursosCurriculares;
    }

    public List<String> getFomentoSelecionados() {
        return fomentoSelecionados;
    }

    public void setFomentoSelecionados(List<String> fomentoSelecionados) {
        this.fomentoSelecionados = fomentoSelecionados;
    }

    public String getFomentoOutro() {
        return fomentoOutro;
    }

    public void setFomentoOutro(String fomentoOutro) {
        this.fomentoOutro = fomentoOutro;
    }

    public AcaoVinculadaTipo getAcaoMaisAbrangente() {
        return acaoMaisAbrangente;
    }

    public void setAcaoMaisAbrangente(AcaoVinculadaTipo acaoMaisAbrangente) {
        this.acaoMaisAbrangente = acaoMaisAbrangente;
    }

    public String getAcaoMaisAbrangenteOutro() {
        return acaoMaisAbrangenteOutro;
    }

    public void setAcaoMaisAbrangenteOutro(String acaoMaisAbrangenteOutro) {
        this.acaoMaisAbrangenteOutro = acaoMaisAbrangenteOutro;
    }

    public String getNumeroProcessoSipac() {
        return numeroProcessoSipac;
    }

    public void setNumeroProcessoSipac(String numeroProcessoSipac) {
        this.numeroProcessoSipac = numeroProcessoSipac;
    }
}
