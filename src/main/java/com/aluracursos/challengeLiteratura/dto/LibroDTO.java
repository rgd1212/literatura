package com.aluracursos.challengeLiteratura.dto;

import com.aluracursos.challengeLiteratura.model.Autor;

public record LibroDTO(int idLibro,
                       String titulo,
                       Autor autor,
                       String idioma,
                       Double numDescargas) {
}
