package uk.gov.digital.ho.proving.financial.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public final class AccountBalance {
    private final LocalDate balanceDate;
    private final BigDecimal closingBalance;
}
