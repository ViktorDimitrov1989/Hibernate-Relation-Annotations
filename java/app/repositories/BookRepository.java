package app.repositories;

import app.enumerations.AgeRestriction;
import app.enumerations.EditionType;
import app.models.Book;
import app.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

    List<Book> findAllByAgeRestrictionLike(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, long copiesCount);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal lowerBound, BigDecimal upperBound);

    @Query(value = "SELECT b FROM Book AS b WHERE YEAR(b.releaseDate) != :year")
    List<Book> findAllNotReleasedIn(@Param(value = "year")int year);

    List<Book> findAllByCategoriesIn(List<Category> categories);

    List<Book> findAllByReleaseDateBefore(Date date);

    List<Book> findAllByTitleContainingIgnoreCase(String string);

    List<Book> findAllByAuthorLastNameStartingWith(String string);

    @Query(value = "SELECT COUNT(b) FROM Book AS b WHERE CHAR_LENGTH(b.title) > :length")
    int findCountOfBooksWhereTitleIsLongerThan(@Param(value = "length") long length);

}
