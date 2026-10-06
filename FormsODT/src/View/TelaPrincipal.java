package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;

import Controller.DocumentoController;
import Controller.FormularioController;
import Model.ItemCronograma;
import Model.ItemProgramacaoEvento;
import Model.enums.AcaoVinculadaTipo;
import Model.enums.AreaTematica;
import Model.enums.CampusIfes;
import Model.enums.InovacaoResposta;
import Model.enums.ModalidadeAcao;
import Model.enums.TipoCargo;
import Util.FiltroLimiteCaracteres;
import Util.FiltroSomenteNumeros;

public class TelaPrincipal extends JFrame {

    private FormularioController formularioController;
    private DocumentoController documentoController;

    // Seção I: Dados Cadastrais
    private JTextField txtTituloAcao;
    private JTextField txtNomeCoordenador;
    private JTextField txtSiape;
    private JTextField txtEmail;
    private JRadioButton rbCargoDocente;
    private JRadioButton rbCargoAdmin;
    private JTextField txtSetor;
    private JComboBox<CampusIfes> comboCampus;
    private JTextField txtInicioVigencia;
    private JTextField txtFimVigencia;
    private JRadioButton rbInovacaoSim;
    private JRadioButton rbInovacaoNao;
    private JRadioButton rbInovacaoNaoSei;

    // Seção Modalidade (Decisão 1)
    private JComboBox<ModalidadeAcao> comboModalidade;
    private JPanel panelProgramaRede;
    private JRadioButton rbRedeSim;
    private JRadioButton rbRedeNao;

    private JPanel panelProgramaMulticampi;
    private Map<CampusIfes, JCheckBox> checkCampiMulticampi = new HashMap<>();

    private JPanel panelEvento;
    private PainelProgramacaoEvento painelProgramacaoEvento;

    private JPanel panelPrestacaoServico;
    private JTextField txtPrestacaoNome;
    private JTextField txtPrestacaoRegistro;
    private JTextField txtPrestacaoSiape;
    private JTextField txtPrestacaoEmail;
    private JTextArea txtPrestacaoDescricao;

    // Seção II: Caracterização (Decisão 2)
    private JCheckBox chkNaoPossuiCurricular;
    private JTextField txtCursosCurriculares;
    private List<JCheckBox> chkFomentos = new ArrayList<>();
    private JTextField txtFomentoOutro;
    private JComboBox<AcaoVinculadaTipo> comboAcaoVinculada;
    private JTextField txtAcaoVinculadaOutra;
    private JPanel panelAcaoVinculada;
    private JTextField txtNumeroProcessoSipac;

    // Seção III: Áreas Temáticas
    private JComboBox<AreaTematica> comboAreaPrincipal;
    private JComboBox<String> comboAreaSecundaria;
    private List<JCheckBox> checkOds = new ArrayList<>();

    // Seção IV: Público Alvo e Parceiros
    private JTextArea txtCaracterizacaoPublicoAlvo;
    private JTextField txtNumeroPublicoExterno;
    private JTextArea txtOrganizacoesParceiras;
    private JComboBox<String> comboParceiroRecursos;

    // Seção V: Equipe Executora (Decisão 3)
    private JTextField txtEstudantesFic;
    private JTextField txtEstudantesTecnico;
    private JTextField txtEstudantesGraduacao;
    private JTextField txtEstudantesPosGraduacao;
    private JTextField txtDocentes;
    private JTextField txtTae;
    private JTextField txtColaboradoresExternos;
    private JRadioButton rbAdjuntoSim;
    private JRadioButton rbAdjuntoNao;
    private JPanel panelCoordenacaoAdjunta;
    private JTextField txtAdjuntoNome;
    private JTextField txtAdjuntoSiape;
    private JTextField txtAdjuntoEmail;
    private JRadioButton rbAdjuntoCargoDocente;
    private JRadioButton rbAdjuntoCargoAdmin;
    private JTextField txtAdjuntoSetor;
    private JComboBox<CampusIfes> comboAdjuntoCampus;

    // Seção VI: Público Interno
    private JTextArea txtCaracterizacaoPublicoInterno;
    private JTextField txtNumeroPublicoInterno;

    // Seção VII: Detalhamento da Ação
    private JTextArea txtResumo;
    private JTextField txtPalavrasChave;
    private JTextArea txtObjetivoGeral;
    private JTextArea txtObjetivosEspecificos;

    // Seção VIII: Fundamentação
    private JTextArea txtInfluenciaGrupos;
    private JTextArea txtMudancasPublico;
    private JTextArea txtRelacaoEnsinoPesquisa;
    private JTextArea txtProtagonismoEstudantes;
    private JTextArea txtInstalacoesEquipamentos;

    // Seção IX: Cronograma
    private PainelCronograma painelCronogramaVisual;
    private JTextArea txtObservacoes;

    // Botões
    private JButton btnGerarOdt;
    private JButton btnPreencherExemplo;
    private JButton btnLimpar;

    public TelaPrincipal() {
        setTitle("Cadastro de Extensão - IFES (Orientação Normativa CAEX 01/2020)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Tamanho lateral reduzido para caber confortavelmente em qualquer tela sem scroll lateral
        setSize(780, 820);
        setMinimumSize(new Dimension(740, 600));
        setLocationRelativeTo(null);

        inicializarComponentes();

        // Instancia os controllers
        formularioController = new FormularioController(this);
        documentoController = new DocumentoController(this, formularioController);

        configurarEventos();

        // Aplica o estado inicial de regras de desabilitação/habilitação
        formularioController.inicializarEstado();
    }

    private void inicializarComponentes() {
        JPanel painelConteudoPrincipal = new JPanel();
        painelConteudoPrincipal.setLayout(new BoxLayout(painelConteudoPrincipal, BoxLayout.Y_AXIS));
        painelConteudoPrincipal.setBorder(BorderFactory.createEmptyBorder(8, 10, 12, 10));

        // Cabeçalho
        painelConteudoPrincipal.add(criarPainelCabecalho());
        painelConteudoPrincipal.add(Box.createVerticalStrut(8));

        // 1. Dados Cadastrais (Pergunta 8 removida, 9 e 10 compactos)
        painelConteudoPrincipal.add(criarPainelDadosCadastrais());
        painelConteudoPrincipal.add(Box.createVerticalStrut(8));

        // 2. Modalidade e seções condicionais (Pergunta 15 em tabela)
        painelConteudoPrincipal.add(criarPainelModalidade());
        painelConteudoPrincipal.add(Box.createVerticalStrut(8));

        // 3. Caracterização (Grid compacto de 2 colunas)
        painelConteudoPrincipal.add(criarPainelCaracterizacao());
        painelConteudoPrincipal.add(Box.createVerticalStrut(8));

        // 4. Áreas Temáticas (ODS com limite automático de 2 opções)
        painelConteudoPrincipal.add(criarPainelAreasTematicas());
        painelConteudoPrincipal.add(Box.createVerticalStrut(8));

        // 5. Público Alvo e Parceiros
        painelConteudoPrincipal.add(criarPainelPublicoAlvo());
        painelConteudoPrincipal.add(Box.createVerticalStrut(8));

        // 6. Equipe Executora e Adjunto
        painelConteudoPrincipal.add(criarPainelEquipeExecutora());
        painelConteudoPrincipal.add(Box.createVerticalStrut(8));

        // 7. Público Interno
        painelConteudoPrincipal.add(criarPainelPublicoInterno());
        painelConteudoPrincipal.add(Box.createVerticalStrut(8));

        // 8. Detalhamento da Ação
        painelConteudoPrincipal.add(criarPainelDetalhamento());
        painelConteudoPrincipal.add(Box.createVerticalStrut(8));

        // 9. Fundamentação
        painelConteudoPrincipal.add(criarPainelFundamentacao());
        painelConteudoPrincipal.add(Box.createVerticalStrut(8));

        // 10. Cronograma
        painelConteudoPrincipal.add(criarPainelCronograma());

        // JScrollPane estritamente vertical: NÃO requer arrastar para o lado!
        JScrollPane scrollPane = new JScrollPane(painelConteudoPrincipal);
        scrollPane.getVerticalScrollBar().setUnitIncrement(22);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Barra inferior de botões (sempre visível no rodapé com alto contraste)
        JPanel painelBotoesRodape = criarPainelBotoesRodape();

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(painelBotoesRodape, BorderLayout.SOUTH);

        // Aplica restrições de caracteres e números estritos
        aplicarRestricoesCampos();
    }

    private void aplicarRestricoesCampos() {
        // Campos que só podem ter números (impede letras e caracteres em tempo real)
        ((AbstractDocument) txtSiape.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());
        ((AbstractDocument) txtPrestacaoSiape.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());
        ((AbstractDocument) txtAdjuntoSiape.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());
        ((AbstractDocument) txtNumeroPublicoExterno.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());
        ((AbstractDocument) txtNumeroPublicoInterno.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());

        ((AbstractDocument) txtEstudantesFic.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());
        ((AbstractDocument) txtEstudantesTecnico.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());
        ((AbstractDocument) txtEstudantesGraduacao.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());
        ((AbstractDocument) txtEstudantesPosGraduacao.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());
        ((AbstractDocument) txtDocentes.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());
        ((AbstractDocument) txtTae.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());
        ((AbstractDocument) txtColaboradoresExternos.getDocument()).setDocumentFilter(new FiltroSomenteNumeros());

        // Campos com limite de caracteres
        ((AbstractDocument) txtTituloAcao.getDocument()).setDocumentFilter(new FiltroLimiteCaracteres(250));
        ((AbstractDocument) txtPalavrasChave.getDocument()).setDocumentFilter(new FiltroLimiteCaracteres(100));
        ((AbstractDocument) txtResumo.getDocument()).setDocumentFilter(new FiltroLimiteCaracteres(3000));

        // Validação visual imediata ao sair do campo (FocusLost)
        txtSiape.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String val = txtSiape.getText().trim();
                if (!val.isEmpty() && val.length() < 7) {
                    txtSiape.setBackground(new Color(255, 230, 230));
                    txtSiape.setToolTipText("Atenção: O Siape deve conter pelo menos 7 dígitos.");
                } else {
                    txtSiape.setBackground(Color.WHITE);
                    txtSiape.setToolTipText(null);
                }
            }
        });

        txtEmail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String val = txtEmail.getText().trim();
                if (!val.isEmpty() && !val.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                    txtEmail.setBackground(new Color(255, 230, 230));
                    txtEmail.setToolTipText("Atenção: Insira um endereço de e-mail válido (ex: coordenador@ifes.edu.br)");
                } else {
                    txtEmail.setBackground(Color.WHITE);
                    txtEmail.setToolTipText(null);
                }
            }
        });

        txtNumeroPublicoExterno.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                int val = getNumeroPublicoExterno();
                if (val <= 0) {
                    txtNumeroPublicoExterno.setBackground(new Color(255, 230, 230));
                    txtNumeroPublicoExterno.setToolTipText("O público externo deve ser um número maior que 0.");
                } else {
                    txtNumeroPublicoExterno.setBackground(Color.WHITE);
                    txtNumeroPublicoExterno.setToolTipText(null);
                }
            }
        });
    }

    private JPanel criarPainelCabecalho() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(245, 248, 252));
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 230), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel lblInst = new JLabel("INSTITUTO FEDERAL DO ESPÍRITO SANTO - IFES | PRÓ-REITORIA DE EXTENSÃO");
        lblInst.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblInst.setForeground(new Color(0, 70, 130));

        JLabel lblTitulo = new JLabel("CADASTRO DE EXTENSÃO (ORIENTAÇÃO NORMATIVA CAEX 01/2020)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(20, 40, 70));

        JLabel lblSub = new JLabel("Todas as opções integradas. Conforme as decisões forem tomadas, os campos não aplicáveis ficam cinzas.");
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSub.setForeground(new Color(90, 90, 90));

        p.add(lblInst);
        p.add(Box.createVerticalStrut(2));
        p.add(lblTitulo);
        p.add(Box.createVerticalStrut(3));
        p.add(lblSub);

        return p;
    }

    private JPanel criarPainelDadosCadastrais() {
        JPanel p = criarPainelComBorda("I. DADOS CADASTRAIS (Perguntas 1 a 11)");
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = criarGbcBase();

        // 1. Titulo
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        p.add(new JLabel("1. Título da ação: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.gridwidth = 3;
        txtTituloAcao = new JTextField();
        txtTituloAcao.setToolTipText("Máximo de 250 caracteres");
        p.add(txtTituloAcao, gbc);

        // 2. Nome coordenador
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0;
        p.add(new JLabel("2. Nome coordenador: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtNomeCoordenador = new JTextField();
        p.add(txtNomeCoordenador, gbc);

        // 3. Siape
        gbc.gridx = 2; gbc.weightx = 0;
        p.add(new JLabel("3. Siape: *"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5;
        txtSiape = new JTextField();
        txtSiape.setToolTipText("Pelo menos 7 dígitos (somente números)");
        p.add(txtSiape, gbc);

        // 4. Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        p.add(new JLabel("4. Seu E-mail: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtEmail = new JTextField();
        p.add(txtEmail, gbc);

        // 5. Cargo
        gbc.gridx = 2; gbc.weightx = 0;
        p.add(new JLabel("5. Cargo: *"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5;
        JPanel pCargo = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        rbCargoDocente = new JRadioButton("Docente", true);
        rbCargoAdmin = new JRadioButton("Administrativo");
        ButtonGroup bgCargo = new ButtonGroup();
        bgCargo.add(rbCargoDocente);
        bgCargo.add(rbCargoAdmin);
        pCargo.add(rbCargoDocente);
        pCargo.add(rbCargoAdmin);
        p.add(pCargo, gbc);

        // 6. Setor
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        p.add(new JLabel("6. Setor: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtSetor = new JTextField();
        p.add(txtSetor, gbc);

        // 7. Campus
        gbc.gridx = 2; gbc.weightx = 0;
        p.add(new JLabel("7. Campus: *"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5;
        comboCampus = new JComboBox<>(CampusIfes.values());
        comboCampus.setSelectedItem(CampusIfes.VITORIA);
        p.add(comboCampus, gbc);

        // 9 e 10. Vigência (Campos compactos, Pergunta 8 removida)
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 0;
        p.add(new JLabel("9. Início vigência: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        JPanel pVigenciaIni = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        txtInicioVigencia = new JTextField(10);
        txtInicioVigencia.setToolTipText("Formato: DD/MM/AAAA");
        pVigenciaIni.add(txtInicioVigencia);
        JLabel lblDica1 = new JLabel("(DD/MM/AAAA)");
        lblDica1.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblDica1.setForeground(Color.GRAY);
        pVigenciaIni.add(lblDica1);
        p.add(pVigenciaIni, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        p.add(new JLabel("10. Fim vigência: *"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5;
        JPanel pVigenciaFim = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        txtFimVigencia = new JTextField(10);
        txtFimVigencia.setToolTipText("Formato: DD/MM/AAAA");
        pVigenciaFim.add(txtFimVigencia);
        JLabel lblDica2 = new JLabel("(DD/MM/AAAA)");
        lblDica2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblDica2.setForeground(Color.GRAY);
        pVigenciaFim.add(lblDica2);
        p.add(pVigenciaFim, gbc);

        // 11. Proposta inovadora
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        p.add(new JLabel("11. Método/negócio inovador?: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.gridwidth = 3;
        JPanel pInov = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        rbInovacaoSim = new JRadioButton("Sim");
        rbInovacaoNao = new JRadioButton("Não", true);
        rbInovacaoNaoSei = new JRadioButton("Não sei informar");
        ButtonGroup bgInov = new ButtonGroup();
        bgInov.add(rbInovacaoSim);
        bgInov.add(rbInovacaoNao);
        bgInov.add(rbInovacaoNaoSei);
        pInov.add(rbInovacaoSim);
        pInov.add(rbInovacaoNao);
        pInov.add(rbInovacaoNaoSei);
        p.add(pInov, gbc);

        return p;
    }

    private JPanel criarPainelModalidade() {
        JPanel container = criarPainelComBorda("MODALIDADE DA AÇÃO (Pergunta 12 e Seções Específicas)");
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Linha seletora da Modalidade
        JPanel pEscolha = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JLabel lblMod = new JLabel("12. Modalidade da Ação: * ");
        lblMod.setFont(new Font("Segoe UI", Font.BOLD, 12));
        comboModalidade = new JComboBox<>(ModalidadeAcao.values());
        comboModalidade.setPreferredSize(new Dimension(260, 26));
        pEscolha.add(lblMod);
        pEscolha.add(comboModalidade);
        container.add(pEscolha);

        // A. PROGRAMA EM REDE
        panelProgramaRede = criarPainelComBorda("SEÇÃO ESPECÍFICA: PROGRAMA EM REDE");
        panelProgramaRede.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JLabel lblRede = new JLabel("14. É nova proposição de Programa em Rede vinculado à PROEX? * ");
        rbRedeSim = new JRadioButton("Sim");
        rbRedeNao = new JRadioButton("Não", true);
        ButtonGroup bgRede = new ButtonGroup();
        bgRede.add(rbRedeSim);
        bgRede.add(rbRedeNao);
        panelProgramaRede.add(lblRede);
        panelProgramaRede.add(rbRedeSim);
        panelProgramaRede.add(rbRedeNao);
        container.add(panelProgramaRede);

        // B. PROGRAMA MULTICAMPI (3 colunas para caber perfeitamente na largura)
        panelProgramaMulticampi = criarPainelComBorda("SEÇÃO ESPECÍFICA: PROGRAMA MULTICAMPI");
        panelProgramaMulticampi.setLayout(new BorderLayout(4, 4));
        JLabel lblMulti = new JLabel("13. Unidades onde a ação está sendo executada (Marque todas as participantes): *");
        panelProgramaMulticampi.add(lblMulti, BorderLayout.NORTH);

        JPanel pCampiGrid = new JPanel(new GridLayout(0, 3, 6, 2));
        for (CampusIfes c : CampusIfes.values()) {
            JCheckBox chk = new JCheckBox(c.getNome());
            checkCampiMulticampi.put(c, chk);
            pCampiGrid.add(chk);
        }
        panelProgramaMulticampi.add(pCampiGrid, BorderLayout.CENTER);
        container.add(panelProgramaMulticampi);

        // C. EVENTO (Tabela para número grande de linhas)
        panelEvento = criarPainelComBorda("SEÇÃO ESPECÍFICA: EVENTO");
        panelEvento.setLayout(new BorderLayout(4, 4));
        JLabel lblEv = new JLabel("15. Programação do evento (atividades, data, horário, local e responsáveis): *");
        lblEv.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEv.setForeground(new Color(20, 60, 120));
        panelEvento.add(lblEv, BorderLayout.NORTH);

        painelProgramacaoEvento = new PainelProgramacaoEvento();
        panelEvento.add(painelProgramacaoEvento, BorderLayout.CENTER);
        container.add(panelEvento);

        // D. PRESTAÇÃO DE SERVIÇO
        panelPrestacaoServico = criarPainelComBorda("SEÇÃO ESPECÍFICA: PRESTAÇÃO DE SERVIÇO");
        panelPrestacaoServico.setLayout(new GridBagLayout());
        GridBagConstraints gbc = criarGbcBase();

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelPrestacaoServico.add(new JLabel("16. Responsável técnico: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtPrestacaoNome = new JTextField();
        panelPrestacaoServico.add(txtPrestacaoNome, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelPrestacaoServico.add(new JLabel("17. Registro (CREA/CRM/etc):"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5;
        txtPrestacaoRegistro = new JTextField();
        panelPrestacaoServico.add(txtPrestacaoRegistro, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelPrestacaoServico.add(new JLabel("18. Siape:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtPrestacaoSiape = new JTextField();
        panelPrestacaoServico.add(txtPrestacaoSiape, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelPrestacaoServico.add(new JLabel("19. E-mail: *"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5;
        txtPrestacaoEmail = new JTextField();
        panelPrestacaoServico.add(txtPrestacaoEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelPrestacaoServico.add(new JLabel("20. Descrição técnica: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.gridwidth = 3;
        txtPrestacaoDescricao = new JTextArea(2, 30);
        txtPrestacaoDescricao.setLineWrap(true);
        txtPrestacaoDescricao.setWrapStyleWord(true);
        panelPrestacaoServico.add(new JScrollPane(txtPrestacaoDescricao), gbc);

        container.add(panelPrestacaoServico);

        return container;
    }

    private JPanel criarPainelCaracterizacao() {
        JPanel p = criarPainelComBorda("II. CARACTERIZAÇÃO (Perguntas 21 a 24)");
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        // 21. Curricular
        JPanel pCurric = new JPanel(new BorderLayout(4, 4));
        chkNaoPossuiCurricular = new JCheckBox("Não possui atividades curriculares em curso regular.");
        JLabel lblCurric = new JLabel("21. Cursos regulares com atividades curriculares integradas:");
        txtCursosCurriculares = new JTextField("Ex: Análise e Desenvolvimento de Sistemas, Engenharia Elétrica");
        pCurric.add(chkNaoPossuiCurricular, BorderLayout.NORTH);
        JPanel pCurricSub = new JPanel(new BorderLayout(4, 2));
        pCurricSub.add(lblCurric, BorderLayout.NORTH);
        pCurricSub.add(txtCursosCurriculares, BorderLayout.CENTER);
        pCurric.add(pCurricSub, BorderLayout.CENTER);
        p.add(pCurric);
        p.add(Box.createVerticalStrut(6));

        // 22. Fomento (2 colunas para nunca quebrar ou expandir a tela para o lado)
        JPanel pFom = new JPanel(new BorderLayout(4, 4));
        pFom.add(new JLabel("22. Assinale o fomento da ação: *"), BorderLayout.NORTH);
        JPanel pFomGrid = new JPanel(new GridLayout(0, 2, 8, 2));
        String[] opcoesFomento = {
            "Não possui", "Ifes - PAEx", "Ifes - PAIn", "Ifes - outro", 
            "Fapes", "CNPq", "Finep", "Petrobras", 
            "Governo estadual", "Governo municipal", "Empresa privada"
        };
        for (String op : opcoesFomento) {
            JCheckBox chk = new JCheckBox(op);
            chkFomentos.add(chk);
            pFomGrid.add(chk);
        }
        JPanel pFomOutro = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pFomOutro.add(new JLabel("Outra:"));
        txtFomentoOutro = new JTextField(15);
        pFomOutro.add(txtFomentoOutro);
        pFomGrid.add(pFomOutro);

        pFom.add(pFomGrid, BorderLayout.CENTER);
        p.add(pFom);
        p.add(Box.createVerticalStrut(6));

        // 23. Ação mais abrangente
        JPanel pAbr = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        pAbr.add(new JLabel("23. Ação institucional mais abrangente: *"));
        comboAcaoVinculada = new JComboBox<>(AcaoVinculadaTipo.values());
        comboAcaoVinculada.setSelectedItem(AcaoVinculadaTipo.NAO_VINCULADA);
        pAbr.add(comboAcaoVinculada);

        pAbr.add(new JLabel("Se outra:"));
        txtAcaoVinculadaOutra = new JTextField(12);
        txtAcaoVinculadaOutra.setEnabled(false);
        pAbr.add(txtAcaoVinculadaOutra);
        p.add(pAbr);

        // Painel condicional da Ação de Extensão Vinculada
        panelAcaoVinculada = criarPainelComBorda("AÇÃO DE EXTENSÃO VINCULADA");
        panelAcaoVinculada.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panelAcaoVinculada.add(new JLabel("24. Processo SIPAC da ação vinculada: *"));
        txtNumeroProcessoSipac = new JTextField(22);
        panelAcaoVinculada.add(txtNumeroProcessoSipac);
        p.add(panelAcaoVinculada);

        return p;
    }

    private JPanel criarPainelAreasTematicas() {
        JPanel p = criarPainelComBorda("ÁREAS TEMÁTICAS E ODS (Perguntas 25 a 27)");
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JPanel pCombos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        pCombos.add(new JLabel("25. Área principal: *"));
        comboAreaPrincipal = new JComboBox<>(AreaTematica.values());
        pCombos.add(comboAreaPrincipal);

        pCombos.add(new JLabel("26. Área secundária:"));
        comboAreaSecundaria = new JComboBox<>();
        comboAreaSecundaria.addItem("Nenhuma");
        for (AreaTematica at : AreaTematica.values()) {
            comboAreaSecundaria.addItem(at.getDescricao());
        }
        pCombos.add(comboAreaSecundaria);
        p.add(pCombos);

        p.add(Box.createVerticalStrut(4));
        JLabel lblOds = new JLabel("27. Objetivos do Desenvolvimento Sustentável (ODS) - Selecione no máximo 2 opções: *");
        lblOds.setFont(new Font("Segoe UI", Font.BOLD, 11));
        p.add(lblOds);

        // 2 colunas para ajuste lateral perfeito
        JPanel pOdsGrid = new JPanel(new GridLayout(0, 2, 6, 2));
        String[] listaOds = {
            "1. Erradicação da Pobreza", "2. Fome Zero e Agric. Sustentável", "3. Saúde e Bem-Estar",
            "4. Educação de Qualidade", "5. Igualdade de Gênero", "6. Água Potável e Saneamento",
            "7. Energia Acessível e Limpa", "8. Trabalho Decente", "9. Indústria, Inovação e Infra.",
            "10. Redução das Desigualdades", "11. Cidades Sustentáveis", "12. Consumo Sustentável",
            "13. Ação Climática", "14. Vida Aquática", "15. Vida Terrestre",
            "16. Paz, Justiça e Instituições", "17. Parcerias e Meios"
        };
        for (String ods : listaOds) {
            JCheckBox chk = new JCheckBox(ods);
            chk.addActionListener(e -> {
                if (chk.isSelected() && getOdsSelecionados().size() > 2) {
                    chk.setSelected(false);
                    JOptionPane.showMessageDialog(this, 
                        "A Orientação Normativa CAEX 01/2020 permite a seleção de no máximo 2 ODS.", 
                        "Limite de ODS Atingido", 
                        JOptionPane.WARNING_MESSAGE);
                }
            });
            checkOds.add(chk);
            pOdsGrid.add(chk);
        }
        p.add(pOdsGrid);

        return p;
    }

    private JPanel criarPainelPublicoAlvo() {
        JPanel p = criarPainelComBorda("PÚBLICO ALVO E ORGANIZAÇÕES PARTICIPANTES (Perguntas 28 a 31)");
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = criarGbcBase();

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        p.add(new JLabel("28. Caracterização do público alvo: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCaracterizacaoPublicoAlvo = new JTextArea(2, 30);
        txtCaracterizacaoPublicoAlvo.setLineWrap(true);
        txtCaracterizacaoPublicoAlvo.setWrapStyleWord(true);
        p.add(new JScrollPane(txtCaracterizacaoPublicoAlvo), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        p.add(new JLabel("29. Total estimado público EXTERNO: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNumeroPublicoExterno = new JTextField("0");
        txtNumeroPublicoExterno.setToolTipText("Somente números maiores que 0");
        p.add(txtNumeroPublicoExterno, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        p.add(new JLabel("30. Organizações parceiras e participação:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtOrganizacoesParceiras = new JTextArea(2, 30);
        txtOrganizacoesParceiras.setLineWrap(true);
        txtOrganizacoesParceiras.setWrapStyleWord(true);
        p.add(new JScrollPane(txtOrganizacoesParceiras), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        p.add(new JLabel("31. O parceiro vai aportar recursos?:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboParceiroRecursos = new JComboBox<>(new String[]{"Não se aplica", "Sim", "Não", "Misto / Parcial"});
        p.add(comboParceiroRecursos, gbc);

        return p;
    }

    private JPanel criarPainelEquipeExecutora() {
        JPanel container = criarPainelComBorda("EQUIPE EXECUTORA E COORDENAÇÃO ADJUNTA (Perguntas 32 a 45)");
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Quantitativos numéricos da equipe
        JPanel pQuant = new JPanel(new GridLayout(2, 4, 6, 3));
        txtEstudantesFic = new JTextField("0");
        txtEstudantesTecnico = new JTextField("0");
        txtEstudantesGraduacao = new JTextField("0");
        txtEstudantesPosGraduacao = new JTextField("0");
        txtDocentes = new JTextField("1");
        txtTae = new JTextField("0");
        txtColaboradoresExternos = new JTextField("0");

        pQuant.add(criarItemComLabel("32. Estudantes FIC:", txtEstudantesFic));
        pQuant.add(criarItemComLabel("33. Estudantes Técnico:", txtEstudantesTecnico));
        pQuant.add(criarItemComLabel("34. Estudantes Graduação:", txtEstudantesGraduacao));
        pQuant.add(criarItemComLabel("35. Estudantes Pós:", txtEstudantesPosGraduacao));
        pQuant.add(criarItemComLabel("36. Docentes:", txtDocentes));
        pQuant.add(criarItemComLabel("37. TAEs:", txtTae));
        pQuant.add(criarItemComLabel("38. Colaboradores Externos:", txtColaboradoresExternos));
        container.add(pQuant);

        container.add(Box.createVerticalStrut(6));

        // Pergunta 39: Há coordenador adjunto?
        JPanel pAdjDecisao = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));
        JLabel lblAdj = new JLabel("39. Há coordenador adjunto? * ");
        lblAdj.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rbAdjuntoSim = new JRadioButton("Sim");
        rbAdjuntoNao = new JRadioButton("Não", true);
        ButtonGroup bgAdj = new ButtonGroup();
        bgAdj.add(rbAdjuntoSim);
        bgAdj.add(rbAdjuntoNao);
        pAdjDecisao.add(lblAdj);
        pAdjDecisao.add(rbAdjuntoSim);
        pAdjDecisao.add(rbAdjuntoNao);
        container.add(pAdjDecisao);

        // Painel condicional da Coordenação Adjunta
        panelCoordenacaoAdjunta = criarPainelComBorda("DADOS DO COORDENADOR ADJUNTO");
        panelCoordenacaoAdjunta.setLayout(new GridBagLayout());
        GridBagConstraints gbc = criarGbcBase();

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelCoordenacaoAdjunta.add(new JLabel("40. Nome adjunto: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtAdjuntoNome = new JTextField();
        panelCoordenacaoAdjunta.add(txtAdjuntoNome, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelCoordenacaoAdjunta.add(new JLabel("41. Siape: *"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5;
        txtAdjuntoSiape = new JTextField();
        panelCoordenacaoAdjunta.add(txtAdjuntoSiape, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelCoordenacaoAdjunta.add(new JLabel("42. E-mail: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtAdjuntoEmail = new JTextField();
        panelCoordenacaoAdjunta.add(txtAdjuntoEmail, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelCoordenacaoAdjunta.add(new JLabel("43. Cargo: *"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5;
        JPanel pAdjCargo = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        rbAdjuntoCargoDocente = new JRadioButton("Docente", true);
        rbAdjuntoCargoAdmin = new JRadioButton("Administrativo");
        ButtonGroup bgAdjCargo = new ButtonGroup();
        bgAdjCargo.add(rbAdjuntoCargoDocente);
        bgAdjCargo.add(rbAdjuntoCargoAdmin);
        pAdjCargo.add(rbAdjuntoCargoDocente);
        pAdjCargo.add(rbAdjuntoCargoAdmin);
        panelCoordenacaoAdjunta.add(pAdjCargo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelCoordenacaoAdjunta.add(new JLabel("44. Setor: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtAdjuntoSetor = new JTextField();
        panelCoordenacaoAdjunta.add(txtAdjuntoSetor, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelCoordenacaoAdjunta.add(new JLabel("45. Campus: *"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5;
        comboAdjuntoCampus = new JComboBox<>(CampusIfes.values());
        panelCoordenacaoAdjunta.add(comboAdjuntoCampus, gbc);

        container.add(panelCoordenacaoAdjunta);

        return container;
    }

    private JPanel criarPainelPublicoInterno() {
        JPanel p = criarPainelComBorda("PÚBLICO INTERNO (Perguntas 46 e 47)");
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = criarGbcBase();

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        p.add(new JLabel("46. Caracterização do público interno: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCaracterizacaoPublicoInterno = new JTextArea(2, 30);
        txtCaracterizacaoPublicoInterno.setLineWrap(true);
        txtCaracterizacaoPublicoInterno.setWrapStyleWord(true);
        p.add(new JScrollPane(txtCaracterizacaoPublicoInterno), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        p.add(new JLabel("47. Total estimado público INTERNO: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNumeroPublicoInterno = new JTextField("0");
        txtNumeroPublicoInterno.setToolTipText("Somente números");
        p.add(txtNumeroPublicoInterno, gbc);

        return p;
    }

    private JPanel criarPainelDetalhamento() {
        JPanel p = criarPainelComBorda("DETALHAMENTO DA AÇÃO (Perguntas 48 a 51)");
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = criarGbcBase();

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        p.add(new JLabel("48. Resumo (máx 3000 carac.): *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtResumo = new JTextArea(3, 30);
        txtResumo.setLineWrap(true);
        txtResumo.setWrapStyleWord(true);
        p.add(new JScrollPane(txtResumo), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        p.add(new JLabel("49. Palavras-chave: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPalavrasChave = new JTextField();
        txtPalavrasChave.setToolTipText("Máximo de 100 caracteres");
        p.add(txtPalavrasChave, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        p.add(new JLabel("50. Objetivo Geral: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtObjetivoGeral = new JTextArea(2, 30);
        txtObjetivoGeral.setLineWrap(true);
        txtObjetivoGeral.setWrapStyleWord(true);
        p.add(new JScrollPane(txtObjetivoGeral), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        p.add(new JLabel("51. Objetivos específicos: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtObjetivosEspecificos = new JTextArea(3, 30);
        txtObjetivosEspecificos.setLineWrap(true);
        txtObjetivosEspecificos.setWrapStyleWord(true);
        p.add(new JScrollPane(txtObjetivosEspecificos), gbc);

        return p;
    }

    private JPanel criarPainelFundamentacao() {
        JPanel p = criarPainelComBorda("FUNDAMENTAÇÃO - DIRETRIZES DA EXTENSÃO (Perguntas 52 a 56)");
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = criarGbcBase();

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        p.add(new JLabel("52. Influência de grupos sociais externos: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtInfluenciaGrupos = new JTextArea(2, 30);
        txtInfluenciaGrupos.setLineWrap(true);
        txtInfluenciaGrupos.setWrapStyleWord(true);
        p.add(new JScrollPane(txtInfluenciaGrupos), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        p.add(new JLabel("53. Mudanças produzidas no público: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMudancasPublico = new JTextArea(2, 30);
        txtMudancasPublico.setLineWrap(true);
        txtMudancasPublico.setWrapStyleWord(true);
        p.add(new JScrollPane(txtMudancasPublico), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        p.add(new JLabel("54. Relação com ensino e/ou pesquisa: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtRelacaoEnsinoPesquisa = new JTextArea(2, 30);
        txtRelacaoEnsinoPesquisa.setLineWrap(true);
        txtRelacaoEnsinoPesquisa.setWrapStyleWord(true);
        p.add(new JScrollPane(txtRelacaoEnsinoPesquisa), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        p.add(new JLabel("55. Protagonismo de estudantes: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtProtagonismoEstudantes = new JTextArea(2, 30);
        txtProtagonismoEstudantes.setLineWrap(true);
        txtProtagonismoEstudantes.setWrapStyleWord(true);
        p.add(new JScrollPane(txtProtagonismoEstudantes), gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        p.add(new JLabel("56. Instalações e equipamentos:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtInstalacoesEquipamentos = new JTextArea(2, 30);
        txtInstalacoesEquipamentos.setLineWrap(true);
        txtInstalacoesEquipamentos.setWrapStyleWord(true);
        p.add(new JScrollPane(txtInstalacoesEquipamentos), gbc);

        return p;
    }

    private JPanel criarPainelCronograma() {
        JPanel p = criarPainelComBorda("CRONOGRAMA E OBSERVAÇÕES (Perguntas 57 e 58)");
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel lblCron = new JLabel("57. Atividades e Cronograma de Execução (Calendário / Gráfico de Gantt): *");
        lblCron.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCron.setForeground(new Color(20, 60, 120));
        p.add(lblCron);
        p.add(Box.createVerticalStrut(4));

        painelCronogramaVisual = new PainelCronograma();
        p.add(painelCronogramaVisual);
        p.add(Box.createVerticalStrut(8));

        JPanel pObs = new JPanel(new BorderLayout(4, 3));
        JLabel lblObs = new JLabel("58. Observações complementares:");
        lblObs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtObservacoes = new JTextArea(2, 30);
        txtObservacoes.setLineWrap(true);
        txtObservacoes.setWrapStyleWord(true);
        pObs.add(lblObs, BorderLayout.NORTH);
        pObs.add(new JScrollPane(txtObservacoes), BorderLayout.CENTER);
        p.add(pObs);

        return p;
    }

    private JPanel criarPainelBotoesRodape() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        p.setBackground(new Color(236, 241, 248));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(195, 208, 225)));

        btnPreencherExemplo = new JButton("Preencher Dados de Exemplo");
        btnPreencherExemplo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnPreencherExemplo.setToolTipText("Preenche todos os campos com dados de teste válidos.");

        btnLimpar = new JButton("Limpar Formulário");
        btnLimpar.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Botão com excelente contraste: texto escuro nítido sobre fundo azul celeste claro delimitado
        btnGerarOdt = new JButton("💾  GERAR ARQUIVO ODT (.odt)");
        btnGerarOdt.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGerarOdt.setForeground(new Color(10, 40, 80)); // Letra escura de altíssimo contraste
        btnGerarOdt.setBackground(new Color(185, 220, 255)); // Fundo azul celeste claro bem nítido
        btnGerarOdt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 80, 160), 2, true),
            BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        btnGerarOdt.setFocusPainted(false);
        btnGerarOdt.setCursor(new Cursor(Cursor.HAND_CURSOR));

        p.add(btnPreencherExemplo);
        p.add(btnLimpar);
        p.add(btnGerarOdt);

        return p;
    }

    private void configurarEventos() {
        // Evento da Modalidade (Decisão 1)
        comboModalidade.addActionListener(e -> formularioController.atualizarEstadoModalidade());

        // Evento da Ação Mais Abrangente (Decisão 2)
        comboAcaoVinculada.addActionListener(e -> formularioController.atualizarEstadoAcaoVinculada());

        // Evento do Coordenador Adjunto (Decisão 3)
        rbAdjuntoSim.addActionListener(e -> formularioController.atualizarEstadoCoordenadorAdjunto());
        rbAdjuntoNao.addActionListener(e -> formularioController.atualizarEstadoCoordenadorAdjunto());

        // Evento da Atividade Curricular
        chkNaoPossuiCurricular.addActionListener(e -> formularioController.atualizarEstadoAtividadesCurriculares());

        // Botões de Ação
        btnGerarOdt.addActionListener(e -> documentoController.processarGeracaoOdt());
        btnPreencherExemplo.addActionListener(e -> formularioController.preencherDadosExemplo());
        btnLimpar.addActionListener(e -> {
            int opt = JOptionPane.showConfirmDialog(this, "Deseja realmente limpar todos os campos?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                formularioController.limparFormulario();
            }
        });
    }

    // Helpers de interface
    private JPanel criarPainelComBorda(String titulo) {
        JPanel p = new JPanel();
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 195, 215), 1),
            titulo,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(20, 60, 120)
        );
        p.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(4, 8, 6, 8)));
        return p;
    }

    private GridBagConstraints criarGbcBase() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private JPanel criarItemComLabel(String labelText, JTextField tf) {
        JPanel p = new JPanel(new BorderLayout(3, 1));
        p.add(new JLabel(labelText), BorderLayout.NORTH);
        p.add(tf, BorderLayout.CENTER);
        return p;
    }

    // Getters para os componentes de controle
    public ModalidadeAcao getModalidadeSelecionada() {
        return (ModalidadeAcao) comboModalidade.getSelectedItem();
    }

    public AcaoVinculadaTipo getAcaoVinculadaSelecionada() {
        return (AcaoVinculadaTipo) comboAcaoVinculada.getSelectedItem();
    }

    public boolean isHaCoordenadorAdjuntoSelecionado() {
        return rbAdjuntoSim.isSelected();
    }

    public boolean isNaoPossuiCurricularMarcado() {
        return chkNaoPossuiCurricular.isSelected();
    }

    public boolean isRedeConfirmado() {
        return rbRedeSim.isSelected();
    }

    public List<CampusIfes> getCampiMulticampiSelecionados() {
        List<CampusIfes> selecionados = new ArrayList<>();
        for (Map.Entry<CampusIfes, JCheckBox> entry : checkCampiMulticampi.entrySet()) {
            if (entry.getValue().isSelected()) {
                selecionados.add(entry.getKey());
            }
        }
        return selecionados;
    }

    public List<String> getFomentosSelecionados() {
        List<String> list = new ArrayList<>();
        for (JCheckBox chk : chkFomentos) {
            if (chk.isSelected()) {
                list.add(chk.getText());
            }
        }
        return list;
    }

    public List<String> getOdsSelecionados() {
        List<String> list = new ArrayList<>();
        for (JCheckBox chk : checkOds) {
            if (chk.isSelected()) {
                list.add(chk.getText());
            }
        }
        return list;
    }

    public TipoCargo getCargoSelecionado() {
        return rbCargoDocente.isSelected() ? TipoCargo.DOCENTE : TipoCargo.ADMINISTRATIVO;
    }

    public CampusIfes getCampusSelecionado() {
        return (CampusIfes) comboCampus.getSelectedItem();
    }

    public InovacaoResposta getPropostaInovadoraSelecionada() {
        if (rbInovacaoSim.isSelected()) return InovacaoResposta.SIM;
        if (rbInovacaoNao.isSelected()) return InovacaoResposta.NAO;
        return InovacaoResposta.NAO_SEI;
    }

    public AreaTematica getAreaPrincipalSelecionada() {
        return (AreaTematica) comboAreaPrincipal.getSelectedItem();
    }

    public AreaTematica getAreaSecundariaSelecionada() {
        int idx = comboAreaSecundaria.getSelectedIndex();
        if (idx <= 0) return null;
        return AreaTematica.values()[idx - 1];
    }

    public TipoCargo getAdjuntoCargoSelecionado() {
        return rbAdjuntoCargoDocente.isSelected() ? TipoCargo.DOCENTE : TipoCargo.ADMINISTRATIVO;
    }

    public CampusIfes getAdjuntoCampusSelecionado() {
        return (CampusIfes) comboAdjuntoCampus.getSelectedItem();
    }

    public int getNumeroPublicoExterno() {
        return getNumeroInteiro(txtNumeroPublicoExterno.getText());
    }

    public int getNumeroPublicoInterno() {
        return getNumeroInteiro(txtNumeroPublicoInterno.getText());
    }

    public int getNumeroInteiro(String str) {
        if (str == null || str.trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Getters de painéis e componentes para o Controller
    public JComboBox<ModalidadeAcao> getComboModalidade() { return comboModalidade; }
    public JPanel getPanelProgramaRede() { return panelProgramaRede; }
    public JPanel getPanelProgramaMulticampi() { return panelProgramaMulticampi; }
    public JPanel getPanelEvento() { return panelEvento; }
    public PainelProgramacaoEvento getPainelProgramacaoEvento() { return painelProgramacaoEvento; }
    public JPanel getPanelPrestacaoServico() { return panelPrestacaoServico; }
    public JPanel getPanelAcaoVinculada() { return panelAcaoVinculada; }
    public JPanel getPanelCoordenacaoAdjunta() { return panelCoordenacaoAdjunta; }

    public JTextField getTxtTituloAcao() { return txtTituloAcao; }
    public JTextField getTxtNomeCoordenador() { return txtNomeCoordenador; }
    public JTextField getTxtSiape() { return txtSiape; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JTextField getTxtSetor() { return txtSetor; }
    public JTextField getTxtInicioVigencia() { return txtInicioVigencia; }
    public JTextField getTxtFimVigencia() { return txtFimVigencia; }

    public JTextField getTxtPrestacaoNome() { return txtPrestacaoNome; }
    public JTextField getTxtPrestacaoRegistro() { return txtPrestacaoRegistro; }
    public JTextField getTxtPrestacaoSiape() { return txtPrestacaoSiape; }
    public JTextField getTxtPrestacaoEmail() { return txtPrestacaoEmail; }
    public JTextArea getTxtPrestacaoDescricao() { return txtPrestacaoDescricao; }

    public JTextField getTxtCursosCurriculares() { return txtCursosCurriculares; }
    public JTextField getTxtFomentoOutro() { return txtFomentoOutro; }
    public JTextField getTxtAcaoVinculadaOutra() { return txtAcaoVinculadaOutra; }
    public JTextField getTxtNumeroProcessoSipac() { return txtNumeroProcessoSipac; }

    public JTextArea getTxtCaracterizacaoPublicoAlvo() { return txtCaracterizacaoPublicoAlvo; }
    public JTextField getTxtNumeroPublicoExternoField() { return txtNumeroPublicoExterno; }
    public JTextArea getTxtOrganizacoesParceiras() { return txtOrganizacoesParceiras; }
    public String getTxtParceiroRecursos() { return (String) comboParceiroRecursos.getSelectedItem(); }

    public JTextField getTxtEstudantesFic() { return txtEstudantesFic; }
    public JTextField getTxtEstudantesTecnico() { return txtEstudantesTecnico; }
    public JTextField getTxtEstudantesGraduacao() { return txtEstudantesGraduacao; }
    public JTextField getTxtEstudantesPosGraduacao() { return txtEstudantesPosGraduacao; }
    public JTextField getTxtDocentes() { return txtDocentes; }
    public JTextField getTxtTae() { return txtTae; }
    public JTextField getTxtColaboradoresExternos() { return txtColaboradoresExternos; }

    public JTextField getTxtAdjuntoNome() { return txtAdjuntoNome; }
    public JTextField getTxtAdjuntoSiape() { return txtAdjuntoSiape; }
    public JTextField getTxtAdjuntoEmail() { return txtAdjuntoEmail; }
    public JTextField getTxtAdjuntoSetor() { return txtAdjuntoSetor; }

    public JTextArea getTxtCaracterizacaoPublicoInterno() { return txtCaracterizacaoPublicoInterno; }
    public JTextField getTxtNumeroPublicoInternoField() { return txtNumeroPublicoInterno; }
    public JTextArea getTxtResumo() { return txtResumo; }
    public JTextField getTxtPalavrasChave() { return txtPalavrasChave; }
    public JTextArea getTxtObjetivoGeral() { return txtObjetivoGeral; }
    public JTextArea getTxtObjetivosEspecificos() { return txtObjetivosEspecificos; }

    public JTextArea getTxtInfluenciaGrupos() { return txtInfluenciaGrupos; }
    public JTextArea getTxtMudancasPublico() { return txtMudancasPublico; }
    public JTextArea getTxtRelacaoEnsinoPesquisa() { return txtRelacaoEnsinoPesquisa; }
    public JTextArea getTxtProtagonismoEstudantes() { return txtProtagonismoEstudantes; }
    public JTextArea getTxtInstalacoesEquipamentos() { return txtInstalacoesEquipamentos; }

    public PainelCronograma getPainelCronogramaVisual() { return painelCronogramaVisual; }
    public JTextArea getTxtObservacoes() { return txtObservacoes; }

    public void preencherExemplo() {
        txtTituloAcao.setText("Oficinas de Robótica Sustentável para Escolas Públicas");
        txtNomeCoordenador.setText("Prof. Dr. Carlos Eduardo Silva");
        txtSiape.setText("1892345");
        txtEmail.setText("carlos.silva@ifes.edu.br");
        rbCargoDocente.setSelected(true);
        txtSetor.setText("Coordenadoria de Informática");
        comboCampus.setSelectedItem(CampusIfes.SERRA);
        txtInicioVigencia.setText("01/03/2026");
        txtFimVigencia.setText("30/11/2026");
        rbInovacaoSim.setSelected(true);

        comboModalidade.setSelectedItem(ModalidadeAcao.PROJETO);

        // Preenche exemplo para a tabela de programação do evento caso troque para Evento
        List<ItemProgramacaoEvento> eventoExemplo = new ArrayList<>();
        eventoExemplo.add(new ItemProgramacaoEvento("Credenciamento e Abertura", "15/05/2026", "08:30 - 09:30", "Auditório Central", "Comissão"));
        eventoExemplo.add(new ItemProgramacaoEvento("Mesa Redonda: Sustentabilidade e Robótica", "15/05/2026", "09:30 - 11:30", "Auditório Central", "Prof. Dr. Carlos"));
        eventoExemplo.add(new ItemProgramacaoEvento("Oficina Prática de Sensores", "15/05/2026", "14:00 - 17:00", "Laboratório 102", "Estudantes Monitores"));
        painelProgramacaoEvento.setItens(eventoExemplo);

        chkNaoPossuiCurricular.setSelected(false);
        txtCursosCurriculares.setText("Bacharelado em Sistemas de Informação, Engenharia de Controle e Automação");

        if (!chkFomentos.isEmpty()) {
            chkFomentos.get(1).setSelected(true); // Ifes - PAEx
        }

        comboAcaoVinculada.setSelectedItem(AcaoVinculadaTipo.EXTENSAO);
        txtNumeroProcessoSipac.setText("23147.001234/2026-88");

        comboAreaPrincipal.setSelectedItem(AreaTematica.TECNOLOGIA_E_PRODUCAO);
        comboAreaSecundaria.setSelectedIndex(4); // Educação

        if (checkOds.size() >= 9) {
            checkOds.get(3).setSelected(true); // Educação de Qualidade
            checkOds.get(8).setSelected(true); // Indústria, Inovação
        }

        txtCaracterizacaoPublicoAlvo.setText("Estudantes do 9º ano do Ensino Fundamental e Ensino Médio de escolas públicas do entorno do campus.");
        txtNumeroPublicoExterno.setText("120");
        txtOrganizacoesParceiras.setText("Escola Estadual Professor Aristides - cessão de espaço e turmas para oficinas.");
        comboParceiroRecursos.setSelectedItem("Não");

        txtEstudantesFic.setText("0");
        txtEstudantesTecnico.setText("2");
        txtEstudantesGraduacao.setText("4");
        txtEstudantesPosGraduacao.setText("0");
        txtDocentes.setText("2");
        txtTae.setText("1");
        txtColaboradoresExternos.setText("0");

        rbAdjuntoSim.setSelected(true);
        txtAdjuntoNome.setText("Profª. Maria Oliveira");
        txtAdjuntoSiape.setText("2198765");
        txtAdjuntoEmail.setText("maria.oliveira@ifes.edu.br");
        rbAdjuntoCargoDocente.setSelected(true);
        txtAdjuntoSetor.setText("Coordenadoria de Automação");
        comboAdjuntoCampus.setSelectedItem(CampusIfes.SERRA);

        txtCaracterizacaoPublicoInterno.setText("Estudantes monitores do Ifes envolvidos no desenvolvimento e aplicação dos kits de robótica.");
        txtNumeroPublicoInterno.setText("15");

        txtResumo.setText("O projeto visa capacitar jovens de escolas públicas em conceitos introdutórios de pensamento computacional, eletrônica básica e sustentabilidade por meio da montagem de robôs seguidores de linha e automação com materiais reciclados.");
        txtPalavrasChave.setText("Robótica, Inclusão Digital, Extensão, Ensino Público, Sustentabilidade");
        txtObjetivoGeral.setText("Promover a inclusão sociodigital e despertar o interesse pelas carreiras científicas e tecnológicas em estudantes da rede pública.");
        txtObjetivosEspecificos.setText("1. Desenvolver 5 kits de robótica educacional de baixo custo.\n2. Realizar 8 oficinas práticas em 2 escolas parceiras.\n3. Capacitar os estudantes monitores do Ifes no papel extensionista.");

        txtInfluenciaGrupos.setText("A demanda foi apresentada pelas diretorias das escolas parceiras durante reunião com o setor de extensão.");
        txtMudancasPublico.setText("Aumento do interesse pelas áreas de STEM, melhoria no raciocínio lógico e aproximação com os cursos técnicos do Ifes.");
        txtRelacaoEnsinoPesquisa.setText("O projeto utiliza conhecimentos das disciplinas de Programação I, Microcontroladores e Circuitos Digitais.");
        txtProtagonismoEstudantes.setText("Os alunos do Ifes liderarão a elaboração das apostilas didáticas e a ministração prática das oficinas como tutores.");
        txtInstalacoesEquipamentos.setText("Laboratório de Robótica do Campus Serra e salas de informática das escolas parceiras.");

        List<ItemCronograma> itensExemplo = new ArrayList<>();
        itensExemplo.add(new ItemCronograma("Formalização de parcerias com escolas e divulgação do edital", 1, 2));
        itensExemplo.add(new ItemCronograma("Capacitação dos estudantes extensionistas tutores", 2, 3));
        itensExemplo.add(new ItemCronograma("Preparação e montagem dos kits didáticos de robótica", 2, 4));
        itensExemplo.add(new ItemCronograma("Aplicação prática das oficinas de robótica nas escolas parceiras", 3, 7));
        itensExemplo.add(new ItemCronograma("Mostra de Robótica, avaliação de impactos e elaboração de relatório final", 7, 8));
        painelCronogramaVisual.setItens(itensExemplo);
        txtObservacoes.setText("Ação alinhada com as diretrizes do PDI institucional.");
    }

    public void limparCampos() {
        txtTituloAcao.setText("");
        txtNomeCoordenador.setText("");
        txtSiape.setText("");
        txtEmail.setText("");
        txtSetor.setText("");
        txtInicioVigencia.setText("");
        txtFimVigencia.setText("");
        rbInovacaoNao.setSelected(true);
        comboCampus.setSelectedIndex(0);

        comboModalidade.setSelectedIndex(0);
        rbRedeNao.setSelected(true);
        for (JCheckBox chk : checkCampiMulticampi.values()) {
            chk.setSelected(false);
        }
        painelProgramacaoEvento.limpar();
        txtPrestacaoNome.setText("");
        txtPrestacaoRegistro.setText("");
        txtPrestacaoSiape.setText("");
        txtPrestacaoEmail.setText("");
        txtPrestacaoDescricao.setText("");

        chkNaoPossuiCurricular.setSelected(false);
        txtCursosCurriculares.setText("");
        for (JCheckBox chk : chkFomentos) {
            chk.setSelected(false);
        }
        txtFomentoOutro.setText("");
        comboAcaoVinculada.setSelectedItem(AcaoVinculadaTipo.NAO_VINCULADA);
        txtAcaoVinculadaOutra.setText("");
        txtNumeroProcessoSipac.setText("");

        comboAreaPrincipal.setSelectedIndex(0);
        comboAreaSecundaria.setSelectedIndex(0);
        for (JCheckBox chk : checkOds) {
            chk.setSelected(false);
        }

        txtCaracterizacaoPublicoAlvo.setText("");
        txtNumeroPublicoExterno.setText("0");
        txtOrganizacoesParceiras.setText("");
        comboParceiroRecursos.setSelectedIndex(0);

        txtEstudantesFic.setText("0");
        txtEstudantesTecnico.setText("0");
        txtEstudantesGraduacao.setText("0");
        txtEstudantesPosGraduacao.setText("0");
        txtDocentes.setText("0");
        txtTae.setText("0");
        txtColaboradoresExternos.setText("0");

        rbAdjuntoNao.setSelected(true);
        txtAdjuntoNome.setText("");
        txtAdjuntoSiape.setText("");
        txtAdjuntoEmail.setText("");
        txtAdjuntoSetor.setText("");
        comboAdjuntoCampus.setSelectedIndex(0);

        txtCaracterizacaoPublicoInterno.setText("");
        txtNumeroPublicoInterno.setText("0");

        txtResumo.setText("");
        txtPalavrasChave.setText("");
        txtObjetivoGeral.setText("");
        txtObjetivosEspecificos.setText("");

        txtInfluenciaGrupos.setText("");
        txtMudancasPublico.setText("");
        txtRelacaoEnsinoPesquisa.setText("");
        txtProtagonismoEstudantes.setText("");
        txtInstalacoesEquipamentos.setText("");

        painelCronogramaVisual.limpar();
        txtObservacoes.setText("");
    }
}
