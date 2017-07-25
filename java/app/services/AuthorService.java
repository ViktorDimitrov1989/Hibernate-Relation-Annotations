package app.services;

import app.models.Author;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorService extends BaseService<Author>{

    List<Author> findAll();

}
