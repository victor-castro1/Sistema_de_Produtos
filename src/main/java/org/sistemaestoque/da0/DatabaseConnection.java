package org.sistemaestoque.da0;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe do Banco de Dados para conexão
public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/estoque_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin1"; // Senha do Postgres fica aqui

    public static Connection getConnection() {
        try {

            Class.forName("org.postgresql.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexão com PostgreSQL foi estabelecida");
            return connection;

        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do PostgreSQL não encontrado!");
            System.err.println("Verifique se a dependência está no pom.xml");
            e.printStackTrace();
            return null;

        } catch (SQLException e) {
            System.err.println(" Erro ao conectar ao banco de dados");
            System.err.println("Verifique: URL, usuário, senha e se o PostgreSQL está rodando");
            e.printStackTrace();
            return null;
        }
    }

    // Classe para fechar conexão
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão fechada");
            } catch (SQLException e) {
                System.err.println(" Erro ao fechar conexão!");
                e.printStackTrace();
            }
        }
    }

    // Função: Testar conexão
    public static void testarConexao() {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Teste de conexão ");
            closeConnection(conn);
        } else {
            System.out.println("Falha no teste de conexão");
        }
    }

    // Main: Aplicar o testar a conexão
    public static void main(String[] args) {
        testarConexao();
    }
}