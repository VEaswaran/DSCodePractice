package com.datasite.practice.representation

import lombok.Builder
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@Builder
class ProjectMemberships {
    String id
    String projectId
    String userId
}
