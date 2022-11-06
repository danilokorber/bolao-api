package io.easyware.shared.keycloak;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class KeycloakUserAttributes {
    List<String> picture;
    List<String> locale;
    List<String> country;
    List<Date> payment_on;
    List<String> first;
    List<String> second;
    List<String> third;
    List<String> fourth;
    List<Date> data_privacy_accepted_on;
    List<Date> rules_accepted_on;
}
