package fr.iban.customitems.attribute;

import java.util.function.Predicate;

public class CustomAttributeValue<T> {
    
    private final String name;
    private final Predicate<T> validator;

    public CustomAttributeValue(String name, Predicate<T> validator) {
        this.name = name;
        this.validator = validator;
    }

    public String getName() {
        return name;
    }

    public boolean isValid(T value) {
        return validator.test(value);
    }
}