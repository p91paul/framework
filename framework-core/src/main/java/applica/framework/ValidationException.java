package applica.framework;

public class ValidationException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -7215525532409836031L;

    private ValidationResult validationResult;

    public ValidationException(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public void setValidationResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    @Override
    public synchronized Throwable getCause() {
        if (validationResult == null || validationResult.getErrors() == null)
            return super.getCause();
        return validationResult.getErrors().stream()
                .filter(e -> e.getException() != null)
                .map(ValidationResult.Error::getException)
                .findAny().orElse(super.getCause());
    }

}
