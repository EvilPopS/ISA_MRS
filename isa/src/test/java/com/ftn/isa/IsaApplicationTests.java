package com.ftn.isa;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ftn.isa.DTO.AnswerDTO;
import com.ftn.isa.DTO.ClientProfileDTO;
import com.ftn.isa.controllers.AdminController;
import com.ftn.isa.model.Client;
import com.ftn.isa.model.Request;
import com.ftn.isa.model.User;
import com.ftn.isa.services.AdminService;
import com.ftn.isa.services.ClientService;
import com.ftn.isa.services.RequestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import com.ftn.isa.services.ReservationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IsaApplicationTests {
	@Autowired
	private ReservationService reservationService;
	@Autowired
	private ClientService clientService;

	@Autowired
	private AdminService adminService;
	@Autowired
	private RequestService requestService;

	@Test(expected = ObjectOptimisticLockingFailureException.class)
	public void testAccDelRequests() throws Throwable {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try { Thread.sleep(150); } catch (InterruptedException ignored) { }// otprilike 150 milisekundi posle prvog threada krece da se izvrsava drugi
				adminService.delUserByReq(requestService.findOneById(1L), "allow");
			}
		});
		Future<?> future2 = executor.submit(new Runnable() {
			@Override
			public void run() {
				adminService.delUserByReq(requestService.findOneById(1L), "allow");
			}
		});

		try {
			future2.get();
		} catch (ExecutionException e) {
			System.out.println("Exception from thread " + e.getCause().getClass());
			throw e.getCause();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}

	@Test(expected = ObjectOptimisticLockingFailureException.class)
	public void testAccRegRequests() throws Throwable {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try { Thread.sleep(150); } catch (InterruptedException ignored) { }// otprilike 150 milisekundi posle prvog threada krece da se izvrsava drugi
				adminService.registerAllow(requestService.findOneById(1L), "allow");
			}
		});
		Future<?> future2 = executor.submit(new Runnable() {
			@Override
			public void run() {
				adminService.registerAllow(requestService.findOneById(1L), "allow");
			}
		});

		try {
			future2.get();
		} catch (ExecutionException e) {
			System.out.println("Exception from thread " + e.getCause().getClass());
			throw e.getCause();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}

	@Test(expected = ObjectOptimisticLockingFailureException.class)
	public void testReports() throws Throwable {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try { Thread.sleep(150); } catch (InterruptedException ignored) { }// otprilike 150 milisekundi posle prvog threada krece da se izvrsava drugi
				AnswerDTO dto = new AnswerDTO();
				dto.setReportId(10L);
				dto.setClientEmail("nekimail@maildrop.cc");
				adminService.answerOnReport("client", dto);
			}
		});
		Future<?> future2 = executor.submit(new Runnable() {
			@Override
			public void run() {
				AnswerDTO dto = new AnswerDTO();
				dto.setReportId(10L);
				dto.setClientEmail("nekimail@maildrop.cc");
				adminService.answerOnReport("client", dto);
			}
		});

		try {
			future2.get();
		} catch (ExecutionException e) {
			System.out.println("Exception from thread " + e.getCause().getClass());
			throw e.getCause();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}

	@Test(expected = ObjectOptimisticLockingFailureException.class)
	public void student1OptimisticLockingFailTest() throws Throwable {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try { Thread.sleep(150); }
				catch (InterruptedException ignored) { }
				clientService.resetPenalties();
			}
		});
		Future<?> future2 = executor.submit(new Runnable() {
			@Override
			public void run() {
				Client c = clientService.findByEmail("client1@gmail.com");
				ClientProfileDTO dto = new ClientProfileDTO(c);
				dto.setName("New name");
				clientService.updatePersonalInfo(dto, c);
			}
		});

		try {
			future2.get();
		} catch (ExecutionException e) {
			System.out.println("Exception from thread " + e.getCause().getClass());
			throw e.getCause();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}
}
