package navita.exercicio02.repositorio;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import navita.exercicio02.models.MarcaModel;

public interface MarcaRepositorio extends JpaRepository<MarcaModel, Long>{

	Optional<MarcaModel> findByNome(String nome);
}
