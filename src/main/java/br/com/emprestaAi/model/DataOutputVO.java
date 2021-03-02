package br.com.emprestaAi.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataOutputVO {
	private String name;
	private List<Modality> modality;
}
