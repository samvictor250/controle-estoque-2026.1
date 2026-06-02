package Java.controle_estoque;

import java.util.Scanner;

public class Estoque { 
    
    public static class Produto {
        String nome;
        String descricao;
        int qtdEstoque;
        double precoUnit;
        String categoria;
        int QTD_MIN;
    }

    public static Scanner input = new Scanner(System.in);
    
    public static void main(String[] args) {
        Produto[] p = new Produto[100]; 
        int totalCadastrados = 0; // O GPS da gaveta livre
        
        while (true) {
            if (!input.hasNextInt()) break;
            
            int opcao = input.nextInt();
            input.nextLine(); 
            
            if (opcao == 0) {
                break;
            } 
            else if (opcao == 1) {
                cadastrarProduto(p, totalCadastrados);
                totalCadastrados += 1;
            } 
            else if (opcao == 2) {
                listarProdutos(p, totalCadastrados);
            }
            else if (opcao == 3) {
                System.out.print("Digite a categoria para filtrar: ");
                String catBuscada = input.nextLine();
                listarPorCategoria(p, totalCadastrados, catBuscada);
            }
            else if (opcao == 4) {
                ordenarPorNome(p, totalCadastrados);
                System.out.println("Estoque ordenado por Nome (A-Z).");
            }
            else if (opcao == 5) {
                System.out.print("Digite o nome do produto para remover: ");
                String nomeRemover = input.nextLine();
                totalCadastrados = removerProduto(p, totalCadastrados, nomeRemover);
            }
            else if (opcao == 6) {
                System.out.print("Digite o nome do produto para atualizar preco: ");
                String nomeBusca = input.nextLine();
                System.out.print("Digite o novo preco: ");
                double novoPreco = input.nextDouble();
                input.nextLine(); // Descarga de buffer
                atualizarPreco(p, totalCadastrados, nomeBusca, novoPreco);
            }
            else if (opcao == 7) {
                listarSubtotalPorCategoria(p, totalCadastrados);
            }
        }
    }

    // 1 - Cadastrar
    public static void cadastrarProduto(Produto[] p, int i) {
        p[i] = new Produto();
        
        System.out.println("\n+++ Cadastro de Produto +++");
        System.out.print("Nome: ");    
        p[i].nome = input.nextLine();
        
        System.out.print("Descricao: ");
        p[i].descricao = input.nextLine();

        System.out.print("Categoria: ");
        p[i].categoria = input.nextLine();
        
        System.out.print("Quantidade em estoque: ");
        p[i].qtdEstoque = input.nextInt();

        System.out.print("Preco unitario: ");
        p[i].precoUnit = input.nextDouble();

        System.out.print("Quantidade minima: ");
        p[i].QTD_MIN = input.nextInt();
    
        // limpeza de buffer
        input.nextLine(); 
    }

    // 2 - Listar Todos
    public static void listarProdutos(Produto[] p, int limite) { 
        System.out.println("\n+++ Listagem Geral +++");
        for(int j = 0; j < limite; j++) {
            if(p[j] != null) { 
                System.out.printf("Nome: %-15s | Categoria: %-10s | Qtd: %4d | Preco: R$ %.2f\n", 
                                  p[j].nome, p[j].categoria, p[j].qtdEstoque, p[j].precoUnit);
            }
        }
    }

    // 3 - Listar por Categoria (Motor de Filtragem)
    public static void listarPorCategoria(Produto[] p, int limite, String categoriaBuscada) {
        System.out.printf("\n+++ Filtrando Categoria: %s +++\n", categoriaBuscada);
        for(int j = 0; j < limite; j++) {
            if(p[j] != null && p[j].categoria.equalsIgnoreCase(categoriaBuscada)) {
                System.out.printf("Nome: %-15s | Qtd: %4d | Preco: R$ %.2f\n", 
                                  p[j].nome, p[j].qtdEstoque, p[j].precoUnit);
            }
        }
    }

    // 4 - Ordenar por Nome (Bubble Sort Raiz)
    public static void ordenarPorNome(Produto[] p, int limite) {
        for (int i = 0; i < limite; i++) {
            for (int j = 0; j < limite - 1; j++) {
                if (p[j].nome.compareToIgnoreCase(p[j+1].nome) > 0) {
                    Produto temp = p[j];
                    p[j] = p[j+1];
                    p[j+1] = temp;
                }
            }
        }
    }

    // 5 - Remover Produto (Evita Buraco Negro na Memória)
    public static int removerProduto(Produto[] p, int limite, String nomeRemover) {
        for (int j = 0; j < limite; j++) {
            if (p[j] != null && p[j].nome.equalsIgnoreCase(nomeRemover)) {
                // Produto achado. Agora, puxa toda a fila da frente uma gaveta para trás.
                for (int k = j; k < limite - 1; k++) {
                    p[k] = p[k+1];
                }
                p[limite - 1] = null;
                System.out.println("Produto removido com sucesso.");
                return limite - 1; 
            }
        }
        System.out.println("Erro: Produto nao encontrado.");
        return limite;
    }

    // 6 - Atualizar Preço (Busca Otimizada)
    public static void atualizarPreco(Produto[] p, int limite, String nomeBusca, double novoPreco) {
        for (int j = 0; j < limite; j++) {
            if (p[j] != null && p[j].nome.equalsIgnoreCase(nomeBusca)) {
                p[j].precoUnit = novoPreco;
                System.out.println("Preco atualizado.");
                return; // Corta o laço cedo para poupar CPU
            }
        }
        System.out.println("Erro: Produto nao encontrado.");
    }

    // 7 - Listar com Subtotal por Categoria (Relatório de Quebra)
    public static void listarSubtotalPorCategoria(Produto[] p, int limite) {
        if (limite == 0) return;

        // 7.1: Ordenar temporariamente por Categoria para agrupar tudo
        for (int i = 0; i < limite; i++) {
            for (int j = 0; j < limite - 1; j++) {
                if (p[j].categoria.compareToIgnoreCase(p[j+1].categoria) > 0) {
                    Produto temp = p[j];
                    p[j] = p[j+1];
                    p[j+1] = temp;
                }
            }
        }

        System.out.println("\n+++ Relatorio de Subtotais +++");
        String categoriaAtual = p[0].categoria;
        double subtotal = 0.0;
        double totalGeral = 0.0;

        System.out.printf("\nCategoria: %s\n", categoriaAtual);

        // 7.2: Varredura calculando o financeiro
        for (int j = 0; j < limite; j++) {
            if (p[j] != null) {
                // Se a categoria mudou, imprime o subtotal da anterior e reinicia
                if (!p[j].categoria.equalsIgnoreCase(categoriaAtual)) {
                    System.out.printf(">>> Subtotal %s: R$ %.2f\n\n", categoriaAtual, subtotal);
                    categoriaAtual = p[j].categoria;
                    subtotal = 0;
                    System.out.printf("Categoria: %s\n", categoriaAtual);
                }

                double valorEstoque = p[j].qtdEstoque * p[j].precoUnit;
                subtotal += valorEstoque;
                totalGeral += valorEstoque;

                System.out.printf("%-15s | %4d * R$ %.2f = R$ %.2f\n", 
                                  p[j].nome, p[j].qtdEstoque, p[j].precoUnit, valorEstoque);
            }
        }
        // Imprime o subtotal da última categoria
        System.out.printf(">>> Subtotal %s: R$ %.2f\n", categoriaAtual, subtotal);
        System.out.printf("\n=====================================\n");
        System.out.printf("TOTAL GERAL: R$ %.2f\n", totalGeral);
    }
}