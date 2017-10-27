package uk.gov.digital.ho.proving.financial.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Account {
    private final long accountId;
    private final long sortCode;
    private final long accountNumber;
    private final String currency;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final List<AccountBalance> balances;
}
