package springbook.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));
        user.setEmail(rs.getString("email"));
        return user;
    };

    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(User user) {

        jdbcTemplate.update("INSERT INTO users(id, name, password, level, login, recommend, email) VALUES (?, ?, ?, ?, ?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getLevel().intValue(),
                user.getLogin(),
                user.getRecommend(),
                user.getEmail()
        );
    }

    @Override
    public User get(String id) {

        return this.jdbcTemplate.queryForObject("SELECT  * FROM users WHERE id = ?", new Object[]{id}, userRowMapper);
    }

    @Override
    public List<User> getAll() {

        return this.jdbcTemplate.query("SELECT * FROM users ORDER BY id ASC", userRowMapper);
    }

    @Override
    public void deleteAll() {

        jdbcTemplate.update("DELETE FROM users");
    }

    @Override
    public int getCount() {

        return this.jdbcTemplate.queryForObject("SELECT count(*) FROM users", (rs, rowNum) -> rs.getInt(1));
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(
                "UPDATE users set name = ?" +
                        ", password = ?" +
                        ", level = ?" +
                        ", login = ?" +
                        ", recommend = ?" +
                        ", email = ?" +
                        " WHERE id = ?",
                user.getName(),
                user.getPassword(),
                user.getLevel().intValue(),
                user.getLogin(),
                user.getRecommend(),
                user.getEmail(),
                user.getId()
        );
    }
}
