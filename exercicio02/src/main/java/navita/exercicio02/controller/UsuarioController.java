package navita.exercicio02.controller;

import java.io.UnsupportedEncodingException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import navita.exercicio02.dto.LoginDTO;
import navita.exercicio02.erros.CampoVazio;
import navita.exercicio02.erros.DadoIgual;
import navita.exercicio02.erros.NotFound;
import navita.exercicio02.erros.SemAuth;
import navita.exercicio02.models.UsuarioModel;
import navita.exercicio02.repositorio.UsuarioRepositorio;

@RestController
@RequestMapping(value="usuario")
public class UsuarioController {

	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	
	void autenticarToken(String token) {
		if(token == "") {
			throw new SemAuth("Sem autenticação.");
		}
	}
	
	@GetMapping("/listausuarios")
	public List<UsuarioModel> listaUsuarios(@RequestHeader (value = "token") String token) throws JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException{
		autenticarToken(token);
		return usuarioRepositorio.findAll();
	}
	
	@GetMapping("/{email}")
	public ResponseEntity<UsuarioModel> findUsuario(@RequestHeader (value = "token") String token, @PathVariable (value = "email") String email) throws Exception, JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException {
		autenticarToken(token);
		verificaContemUsuario(email);
		UsuarioModel user = usuarioRepositorio.findByEmail(email).orElseThrow((()->new Exception("Algo deu errado")));
		return ResponseEntity.ok().body(user);
	}
	
	private void verificaContemUsuario(String email) {
		Optional<UsuarioModel> existsUser =  usuarioRepositorio.findByEmail(email);
		if(existsUser.equals(Optional.empty())) {
			throw new NotFound("Usuario nao encontrado com o email: "+email);
		}
	}
	private void userAlreadyExists(String email) {
		Optional<UsuarioModel> existsUser =  usuarioRepositorio.findByEmail(email);
		if(existsUser.equals(Optional.empty()) == false) {
			throw new DadoIgual("O email: " + email + " ja existe!  ");
		}
	}
	
	@PostMapping("/cadastro")
	public UsuarioModel cadastroUsuario(@RequestBody UsuarioModel usuario) {
		if(usuario.getNome() == null || usuario.getEmail() == null || usuario.getSenha() == null) {
			throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
		}
		if(usuario.getNome().isEmpty() || usuario.getEmail().isEmpty() || usuario.getSenha().isEmpty()) {
			throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
		}
		userAlreadyExists(usuario.getEmail());
		return usuarioRepositorio.save(usuario);
	}
	
	@PutMapping("/{email}")
	public ResponseEntity<UsuarioModel> atualizarUsuario(@RequestHeader (value = "token") String token, @PathVariable (value = "email") String email, @RequestBody UsuarioModel dadosUser) throws Exception, JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException{
		autenticarToken(token);
		verificaContemUsuario(email);
		UsuarioModel user = usuarioRepositorio.findByEmail(email).orElseThrow((()->new Exception("Algo deu errado")));
		if(dadosUser.getNome() == null && dadosUser.getEmail() == null && dadosUser.getSenha() == null) {
			throw new CampoVazio("Tem que haver pelo menos um campo para ser atualizado.");
		}
		if(dadosUser.getNome() != null ) {
			if(dadosUser.getNome().contentEquals(user.getNome())) {
				throw new DadoIgual("O Dado nao pode ser igual ao antigo!");
			}
			if(dadosUser.getNome().isEmpty()) {	
				throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
			}
			user.setNome(dadosUser.getNome());
		}
		if(dadosUser.getEmail() != null ){
			if(dadosUser.getEmail().contentEquals(user.getEmail())) {
				throw new DadoIgual("O Dado nao pode ser igual ao antigo!");
			}
			if(dadosUser.getEmail().isEmpty()) {
				throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
			}
			userAlreadyExists(dadosUser.getEmail());
			user.setEmail(dadosUser.getEmail());
		}
		if(dadosUser.getSenha() != null) {
			if(dadosUser.getSenha().contentEquals(user.getSenha())) {
				throw new DadoIgual("O Dado nao pode ser igual ao antigo!");
			}
			if(dadosUser.getSenha().isEmpty()) {
				throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
			}
			user.setSenha(dadosUser.getSenha());
		}
		usuarioRepositorio.save(user);
		return ResponseEntity.ok().body(user);
	}
	
	@DeleteMapping("/{email}")
	public ResponseEntity<?> deletarUsuario(@RequestHeader (value = "token") String token, @PathVariable (value = "email") String email) throws Exception, JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException {
		autenticarToken(token);
		verificaContemUsuario(email);
		UsuarioModel user = usuarioRepositorio.findByEmail(email).orElseThrow((()->new Exception("Algo deu errado")));
		usuarioRepositorio.delete(user);
		return ResponseEntity.ok().build();
		
	}
	
	@PostMapping("/login")
	public void login(@RequestBody LoginDTO dados, HttpServletResponse response) throws Exception {
		if(dados.getEmail() == null || dados.getSenha() == null ) {
			throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
		}
		if(dados.getEmail().isEmpty() || dados.getSenha().isEmpty() ) {
			throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
		}
		verificaContemUsuario(dados.getEmail());
		UsuarioModel user = usuarioRepositorio.findByEmail(dados.getEmail()).orElseThrow((()->new Exception("Algo deu errado")));
		if(dados.getSenha().contentEquals(user.getSenha()) == false){
			throw new SemAuth("Senha errada!");
		}
		
		Date horaExpirar = new Date(System.currentTimeMillis()+3600000);
		String jwt = JWT.create().withClaim("emailUsuarioLogado", dados.getEmail()).withExpiresAt(horaExpirar).sign(Algorithm.HMAC256("teste secreto"));
		
		response.addHeader("token", jwt);
	}
}
