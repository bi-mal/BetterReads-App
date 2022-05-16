package BetterReads.book;

import BetterReads.userBooks.UserBooks;
import BetterReads.userBooks.UserBooksPrimaryKey;
import BetterReads.userBooks.UserBooksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired
    bookRepo bookRepo;

    @Autowired
    UserBooksRepo userBooksRepo;

    @GetMapping(value="/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model, @AuthenticationPrincipal OAuth2User principal)
    {
        var optionalBook=bookRepo.findById(bookId);
        if(optionalBook.isPresent()) {
            Book book = optionalBook.get();
            String coverImageUrl= "/image/no-image.png";
            if (book.getCoverIds()!=null&& book.getCoverIds().size()>0) {
                coverImageUrl = COVER_IMAGE_ROOT + book.getCoverIds().get(0) + "-L.jpg";
            } 
            model.addAttribute("coverImage",coverImageUrl);
            model.addAttribute("book", book);
            if(principal!=null&&principal.getAttribute("login")!=null)
            {
                String userId=principal.getAttribute("login");
                model.addAttribute("loginId", userId);
                UserBooksPrimaryKey key = new UserBooksPrimaryKey();
                key.setBookId(bookId);
                key.setUserId(userId);
                Optional<UserBooks> userBooks = userBooksRepo.findById(key);
                if (userBooks.isPresent()) {
                    model.addAttribute("userBooks", userBooks.get());
                } else {
                    model.addAttribute("userBooks", new UserBooks());
                }
            }
            return "book";
        }
        return "book-not-found";
    }

}
