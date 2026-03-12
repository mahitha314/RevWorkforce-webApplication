package com.revworkforce.adminserviceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.adminserviceImpl.SystemConfigServiceImpl;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;
import com.revworkforce.repository.DepartmentRepository;
import com.revworkforce.repository.DesignationRepository;

@ExtendWith(MockitoExtension.class)
public class SystemConfigServiceImplTest {

	@InjectMocks
	private SystemConfigServiceImpl service;

	@Mock
	private DepartmentRepository departmentRepository;

	@Mock
	private DesignationRepository designationRepository;

	@Mock
	private ActivityLogService activityLogService;

	@Mock
	private HttpServletRequest request; // 🔥 IMPORTANT

	private Department department;
	private Designation designation;

	@BeforeEach
	void setUp() {
		department = new Department();
		department.setId(1L);
		department.setName("IT");

		designation = new Designation();
		designation.setId(1L);
		designation.setTitle("Developer");
		designation.setDepartment(department);
	}

	@Test
	public void saveDepartment_Positive() {

		when(departmentRepository.save(any(Department.class))).thenReturn(department);

		Department saved = service.saveDepartment(department);

		assertNotNull(saved);
		assertEquals("IT", saved.getName());

		verify(departmentRepository).save(any(Department.class));
		verify(activityLogService).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	@Test
	public void getAllDepartments_Positive() {

		when(departmentRepository.findAll()).thenReturn(Arrays.asList(department));

		assertEquals(1, service.getAllDepartments().size());
	}

	@Test
	public void getDepartmentById_Positive() {

		when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

		Department found = service.getDepartmentById(1L);

		assertEquals("IT", found.getName());
	}

	@Test
	public void getDepartmentById_NotFound() {

		when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> {
			service.getDepartmentById(1L);
		});
	}

	@Test
	public void deleteDepartment_Positive() {

		when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

		service.deleteDepartment(1L);

		verify(departmentRepository).delete(department);
		verify(activityLogService).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	@Test
	public void saveDesignation_Positive() {

		when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

		when(designationRepository.save(any(Designation.class))).thenReturn(designation);

		Designation saved = service.saveDesignation(1L, designation);

		assertNotNull(saved);
		assertEquals("Developer", saved.getTitle());

		verify(designationRepository).save(any(Designation.class));
		verify(activityLogService).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	@Test
	public void getDesignationsByDepartmentId_Positive() {

		when(designationRepository.findByDepartmentId(1L)).thenReturn(Arrays.asList(designation));

		assertEquals(1, service.getDesignationsByDepartmentId(1L).size());
	}

	@Test
	public void deleteDesignation_Positive() {

		when(designationRepository.findById(1L)).thenReturn(Optional.of(designation));

		service.deleteDesignation(1L);

		verify(designationRepository).delete(designation);
		verify(activityLogService).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	@Test
	public void deleteDesignation_NotFound() {

		when(designationRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> {
			service.deleteDesignation(1L);
		});
	}

	@Test
	public void countDepartments_Test() {

		when(departmentRepository.count()).thenReturn(5L);

		assertEquals(5L, service.countDepartments());
	}

	@Test
	public void countDesignations_Test() {

		when(designationRepository.count()).thenReturn(3L);

		assertEquals(3L, service.countDesignations());
	}
}