package br.com.emprestaAi.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.emprestaAi.controller.LoanController;
import br.com.emprestaAi.model.DataInputVO;
import br.com.emprestaAi.model.DataOutputVO;
import br.com.emprestaAi.model.Modality;
import br.com.emprestaAi.service.LoanService;

@WebMvcTest(LoanController.class)
public class LoanTest {

	private static final String APPLICATION_JSON = "application/json";
	private static final String V1_LOAN = "/v1/loan";
	private static final String SAO_PAULO = "SP";
	private static final String MATO_GROSSO_DO_SUL = "MS";
	private static final String RIO_GRANDE_DO_NORTE = "RN";
	private static final String CEARA = "CE";
	private static final String CONSIGNED = "Consignado";
	private static final String LOAN_WITH_GUARANTEE = "Emprestimo com Garantia";
	private static final String PERSONAL_LOAN = "Emprestimo Pessoal";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LoanService loanService;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	private void setUp() {
		loanService = new LoanService();
	}

	@Test
	public void test01HasSalaryEqualsThreeThousandAndLivingInCeara() throws Exception {
		DataInputVO data = new DataInputVO("teste", "111.111.111-11", 33, CEARA, 3000.00);

		mockMvc.perform(post(V1_LOAN).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(data)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(data);

		assertEquals(dataOutputVO.getName(), data.getName());
		dataOutputVO.getModality().forEach(modality -> {
			assertEquals(modality.getType(), PERSONAL_LOAN);
		});
	}

	@Test
	public void test02HasSalaryEqualsThreeThousandAndLivingInCearaAndAgeUnderThirty() throws Exception {
		DataInputVO data = new DataInputVO("teste", "111.111.111-11", 27, CEARA, 3000.00);

		mockMvc.perform(post(V1_LOAN).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(data)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(data);

		assertEquals(dataOutputVO.getName(), data.getName());
		assertEquals(dataOutputVO.getModality().size(), 2);
	}

	@Test
	public void test03HasSalaryLowerThreeThousand() throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 20, MATO_GROSSO_DO_SUL, 1500.00);

		mockMvc.perform(
				post(V1_LOAN).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		dataOutputVO.getModality().forEach(modality -> {
			assertEquals(modality.getType(), PERSONAL_LOAN);
		});
	}

	@Test
	public void test04LivingCearaAndHasSalaryLowerThreeThousand() throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 20, CEARA, 1500.00);

		mockMvc.perform(
				post(V1_LOAN).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		assertEquals(dataOutputVO.getModality().size(), 2);
	}

	@Test
	public void test05HasSalaryBiggerThirtyAndLowerFiveThousand() throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 20, RIO_GRANDE_DO_NORTE, 4500.00);

		mockMvc.perform(
				post(V1_LOAN).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		assertEquals(dataOutputVO.getModality().size(), 1);
		dataOutputVO.getModality().forEach(modality -> {
			assertEquals(modality.getType(), PERSONAL_LOAN);
		});
	}

	@Test
	public void test06HasSalaryBiggerThirtyAndLowerFiveThousandAndLivingInCeara()
			throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 20, CEARA, 4500.00);

		mockMvc.perform(
				post(V1_LOAN).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		assertEquals(dataOutputVO.getModality().size(), 2);
		dataOutputVO.getModality().forEach(modality -> {
			if(verifyModalityWithWarranty(modality)) {
				assertEquals(modality.getType(), LOAN_WITH_GUARANTEE);
			}
		});
	}


	@Test
	public void test07HasSalaryBiggerThirtyAndLowerFiveThousand() throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 30, SAO_PAULO, 4500.00);

		mockMvc.perform(
				post(V1_LOAN).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		dataOutputVO.getModality().forEach(modality -> {
			assertEquals(modality.getType(), PERSONAL_LOAN);
		});
	}

	@Test
	public void test08HasSalaryIsBiggerFiveThousandAndAgeUnderThirty() throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 27, SAO_PAULO, 7000.00);

		mockMvc.perform(
				post(V1_LOAN).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		assertEquals(dataOutputVO.getModality().size(), 3);
		dataOutputVO.getModality().forEach(modality -> {
			if(verifyModalityWitconsigned(modality)) {
				assertEquals(modality.getType(), CONSIGNED);
			}
		});
	}

	@Test
	public void test09HasSalaryIsBiggerFiveThousandAndAgeOverThirty() throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 35, SAO_PAULO, 7000.00);

		mockMvc.perform(
				post(V1_LOAN).contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		assertEquals(dataOutputVO.getModality().size(), 2);
		
	}
	

	private boolean verifyModalityWitconsigned(Modality modality) {
		return modality.getType().equals(CONSIGNED);
	}
	
	private boolean verifyModalityWithWarranty(Modality modality) {
		return modality.getType().equals(LOAN_WITH_GUARANTEE);
	}
}
