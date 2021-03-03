package br.com.emprestaAi.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.emprestaAi.model.DataInputVO;
import br.com.emprestaAi.model.DataOutputVO;
import br.com.emprestaAi.service.LoanService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/v1/loan")
public class LoanController {
	
	private LoanService loanService;
	
	public LoanController(LoanService loanService) {
		this.loanService = loanService;
	}

	@PostMapping
	public ResponseEntity<?> postLoan(@Valid @RequestBody DataInputVO data){
			log.info("receiving data:"+data);
			DataOutputVO dataOutputVO = loanService.verifyLoan(data);
			log.info("analysing data:"+data);
		 return ResponseEntity.status(HttpStatus.OK).body(dataOutputVO);
	}
}
