package applica.documentation.frontend.controllers;

import applica.documentation.domain.data.ArticlesRepository;
import applica.documentation.domain.facade.CategoriesFacade;
import applica.documentation.domain.model.Article;
import applica.documentation.domain.model.Category;
import applica.documentation.domain.model.Content;
import applica.documentation.domain.model.Filters;
import applica.framework.ApplicationContextProvider;
import applica.framework.builders.LoadRequestBuilder;
import applica.framework.data.Key;
import applica.framework.ApplicationContextProvider;
import applica.framework.library.responses.ErrorResponse;
import applica.framework.library.responses.SimpleResponse;
import applica.framework.library.responses.ValueResponse;
import applica.framework.library.ui.PartialViewRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 13/09/14
 * Time: 11:58
 */
@Controller
@RequestMapping("/articles")
public class ArticlesController {

    @Autowired
    private CategoriesFacade categoriesFacade;

    @Autowired
    private ArticlesRepository articlesRepository;

    @RequestMapping("/{code}")
    public @ResponseBody SimpleResponse get(@PathVariable String code, HttpServletRequest request, HttpServletResponse response) {
        Article article = articlesRepository.find(LoadRequestBuilder.build().eq(Filters.CMSOBJECT_CODE, code)).getOne(Article.class);
        if (article == null) {
            response.setStatus(404);
            try {
                response.sendError(404);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        Category category = categoriesFacade.loadCategoryTree(new Key(article.getCategoryId()));
        Category parent = null;
        if (category != null && category.getParentCategoryId() != null && StringUtils.hasLength(category.getParentCategoryId().toString())) {
            parent = categoriesFacade.loadCategoryTree(new Key(category.getParentCategoryId()));
        }


        try {
            HashMap<String, Object> context = new HashMap<>();
            context.put("article", article);
            context.put("category", category);
            context.put("parent", parent);

            PartialViewRenderer partialViewRenderer = new PartialViewRenderer();
            String content = partialViewRenderer.render(
                    ((ViewResolver) ApplicationContextProvider.provide().getBean("viewResolver")),
                    "articles/content",
                    context,
                    LocaleContextHolder.getLocale(),
                    request
            );

            return new ValueResponse(new Content(article, content));
        } catch (Exception e) {
            return new ErrorResponse("Error rendering view");
        }
    }

}
