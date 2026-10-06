package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import Model.ItemCronograma;
import Util.ComponentUtils;

public class PainelCronograma extends JPanel {

    private final List<ItemCronograma> itens = new ArrayList<>();
    private int quantidadeMesesVisiveis = 12;

    private JTextField txtDescricao;
    private JComboBox<Integer> comboMesInicio;
    private JComboBox<Integer> comboMesFim;
    private JButton btnAdicionar;
    private JButton btnRemover;
    private JButton btnLimpar;
    private JComboBox<String> comboEscalaMeses;
    private JLabel lblContador;

    private JTable tabela;
    private CronogramaTableModel tableModel;

    public PainelCronograma() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Painel Superior: Inserção intuitiva de atividades
        JPanel painelInsercao = new JPanel(new GridBagLayout());
        painelInsercao.setBackground(new Color(245, 248, 253));
        painelInsercao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 225, 240), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 4, 3, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: Descrição da atividade
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel lblDesc = new JLabel("Descrição da Atividade:");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 12));
        painelInsercao.add(lblDesc, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.gridwidth = 5;
        txtDescricao = new JTextField();
        txtDescricao.setToolTipText("Digite a ação a ser realizada (ex: Divulgação das oficinas nas escolas)");
        painelInsercao.add(txtDescricao, gbc);

        // Linha 1: Período e Botões
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        JLabel lblPeriodo = new JLabel("Período de Execução:");
        lblPeriodo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        painelInsercao.add(lblPeriodo, gbc);

        // Painel de seleção de meses
        JPanel painelMeses = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        painelMeses.setOpaque(false);
        painelMeses.add(new JLabel("De:"));
        comboMesInicio = new JComboBox<>();
        comboMesFim = new JComboBox<>();
        for (int i = 1; i <= 36; i++) {
            comboMesInicio.addItem(i);
            comboMesFim.addItem(i);
        }
        comboMesInicio.setSelectedItem(1);
        comboMesFim.setSelectedItem(3);

        comboMesInicio.addActionListener(e -> {
            int ini = (Integer) comboMesInicio.getSelectedItem();
            int fim = (Integer) comboMesFim.getSelectedItem();
            if (fim < ini) {
                comboMesFim.setSelectedItem(ini);
            }
        });

        painelMeses.add(comboMesInicio);
        painelMeses.add(new JLabel("Até:"));
        painelMeses.add(comboMesFim);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.3;
        painelInsercao.add(painelMeses, gbc);

        // Botões de Adicionar e Remover
        JPanel painelBotoesAcao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        painelBotoesAcao.setOpaque(false);

        btnAdicionar = new JButton("+ Adicionar");
        btnAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAdicionar.setBackground(new Color(25, 125, 60));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setOpaque(true);

        btnRemover = new JButton("- Remover");
        btnRemover.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        painelBotoesAcao.add(btnAdicionar);
        painelBotoesAcao.add(btnRemover);
        painelBotoesAcao.add(btnLimpar);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.7; gbc.gridwidth = 4;
        painelInsercao.add(painelBotoesAcao, gbc);

        add(painelInsercao, BorderLayout.NORTH);

        // Painel Central: Tabela tipo Calendário / Gráfico de Gantt
        tableModel = new CronogramaTableModel();
        tabela = new JTable(tableModel);
        tabela.setRowHeight(26);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBackground(new Color(230, 238, 248));
        header.setForeground(new Color(20, 50, 90));

        configurarRenderizadoresTabela();

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setPreferredSize(new Dimension(660, 160));
        add(scrollTabela, BorderLayout.CENTER);

        // Painel Inferior: Legenda e Filtro de Meses Visíveis
        JPanel painelInferior = new JPanel(new BorderLayout(5, 5));
        painelInferior.setBorder(BorderFactory.createEmptyBorder(4, 2, 2, 2));

        JPanel painelLegenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JLabel lblIconeAtivo = new JLabel(" ■ ");
        lblIconeAtivo.setOpaque(true);
        lblIconeAtivo.setBackground(new Color(0, 102, 204));
        lblIconeAtivo.setForeground(Color.WHITE);
        painelLegenda.add(lblIconeAtivo);
        painelLegenda.add(new JLabel("Mês com atividade em execução"));

        lblContador = new JLabel("0 atividades no cronograma.");
        lblContador.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblContador.setForeground(Color.GRAY);
        painelLegenda.add(lblContador);

        JPanel painelEscala = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        painelEscala.add(new JLabel("Escala do Calendário:"));
        comboEscalaMeses = new JComboBox<>(new String[]{"12 Meses", "6 Meses", "18 Meses", "24 Meses", "36 Meses"});
        comboEscalaMeses.addActionListener(e -> {
            String sel = (String) comboEscalaMeses.getSelectedItem();
            if (sel != null) {
                quantidadeMesesVisiveis = Integer.parseInt(sel.split(" ")[0]);
                tableModel.fireTableStructureChanged();
                configurarRenderizadoresTabela();
            }
        });
        painelEscala.add(comboEscalaMeses);

        painelInferior.add(painelLegenda, BorderLayout.WEST);
        painelInferior.add(painelEscala, BorderLayout.EAST);

        add(painelInferior, BorderLayout.SOUTH);

        // Configuração de Eventos
        btnAdicionar.addActionListener(e -> adicionarAtividade());
        txtDescricao.addActionListener(e -> adicionarAtividade());

        btnRemover.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < itens.size()) {
                itens.remove(selectedRow);
                tableModel.fireTableDataChanged();
                atualizarContador();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma atividade na tabela para remover.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnLimpar.addActionListener(e -> {
            if (!itens.isEmpty()) {
                int opt = JOptionPane.showConfirmDialog(this, "Deseja remover todas as atividades do cronograma?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    limpar();
                }
            }
        });
    }

    private void adicionarAtividade() {
        String desc = txtDescricao.getText().trim();
        if (desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite a descrição da atividade antes de inserir.", "Descrição Obrigatória", JOptionPane.WARNING_MESSAGE);
            txtDescricao.requestFocus();
            return;
        }

        int inicio = (Integer) comboMesInicio.getSelectedItem();
        int fim = (Integer) comboMesFim.getSelectedItem();
        if (fim < inicio) {
            JOptionPane.showMessageDialog(this, "O mês final não pode ser anterior ao mês inicial.", "Período Inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        itens.add(new ItemCronograma(desc, inicio, fim));
        tableModel.fireTableDataChanged();
        atualizarContador();

        txtDescricao.setText("");
        txtDescricao.requestFocus();

        // Rola até a nova linha inserida
        int novaLinha = itens.size() - 1;
        tabela.setRowSelectionInterval(novaLinha, novaLinha);
    }

    private void configurarRenderizadoresTabela() {
        // Coluna 0: # (número da linha)
        if (tabela.getColumnCount() > 0) {
            tabela.getColumnModel().getColumn(0).setPreferredWidth(30);
            tabela.getColumnModel().getColumn(0).setMaxWidth(38);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        }

        // Coluna 1: Descrição da Atividade
        if (tabela.getColumnCount() > 1) {
            tabela.getColumnModel().getColumn(1).setPreferredWidth(210);
        }

        // Coluna 2: Período
        if (tabela.getColumnCount() > 2) {
            tabela.getColumnModel().getColumn(2).setPreferredWidth(75);
            tabela.getColumnModel().getColumn(2).setMaxWidth(95);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            tabela.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        }

        // Colunas de meses M1 a MN: Calendário Visual com Gantt em cores
        GanttCellRenderer ganttRenderer = new GanttCellRenderer();
        for (int c = 3; c < tabela.getColumnCount(); c++) {
            tabela.getColumnModel().getColumn(c).setPreferredWidth(28);
            tabela.getColumnModel().getColumn(c).setMaxWidth(36);
            tabela.getColumnModel().getColumn(c).setCellRenderer(ganttRenderer);
        }
    }

    private void atualizarContador() {
        lblContador.setText(itens.size() + " atividade(s) no cronograma.");
    }

    public List<ItemCronograma> getItens() {
        return new ArrayList<>(itens);
    }

    public void setItens(List<ItemCronograma> novosItens) {
        this.itens.clear();
        if (novosItens != null) {
            this.itens.addAll(novosItens);
        }
        tableModel.fireTableDataChanged();
        atualizarContador();
    }

    public void limpar() {
        itens.clear();
        txtDescricao.setText("");
        comboMesInicio.setSelectedItem(1);
        comboMesFim.setSelectedItem(3);
        tableModel.fireTableDataChanged();
        atualizarContador();
    }

    // Renderizador customizado que pinta as células ativas como calendário / Gantt
    private class GanttCellRenderer extends DefaultTableCellRenderer {
        private final Color COR_ATIVO = new Color(0, 102, 204);
        private final Color COR_INATIVO = Color.WHITE;
        private final Color COR_ZEBRA = new Color(248, 250, 253);

        public GanttCellRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int mes = column - 2; // Coluna 3 corresponde ao Mês 1

            if (row >= 0 && row < itens.size()) {
                ItemCronograma item = itens.get(row);
                if (item.isAtivoNoMes(mes)) {
                    c.setBackground(COR_ATIVO);
                    c.setForeground(Color.WHITE);
                    setText("■");
                    setToolTipText("Mês " + mes + ": " + item.getDescricao() + " (Em execução)");
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : (row % 2 == 0 ? COR_INATIVO : COR_ZEBRA));
                    c.setForeground(Color.LIGHT_GRAY);
                    setText("");
                    setToolTipText(null);
                }
            }
            return c;
        }
    }

    // TableModel customizado dinâmico
    private class CronogramaTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return itens.size();
        }

        @Override
        public int getColumnCount() {
            return 3 + quantidadeMesesVisiveis; // #, Atividade, Período + M1..MN
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) return "#";
            if (column == 1) return "Atividade";
            if (column == 2) return "Período";
            int mes = column - 2;
            return "M" + mes;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex < 0 || rowIndex >= itens.size()) return null;
            ItemCronograma item = itens.get(rowIndex);

            if (columnIndex == 0) return (rowIndex + 1);
            if (columnIndex == 1) return item.getDescricao();
            if (columnIndex == 2) return item.getPeriodoFormatado();

            int mes = columnIndex - 2;
            return item.isAtivoNoMes(mes) ? "■" : "";
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
