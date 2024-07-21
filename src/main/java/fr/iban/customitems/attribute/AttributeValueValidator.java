package fr.iban.customitems.attribute;

@FunctionalInterface
public interface AttributeValueValidator {

    boolean validate(String value);

    default String errorMessage() {
        return "Invalid value.";
    }
}
