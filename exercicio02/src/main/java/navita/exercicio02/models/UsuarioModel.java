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
@Table(name = "usuarios")
public class UsuarioModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "incrementDomain")
    @GenericGenerator(name = "incrementDomain", strategy = "increment")
	private long idUsuario;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String senha;

	public UsuarioModel() {
		super();
	}

	public UsuarioModel(String nome, String email, String senha) {
		super();
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
