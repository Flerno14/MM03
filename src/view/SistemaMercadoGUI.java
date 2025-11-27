package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SistemaMercadoGUI extends JFrame {

    private JTextField n1, n2, n3;
    private JTable tab;
    private DefaultTableModel mdl;
    private ProdutoDAO d;
    private int selecionado = -1;

    public SistemaMercadoGUI() {

        d = new ProdutoDAO();

        setTitle("Mini Mercado (V1)");
        setSize(900, 500); // tamanho estranho
        setLayout(null);   // LAYOUT HORRÍVEL DE PROPÓSITO
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ABA "fake" feita na mão (trabalho ruim mesmo)
        JButton btnProd = new JButton("PRODUTOS");
        JButton btnRel = new JButton("RELATÓRIOS");

        btnProd.setBounds(10, 5, 120, 25);
        btnRel.setBounds(140, 5, 120, 25);

        add(btnProd);
        add(btnRel);

        JPanel telaProd = telaProdutos();
        telaProd.setBounds(10, 40, 860, 420);
        add(telaProd);

        JPanel telaRel = telaRelatorios();
        telaRel.setBounds(10, 40, 860, 420);
        telaRel.setVisible(false);
        add(telaRel);

        btnProd.addActionListener(e -> {
            telaProd.setVisible(true);
            telaRel.setVisible(false);
        });

        btnRel.addActionListener(e -> {
            telaProd.setVisible(false);
            telaRel.setVisible(true);
        });

        atualizar();
    }

    // TELA DE PRODUTOS HORRÍVEL
    private JPanel telaProdutos() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(225, 235, 255)); // cor nada a ver

        JLabel l1 = new JLabel("Nome do Produto:");
        l1.setBounds(20, 10, 150, 25);
        p.add(l1);

        n1 = new JTextField();
        n1.setBounds(150, 10, 200, 25);
        p.add(n1);

        JLabel l2 = new JLabel("Preço:");
        l2.setBounds(20, 50, 150, 25);
        p.add(l2);

        n2 = new JTextField();
        n2.setBounds(150, 50, 200, 25);
        p.add(n2);

        JLabel l3 = new JLabel("Estoque:");
        l3.setBounds(20, 90, 150, 25);
        p.add(l3);

        n3 = new JTextField();
        n3.setBounds(150, 90, 200, 25);
        p.add(n3);

        JButton salvar = new JButton("Salvar");
        JButton excluir = new JButton("Excluir");
        JButton limpar = new JButton("Limpar");

        salvar.setBounds(380, 10, 120, 25);
        excluir.setBounds(380, 45, 120, 25);
        limpar.setBounds(380, 80, 120, 25);

        p.add(salvar);
        p.add(excluir);
        p.add(limpar);

        mdl = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço", "Estoque"}, 0);
        tab = new JTable(mdl);

        JScrollPane sp = new JScrollPane(tab);
        sp.setBounds(20, 150, 820, 250);
        p.add(sp);

        salvar.addActionListener(e -> salvarOuEditar());
        excluir.addActionListener(e -> excluir());
        limpar.addActionListener(e -> limpaCampos());

        tab.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = tab.getSelectedRow();
                selecionado = (int) mdl.getValueAt(i, 0);
                n1.setText(mdl.getValueAt(i, 1).toString());
                n2.setText(mdl.getValueAt(i, 2).toString());
                n3.setText(mdl.getValueAt(i, 3).toString());
            }
        });

        return p;
    }

    // TELA DE RELATÓRIOS MAIS FEIA AINDA
    private JPanel telaRelatorios() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(255, 240, 220)); // cor feia também

        JTextArea area = new JTextArea();
        area.setBounds(20, 60, 820, 320);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setEditable(false);

        JScrollPane sp = new JScrollPane(area);
        sp.setBounds(20, 60, 820, 320);

        JButton r1 = new JButton("Estoque");
        JButton r2 = new JButton("Catálogo");

        r1.setBounds(20, 15, 120, 30);
        r2.setBounds(150, 15, 120, 30);

        r1.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=== RELATÓRIO ESTOQUE ===\n");
            sb.append("NOME                     | QTD\n");
            sb.append("-----------------------------------\n");
            for (Produto x : d.listarTodos()) {
                sb.append(String.format("%-25s | %d\n", x.getNome(), x.getEstoque()));
            }
            area.setText(sb.toString());
        });

        r2.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=== CATÁLOGO ===\n");
            sb.append("NOME                     | PREÇO\n");
            sb.append("-----------------------------------\n");
            for (Produto x : d.listarTodos()) {
                if (x.getEstoque() > 0)
                    sb.append(String.format("%-25s | R$ %.2f\n", x.getNome(), x.getPreco()));
            }
            area.setText(sb.toString());
        });

        p.add(r1);
        p.add(r2);
        p.add(sp);

        return p;
    }

    private void salvarOuEditar() {
        try {
            Produto a = new Produto();
            a.setNome(n1.getText());
            a.setPreco(Double.parseDouble(n2.getText().replace(",", ".")));
            a.setEstoque(Integer.parseInt(n3.getText()));

            if (selecionado == -1) {
                d.salvar(a);
                JOptionPane.showMessageDialog(this, "Salvo!");
            } else {
                a.setId(selecionado);
                d.atualizar(a);
                JOptionPane.showMessageDialog(this, "Atualizado!");
            }

            limpaCampos();
            atualizar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void excluir() {
        if (selecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione algo!");
            return;
        }

        try {
            d.excluir(selecionado);
            JOptionPane.showMessageDialog(this, "Removido!");
            limpaCampos();
            atualizar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void limpaCampos() {
        n1.setText("");
        n2.setText("");
        n3.setText("");
        selecionado = -1;
    }

    private void atualizar() {
        mdl.setRowCount(0);
        for (Produto p : d.listarTodos()) {
            mdl.addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getEstoque()});
        }
    }
}
