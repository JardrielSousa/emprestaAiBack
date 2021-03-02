package br.com.emprestaAi.model;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataInputVO {
	private String name;
	private String cpf;
	private Long age;	
	private String uf;
	private double salary;	
	
}
