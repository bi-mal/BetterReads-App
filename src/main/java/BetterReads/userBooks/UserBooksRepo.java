package BetterReads.userBooks;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface UserBooksRepo extends CassandraRepository<UserBooks, UserBooksPrimaryKey> {
}
