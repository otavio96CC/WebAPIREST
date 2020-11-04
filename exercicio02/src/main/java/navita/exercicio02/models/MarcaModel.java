package navita.exercicio02.models;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "marcas")
public class MarcaModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "incrementDomain")
    @GenericGenerator(name = "incrementDomain", strategy = "increment")
	private Long idMarca;
	
	@Column(nullable = false, unique = true)
	private String nome;

	public MarcaModel() {
		super();
	}

	public MarcaModel(String nome) {
		super();
		this.nome = nome;
	}

	public long getIdMarca() {
		return idMarca;
	}

	public void setIdMarca(long idMarca) {
		this.idMarca = idMarca;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
