package data;

import domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CustomerDAO {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDAO.class);
    private final String url;
    private final String user;
    private final String password;

    public CustomerDAO() {
        Properties props = new Properties();
        try (var in = getClass().getClassLoader().getResourceAsStream("app.properties")) {
            if (in == null) throw new RuntimeException("app.properties not found on classpath");
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load DB config", e);
        }
        url = props.getProperty("db.url");
        user = props.getProperty("db.user");
        password = props.getProperty("db.password");
    }

    private Connection getConn() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void create(Customer c) {
        String sql = "INSERT INTO customer (name, email, phone) VALUES (?, ?, ?)";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhone());
            ps.executeUpdate();
            logger.info("Created customer: {}", c.getName());
        } catch (SQLException e) {
            logger.error("Create failed", e);
        }
    }

    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer ORDER BY id DESC";
        try (Connection conn = getConn(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Customer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            logger.error("FindAll failed", e);
        }
        return list;
    }

    public void update(Customer c) {
        String sql = "UPDATE customer SET name=?, email=?, phone=? WHERE id=?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhone());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
            logger.info("Updated customer: {}", c.getId());
        } catch (SQLException e) {
            logger.error("Update failed", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM customer WHERE id=?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            logger.info("Deleted customer: {}", id);
        } catch (SQLException e) {
            logger.error("Delete failed", e);
        }
    }

    public void exportToCSV(String filename) throws Exception {
        List<Customer> customers = findAll();
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("id,name,email,phone,created_at\n");
            for (Customer c : customers) {
                writer.write(String.format("%d,%s,%s,%s,%s\n",
                    c.getId(), escape(c.getName()), escape(c.getEmail()), escape(c.getPhone()), c.getCreatedAt()));
            }
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replaceAll(",", "\\,").replaceAll("\n", " ");
    }
}
