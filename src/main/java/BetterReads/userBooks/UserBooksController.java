package BetterReads.userBooks;

import BetterReads.book.Book;
import BetterReads.book.bookRepo;
import BetterReads.user.BooksByUser;
import BetterReads.user.BooksByUserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class UserBooksController {

    @Autowired
    UserBooksRepo userBooksRepo;

    @Autowired
    BooksByUserRepo booksByUserRepo;

    @Autowired
    bookRepo bookRepo;

    @PostMapping("/addUserBook")
    public ModelAndView addBookForUser(@RequestBody MultiValueMap<String,String> formData
                                 , @AuthenticationPrincipal OAuth2User principal)
    {
        if(principal==null||principal.getAttribute("login")==null) {
            return null;
        }
        String bookId = formData.getFirst("bookId");
        Optional<Book> optionalBook = bookRepo.findById(bookId);
        if (!optionalBook.isPresent()) {
            return new ModelAndView("redirect:/");
        }
        Book book = optionalBook.get();

        UserBooks userBooks=new UserBooks();
        UserBooksPrimaryKey key=new UserBooksPrimaryKey();
        String userId=principal.getAttribute("login");
        key.setUserId(userId);
        key.setBookId(bookId);
        int rating = Integer.parseInt(formData.getFirst("rating"));

        userBooks.setKey(key);
        userBooks.setStartedDate(LocalDate.parse(formData.getFirst("startDate")));
        userBooks.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
        userBooks.setRating(rating);
        userBooks.setReadingStatus(formData.getFirst("readingStatus"));
        userBooksRepo.save(userBooks);
//
        BooksByUser booksByUser = new BooksByUser();
        booksByUser.setId(userId);
        booksByUser.setBookId(bookId);
        booksByUser.setBookName(book.getName());
        booksByUser.setCoverIds(book.getCoverIds());
        booksByUser.setAuthorNames(book.getAuthorNames());
        booksByUser.setReadingStatus(formData.getFirst("readingStatus"));
        booksByUser.setRating(rating);
        booksByUserRepo.save(booksByUser);
        return new ModelAndView("redirect:/books/"+bookId);
    }
}
