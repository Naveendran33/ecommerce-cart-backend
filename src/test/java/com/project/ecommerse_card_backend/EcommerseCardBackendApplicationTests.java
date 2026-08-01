package com.project.ecommerse_card_backend;

import org.junit.jupiter.api.Test;

// This test would load the full Spring context which requires a running PostgreSQL database.
// Since our unit tests use Mockito and don't need a real DB, we skip the full context load here.
class EcommerseCardBackendApplicationTests {

	@Test
	void contextLoads() {
		// Unit tests are in the service/ and controller/ packages.
		// This placeholder avoids a database connection requirement during CI testing.
	}

}
