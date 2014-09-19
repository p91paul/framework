package applica.framework.data;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadRequest {

    private int page;
    private int rowsPerPage;
    private Sort sortBy;
    private List<Filter> filters = new ArrayList<>();

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

    public Sort getSortBy() {
        return sortBy;
    }

    public void setSortBy(Sort sortBy) {
        this.sortBy = sortBy;
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
