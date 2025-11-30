package org.sistemaestoque.view;

import org.sistemaestoque.da0.ProdutoDA0;
import org.sistemaestoque.model.Produto;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    private ProdutoDA0 produtoDAO;
    private TableView<Produto> tabelaProdutos;
    private ObservableList<Produto> listaProdutos;

    // Aqui fica os campo de entrada
    private TextField campoId;
    private TextField campoNome;
    private TextField campoPreco;
    private TextField campoQuantidade;

    @Override
    public void start(Stage primaryStage) {
        produtoDAO = new ProdutoDA0();
        listaProdutos = FXCollections.observableArrayList();

        primaryStage.setTitle("Sistema de Gerenciamento de Estoque");

        // Aqui é o layout principal
        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setPadding(new Insets(10));

        // Aqui há o painel do formulário que está localizado no topo
        VBox painelFormulario = criarPainelFormulario();
        layoutPrincipal.setTop(painelFormulario);

        // Tabela dos produtos --> está no centro do layout
        tabelaProdutos = criarTabelaProdutos();
        layoutPrincipal.setCenter(tabelaProdutos);

        // Aqui carrega os produtos que já existe
        atualizarTabela();

        // Aqui cria uma cena e ele mostra
        Scene scene = new Scene(layoutPrincipal, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox criarPainelFormulario() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(10));
        painel.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc;");

        // Grid para organizar os campos abaixo
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Aqui fica o campo ID --> Somente para ler
        Label labelId = new Label("ID:");
        campoId = new TextField();
        campoId.setEditable(false);
        campoId.setPromptText("Automático");
        campoId.setPrefWidth(100);

        // Aqui fica o campo Nome
        Label labelNome = new Label("Nome:");
        campoNome = new TextField();
        campoNome.setPromptText("Digite o nome do produto");
        campoNome.setPrefWidth(250);

        // Aqui fica o campo do preço
        Label labelPreco = new Label("Preço:");
        campoPreco = new TextField();
        campoPreco.setPromptText("0.00");
        campoPreco.setPrefWidth(100);

        // Aqui fica o campo quantidade
        Label labelQuantidade = new Label("Quantidade:");
        campoQuantidade = new TextField();
        campoQuantidade.setPromptText("0");
        campoQuantidade.setPrefWidth(100);

        // Aqui fica a parte dos GRID
        grid.add(labelId, 0, 0);
        grid.add(campoId, 1, 0);
        grid.add(labelNome, 2, 0);
        grid.add(campoNome, 3, 0);
        grid.add(labelPreco, 0, 1);
        grid.add(campoPreco, 1, 1);
        grid.add(labelQuantidade, 2, 1);
        grid.add(campoQuantidade, 3, 1);

        // Aqui fica o painel dos botões
        HBox painelBotoes = new HBox(10);
        painelBotoes.setPadding(new Insets(10, 0, 0, 0));

        Button btnAdicionar = new Button("Adicionar Produto");
        btnAdicionar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnAdicionar.setOnAction(e -> adicionarProduto());

        Button btnAtualizar = new Button("Atualizar Produto");
        btnAtualizar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        btnAtualizar.setOnAction(e -> atualizarProduto());

        Button btnExcluir = new Button("Excluir Produto");
        btnExcluir.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        btnExcluir.setOnAction(e -> excluirProduto());

        Button btnLimpar = new Button("Limpar Campos");
        btnLimpar.setOnAction(e -> limparCampos());

        painelBotoes.getChildren().addAll(btnAdicionar, btnAtualizar, btnExcluir, btnLimpar);

        painel.getChildren().addAll(grid, painelBotoes);
        return painel;
    }

    // Atualiza um produto que já existe
    private void atualizarProduto() {
        try {
            if (campoId.getText().isEmpty()) {
                mostrarAlerta("Erro", "Selecione um produto na tabela para atualizar!");
                return;
            }

            int id = Integer.parseInt(campoId.getText().trim());
            String nome = campoNome.getText().trim();
            double preco = Double.parseDouble(campoPreco.getText().trim());
            int quantidade = Integer.parseInt(campoQuantidade.getText().trim());

            if (nome.isEmpty()) {
                mostrarAlerta("Erro", "O nome do produto não pode estar vazio!");
                return;
            }

            Produto produtoAtualizado = new Produto(id, nome, preco, quantidade);
            produtoDAO.atualizarProduto(produtoAtualizado);
            atualizarTabela();
            limparCampos();

            mostrarAlerta("Sucesso", "Produto atualizado com sucesso!");

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Valores inválidos! Verifique os campos.");
        }
    }

    /**
     * Cria a tabela de produtos
     */
    private TableView<Produto> criarTabelaProdutos() {
        TableView<Produto> tabela = new TableView<>();

        // Coluna ID
        TableColumn<Produto, Integer> colunaId = new TableColumn<>("ID");
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaId.setPrefWidth(50);

        // Coluna Nome
        TableColumn<Produto, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaNome.setPrefWidth(300);

        // Coluna Preço
        TableColumn<Produto, Double> colunaPreco = new TableColumn<>("Preço (R$)");
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colunaPreco.setPrefWidth(150);

        // Coluna Quantidade
        TableColumn<Produto, Integer> colunaQuantidade = new TableColumn<>("Quantidade");
        colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colunaQuantidade.setPrefWidth(150);

        tabela.getColumns().addAll(colunaId, colunaNome, colunaPreco, colunaQuantidade);
        tabela.setItems(listaProdutos);

        // Evento de clique na linha da tabela
        tabela.setOnMouseClicked(event -> {
            Produto produtoSelecionado = tabela.getSelectionModel().getSelectedItem();
            if (produtoSelecionado != null) {
                preencherCampos(produtoSelecionado);
            }
        });

        return tabela;
    }

    /**
     * Adiciona um novo produto
     */
    private void adicionarProduto() {
        try {
            String nome = campoNome.getText().trim();
            double preco = Double.parseDouble(campoPreco.getText().trim());
            int quantidade = Integer.parseInt(campoQuantidade.getText().trim());

            if (nome.isEmpty()) {
                mostrarAlerta("Erro", "O nome do produto não pode estar vazio!");
                return;
            }

            int novoId = produtoDAO.carregarProximoId();
            Produto novoProduto = new Produto(novoId, nome, preco, quantidade);

            produtoDAO.salvarProduto(novoProduto);
            atualizarTabela();
            limparCampos();

            mostrarAlerta("Sucesso", "Produto adicionado com sucesso!");

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Preço e Quantidade devem ser números válidos!");
        }
    }

    // Aqui exclui o produto
    private void excluirProduto() {
        if (campoId.getText().isEmpty()) {
            mostrarAlerta("Erro", "Selecione um produto na tabela para excluir!");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir este produto?");
        confirmacao.setContentText("Esta ação não pode ser desfeita.");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                int id = Integer.parseInt(campoId.getText().trim());
                produtoDAO.excluirProduto(id);
                atualizarTabela();
                limparCampos();
                mostrarAlerta("Sucesso", "Produto excluído com sucesso!");
            }
        });
    }

    // Atualiza a tabela com os produtos dos arquivos
    private void atualizarTabela() {
        listaProdutos.clear();
        listaProdutos.addAll(produtoDAO.listarProdutos());
    }

    // Parte para preencher campos
    private void preencherCampos(Produto produto) {
        campoId.setText(String.valueOf(produto.getId()));
        campoNome.setText(produto.getNome());
        campoPreco.setText(String.valueOf(produto.getPreco()));
        campoQuantidade.setText(String.valueOf(produto.getQuantidade()));
    }

    // Aqui limpa todos os campo do Formulário
    private void limparCampos() {
        campoId.clear();
        campoNome.clear();
        campoPreco.clear();
        campoQuantidade.clear();
        tabelaProdutos.getSelectionModel().clearSelection();
    }

    // Aqui mostrará um alerta para o usuário
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}