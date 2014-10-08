package applica.framework.fileserver.controllers;

import applica.framework.fileserver.facade.EmptyFileException;
import applica.framework.fileserver.viewmodel.UIFileUpload;
import applica.framework.fileserver.viewmodel.UIImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import applica.framework.fileserver.facade.UploadFacade;
import applica.framework.library.controllers.LocalizedController;
import applica.framework.library.responses.*;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 07/03/14
 * Time: 15:01
 */
@RequestMapping("/upload")
public class UploadController extends LocalizedController {

    @Autowired
    private UploadFacade uploadFacade;

    @RequestMapping("/image")
    public @ResponseBody SimpleResponse uploadImage(UIImageUpload data) {

        try {
            String path = uploadFacade.uploadImage(data);
            return new ValueResponse(path);
        } catch (EmptyFileException e) {
            e.printStackTrace();
            return new ErrorResponse(localization.getMessage("msg.no_file_uploaded"));
        } catch (IOException e) {
            e.printStackTrace();
            return new ErrorResponse(localization.getMessage("msg.image_upload_error_generic"));
        }

    }

    @RequestMapping("/file")
    public @ResponseBody SimpleResponse uploadFile(UIFileUpload data) {

        try {
            String path = uploadFacade.uploadFile(data);
            String originalFileName = data.getFile().getOriginalFilename();
            ValueResponse r =  new ValueResponse(path);
            r.setMessage(originalFileName);
            return r;
        } catch (EmptyFileException e) {
            e.printStackTrace();
            return new ErrorResponse(localization.getMessage("msg.no_file_uploaded"));
        } catch (IOException e) {
            e.printStackTrace();
            return new ErrorResponse(localization.getMessage("msg.upload_error_generic"));
        }

    }

}