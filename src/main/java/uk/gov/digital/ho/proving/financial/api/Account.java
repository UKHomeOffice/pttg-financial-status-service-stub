package uk.gov.digital.ho.proving.financial.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Account {
    private final String accountId;
    private final String sortCode;
    private final String accountNumber;
    private final String currency;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final List<AccountBalance> balances;
}
