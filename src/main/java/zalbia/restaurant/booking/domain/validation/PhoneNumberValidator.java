package zalbia.restaurant.booking.domain.validation;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates phone number strings for {@link PhoneNumber}.
 * <p>
 * Local numbers are assumed to be from the Philippines.
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private static final String REGION = "PH";

    private PhoneNumberUtil phoneNumberUtil;

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        phoneNumberUtil = PhoneNumberUtil.getInstance();
    }

    /**
     * A phone number string is valid if it's a possible international number or a local number in the Philippines.
     */
    @Override
    public boolean isValid(String phoneNumberStr, ConstraintValidatorContext context) {
        return phoneNumberUtil.isPossibleNumber(phoneNumberStr, REGION);
    }
}
