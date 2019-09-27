package io.github.needle.controller;

import io.github.kits.log.Logger;
import io.github.needle.annotations.Controller;
import io.github.needle.annotations.Param;
import io.github.needle.annotations.Router;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
@Controller(value = "/controller1")
public class ControllerTest1 {

	@Router(value = "method1")
	public String method1(@Param(value = "name") String name, @Param(value = "age") int age) {
		Logger.infof("WinterController1.Method1 received paramters: name = {}, age = {}", name, age);
		return name;
	}

	@Router(value = "/method2")
	public String method2(@Param(value = "name") String name, @Param(value = "age") int age) {
		Logger.infof("WinterController1.Method2 received paramters: name = {}, age = {}", name, age);
		return name;
	}

}
