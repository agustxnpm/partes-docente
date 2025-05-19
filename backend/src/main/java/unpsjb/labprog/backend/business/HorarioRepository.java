package unpsjb.labprog.backend.business;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import unpsjb.labprog.backend.model.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    Optional<Horario> findByDiaAndHora(String dia, int hora);

}
