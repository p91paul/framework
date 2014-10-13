package applica.framework.builders;

import applica.framework.data.Disjunction;
import applica.framework.data.Filter;
import applica.framework.data.Sort;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 13/10/14
 * Time: 09:48
 */
public class DisjunctionBuilder extends Disjunction {

    private final LoadRequestBuilder loadRequestBuilder;

    public DisjunctionBuilder(LoadRequestBuilder loadRequestBuilder) {
        this.loadRequestBuilder = loadRequestBuilder;
    }

    public static DisjunctionBuilder begin(LoadRequestBuilder loadRequestBuilder) {
        return new DisjunctionBuilder(loadRequestBuilder);
    }

    public DisjunctionBuilder filter(String property, Object value) {
        if(value != null) {
            getChildren().add(new Filter(property, value));
        }
        return this;
    }

    public DisjunctionBuilder filter(String property, Object value, String type) {
        if(value != null) {
            getChildren().add(new Filter(property, value, type));
        }
        return this;
    }

    public DisjunctionBuilder like(String property, Object value) {
        if(value != null) {
            getChildren().add(new Filter(property, value, Filter.LIKE));
        }
        return this;
    }

    public DisjunctionBuilder gt(String property, Object value) {
        if(value != null) {
            getChildren().add(new Filter(property, value, Filter.GT));
        }
        return this;
    }

    public DisjunctionBuilder gte(String property, Object value) {
        if(value != null) {
            getChildren().add(new Filter(property, value, Filter.GTE));
        }
        return this;
    }

    public DisjunctionBuilder lt(String property, Object value) {
        if(value != null) {
            getChildren().add(new Filter(property, value, Filter.LT));
        }
        return this;
    }

    public DisjunctionBuilder lte(String property, Object value) {
        if(value != null) {
            getChildren().add(new Filter(property, value, Filter.LTE));
        }
        return this;
    }

    public DisjunctionBuilder eq(String property, Object value) {
        if(value != null) {
            getChildren().add(new Filter(property, value, Filter.EQ));
        }
        return this;
    }

    public DisjunctionBuilder in(String property, Object value) {
        if(value != null) {
            getChildren().add(new Filter(property, value, Filter.IN));
        }
        return this;
    }

    public DisjunctionBuilder nin(String property, Object value) {
        if(value != null) {
            getChildren().add(new Filter(property, value, Filter.NIN));
        }
        return this;
    }

    public DisjunctionBuilder id(String property, Object value) {
        if(value != null) {
            getChildren().add(new Filter(property, value, Filter.ID));
        }
        return this;
    }

    public DisjunctionBuilder id(Object value) {
        if(value != null) {
            getChildren().add(new Filter(null, value, Filter.ID));
        }
        return this;
    }

    public LoadRequestBuilder finish() {
        loadRequestBuilder.getFilters().add(this);
        return loadRequestBuilder;
    }

}
