package navita.exercicio02.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

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

import com.auth0.jwt.exceptions.JWTVerificationException;

import navita.exercicio02.erros.CampoVazio;
import navita.exercicio02.erros.DadoIgual;
import navita.exercicio02.erros.NotFound;
import navita.exercicio02.erros.SemAuth;
import navita.exercicio02.models.MarcaModel;
import navita.exercicio02.models.PatrimonioModel;
import navita.exercicio02.repositorio.MarcaRepositorio;
import navita.exercicio02.repositorio.PatrimonioRepositorio;

@RestController
@RequestMapping(value="usuario/login/patrimonio")
public class PatrimonioController {
	
	@Autowired
	MarcaRepositorio marcaRepositorio;
	
	@Autowired
	PatrimonioRepositorio patrimonioRepositorio;
	
	void autenticarToken(String token) {
		if(token == "") {
			throw new SemAuth("Sem autenticação.");
		}
	}
	
	@PostMapping("/cadastro")
	public PatrimonioModel cadastrarPatrimonio(@RequestHeader (value = "token") String token, @RequestBody PatrimonioModel patrimonio) throws Exception, JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException {
		autenticarToken(token);
		if(patrimonio.getNome() == null || patrimonio.getMarcaId() == null || patrimonio.getMarcaId().getNome() == null || patrimonio.getDescricao() == null) {
			throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
		}
		if(patrimonio.getNome().isEmpty() || patrimonio.getMarcaId().getNome().isEmpty()) {
			throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
		}
		marcaAlreadyExists(patrimonio.getMarcaId().getNome());
		List<PatrimonioModel> listaPatrimonio = patrimonioRepositorio.findAll();
		int  indexLastPatrimonio;
		if(listaPatrimonio.size() == 0) {
			indexLastPatrimonio = 1;
			patrimonio.setNumeroTombo(indexLastPatrimonio);
			
		}else {
			indexLastPatrimonio = listaPatrimonio.size()-1;
			patrimonio.setNumeroTombo(indexLastPatrimonio);
			PatrimonioModel novoNumeroTombo = listaPatrimonio.get(indexLastPatrimonio);
			patrimonio.setNumeroTombo(novoNumeroTombo.getNumeroTombo()+1);
		}
		return patrimonioRepositorio.save(patrimonio);
	}
	private void marcaAlreadyExists(String nome) {
		Optional<MarcaModel> existsMarca =  marcaRepositorio.findByNome(nome);
		if(existsMarca.equals(Optional.empty()) == false) {
			throw new DadoIgual("O nome da marca: " + nome + " ja existe!  ");
		}
	}
	
	private void verificarMarca(String nome) {
		Optional<MarcaModel> existsMarca = marcaRepositorio.findByNome(nome);
		if(existsMarca.equals(Optional.empty())) {
			throw new NotFound("Patrimonio nao encontrado com o nome dessa marca: "+ nome);
		}
	}
	private void verificarNumeroTombo(Long tombo) {
		Optional<PatrimonioModel> existsTombo = patrimonioRepositorio.findByNumeroTombo(tombo);
		if(existsTombo.equals(Optional.empty())) {
			throw new NotFound("Patrimonio nao encontrado com esse numero do tombo: "+ tombo);
		}
	}
	
	@GetMapping("/listarpatrimonios")
	public List<PatrimonioModel> listarPatrimonios(@RequestHeader (value = "token") String token)throws JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException{
		autenticarToken(token);
		return patrimonioRepositorio.findAll();
	}
	
	@GetMapping("/numerotombo/{numero}")
	public ResponseEntity<PatrimonioModel> findPatrimonioByNumeroTombo(@RequestHeader (value = "token") String token, @PathVariable (value = "numero") Long tombo) throws Exception, JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException {
		verificarNumeroTombo(tombo);
		PatrimonioModel patrimonio = patrimonioRepositorio.findByNumeroTombo(tombo).orElseThrow((()->new Exception("Algo deu errado")));
		return ResponseEntity.ok().body(patrimonio);
	}
	
	@GetMapping("/nomemarca/{nomeMarca}")
	public ResponseEntity<PatrimonioModel> findPatrimonioByNameMarca(@RequestHeader (value = "token") String token, @PathVariable (value = "nomeMarca") String nomeMarca) throws Exception, JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException {
		autenticarToken(token);
		verificarMarca(nomeMarca);
		MarcaModel marca = marcaRepositorio.findByNome(nomeMarca).orElseThrow((()->new Exception("Algo deu errado")));
		PatrimonioModel patrimonio = patrimonioRepositorio.findByMarcaId(marca).orElseThrow((()->new Exception("Algo deu errado")));
		return ResponseEntity.ok().body(patrimonio);
	}
	
	@PutMapping("/numerotombo/{numero}")
	public ResponseEntity<PatrimonioModel> atualizarPatrimonioByNumeroTombo(@RequestHeader (value = "token") String token, @PathVariable (value = "numero") Long tombo, @RequestBody PatrimonioModel dadosPatrimonio) throws Exception, JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException{
		autenticarToken(token);
		verificarNumeroTombo(tombo);
		PatrimonioModel patrimonio = patrimonioRepositorio.findByNumeroTombo(tombo).orElseThrow((()->new Exception("Algo deu errado")));
		
		if(dadosPatrimonio.getNome() != null) {
			if(dadosPatrimonio.getNome().contentEquals(patrimonio.getNome())) {
				throw new DadoIgual("O Dado nao pode ser igual ao antigo!");
			}
			if(dadosPatrimonio.getNome().isEmpty()) {	
				throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
			}
			patrimonio.setNome(dadosPatrimonio.getNome());
		}
		if(dadosPatrimonio.getMarcaId() != null) {
			if(dadosPatrimonio.getMarcaId().getNome().contentEquals(patrimonio.getMarcaId().getNome())) {
				
				throw new DadoIgual("O Dado nao pode ser igual ao antigo!");
			}
			if(dadosPatrimonio.getMarcaId().getNome().isEmpty()) {	
				throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
			}
			marcaAlreadyExists(dadosPatrimonio.getMarcaId().getNome());
			patrimonio.getMarcaId().setNome(dadosPatrimonio.getMarcaId().getNome());
		}
		if(dadosPatrimonio.getDescricao() != null) {
			if(dadosPatrimonio.getDescricao().contentEquals(patrimonio.getDescricao())) {
				throw new DadoIgual("O Dado nao pode ser igual ao antigo!");
			}
			if(dadosPatrimonio.getDescricao().isEmpty()) {	
				throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
			}
			patrimonio.setDescricao(dadosPatrimonio.getDescricao());
		}
		patrimonioRepositorio.save(patrimonio);
		return ResponseEntity.ok().body(patrimonio);
	}
	
	@PutMapping("/nomemarca/{nomeMarca}")
	public ResponseEntity<PatrimonioModel> atualizarPatrimonioByNomeMarca(@RequestHeader (value = "token") String token, @PathVariable (value = "nomeMarca") String nomeMarca, @RequestBody PatrimonioModel dadosPatrimonio) throws Exception, JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException{
		autenticarToken(token);
		verificarMarca(nomeMarca);
		MarcaModel marca = marcaRepositorio.findByNome(nomeMarca).orElseThrow((()->new Exception("Algo deu errado")));
		PatrimonioModel patrimonio = patrimonioRepositorio.findByMarcaId(marca).orElseThrow((()->new Exception("Algo deu errado")));
		
		if(dadosPatrimonio.getNome() != null) {
			if(dadosPatrimonio.getNome().contentEquals(patrimonio.getNome())) {
				throw new DadoIgual("O Dado nao pode ser igual ao antigo!");
			}
			if(dadosPatrimonio.getNome().isEmpty()) {	
				throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
			}
			patrimonio.setNome(dadosPatrimonio.getNome());
		}
		if(dadosPatrimonio.getMarcaId() != null) {
			if(dadosPatrimonio.getMarcaId().getNome().contentEquals(patrimonio.getMarcaId().getNome())) {
				
				throw new DadoIgual("O Dado nao pode ser igual ao antigo!");
			}
			if(dadosPatrimonio.getMarcaId().getNome().isEmpty()) {	
				throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
			}
			marcaAlreadyExists(dadosPatrimonio.getMarcaId().getNome());
			patrimonio.getMarcaId().setNome(dadosPatrimonio.getMarcaId().getNome());
		}
		if(dadosPatrimonio.getDescricao() != null) {
			if(dadosPatrimonio.getDescricao().contentEquals(patrimonio.getDescricao())) {
				throw new DadoIgual("O Dado nao pode ser igual ao antigo!");
			}
			if(dadosPatrimonio.getDescricao().isEmpty()) {	
				throw new CampoVazio("O(s) campo(s) nao pode(m) estar vazio(s)!");
			}
			patrimonio.setDescricao(dadosPatrimonio.getDescricao());
		}
		patrimonioRepositorio.save(patrimonio);
		return ResponseEntity.ok().body(patrimonio);
	}
	
	@DeleteMapping("/numerotombo/{numero}")
	public ResponseEntity<?> deletarPatrimonioByNumeroTombo(@RequestHeader (value = "token") String token, @PathVariable (value = "numero") Long tombo) throws Exception, JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException{
		autenticarToken(token);
		verificarNumeroTombo(tombo);
		PatrimonioModel patrimonio = patrimonioRepositorio.findByNumeroTombo(tombo).orElseThrow((()->new Exception("Algo deu errado")));
		patrimonioRepositorio.delete(patrimonio);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/nomemarca/{nomeMarca}")
	public ResponseEntity<?> deletarPatrimonioByNomeMarca(@RequestHeader (value = "token") String token, @PathVariable (value = "nomeMarca") String nome) throws Exception, JWTVerificationException, IllegalArgumentException, UnsupportedEncodingException{
		autenticarToken(token);
		verificarMarca(nome);
		MarcaModel marca = marcaRepositorio.findByNome(nome).orElseThrow((()->new Exception("Algo deu errado")));
		PatrimonioModel patrimonio = patrimonioRepositorio.findByMarcaId(marca).orElseThrow((()->new Exception("Algo deu errado")));
		patrimonioRepositorio.delete(patrimonio);
		return ResponseEntity.ok().build();
	}
}