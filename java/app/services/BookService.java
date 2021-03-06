package app.services;

import app.enumerations.EditionType;
import app.models.Book;
import app.models.Category;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface BookService extends BaseService<Book>{

    List<Book> getBooksByAgeRestriction(String ageRestriction);

    List<Book> getAllGoldenBooks();

    List<Book> getBooksBetweenBounds();

    List<Book> getBooksNotReleasedInYear(int year);

    List<Book> getBooksByCategories(List<Category> categories);

    List<Book> getBooksBeforeGivenDate(Date date);

    List<Book> getBooksContainingString(String str);

    List<Book> getBooksByAuthorLastNameContainsString(String string);

    int getBooksByGivenCharLength(long length);

}
