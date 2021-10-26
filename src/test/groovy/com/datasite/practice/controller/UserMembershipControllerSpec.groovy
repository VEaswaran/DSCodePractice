package com.datasite.practice.controller

import com.datasite.practice.representation.ProjectMemberships
import com.datasite.practice.representation.RegisteredUsers
import com.datasite.practice.representation.UnRegisteredUsers
import com.datasite.practice.service.UserMembershipService
import com.datasite.practice.service.UserMembershipServiceImpl
import spock.lang.Specification

class UserMembershipControllerSpec extends Specification {

    UserMembershipController userMembershipController
    UserMembershipService userMembershipService

    def setup() {
        userMembershipService = Mock(UserMembershipService)
        userMembershipController = new UserMembershipController(userMembershipService: userMembershipService)
    }

    def "Method to test Happy path"() {
        given:
        List<ProjectMemberships> projectMemberships = [new ProjectMemberships(userId: 1, projectId: 3), new ProjectMemberships(userId: 1, projectId: 4), new ProjectMemberships(userId: 2, projectId: 5)]
        List<Object> objectList = [new RegisteredUsers(id: "'1", projectIds: ["1", "2"]), new UnRegisteredUsers(id: "1", projectIds: ["1"])]
        when:
        def result = userMembershipController.getUserMembershipDetails()
        then:
        assert result.getBody().size() == 2
        1 * userMembershipService.getUserMembershipDetails() >> { objectList }
    }

}
