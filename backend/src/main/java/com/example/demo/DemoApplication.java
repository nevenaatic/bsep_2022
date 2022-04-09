package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.service.CertificateExample;
import com.example.demo.service.CertificateGenerator;
import com.example.demo.service.KeyStoreWriter;

@SpringBootApplication
public class DemoApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		CertificateExample certificateExample = new CertificateExample();
		certificateExample.testIt();
	}

}
