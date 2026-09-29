package Model;

import java.util.ArrayList;
import java.util.List;
import Model.enums.CampusIfes;
import Model.enums.ModalidadeAcao;

public class SecaoModalidadeEspecifica {
    private ModalidadeAcao modalidade = ModalidadeAcao.SELECIONE;
    
    // Para Programa em Rede (Pergunta 14)
    private Boolean redeConfirmado = false;

    // Para Programa Multicampi (Pergunta 13)
    private List<CampusIfes> unidadesMulticampi = new ArrayList<>();

    // Para Evento (Pergunta 15)
    private String programacaoEvento = "";

    // Para Prestação de Serviços (Perguntas 16 a 20)
    private String prestacaoNomeResponsavel = "";
    private String prestacaoRegistroTecnico = "";
    private String prestacaoSiape = "";
    private String prestacaoEmail = "";
    private String prestacaoDescricaoTecnica = "";

    public SecaoModalidadeEspecifica() {}

    public ModalidadeAcao getModalidade() {
        return modalidade;
    }

    public void setModalidade(ModalidadeAcao modalidade) {
        this.modalidade = modalidade;
    }

    public Boolean getRedeConfirmado() {
        return redeConfirmado;
    }

    public void setRedeConfirmado(Boolean redeConfirmado) {
        this.redeConfirmado = redeConfirmado;
    }

    public List<CampusIfes> getUnidadesMulticampi() {
        return unidadesMulticampi;
    }

    public void setUnidadesMulticampi(List<CampusIfes> unidadesMulticampi) {
        this.unidadesMulticampi = unidadesMulticampi;
    }

    public String getProgramacaoEvento() {
        return programacaoEvento;
    }

    public void setProgramacaoEvento(String programacaoEvento) {
        this.programacaoEvento = programacaoEvento;
    }

    public String getPrestacaoNomeResponsavel() {
        return prestacaoNomeResponsavel;
    }

    public void setPrestacaoNomeResponsavel(String prestacaoNomeResponsavel) {
        this.prestacaoNomeResponsavel = prestacaoNomeResponsavel;
    }

    public String getPrestacaoRegistroTecnico() {
        return prestacaoRegistroTecnico;
    }

    public void setPrestacaoRegistroTecnico(String prestacaoRegistroTecnico) {
        this.prestacaoRegistroTecnico = prestacaoRegistroTecnico;
    }

    public String getPrestacaoSiape() {
        return prestacaoSiape;
    }

    public void setPrestacaoSiape(String prestacaoSiape) {
        this.prestacaoSiape = prestacaoSiape;
    }

    public String getPrestacaoEmail() {
        return prestacaoEmail;
    }

    public void setPrestacaoEmail(String prestacaoEmail) {
        this.prestacaoEmail = prestacaoEmail;
    }

    public String getPrestacaoDescricaoTecnica() {
        return prestacaoDescricaoTecnica;
    }

    public void setPrestacaoDescricaoTecnica(String prestacaoDescricaoTecnica) {
        this.prestacaoDescricaoTecnica = prestacaoDescricaoTecnica;
    }
}
