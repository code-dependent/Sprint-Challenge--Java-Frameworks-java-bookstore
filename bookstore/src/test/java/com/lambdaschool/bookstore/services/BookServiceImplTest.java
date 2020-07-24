package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.Author;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Wrote;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//**********
// Note security is handled at the controller, hence we do not need to worry about security here!
//**********
public class BookServiceImplTest
{

    @Autowired
    private BookService bookService;

    @Before
    public void setUp() throws
            Exception
    {
        MockitoAnnotations.initMocks(this);
        List<Book> books = bookService.findAll();
        for(Book book:books){
            System.out.println("Book ID: " +book.getBookid() + ", Book Title: "+book.getTitle());
        }
    }

    @After
    public void tearDown() throws
            Exception
    {
        System.out.println("********** AFTER ************");
        List<Book> books = bookService.findAll();
        for(Book book:books){
            System.out.println("Book ID: " +book.getBookid() + ", Book Title: "+book.getTitle());
        }
    }

    @Test
    public void a_findAll()
    {
        assertEquals(5,bookService.findAll().size());
    }

    @Test
    public void ba_findBookById()
    {
        assertEquals("test_Essentials of Finance", bookService.findBookById(29).getTitle());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void bb_notFindBookById()
    {
        bookService.findBookById(99);
    }

    @Test
    public void ca_delete()
    {
        assertEquals(5, bookService.findAll().size());
        bookService.delete(26);
        assertEquals(4, bookService.findAll().size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void cb_cannotDelete()
    {
        assertEquals(4, bookService.findAll().size());
        bookService.delete(99);
        assertEquals(4, bookService.findAll().size());
    }

    @Test
    public void da_save()
    {
        Book book = new Book();
        book.setTitle("Hello, World - Java");
        book.setIsbn("dbzahstmnt");
        book.setCopy(500);
        assertEquals(4, bookService.findAll().size());
        bookService.save(book);
        assertEquals(5, bookService.findAll().size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void db_cannotsave()
    {
        Book book = new Book();
        book.setTitle("Hello, World - Java");
        book.setIsbn("dbzahstmnt");
        book.setCopy(500);
        book.setBookid(500);

        bookService.save(book);

    }
    @Test(expected = ResourceNotFoundException.class)
    public void dc_cannotsave()
    {
        Set<Author> authors = new HashSet<>();

        Book book = new Book();
        book.setTitle("Hello, World - Java");
        book.setIsbn("dbzahstmnt");
        book.setCopy(500);
        book.setBookid(500);

        Author a1 = new Author("test_Josh", "williams");
        Author a2 = new Author("test_Aaliyah", "williams");
        Author a3 = new Author("test_Kaelani", "williams");
        Author a4 = new Author("test_Levi", "williams");
        a1.setAuthorid(88);
        a2.setAuthorid(89);
        a3.setAuthorid(90);
        a4.setAuthorid(91);

        authors.add(a1);
        authors.add(a2);
        authors.add(a3);
        authors.add(a4);

        for(Author a: authors){
            book.getWrotes().add(new Wrote(a, book));

        }

        bookService.save(book);

    }

    @Test
    public void e_update()
    {
        Book book = new Book();
        book.setTitle("The Good Book");
        assertEquals("test_Calling Texas Home", bookService.findBookById(30).getTitle());
        assertEquals("The Good Book", bookService.update(book,30).getTitle());

    }

    @Test
    public void f_deleteAll()
    {
        bookService.deleteAll();
        assertEquals(0, bookService.findAll().size());
    }
}