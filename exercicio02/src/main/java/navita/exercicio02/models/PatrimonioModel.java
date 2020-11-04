package navita.exercicio02.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "patrimonios")
public class PatrimonioModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "incrementDomain")
    @GenericGenerator(name = "incrementDomain", strategy = "increment")
	private long idPatrimonio;
	
	@Column(nullable = false)
	private String nome;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idMarca")
	private MarcaModel marcaId;
	
	@Column
	private String descricao;

	@Column
	private long numeroTombo;
	
	public PatrimonioModel() {
		super();
	}

	public PatrimonioModel(String nome, MarcaModel marcaId, String descricao, long numeroTombo) {
		super();
		this.nome = nome;
		this.marcaId = marcaId;
		this.descricao = descricao;
		this.numeroTombo = numeroTombo;
	}

	public MarcaModel getMarcaId() {
		return marcaId;
	}

	public void setMarcaId(MarcaModel marcaId) {
		this.marcaId = marcaId;
	}

	public long getIdPatrimonio() {
		return idPatrimonio;
	}

	public void setIdPatrimonio(long idPatrimonio) {
		this.idPatrimonio = idPatrimonio;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public long getNumeroTombo() {
		return numeroTombo;
	}

	public void setNumeroTombo(long numeroTombo) {
		this.numeroTombo = numeroTombo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
