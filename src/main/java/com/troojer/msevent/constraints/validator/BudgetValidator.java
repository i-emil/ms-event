package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.client.LocalizationClient;
import com.troojer.msevent.constraints.CurrencyParameters;
import com.troojer.msevent.model.CurrencyDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BudgetValidator
        implements ConstraintValidator<CurrencyParameters, CurrencyDto> {

    private final LocalizationClient localizationClient;

    public BudgetValidator(LocalizationClient locationClient) {
        this.localizationClient = locationClient;
    }

    @Override
    public boolean isValid(CurrencyDto currency, ConstraintValidatorContext context) {
        return currency != null && currency.getCode() != null &&
                localizationClient.getCurrencies()
                        .contains(new CurrencyDto(currency.getCode()));
    }
}
