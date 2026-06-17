package org.avalverde.test.springboot.app.demo.controllers;

import org.avalverde.test.springboot.app.demo.models.Cuenta;
import org.avalverde.test.springboot.app.demo.models.TransaccionDto;
import org.avalverde.test.springboot.app.demo.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

//Indicamos con spring que va a ser un controlador
@RestController //Incluya controller y responsebody que contiene automaticamente en json a lka repuesta
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cuenta> listar(){
        return cuentaService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cuenta guardar(@RequestBody Cuenta cuenta){
        return cuentaService.save(cuenta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable(name = "id") Long id){
        Cuenta cuenta = null;
        try {
            cuenta = cuentaService.findById(id);
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cuenta);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransaccionDto dto){
        cuentaService.transferir(dto.getCuentaOrigenId(),
                                 dto.getCuentaDestinoId(),
                                 dto.getMonto(),
                                 dto.getBancoId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","OK");
        response.put("mensaje","Transferencia realizada con exito");
        response.put("transaccion",dto);

        //respondemos y enviamnos un json de respuesta
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id){
        cuentaService.deleteById(id);
    }
}
