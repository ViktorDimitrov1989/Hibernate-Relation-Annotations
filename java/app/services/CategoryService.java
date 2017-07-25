package app.services;

import app.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService extends BaseService<Category>{

    List<Category> findAll();

    Category findByName(String name);

}
