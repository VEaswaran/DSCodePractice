package com.datasite.practice.representation

import com.fasterxml.jackson.annotation.JsonInclude
import lombok.Getter
import lombok.Setter

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
class RegisteredUsers {
    String id
    String city
    String company
    String country
    String firstName
    String lastName
    String organizationType
    String phone
    String state
    String zipCode
    boolean disclaimerAccepted
    String languageCode
    String emailAddress
    List<String> projectIds = []
}
