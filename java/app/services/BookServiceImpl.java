package app.services;

import app.enumerations.AgeRestriction;
import app.enumerations.EditionType;
import app.models.Book;
import app.models.Category;
import app.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void save(Book book) {
        this.bookRepository.save(book);
    }

    @Override
    public List<Book> getBooksByAgeRestriction(String ageRestriction) {
        return this.bookRepository.findAllByAgeRestrictionLike(AgeRestriction.valueOf(ageRestriction.toUpperCase()));
    }

    @Override
    public List<Book> getAllGoldenBooks() {
        return this.bookRepository.findAllByEditionTypeAndCopiesLessThan(EditionType.GOLD, 5000L);
    }

    @Override
    public List<Book> getBooksBetweenBounds() {
        return this.bookRepository.findAllByPriceLessThanOrPriceGreaterThan(BigDecimal.valueOf(5L),BigDecimal.valueOf(40L));
    }

    @Override
    public List<Book> getBooksNotReleasedInYear(int year) {
        return this.bookRepository.findAllNotReleasedIn(year);
    }

    @Override
    public List<Book> getBooksByCategories(List<Category> categories) {
        return this.bookRepository.findAllByCategoriesIn(categories);
    }

    @Override
    public List<Book> getBooksBeforeGivenDate(Date date) {
        return this.bookRepository.findAllByReleaseDateBefore(date);
    }

    @Override
    public List<Book> getBooksContainingString(String str) {
        return this.bookRepository.findAllByTitleContainingIgnoreCase(str);
    }

    @Override
    public List<Book> getBooksByAuthorLastNameContainsString(String string) {
        return this.bookRepository.findAllByAuthorLastNameStartingWith(string);
    }

    @Override
    public int getBooksByGivenCharLength(long length) {
        return this.bookRepository.findCountOfBooksWhereTitleIsLongerThan(length);
    }
}
