package com.raulparamio.smarttransaction.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration.class
})
public class SmartTransactionLedgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartTransactionLedgerApplication.class, args);
	}

}