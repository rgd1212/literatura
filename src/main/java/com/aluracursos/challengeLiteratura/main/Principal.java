package com.aluracursos.challengeLiteratura.main;

import com.aluracursos.challengeLiteratura.model.*;
import com.aluracursos.challengeLiteratura.repositorio.AutorRepository;
import com.aluracursos.challengeLiteratura.repositorio.LibroRepository;
import com.aluracursos.challengeLiteratura.service.AutorService;
import com.aluracursos.challengeLiteratura.service.ConsumoApi;
import com.aluracursos.challengeLiteratura.service.ConvierteDatos;
import com.aluracursos.challengeLiteratura.service.LibroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    public LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private Scanner consola = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private DatosLibro datosLibro;
    private DatosAutor datosAutor;
    private LibroService libroServices = new LibroService();
    private AutorService autorServices = new AutorService();
    private Autor autorDelLibro;

    private List<Autor> autores;
    private List<Libro> libros;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void muestraElMenu() throws Exception {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    \n M E N U  P R I N C I P A L - C H A L L E N G E  L I B R A R Y 
                    1 - Buscar libro por titulo
                    2 - Lista de libros registrados
                    3 - Lista de autores registrados
                    4 - Lista de autores vivos en determinado anio
                    5 - Lista un libro por el idioma
                    6 - Top 5 libros más descargados 
                    7 - Listado de libros por autor               
                    0 - Salir
                    \n 
                    Elija el numero en base a la opción deseada: 
                    """;
            System.out.println(menu);
            opcion = consola.nextInt();
            consola.nextLine();

            switch (opcion) {
                case 1:
                    obtenerLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;

                case 3:
                    listarAutoresRegistrados();
                    break;

                case 4:
                    listarAutoresVivosPorAnio();
                    break;

                case 5:
                    listarLibrosPorIdioma();
                    break;

                case 6:
                    top10LibrosDescargados();
                    break;

                case 7:
                    listadoDeLibrosPorAutor();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void obtenerLibroPorTitulo() throws Exception {
        busquedaDeLibros();
    }

    private void listarLibrosRegistrados() {
        System.out.println("\nListado de libros registrados: ");
        libros = libroRepository.findAll();
        libros.forEach(System.out::println);
        sugerirPausa();
    }

    private void listarAutoresRegistrados() {
        System.out.println("\nListado de autores registrados: ");
        autores = autorRepository.findAll();
        autores.forEach(System.out::println);
        sugerirPausa();
    }

    private void listarAutoresVivosPorAnio() {
        System.out.println("\nEscriba el año que desea buscar: ");
        var ano = consola.nextInt();
        autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores vivos en ese año..");
        } else {
            System.out.println(ano);
            System.out.println("\nListado de autores vivos en el año " + ano + ": ");
            autores = autorRepository.findAutorByFecha(ano);
            autores.forEach(System.out::println);
        }
        sugerirPausa();
    }

    private void listarLibrosPorIdioma() {
        System.out.println("\nListado de idioma registrados: ");
        var listaDeIdiomas = libroRepository.findDistinctIdiomas();
        for (int i = 0; i < listaDeIdiomas.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + Idioma.from(listaDeIdiomas.get(i)));
        }
        System.out.println("\nSeleccione el numero del idioma que desea buscar: ");
        var eleccion = consola.nextInt();
        if (eleccion < 1 || eleccion > listaDeIdiomas.size()) {
            System.out.println("Opción Inválida..");
            return;
        }
        String idiomaSeleccionado = Idioma.from(listaDeIdiomas.get(eleccion - 1)).toString();
        System.out.println("\nListado de libros en - [ " + idiomaSeleccionado.toUpperCase() + " ]: ");
        libros = libroRepository.findByIdioma(listaDeIdiomas.get(eleccion - 1));
        libros.forEach(System.out::println);
        System.out.println();
        sugerirPausa();
    }

    private void top10LibrosDescargados() {
        System.out.println("\n Top 10 libros más descargados:\n");
        libros = libroRepository.findTop10Descargados();
        libros.forEach(System.out::println);
        sugerirPausa();
    }

    private void listadoDeLibrosPorAutor() {
        autores = autorRepository.findAll();
        System.out.println("\nListado de autores registrados: ");
        for (int i = 0; i < autores.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + autores.get(i).getNombre());
        }
        System.out.println("\nEscriba el número del autor que desea buscar: ");
        var eleccion = consola.nextInt();
        if (eleccion < 1 || eleccion > autores.size()) {
            System.out.println("Opción Inválida");
            return;
        }
        System.out.println("\nListado de libros de : " + autores.get(eleccion - 1).getNombre());
        libros = libroRepository.findLibrosByAutor(autores.get(eleccion - 1));
        libros.forEach(System.out::println);
        sugerirPausa();
    }

    public void sugerirPausa() {
        System.out.println();
        Terminal terminal = null;
        try {
            terminal = TerminalBuilder.builder().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

        System.out.println("Presione cualquier tecla para continuar...");

        // Esperar a que el usuario presione cualquier tecla
        lineReader.readLine();
        // Cerrar la terminal
    }

    private void busquedaDeLibros() throws Exception {

        System.out.println("Escriba el título del libro que desea buscar: ");
        var tituloLibro = consola.nextLine();
        var resultadoBusqueda = new ConsumoApi().obtenerDatosLibro(tituloLibro);

        JSONObject jsonObject = new JSONObject(resultadoBusqueda);
        JSONArray resultsArray = jsonObject.getJSONArray("results");

        if (resultsArray.isEmpty()) {
            System.out.println("Libro no encontrado con el título: " + tituloLibro);
            return;
        }

        System.out.println("Se encontraron " + resultsArray.length() + " libros: \n");
        for (int i = 0; i < resultsArray.length(); i++) {
            System.out.println("[" + (i + 1) + "] " + resultsArray.getJSONObject(i).getString("title"));
        }

        System.out.println("\nSeleccone el libro deseado indicando su número, o presione 0 para cancelar: ");
        var numeroLibro = consola.nextInt();
        if (numeroLibro == 0) {
            return;
        }
        numeroLibro = numeroLibro - 1;

        jsonObject = new JSONObject(resultsArray.getJSONObject(numeroLibro).toString());
        datosLibro = conversor.obtenerDatos(jsonObject.toString(), DatosLibro.class);

        //Verificar si el libro ya esta registrado
        Optional<Libro> libro = libroRepository.findById(datosLibro.idLibro());
        if (libro.isPresent()) {
            System.out.println("Libro ya esta  registrado");
            System.out.println();
            System.out.println(libro.get());
            sugerirPausa();
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = mapper.readValue(jsonObject.toString(), Map.class);
        String authorsJson = mapper.writeValueAsString((List<Map<String, Object>>) jsonMap.get("authors"));
        String resultado = authorsJson.substring(1, authorsJson.length() - 1);
        datosAutor = conversor.obtenerDatos(resultado, DatosAutor.class);

        String idioma = new ConsumoApi().getIdioma(jsonMap);

        autores = autorRepository.findAll();
        Optional<Autor> autor = autores.stream()
                .filter(a -> a.getNombre().equals(datosAutor.nombre()))
                .findFirst();
        if (autor.isPresent()) {
            autorDelLibro = autor.get();
        } else {
            autorDelLibro = new Autor(datosAutor);
            autorRepository.save(autorDelLibro);
        }
        Libro libroNuevo = new Libro(datosLibro, autorDelLibro, idioma);
        libroRepository.save(libroNuevo);
        System.out.println("Libro registrado exitosamente");
        System.out.println();
        System.out.println(libroNuevo);
        sugerirPausa();
    }
}
