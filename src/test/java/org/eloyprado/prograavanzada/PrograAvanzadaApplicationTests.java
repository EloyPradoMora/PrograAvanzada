package org.eloyprado.prograavanzada;

import org.eloyprado.prograavanzada.Repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@SpringBootTest
class PrograAvanzadaApplicationTest {

    @Test
    void contextLoads() {}

    @Test
    void initialData_insertsProducts_whenRepositoryIsEmpty() throws Exception {
        ProductoRepository mockRepo = Mockito.mock(ProductoRepository.class);
        when(mockRepo.count()).thenReturn(0L);

        PrograAvanzadaApplication app = new PrograAvanzadaApplication();
        CommandLineRunner runner = app.initialData(mockRepo);

        runner.run();

        verify(mockRepo, times(1)).saveAll(any());
    }

    @Test
    void initialData_doesNotInsertProducts_whenRepositoryNotEmpty() throws Exception {
        ProductoRepository mockRepo = Mockito.mock(ProductoRepository.class);
        when(mockRepo.count()).thenReturn(5L);

        PrograAvanzadaApplication app = new PrograAvanzadaApplication();
        CommandLineRunner runner = app.initialData(mockRepo);

        runner.run();

        verify(mockRepo, never()).saveAll(any());
    }
}