package applica.framework.data;

import applica.framework.builders.LoadRequestBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains informations for a repository to load data from a database, filters, paginations and sorts
 */
public class LoadRequest {

    private int page;
    private int rowsPerPage;
    private List<Sort> sorts;
    private List<Filter> filters = new ArrayList<>();

    public static LoadRequestBuilder build() {
        return new LoadRequestBuilder();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public Map<String, Object> filtersMap() {
        Map<String, Object> data = new HashMap<String, Object>();
        for (Filter filter : filters) {
            data.put(filter.getProperty(), filter.getValue());
        }

        return data;
    }

    public static LoadRequest fromJSON(String loadRequestJSON) {
        ObjectMapper mapper = new ObjectMapper();
        LoadRequest request = new LoadRequest();

        try {
            if (StringUtils.hasLength(loadRequestJSON)) {
                request = mapper.readValue(loadRequestJSON, LoadRequest.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }

    public Object getFilterValue(final String property) {
        Filter filter = (Filter) CollectionUtils.find(filters, new Predicate() {
            @Override
            public boolean evaluate(Object item) {
                return property.equals(((Filter) item).getProperty());
            }
        });

        if (filter != null) return filter.getValue();
        return null;
    }

    public boolean hasFilter(final String property) {
        Filter filter = (Filter) CollectionUtils.find(filters, new Predicate() {
            @Override
            public boolean evaluate(Object item) {
                return property.equals(((Filter) item).getProperty());
            }
        });

        return filter != null;
    }
}
