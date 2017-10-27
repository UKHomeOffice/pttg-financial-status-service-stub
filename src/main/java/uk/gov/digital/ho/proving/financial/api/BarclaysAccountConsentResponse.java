package uk.gov.digital.ho.proving.financial.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BarclaysAccountConsentResponse {
    private final ConsentResponse consent;
}
