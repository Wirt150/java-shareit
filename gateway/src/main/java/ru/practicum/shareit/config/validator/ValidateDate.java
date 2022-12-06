package config.validator;


import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateDate.DateValidator.class)
@Documented
public @interface ValidateDate {

    String message() default "{ru.practicum.shareit.config.validator.ValidateDate.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class DateValidator implements ConstraintValidator<ValidateDate, BookingDtoResponse> {

        @Override
        public boolean isValid(BookingDtoResponse value, ConstraintValidatorContext context) {
            return value.getEnd().isAfter(value.getStart());
        }
    }
}
