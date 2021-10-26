package com.datasite.practice.representation

import lombok.Builder
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@Builder
class UnRegisteredUsers {
    String id
    String emailAddress
    String languageCode
    String registrationId
    String registrationIdGeneratedTime
    List<String> projectIds = []
}
