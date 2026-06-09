package org.avalverde.test.springboot.app.demo.repositories;

import org.avalverde.test.springboot.app.demo.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta,Long> {
//No necesitamos implementar ya que Jpa lo trae todo
//    List<Cuenta> findAll();
//    Cuenta findById(Long id);
//    void update(Cuenta cuenta);
    @Query("select c from Cuenta c where c.persona=?1")
    Optional<Cuenta> findByPersona(String persona); //Para buscar por el atributo persona
}
