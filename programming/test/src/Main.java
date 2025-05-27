import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/test";
        String user = "grisa";      // Замените на вашего пользователя
        String password = "123";    // Замените на реальный пароль

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            System.out.println("✅ Подключение к БД 'test' успешно!");

            // Пример запроса (если таблица существует)
            ResultSet rs = stmt.executeQuery("SELECT * FROM information_schema.tables LIMIT 5;");
            while (rs.next()) {
                System.out.println(rs.getString("table_name"));
            }

        } catch (Exception e) {
            System.err.println("❌ Ошибка подключения: " + e.getMessage());
            e.printStackTrace();  // Для деталей
        }
    }
}