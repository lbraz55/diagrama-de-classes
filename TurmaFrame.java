package view;

import controller.Controlador;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Janela principal da turma.
 */
public class TurmaFrame extends JFrame {

    private final JButton listarAlunosButton = new JButton("Listar alunos");
    private final JButton resultadosButton = new JButton("Resultados");
    private final JButton novoAlunoButton = new JButton("Novo aluno");
    private final Controlador controlador;

    public TurmaFrame(Controlador controlador) {
        super("Turma");
        this.controlador = controlador;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 8, 8));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(listarAlunosButton);
        add(resultadosButton);
        add(novoAlunoButton);

        listarAlunosButton.addActionListener(e -> listarAlunosButtonActionPerformed());
        resultadosButton.addActionListener(e -> resultadosButtonActionPerformed());
        novoAlunoButton.addActionListener(e -> novoAlunoButtonActionPerformed());

        setSize(280, 220);
        setLocationRelativeTo(null);
    }

    private void listarAlunosButtonActionPerformed() {
        controlador.abrirLista();
    }

    private void resultadosButtonActionPerformed() {
        if (controlador.getAlunos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum aluno cadastrado.");
            return;
        }
        String mensagem = String.format("Alunos cadastrados: %d%nMédia da turma: %.2f",
                controlador.getAlunos().size(), controlador.calcularMedia());
        JOptionPane.showMessageDialog(this, mensagem, "Resultados", JOptionPane.INFORMATION_MESSAGE);
    }

    private void novoAlunoButtonActionPerformed() {
        controlador.abrirNovoAluno(this);
    }
}
