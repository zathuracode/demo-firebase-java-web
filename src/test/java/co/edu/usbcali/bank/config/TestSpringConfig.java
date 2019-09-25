package co.edu.usbcali.bank.config;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/applicationContext.xml")
class TestSpringConfig {
	
	@PersistenceContext
	EntityManager entityManager;

	@Test
	void test() {
		assertNotNull(entityManager);
	}

}
