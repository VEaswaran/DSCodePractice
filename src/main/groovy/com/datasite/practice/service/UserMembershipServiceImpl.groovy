package com.datasite.practice.service

import com.datasite.practice.common.Constants
import com.datasite.practice.representation.ProjectMemberships
import com.datasite.practice.representation.RegisteredUsers
import com.datasite.practice.representation.UnRegisteredUsers
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import java.util.concurrent.CompletableFuture

@Service
@Slf4j
class UserMembershipServiceImpl implements UserMembershipService {

    @Autowired
    RestTemplate restTemplate

    @Value('${datasite.host}')
    private String host

    @Value('${datasite.registeredusers.uri}')
    private String registeredUsersUri

    @Value('${datasite.unregisteredusers.uri}')
    private String unRegisteredUsersUri

    @Value('${datasite.projectmemberships.uri}')
    private String projectMemberShipUri


    List<Object> getUserMembershipDetails() {
        List<Object> objectList = []
        CompletableFuture<List<RegisteredUsers>> registeredUsersFuture = callRegisteredUsers()
        CompletableFuture<List<UnRegisteredUsers>> unRegisteredUsersFuture = callUnRegisteredUsers()
        CompletableFuture<List<ProjectMemberships>> projectMembershipsFuture = callProjectMemberships()

        try {
            if (projectMembershipsFuture.get() != null) {
                checkAndUpdateProjectIds(projectMembershipsFuture, registeredUsersFuture, objectList, unRegisteredUsersFuture)
            } else {
                registeredUsersFuture?.get() ? objectList.addAll(registeredUsersFuture?.get()) : null
                unRegisteredUsersFuture?.get() ? objectList.addAll(unRegisteredUsersFuture?.get()) : null
            }
        }
        catch (Exception ex) {
            log.error("unable to process the records and returning null")
        }
        objectList
    }

    private void checkAndUpdateProjectIds(CompletableFuture<List<ProjectMemberships>> projectMembershipsFuture, CompletableFuture<List<RegisteredUsers>> registeredUsersFuture, objectList, CompletableFuture<List<UnRegisteredUsers>> unRegisteredUsersFuture) {
        Map<String, List> userProjectMap = projectMembershipsFuture.get().groupBy { it.userId }.collectEntries { [it.key, it.value.collect { it.projectId }] }
        registeredUsersFuture.get().parallelStream().forEach(it -> {
            if (userProjectMap.get(it.id) != null) {
                it.projectIds.addAll(userProjectMap.get(it.id))
            }
            objectList.add(it)
        })
        unRegisteredUsersFuture.get().parallelStream().forEach(it -> {
            if (userProjectMap.get(it.id) != null) {
                it.projectIds.addAll(userProjectMap.get(it.id))
            }
            objectList.add(it)
        })
    }


    @Async
    private CompletableFuture<List<RegisteredUsers>> callRegisteredUsers() {
        log.info("prepare the contract to call Registered Users service api")
        RegisteredUsers[] registeredUsers = callDSClient(registeredUsersUri, RegisteredUsers[].class, Constants.REGISTERED_USER)?.getBody() as RegisteredUsers[]
        CompletableFuture.completedFuture(registeredUsers != null ? registeredUsers.toList() : null)
    }

    @Async
    private CompletableFuture<List<UnRegisteredUsers>> callUnRegisteredUsers() {
        log.info("prepare the contract to call Un Registered Users service api")
        UnRegisteredUsers[] unRegisteredUsers = callDSClient(unRegisteredUsersUri, UnRegisteredUsers[].class, Constants.UNREGISTERED_USER)?.getBody() as UnRegisteredUsers[]
        CompletableFuture.completedFuture(unRegisteredUsers != null ? Arrays.asList(unRegisteredUsers) : null)
    }

    @Async
    private CompletableFuture<List<ProjectMemberships>> callProjectMemberships() {
        log.info("prepare the contract to call Project Memberships service api")
        ProjectMemberships[] projectMemberships = callDSClient(projectMemberShipUri, ProjectMemberships[].class, Constants.PROJECT_MEMBERSHIP)?.getBody() as ProjectMemberships[]
        CompletableFuture.completedFuture(projectMemberships !=null ? projectMemberships.toList() : null)
    }


    private ResponseEntity callDSClient(String uri, Class aClass, String serviceName) {
        ResponseEntity responseEntity = null
        try {
            HttpHeaders headers = new HttpHeaders()
            HttpEntity<String> entity = new HttpEntity<String>(headers)
            responseEntity = restTemplate.exchange(host + uri, HttpMethod.GET, entity, aClass)
            log.info(serviceName + " Successfully processed with status:  {}", responseEntity.statusCode)
        } catch (Exception ex) {
            log.error("External uri {} failed  for following reason", uri, ex.stackTrace)
        }
        responseEntity
    }


}
