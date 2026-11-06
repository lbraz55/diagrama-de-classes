package controller;

import model.Aluno;
import view.AlunoFrame;
import view.CadastroeditarMenuItemFrame;
import view.TurmaFrame;

import java.awt.Window;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controla os dados dos alunos (cadastro, edição, média, persistência em arquivo)
 * e a abertura das janelas da interface gráfica.
 */
public class Controlador {

    private static final String ARQUIVO = "alunos_notas.txt";

    private final List<Aluno> alunos = new ArrayList<>();
    private TurmaFrame turmaFrame;
    private CadastroeditarMenuItemFrame listaFrame;

    public Controlador() {
        carregarDados();
    }

    // ---------------- Regras de negócio ----------------

    public List<Aluno> getAlunos() {
        return Collections.unmodifiableList(alunos);
    }

    public void cadastrar(String nome, double nota) {
        validarNome(nome);
        validarNota(nota);
        alunos.add(new Aluno(nome.trim(), nota));
        salvarDados();
    }

    public void editarNota(int indice, double novaNota) {
        if (indice < 0 || indice >= alunos.size()) {
            throw new IllegalArgumentException("Índice inválido.");
        }
        validarNota(novaNota);
        alunos.get(indice).setNota(novaNota);
        salvarDados();
    }

    public double calcularMedia() {
        if (alunos.isEmpty()) {
            return 0;
        }
        double soma = 0;
        for (Aluno aluno : alunos) {
            soma += aluno.getNota();
        }
        return soma / alunos.size();
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ficar vazio.");
        }
    }

    private void validarNota(double nota) {
        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException("A nota deve estar entre 0 e 10.");
        }
    }

    // ---------------- Persistência ----------------

    public void carregarDados() {
        alunos.clear();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return; // primeira execução: ainda não há dados salvos
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                int separador = linha.lastIndexOf(':');
                if (separador <= 0) {
                    continue; // linha inválida
                }
                try {
                    String nome = linha.substring(0, separador);
                    double nota = Double.parseDouble(linha.substring(separador + 1).trim());
                    alunos.add(new Aluno(nome, nota));
                } catch (NumberFormatException e) {
                    System.out.println("Linha ignorada (nota inválida): " + linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados dos alunos: " + e.getMessage());
        }
    }

    public void salvarDados() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Aluno aluno : alunos) {
                writer.write(aluno.getNome() + ":" + aluno.getNota());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados dos alunos: " + e.getMessage());
        }
    }

    // ---------------- Interface gráfica ----------------

    public void iniciar() {
        turmaFrame = new TurmaFrame(this);
        turmaFrame.setVisible(true);
    }

    public void abrirLista() {
        if (listaFrame == null || !listaFrame.isDisplayable()) {
            listaFrame = new CadastroeditarMenuItemFrame(this);
            listaFrame.setVisible(true);
        } else {
            listaFrame.atualizarDados();
            listaFrame.toFront();
        }
    }

    public void abrirNovoAluno(Window dono) {
        new AlunoFrame(dono, this, -1).setVisible(true); // janela modal: espera fechar
        atualizarLista();
    }

    public void abrirEdicao(Window dono, int indice) {
        new AlunoFrame(dono, this, indice).setVisible(true);
        atualizarLista();
    }

    private void atualizarLista() {
        if (listaFrame != null && listaFrame.isDisplayable()) {
            listaFrame.atualizarDados();
        }
    }
}
