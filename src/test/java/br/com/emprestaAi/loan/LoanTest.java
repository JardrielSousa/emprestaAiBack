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
import br.com.emprestaAi.service.LoanService;

@WebMvcTest(LoanController.class)
public class LoanTest {

	private static final String CEARA = "CE";
	private static final String CONSIGNADO = "Consignado";
	private static final String EMPRESTIMO_COM_GARANTIA = "Emprestimo com Garantia";
	private static final String EMPRESTIMO_PESSOAL = "Emprestimo Pessoal";

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
		DataInputVO data = new DataInputVO("teste", "111.111.111-11", 33, "CE", 3000.00);

		mockMvc.perform(post("/v1/loan").contentType("application/json").content(objectMapper.writeValueAsString(data)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(data);

		assertEquals(dataOutputVO.getName(), data.getName());
		dataOutputVO.getModality().forEach(modality -> {
			assertEquals(modality.getType(), EMPRESTIMO_PESSOAL);
		});
	}

	@Test
	public void test02HasSalaryEqualsThreeThousandAndLivingInCearaAndAgeUnderThirty() throws Exception {
		DataInputVO data = new DataInputVO("teste", "111.111.111-11", 27, "CE", 3000.00);

		mockMvc.perform(post("/v1/loan").contentType("application/json").content(objectMapper.writeValueAsString(data)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(data);

		assertEquals(dataOutputVO.getName(), data.getName());
		assertEquals(dataOutputVO.getModality().size(), 2);
	}

	@Test
	public void test03HasSalaryLowerThreeThousand() throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 20, "MS", 1500.00);

		mockMvc.perform(
				post("/v1/loan").contentType("application/json").content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		dataOutputVO.getModality().forEach(modality -> {
			assertEquals(modality.getType(), EMPRESTIMO_PESSOAL);
		});
	}

	@Test
	public void test04LivingCearaAndHasSalaryLowerThreeThousand() throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 20, "CE", 1500.00);

		mockMvc.perform(
				post("/v1/loan").contentType("application/json").content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		assertEquals(dataOutputVO.getModality().size(), 2);
	}

	@Test
	public void test05HasSalaryBiggerThirtyAndLowerFiveThousand() throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 20, "RN", 4500.00);

		mockMvc.perform(
				post("/v1/loan").contentType("application/json").content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		assertEquals(dataOutputVO.getModality().size(), 1);
		dataOutputVO.getModality().forEach(modality -> {
			assertEquals(modality.getType(), EMPRESTIMO_PESSOAL);
		});
	}

	@Test
	public void test06HasSalaryBiggerThirtyAndLowerFiveThousandAndLivingInCeara()
			throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 20, "CE", 4500.00);

		mockMvc.perform(
				post("/v1/loan").contentType("application/json").content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		assertEquals(dataOutputVO.getModality().size(), 2);
	}

	@Test
	public void test07HasSalaryBiggerThirtyAndLowerFiveThousand() throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 30, "SP", 4500.00);

		mockMvc.perform(
				post("/v1/loan").contentType("application/json").content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		dataOutputVO.getModality().forEach(modality -> {
			assertEquals(modality.getType(), EMPRESTIMO_PESSOAL);
		});
	}

	@Test
	public void test08HasSalaryIsBiggerFiveThousandAndAgeUnderThirty() throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 27, "SP", 7000.00);

		mockMvc.perform(
				post("/v1/loan").contentType("application/json").content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		assertEquals(dataOutputVO.getModality().size(), 3);
	}

	@Test
	public void test09HasSalaryIsBiggerFiveThousandAndAgeOverThirty() throws JsonProcessingException, Exception {
		DataInputVO dataInputVO = new DataInputVO("teste", "111.111.111-11", 35, "SP", 7000.00);

		mockMvc.perform(
				post("/v1/loan").contentType("application/json").content(objectMapper.writeValueAsString(dataInputVO)))
				.andExpect(status().isOk());

		DataOutputVO dataOutputVO = loanService.verifyLoan(dataInputVO);

		assertEquals(dataOutputVO.getName(), dataInputVO.getName());
		assertEquals(dataOutputVO.getModality().size(), 2);
	}
}
