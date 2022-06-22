package com.ftn.isa;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ftn.isa.DTO.ClientProfileDTO;
import com.ftn.isa.model.Client;
import com.ftn.isa.services.ClientService;
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



//	@Test(expected = ObjectOptimisticLockingFailureException.class)
//	public void testPessimisticLockingScenario() throws Throwable {
//
//		ExecutorService executor = Executors.newFixedThreadPool(2);
//		executor.submit(new Runnable() {
//			@Override
//			public void run() {
//				try { Thread.sleep(150); } catch (InterruptedException ignored) { }// otprilike 150 milisekundi posle prvog threada krece da se izvrsava drugi
//				//service1
//			}
//		});
//		Future<?> future2 = executor.submit(new Runnable() {
//			@Override
//			public void run() {
//				//service2
//			}
//		});
//
//		try {
//			future2.get(); // podize ExecutionException za bilo koji izuzetak iz drugog child threada
//		} catch (ExecutionException e) {
//			System.out.println("Exception from thread " + e.getCause().getClass()); // u pitanju je bas PessimisticLockingFailureException
//			throw e.getCause();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		executor.shutdown();
//	}

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
