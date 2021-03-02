package br.com.emprestaAi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.emprestaAi.model.DataInputVO;
import br.com.emprestaAi.model.DataOutputVO;
import br.com.emprestaAi.model.Modality;

@Service
public class LoanService {
	private static final String CEARA = "CE";
	private static final String CONSIGNADO = "Consignado";
	private static final String EMPRESTIMO_COM_GARANTIA = "Emprestimo com Garantia";
	private static final String EMPRESTIMO_PESSOAL = "Emprestimo Pessoal";
	List<Modality> modalityList;
	
	public DataOutputVO verifyLoan(DataInputVO data) {
		
		modalityList = new ArrayList<Modality>();
		
		if (isLowerThreeThousand(data)) {
			ageUnderThirtyAndLivingInCeara(data);
			addModalityList(EMPRESTIMO_PESSOAL, 4);
		} else if (isBiggerThreeThousandAndLowerFiveThousand(data)) {
			addModalityList(EMPRESTIMO_PESSOAL, 4);
			if(isLivingInCeara(data)) {
				addModalityList(EMPRESTIMO_COM_GARANTIA, 3);
			}
		} else if(isBiggerFiveThousand(data)){
			addModalityList(CONSIGNADO, 2);
			addModalityList(EMPRESTIMO_PESSOAL, 4);
			if(ageUnderThirty(data)) {
				addModalityList(EMPRESTIMO_COM_GARANTIA, 3);
			}
		}
		return new DataOutputVO(data.getName(), modalityList);
	}

	private void ageUnderThirtyAndLivingInCeara(DataInputVO data) {
		if(ageUnderThirty(data) && isLivingInCeara(data)) {
			addModalityList(EMPRESTIMO_COM_GARANTIA, 3);
		}
	}

	private boolean ageUnderThirty(DataInputVO data) {
		return data.getAge()<30;
	}

	private void addModalityList(String type , int tax) {
		modalityList.add(new Modality(type,tax));
	}
	private boolean isLivingInCeara(DataInputVO data) {
		return data.getUf().equals(CEARA);
	}

	private boolean isBiggerFiveThousand(DataInputVO data) {
		return data.getSalary()>=5000;
	}

	private boolean isBiggerThreeThousandAndLowerFiveThousand(DataInputVO data) {
		return data.getSalary() > 3000 && data.getSalary() < 5000 ;
	}

	private boolean isLowerThreeThousand(DataInputVO data) {
		return data.getSalary() <= 3000;
	}
}
