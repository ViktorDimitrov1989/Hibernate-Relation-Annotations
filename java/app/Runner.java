package app;

import app.enumerations.AgeRestriction;
import app.enumerations.EditionType;
import app.models.Author;
import app.models.Book;
import app.models.Category;
import app.services.AuthorService;
import app.services.BookService;
import app.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class Runner implements CommandLineRunner{
    //here must be the services @Autowired
    private BookService bookService;
    private AuthorService authorService;
    private CategoryService categoryService;

    @Autowired
    public Runner(BookService bookService, AuthorService authorService, CategoryService categoryService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... strings) throws Exception {
        this.importAuthors();
        this.importCategories();
        this.importBooks();

    }

    private void importAuthors() throws IOException {
        final String path = "/src/main/resources/";
        String projectPath = System.getProperty("user.dir");

        BufferedReader authorsReader = new BufferedReader(
                new FileReader(projectPath + path + "authors.txt"));
        String line = authorsReader.readLine();


        while ((line = authorsReader.readLine()) != null){
            String[] authorTokens = line.trim().split("\\s+");
            String firstName = authorTokens[0];
            String lastName = authorTokens[1];

            Author author = new Author();
            author.setFirstName(firstName);
            author.setLastName(lastName);

            this.authorService.save(author);
        }

    }

    private void importCategories() throws IOException {
        final String path = "/src/main/resources/";
        String projectPath = System.getProperty("user.dir");

        BufferedReader authorsReader = new BufferedReader(
                new FileReader(projectPath + path + "categories.txt"));
        String line = authorsReader.readLine();

        while ((line = authorsReader.readLine()) != null){
            String categoryName = line;

            Category category = new Category();
            category.setName(categoryName);

            this.categoryService.save(category);
        }

    }



    private void importBooks() throws IOException, ParseException {
        final String path = "/src/main/resources/";
        String projectPath = System.getProperty("user.dir");
        BufferedReader booksReader = new BufferedReader(new FileReader(projectPath + path + "books.txt"));
        String line = booksReader.readLine();
        while((line = booksReader.readLine()) != null){
            String[] data = line.split("\\s+");

            List<Author> authors = this.authorService.findAll();
            Random random = new Random();

            int authorIndex = random.nextInt(authors.size());
            Author author = authors.get(authorIndex);
            EditionType editionType = EditionType.values()[Integer.parseInt(data[0])];
            SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
            Date releaseDate = formatter.parse(data[1]);
            int copies = Integer.parseInt(data[2]);
            BigDecimal price = new BigDecimal(data[3]);
            AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(data[4])];
            StringBuilder titleBuilder = new StringBuilder();
            for (int i = 5; i < data.length; i++) {
                titleBuilder.append(data[i]).append(" ");
            }
            titleBuilder.delete(titleBuilder.lastIndexOf(" "), titleBuilder.lastIndexOf(" "));
            String title = titleBuilder.toString();

            Book book = new Book();
            book.setAuthor(author);
            book.setEditionType(editionType);
            book.setReleaseDate(releaseDate);
            book.setCopies(copies);
            book.setPrice(price);
            book.setAgeRestriction(ageRestriction);
            book.setTitle(title);

            List<Category> categories = this.categoryService.findAll();

            Set<Category> randomCategoryList = new HashSet<>();
            for (int i = 0; i < 3; i++) {
                int categoryIndex = random.nextInt(categories.size());

                randomCategoryList.add(categories.get(categoryIndex));
            }

            book.setCategories(randomCategoryList);

            this.bookService.save(book);
        }
    }


}
