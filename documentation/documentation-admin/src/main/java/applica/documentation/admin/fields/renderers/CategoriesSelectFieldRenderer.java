package applica.documentation.admin.fields.renderers;

import applica.documentation.domain.data.CategoriesRepository;
import applica.documentation.domain.model.Category;
import applica.framework.library.SimpleItem;
import applica.framework.library.fields.renderers.SelectFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 11/09/14
 * Time: 19:17
 */
@Component
public class CategoriesSelectFieldRenderer extends SelectFieldRenderer {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public List<SimpleItem> getItems() {
        List<Category> categories = categoriesRepository.find(null).getRows(Category.class);
        return SimpleItem.createList(categories, "title", "id");
    }



}
