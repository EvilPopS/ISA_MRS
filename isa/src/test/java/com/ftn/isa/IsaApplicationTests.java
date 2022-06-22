package com.ftn.isa;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ftn.isa.DTO.*;
import com.ftn.isa.controllers.AdminController;
import com.ftn.isa.model.*;
import com.ftn.isa.services.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

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
	@Autowired
	private CottageOwnerService cottageOwnerService;

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

	@Test(expected = ObjectOptimisticLockingFailureException.class)
	@Rollback(true)
	public void testClientAndOwnerRes() throws Throwable {

		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<?> future1 = executor.submit(new Runnable() {

			@Override
			public void run() {
				System.out.println("Startovan Thread 1");
				Reservation newRes = new Reservation(LocalDateTime.now(), LocalDateTime.now().plusDays(3),
						false, 150.0, true, false, "");
				newRes.setCanceled(false);
				newRes.setClient(clientService.findByEmail("client1@gmail.com"));
				//Cottage cottage = cottageService.findById(4l); //na ovaj nacin se javi lazy iz nekog razloga
				//newRes.setRental(cottage);

				CottageOwner cottageOwner = cottageOwnerService.findByEmail("srdjan@gmail.com");
				Cottage c = null;
				for (Cottage cottage : cottageOwner.getCottages()){
					c = cottage;
					break;
				}
				newRes.setRental(c);
				reservationService.saveReservation(newRes, c);    //poveze se rezervacija sa cottage drvence
				try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
			}
		});
		Future<?> future2 = executor.submit(new Runnable() {

			@Override
			public void run() {
				System.out.println("Startovan Thread 2");
				try { Thread.sleep(150); } catch (InterruptedException ignored) { }
				RegularResDTO resDTO = new RegularResDTO(4L, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
						"client1@gmail.com", 100.0);
				reservationService.addNewRegularRes(resDTO, clientService.findByEmail("client1@gmail.com"), false, "COTTAGE_OWNER");
			}
		});
		try {
			future2.get();
		} catch (ExecutionException e) {
			System.out.println("Exception from thread " + e.getCause().getClass()); // u pitanju je bas OrptimisticLockingFailure
			throw e.getCause();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}


	@Test(expected = ObjectOptimisticLockingFailureException.class)
	@Rollback(true)
	public void testClientAndOwnerActionRes() throws Throwable {

		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<?> future1 = executor.submit(new Runnable() {

			@Override
			public void run() {
				System.out.println("Startovan Thread 1");
				Reservation newRes = new Reservation(LocalDateTime.now(), LocalDateTime.now().plusDays(3),
						false, 150.0, true, false, "");
				newRes.setCanceled(false);
				newRes.setClient(clientService.findByEmail("client1@gmail.com"));

				CottageOwner cottageOwner = cottageOwnerService.findByEmail("srdjan@gmail.com");
				Cottage c = null;
				for (Cottage cottage : cottageOwner.getCottages()){
					c = cottage;
					break;
				}
				newRes.setRental(c);
				reservationService.saveReservation(newRes, c);    //poveze se rezervacija sa cottage drvence
				try { Thread.sleep(350); } catch (InterruptedException e) { e.printStackTrace(); }
			}
		});
		Future<?> future2 = executor.submit(new Runnable() {

			@Override
			public void run() {
				System.out.println("Startovan Thread 2");
				//try { Thread.sleep(150); } catch (InterruptedException e) { }// otprilike 150 milisekundi posle prvog threada krece da se izvrsava drugi

				ActionResDTO resDTO = new ActionResDTO(LocalDateTime.now(), LocalDateTime.now().plusDays(3),
						100.0, "", 4l);
				reservationService.addNewActionRes(resDTO, "COTTAGE_OWNER");
			}
		});
		try {
			future2.get(); // podize ExecutionException za bilo koji izuzetak iz drugog child threada
			//future1.get();
		} catch (ExecutionException e) {
			System.out.println("Exception from thread " + e.getCause().getClass()); // u pitanju je bas OrptimisticLockingFailure
			throw e.getCause();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}

	@Test(expected = ObjectOptimisticLockingFailureException.class)
	@Rollback(true)
	public void testChangingDataAndReservation() throws Throwable {

		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<?> future1 = executor.submit(new Runnable() {

			@Override
			public void run() {
				System.out.println("Startovan Thread 1");
				Reservation newRes = new Reservation(LocalDateTime.now(), LocalDateTime.now().plusDays(3),
						false, 150.0, true, false, "");
				newRes.setCanceled(false);
				newRes.setClient(clientService.findByEmail("client1@gmail.com"));
				//Cottage cottage = cottageService.findById(4l); //na ovaj nacin se javi lazy iz nekog razloga
				//newRes.setRental(cottage);

				CottageOwner cottageOwner = cottageOwnerService.findByEmail("srdjan@gmail.com");
				Cottage c = null;
				for (Cottage cottage : cottageOwner.getCottages()){
					c = cottage;
					break;
				}
				newRes.setRental(c);
				try { Thread.sleep(250); } catch (InterruptedException e) { e.printStackTrace(); }
				reservationService.saveReservation(newRes, c);    //poveze se rezervacija sa cottage drvence
			}
		});
		Future<?> future2 = executor.submit(new Runnable() {

			@Override
			public void run() {
				System.out.println("Startovan Thread 2");

				CottageOwner cottageOwner = cottageOwnerService.findByEmail("srdjan@gmail.com");
				Cottage c = null;
				for (Cottage cottage : cottageOwner.getCottages()){
					c = cottage;
					break;
				}

				CottageDTO cottageDTO = new CottageDTO(c);
				cottageOwnerService.save(cottageOwner, cottageDTO, new HashSet<>());
			}
		});
		try {
			future2.get();
		} catch (ExecutionException e) {
			System.out.println("Exception from thread " + e.getCause().getClass()); // u pitanju je bas OrptimisticLockingFailure
			throw e.getCause();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}
}
