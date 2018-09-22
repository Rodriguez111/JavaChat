package chat;

import java.io.Serializable;
import java.util.Objects;

public class User {
    private String userName;

    private Connection connection;

    public User(String userName, Connection connection) {
        this.userName = userName;
        this.connection = connection;
    }

    public String getUserName() {
        return userName;
    }


    public Connection getConnection() {
        return connection;

    }




   //переопределяем. чтобы сравнивать только по имени, так как имя уникально
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
