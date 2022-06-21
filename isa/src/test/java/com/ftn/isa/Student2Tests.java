package com.ftn.isa;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ftn.isa.DTO.RegularResDTO;
import com.ftn.isa.model.Client;
import com.ftn.isa.model.Cottage;
import com.ftn.isa.model.RentalService;
import com.ftn.isa.model.Reservation;
import com.ftn.isa.services.ClientService;
import com.ftn.isa.services.CottageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import com.ftn.isa.services.ReservationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Student2Tests {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CottageService cottageService;

	@Test(expected = OptimisticLockingFailureException.class)
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
                newRes.setRental(cottageService.findById(4l));
                RentalService cot =  newRes.getRental();    //zbog lazy inicijalizacije

                reservationService.saveReservation(newRes, cottageService.findById(4l));    //poveze se rezervacija sa cottage drvence
                //spava 3 sekunde
                try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
            }
		});
		Future<?> future2 = executor.submit(new Runnable() {

			@Override
			public void run() {
				System.out.println("Startovan Thread 2");
				//try { Thread.sleep(150); } catch (InterruptedException e) { }// otprilike 150 milisekundi posle prvog threada krece da se izvrsava drugi

				RegularResDTO resDTO = new RegularResDTO(4l, LocalDateTime.now(), LocalDateTime.now().plusDays(3),
                        "client1@gmail.com", 100.0);
                reservationService.addNewRegularRes(resDTO, clientService.findByEmail("client1@gmail.com"), false, "COTTAGE_OWNER");
			}
		});
		try {
			future2.get(); // podize ExecutionException za bilo koji izuzetak iz drugog child threada
            future1.get();
		} catch (ExecutionException e) {
			System.out.println("Exception from thread " + e.getCause().getClass()); // u pitanju je bas OrptimisticLockingFailure
			throw e.getCause();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}

}
