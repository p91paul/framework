package applica.framework.data.mongodb;

import applica.framework.data.*;
import com.mongodb.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

public abstract class MongoRepository implements Repository {
	
	@Autowired
	private MongoHelper mongoHelper;
	
	private Log logger = LogFactory.getLog(getClass());
	
	protected DBCollection collection;
	
	@PostConstruct
	protected void init() {
		DB db = mongoHelper.getDB();
		if(db != null) {
			collection = db.getCollection(getCollectionName());
		} else {
			logger.warn("Mongo DB is null");
		}
	}
	
	@PreDestroy
	protected void destroy() {
		mongoHelper.close();
	}
	
	protected abstract String getCollectionName();
	protected abstract Class<? extends Entity> getType();
	
	@Override
	public Entity get(Object id) {
		if(collection == null) { 
			logger.warn("Mongo collection is null");
			return null;
		}
		Entity entity = null;
		
		if(id != null) {
			BasicDBObject document = (BasicDBObject)collection.findOne(Query.mk().id(String.valueOf(id)));
			if(document != null) {
				entity = (Entity)MongoUtils.loadObject(document, getType());
			}
		}
		
		return entity;
	}

	@Override
	public LoadResponse find(LoadRequest loadRequest) {
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
		if(limit != 0) cur.limit(limit);
		if(skip != 0) cur.skip(skip);
		
		Sort sort = loadRequest.getSortBy();
		if(sort == null) sort = getDefaultSort();
		
		if(sort != null) {
			cur.sort(new BasicDBObject(sort.getProperty(), sort.isDescending() ? -1 : 1));
		} 
		
		while(cur.hasNext()) {
			BasicDBObject document = (BasicDBObject)cur.next();
			Entity entity = (Entity)MongoUtils.loadObject(document, getType());
			entities.add(entity);		
        }
		
		response.setRows(entities);
		response.setTotalRows(count);
		
		return response;
	}

	protected Query createQuery(LoadRequest loadRequest) {
		Query query = query();

        for(Filter filter : loadRequest.getFilters()) {
            if(filter.getValue() == null) {
                continue;
            }
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
            }
        }

        return query;
	}

	@Override
	public void delete(Object id) {
		if(collection == null) { 
			logger.warn("Mongo collection is null");
			return;
		}
		
		if(id != null) {
			collection.remove(Query.mk().id(String.valueOf(id)));
		}
	}

	@Override
	public void save(Entity entity) {
		if(collection == null) { 
			logger.warn("Mongo collection is null");
			return;
		}
		
		BasicDBObject document = MongoUtils.loadBasicDBObject(entity);	
		collection.save(document);
		entity.setId(document.getString("_id"));
	}
	
	public Sort getDefaultSort() {
		return null;
	}

    protected Query query() {
        return Query.mk();
    }
}
