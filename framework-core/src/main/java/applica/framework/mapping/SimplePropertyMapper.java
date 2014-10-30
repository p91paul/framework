package applica.framework.mapping;

import applica.framework.ApplicationContextProvider;
import applica.framework.FormDescriptor;
import applica.framework.FormField;
import applica.framework.RelatedFormField;
import applica.framework.data.Entity;
import applica.framework.data.RepositoriesFactory;
import applica.framework.data.Repository;
import applica.framework.utils.TypeUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimplePropertyMapper implements PropertyMapper {

    private RepositoriesFactory repositoriesFactory;

    public SimplePropertyMapper() {
        repositoriesFactory = ApplicationContextProvider.provide().getBean(RepositoriesFactory.class);
    }

    private Log logger = LogFactory.getLog(getClass());

    @SuppressWarnings({"rawtypes"})
    @Override
    public void mapFormValueFromEntityProperty(FormDescriptor formDescriptor, FormField formField, Map<String, Object> values, Entity entity)
            throws MappingException {

        if (formField instanceof RelatedFormField) {
            logger.info(String.format("field %s is a related form field", formField.getProperty()));

            try {
                Object finalValue = null;
                Class<List> listType = List.class;
                if (listType.isAssignableFrom(TypeUtils.genericCheckedType(formField.getDataType()))) {
                    logger.info(String.format("related field %s is list of entity type", formField.getProperty()));

                    List list = (List) PropertyUtils.getProperty(entity, formField.getProperty());
                    finalValue = list;
                } else {
                    logger.info(String.format("related field %s is single entity type", formField.getProperty()));

                    Entity relatedEntity = (Entity) PropertyUtils.getProperty(entity, formField.getProperty());
                    finalValue = relatedEntity;
                }
                values.put(formField.getProperty(), finalValue);
            } catch (Exception e) {
                throw new MappingException(formField.getProperty(), e);
            }
        } else {
            logger.info(String.format("field %s is standard field", formField.getProperty()));
            Object value = null;
            try {
                value = PropertyUtils.getProperty(entity, formField.getProperty());
            } catch (Exception e) {
                throw new MappingException(formField.getProperty(), e);
            }

            values.put(formField.getProperty(), value);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void mapEntityPropertyFromRequestValue(FormDescriptor formDescriptor, FormField formField, Entity entity, Map<String, String[]> requestValues)
            throws MappingException {
        if (requestValues.containsKey(formField.getProperty())) {
            String[] requestValueArray = requestValues.get(formField.getProperty());
            Object finalValue = null;

            if (formField instanceof RelatedFormField) {
                logger.info(String.format("field %s is a related form field", formField.getProperty()));

                Repository repository = null;

                Class<List> listType = List.class;
                if (listType.isAssignableFrom(TypeUtils.genericCheckedType(formField.getDataType()))) {
                    Class<?> typeArgument = TypeUtils.getFirstGenericArgumentType(formField.getDataType());
                    if (repository == null) {
                        repository = repositoriesFactory.createForEntity((Class<? extends Entity>) typeArgument);
                    }

                    logger.info(String.format("related field %s is list of entity type", formField.getProperty()));

                    List entities = null;
                    try {
                        entities = ((List) PropertyUtils.getProperty(entity, formField.getProperty()));
                        if (entities != null) {
                            entities.clear();
                        } else {
                            entities = new ArrayList();
                        }
                    } catch (Exception e) {}
                    for (String id : requestValueArray) {
                        repository.get(id).ifPresent(entities::add);
                    }

                    finalValue = entities;
                } else {
                    logger.info(String.format("related field %s is single entity type", formField.getProperty()));

                    if (repository == null) {
                        repository = repositoriesFactory.createForEntity((Class<? extends Entity>) formField.getDataType());
                    }

                    String relatedId = requestValueArray[0];
                    Entity relatedEntity = null;
                    if (StringUtils.hasLength(relatedId)) {
                        relatedEntity = (Entity) repository.get(relatedId).orElseGet(() -> null);
                    }

                    finalValue = relatedEntity;
                }

                try {
                    BeanUtils.setProperty(entity, formField.getProperty(), finalValue);
                } catch (Exception e) {
                    throw new MappingException(formField.getProperty(), e);
                }
            } else {
                logger.info(String.format("field %s is a standard type", formField.getProperty()));

                if (List.class.isAssignableFrom(TypeUtils.genericCheckedType(formField.getDataType()))) {
                    Class<?> genericType = TypeUtils.getListGeneric(entity.getClass(), formField.getProperty());
                    if (genericType == null)
                        throw new RuntimeException("Trying to mapping a list without generic type");
                    List list = new ArrayList();
                    for (int i = 0; i < requestValueArray.length; i++) {
                        list.add(ConvertUtils.convert(requestValueArray[i], genericType));
                    }

                    finalValue = list;
                } else {
                    finalValue = requestValueArray;
                }

                try {
                    BeanUtils.setProperty(entity, formField.getProperty(), finalValue);
                } catch (Exception e) {
                    throw new MappingException(formField.getProperty(), e);
                }


            }
        } else {
            //if value is a list and nothing comes from request, the list must be cleared
            Object finalValue = null;

            if (List.class.isAssignableFrom(TypeUtils.genericCheckedType(formField.getDataType()))) {
                List entities = null;
                try {
                    entities = ((List) PropertyUtils.getProperty(entity, formField.getProperty()));
                    if (entities != null) {
                        entities.clear();
                    }
                } catch (Exception e) {}

                finalValue = entities;

                try {
                    BeanUtils.setProperty(entity, formField.getProperty(), finalValue);
                } catch (Exception e) {
                    throw new MappingException(formField.getProperty(), e);
                }
            }
        }
    }


}
