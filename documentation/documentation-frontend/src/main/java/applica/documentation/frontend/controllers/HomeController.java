package applica.documentation.frontend.controllers;

import applica.documentation.domain.data.ArticlesRepository;
import applica.documentation.domain.data.CategoriesRepository;
import applica.documentation.domain.model.Article;
import applica.documentation.domain.model.Category;
import applica.documentation.domain.model.Filters;
import applica.documentation.domain.model.Sorts;
import applica.framework.builders.LoadRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 25/10/13
 * Time: 18:40
 */
@Controller
public class HomeController {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private ArticlesRepository articlesRepository;


    @RequestMapping("/")
    public String index(Model model) {
        List<Category> categories = categoriesRepository.find(
                LoadRequestBuilder.build()
                        .eq(Filters.CMSOBJECT_SHOW_IN_MENU, true)
                        .eq(Filters.CMSOBJECT_ACTIVE, true)
                        .sort(Sorts.CMSOBJECT_PRIORITY, false)
        ).getRows(Category.class);

        List<Article> articles = articlesRepository.find(
                LoadRequestBuilder.build()
                        .eq(Filters.CMSOBJECT_SHOW_IN_MENU, true)
                        .eq(Filters.CMSOBJECT_ACTIVE, true)
                        .sort(Sorts.CMSOBJECT_PRIORITY, false)
        ).getRows(Article.class);

        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);

        return "home/index";
    }

}
