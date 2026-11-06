package view;

import controller.Controlador;
import model.Aluno;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Janela (modal) para cadastrar um novo aluno ou editar a nota de um aluno existente.
 */
public class AlunoFrame extends JDialog {

    private final JTextField nomeTextField = new JTextField(20);
    private final JTextField notaTextField = new JTextField(20);
    private final JButton okButton = new JButton("OK");
    private final JButton cancelarButton = new JButton("Cancelar");
    private final Controlador controlador;
    private final int indice;
    private final boolean editando;
    private Aluno aluno;

    /**
     * @param indice posição do aluno na lista, ou -1 para cadastrar um novo aluno
     */
    public AlunoFrame(Window dono, Controlador controlador, int indice) {
        super(dono, ModalityType.APPLICATION_MODAL);
        this.controlador = controlador;
        this.indice = indice;
        this.editando = indice >= 0;

        setTitle(editando ? "Editar nota" : "Novo aluno");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel campos = new JPanel(new GridLayout(2, 2, 8, 8));
        campos.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        campos.add(new JLabel("Nome:"));
        campos.add(nomeTextField);
        campos.add(new JLabel("Nota (0 a 10):"));
        campos.add(notaTextField);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(okButton);
        botoes.add(cancelarButton);

        add(campos, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        okButton.addActionListener(e -> okButtonActionPerformed());
        cancelarButton.addActionListener(e -> cancelarButtonActionPerformed());
        getRootPane().setDefaultButton(okButton);

        carregarDados();

        pack();
        setLocationRelativeTo(dono);
    }

    private void carregarDados() {
        if (editando) {
            aluno = controlador.getAlunos().get(indice);
            nomeTextField.setText(aluno.getNome());
            nomeTextField.setEditable(false); // só a nota pode ser editada
            notaTextField.setText(String.valueOf(aluno.getNota()));
        }
    }

    private void okButtonActionPerformed() {
        try {
            double nota = Double.parseDouble(notaTextField.getText().trim().replace(',', '.'));
            if (editando) {
                controlador.editarNota(indice, nota);
            } else {
                controlador.cadastrar(nomeTextField.getText(), nota);
            }
            dispose();
        } catch (NumberFormatException e) {
            mostrarErro("Digite uma nota numérica válida.");
        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void cancelarButtonActionPerformed() {
        dispose();
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public Aluno getAluno() {
        return aluno;
    }

    public boolean isEditando() {
        return editando;
    }
}
