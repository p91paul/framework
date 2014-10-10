package applica.framework.data.mongodb;

import applica.framework.annotations.ManyToMany;
import applica.framework.data.Entity;
import applica.framework.data.Key;
import applica.framework.data.RepositoriesFactory;
import applica.framework.data.Repository;
import applica.framework.utils.TypeUtils;
import com.mongodb.BasicDBObject;
import org.apache.commons.logging.Log;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MongoMapper {

    @Autowired
    private RepositoriesFactory repositoriesFactory;

	private static Log logger = org.apache.commons.logging.LogFactory.getLog(MongoMapper.class);
	
	private static final Class<?>[] ALLOWED_TYPES = new Class<?>[] { 
		String.class,
		Integer.class,
		Float.class,
		Double.class,
		Boolean.class,
		Byte.class,
		Short.class,
		Long.class,
		Date.class
	};
	
	public BasicDBObject loadBasicDBObject(Entity source) {
		BasicDBObject document = new BasicDBObject();
		if (source != null && document != null) {			
			Class<?> type = source.getClass();
            
            //put entity id in document
            if (source.getId() != null) {
                document.put("_id", new ObjectId(String.valueOf(source.getId())));
            }
            
			//logger.warn("Converting " + type.getSimpleName());
			for(Field field : TypeUtils.getAllFields(type)) {
				if (!Modifier.isTransient(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
					//logger.warn(" --- field: " + field.getName());
					field.setAccessible(true);
									
					if (field.getName().equals("id")) {
						continue;
					} else {
                        try {
                            if (field.getAnnotation(ManyToMany.class) != null) {
                                Object fieldSourceValue = field.get(source);
                                if (fieldSourceValue != null && TypeUtils.isList(fieldSourceValue.getClass())) {
                                    List<?> sourceList = (List<?>) fieldSourceValue;
                                    ArrayList<Object> values = new ArrayList<Object>();
                                    Class<?> typeArgument = TypeUtils.getFirstGenericArgumentType(field.getGenericType());
                                    Assert.isTrue(TypeUtils.isEntity(typeArgument), "ManyToMany not allowed for non-entity types: " + field.getName());
                                    Repository repository = repositoriesFactory.createForEntity((Class<? extends Entity>) typeArgument);
                                    Objects.requireNonNull(repository, "Repository for class not found: " + typeArgument.toString());
                                    for (Object el : sourceList) {
                                        Entity e = (Entity) el;
                                        values.add(e.getId());
                                    }
                                    document.put(field.getName(), values);
                                }
                            } else {
                                Object fieldSourceValue = field.get(source);
                                if (fieldSourceValue != null) {
                                    Object basicDBObjectValue = convertToBasicDBObjectValue(fieldSourceValue);
                                    if (basicDBObjectValue != null) {
                                        document.put(field.getName(), basicDBObjectValue);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
					}
				}
			}
		} 
		
		return document;
	}
	
	
	
	private Object convertToBasicDBObjectValue(Object source) {
		Object value = null;
		if (TypeUtils.isEntity(source.getClass())) {
			try {
				//logger.warn("Field is entity");
				value = loadBasicDBObject((Entity)source);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		} else if (source.getClass().equals(Key.class)) {
            value = ((Key) source).getValue();
        } else if (isAllowed(source.getClass())) {	
            value = source;	
		} else if (TypeUtils.isList(source.getClass())) {
			try {
				List<?> list = (List<?>)source;
				ArrayList<Object> values = new ArrayList<Object>();
				for(Object el : list) {
					Object elVal = convertToBasicDBObjectValue(el);
					if (elVal != null) values.add(elVal);
				}
				value = values;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		
		return value;
	}
	
	public Object loadObject(BasicDBObject source, Class<?> destinationType) {
		Entity destination = null;
		try {
			destination = (Entity)destinationType.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if (source != null && destination != null) {
			Class<?> type = destination.getClass();
			for(String key : source.keySet()) {
				if (key.equals("_id")) { 
					destination.setId(source.getString(key));
				} else {
					try {
						Field field = TypeUtils.getField(type, key);
						if (!Modifier.isTransient(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
							field.setAccessible(true);
							
							if (TypeUtils.isEntity(field.getType())) {
								BasicDBObject childDocument = (BasicDBObject)source.get(key);
								Object value = loadObject(childDocument, field.getType());
								field.set(destination, value);
                            } else if (field.getType().equals(Key.class)) {
                                field.set(destination, new Key(source.get(key)));
                            } else if (isAllowed(field.getType())) {
								field.set(destination, source.get(key));
							} else if (TypeUtils.isList(field.getType())) {
                                if (field.getAnnotation(ManyToMany.class) != null) {
                                    ArrayList<Object> values = new ArrayList<Object>();
                                    Class<?> typeArgument = TypeUtils.getFirstGenericArgumentType(field.getGenericType());
                                    Assert.isTrue(TypeUtils.isEntity(typeArgument), "ManyToMany not allowed for non-entity types: " + field.getName());
                                    List<?> sourceList = (List<?>)source.get(field.getName());
                                    Repository repository = repositoriesFactory.createForEntity((Class<? extends Entity>) typeArgument);
                                    Objects.requireNonNull(repository, "Repository for class not found: " + typeArgument.toString());
                                    for(Object el : sourceList) {
                                        repository.get(el).ifPresent(values::add);
                                    }
                                    field.set(destination, values);
                                } else {
                                    ArrayList<Object> values = new ArrayList<Object>();
                                    Class<?> typeArgument = TypeUtils.getFirstGenericArgumentType(field.getGenericType());
                                    List<?> sourceList = (List<?>)source.get(field.getName());
                                    for(Object el : sourceList) {
                                        if (TypeUtils.isEntity(typeArgument)) {
                                            values.add(loadObject((BasicDBObject)el, typeArgument));
                                        } else if (isAllowed(typeArgument)) {
                                            values.add(el);
                                        }
                                    }
                                    field.set(destination, values);
                                }
							}
												
						}
					} catch(NoSuchFieldException e) {
						logger.warn("Field in database " + key + " isn't available on class");
					} catch (Exception e) {	
						logger.warn("Error in field " + key);
						e.printStackTrace();
					}
				}
			}
		}
		
		return destination;
	}
	
	private static boolean isAllowed(Class<?> type) {
		if (type.isPrimitive()) return true;
		
		for(Class<?> allowedType : ALLOWED_TYPES) {
			if (type.equals(allowedType)) return true;
		}
			
		return false;
	}
}
