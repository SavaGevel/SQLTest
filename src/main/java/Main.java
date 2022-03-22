import java.sql.*;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/skillbox?characterEncoding=utf8";
        String login = "root";
        String pass = "23242526Tyu";
        int year = 2018;

        try (Connection connection = DriverManager.getConnection(url, login, pass)) {

            String query = """
                    SELECT course_name ,
                    ((SELECT (SELECT COUNT(*) FROM PurchaseList pl WHERE pl.course_name=pl3.course_name) /
                    (SELECT MONTH(MAX(subscription_date)) - MONTH (MIN(subscription_date)) + 1
                    FROM PurchaseList pl2 WHERE pl2.course_name=pl3.course_name))) as average_purchase_per_month
                    FROM PurchaseList pl3 WHERE YEAR(pl3.subscription_date) = ? GROUP BY pl3.course_name""";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, year);

            ResultSet result = preparedStatement.executeQuery();

            System.out.println("Название курса | среднее количество покупок в месяц" );

            while(result.next()) {
                System.out.println(result.getString("course_name") + " | " + result.getString("average_purchase_per_month"));
            }

            result.close();
            preparedStatement.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
