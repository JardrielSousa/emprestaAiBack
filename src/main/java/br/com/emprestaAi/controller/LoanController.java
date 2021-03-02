package br.com.emprestaAi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.emprestaAi.model.DataInputVO;
import br.com.emprestaAi.service.LoanService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/loan")
public class LoanController {
	
	@Autowired
	private LoanService loanService;
	
	@GetMapping
	public ResponseEntity<String> getLoans() {
		 return ResponseEntity.status(HttpStatus.OK).body("Hello world");
	}
	
	@PostMapping
	public ResponseEntity<?> postLoan(@RequestBody DataInputVO data){
			log.info("receiving data"+data);
		 return ResponseEntity.status(HttpStatus.OK).body(loanService.verifyLoan(data));
	}
}
