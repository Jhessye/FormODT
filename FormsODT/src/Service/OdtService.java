package Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import Model.Formulario;
import Model.enums.AcaoVinculadaTipo;
import Model.enums.CampusIfes;
import Model.enums.ModalidadeAcao;

public class OdtService {

    public void gerarDocumentoOdt(Formulario form, File arquivoDestino) throws IOException {
        if (arquivoDestino.getParentFile() != null) {
            arquivoDestino.getParentFile().mkdirs();
        }
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(arquivoDestino))) {
            // 1. mimetype (OBRIGATÓRIO: STORED, sem compressão, primeiro item do ZIP)
            byte[] mimeBytes = "application/vnd.oasis.opendocument.text".getBytes(StandardCharsets.US_ASCII);
            ZipEntry mimeEntry = new ZipEntry("mimetype");
            mimeEntry.setMethod(ZipEntry.STORED);
            mimeEntry.setSize(mimeBytes.length);
            mimeEntry.setCrc(calcularCrc(mimeBytes));
            zos.putNextEntry(mimeEntry);
            zos.write(mimeBytes);
            zos.closeEntry();

            // 2. META-INF/manifest.xml
            String manifestXml = gerarManifestXml();
            adicionarEntradaZip(zos, "META-INF/manifest.xml", manifestXml);

            // 3. meta.xml
            String metaXml = gerarMetaXml(form);
            adicionarEntradaZip(zos, "meta.xml", metaXml);

            // 4. styles.xml
            String stylesXml = gerarStylesXml();
            adicionarEntradaZip(zos, "styles.xml", stylesXml);

            // 5. content.xml (Conteúdo principal formatado do formulário)
            String contentXml = gerarContentXml(form);
            adicionarEntradaZip(zos, "content.xml", contentXml);
        }
    }

    private void adicionarEntradaZip(ZipOutputStream zos, String nomeEntrada, String conteudo) throws IOException {
        byte[] bytes = conteudo.getBytes(StandardCharsets.UTF_8);
        ZipEntry entry = new ZipEntry(nomeEntrada);
        entry.setMethod(ZipEntry.DEFLATED);
        zos.putNextEntry(entry);
        zos.write(bytes);
        zos.closeEntry();
    }

    private long calcularCrc(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }

    private String gerarManifestXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
             + "<manifest:manifest xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\" manifest:version=\"1.3\">\n"
             + "  <manifest:file-entry manifest:full-path=\"/\" manifest:version=\"1.3\" manifest:media-type=\"application/vnd.oasis.opendocument.text\"/>\n"
             + "  <manifest:file-entry manifest:full-path=\"content.xml\" manifest:media-type=\"text/xml\"/>\n"
             + "  <manifest:file-entry manifest:full-path=\"styles.xml\" manifest:media-type=\"text/xml\"/>\n"
             + "  <manifest:file-entry manifest:full-path=\"meta.xml\" manifest:media-type=\"text/xml\"/>\n"
             + "</manifest:manifest>";
    }

    private String gerarMetaXml(Formulario form) {
        String dataAtual = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
        String autor = escapeXml(form.getDadosCadastrais().getNomeCoordenador());
        String titulo = escapeXml(form.getDadosCadastrais().getTituloAcao());
        if (titulo.isEmpty()) titulo = "Formulário de Ação de Extensão";

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
             + "<office:document-meta xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
             + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" "
             + "xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" "
             + "office:version=\"1.3\">\n"
             + "  <office:meta>\n"
             + "    <dc:title>" + titulo + "</dc:title>\n"
             + "    <dc:creator>" + autor + "</dc:creator>\n"
             + "    <dc:date>" + dataAtual + "</dc:date>\n"
             + "    <meta:creation-date>" + dataAtual + "</meta:creation-date>\n"
             + "  </office:meta>\n"
             + "</office:document-meta>";
    }

    private String gerarStylesXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
             + "<office:document-styles xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" "
             + "xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" "
             + "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" "
             + "xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" "
             + "office:version=\"1.3\">\n"
             + "  <office:styles>\n"
             + "    <style:default-style style:family=\"paragraph\">\n"
             + "      <style:paragraph-properties fo:line-height=\"120%\" fo:margin-top=\"2pt\" fo:margin-bottom=\"3pt\"/>\n"
             + "      <style:text-properties fo:font-size=\"11pt\" fo:font-family=\"Liberation Sans, Arial, sans-serif\" fo:color=\"#222222\"/>\n"
             + "    </style:default-style>\n"
             + "  </office:styles>\n"
             + "  <office:automatic-styles>\n"
             + "    <style:page-layout style:name=\"pm1\">\n"
             + "      <style:page-layout-properties fo:page-width=\"210mm\" fo:page-height=\"297mm\" fo:margin-top=\"20mm\" fo:margin-bottom=\"20mm\" fo:margin-left=\"20mm\" fo:margin-right=\"20mm\"/>\n"
             + "    </style:page-layout>\n"
             + "  </office:automatic-styles>\n"
             + "  <office:master-styles>\n"
             + "    <style:master-page style:name=\"Standard\" style:page-layout-name=\"pm1\"/>\n"
             + "  </office:master-styles>\n"
             + "</office:document-styles>";
    }

    private String gerarContentXml(Formulario f) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" ");
        sb.append("xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" ");
        sb.append("xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" ");
        sb.append("xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" ");
        sb.append("xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" ");
        sb.append("office:version=\"1.3\">\n");

        // Estilos automáticos do conteúdo
        sb.append("  <office:automatic-styles>\n");
        sb.append("    <style:style style:name=\"DocTitle\" style:family=\"paragraph\">\n");
        sb.append("      <style:paragraph-properties fo:text-align=\"center\" fo:margin-bottom=\"4pt\"/>\n");
        sb.append("      <style:text-properties fo:font-size=\"15pt\" fo:font-weight=\"bold\" fo:color=\"#003366\" fo:font-family=\"Liberation Sans, Arial\"/>\n");
        sb.append("    </style:style>\n");

        sb.append("    <style:style style:name=\"DocSubTitle\" style:family=\"paragraph\">\n");
        sb.append("      <style:paragraph-properties fo:text-align=\"center\" fo:margin-bottom=\"14pt\"/>\n");
        sb.append("      <style:text-properties fo:font-size=\"10pt\" fo:font-style=\"italic\" fo:color=\"#555555\" fo:font-family=\"Liberation Sans, Arial\"/>\n");
        sb.append("    </style:style>\n");

        sb.append("    <style:style style:name=\"SecHeading\" style:family=\"paragraph\">\n");
        sb.append("      <style:paragraph-properties fo:margin-top=\"12pt\" fo:margin-bottom=\"4pt\" fo:keep-with-next=\"always\"/>\n");
        sb.append("      <style:text-properties fo:font-size=\"12pt\" fo:font-weight=\"bold\" fo:color=\"#003366\" fo:font-family=\"Liberation Sans, Arial\"/>\n");
        sb.append("    </style:style>\n");

        sb.append("    <style:style style:name=\"FieldLabel\" style:family=\"text\">\n");
        sb.append("      <style:text-properties fo:font-weight=\"bold\" fo:color=\"#111111\"/>\n");
        sb.append("    </style:style>\n");

        sb.append("    <style:style style:name=\"FieldValue\" style:family=\"text\">\n");
        sb.append("      <style:text-properties fo:color=\"#222222\"/>\n");
        sb.append("    </style:style>\n");

        sb.append("    <style:style style:name=\"BodyPara\" style:family=\"paragraph\">\n");
        sb.append("      <style:paragraph-properties fo:margin-top=\"2pt\" fo:margin-bottom=\"4pt\" fo:text-align=\"justify\"/>\n");
        sb.append("      <style:text-properties fo:font-size=\"10.5pt\" fo:font-family=\"Liberation Sans, Arial\"/>\n");
        sb.append("    </style:style>\n");

        sb.append("    <style:style style:name=\"TableBorder\" style:family=\"table\">\n");
        sb.append("      <style:table-properties fo:margin-top=\"4pt\" fo:margin-bottom=\"8pt\" style:width=\"100%\"/>\n");
        sb.append("    </style:style>\n");

        sb.append("    <style:style style:name=\"TableCellHead\" style:family=\"table-cell\">\n");
        sb.append("      <style:table-cell-properties fo:background-color=\"#E8EEF5\" fo:padding=\"4pt\" fo:border=\"0.5pt solid #999999\"/>\n");
        sb.append("    </style:style>\n");
        sb.append("\n");
        sb.append("    <style:style style:name=\"TableCell\" style:family=\"table-cell\">\n");
        sb.append("      <style:table-cell-properties fo:padding=\"4pt\" fo:border=\"0.5pt solid #CCCCCC\"/>\n");
        sb.append("    </style:style>\n");
        sb.append("\n");
        sb.append("    <style:style style:name=\"TableCellActive\" style:family=\"table-cell\">\n");
        sb.append("      <style:table-cell-properties fo:background-color=\"#0066CC\" fo:padding=\"3pt\" fo:border=\"0.5pt solid #004C99\"/>\n");
        sb.append("    </style:style>\n");
        sb.append("\n");
        sb.append("    <style:style style:name=\"TextCenter\" style:family=\"paragraph\">\n");
        sb.append("      <style:paragraph-properties fo:text-align=\"center\"/>\n");
        sb.append("      <style:text-properties fo:font-size=\"9pt\" fo:font-family=\"Liberation Sans, Arial\"/>\n");
        sb.append("    </style:style>\n");
        sb.append("\n");
        sb.append("    <style:style style:name=\"TextCenterWhite\" style:family=\"paragraph\">\n");
        sb.append("      <style:paragraph-properties fo:text-align=\"center\"/>\n");
        sb.append("      <style:text-properties fo:color=\"#FFFFFF\" fo:font-weight=\"bold\" fo:font-size=\"9pt\" fo:font-family=\"Liberation Sans, Arial\"/>\n");
        sb.append("    </style:style>\n");
        sb.append("  </office:automatic-styles>\n");

        sb.append("  <office:body>\n");
        sb.append("    <office:text>\n");

        // Cabeçalho Institucional
        sb.append("      <text:p text:style-name=\"DocTitle\">CADASTRO DE EXTENSÃO</text:p>\n");
        sb.append("      <text:p text:style-name=\"DocSubTitle\">MODELO - ORIENTAÇÃO NORMATIVA CAEX 01/2020 - INSTITUCIONALIZAÇÃO DE AÇÕES DE EXTENSÃO</text:p>\n");

        // I. DADOS CADASTRAIS
        sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"1\">I. DADOS CADASTRAIS</text:h>\n");
        escreverCampo(sb, "1. Título da ação", f.getDadosCadastrais().getTituloAcao());
        escreverCampo(sb, "2. Nome completo do coordenador", f.getDadosCadastrais().getNomeCoordenador());
        escreverCampo(sb, "3. Siape", f.getDadosCadastrais().getSiape());
        escreverCampo(sb, "4. E-mail do coordenador", f.getDadosCadastrais().getEmail());
        escreverCampo(sb, "5. Cargo", f.getDadosCadastrais().getCargo() != null ? f.getDadosCadastrais().getCargo().getDescricao() : "-");
        escreverCampo(sb, "6. Setor", f.getDadosCadastrais().getSetor());
        escreverCampo(sb, "7. Campus", f.getDadosCadastrais().getCampus() != null ? f.getDadosCadastrais().getCampus().getNome() : "-");
        escreverCampo(sb, "9. Início do período de vigência", f.getDadosCadastrais().getInicioVigencia());
        escreverCampo(sb, "10. Fim do período de vigência", f.getDadosCadastrais().getFimVigencia());
        escreverCampo(sb, "11. A proposta desenvolve técnica, método ou modelo inovador?", f.getDadosCadastrais().getPropostaInovadora() != null ? f.getDadosCadastrais().getPropostaInovadora().getDescricao() : "-");

        // 12. MODALIDADE DA AÇÃO
        sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"1\">MODALIDADE DA AÇÃO</text:h>\n");
        ModalidadeAcao mod = f.getModalidadeEspecifica().getModalidade();
        escreverCampo(sb, "12. Modalidade escolhida", mod != null ? mod.getDescricao() : "Não informada");

        // Seções condicionais conforme a modalidade
        if (mod == ModalidadeAcao.PROGRAMA_REDE) {
            sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"2\">SEÇÃO ESPECÍFICA: PROGRAMA EM REDE</text:h>\n");
            escreverCampo(sb, "14. Está certo que a proposição é de novo Programa em Rede?", f.getModalidadeEspecifica().getRedeConfirmado() ? "Sim" : "Não");
        } else if (mod == ModalidadeAcao.PROGRAMA_MULTICAMPI) {
            sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"2\">SEÇÃO ESPECÍFICA: PROGRAMA MULTICAMPI</text:h>\n");
            StringBuilder campiStr = new StringBuilder();
            for (CampusIfes c : f.getModalidadeEspecifica().getUnidadesMulticampi()) {
                if (campiStr.length() > 0) campiStr.append(", ");
                campiStr.append(c.getNome());
            }
            escreverCampo(sb, "13. Unidades onde a ação está sendo executada", campiStr.length() > 0 ? campiStr.toString() : "Nenhuma unidade selecionada");
        } else if (mod == ModalidadeAcao.EVENTO) {
            sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"2\">SEÇÃO ESPECÍFICA: EVENTO</text:h>\n");
            escreverProgramacaoEventoComTabela(sb, f.getModalidadeEspecifica());
        } else if (mod == ModalidadeAcao.PRESTACAO_SERVICOS) {
            sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"2\">SEÇÃO ESPECÍFICA: PRESTAÇÃO DE SERVIÇO</text:h>\n");
            escreverCampo(sb, "16. Nome do responsável técnico", f.getModalidadeEspecifica().getPrestacaoNomeResponsavel());
            escreverCampo(sb, "17. Registro técnico (CREA/CRM/etc)", f.getModalidadeEspecifica().getPrestacaoRegistroTecnico());
            escreverCampo(sb, "18. Siape do responsável técnico", f.getModalidadeEspecifica().getPrestacaoSiape());
            escreverCampo(sb, "19. E-mail do responsável técnico", f.getModalidadeEspecifica().getPrestacaoEmail());
            escreverCampoTextoLongo(sb, "20. Descrição técnica do serviço", f.getModalidadeEspecifica().getPrestacaoDescricaoTecnica());
        }

        // II. CARACTERIZAÇÃO
        sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"1\">II. CARACTERIZAÇÃO</text:h>\n");
        String curricular = f.getCaracterizacao().isNaoPossuiCurricular() 
            ? "Não possui atividades curriculares em curso regular." 
            : String.join(", ", f.getCaracterizacao().getCursosCurriculares());
        if (curricular.trim().isEmpty()) curricular = "Nenhum curso informado";
        escreverCampo(sb, "21. Atividades curriculares de extensão", curricular);

        String fomento = String.join(", ", f.getCaracterizacao().getFomentoSelecionados());
        if (!f.getCaracterizacao().getFomentoOutro().isEmpty()) {
            fomento += (fomento.isEmpty() ? "" : ", ") + "Outro: " + f.getCaracterizacao().getFomentoOutro();
        }
        escreverCampo(sb, "22. Fomento da ação", fomento.isEmpty() ? "Não possui" : fomento);

        AcaoVinculadaTipo acaoVinc = f.getCaracterizacao().getAcaoMaisAbrangente();
        String acaoVincStr = acaoVinc != null ? acaoVinc.getDescricao() : "Não vinculada";
        if (acaoVinc == AcaoVinculadaTipo.OUTRA && !f.getCaracterizacao().getAcaoMaisAbrangenteOutro().isEmpty()) {
            acaoVincStr += " (" + f.getCaracterizacao().getAcaoMaisAbrangenteOutro() + ")";
        }
        escreverCampo(sb, "23. Ação institucional mais abrangente vinculada", acaoVincStr);

        if (acaoVinc == AcaoVinculadaTipo.EXTENSAO) {
            escreverCampo(sb, "24. Número do processo SIPAC da ação vinculada", f.getCaracterizacao().getNumeroProcessoSipac());
        }

        // III. ÁREAS TEMÁTICAS
        sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"1\">ÁREAS TEMÁTICAS E ODS</text:h>\n");
        escreverCampo(sb, "25. Área temática principal", f.getAreasTematicas().getAreaPrincipal() != null ? f.getAreasTematicas().getAreaPrincipal().getDescricao() : "-");
        escreverCampo(sb, "26. Área temática secundária", f.getAreasTematicas().getAreaSecundaria() != null ? f.getAreasTematicas().getAreaSecundaria().getDescricao() : "Nenhuma");
        String ods = String.join(" | ", f.getAreasTematicas().getOdsSelecionados());
        escreverCampo(sb, "27. Objetivos de Desenvolvimento Sustentável (ODS)", ods.isEmpty() ? "Nenhum selecionado" : ods);

        // IV. PÚBLICO ALVO, ORGANIZAÇÕES PARTICIPANTES
        sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"1\">PÚBLICO ALVO E ORGANIZAÇÕES PARTICIPANTES</text:h>\n");
        escreverCampoTextoLongo(sb, "28. Caracterização do público alvo", f.getPublicoParceiros().getCaracterizacaoPublicoAlvo());
        escreverCampo(sb, "29. Estimativa de público externo", String.valueOf(f.getPublicoParceiros().getTotalPublicoExterno()));
        escreverCampoTextoLongo(sb, "30. Organizações parceiras e descrição da participação", f.getPublicoParceiros().getOrganizacoesParceiras());
        escreverCampo(sb, "31. O parceiro vai aportar recursos?", f.getPublicoParceiros().getParceiroAportaRecursos());

        // V. EQUIPE EXECUTORA
        sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"1\">EQUIPE EXECUTORA</text:h>\n");
        escreverCampo(sb, "32. Estudantes de Formação Inicial e Continuada (FIC)", String.valueOf(f.getEquipeExecutora().getEstudantesFic()));
        escreverCampo(sb, "33. Estudantes de Curso Técnico", String.valueOf(f.getEquipeExecutora().getEstudantesTecnico()));
        escreverCampo(sb, "34. Estudantes de Graduação", String.valueOf(f.getEquipeExecutora().getEstudantesGraduacao()));
        escreverCampo(sb, "35. Estudantes de Pós-graduação", String.valueOf(f.getEquipeExecutora().getEstudantesPosGraduacao()));
        escreverCampo(sb, "36. Servidores Docentes", String.valueOf(f.getEquipeExecutora().getServidoresDocentes()));
        escreverCampo(sb, "37. Servidores Técnico-Administrativos", String.valueOf(f.getEquipeExecutora().getServidoresTecnicoAdministrativos()));
        escreverCampo(sb, "38. Colaboradores Externos", String.valueOf(f.getEquipeExecutora().getColaboradoresExternos()));

        // Coordenação Adjunta (se houver)
        if (f.getEquipeExecutora().isHaCoordenadorAdjunto()) {
            sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"2\">COORDENAÇÃO ADJUNTA</text:h>\n");
            escreverCampo(sb, "40. Nome do coordenador adjunto", f.getCoordenacaoAdjunta().getNomeCoordenadorAdjunto());
            escreverCampo(sb, "41. Siape do coordenador adjunto", f.getCoordenacaoAdjunta().getSiape());
            escreverCampo(sb, "42. E-mail do coordenador adjunto", f.getCoordenacaoAdjunta().getEmail());
            escreverCampo(sb, "43. Cargo", f.getCoordenacaoAdjunta().getCargo() != null ? f.getCoordenacaoAdjunta().getCargo().getDescricao() : "-");
            escreverCampo(sb, "44. Setor", f.getCoordenacaoAdjunta().getSetor());
            escreverCampo(sb, "45. Campus", f.getCoordenacaoAdjunta().getCampus() != null ? f.getCoordenacaoAdjunta().getCampus().getNome() : "-");
        } else {
            escreverCampo(sb, "39. Há coordenador adjunto?", "Não");
        }

        // VI. PÚBLICO INTERNO
        sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"1\">PÚBLICO INTERNO</text:h>\n");
        escreverCampoTextoLongo(sb, "46. Caracterização do público interno", f.getPublicoInterno().getCaracterizacaoPublicoInterno());
        escreverCampo(sb, "47. Estimativa de público interno", String.valueOf(f.getPublicoInterno().getTotalPublicoInterno()));

        // VII. DETALHAMENTO DA AÇÃO
        sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"1\">DETALHAMENTO DA AÇÃO</text:h>\n");
        escreverCampoTextoLongo(sb, "48. Resumo", f.getDetalhamentoAcao().getResumo());
        escreverCampo(sb, "49. Palavras-chave", f.getDetalhamentoAcao().getPalavrasChave());
        escreverCampoTextoLongo(sb, "50. Objetivo Geral", f.getDetalhamentoAcao().getObjetivoGeral());
        escreverCampoTextoLongo(sb, "51. Objetivos Específicos", f.getDetalhamentoAcao().getObjetivosEspecificos());

        // VIII. FUNDAMENTAÇÃO
        sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"1\">FUNDAMENTAÇÃO E DIRETRIZES DA EXTENSÃO</text:h>\n");
        escreverCampoTextoLongo(sb, "52. Participação dos grupos sociais / externos no planejamento", f.getFundamentacao().getInfluenciaGruposSociais());
        escreverCampoTextoLongo(sb, "53. Mudanças a serem produzidas com o público externo", f.getFundamentacao().getMudancasPublicoExterno());
        escreverCampoTextoLongo(sb, "54. Relação com Ensino e Pesquisa", f.getFundamentacao().getRelacaoEnsinoPesquisa());
        escreverCampoTextoLongo(sb, "55. Participação de estudantes como protagonistas", f.getFundamentacao().getProtagonismoEstudantes());
        escreverCampoTextoLongo(sb, "56. Instalações, equipamentos e materiais necessários", f.getFundamentacao().getInstalacoesEquipamentos());

        // IX. CRONOGRAMA
        sb.append("      <text:h text:style-name=\"SecHeading\" text:outline-level=\"1\">CRONOGRAMA E OBSERVAÇÕES</text:h>\n");
        escreverCronogramaComMatriz(sb, f.getCronograma());
        escreverCampoTextoLongo(sb, "58. Observações complementares", f.getCronograma().getObservacoes());

        // Campos de Assinaturas institucionais
        sb.append("      <text:p text:style-name=\"SecHeading\"><text:line-break/><text:line-break/>CAMPOS DE ASSINATURA</text:p>\n");
        sb.append("      <text:p text:style-name=\"BodyPara\">__________________________________________________</text:p>\n");
        sb.append("      <text:p text:style-name=\"BodyPara\">Coordenador(a) da Ação de Extensão</text:p>\n");
        sb.append("      <text:p text:style-name=\"BodyPara\"><text:line-break/>__________________________________________________</text:p>\n");
        sb.append("      <text:p text:style-name=\"BodyPara\">Chefia Imediata</text:p>\n");
        sb.append("      <text:p text:style-name=\"BodyPara\"><text:line-break/>__________________________________________________</text:p>\n");
        sb.append("      <text:p text:style-name=\"BodyPara\">Gestão de Extensão do Campus</text:p>\n");

        sb.append("    </office:text>\n");
        sb.append("  </office:body>\n");
        sb.append("</office:document-content>");

        return sb.toString();
    }

    private void escreverCampo(StringBuilder sb, String label, String valor) {
        String val = (valor == null || valor.trim().isEmpty()) ? "-" : escapeXml(valor);
        sb.append("      <text:p text:style-name=\"BodyPara\">");
        sb.append("<text:span text:style-name=\"FieldLabel\">").append(escapeXml(label)).append(": </text:span>");
        sb.append("<text:span text:style-name=\"FieldValue\">").append(val).append("</text:span>");
        sb.append("</text:p>\n");
    }

    private void escreverCampoTextoLongo(StringBuilder sb, String label, String valor) {
        sb.append("      <text:p text:style-name=\"BodyPara\">");
        sb.append("<text:span text:style-name=\"FieldLabel\">").append(escapeXml(label)).append(":</text:span>");
        sb.append("</text:p>\n");

        if (valor == null || valor.trim().isEmpty()) {
            sb.append("      <text:p text:style-name=\"BodyPara\"><text:span text:style-name=\"FieldValue\">-</text:span></text:p>\n");
        } else {
            String[] linhas = valor.split("\r?\n");
            for (String l : linhas) {
                if (!l.trim().isEmpty()) {
                    sb.append("      <text:p text:style-name=\"BodyPara\">")
                      .append(escapeXml(l))
                      .append("</text:p>\n");
                }
            }
        }
    }

    private void escreverCronogramaComMatriz(StringBuilder sb, Model.Cronograma cro) {
        sb.append("      <text:p text:style-name=\"BodyPara\">");
        sb.append("<text:span text:style-name=\"FieldLabel\">57. Atividades e Cronograma de Execução:</text:span>");
        sb.append("</text:p>\n");

        if (cro.getItens() == null || cro.getItens().isEmpty()) {
            escreverCampoTextoLongo(sb, "Detalhamento", cro.getAtividadesCronograma());
            return;
        }

        // 1. Lista descritiva (padrão solicitado na Orientação Normativa CAEX 01/2020)
        for (int i = 0; i < cro.getItens().size(); i++) {
            Model.ItemCronograma item = cro.getItens().get(i);
            sb.append("      <text:p text:style-name=\"BodyPara\">")
              .append("<text:span text:style-name=\"FieldLabel\">Atividade ").append(i + 1).append(" - </text:span>")
              .append("<text:span text:style-name=\"FieldValue\">").append(escapeXml(item.getDescricao())).append(" ... ")
              .append(escapeXml(item.getPeriodoFormatado())).append("</text:span>")
              .append("</text:p>\n");
        }

        // 2. Determina o maior mês para dimensionar a matriz do calendário
        int maxMes = 12;
        for (Model.ItemCronograma item : cro.getItens()) {
            if (item.getMesFim() > maxMes) {
                maxMes = item.getMesFim();
            }
        }

        // 3. Tabela Matriz do Cronograma (Gráfico de Gantt em ODT)
        sb.append("      <text:p text:style-name=\"SecHeading\"><text:line-break/>Matriz de Execução Mensal (Cronograma Físico):</text:p>\n");
        sb.append("      <table:table table:name=\"TabelaCronogramaMatriz\">\n");
        sb.append("        <table:table-column table:number-columns-repeated=\"1\"/>\n"); // #
        sb.append("        <table:table-column table:number-columns-repeated=\"1\"/>\n"); // Atividade
        sb.append("        <table:table-column table:number-columns-repeated=\"1\"/>\n"); // Período
        sb.append("        <table:table-column table:number-columns-repeated=\"").append(maxMes).append("\"/>\n"); // Meses M1..MN

        // Cabeçalho da Tabela
        sb.append("        <table:table-header-rows>\n");
        sb.append("          <table:table-row>\n");
        sb.append("            <table:table-cell table:style-name=\"TableCellHead\"><text:p text:style-name=\"TextCenter\">#</text:p></table:table-cell>\n");
        sb.append("            <table:table-cell table:style-name=\"TableCellHead\"><text:p text:style-name=\"TextCenter\">Atividade</text:p></table:table-cell>\n");
        sb.append("            <table:table-cell table:style-name=\"TableCellHead\"><text:p text:style-name=\"TextCenter\">Período</text:p></table:table-cell>\n");
        for (int m = 1; m <= maxMes; m++) {
            sb.append("            <table:table-cell table:style-name=\"TableCellHead\"><text:p text:style-name=\"TextCenter\">M").append(m).append("</text:p></table:table-cell>\n");
        }
        sb.append("          </table:table-row>\n");
        sb.append("        </table:table-header-rows>\n");

        // Linhas de Atividades
        for (int i = 0; i < cro.getItens().size(); i++) {
            Model.ItemCronograma item = cro.getItens().get(i);
            sb.append("        <table:table-row>\n");
            sb.append("          <table:table-cell table:style-name=\"TableCell\"><text:p text:style-name=\"TextCenter\">").append(i + 1).append("</text:p></table:table-cell>\n");
            sb.append("          <table:table-cell table:style-name=\"TableCell\"><text:p text:style-name=\"BodyPara\">").append(escapeXml(item.getDescricao())).append("</text:p></table:table-cell>\n");
            sb.append("          <table:table-cell table:style-name=\"TableCell\"><text:p text:style-name=\"TextCenter\">").append(escapeXml(item.getPeriodoFormatado())).append("</text:p></table:table-cell>\n");

            for (int m = 1; m <= maxMes; m++) {
                if (item.isAtivoNoMes(m)) {
                    sb.append("          <table:table-cell table:style-name=\"TableCellActive\"><text:p text:style-name=\"TextCenterWhite\">■</text:p></table:table-cell>\n");
                } else {
                    sb.append("          <table:table-cell table:style-name=\"TableCell\"><text:p text:style-name=\"TextCenter\">-</text:p></table:table-cell>\n");
                }
            }
            sb.append("        </table:table-row>\n");
        }

        sb.append("      </table:table>\n");
        sb.append("      <text:p text:style-name=\"BodyPara\"><text:line-break/></text:p>\n");
    }

    private void escreverProgramacaoEventoComTabela(StringBuilder sb, Model.SecaoModalidadeEspecifica sme) {
        sb.append("      <text:p text:style-name=\"BodyPara\">");
        sb.append("<text:span text:style-name=\"FieldLabel\">15. Programação do evento:</text:span>");
        sb.append("</text:p>\n");

        if (sme.getItensProgramacaoEvento() == null || sme.getItensProgramacaoEvento().isEmpty()) {
            escreverCampoTextoLongo(sb, "Detalhamento", sme.getProgramacaoEvento());
            return;
        }

        // Tabela ODT formatada para a programação do evento
        sb.append("      <table:table table:name=\"TabelaProgramacaoEvento\">\n");
        sb.append("        <table:table-column table:number-columns-repeated=\"1\"/>\n"); // #
        sb.append("        <table:table-column table:number-columns-repeated=\"1\"/>\n"); // Atividade
        sb.append("        <table:table-column table:number-columns-repeated=\"1\"/>\n"); // Data
        sb.append("        <table:table-column table:number-columns-repeated=\"1\"/>\n"); // Horário
        sb.append("        <table:table-column table:number-columns-repeated=\"1\"/>\n"); // Local
        sb.append("        <table:table-column table:number-columns-repeated=\"1\"/>\n"); // Responsável

        // Cabeçalho
        sb.append("        <table:table-header-rows>\n");
        sb.append("          <table:table-row>\n");
        sb.append("            <table:table-cell table:style-name=\"TableCellHead\"><text:p text:style-name=\"TextCenter\">#</text:p></table:table-cell>\n");
        sb.append("            <table:table-cell table:style-name=\"TableCellHead\"><text:p text:style-name=\"TextCenter\">Atividade / Conteúdo</text:p></table:table-cell>\n");
        sb.append("            <table:table-cell table:style-name=\"TableCellHead\"><text:p text:style-name=\"TextCenter\">Data</text:p></table:table-cell>\n");
        sb.append("            <table:table-cell table:style-name=\"TableCellHead\"><text:p text:style-name=\"TextCenter\">Horário</text:p></table:table-cell>\n");
        sb.append("            <table:table-cell table:style-name=\"TableCellHead\"><text:p text:style-name=\"TextCenter\">Local</text:p></table:table-cell>\n");
        sb.append("            <table:table-cell table:style-name=\"TableCellHead\"><text:p text:style-name=\"TextCenter\">Responsável</text:p></table:table-cell>\n");
        sb.append("          </table:table-row>\n");
        sb.append("        </table:table-header-rows>\n");

        // Linhas de atividades do evento
        for (int i = 0; i < sme.getItensProgramacaoEvento().size(); i++) {
            Model.ItemProgramacaoEvento it = sme.getItensProgramacaoEvento().get(i);
            sb.append("        <table:table-row>\n");
            sb.append("          <table:table-cell table:style-name=\"TableCell\"><text:p text:style-name=\"TextCenter\">").append(i + 1).append("</text:p></table:table-cell>\n");
            sb.append("          <table:table-cell table:style-name=\"TableCell\"><text:p text:style-name=\"BodyPara\">").append(escapeXml(it.getAtividade())).append("</text:p></table:table-cell>\n");
            sb.append("          <table:table-cell table:style-name=\"TableCell\"><text:p text:style-name=\"TextCenter\">").append(escapeXml(it.getData())).append("</text:p></table:table-cell>\n");
            sb.append("          <table:table-cell table:style-name=\"TableCell\"><text:p text:style-name=\"TextCenter\">").append(escapeXml(it.getHorario())).append("</text:p></table:table-cell>\n");
            sb.append("          <table:table-cell table:style-name=\"TableCell\"><text:p text:style-name=\"BodyPara\">").append(escapeXml(it.getLocal())).append("</text:p></table:table-cell>\n");
            sb.append("          <table:table-cell table:style-name=\"TableCell\"><text:p text:style-name=\"BodyPara\">").append(escapeXml(it.getResponsavel())).append("</text:p></table:table-cell>\n");
            sb.append("        </table:table-row>\n");
        }

        sb.append("      </table:table>\n");
        sb.append("      <text:p text:style-name=\"BodyPara\"><text:line-break/></text:p>\n");
    }

    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
}
