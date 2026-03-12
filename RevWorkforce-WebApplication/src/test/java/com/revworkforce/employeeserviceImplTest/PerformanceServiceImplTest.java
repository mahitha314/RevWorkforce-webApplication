package com.revworkforce.employeeserviceImplTest;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.PerformanceReviewDTO;
import com.revworkforce.employeeserviceImpl.PerformanceServiceImpl;
import com.revworkforce.exception.EmployeeNotFoundException;
import com.revworkforce.model.Employee;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.PerformanceReviewRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceImplTest {

	@Mock
	private PerformanceReviewRepository reviewRepo;

	@Mock
	private EmployeeRepository employeeRepo;

	@Mock
	private NotificationService notificationService;

	@Mock
	private ActivityLogService activityLogService;

	@InjectMocks
	private PerformanceServiceImpl performanceService;

	private Employee employee;

	@BeforeEach
	void setup() {
		employee = new Employee();
		employee.setId(1L);
		employee.setFirstName("John");
		employee.setLastName("Doe");
	}

	@Test
	void createSelfReview_shouldReturnCreatedResponse() {

		PerformanceReviewDTO dto = new PerformanceReviewDTO();
		dto.setAccomplishments("Completed project");
		dto.setDeliverables("Delivered API");
		dto.setAreasOfImprovement("Time management");
		dto.setSelfRating(4);

		when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));
		when(reviewRepo.save(any(PerformanceReview.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ApiResponse response = performanceService.createSelfReview(1L, dto);

		assertEquals(201, response.getStatus());
		verify(reviewRepo, times(1)).save(any(PerformanceReview.class));
		verify(activityLogService, times(1)).log(eq(1L), anyString());
	}

	@Test
	void submitReview_shouldUpdateStatusAndNotify() {

		PerformanceReview review = new PerformanceReview();
		review.setId(10L);
		review.setEmployee(employee);
		review.setStatus("Draft");

		when(reviewRepo.findById(10L)).thenReturn(Optional.of(review));

		ApiResponse response = performanceService.submitReview(10L);

		assertEquals(200, response.getStatus());
		assertEquals("Submitted", review.getStatus());

		verify(reviewRepo, times(1)).save(review);
		verify(activityLogService, times(1)).log(eq(employee.getId()), anyString());
	}

	@Test
	void deleteReview_shouldDeleteIfDraft() {

		PerformanceReview review = new PerformanceReview();
		review.setId(20L);
		review.setEmployee(employee);
		review.setStatus("Draft");

		when(reviewRepo.findById(20L)).thenReturn(Optional.of(review));

		ApiResponse response = performanceService.deleteReview(20L);

		assertEquals(200, response.getStatus());
		verify(reviewRepo, times(1)).delete(review);
	}

	@Test
	void deleteReview_shouldNotDeleteIfSubmitted() {

		PerformanceReview review = new PerformanceReview();
		review.setId(30L);
		review.setEmployee(employee);
		review.setStatus("Submitted");

		when(reviewRepo.findById(30L)).thenReturn(Optional.of(review));

		ApiResponse response = performanceService.deleteReview(30L);

		assertEquals(400, response.getStatus());
		verify(reviewRepo, never()).delete(any());
	}

	@Test
	void createSelfReview_shouldThrowExceptionIfEmployeeNotFound() {

		PerformanceReviewDTO dto = new PerformanceReviewDTO();

		when(employeeRepo.findById(99L)).thenReturn(Optional.empty());

		assertThrows(EmployeeNotFoundException.class, () -> performanceService.createSelfReview(99L, dto));
	}
}