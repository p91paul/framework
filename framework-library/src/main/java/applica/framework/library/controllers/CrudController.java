package applica.framework.library.controllers;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 2/26/13
 * Time: 4:30 PM
 */

import applica.framework.*;
import applica.framework.builders.*;
import applica.framework.data.*;
import applica.framework.library.crud.acl.CrudAuthorizationException;
import applica.framework.library.crud.acl.CrudGuard;
import applica.framework.library.crud.acl.CrudPermission;
import applica.framework.library.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;

@RequestMapping("/crud")
public class CrudController extends LocalizedController {

    @Autowired(required = false)
    private CrudGuard crudGuard;

    @RequestMapping(value="/grid/{entity}")
    public @ResponseBody SimpleResponse grid(@PathVariable String entity, String loadRequest) {
        if(crudGuard != null) {
            try {
                crudGuard.check(CrudPermission.LIST, entity);
            } catch (CrudAuthorizationException e) {
                return new ErrorResponse("Unauthorized");
            }
        }

        GridResponse response = new GridResponse();

        StringWriter writer = new StringWriter();
        Grid grid;
        GridDataProvider dataProvider;
        try {
            grid = GridBuilder.instance().build(entity);
            dataProvider = GridDataProviderBuilder.instance().build(entity);
            dataProvider.load(grid, LoadRequest.fromJSON(loadRequest));
            grid.write(writer);
            response.setTitle(localization.getMessage("crud.grid.title." + entity));
            response.setSearchFormIncluded(grid.getSearchForm() != null);
            response.setFormIdentifier(grid.getFormIdentifier());
            response.setCurrentPage(grid.getCurrentPage());
            response.setPages(grid.getPages());
            response.setError(false);
        } catch (CrudConfigurationException e) {
            response.setError(true);
            response.setMessage("Crud configuration error: " + e.getMessage());
        } catch (GridCreationException e) {
            response.setError(true);
            response.setMessage("Error creating grid: " + e.getMessage());
        }

        response.setContent(writer.toString());
        return response;
    }

    @RequestMapping(value="/grid/{entity}/delete", method= RequestMethod.POST)
    public @ResponseBody SimpleResponse gridDelete(@PathVariable String entity, @RequestParam String ids) {
        if(crudGuard != null) {
            try {
                crudGuard.check(CrudPermission.DELETE, entity);
            } catch (CrudAuthorizationException e) {
                return new ErrorResponse("Unauthorized");
            }
        }

        SimpleResponse response = new SimpleResponse();

        DeleteOperation deleteOperation;
        try {
            deleteOperation = DeleteOperationBuilder.instance().build(entity);
            deleteOperation.delete(ids.split(","));
            response.setError(false);
        } catch (CrudConfigurationException e) {
            response.setError(true);
            response.setMessage("Crud configuration error: " + e.getMessage());
        }

        return response;
    }

    @RequestMapping(value="/form/{entity}", method=RequestMethod.GET)
    public @ResponseBody SimpleResponse form(@PathVariable String entity, String id) {
        if(crudGuard != null) {
            try {
                String crudPermission = StringUtils.hasLength(id) ? CrudPermission.EDIT : CrudPermission.NEW;
                crudGuard.check(crudPermission, entity);
            } catch (CrudAuthorizationException e) {
                return new ErrorResponse("Unauthorized");
            }
        }

        FormResponse response = new FormResponse();

        StringWriter writer = new StringWriter();
        Form form;
        FormDataProvider dataProvider;
        try {
            form = FormBuilder.instance().build(entity);
            dataProvider = FormDataProviderBuilder.instance().build(entity);
            dataProvider.load(form, id);
            form.write(writer);

            if(!form.isEditMode()) {
                response.setTitle(localization.getMessage(String.format("crud.form.create.%s", entity)));
            } else {
                response.setTitle(localization.getMessage(String.format("crud.form.edit.%s", entity)));
            }

            response.setError(false);
        } catch (FormProcessException e) {
            response.setError(true);
            response.setMessage("Error processing form: " + e.getMessage());
        } catch (CrudConfigurationException e) {
            response.setError(true);
            response.setMessage("Crud configuration error: " + e.getMessage());
        } catch (FormCreationException e) {
            response.setError(true);
            response.setMessage("Error creating form: " + e.getMessage());
        }

        response.setContent(writer.toString());

        return response;
    }

    @RequestMapping(value="/form/{entity}/save", method=RequestMethod.POST)
    public @ResponseBody SimpleResponse save(@PathVariable String entity, HttpServletRequest request) {
        if(crudGuard != null) {
            try {
                crudGuard.check(CrudPermission.SAVE, entity);
            } catch (CrudAuthorizationException e) {
                return new ErrorResponse("Unauthorized");
            }
        }

        FormActionResponse response = new FormActionResponse();
        SaveOperation saveOperation;
        try {
            Form form = FormBuilder.instance().build(entity);
            saveOperation = SaveOperationBuilder.instance().build(entity);
            saveOperation.save(form, request.getParameterMap());
            response.setValid(true);
        } catch(ValidationException e) {
            response.setError(false);
            response.setValid(false);
            response.setMessage("Validation error");
            response.setValidationResult(e.getValidationResult());
        } catch (FormProcessException e) {
            response.setError(true);
            response.setMessage("Error processing form");
        } catch (Exception e) {
            response.setError(true);
            response.setMessage("Error saving entity: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

}
