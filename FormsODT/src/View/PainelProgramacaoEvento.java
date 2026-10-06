package View;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Model.ItemProgramacaoEvento;
import Util.ComponentUtils;

public class PainelProgramacaoEvento extends JPanel {

    private JTextField txtAtividade;
    private JTextField txtData;
    private JTextField txtHorario;
    private JTextField txtLocal;
    private JTextField txtResponsavel;

    private JButton btnAdicionar;
    private JButton btnRemover;
    private JButton btnLimpar;
    private JLabel lblContador;

    private JTable tabela;
    private DefaultTableModel tableModel;

    public PainelProgramacaoEvento() {
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Painel Superior de Entrada Rápida
        JPanel painelInput = new JPanel(new GridBagLayout());
        painelInput.setBackground(new Color(246, 249, 253));
        painelInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 225, 240), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 3, 2, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: Atividade e Responsável
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painelInput.add(new JLabel("Atividade / Ação: *"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtAtividade = new JTextField();
        txtAtividade.setToolTipText("Nome da palestra, mesa redonda, minicurso, credenciamento, etc.");
        painelInput.add(txtAtividade, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        painelInput.add(new JLabel("Responsável:"), gbc);

        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.6;
        txtResponsavel = new JTextField();
        painelInput.add(txtResponsavel, gbc);

        // Linha 1: Data, Horário e Local
        JPanel painelDetalhes = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        painelDetalhes.setOpaque(false);

        painelDetalhes.add(new JLabel("Data:"));
        txtData = new JTextField(8);
        txtData.setToolTipText("Ex: 15/05/2026");
        painelDetalhes.add(txtData);

        painelDetalhes.add(new JLabel("Horário:"));
        txtHorario = new JTextField(9);
        txtHorario.setToolTipText("Ex: 09:00 - 11:30");
        painelDetalhes.add(txtHorario);

        painelDetalhes.add(new JLabel("Local:"));
        txtLocal = new JTextField(12);
        txtLocal.setToolTipText("Ex: Auditório Principal, Sala 102");
        painelDetalhes.add(txtLocal);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        painelInput.add(painelDetalhes, gbc);

        // Botões de ação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        painelBotoes.setOpaque(false);

        btnAdicionar = new JButton("+ Adicionar Linha");
        btnAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAdicionar.setBackground(new Color(25, 125, 60));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setOpaque(true);

        btnRemover = new JButton("- Remover");
        btnLimpar = new JButton("Limpar");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnLimpar);

        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 0;
        painelInput.add(painelBotoes, gbc);

        add(painelInput, BorderLayout.NORTH);

        // Tabela central da programação
        String[] colunas = {"#", "Atividade / Descrição", "Data", "Horário", "Local", "Responsável"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Coluna # não editável, as outras podem ser editadas diretamente na tabela!
            }
        };

        tabela = new JTable(tableModel);
        tabela.setRowHeight(24);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBackground(new Color(230, 238, 248));
        header.setForeground(new Color(20, 50, 90));

        // Dimensionamento das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(35);
        tabela.getColumnModel().getColumn(0).setMaxWidth(45);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        tabela.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(85);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(95);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(130);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(650, 150));
        add(scrollPane, BorderLayout.CENTER);

        // Barra de status inferior
        lblContador = new JLabel("0 atividade(s) na programação.");
        lblContador.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblContador.setForeground(Color.GRAY);
        add(lblContador, BorderLayout.SOUTH);

        // Eventos
        btnAdicionar.addActionListener(e -> adicionarLinha());
        txtAtividade.addActionListener(e -> adicionarLinha());

        btnRemover.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row >= 0) {
                tableModel.removeRow(row);
                renumerarLinhas();
                atualizarContador();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma linha da tabela para remover.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnLimpar.addActionListener(e -> {
            if (tableModel.getRowCount() > 0) {
                int opt = JOptionPane.showConfirmDialog(this, "Deseja remover todas as atividades da programação?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    limpar();
                }
            }
        });
    }

    private void adicionarLinha() {
        String ativ = txtAtividade.getText().trim();
        if (ativ.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite o nome da atividade da programação.", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtAtividade.requestFocus();
            return;
        }

        String data = txtData.getText().trim();
        String hora = txtHorario.getText().trim();
        String local = txtLocal.getText().trim();
        String resp = txtResponsavel.getText().trim();

        int num = tableModel.getRowCount() + 1;
        tableModel.addRow(new Object[]{num, ativ, data, hora, local, resp});
        atualizarContador();

        txtAtividade.setText("");
        txtData.setText("");
        txtHorario.setText("");
        txtLocal.setText("");
        txtResponsavel.setText("");
        txtAtividade.requestFocus();

        int novaLinha = tableModel.getRowCount() - 1;
        tabela.setRowSelectionInterval(novaLinha, novaLinha);
    }

    private void renumerarLinhas() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(i + 1, i, 0);
        }
    }

    private void atualizarContador() {
        lblContador.setText(tableModel.getRowCount() + " atividade(s) na programação.");
    }

    public List<ItemProgramacaoEvento> getItens() {
        List<ItemProgramacaoEvento> lista = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String ativ = (String) tableModel.getValueAt(i, 1);
            String data = (String) tableModel.getValueAt(i, 2);
            String hora = (String) tableModel.getValueAt(i, 3);
            String local = (String) tableModel.getValueAt(i, 4);
            String resp = (String) tableModel.getValueAt(i, 5);
            lista.add(new ItemProgramacaoEvento(ativ, data, hora, local, resp));
        }
        return lista;
    }

    public void setItens(List<ItemProgramacaoEvento> itens) {
        tableModel.setRowCount(0);
        if (itens != null) {
            int num = 1;
            for (ItemProgramacaoEvento it : itens) {
                tableModel.addRow(new Object[]{num++, it.getAtividade(), it.getData(), it.getHorario(), it.getLocal(), it.getResponsavel()});
            }
        }
        atualizarContador();
    }

    public void limpar() {
        tableModel.setRowCount(0);
        txtAtividade.setText("");
        txtData.setText("");
        txtHorario.setText("");
        txtLocal.setText("");
        txtResponsavel.setText("");
        atualizarContador();
    }
}
