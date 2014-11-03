package applica.framework.data.mongodb;

import applica.framework.data.*;
import applica.framework.data.mongodb.constraints.ConstraintsChecker;
import applica.framework.utils.Strings;
import com.mongodb.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class MongoRepository<T extends Entity> implements Repository<T> {
	
	@Autowired
	private MongoHelper mongoHelper;

    @Autowired
    private MongoMapper mongoMapper;

    @Autowired(required = false)
    private ConstraintsChecker constraintsChecker;
	
	private Log logger = LogFactory.getLog(getClass());
	
	protected DBCollection collection;
	
	protected void init() {
        if (collection == null) {
            DB db = mongoHelper.getDB();
            if (db != null) {
                collection = db.getCollection(getCollectionName());
            } else {
                logger.warn("Mongo DB is null");
            }
        }
	}
	
	@PreDestroy
	protected void destroy() {
		mongoHelper.close();
	}
	
	protected String getCollectionName() {
        return Strings.pluralize(StringUtils.uncapitalize(getEntityType().getSimpleName()));
    }
	
	@Override
	public Optional<T> get(Object id) {
        init();

		if(collection == null) { 
			logger.warn("Mongo collection is null");
			return null;
		}
		T entity = null;
		
		if(id != null) {
			BasicDBObject document = (BasicDBObject)collection.findOne(Query.mk().id(String.valueOf(id)));
			if(document != null) {
				entity = (T) mongoMapper.loadObject(document, getEntityType());
			}
		}
		
		return Optional.ofNullable(entity);
	}

	@Override
	public LoadResponse find(LoadRequest loadRequest) {
        init();

		if(collection == null) { 
			logger.warn("Mongo collection is null");
			return null;
		}
		
		if(loadRequest == null) loadRequest = new LoadRequest();
		
		LoadResponse response = new LoadResponse();
		List<Entity> entities = new ArrayList<>();
		
		DBObject query = createQuery(loadRequest);
		long count = collection.count(query);
		int limit = loadRequest.getRowsPerPage();
		int skip = loadRequest.getRowsPerPage() * (loadRequest.getPage() - 1);
		
		DBCursor cur = collection.find(query);
		if (limit != 0) cur.limit(limit);
		if (skip != 0) cur.skip(skip);
		
		List<Sort> sorts = loadRequest.getSorts();
        if (sorts == null) {
            sorts = getDefaultSorts();
        }

		if (sorts != null) {
            BasicDBObject sortObject = new BasicDBObject();
            for (Sort sort : sorts) {
                sortObject.append(sort.getProperty(), sort.isDescending() ? -1 : 1);
            }
            cur.sort(sortObject);
		} 
		
		while(cur.hasNext()) {
			BasicDBObject document = (BasicDBObject)cur.next();
			Entity entity = (Entity) mongoMapper.loadObject(document, getEntityType());
			entities.add(entity);		
        }
		
		response.setRows(entities);
		response.setTotalRows(count);
		
		return response;
	}

    protected void pushFilter(Query query, Filter filter) {
        switch (filter.getType()) {
            case Filter.LIKE:
                query.like(filter.getProperty(), filter.getValue().toString());
                break;
            case Filter.GT:
                query.gt(filter.getProperty(), filter.getValue());
                break;
            case Filter.GTE:
                query.gte(filter.getProperty(), filter.getValue());
                break;
            case Filter.LT:
                query.lt(filter.getProperty(), filter.getValue());
                break;
            case Filter.LTE:
                query.lte(filter.getProperty(), filter.getValue());
                break;
            case Filter.EQ:
                query.eq(filter.getProperty(), filter.getValue());
                break;
            case Filter.IN:
                query.in(filter.getProperty(), (List)filter.getValue());
                break;
            case Filter.NIN:
                query.nin(filter.getProperty(), (List)filter.getValue());
                break;
            case Filter.ID:
                if(filter.getProperty() == null) {
                    query.id((String)filter.getValue());
                } else {
                    query.id(filter.getProperty(), (String)filter.getValue());
                }
                break;
            case Filter.OR:
                List<Filter> ors = (List<Filter>) filter.getValue();
                query.or(ors.stream().map((f) -> {
                    Query q = query();
                    pushFilter(query, f);
                    return q;
                }).collect(Collectors.toList()));
                break;
        }
    }

	protected Query createQuery(LoadRequest loadRequest) {
		Query query = query();

        for (Filter filter : loadRequest.getFilters()) {
            if (filter.getValue() == null) {
                continue;
            }

            pushFilter(query, filter);
        }
        return query;
	}

	@Override
	public void delete(Object id) {
        init();

		if(collection == null) { 
			logger.warn("Mongo collection is null");
			return;
		}
		
		if(id != null) {
            if (constraintsChecker != null) {
                get(id).ifPresent(constraintsChecker::check);
            }
			collection.remove(Query.mk().id(String.valueOf(id)));
		}
	}

	@Override
	public void save(T entity) {
        init();

		if(collection == null) { 
			logger.warn("Mongo collection is null");
			return;
		}

        if (constraintsChecker != null) {
            constraintsChecker.check(entity);
        }

		BasicDBObject document = mongoMapper.loadBasicDBObject(entity);
		collection.save(document);
		entity.setId(document.getString("_id"));
	}

    public List<Sort> getDefaultSorts() {
		return null;
	}

    protected Query query() {
        return Query.mk();
    }
}
