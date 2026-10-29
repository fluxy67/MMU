package mmu.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlPageStorage implements PageStorage {

    private final String url;
    private final String user;
    private final String password;

    public MySqlPageStorage(String host, int port,
                            String database, String user, String password) {

        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.user = user;
        this.password = password;
    }

    @Override
    public String readPage(int pageNumber) {

        String sql = "SELECT content FROM pages WHERE page_number = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, pageNumber);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return result.getString("content");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur de lecture MySQL : " + e.getMessage());
        }

        return null;
    }

    @Override
    public void writePage(int pageNumber, String content) {

        String sql = """
                INSERT INTO pages (page_number, content)
                VALUES (?, ?)
                ON DUPLICATE KEY UPDATE content = VALUES(content)
                """;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, pageNumber);
            statement.setString(2, content);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur d'écriture MySQL : " + e.getMessage());
        }
    }
}