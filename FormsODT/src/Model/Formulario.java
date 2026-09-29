package Model;

public class Formulario {
    private DadosCadastrais dadosCadastrais = new DadosCadastrais();
    private SecaoModalidadeEspecifica modalidadeEspecifica = new SecaoModalidadeEspecifica();
    private Caracterizacao caracterizacao = new Caracterizacao();
    private AreasTematicas areasTematicas = new AreasTematicas();
    private PublicoParceiros publicoParceiros = new PublicoParceiros();
    private EquipeExecutora equipeExecutora = new EquipeExecutora();
    private CoordenacaoAdjunta coordenacaoAdjunta = new CoordenacaoAdjunta();
    private PublicoInterno publicoInterno = new PublicoInterno();
    private DetalhamentoAcao detalhamentoAcao = new DetalhamentoAcao();
    private Fundamentacao fundamentacao = new Fundamentacao();
    private Cronograma cronograma = new Cronograma();

    public Formulario() {}

    public DadosCadastrais getDadosCadastrais() {
        return dadosCadastrais;
    }

    public void setDadosCadastrais(DadosCadastrais dadosCadastrais) {
        this.dadosCadastrais = dadosCadastrais;
    }

    public SecaoModalidadeEspecifica getModalidadeEspecifica() {
        return modalidadeEspecifica;
    }

    public void setModalidadeEspecifica(SecaoModalidadeEspecifica modalidadeEspecifica) {
        this.modalidadeEspecifica = modalidadeEspecifica;
    }

    public Caracterizacao getCaracterizacao() {
        return caracterizacao;
    }

    public void setCaracterizacao(Caracterizacao caracterizacao) {
        this.caracterizacao = caracterizacao;
    }

    public AreasTematicas getAreasTematicas() {
        return areasTematicas;
    }

    public void setAreasTematicas(AreasTematicas areasTematicas) {
        this.areasTematicas = areasTematicas;
    }

    public PublicoParceiros getPublicoParceiros() {
        return publicoParceiros;
    }

    public void setPublicoParceiros(PublicoParceiros publicoParceiros) {
        this.publicoParceiros = publicoParceiros;
    }

    public EquipeExecutora getEquipeExecutora() {
        return equipeExecutora;
    }

    public void setEquipeExecutora(EquipeExecutora equipeExecutora) {
        this.equipeExecutora = equipeExecutora;
    }

    public CoordenacaoAdjunta getCoordenacaoAdjunta() {
        return coordenacaoAdjunta;
    }

    public void setCoordenacaoAdjunta(CoordenacaoAdjunta coordenacaoAdjunta) {
        this.coordenacaoAdjunta = coordenacaoAdjunta;
    }

    public PublicoInterno getPublicoInterno() {
        return publicoInterno;
    }

    public void setPublicoInterno(PublicoInterno publicoInterno) {
        this.publicoInterno = publicoInterno;
    }

    public DetalhamentoAcao getDetalhamentoAcao() {
        return detalhamentoAcao;
    }

    public void setDetalhamentoAcao(DetalhamentoAcao detalhamentoAcao) {
        this.detalhamentoAcao = detalhamentoAcao;
    }

    public Fundamentacao getFundamentacao() {
        return fundamentacao;
    }

    public void setFundamentacao(Fundamentacao fundamentacao) {
        this.fundamentacao = fundamentacao;
    }

    public Cronograma getCronograma() {
        return cronograma;
    }

    public void setCronograma(Cronograma cronograma) {
        this.cronograma = cronograma;
    }
}
