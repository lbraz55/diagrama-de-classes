package view;

import controller.Controlador;
import model.Aluno;

import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * Janela com a lista de alunos e um menu para cadastrar ou editar.
 */
public class CadastroeditarMenuItemFrame extends JFrame {

    private final JMenuItem cadastrarMenuItem = new JMenuItem("Cadastrar aluno");
    private final JMenuItem editarMenuItem = new JMenuItem("Editar nota");
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private final JList<String> alunosList = new JList<>(modeloLista);
    private final Controlador controlador;

    public CadastroeditarMenuItemFrame(Controlador controlador) {
        super("Alunos");
        this.controlador = controlador;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JMenu menu = new JMenu("Aluno");
        menu.add(cadastrarMenuItem);
        menu.add(editarMenuItem);
        JMenuBar barra = new JMenuBar();
        barra.add(menu);
        setJMenuBar(barra);

        alunosList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(alunosList), BorderLayout.CENTER);

        cadastrarMenuItem.addActionListener(e -> cadastrarMenuItemActionPerformed());
        editarMenuItem.addActionListener(e -> editarMenuItemActionPerformed());

        atualizarDados();

        setSize(320, 360);
        setLocationRelativeTo(null);
    }

    /** Recarrega a lista exibida a partir dos dados do controlador. */
    public void atualizarDados() {
        modeloLista.clear();
        for (Aluno aluno : controlador.getAlunos()) {
            modeloLista.addElement(aluno.toString());
        }
    }

    private void cadastrarMenuItemActionPerformed() {
        controlador.abrirNovoAluno(this);
    }

    private void editarMenuItemActionPerformed() {
        int indice = alunosList.getSelectedIndex();
        if (indice < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na lista primeiro.");
            return;
        }
        controlador.abrirEdicao(this, indice);
    }
}
