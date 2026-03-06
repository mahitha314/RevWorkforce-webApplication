package com.revworkforce.managerserviceImplTest;

import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.PerformanceReviewDTO;
import com.revworkforce.managerserviceImpl.PerformanceReviewServiceImpl;
import com.revworkforce.model.Employee;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.PerformanceReviewRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerformanceReviewServiceImplTest {

	@Mock
	private PerformanceReviewRepository reviewRepo;

	@Mock
	private EmployeeRepository employeeRepo;

	@Mock
	private NotificationService notificationService;

	@Mock
	private ActivityLogService activityLogService;

	@InjectMocks
	private PerformanceReviewServiceImpl service;

	private Employee manager;
	private Employee employee;
	private PerformanceReview review;

	@BeforeEach
	void setup() {

		manager = new Employee();
		manager.setId(100L);

		employee = new Employee();
		employee.setId(200L);
		employee.setFirstName("John");
		employee.setLastName("Doe");
		employee.setManager(manager);

		review = new PerformanceReview();
		review.setId(1L);
		review.setEmployee(employee);
		review.setStatus("Submitted");
	}

	@Test
	void getTeamReviews_managerNotFound() {

		when(employeeRepo.existsById(100L)).thenReturn(false);

		ApiResponse response = service.getTeamPerformanceReviews(100L);

		assertEquals(404, response.getStatus());
	}

	@Test
	void getTeamReviews_success() {

		when(employeeRepo.existsById(100L)).thenReturn(true);
		when(reviewRepo.findByEmployee_Manager_Id(100L)).thenReturn(Collections.singletonList(review));

		ApiResponse response = service.getTeamPerformanceReviews(100L);

		assertEquals(200, response.getStatus());
		verify(activityLogService, times(1)).log(eq(100L), anyString());
	}

	@Test
	void submitFeedback_reviewNotFound() {

		PerformanceReviewDTO dto = new PerformanceReviewDTO();
		dto.setReviewId(1L);

		when(reviewRepo.findById(1L)).thenReturn(Optional.empty());

		ApiResponse response = service.submitManagerFeedback(100L, dto);

		assertEquals(404, response.getStatus());
	}

	@Test
	void submitFeedback_unauthorizedManager() {

		Employee otherManager = new Employee();
		otherManager.setId(999L);
		employee.setManager(otherManager);

		when(reviewRepo.findById(1L)).thenReturn(Optional.of(review));

		PerformanceReviewDTO dto = new PerformanceReviewDTO();
		dto.setReviewId(1L);

		ApiResponse response = service.submitManagerFeedback(100L, dto);

		assertEquals(403, response.getStatus());
	}

	@Test
	void submitFeedback_reviewAlreadyCompleted() {

		review.setStatus("REVIEWED");

		when(reviewRepo.findById(1L)).thenReturn(Optional.of(review));

		PerformanceReviewDTO dto = new PerformanceReviewDTO();
		dto.setReviewId(1L);

		ApiResponse response = service.submitManagerFeedback(100L, dto);

		assertEquals(400, response.getStatus());
	}

	@Test
	void submitFeedback_notSubmittedYet() {

		review.setStatus("Draft");

		when(reviewRepo.findById(1L)).thenReturn(Optional.of(review));

		PerformanceReviewDTO dto = new PerformanceReviewDTO();
		dto.setReviewId(1L);

		ApiResponse response = service.submitManagerFeedback(100L, dto);

		assertEquals(400, response.getStatus());
	}

	@Test
	void submitFeedback_invalidRating() {

		when(reviewRepo.findById(1L)).thenReturn(Optional.of(review));

		PerformanceReviewDTO dto = new PerformanceReviewDTO();
		dto.setReviewId(1L);
		dto.setManagerRating(6); // invalid

		ApiResponse response = service.submitManagerFeedback(100L, dto);

		assertEquals(400, response.getStatus());
	}

	@Test
	void submitFeedback_success() {

		when(reviewRepo.findById(1L)).thenReturn(Optional.of(review));

		PerformanceReviewDTO dto = new PerformanceReviewDTO();
		dto.setReviewId(1L);
		dto.setManagerRating(4);
		dto.setManagerFeedback("Good performance");

		ApiResponse response = service.submitManagerFeedback(100L, dto);

		assertEquals(200, response.getStatus());
		assertEquals("REVIEWED", review.getStatus());

		verify(reviewRepo, times(1)).save(review);
		verify(notificationService, times(1)).createNotification(any());
		verify(activityLogService, times(1)).log(eq(100L), anyString());
	}
}