package BetterReads.userBooks;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.Objects;

@PrimaryKeyClass
public class UserBooksPrimaryKey {
    @PrimaryKeyColumn(name="user_id",ordinal = 0,type= PrimaryKeyType.PARTITIONED)
    private String userId;

    @PrimaryKeyColumn(name="book_id",ordinal = 1,type= PrimaryKeyType.PARTITIONED)
    private String bookId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserBooksPrimaryKey)) return false;
        UserBooksPrimaryKey that = (UserBooksPrimaryKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookId);
    }
}
