package com.datasite.practice.service

import com.datasite.practice.representation.ProjectMemberships
import com.datasite.practice.representation.RegisteredUsers
import com.datasite.practice.representation.UnRegisteredUsers
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class UserMembershipServiceImplSpec extends Specification {
    UserMembershipService userMembershipService
    String host
    String registeredUsersUri
    String unRegisteredUsersUri
    String projectMemberShipUri
    RestTemplate restTemplate

    def setup() {
        host = "localhost"
        registeredUsersUri = "/api/registeredUsersUri"
        unRegisteredUsersUri = "/api/unRegisteredUsersUri"
        projectMemberShipUri = "/api/projectMemberShipUri"
        restTemplate = Mock(RestTemplate)
        userMembershipService = new UserMembershipServiceImpl(registeredUsersUri: registeredUsersUri,
                unRegisteredUsersUri: unRegisteredUsersUri, projectMemberShipUri: projectMemberShipUri, restTemplate: restTemplate)
    }

    def "Method to test Happy Path scenario"() {
        given:
        List<RegisteredUsers> registeredUsers = [new RegisteredUsers(id: "1", emailAddress: "1@gmail.com"), new RegisteredUsers(id: "3", emailAddress: "1@gmail.com")]
        List<UnRegisteredUsers> unRegisteredUsers = [new UnRegisteredUsers(id: "2", emailAddress: "1@gmail.com"), new UnRegisteredUsers(id: "4", emailAddress: "1@gmail.com")]
        List<ProjectMemberships> projectMemberships = [new ProjectMemberships(id: "1", userId: "1", projectId: "2"), new ProjectMemberships(id: "2", userId: "1", projectId: "5")]

        when:
        def result = userMembershipService.getUserMembershipDetails()

        then:
        assert result
        result.size() == 4
        1 * restTemplate.exchange(_, _, _, _) >> { ResponseEntity.ok().body(registeredUsers) }
        1 * restTemplate.exchange(_, _, _, _) >> { ResponseEntity.ok().body(unRegisteredUsers) }
        1 * restTemplate.exchange(_, _, _, _) >> { ResponseEntity.ok().body(projectMemberships) }
    }

    def "Method to test failure scenario scenario"() {
        given:
        List<RegisteredUsers> registeredUsers = [new RegisteredUsers(id: "1", emailAddress: "1@gmail.com"), new RegisteredUsers(id: "3", emailAddress: "1@gmail.com")]

        when:
        def result = userMembershipService.getUserMembershipDetails()

        then:
        assert result
        result.size() == 2
        1 * restTemplate.exchange(_, _, _, _) >> { ResponseEntity.ok().body(registeredUsers) }
        1 * restTemplate.exchange(_, _, _, _) >> { throw new Exception() }
        1 * restTemplate.exchange(_, _, _, _) >> { throw new Exception() }
    }


}
