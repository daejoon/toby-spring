package springbook.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.util.List;

public class UserDao {

    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));
        return user;
    };

    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {

        jdbcTemplate.update("INSERT INTO users(id, name, password, level, login, recommend) VALUES (?, ?, ?, ?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getLevel().intValue(),
                user.getLogin(),
                user.getRecommend()
        );
    }

    public User get(String id) {

        return this.jdbcTemplate.queryForObject("SELECT  * FROM users WHERE id = ?", new Object[]{id}, userRowMapper);
    }

    public List<User> getAll() {

        return this.jdbcTemplate.query("SELECT * FROM users ORDER BY id ASC", userRowMapper);
    }

    public void deleteAll() {

        jdbcTemplate.update("DELETE FROM users");
    }

    public int getCount() {

        return this.jdbcTemplate.queryForObject("SELECT count(*) FROM users", (rs, rowNum) -> rs.getInt(1));
    }

    public void update(User user) {
        this.jdbcTemplate.update(
                "UPDATE users set name = ?" +
                        ", password = ?" +
                        ", level = ?" +
                        ", login = ?" +
                        ", recommend = ?" +
                        " WHERE id = ?",
                user.getName(),
                user.getPassword(),
                user.getLevel().intValue(),
                user.getLogin(),
                user.getRecommend(),
                user.getId()
        );
    }
}
