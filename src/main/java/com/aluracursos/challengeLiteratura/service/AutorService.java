package com.aluracursos.challengeLiteratura.service;

import com.aluracursos.challengeLiteratura.model.Autor;
import com.aluracursos.challengeLiteratura.repositorio.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> getAutoresVivosPorAnio(int year){
        return  autorRepository.findAutorByFecha(year);
    }
}
