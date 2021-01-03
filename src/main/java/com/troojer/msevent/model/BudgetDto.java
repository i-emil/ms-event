package com.troojer.msevent.model;

import com.troojer.msevent.constraints.CurrencyParameters;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BudgetDto {

    @Range(min = 1, max = 1000, message = "event.budgetAmount.range", groups = {CreateValidation.class, UpdateValidation.class})
    private Integer amount;

    @CurrencyParameters(param = "budget.currency", message = "event.budgetCurrency.incorrect", groups = {CreateValidation.class, UpdateValidation.class})
    private CurrencyDto currency;
}
