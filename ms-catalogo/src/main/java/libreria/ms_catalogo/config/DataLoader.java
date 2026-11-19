package libreria.ms_catalogo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import libreria.ms_catalogo.model.Libro;
import libreria.ms_catalogo.repository.LibroRepository;

@Configuration
public class DataLoader {

    private static final List<Libro> INITIAL_CATALOG = List.of(
        libro("Cien años de soledad", "Gabriel García Márquez", "$18.990", "Latinoaméricanos", 10,
            "https://images.cdn1.buscalibre.com/fit-in/660x660/a2/8c/a28c74c0fdb8c85fe576fac52491e119.jpg",
            "La familia Buendía atraviesa varias generaciones en el pueblo ficticio de Macondo.", false),
        libro("1984", "George Orwell", "$14.500", "Clásicos", 4,
            "https://images.cdn1.buscalibre.com/fit-in/360x360/c9/ee/c9eef0bafc045010fe576fac52491e119.jpg",
            "Winston Smith vive bajo un régimen que vigila la vida privada y controla la información.", false),
        libro("El Principito", "Antoine de Saint-Exupéry", "$9.900", "Clásicos", 5,
            "https://cdn.prod.website-files.com/6034d7d1f3e0f52c50b2adee/681b63dd7d9dbb4c4ce5ae76_WJlUnXLgNrZqh3HN_u7WMEnTVs1tV0qKwtUkvXJ2JTk.jpeg",
            "Un piloto conoce a un pequeño viajero cuya mirada transforma su forma de entender el mundo.", false),
        libro("Don Quijote de la Mancha", "Miguel de Cervantes", "$22.000", "Clásicos", 3,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFEC5rSQYIhmOJvnMXdQDkfs2UsRO0xbNYmuDLkLju-GEU6TaP_u7hVp6E&s=10",
            "Un hidalgo manchego emprende aventuras inspirado por los libros de caballería.", false),
        libro("Fahrenheit 451", "Ray Bradbury", "$15.200", "Clásicos", 7,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTCZqcppciTLp2robBPBa7JMm5FMaDcbEzD28rEjelhTCqAQIHcdHFLUv7G&s=10",
            "En una sociedad que prohíbe los libros, un bombero comienza a cuestionar su trabajo.", false),
        libro("Orgullo y Prejuicio", "Jane Austen", "$12.500", "Clásicos", 8,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr3RZC9kruGsbRu6lpS0-IP_6Rsic9hmCZ5A2GBM-Bqw&s",
            "Elizabeth Bennet y Fitzwilliam Darcy afrontan malentendidos, expectativas y diferencias sociales.", false),
        libro("El Jugador", "Fiodor M. Dostoyevski", "$28.000", "Clásicos", 2,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDEvnbgt_atlMISzAJu7cwXXyrHeeK8MngzkAsCyM_Fg&s=10",
            "Un joven tutor queda atrapado entre el juego, las deudas y una relación amorosa.", false),
        libro("Cometierra", "Dolores Reyes", "$12.000", "Latinoaméricanos", 9,
            "https://images.cdn2.buscalibre.com/fit-in/660x660/9e/5a/9e5a9a0556f662362cdc8145194efecc.jpg",
            "Una joven descubre que al comer tierra puede percibir el destino de personas desaparecidas.", false),
        libro("Crimen y Castigo", "Fiodor M. Dostoyevski", "$39.900", "Clásicos", 4,
            "https://images.cdn1.buscalibre.com/fit-in/660x660/2b/34/2b342d36ef089ce9bdd7b81aa3329093.jpg",
            "Raskólnikov enfrenta las consecuencias morales y psicológicas de un crimen.", false),
        libro("El Jardinero y la Muerte", "Gueorgui Gopodínov", "$34.900", "Contemporáneos", 1,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPAR1yZUP5ZMgOZH24-GYWu2H_JodvTiOU_orwsQoRr85hEA6c1rr55pWj&s=10",
            "Un hijo recuerda a su padre jardinero y acompaña su enfermedad mediante la memoria.", false),
        libro("Las Gratitudes", "Delphine de Vigan", "$20.000", "Contemporáneos", 5,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS1jxN0sSVf03l4v1U4QsHnN23s_b31GBMKBb5eYQQlow&s=10",
            "Una mujer mayor pierde gradualmente el lenguaje mientras quienes la rodean buscan agradecerle.", false),
        libro("Pura Pasión", "Annie Ernaux", "$24.900", "Contemporáneos", 3,
            "https://proassets.planetadelibros.cl/usuaris/libros/fotos/368/original/portada_pura-pasion_annie-ernaux_202210171830.jpg",
            "Una narradora examina la intensidad de una relación amorosa y la espera que la acompaña.", false),
        libro("Carta de una Desconocida", "Stefan Zweig", "$19.900", "Clásicos", 2,
            "https://images.cdn3.buscalibre.com/fit-in/660x660/74/df/74dfb357e1e3b66a88cf9833458fa244.jpg",
            "Una carta revela la historia de un amor secreto que marcó la vida de quien la escribe.", false),
        libro("Ya Nadie Escribe Cartas", "Jang Eun-jin", "$19.900", "Asiáticos", 4,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR4fM9DvDE8Dem40owtU6rcX8XXo9NN4Hy1LISkJvkoVw&s",
            "Un viaje y unas cartas no enviadas conectan a una mujer con la soledad y los demás.", false),
        libro("Nada Dentro", "Asako Otani", "$14.900", "Asiáticos", 4,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQoJqIhdMwS-rviypO6txL1MdejtWOsksc7Xa1BxTdLw&s=10",
            "Dos mujeres exploran una vida alejada de las expectativas sociales en Japón.", false),
        libro("Blanco", "Han Kang", "$21.000", "Asiáticos", 3,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSl4AikPdWNpNLxnVdFjYfzR8pSoLvM8v68ZyEeA7AfTA&s=10",
            "Una serie de imágenes blancas enlaza duelo, memoria y fragilidad.", false),
        libro("La Vegetariana", "Han Kang", "$21.900", "Asiáticos", 3,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSGRrMFo9rRVxlaDz-18gf5CvqDvP-TFo2blX9-19JZyg&s=10",
            "La decisión de Yeong-hye de dejar de comer carne altera las relaciones de su familia.", true),
        libro("Del amor y otros demonios", "Gabriel Garcia Marquez", "$12.000", "Latinoaméricanos", 7,
            "https://images.cdn1.buscalibre.com/fit-in/660x660/d5/7f/d57fa2f6bf1ea3a0216f7b58b40711ca.jpg",
            "En la Cartagena colonial, una joven y un sacerdote viven una historia marcada por prejuicios.", true),
        libro("Los peligros de fumar en la cama", "Mariana Enriquez", "$24.900", "Latinoaméricanos", 6,
            "https://cms.anagrama-ed.es/uploads/media/portadas/0001/27/0626dbb1eef4e0172728f155d02b860c2fcf396a.jpeg",
            "Doce relatos de terror cotidiano exploran miedos y violencia en la vida urbana.", false),
        libro("Kentukis", "Samantha Scheweblin", "$20.000", "Contemporáneos", 3,
            "https://contrapunto.cl/cdn/shop/files/9789566248798.jpg?v=1771288538",
            "Dispositivos móviles conectan a desconocidos y ponen a prueba la intimidad y la vigilancia.", true),
        libro("Actos Humanos", "Han Kang", "$20.000", "Asiáticos", 6,
            "https://www.libreriadelgam.cl/imagenes/9789566/978956624855.JPG",
            "Varias voces reconstruyen la represión de Gwangju y sus efectos en sobrevivientes y familias.", false),
        libro("La Única Historia", "Julian Barnes", "$14.000", "Contemporáneos", 7,
            "https://images.cdn1.buscalibre.com/fit-in/360x360/bf/82/bf8235d8cb6116a8ed86faa69c37dbd7.jpg",
            "Un hombre recuerda su primer amor y cómo la memoria transforma esa experiencia.", true)
    );

    @Bean
    CommandLineRunner initDatabase(
            LibroRepository repository,
            @Value("${CATALOG_IMPORT_DATASET:false}") boolean importDataset) {
        return args -> {
            if (repository.count() > 0 && !importDataset) {
                return;
            }

            for (Libro bookData : INITIAL_CATALOG) {
                Libro book = repository.findFirstByTituloIgnoreCase(bookData.getTitulo())
                    .orElseGet(Libro::new);
                book.setTitulo(bookData.getTitulo());
                book.setAutor(bookData.getAutor());
                book.setPrecio(bookData.getPrecio());
                book.setDescripcion(bookData.getDescripcion());
                book.setCategoria(bookData.getCategoria());
                book.setStock(bookData.getStock());
                book.setImagen(bookData.getImagen());
                book.setNovedad(bookData.isNovedad());
                repository.save(book);
            }
        };
    }

    private static Libro libro(
            String titulo,
            String autor,
            String precio,
            String categoria,
            Integer stock,
            String imagen,
            String descripcion,
            boolean novedad) {
        Libro libro = new Libro(titulo, autor, precio, categoria, stock, imagen);
        libro.setDescripcion(descripcion);
        libro.setNovedad(novedad);
        return libro;
    }
}