package br.com.emprestaAi.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataInputVO {
	@NotEmpty(message = "Name is mandatory")
	@NotNull(message = "Name is mandatory")
	@Size( min = 4)
	private String name;
	@NotNull(message = "cpf is mandatory")
	@Size(min = 11)
	private String cpf;
	@Max(value = 99 ,message = "Age should not be greater than 99")
	@NotNull(message = "age is mandatory")
	private int age;	
	@NotNull(message = "uf is mandatory")
	@NotEmpty(message = "uf is mandatory")
	@Length(max = 2)
	private String uf;
	@NotNull(message = "salary is mandatory")
	private double salary;	
	
}
