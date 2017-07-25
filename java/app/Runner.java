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
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class Runner implements CommandLineRunner{
    //here must be the services @Autowired
    private BookService bookService;
    private AuthorService authorService;
    private CategoryService categoryService;
    private BufferedReader bufferedReader;

    @Autowired
    public Runner(BookService bookService, AuthorService authorService, CategoryService categoryService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... strings) throws Exception {
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        //1//
        //bookTitlesByAgeRestriction();
        //2//
        //listGoldenBooks();
        //3//
        //listBooksBetweenBounds();
        //4//
        //listAllNotReleasedIn();
        //5//
        //listAllBooksByCategories();
        //6//
        //listBooksReleasedBeforeData();
        //7//
        //listAutorsWithFirstNameEndingWithGivenString();
        //8//
        //listAllBooksContainingString();
        //9//
        //listBooksByAuthorStartsWithString();
        //10//
        //listCountOfBooksByTitleLength();

    }

    private void listCountOfBooksByTitleLength() throws IOException {
        int length = Integer.parseInt(this.bufferedReader.readLine());

        int result = this.bookService.getBooksByGivenCharLength(length);
        System.out.println(result);
    }

    private void listBooksByAuthorStartsWithString() throws IOException {
        String str = this.bufferedReader.readLine();

        List<Book> result = this.bookService.getBooksByAuthorLastNameContainsString(str);

        for (Book book : result) {
            System.out.println(book.getTitle());
        }
    }

    private void listAllBooksContainingString() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String str = reader.readLine();

        List<Book> result = this.bookService.getBooksContainingString(str);

        for (Book book : result) {
            System.out.println(book.getTitle());
        }
    }

    private void listAutorsWithFirstNameEndingWithGivenString() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<Author> result = this.authorService.getAuthorsByFirstNameEnding(reader.readLine());

        for (Author author : result) {
            System.out.println(String.format("%s %s", author.getFirstName(), author.getLastName()));
        }

    }

    private void listBooksReleasedBeforeData() throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date targetDate =  df.parse(reader.readLine());

        List<Book> result = this.bookService.getBooksBeforeGivenDate(targetDate);

        for (Book book : result) {
            System.out.println(String.format(
                    "Title: %s, Edition type: %s, Price: %.2f",
                    book.getTitle(), book.getEditionType().toString(), book.getPrice()));
        }

    }

    private void listAllBooksByCategories() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] categories = reader.readLine().split("\\s+");

        List<Category> catList =  new ArrayList<>();
        for (String category : categories) {
            Category c = this.categoryService.findByName(category);
            if (c != null){
                catList.add(c);
            }
        }

        List<Book> result = this.bookService.getBooksByCategories(catList);

        String debug = "";

        for (Book book : result) {
            System.out.println(book.getTitle());
        }


    }

    private void listAllNotReleasedIn() {
        List<Book> result = this.bookService.getBooksNotReleasedInYear(2000);

        for (Book book : result) {
            System.out.println(book.getTitle());
        }

    }

    private void listBooksBetweenBounds() {
        List<Book> res = this.bookService.getBooksBetweenBounds();

        String debug = "";

        for (Book book : res) {
            System.out.println(String.format("$%s - %.2f", book.getTitle(), book.getPrice()));
        }

    }

    private void listGoldenBooks() {

        List<Book> books = this.bookService.getAllGoldenBooks();

        for (Book book : books) {
            System.out.println(book.getTitle());
        }

    }

    private void bookTitlesByAgeRestriction() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String ageRestriction = reader.readLine();

        List<Book> result = this.bookService.getBooksByAgeRestriction(ageRestriction);

        for (Book book : result) {
            System.out.println(book.getTitle());
        }

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
