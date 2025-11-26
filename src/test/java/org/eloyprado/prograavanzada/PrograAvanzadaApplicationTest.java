package org.eloyprado.prograavanzada;

import org.eloyprado.prograavanzada.Repository.ProductoRepository;
import org.eloyprado.prograavanzada.config.DataInitializer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import usuario.Producto;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class PrograAvanzadaApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void initialData_insertsProducts_whenRepositoryIsEmpty() throws Exception {
        ProductoRepository mockRepo = Mockito.mock(ProductoRepository.class);
        when(mockRepo.count()).thenReturn(0L);

        DataInitializer initializer = new DataInitializer(mockRepo);
        initializer.run();

        verify(mockRepo, times(1)).saveAll(any());
    }

    @Test
    void initialData_doesNotInsertProducts_whenRepositoryNotEmpty() throws Exception {
        ProductoRepository mockRepo = Mockito.mock(ProductoRepository.class);
        when(mockRepo.count()).thenReturn(5L);

        DataInitializer initializer = new DataInitializer(mockRepo);
        initializer.run();

        verify(mockRepo, never()).saveAll(any());
    }

    @Test
    void initialData_InsertsCorrectProducts_WhenEmpty() throws Exception {
        ProductoRepository repo = mock(ProductoRepository.class);

        when(repo.count()).thenReturn(0L);

        DataInitializer initializer = new DataInitializer(repo);
        initializer.run();

        verify(repo, times(1)).saveAll(argThat(iterable -> {
            List<Producto> list = (List<Producto>) iterable;

            return list.size() == 3 &&
                    list.get(0).getNombre().equals("Acer Nitro 5") &&
                    list.get(1).getPrecio() == 1000 &&
                    list.get(2).getId().equals("3");
        }));
    }
}