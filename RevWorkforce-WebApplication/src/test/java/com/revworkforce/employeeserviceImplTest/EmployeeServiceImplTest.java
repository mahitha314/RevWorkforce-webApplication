package com.revworkforce.employeeserviceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.employeeserviceImpl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

	@InjectMocks
	private EmployeeServiceImpl service;

	@Mock
	private EmployeeRepository employeeRepo;

	private Employee employee;

	@BeforeEach
	void setUp() {
		employee = new Employee();
		employee.setId(1L);
		employee.setFirstName("John");
		employee.setLastName("Doe");
		employee.setEmail("john@test.com");
	}

	@Test
	public void findByEmail_Positive() {

		when(employeeRepo.findByEmail("john@test.com")).thenReturn(Optional.of(employee));

		Optional<Employee> result = service.findByEmail("john@test.com");

		assertTrue(result.isPresent());
		assertEquals("John", result.get().getFirstName());

		verify(employeeRepo).findByEmail("john@test.com");
	}

	@Test
	public void findByEmail_NotFound() {

		when(employeeRepo.findByEmail("abc@test.com")).thenReturn(Optional.empty());

		Optional<Employee> result = service.findByEmail("abc@test.com");

		assertFalse(result.isPresent());
	}

	@Test
	public void getAllEmployees_Positive() {

		when(employeeRepo.findAll()).thenReturn(Arrays.asList(employee));

		assertEquals(1, service.getAllEmployees().size());

		verify(employeeRepo).findAll();
	}

	@Test
	public void searchEmployees_NullKeyword() {

		when(employeeRepo.findAll()).thenReturn(Arrays.asList(employee));

		assertEquals(1, service.searchEmployees(null).size());

		verify(employeeRepo).findAll();
	}

	@Test
	public void searchEmployees_EmptyKeyword() {

		when(employeeRepo.findAll()).thenReturn(Arrays.asList(employee));

		assertEquals(1, service.searchEmployees(" ").size());

		verify(employeeRepo).findAll();
	}

	@Test
	public void searchEmployees_FirstAndLastName() {

		when(employeeRepo.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase("John", "Doe"))
				.thenReturn(Arrays.asList(employee));

		assertEquals(1, service.searchEmployees("John Doe").size());

		verify(employeeRepo).findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase("John", "Doe");
	}

	@Test
	public void searchEmployees_SingleKeyword() {

		when(employeeRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
				"John", "John", "John")).thenReturn(Arrays.asList(employee));

		assertEquals(1, service.searchEmployees("John").size());

		verify(employeeRepo)
				.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase("John",
						"John", "John");
	}

	@Test
	public void searchEmployees_NoResults() {

		when(employeeRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
				"XYZ", "XYZ", "XYZ")).thenReturn(Collections.emptyList());

		assertTrue(service.searchEmployees("XYZ").isEmpty());
	}

}