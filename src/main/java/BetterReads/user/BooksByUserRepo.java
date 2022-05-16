package BetterReads.user;

import BetterReads.userBooks.UserBooksPrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BooksByUserRepo extends CassandraRepository<BooksByUser, UserBooksPrimaryKey> {

    Slice<BooksByUser> findAllById(String id, Pageable pageable);
}
