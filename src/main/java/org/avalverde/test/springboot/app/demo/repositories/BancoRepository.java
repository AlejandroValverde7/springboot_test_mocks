package org.avalverde.test.springboot.app.demo.repositories;

import org.avalverde.test.springboot.app.demo.models.Banco;

import java.util.List;

public interface BancoRepository {
        List<Banco> findAll();
        Banco findById(Long id);
        void update(Banco banco);
}
