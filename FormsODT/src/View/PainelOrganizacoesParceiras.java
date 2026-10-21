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
import javax.swing.JComboBox;
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

import Model.ItemOrganizacaoParceira;

public class PainelOrganizacoesParceiras extends JPanel {

    private JTextField txtNome;
    private JTextField txtDescricao;
    private JComboBox<String> comboAportaRecursos;

    private JButton btnAdicionar;
    private JButton btnRemover;
    private JButton btnLimpar;
    private JLabel lblContador;

    private JTable tabela;
    private DefaultTableModel tableModel;

    public PainelOrganizacoesParceiras() {
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Painel Superior com campos de entrada
        JPanel painelInput = new JPanel(new GridBagLayout());
        painelInput.setBackground(new Color(246, 249, 253));
        painelInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 225, 240), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 3, 2, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: Nome da Organização e Aporte de recursos
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painelInput.add(new JLabel("Organização Parceira: *"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtNome = new JTextField();
        txtNome.setToolTipText("Ex: Prefeitura de Vitória, Escola Estadual Modelo, Empresa ABC");
        painelInput.add(txtNome, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        painelInput.add(new JLabel("Aporta recursos?"), gbc);

        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.3;
        comboAportaRecursos = new JComboBox<>(new String[]{"Não", "Sim"});
        comboAportaRecursos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        painelInput.add(comboAportaRecursos, gbc);

        // Linha 1: Descrição da Participação e Botões
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painelInput.add(new JLabel("Descrição da Participação:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtDescricao = new JTextField();
        txtDescricao.setToolTipText("Ex: Cessão de salas e equipamentos, transporte de alunos, apoio operacional...");
        painelInput.add(txtDescricao, gbc);

        // Botões de ação com botões padrões e alto contraste
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        painelBotoes.setOpaque(false);

        btnAdicionar = new JButton("+ Adicionar");
        btnAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 11));

        btnRemover = new JButton("- Remover");
        btnRemover.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnLimpar);

        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 0;
        painelInput.add(painelBotoes, gbc);

        add(painelInput, BorderLayout.NORTH);

        // Tabela central
        String[] colunas = {"#", "Organização Parceira", "Descrição da Participação", "Aporta Recursos?"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Colunas editáveis diretamente pelo usuário se desejar
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

        tabela.getColumnModel().getColumn(1).setPreferredWidth(210);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(310);

        tabela.getColumnModel().getColumn(3).setPreferredWidth(110);
        tabela.getColumnModel().getColumn(3).setMaxWidth(130);
        tabela.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(650, 130));
        add(scrollPane, BorderLayout.CENTER);

        // Barra inferior
        lblContador = new JLabel("0 organização(ões) parceira(s) adicionada(s). (Opcional)");
        lblContador.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblContador.setForeground(Color.GRAY);
        add(lblContador, BorderLayout.SOUTH);

        // Eventos
        btnAdicionar.addActionListener(e -> adicionarLinha());
        txtNome.addActionListener(e -> adicionarLinha());
        txtDescricao.addActionListener(e -> adicionarLinha());

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
                int opt = JOptionPane.showConfirmDialog(this, "Deseja remover todas as organizações parceiras da tabela?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    limpar();
                }
            }
        });
    }

    private void adicionarLinha() {
        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o nome da organização parceira.", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return;
        }

        String desc = txtDescricao.getText().trim();
        if (desc.isEmpty()) {
            desc = "-";
        }
        String aporta = (String) comboAportaRecursos.getSelectedItem();

        int num = tableModel.getRowCount() + 1;
        tableModel.addRow(new Object[]{num, nome, desc, aporta});
        atualizarContador();

        txtNome.setText("");
        txtDescricao.setText("");
        comboAportaRecursos.setSelectedIndex(0);
        txtNome.requestFocus();

        int novaLinha = tableModel.getRowCount() - 1;
        tabela.setRowSelectionInterval(novaLinha, novaLinha);
    }

    private void renumerarLinhas() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(i + 1, i, 0);
        }
    }

    private void atualizarContador() {
        int count = tableModel.getRowCount();
        if (count == 0) {
            lblContador.setText("0 organização(ões) parceira(s) adicionada(s). (Opcional)");
        } else {
            lblContador.setText(count + " organização(ões) parceira(s) adicionada(s).");
        }
    }

    public List<ItemOrganizacaoParceira> getItens() {
        List<ItemOrganizacaoParceira> lista = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String nome = (String) tableModel.getValueAt(i, 1);
            String desc = (String) tableModel.getValueAt(i, 2);
            String aporta = (String) tableModel.getValueAt(i, 3);
            lista.add(new ItemOrganizacaoParceira(nome, desc, aporta));
        }
        return lista;
    }

    public void setItens(List<ItemOrganizacaoParceira> itens) {
        tableModel.setRowCount(0);
        if (itens != null) {
            int num = 1;
            for (ItemOrganizacaoParceira it : itens) {
                tableModel.addRow(new Object[]{num++, it.getNome(), it.getDescricaoParticipacao(), it.getAportaRecursos()});
            }
        }
        atualizarContador();
    }

    public void limpar() {
        tableModel.setRowCount(0);
        txtNome.setText("");
        txtDescricao.setText("");
        comboAportaRecursos.setSelectedIndex(0);
        atualizarContador();
    }
}
