package org.sistemaestoque.da0;

import org.sistemaestoque.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDA0 {

    public void salvarProduto(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Produto salvo: " + produto.getNome());
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar produto");
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY id";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                int quantidade = rs.getInt("quantidade");

                Produto produto = new Produto(id, nome, preco, quantidade);
                produtos.add(produto);
            }

            System.out.println(" | " + produtos.size() + " produtos carregados do banco");

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos!");
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }

        return produtos;
    }

    public void atualizarProduto(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, quantidade = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setInt(4, produto.getId());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Produto atualizado: " + produto.getNome());
            } else {
                System.out.println("Nenhum produto encontrado com ID: " + produto.getId());
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto!");
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    public void excluirProduto(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Produto excluído. ID: " + id);
            } else {
                System.out.println("️Nenhum produto encontrado com ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao excluir produto!");
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }

    public Produto buscarProdutoPorId(int id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Produto produto = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                int quantidade = rs.getInt("quantidade");

                produto = new Produto(id, nome, preco, quantidade);
                System.out.println("Produto encontrado: " + nome);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto!");
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }

        return produto;
    }

    public int carregarProximoId() {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 as proximo_id FROM produtos";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximoId = 1;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                proximoId = rs.getInt("proximo_id");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar próximo ID!");
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }

        return proximoId;
    }

    public List<Produto> buscarProdutosPorNome(String nome) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nomeProduto = rs.getString("nome");
                double preco = rs.getDouble("preco");
                int quantidade = rs.getInt("quantidade");

                Produto produto = new Produto(id, nomeProduto, preco, quantidade);
                produtos.add(produto);
            }

            System.out.println("" + produtos.size() + " produto(s) encontrado(s)");

        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos por nome!");
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }

        return produtos;
    }

    public int contarProdutos() {
        String sql = "SELECT COUNT(*) as total FROM produtos";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int total = 0;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao contar produtos!");
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }

        return total;
    }

    public double calcularValorTotalEstoque() {
        String sql = "SELECT SUM(preco * quantidade) as valor_total FROM produtos";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double valorTotal = 0.0;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                valorTotal = rs.getDouble("valor_total");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao calcular valor total!");
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }

        return valorTotal;
    }

    public List<Produto> listarProdutosEstoqueBaixo(int quantidadeMinima) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE quantidade <= ? ORDER BY quantidade";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quantidadeMinima);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                int quantidade = rs.getInt("quantidade");

                Produto produto = new Produto(id, nome, preco, quantidade);
                produtos.add(produto);
            }

            System.out.println("" + produtos.size() + " produto(s) com estoque baixo");

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos com estoque baixo!");
            e.printStackTrace();
        } finally {
            fecharRecursos(conn, stmt, rs);
        }

        return produtos;
    }

    private void fecharRecursos(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseConnection.closeConnection(conn);
        } catch (SQLException e) {
            System.err.println(" Erro ao fechar recursos!");
            e.printStackTrace();
        }
    }
}