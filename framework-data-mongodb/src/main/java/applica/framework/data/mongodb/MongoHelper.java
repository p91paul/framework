package applica.framework.data.mongodb;

import applica.framework.library.options.OptionsManager;
import com.mongodb.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.UnknownHostException;
import java.util.Arrays;

public class MongoHelper {

	private MongoClient mongo;
	private DB db;
	
	@Autowired
	private OptionsManager options;
	
	public DB getDB() {
		if(db == null) {
			mongo = getMongo();
			if(mongo != null) {
				db = mongo.getDB(getDbName());
			}
		}
		
		return db;
	}

    private String getDbName() {
        return options.get("applica.framework.data.mongodb.db");
    }
	
	public MongoClient getMongo() {
		if(mongo == null) {
			try {
                String username = options.get("applica.framework.data.mongodb.username");
                String password = options.get("applica.framework.data.mongodb.password");
                String db = getDbName();
                String host = options.get("applica.framework.data.mongodb.host");
                if (StringUtils.isNotEmpty(username)) {
                    MongoCredential mongoCredential = MongoCredential.createMongoCRCredential(username, db, password.toCharArray());
                    mongo = new MongoClient(
                            new ServerAddress(host),
                            Arrays.asList(mongoCredential)
                    );
                } else {
                    mongo = new MongoClient(host);
                }
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
