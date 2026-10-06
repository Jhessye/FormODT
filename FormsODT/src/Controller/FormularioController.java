package Controller;

import java.util.ArrayList;
import java.util.List;
import Model.*;
import Model.enums.*;
import Util.ComponentUtils;
import View.TelaPrincipal;

public class FormularioController {

    private final TelaPrincipal view;
    private final Formulario model;

    public FormularioController(TelaPrincipal view) {
        this.view = view;
        this.model = new Formulario();
    }

    public void inicializarEstado() {
        atualizarEstadoModalidade();
        atualizarEstadoAcaoVinculada();
        atualizarEstadoCoordenadorAdjunto();
        atualizarEstadoAtividadesCurriculares();
    }

    /**
     * Regra 1: Habilita/Desabilita painéis conforme a Modalidade da Ação selecionada
     */
    public void atualizarEstadoModalidade() {
        ModalidadeAcao modalidade = view.getModalidadeSelecionada();

        boolean isRede = (modalidade == ModalidadeAcao.PROGRAMA_REDE);
        boolean isMulticampi = (modalidade == ModalidadeAcao.PROGRAMA_MULTICAMPI);
        boolean isEvento = (modalidade == ModalidadeAcao.EVENTO);
        boolean isPrestacao = (modalidade == ModalidadeAcao.PRESTACAO_SERVICOS);

        ComponentUtils.setEnabledRecursive(view.getPanelProgramaRede(), isRede);
        ComponentUtils.setEnabledRecursive(view.getPanelProgramaMulticampi(), isMulticampi);
        ComponentUtils.setEnabledRecursive(view.getPanelEvento(), isEvento);
        ComponentUtils.setEnabledRecursive(view.getPanelPrestacaoServico(), isPrestacao);
    }

    /**
     * Regra 2: Habilita campo SIPAC apenas se vinculado à Ação de Extensão
     */
    public void atualizarEstadoAcaoVinculada() {
        AcaoVinculadaTipo tipo = view.getAcaoVinculadaSelecionada();
        boolean isExtensao = (tipo == AcaoVinculadaTipo.EXTENSAO);
        boolean isOutra = (tipo == AcaoVinculadaTipo.OUTRA);

        ComponentUtils.setEnabledRecursive(view.getPanelAcaoVinculada(), isExtensao);
        view.getTxtAcaoVinculadaOutra().setEnabled(isOutra);
        if (!isOutra) {
            view.getTxtAcaoVinculadaOutra().setText("");
        }
    }

    /**
     * Regra 3: Habilita Coordenação Adjunta apenas se houver coordenador adjunto
     */
    public void atualizarEstadoCoordenadorAdjunto() {
        boolean haAdjunto = view.isHaCoordenadorAdjuntoSelecionado();
        ComponentUtils.setEnabledRecursive(view.getPanelCoordenacaoAdjunta(), haAdjunto);
    }

    /**
     * Regra 4: Desabilita cursos se marcada opção "Não possui atividades curriculares"
     */
    public void atualizarEstadoAtividadesCurriculares() {
        boolean naoPossui = view.isNaoPossuiCurricularMarcado();
        view.getTxtCursosCurriculares().setEnabled(!naoPossui);
    }

    /**
     * Coleta todos os dados da tela para o objeto Formulario
     */
    public Formulario extrairDadosDaTela() {
        // 1. Dados Cadastrais
        DadosCadastrais dc = model.getDadosCadastrais();
        dc.setTituloAcao(view.getTxtTituloAcao().getText().trim());
        dc.setNomeCoordenador(view.getTxtNomeCoordenador().getText().trim());
        dc.setSiape(view.getTxtSiape().getText().trim());
        dc.setEmail(view.getTxtEmail().getText().trim());
        dc.setCargo(view.getCargoSelecionado());
        dc.setSetor(view.getTxtSetor().getText().trim());
        dc.setCampus(view.getCampusSelecionado());
        dc.setInicioVigencia(view.getTxtInicioVigencia().getText().trim());
        dc.setFimVigencia(view.getTxtFimVigencia().getText().trim());
        dc.setPropostaInovadora(view.getPropostaInovadoraSelecionada());

        // 2. Modalidade e seções condicionais
        SecaoModalidadeEspecifica sme = model.getModalidadeEspecifica();
        sme.setModalidade(view.getModalidadeSelecionada());
        if (sme.getModalidade() == ModalidadeAcao.PROGRAMA_REDE) {
            sme.setRedeConfirmado(view.isRedeConfirmado());
        } else {
            sme.setRedeConfirmado(false);
        }

        if (sme.getModalidade() == ModalidadeAcao.PROGRAMA_MULTICAMPI) {
            sme.setUnidadesMulticampi(view.getCampiMulticampiSelecionados());
        } else {
            sme.setUnidadesMulticampi(new ArrayList<>());
        }

        if (sme.getModalidade() == ModalidadeAcao.EVENTO) {
            sme.setItensProgramacaoEvento(view.getPainelProgramacaoEvento().getItens());
            sme.setProgramacaoEvento(sme.getProgramacaoEvento());
        } else {
            sme.setItensProgramacaoEvento(new ArrayList<>());
            sme.setProgramacaoEvento("");
        }

        if (sme.getModalidade() == ModalidadeAcao.PRESTACAO_SERVICOS) {
            sme.setPrestacaoNomeResponsavel(view.getTxtPrestacaoNome().getText().trim());
            sme.setPrestacaoRegistroTecnico(view.getTxtPrestacaoRegistro().getText().trim());
            sme.setPrestacaoSiape(view.getTxtPrestacaoSiape().getText().trim());
            sme.setPrestacaoEmail(view.getTxtPrestacaoEmail().getText().trim());
            sme.setPrestacaoDescricaoTecnica(view.getTxtPrestacaoDescricao().getText().trim());
        } else {
            sme.setPrestacaoNomeResponsavel("");
            sme.setPrestacaoRegistroTecnico("");
            sme.setPrestacaoSiape("");
            sme.setPrestacaoEmail("");
            sme.setPrestacaoDescricaoTecnica("");
        }

        // 3. Caracterização
        Caracterizacao car = model.getCaracterizacao();
        car.setNaoPossuiCurricular(view.isNaoPossuiCurricularMarcado());
        List<String> cursos = new ArrayList<>();
        if (!car.isNaoPossuiCurricular() && !view.getTxtCursosCurriculares().getText().trim().isEmpty()) {
            cursos.add(view.getTxtCursosCurriculares().getText().trim());
        }
        car.setCursosCurriculares(cursos);
        car.setFomentoSelecionados(view.getFomentosSelecionados());
        car.setFomentoOutro(view.getTxtFomentoOutro().getText().trim());
        car.setAcaoMaisAbrangente(view.getAcaoVinculadaSelecionada());
        car.setAcaoMaisAbrangenteOutro(view.getTxtAcaoVinculadaOutra().getText().trim());
        if (car.getAcaoMaisAbrangente() == AcaoVinculadaTipo.EXTENSAO) {
            car.setNumeroProcessoSipac(view.getTxtNumeroProcessoSipac().getText().trim());
        } else {
            car.setNumeroProcessoSipac("");
        }

        // 4. Áreas Temáticas e ODS
        AreasTematicas at = model.getAreasTematicas();
        at.setAreaPrincipal(view.getAreaPrincipalSelecionada());
        at.setAreaSecundaria(view.getAreaSecundariaSelecionada());
        at.setOdsSelecionados(view.getOdsSelecionados());

        // 5. Público Alvo e Parceiros
        PublicoParceiros pp = model.getPublicoParceiros();
        pp.setCaracterizacaoPublicoAlvo(view.getTxtCaracterizacaoPublicoAlvo().getText().trim());
        pp.setTotalPublicoExterno(view.getNumeroPublicoExterno());
        pp.setOrganizacoesParceiras(view.getTxtOrganizacoesParceiras().getText().trim());
        pp.setParceiroAportaRecursos(view.getTxtParceiroRecursos());

        // 6. Equipe Executora
        EquipeExecutora ee = model.getEquipeExecutora();
        ee.setEstudantesFic(view.getNumeroInteiro(view.getTxtEstudantesFic().getText()));
        ee.setEstudantesTecnico(view.getNumeroInteiro(view.getTxtEstudantesTecnico().getText()));
        ee.setEstudantesGraduacao(view.getNumeroInteiro(view.getTxtEstudantesGraduacao().getText()));
        ee.setEstudantesPosGraduacao(view.getNumeroInteiro(view.getTxtEstudantesPosGraduacao().getText()));
        ee.setServidoresDocentes(view.getNumeroInteiro(view.getTxtDocentes().getText()));
        ee.setServidoresTecnicoAdministrativos(view.getNumeroInteiro(view.getTxtTae().getText()));
        ee.setColaboradoresExternos(view.getNumeroInteiro(view.getTxtColaboradoresExternos().getText()));
        ee.setHaCoordenadorAdjunto(view.isHaCoordenadorAdjuntoSelecionado());

        // 7. Coordenação Adjunta
        CoordenacaoAdjunta ca = model.getCoordenacaoAdjunta();
        if (ee.isHaCoordenadorAdjunto()) {
            ca.setNomeCoordenadorAdjunto(view.getTxtAdjuntoNome().getText().trim());
            ca.setSiape(view.getTxtAdjuntoSiape().getText().trim());
            ca.setEmail(view.getTxtAdjuntoEmail().getText().trim());
            ca.setCargo(view.getAdjuntoCargoSelecionado());
            ca.setSetor(view.getTxtAdjuntoSetor().getText().trim());
            ca.setCampus(view.getAdjuntoCampusSelecionado());
        } else {
            ca.setNomeCoordenadorAdjunto("");
            ca.setSiape("");
            ca.setEmail("");
            ca.setSetor("");
        }

        // 8. Público Interno
        PublicoInterno pi = model.getPublicoInterno();
        pi.setCaracterizacaoPublicoInterno(view.getTxtCaracterizacaoPublicoInterno().getText().trim());
        pi.setTotalPublicoInterno(view.getNumeroPublicoInterno());

        // 9. Detalhamento da Ação
        DetalhamentoAcao da = model.getDetalhamentoAcao();
        da.setResumo(view.getTxtResumo().getText().trim());
        da.setPalavrasChave(view.getTxtPalavrasChave().getText().trim());
        da.setObjetivoGeral(view.getTxtObjetivoGeral().getText().trim());
        da.setObjetivosEspecificos(view.getTxtObjetivosEspecificos().getText().trim());

        // 10. Fundamentação
        Fundamentacao fun = model.getFundamentacao();
        fun.setInfluenciaGruposSociais(view.getTxtInfluenciaGrupos().getText().trim());
        fun.setMudancasPublicoExterno(view.getTxtMudancasPublico().getText().trim());
        fun.setRelacaoEnsinoPesquisa(view.getTxtRelacaoEnsinoPesquisa().getText().trim());
        fun.setProtagonismoEstudantes(view.getTxtProtagonismoEstudantes().getText().trim());
        fun.setInstalacoesEquipamentos(view.getTxtInstalacoesEquipamentos().getText().trim());

        // 11. Cronograma
        Cronograma cro = model.getCronograma();
        cro.setItens(view.getPainelCronogramaVisual().getItens());
        cro.setObservacoes(view.getTxtObservacoes().getText().trim());

        return model;
    }

    public void preencherDadosExemplo() {
        view.preencherExemplo();
        inicializarEstado();
    }

    public void limparFormulario() {
        view.limparCampos();
        inicializarEstado();
    }
}
