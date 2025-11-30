package org.sistemaestoque.da0;

import org.sistemaestoque.model.Produto;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDA0 {
    private static final String ARQUIVO_CSV = "produtos.csv";
    private Path caminhoArquivo;

    public ProdutoDA0() {
        this.caminhoArquivo = Paths.get(ARQUIVO_CSV);
        criarArquivoSeNaoExistir();
    }

    private void criarArquivoSeNaoExistir() {
        try {
            if (!Files.exists(caminhoArquivo)) {
                Files.createFile(caminhoArquivo);
                System.out.println("O arquivo CSV foi criado!: " + ARQUIVO_CSV);
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo CSV: " + e.getMessage());
        }
    }

    public void salvarProduto(Produto produto) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV, true))) {
            String linha = produto.getId() + "," +
                    produto.getNome() + "," +
                    produto.getPreco() + "," +
                    produto.getQuantidade();
            writer.write(linha);
            writer.newLine();
            System.out.println("Produto salvo: " + produto.getNome());
        } catch (IOException e) {
            System.err.println("Erro ao salvar produto: " + e.getMessage());
        }
    }

    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(",");
                    if (dados.length == 4) {
                        int id = Integer.parseInt(dados[0]);
                        String nome = dados[1];
                        double preco = Double.parseDouble(dados[2]);
                        int quantidade = Integer.parseInt(dados[3]);

                        produtos.add(new Produto(id, nome, preco, quantidade));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter dados: " + e.getMessage());
        }

        return produtos;
    }

    public void atualizarProduto(Produto produtoAtualizado) {
        List<Produto> produtos = listarProdutos();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            for (Produto produto : produtos) {
                if (produto.getId() == produtoAtualizado.getId()) {
                    // Escreve o produto atualizado
                    String linha = produtoAtualizado.getId() + "," +
                            produtoAtualizado.getNome() + "," +
                            produtoAtualizado.getPreco() + "," +
                            produtoAtualizado.getQuantidade();
                    writer.write(linha);
                } else {
                    // Mantém o produto original
                    String linha = produto.getId() + "," +
                            produto.getNome() + "," +
                            produto.getPreco() + "," +
                            produto.getQuantidade();
                    writer.write(linha);
                }
                writer.newLine();
            }
            System.out.println("Produto atualizado: " + produtoAtualizado.getNome());
        } catch (IOException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    public void excluirProduto(int id) {
        List<Produto> produtos = listarProdutos();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            for (Produto produto : produtos) {
                if (produto.getId() != id) {
                    String linha = produto.getId() + "," +
                            produto.getNome() + "," +
                            produto.getPreco() + "," +
                            produto.getQuantidade();
                    writer.write(linha);
                    writer.newLine();
                }
            }
            System.out.println("Produto excluído. ID: " + id);
        } catch (IOException e) {
            System.err.println("Erro ao excluir produto: " + e.getMessage());
        }
    }

    public int carregarProximoId() {
        List<Produto> produtos = listarProdutos();

        if (produtos.isEmpty()) {
            return 1;
        }

        int maiorID = 0;
        for (Produto produto : produtos ) {
            if (produto.getId() > maiorID) {
                maiorID = produto.getId();
            }
        }

        return maiorID + 1;
    }
}
