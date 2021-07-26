package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.beans.CreateUserRequest;
import com.example.demo.beans.ModifyCartRequest;
import com.example.demo.controller.CartController;
import com.example.demo.controller.ItemController;
import com.example.demo.controller.OrderController;
import com.example.demo.controller.UserController;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.entity.UserOrder;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthCourseApplicationTests {
	
	@Autowired
	UserController userController;
	
	@Autowired
	CartController cartController;
	
	@Autowired
	ItemController itemController;
	
	@Autowired
	OrderController orderController;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	
	
	@Test
	void contextLoads() {
	}
		
	
	public void intilizeItems() {
		Item item1 = new Item();
		item1.setName("Round Widget");
		item1.setPrice(BigDecimal.valueOf(2.99));
		item1.setDescription("A widget that is round");
		System.out.println(item1.toString());
		itemRepository.save(item1);
		Item item2 = new Item();
		item2.setName("Square Widget");
		item2.setPrice(BigDecimal.valueOf(1.99));
		item2.setDescription("A widget that is square");
		itemRepository.save(item2);
	}
	
	
	
	@Test
	@Order(1)
	public void submitUserOrderFailure() throws Exception{
		ResponseEntity<UserOrder> response = orderController.submit("asaleh");
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}
	
	@Test
	@Order(2)
	public void UserOrdersHistoryFailure() throws Exception{
		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("asaleh");
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}
	
	
	@Test
	@Order(3)
	public void testChangeItemToUserCartFailCase1() {
		ModifyCartRequest request = new ModifyCartRequest("asaleh", 1, 2);
		System.out.println(request.toString());
		ResponseEntity<Cart> response = cartController.addTocart(request);
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}
	
	@Test
	@Order(3)
	public void testChangeItemToUserCartFailCase2() {
		ModifyCartRequest request = new ModifyCartRequest();
		request.setUsername("asaleh");
		request.setQuantity(2);
		request.setItemId(1);
		ResponseEntity<Cart> response = cartController.removeFromcart(request);
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}
	
	
	@Test
	@Order(4)
	public void testCreateUserSuccessfullCase() {
		CreateUserRequest request = new CreateUserRequest();
		System.out.println(request.toString());
		request.setUsername("asaleh");
		request.setPassword1("123456789");
		request.setPassword2("123456789");
		ResponseEntity<Object> result = userController.createUser(request);
		assertNotNull(result);
		ResponseEntity<User> response = userController.findById(1L);
		assertNotNull(response);
		assertEquals(1L, response.getBody().getId());
		response = userController.findByUserName("asaleh");
		assertNotNull(response);
		assertEquals(1L, response.getBody().getId());
	}
	
	@Test
	@Order(5)
	public void testChangeItemToUserCartFailCase3() {
		ModifyCartRequest request = new ModifyCartRequest("asaleh", 1, 2);
		ResponseEntity<Cart> response = cartController.addTocart(request);
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}
	
	@Test
	@Order(5)
	public void testChangeItemToUserCartFailCase4() {
		ModifyCartRequest request = new ModifyCartRequest("asaleh", 1, 2);
		ResponseEntity<Cart> response = cartController.removeFromcart(request);
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}
	
	
	@Test
	@Order(6)
	public void ListItemsTest() {
		intilizeItems();
		ResponseEntity<List<Item>> response = itemController.getItems();
		assertNotNull(response);
		assertEquals(2, response.getBody().size());
	}
	
	
	
	@Test
	@Order(7)
	public void testChangeItemToUserCartSuccessCase() {
		ModifyCartRequest request = new ModifyCartRequest("asaleh", 1, 2);
		ResponseEntity<Cart> response = cartController.addTocart(request);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		request = new ModifyCartRequest("asaleh", 1, 1);
		response = cartController.removeFromcart(request);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
	}
	
	
	@Test
	@Order(8)
	public void submitUserOrderSuccess() throws Exception{
		ResponseEntity<UserOrder> response = orderController.submit("asaleh");
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
	}
	
	@Test
	@Order(9)
	public void UserOrdersHistorySuccess() throws Exception{
		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("asaleh");
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
	}
	
	@Test
	@Order(10)
	public void testFindItemByIdSuccessCase() {
		ResponseEntity<Item> response = itemController.getItemById(1L);
		assertNotNull(response);
		assertEquals(200,response.getStatusCodeValue());
	}
	
	@Test
	@Order(11)
	public void testFindItemByItemNameSuccessCase() {
		ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");
		assertNotNull(response);
		assertEquals(200,response.getStatusCodeValue());
	}
	
	@Test
	public void testFindItemByIdFailCase() {
		ResponseEntity<Item> response = itemController.getItemById(10L);
		assertNotNull(response);
		assertEquals(404,response.getStatusCodeValue());
	}
	
	@Test
	public void testFindItemByItemNameFailCase() {
		ResponseEntity<List<Item>> response = itemController.getItemsByName("door");
		assertNotNull(response);
		assertEquals(404,response.getStatusCodeValue());
	}

	@Test
	public void createUserFailureCaseEmptyPasswords() throws Exception{
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername("asaleh");
		request.setPassword1("");
		request.setPassword2("");
		ResponseEntity<Object> response = userController.createUser(request);
		assertNotNull(response);
		assertEquals(500, response.getStatusCodeValue());
		String error = (String) response.getBody();
		assertEquals(error, "Password cann't be empty");
	}
	
	@Test
	public void searchForNotFoundUsername() throws Exception{
		ResponseEntity<User> response = userController.findByUserName("admin");
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());
	}
	
	@Test
	public void createUserFailureCaseMisMatchPasswords() throws Exception{
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername("asaleh");
		request.setPassword1("123456");
		request.setPassword2("123456789");
		ResponseEntity<Object> response = userController.createUser(request);
		assertNotNull(response);
		assertEquals(500, response.getStatusCodeValue());
		String error = (String) response.getBody();
		assertEquals(error, "Provided passwords are not matched");
	}
	
	@Test
	public void createUserFailureCasePasswordLengthIssue() throws Exception{
		CreateUserRequest request = new CreateUserRequest();
		request.setUsername("asaleh");
		request.setPassword1("123456");
		request.setPassword2("123456");
		ResponseEntity<Object> response = userController.createUser(request);
		assertNotNull(response);
		assertEquals(500, response.getStatusCodeValue());
		String error = (String) response.getBody();
		assertEquals(error, "password length must be higher than 8 characters");
	}
	
	@Test
	public void listAllUsers() {
		List<User> users = userRepository.findAll();
		assertNotNull(users);
	}
	
	@Test
	public void listAllOrders() {
		List<UserOrder> Orders = orderRepository.findAll();
		assertNotNull(Orders);
	}
	
	@Test
	public void listAllCarts() {
		List<Cart> Carts = cartRepository.findAll();
		assertNotNull(Carts);
	}
	
	@Test
	public void findOrderById() {
		Optional<UserOrder> order = orderRepository.findById(2L);
		assertEquals(false, order.isPresent());
	}
	
	@Test
	public void countUsers() {
		userRepository.count();
	}
	
	@Test
	public void countcarts() {
		cartRepository.count();
	}
	
	@Test
	public void countOrders() {
		orderRepository.count();
	}
	
	@Test
	public void countItems() {
		itemRepository.count();
	}
	
	@Test 
	public void saveItemList() {
		Item i1 = new Item();
		i1.setName("item1");
		i1.setPrice(BigDecimal.valueOf(1));
		i1.setDescription("Desc1");
		Item i2 = new Item();
		i2.setName("item2");
		i2.setPrice(BigDecimal.valueOf(1));
		i2.setDescription("Desc2");
		List<Item> items = new ArrayList<Item>();
		items.add(i1);
		items.add(i2);
		itemRepository.saveAll(items);
	}
	
	@Test 
	public void saveUserList() {
		User u1 = new User();
		u1.setUsername("user1");
		u1.setPassword("xpassword");
		User u2 = new User();
		u2.setUsername("user2");
		u2.setPassword("xpassword");
		List<User> users = new ArrayList<User>();
		users.add(u1);
		users.add(u2);
		userRepository.saveAll(users);
	}

	
	
	@Test
	public void deleteAllOrders() {
		orderRepository.deleteAll();
	}
	
	@Test
	public void findCartId() {
		Optional<Cart> cart = cartRepository.findById(2L);
		assertEquals(false, cart.isPresent());
	}
	
	@Test
	public void deleteAllCarts() {
		cartRepository.deleteAll();
	}

}
