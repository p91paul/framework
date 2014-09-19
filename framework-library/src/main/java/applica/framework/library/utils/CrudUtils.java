package applica.framework.library.utils;

import applica.framework.*;
import applica.framework.builders.FormBuilder;
import applica.framework.builders.FormProcessorBuilder;
import applica.framework.builders.GridBuilder;
import applica.framework.data.Entity;
import applica.framework.data.LoadRequest;
import applica.framework.library.i18n.Localization;
import applica.framework.library.responses.FormResponse;
import applica.framework.library.responses.GridResponse;
import applica.framework.library.responses.SimpleResponse;
import applica.framework.mapping.*;
import applica.framework.processors.FormProcessor;
import applica.framework.utils.Paginator;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CrudUtils {

    public static SimpleResponse createFormResponse(Entity entity, String identifier) {
        FormResponse response = new FormResponse();

        try {
            Form form = FormBuilder.instance().build(identifier);
            form.setEditMode(true);
            form.setData(toMap(identifier, entity));

            response.setContent(form.writeToString());
            response.setError(false);
        } catch (FormCreationException e) {
            e.printStackTrace();

            response.setError(true);
            response.setMessage("Error creating form: " + e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();

            response.setError(true);
            response.setMessage("Crud configuration error: " + e.getMessage());
        } catch (FormProcessException e) {
            e.printStackTrace();

            response.setError(true);
            response.setMessage("Error processing form: " + e.getMessage());
        }

        return response;
    }

    public static FormResponse createFormResponse(HttpServletRequest request, String identifier) {
        FormResponse response = new FormResponse();

        try {
            Entity entity = CrudUtils.toEntity(identifier, request.getParameterMap());
            Form form = FormBuilder.instance().build(identifier);
            form.setEditMode(true);
            form.setData(toMap(identifier, entity));

            response.setContent(form.writeToString());
            response.setError(false);
        } catch (FormCreationException e) {
            e.printStackTrace();

            response.setError(true);
            response.setMessage("Error creating form: " + e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();

            response.setError(true);
            response.setMessage("Crud configuration error: " + e.getMessage());
        } catch (FormProcessException e) {
            e.printStackTrace();

            response.setError(true);
            response.setMessage("Error processing form: " + e.getMessage());
        } catch (ValidationException e) {
            //not handled
        }

        return response;
    }

    public static GridResponse createGridResponse(List<? extends Entity> entities, String identifier) {
        return createGridResponse(entities, identifier, null, null);
    }

    public static GridResponse createGridResponse(List<? extends Entity> entities, String identifier, LoadRequest loadRequest) {
        return createGridResponse(entities, identifier, null, loadRequest);
    }

    public static GridResponse createGridResponse(List<? extends Entity> entities, String identifier, GridInitializer initializer) {
        return createGridResponse(entities, identifier, initializer, null);
    }

    public static GridResponse createGridResponse(List<? extends Entity> entities, String identifier, GridInitializer initializer, LoadRequest loadRequest) {
        GridResponse response = new GridResponse();

        try {
            Grid grid = GridBuilder.instance().build(identifier);
            setGridData(grid, entities);

            if (initializer != null) {
                initializer.initialize(grid);
            }

            if (grid.getSearchForm() != null && loadRequest != null) {
                grid.setSearched(loadRequest.getFilters().size() > 0);
                grid.getSearchForm().setData(loadRequest.filtersMap());
            }
            response.setContent(grid.writeToString());
            response.setError(false);
        } catch (GridCreationException e) {
            e.printStackTrace();

            response.setError(true);
            response.setMessage("Error creating grid: " + e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();

            response.setError(true);
            response.setMessage("Crud configuration error: " + e.getMessage());
        }

        return response;
    }

    public static GridResponse createGridResponse(String identifier, Paginator paginator) {
        GridResponse response = new GridResponse();

        try {
            Grid grid = GridBuilder.instance().build(identifier);

            if (paginator != null) {
                grid.setCurrentPage(paginator.getPage());
            }

            paginator.getLoadRequest().setRowsPerPage(grid.getRowsPerPage());
            paginator.paginate();
            grid.setPages(paginator.getPages());
            grid.setCurrentPage(paginator.getPage());

            setGridData(grid, paginator.getEntities());

            response.setContent(grid.writeToString());
            response.setError(false);
        } catch (GridCreationException e) {
            e.printStackTrace();

            response.setError(true);
            response.setMessage("Error creating grid: " + e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();

            response.setError(true);
            response.setMessage("Crud configuration error: " + e.getMessage());
        }

        return response;
    }

    public static void setGridData(Grid grid, List<? extends Entity> entities) {
        List<Map<String, Object>> data = new ArrayList<>();
        GridDataMapper mapper = new SimpleGridDataMapper();
        mapper.mapGridDataFromEntities(grid.getDescriptor(), data, entities);
        grid.setData(data);
    }

    public static Map<String, Object> requestDataToFormData(Map<String, String[]> requestData) {
        Map<String, Object> data = new HashMap<String, Object>();
        for (Entry<String, String[]> param : requestData.entrySet()) {
            Object value = param.getValue().length > 1 ? param.getValue() : param.getValue()[0];
            data.put(param.getKey(), value);
        }

        return data;
    }

    public static void validateEntity(Entity entity, String identifier, WebApplicationContext applicationContext) throws ValidationException {

        Validator validator = (Validator) applicationContext.getBean("validator-" + identifier);
        Localization localization = new Localization(applicationContext);
        Map<Object, Object> map = new HashMap<>();
        MapBindingResult result = new MapBindingResult(map, entity.getClass().getName());
        ValidationResult validationResult = new ValidationResult();
        if (validator != null) {
            validator.validate(entity, result);
            if (result.hasErrors()) {
                for (ObjectError error : result.getAllErrors()) {
                    FieldError ferror = (FieldError) error;
                    if (ferror != null) {
                        String message = error.getDefaultMessage();
                        if (localization != null) {
                            message = localization.getMessage(message);
                        }
                        validationResult.rejectValue(ferror.getField(), message);
                    }
                }
            }
        }
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult);
        }
    }

    public static List<String> valueToStrings(Object value) {
        List<String> ids = new ArrayList<>();
        if (value != null) {
            if (value instanceof String[]) {
                String[] values = (String[]) value;
                for (String id : values) {
                    ids.add(id);
                }
            } else {
                ids.add(value.toString());
            }
        }

        return ids;
    }

    public static Entity toEntity(String identifier, Map<String, String[]> data) throws FormCreationException, CrudConfigurationException, FormProcessException, ValidationException {
        Form form = FormBuilder.instance().build(identifier);
        FormProcessor processor = FormProcessorBuilder.instance().build(identifier);
        Class<? extends Entity> type = CrudConfiguration.instance().getFormTypeFromIdentifier(identifier);
        Entity entity = processor.toEntity(form, type, data, null);

        return entity;
    }

    public static Entity toEntityWithValidation(String identifier, Map<String, String[]> data) throws FormCreationException, CrudConfigurationException, FormProcessException, ValidationException {
        Form form = FormBuilder.instance().build(identifier);
        FormProcessor processor = FormProcessorBuilder.instance().build(identifier);
        Class<? extends Entity> type = CrudConfiguration.instance().getFormTypeFromIdentifier(identifier);
        ValidationResult result = new ValidationResult();
        Entity entity = processor.toEntity(form, type, data, result);

        if(!result.isValid()) {
            throw new ValidationException(result);
        }

        return entity;
    }

    public static Map<String, Object> toMap(String identifier, Entity entity) throws FormCreationException, CrudConfigurationException, FormProcessException {
        Form form = FormBuilder.instance().build(identifier);
        FormProcessor processor = FormProcessorBuilder.instance().build(identifier);
        Class<? extends Entity> type = CrudConfiguration.instance().getFormTypeFromIdentifier(identifier);
        Map<String, Object> map = processor.toMap(form, entity);

        return map;
    }

    public interface GridInitializer {
        void initialize(Grid grid);
    }
}
