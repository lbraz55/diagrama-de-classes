package console;

import controller.Controlador;
import model.Aluno;

import java.util.List;
import java.util.Scanner;

/**
 * Versão em console (menu de texto) do cadastro de alunos.
 */
public class CadastroAlunos {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Controlador controlador = new Controlador(); // já carrega os dados salvos

        boolean executando = true;
        while (executando) {
            System.out.println("\nMenu:");
            System.out.println("1. Cadastrar aluno e nota");
            System.out.println("2. Listar alunos e notas");
            System.out.println("3. Editar nota de aluno");
            System.out.println("4. Calcular média dos alunos");
            System.out.println("5. Salvar e sair");
            System.out.print("Opção: ");

            Integer opcao = lerInteiro(scanner);
            if (opcao == null) {
                System.out.println("Digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    cadastrar(scanner, controlador);
                    break;
                case 2:
                    listar(controlador);
                    break;
                case 3:
                    editar(scanner, controlador);
                    break;
                case 4:
                    media(controlador);
                    break;
                case 5:
                    controlador.salvarDados();
                    System.out.println("Dados salvos. Encerrando o programa.");
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
            }
        }

        scanner.close();
    }

    private static void cadastrar(Scanner scanner, Controlador controlador) {
        System.out.print("Digite o nome do aluno: ");
        String nome = scanner.nextLine();
        System.out.print("Digite a nota do aluno (0 a 10): ");
        Double nota = lerDecimal(scanner);
        if (nota == null) {
            System.out.println("Nota inválida.");
            return;
        }
        try {
            controlador.cadastrar(nome, nota);
            System.out.println("Aluno e nota cadastrados com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void listar(Controlador controlador) {
        List<Aluno> alunos = controlador.getAlunos();
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }
        System.out.println("Lista de alunos e notas:");
        for (int i = 0; i < alunos.size(); i++) {
            System.out.println((i + 1) + ". " + alunos.get(i));
        }
    }

    private static void editar(Scanner scanner, Controlador controlador) {
        if (controlador.getAlunos().isEmpty()) {
            System.out.println("Nenhum aluno cadastrado para editar a nota.");
            return;
        }
        listar(controlador);
        System.out.print("Digite o número do aluno cuja nota deseja editar: ");
        Integer numero = lerInteiro(scanner);
        if (numero == null || numero < 1 || numero > controlador.getAlunos().size()) {
            System.out.println("Número inválido.");
            return;
        }
        System.out.print("Digite a nova nota para o aluno (0 a 10): ");
        Double novaNota = lerDecimal(scanner);
        if (novaNota == null) {
            System.out.println("Nota inválida.");
            return;
        }
        try {
            controlador.editarNota(numero - 1, novaNota);
            System.out.println("Nota do aluno editada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void media(Controlador controlador) {
        if (controlador.getAlunos().isEmpty()) {
            System.out.println("Nenhum aluno cadastrado para calcular a média.");
        } else {
            System.out.printf("A média dos alunos é: %.2f%n", controlador.calcularMedia());
        }
    }

    /** Lê uma linha e converte para int; retorna null se não for um número. */
    private static Integer lerInteiro(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Lê uma linha e converte para double (aceita vírgula ou ponto); retorna null se inválido. */
    private static Double lerDecimal(Scanner scanner) {
        try {
            return Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
