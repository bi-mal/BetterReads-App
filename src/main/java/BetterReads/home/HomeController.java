package BetterReads.home;

import BetterReads.user.BooksByUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    BooksByUserRepo booksByUserRepo;

    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model)
    {
        if(principal==null || principal.getAttribute("login")==null)
        {
            return "index";
        }
        String userId=principal.getAttribute("login");
        var booksSlice=booksByUserRepo.findAllById(userId, CassandraPageRequest.of(0,100));
        var booksOfUser=booksSlice.getContent();
        booksOfUser=booksOfUser.stream().distinct().sorted(Comparator.naturalOrder()).map(book -> {
            String coverImageUrl= "/image/no-image.png";
            if (book.getCoverIds()!=null&& book.getCoverIds().size()>0) {
                coverImageUrl = COVER_IMAGE_ROOT + book.getCoverIds().get(0) + "-M.jpg";
            }
            book.setCoverUrl(coverImageUrl);
            return book;
        }).collect(Collectors.toList());
        model.addAttribute("books",booksOfUser);
        return "home";
    }
}
