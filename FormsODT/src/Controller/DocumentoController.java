package Controller;

import java.awt.Desktop;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import Model.Formulario;
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
        // Validações básicas amigáveis
        String titulo = view.getTxtTituloAcao().getText().trim();
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(view, 
                "Por favor, preencha o 'Título da ação' antes de gerar o documento.", 
                "Campo Obrigatório", 
                JOptionPane.WARNING_MESSAGE);
            view.getTxtTituloAcao().requestFocus();
            return;
        }

        String coordenador = view.getTxtNomeCoordenador().getText().trim();
        if (coordenador.isEmpty()) {
            JOptionPane.showMessageDialog(view, 
                "Por favor, preencha o 'Nome completo do coordenador' antes de gerar o documento.", 
                "Campo Obrigatório", 
                JOptionPane.WARNING_MESSAGE);
            view.getTxtNomeCoordenador().requestFocus();
            return;
        }

        ModalidadeAcao modalidade = view.getModalidadeSelecionada();
        if (modalidade == null || modalidade == ModalidadeAcao.SELECIONE) {
            JOptionPane.showMessageDialog(view, 
                "Por favor, selecione a 'Modalidade da Ação' (Pergunta 12).", 
                "Modalidade Obrigatória", 
                JOptionPane.WARNING_MESSAGE);
            view.getComboModalidade().requestFocus();
            return;
        }

        // Extrai todos os dados atuais do formulário
        Formulario formulario = formularioController.extrairDadosDaTela();

        // Diálogo para escolha do local de salvamento
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Formulário de Extensão (.odt)");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Documento OpenDocument (.odt)", "odt");
        fileChooser.setFileFilter(filter);

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

    private String sanitizarNomeArquivo(String nome) {
        String limpo = nome.replaceAll("[^a-zA-Z0-9_-]", "_");
        if (limpo.length() > 30) {
            limpo = limpo.substring(0, 30);
        }
        return limpo;
    }
}
