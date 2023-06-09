package edu.geekhub.coursework.users;

import edu.geekhub.coursework.users.interfaces.UserRepository;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserValidator validator;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String FETCH_ALL_USERS = """
        SELECT * FROM Users
        """;
    private static final String FETCH_USERS_OF_ROLE_BY_PAGE_AND_INPUT = """
        SELECT * FROM Users
        WHERE CONCAT(REPLACE(Users.firstName, ' ', ''), REPLACE(Users.lastName, ' ', ''))
        ILIKE REPLACE(:input, ' ', '')
        AND role=:role
        LIMIT :limit
        OFFSET :limit * :pageNumber
        """;
    private static final String INSERT_USER = """
        INSERT INTO Users(firstName, lastName, password, email, role)
        VALUES (:firstName, :lastName, :password, :email, :role)
        """;
    private static final String FETCH_USER_BY_ID = """
        SELECT * FROM Users WHERE id=:id
        """;
    private static final String FETCH_USER_BY_EMAIL = """
        SELECT * FROM Users WHERE email=:email
        """;
    private static final String DELETE_USER_BY_ID = """
        DELETE FROM Users WHERE id=:id
        """;
    private static final String UPDATE_USER_BY_ID = """
        UPDATE Users SET
        firstName=:firstName, lastName=:lastName, password=:password, email=:email, role=:role
        WHERE id=:id
        """;
    private static final String UPDATE_USER_WITHOUT_PASS_BY_ID = """
        UPDATE Users SET
        firstName=:firstName, lastName=:lastName, email=:email, role=:role
        WHERE id=:id
        """;

    public UserRepositoryImpl(UserValidator validator, NamedParameterJdbcTemplate jdbcTemplate) {
        this.validator = validator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query(FETCH_ALL_USERS,
            (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("password"),
                rs.getString("email"),
                Role.valueOf(rs.getString("role"))
            ));
    }

    @Override
    public int addUser(User user) {
        validator.validateWithoutPass(user);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("firstName", user.getFirstName())
            .addValue("lastName", user.getLastName())
            .addValue("password", user.getPassword())
            .addValue("email", user.getEmail())
            .addValue("role", user.getRole().toString());

        jdbcTemplate.update(INSERT_USER, mapSqlParameterSource, generatedKeyHolder);

        var keys = generatedKeyHolder.getKeys();
        if (keys != null) {
            return (int) keys.get("id");
        }
        return -1;
    }

    @Override
    public User getUserById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        return jdbcTemplate.query(FETCH_USER_BY_ID, mapSqlParameterSource,
                (rs, rowNum) -> new User(
                    rs.getInt("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("password"),
                    rs.getString("email"),
                    Role.valueOf(rs.getString("role"))
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("email", email);

        return jdbcTemplate.query(FETCH_USER_BY_EMAIL, mapSqlParameterSource,
                (rs, rowNum) -> new User(
                    rs.getInt("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("password"),
                    rs.getString("email"),
                    Role.valueOf(rs.getString("role"))
                ))
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void deleteUserById(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("id", id);

        jdbcTemplate.update(DELETE_USER_BY_ID, mapSqlParameterSource);
    }

    @Override
    public void updateUserById(User user, int id) {
        validator.validateWithoutPass(user);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("firstName", user.getFirstName())
            .addValue("lastName", user.getLastName())
            .addValue("password", user.getPassword())
            .addValue("email", user.getEmail())
            .addValue("role", user.getRole().toString())
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_USER_BY_ID, mapSqlParameterSource);
    }

    @Override
    public void updateUserWithoutPasswordById(User user, int id) {
        validator.validateWithoutPass(user);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("firstName", user.getFirstName())
            .addValue("lastName", user.getLastName())
            .addValue("email", user.getEmail())
            .addValue("role", user.getRole().toString())
            .addValue("id", id);

        jdbcTemplate.update(UPDATE_USER_WITHOUT_PASS_BY_ID, mapSqlParameterSource);
    }

    @Override
    public List<User> getUsersOfRoleByPageAndInput(
        Role role,
        int limit,
        int pageNumber,
        String input
    ) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("role", role.name())
            .addValue("limit", limit)
            .addValue("pageNumber", pageNumber - 1)
            .addValue("input", "%" + input + "%");

        return jdbcTemplate.query(FETCH_USERS_OF_ROLE_BY_PAGE_AND_INPUT,
            mapSqlParameterSource,
            (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("password"),
                rs.getString("email"),
                Role.valueOf(rs.getString("role"))
            ));
    }
}
