package navita.exercicio02.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import navita.exercicio02.models.MarcaModel;
import navita.exercicio02.models.PatrimonioModel;

public interface PatrimonioRepositorio extends JpaRepository<PatrimonioModel, Long>{

	Optional<PatrimonioModel> findByNumeroTombo(Long tombo);
	Optional<PatrimonioModel> findByMarcaId(MarcaModel idMarca);
}
