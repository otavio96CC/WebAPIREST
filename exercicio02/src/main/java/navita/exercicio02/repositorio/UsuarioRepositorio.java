package navita.exercicio02.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import navita.exercicio02.models.UsuarioModel;

public interface UsuarioRepositorio extends JpaRepository<UsuarioModel, Long> {

	Optional<UsuarioModel> findByEmail(String email);
}
