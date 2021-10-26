package com.datasite.practice.controller

import com.datasite.practice.service.UserMembershipService
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/user")
@Slf4j
class UserMembershipController {

    @Autowired
    UserMembershipService userMembershipService

    @GetMapping("/membership/details")
    ResponseEntity<List<Object>> getUserMembershipDetails(){
        List<Object> list = userMembershipService.getUserMembershipDetails()
        return ResponseEntity.ok().body(list);
    }

}
