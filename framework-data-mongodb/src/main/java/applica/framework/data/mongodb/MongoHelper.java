package applica.framework.data.mongodb;

import applica.framework.library.options.OptionsManager;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

@Component
public class MongoHelper {

	private Mongo mongo;
	private DB db;
	
	@Autowired
	private OptionsManager options;
	
	public DB getDB() {
		if(db == null) {
			mongo = getMongo();
			if(mongo != null) {
				db = mongo.getDB(options.get("mongodb.db"));
			}
		}
		
		return db;
	}
	
	public Mongo getMongo() {
		if(mongo == null) {
			try {
				mongo = new Mongo(options.get("mongodb.host"));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (MongoException e) {
				e.printStackTrace();
			}
		}
		return mongo;
	}

	public void close() {
		if(mongo != null) mongo.close();
	}
	
}
