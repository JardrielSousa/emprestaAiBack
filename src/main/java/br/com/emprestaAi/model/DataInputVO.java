package br.com.emprestaAi.model;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataInputVO {
	@NotEmpty(message = "Name is mandatory")
	@NotNull(message = "Name is mandatory")
	private String name;
	@NotNull(message = "cpf is mandatory")
	private String cpf;
	@NotNull(message = "age is mandatory")
	private int age;	
	@NotNull(message = "uf is mandatory")
	private String uf;
	@NotNull(message = "salary is mandatory")
	private double salary;	
	
}
