package dev.ateam.controlmocks;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ResourceProperties.Content;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.google.gson.Gson;

import dev.ateam.entities.AppUser;
import dev.ateam.services.UserService;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
@ContextConfiguration(classes = dev.ateam.app.RevPointRestServiceApplication.class) // mouthfull
class  UserControllerMock {

	@Autowired
	MockMvc mockmvc;

	@MockBean
	@Qualifier("UserServiceDATA")
	UserService us;

	@Test
	void getAllUser() throws Exception{
		// Design tip, use super fake data just in case it persists to prevent possible
		// errors
		Set<AppUser> users = new HashSet<AppUser>();
		users.add(new AppUser(1,"TestUser","Pass","crash test dummy"));
		users.add(new AppUser(2,"TestUser2","Pass","crash test dummy"));
		Mockito.when(us.allUsers()).thenReturn(users);
		Gson gson = new Gson();
		String json = gson.toJson(users);
		System.out.println(json);
		// requires throws
		// mockmvc.perform is a fake of a route
		// data it takes in is a url?
		ResultActions rs = mockmvc.perform(get("/associates"));
		rs.andExpect(status().isOk());
		// rs.andExpect(content().json("{{id:0,name:'Fakey McFake',
		// points:0},{id:1,name:'MilleVanilli',points:-50}}"));
		rs.andExpect(content().json(json));
	}

	@Test
	void addAssociate()  throws Exception{
		String json = "{id:3,name:'Pastiche', points:0}";
		// spring framework import
		Gson gson = new Gson();
		AppUser user = gson.fromJson(json, AppUser.class);
		Mockito.when(us.createUser(user)).thenReturn(user);
		ResultActions ra = mockmvc.perform(post("/associates").contentType(MediaType.APPLICATION_JSON_VALUE).content(json));
		ra.andExpect(status().isOk());
	}

}
