package net.sarri.friends.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import net.sarri.friends.domain.business.UserData;
import net.sarri.friends.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService service;

	/**
	 * Get my profile
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser("testuser1")
	public void findByUsername_me() throws Exception {
		UserData user = UserData.builder().username("testuser1").password("password").visible(true).declinesCount(0)
				.dateJoined(LocalDateTime.now()).build();

		when(service.findByUsernameAndVisibleIsTrue("testuser1")).thenReturn(user);
		when(service.me()).thenReturn(user);

		mockMvc.perform(get("/users/{username}", "testuser1")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("username", is("testuser1"))).andExpect(jsonPath("visible", notNullValue()));
	}

	/**
	 * User not visible
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser("testuser2")
	public void findByUsername_notVisible() throws Exception {
		UserData user = UserData.builder().username("testuser1").password("password").visible(false).declinesCount(0)
				.dateJoined(LocalDateTime.now()).build();

		when(service.findByUsernameAndVisibleIsTrue("testuser1")).thenReturn(null);
		when(service.me()).thenReturn(user);

		mockMvc.perform(get("/users/{username}", "testuser1")).andDo(print()).andExpect(status().isNotFound());
	}

	/**
	 * Other users than you than check your profile can't see the visible attribute
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser("testuser1")
	public void findByUsername_otherUser() throws Exception {
		UserData user1 = UserData.builder().username("testuser1").password("password").visible(true).declinesCount(0)
				.dateJoined(LocalDateTime.now()).build();
		UserData user2 = UserData.builder().username("testuser2").password("password").visible(true).declinesCount(0)
				.dateJoined(LocalDateTime.now()).build();

		when(service.findByUsernameAndVisibleIsTrue("testuser2")).thenReturn(user2);
		when(service.me()).thenReturn(user1);

		mockMvc.perform(get("/users/{username}", "testuser2")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("username", is("testuser2"))).andExpect(jsonPath("visible").doesNotExist());
	}
}
