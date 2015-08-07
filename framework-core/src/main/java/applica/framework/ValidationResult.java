package applica.framework;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    List<Error> errors = new ArrayList<>();

    public void rejectValue(String property, String message) {
        errors.add(new Error(property, message));
    }

    public void rejectValue(String property, Throwable message) {
        errors.add(new Error(property, message));
    }

    public boolean isValid() {
        return errors != null && errors.isEmpty();
    }

    public List<Error> getErrors() {
        return errors;
    }

    public boolean isValid(String property) {
        for (Error error : errors) {
            if (error.getProperty().equals(property)) {
                return false;
            }
        }

        return true;
    }

    public String getErrorMessage(String property) {
        for (Error error : errors) {
            if (error.getProperty().equals(property)) {
                return error.getMessage();
            }
        }

        return null;
    }

    public class Error {
        private String property;
        private String message;
        private Throwable exception;

        public Error(String property, String message) {
            super();
            this.property = property;
            this.message = message;
        }

        public Error(String property, Throwable exception) {
            this(property, exception.getMessage());
            this.exception = exception;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Throwable getException() {
            return exception;
        }

        public void setException(Throwable exception) {
            this.exception = exception;
        }
    }
}
