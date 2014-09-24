package applica.documentation.frontend.controllers;

import applica.documentation.domain.data.CategoriesRepository;
import applica.documentation.domain.facade.CategoriesFacade;
import applica.documentation.domain.model.Category;
import applica.documentation.domain.model.Content;
import applica.documentation.domain.model.Filters;
import applica.framework.ApplicationContextProvider;
import applica.framework.builders.LoadRequestBuilder;
import applica.framework.ApplicationContextProvider;
import applica.framework.library.responses.ErrorResponse;
import applica.framework.library.responses.SimpleResponse;
import applica.framework.library.responses.ValueResponse;
import applica.framework.library.ui.PartialViewRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 13/09/14
 * Time: 11:58
 */
@Controller
@RequestMapping("/categories")
public class CategoriesController {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private CategoriesFacade categoriesFacade;

    @RequestMapping("/{code}")
    public @ResponseBody SimpleResponse get(@PathVariable String code, HttpServletRequest request, HttpServletResponse response) {
        Category category = categoriesRepository.find(LoadRequestBuilder.build().eq(Filters.CMSOBJECT_CODE, code)).getOne(Category.class);
        if (category == null) {
            response.setStatus(404);
            try {
                response.sendError(404);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        Category root = categoriesFacade.loadCategoryTree(code);
        if (root == null) {
            response.setStatus(404);
            try {
                response.sendError(404);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        try {
            HashMap<String, Object> context = new HashMap<>();
            context.put("root", root);

            PartialViewRenderer partialViewRenderer = new PartialViewRenderer();
            String content = partialViewRenderer.render(
                    ((ViewResolver) ApplicationContextProvider.provide().getBean("viewResolver")),
                    "categories/content",
                    context,
                    LocaleContextHolder.getLocale(),
                    request
            );

            return new ValueResponse(new Content(category, content));
        } catch (Exception e) {
            return new ErrorResponse("Error rendering view");
        }


    }

}
