package Controller;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import Model.Formulario;
import Model.enums.AcaoVinculadaTipo;
import Model.enums.ModalidadeAcao;
import Service.OdtService;
import View.TelaPrincipal;

public class DocumentoController {

    private final TelaPrincipal view;
    private final FormularioController formularioController;
    private final OdtService odtService;

    public DocumentoController(TelaPrincipal view, FormularioController formularioController) {
        this.view = view;
        this.formularioController = formularioController;
        this.odtService = new OdtService();
    }

    public void processarGeracaoOdt() {
        // Validação completa de todos os campos com restrição
        List<String> erros = new ArrayList<>();
        Component primeiroCampoComErro = validarFormularioCompleto(erros);

        if (!erros.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Foram identificadas inconsistências para geração do documento:\n\n");
            for (int i = 0; i < erros.size(); i++) {
                sb.append("• ").append(erros.get(i)).append("\n");
            }
            sb.append("\nPor favor, corrija os itens acima e tente novamente.");

            JOptionPane.showMessageDialog(view, sb.toString(), "Validação de Campos Obrigatórios", JOptionPane.WARNING_MESSAGE);
            if (primeiroCampoComErro != null) {
                primeiroCampoComErro.requestFocus();
            }
            return;
        }

        // Extrai todos os dados atuais do formulário
        Formulario formulario = formularioController.extrairDadosDaTela();

        // Diálogo para escolha do local de salvamento
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Formulário de Extensão (.odt)");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Documento OpenDocument (.odt)", "odt");
        fileChooser.setFileFilter(filter);

        String titulo = view.getTxtTituloAcao().getText().trim();
        String nomePadrao = "Cadastro_Extensao_" + sanitizarNomeArquivo(titulo) + ".odt";
        fileChooser.setSelectedFile(new File(nomePadrao));

        int userSelection = fileChooser.showSaveDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = fileChooser.getSelectedFile();
            if (!arquivoSelecionado.getName().toLowerCase().endsWith(".odt")) {
                arquivoSelecionado = new File(arquivoSelecionado.getParentFile(), arquivoSelecionado.getName() + ".odt");
            }

            try {
                odtService.gerarDocumentoOdt(formulario, arquivoSelecionado);

                int opcao = JOptionPane.showConfirmDialog(view,
                    "Arquivo ODT gerado com sucesso!\n\nSalvo em: " + arquivoSelecionado.getAbsolutePath() + "\n\nDeseja abrir o arquivo agora?",
                    "Sucesso",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);

                if (opcao == JOptionPane.YES_OPTION && Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(arquivoSelecionado);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view,
                    "Erro ao gerar arquivo ODT: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Component validarFormularioCompleto(List<String> erros) {
        Component primeiroErro = null;

        // 1. Título da ação
        String titulo = view.getTxtTituloAcao().getText().trim();
        if (titulo.isEmpty()) {
            erros.add("Campo 1: 'Título da ação' é obrigatório.");
            if (primeiroErro == null) primeiroErro = view.getTxtTituloAcao();
        } else if (titulo.length() > 250) {
            erros.add("Campo 1: 'Título da ação' excede 250 caracteres (atual: " + titulo.length() + ").");
            if (primeiroErro == null) primeiroErro = view.getTxtTituloAcao();
        }

        // 2. Coordenador
        String coord = view.getTxtNomeCoordenador().getText().trim();
        if (coord.isEmpty()) {
            erros.add("Campo 2: 'Nome completo do coordenador' é obrigatório.");
            if (primeiroErro == null) primeiroErro = view.getTxtNomeCoordenador();
        }

        // 3. Siape
        String siape = view.getTxtSiape().getText().trim();
        if (siape.isEmpty()) {
            erros.add("Campo 3: 'Siape' é obrigatório.");
            if (primeiroErro == null) primeiroErro = view.getTxtSiape();
        } else if (!siape.matches("\\d+")) {
            erros.add("Campo 3: 'Siape' deve conter apenas números.");
            if (primeiroErro == null) primeiroErro = view.getTxtSiape();
        } else if (siape.length() < 7) {
            erros.add("Campo 3: 'Siape' deve conter pelo menos 7 dígitos (atual: " + siape.length() + ").");
            if (primeiroErro == null) primeiroErro = view.getTxtSiape();
        }

        // 4. E-mail
        String email = view.getTxtEmail().getText().trim();
        if (email.isEmpty()) {
            erros.add("Campo 4: 'Seu E-mail' é obrigatório.");
            if (primeiroErro == null) primeiroErro = view.getTxtEmail();
        } else if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            erros.add("Campo 4: 'Seu E-mail' não possui um formato de e-mail válido.");
            if (primeiroErro == null) primeiroErro = view.getTxtEmail();
        }

        // 6. Setor
        if (view.getTxtSetor().getText().trim().isEmpty()) {
            erros.add("Campo 6: 'Setor' é obrigatório.");
            if (primeiroErro == null) primeiroErro = view.getTxtSetor();
        }

        // 9 e 10. Vigência
        if (view.getTxtInicioVigencia().getText().trim().isEmpty()) {
            erros.add("Campo 9: 'Início do período de vigência' é obrigatório.");
            if (primeiroErro == null) primeiroErro = view.getTxtInicioVigencia();
        }
        if (view.getTxtFimVigencia().getText().trim().isEmpty()) {
            erros.add("Campo 10: 'Fim do período de vigência' é obrigatório.");
            if (primeiroErro == null) primeiroErro = view.getTxtFimVigencia();
        }

        // 12. Modalidade da Ação
        ModalidadeAcao mod = view.getModalidadeSelecionada();
        if (mod == null || mod == ModalidadeAcao.SELECIONE) {
            erros.add("Campo 12: 'Modalidade da Ação' deve ser selecionada.");
            if (primeiroErro == null) primeiroErro = view.getComboModalidade();
        } else {
            if (mod == ModalidadeAcao.PROGRAMA_MULTICAMPI) {
                if (view.getCampiMulticampiSelecionados().isEmpty()) {
                    erros.add("Campo 13: Selecione pelo menos 1 unidade/campus executora para Programa Multicampi.");
                }
            } else if (mod == ModalidadeAcao.EVENTO) {
                if (view.getPainelProgramacaoEvento().getItens().isEmpty()) {
                    erros.add("Campo 15: Adicione pelo menos uma atividade na tabela de Programação do Evento.");
                }
            } else if (mod == ModalidadeAcao.PRESTACAO_SERVICOS) {
                if (view.getTxtPrestacaoNome().getText().trim().isEmpty()) {
                    erros.add("Campo 16: 'Nome completo do responsável técnico' é obrigatório para Prestação de Serviço.");
                    if (primeiroErro == null) primeiroErro = view.getTxtPrestacaoNome();
                }
                String emailPrest = view.getTxtPrestacaoEmail().getText().trim();
                if (emailPrest.isEmpty() || !emailPrest.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                    erros.add("Campo 19: 'E-mail do responsável técnico' é obrigatório e deve ser válido.");
                    if (primeiroErro == null) primeiroErro = view.getTxtPrestacaoEmail();
                }
                if (view.getTxtPrestacaoDescricao().getText().trim().isEmpty()) {
                    erros.add("Campo 20: 'Descrição técnica do serviço' é obrigatória.");
                    if (primeiroErro == null) primeiroErro = view.getTxtPrestacaoDescricao();
                }
            }
        }

        // 23 e 24. Ação Institucional Vinculada
        if (view.getAcaoVinculadaSelecionada() == AcaoVinculadaTipo.EXTENSAO) {
            if (view.getTxtNumeroProcessoSipac().getText().trim().isEmpty()) {
                erros.add("Campo 24: 'Número do processo SIPAC' é obrigatório quando vinculado à Ação de Extensão.");
                if (primeiroErro == null) primeiroErro = view.getTxtNumeroProcessoSipac();
            }
        } else if (view.getAcaoVinculadaSelecionada() == AcaoVinculadaTipo.OUTRA) {
            if (view.getTxtAcaoVinculadaOutra().getText().trim().isEmpty()) {
                erros.add("Campo 23: Descreva a outra ação institucional mais abrangente.");
                if (primeiroErro == null) primeiroErro = view.getTxtAcaoVinculadaOutra();
            }
        }

        // 27. ODS (Objetivos do Desenvolvimento Sustentável)
        List<String> ods = view.getOdsSelecionados();
        if (ods.isEmpty()) {
            erros.add("Campo 27: Selecione pelo menos 1 Objetivo do Desenvolvimento Sustentável (ODS).");
        } else if (ods.size() > 2) {
            erros.add("Campo 27: Selecione no máximo 2 Objetivos do Desenvolvimento Sustentável (ODS).");
        }

        // 28 e 29. Público Alvo
        if (view.getTxtCaracterizacaoPublicoAlvo().getText().trim().isEmpty()) {
            erros.add("Campo 28: 'Caracterização do público alvo' é obrigatória.");
            if (primeiroErro == null) primeiroErro = view.getTxtCaracterizacaoPublicoAlvo();
        }

        String pubExtStr = view.getTxtNumeroPublicoExternoField().getText().trim();
        if (pubExtStr.isEmpty() || !pubExtStr.matches("\\d+")) {
            erros.add("Campo 29: 'Número total estimado de pessoas do público EXTERNO' deve conter apenas números.");
            if (primeiroErro == null) primeiroErro = view.getTxtNumeroPublicoExternoField();
        } else {
            int valExt = Integer.parseInt(pubExtStr);
            if (valExt <= 0) {
                erros.add("Campo 29: 'Público EXTERNO' deve ser maior que 0.");
                if (primeiroErro == null) primeiroErro = view.getTxtNumeroPublicoExternoField();
            }
        }

        // 39 a 45. Coordenação Adjunta (se Sim)
        if (view.isHaCoordenadorAdjuntoSelecionado()) {
            if (view.getTxtAdjuntoNome().getText().trim().isEmpty()) {
                erros.add("Campo 40: 'Nome do coordenador adjunto' é obrigatório.");
                if (primeiroErro == null) primeiroErro = view.getTxtAdjuntoNome();
            }
            String siapeAdj = view.getTxtAdjuntoSiape().getText().trim();
            if (siapeAdj.isEmpty() || !siapeAdj.matches("\\d+") || siapeAdj.length() < 7) {
                erros.add("Campo 41: 'Siape do coordenador adjunto' deve conter pelo menos 7 números.");
                if (primeiroErro == null) primeiroErro = view.getTxtAdjuntoSiape();
            }
            String emailAdj = view.getTxtAdjuntoEmail().getText().trim();
            if (emailAdj.isEmpty() || !emailAdj.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                erros.add("Campo 42: 'E-mail do coordenador adjunto' é obrigatório e deve ser válido.");
                if (primeiroErro == null) primeiroErro = view.getTxtAdjuntoEmail();
            }
            if (view.getTxtAdjuntoSetor().getText().trim().isEmpty()) {
                erros.add("Campo 44: 'Setor do coordenador adjunto' é obrigatório.");
                if (primeiroErro == null) primeiroErro = view.getTxtAdjuntoSetor();
            }
        }

        // 46 e 47. Público Interno
        if (view.getTxtCaracterizacaoPublicoInterno().getText().trim().isEmpty()) {
            erros.add("Campo 46: 'Caracterização do público interno' é obrigatória.");
            if (primeiroErro == null) primeiroErro = view.getTxtCaracterizacaoPublicoInterno();
        }
        String pubIntStr = view.getTxtNumeroPublicoInternoField().getText().trim();
        if (pubIntStr.isEmpty() || !pubIntStr.matches("\\d+")) {
            erros.add("Campo 47: 'Número total estimado de pessoas do público INTERNO' deve conter apenas números.");
            if (primeiroErro == null) primeiroErro = view.getTxtNumeroPublicoInternoField();
        }

        // 48 a 51. Detalhamento
        String resumo = view.getTxtResumo().getText().trim();
        if (resumo.isEmpty()) {
            erros.add("Campo 48: 'Resumo' é obrigatório.");
            if (primeiroErro == null) primeiroErro = view.getTxtResumo();
        } else if (resumo.length() > 3000) {
            erros.add("Campo 48: 'Resumo' excede o limite de 3000 caracteres (atual: " + resumo.length() + ").");
            if (primeiroErro == null) primeiroErro = view.getTxtResumo();
        }

        String palavras = view.getTxtPalavrasChave().getText().trim();
        if (palavras.isEmpty()) {
            erros.add("Campo 49: 'Palavras-chave' é obrigatório.");
            if (primeiroErro == null) primeiroErro = view.getTxtPalavrasChave();
        } else if (palavras.length() > 100) {
            erros.add("Campo 49: 'Palavras-chave' excede o limite de 100 caracteres (atual: " + palavras.length() + ").");
            if (primeiroErro == null) primeiroErro = view.getTxtPalavrasChave();
        }

        if (view.getTxtObjetivoGeral().getText().trim().isEmpty()) {
            erros.add("Campo 50: 'Objetivo Geral' é obrigatório.");
            if (primeiroErro == null) primeiroErro = view.getTxtObjetivoGeral();
        }
        if (view.getTxtObjetivosEspecificos().getText().trim().isEmpty()) {
            erros.add("Campo 51: 'Objetivos específicos' é obrigatório.");
            if (primeiroErro == null) primeiroErro = view.getTxtObjetivosEspecificos();
        }

        // 52 a 55. Fundamentação
        if (view.getTxtInfluenciaGrupos().getText().trim().isEmpty()) {
            erros.add("Campo 52: 'Influência dos grupos sociais externos' é obrigatória.");
            if (primeiroErro == null) primeiroErro = view.getTxtInfluenciaGrupos();
        }
        if (view.getTxtMudancasPublico().getText().trim().isEmpty()) {
            erros.add("Campo 53: 'Mudanças a serem produzidas' é obrigatória.");
            if (primeiroErro == null) primeiroErro = view.getTxtMudancasPublico();
        }
        if (view.getTxtRelacaoEnsinoPesquisa().getText().trim().isEmpty()) {
            erros.add("Campo 54: 'Relação com ensino e/ou pesquisa' é obrigatória.");
            if (primeiroErro == null) primeiroErro = view.getTxtRelacaoEnsinoPesquisa();
        }
        if (view.getTxtProtagonismoEstudantes().getText().trim().isEmpty()) {
            erros.add("Campo 55: 'Participação de estudantes como protagonistas' é obrigatória.");
            if (primeiroErro == null) primeiroErro = view.getTxtProtagonismoEstudantes();
        }

        // 57. Cronograma
        if (view.getPainelCronogramaVisual().getItens().isEmpty()) {
            erros.add("Campo 57: Adicione pelo menos uma atividade no Cronograma de Execução.");
        }

        return primeiroErro;
    }

    private String sanitizarNomeArquivo(String nome) {
        String limpo = nome.replaceAll("[^a-zA-Z0-9_-]", "_");
        if (limpo.length() > 30) {
            limpo = limpo.substring(0, 30);
        }
        return limpo;
    }
}
