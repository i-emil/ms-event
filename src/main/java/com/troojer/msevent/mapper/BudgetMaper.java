package com.troojer.msevent.mapper;

import com.troojer.msevent.client.LocalizationClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.model.BudgetDto;
import org.springframework.stereotype.Component;

@Component
public class BudgetMaper {

    private final LocalizationClient localizationClient;

    public BudgetMaper(LocalizationClient localizationClient) {
        this.localizationClient = localizationClient;
    }

    public BudgetDto eventToBudgetDto(EventEntity eventEntity) {
        return BudgetDto.builder()
                .amount(eventEntity.getBudget())
                .currency(
                        localizationClient.getCurrencies().stream()
                                .filter(x -> x.getCode().equals(eventEntity.getCurrency())).findFirst().orElse(null)
                ).build();
    }
}
