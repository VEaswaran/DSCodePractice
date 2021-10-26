package com.datasite.practice

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableAsync


@EnableAsync
@SpringBootApplication
@ComponentScan("com.datasite.practice")
class Application {

	static void main(String[] args) {
		SpringApplication.run(Application, args)
	}

}
