package com.example.demo;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/biblioteca")
public class ExemploController {

    private List<Livro> livros;

    public ExemploController() {
        livros = new ArrayList<>();

        livros.add(new Livro(110, "Aprendendo Java", "Maria da Silva", 2015));
        livros.add(new Livro(120, "Spring-Boot", "Jose de Souza", 2020));
        livros.add(new Livro(130, "Principios SOLID", "Pedro da Silva", 2023));
        livros.add(new Livro(140, "Padroes de Projeto", "Joana Moura", 2023));
        livros.add(new Livro(150, "Teste Unitario", "Pedro da Silva", 2024)); 
    }

    @GetMapping("/")
    public String getMensagemInicial() {
        return "Aplicacao Spring-Boot funcionando!";
    }

    @GetMapping("/livros")
    public List<Livro> getLivros() {
        return livros;
    }
    
    @GetMapping("/titulos")
    public List<String> getTitulos() {
        return livros.stream()
               .map(livro->livro.getTitulo())
               .toList();
    }

    @GetMapping("/autores")
    public List<String> getListaAutores() {
        return livros.stream()
                .map(l -> l.getAutor())
                .distinct()
                .toList();
    }
    
    // 1. Endpoint com query string
    @GetMapping("/livrosautor")
    public List<Livro> getLivrosPorAutor(@RequestParam("autor") String autor) {
        return livros.stream()
                .filter(livro -> livro.getAutor().equals(autor))
                .toList();
    }
    
    // 3. Endpoint com path parameters
    @GetMapping("/livrosautorano/{autor}/ano/{ano}")
    public List<Livro> getLivrosPorAutorEAno(@PathVariable String autor, @PathVariable int ano) {
        return livros.stream()
                .filter(livro -> livro.getAutor().equals(autor) && livro.getAno() == ano)
                .toList();
    }
    
    // 5. Endpoint para receber body data
    @PostMapping("/novolivro")
    public Livro adicionarLivro(@RequestBody Livro novoLivro) {
        livros.add(novoLivro);
        return novoLivro;
    }
    
    // 8. Endpoint com ResponseEntity
    @GetMapping("/livrotitulo/{titulo}")
    public ResponseEntity<Object> getLivroPorTitulo(@PathVariable String titulo) {
        List<Livro> livrosEncontrados = livros.stream()
                .filter(livro -> livro.getTitulo().contains(titulo))
                .toList();
        
        if (livrosEncontrados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhum livro encontrado com o título contendo: " + titulo);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(livrosEncontrados);
    }
}
