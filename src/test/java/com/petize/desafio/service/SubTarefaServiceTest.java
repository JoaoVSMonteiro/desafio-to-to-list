package com.petize.desafio.service;



import com.petize.desafio.exception.RecursoNaoEncontradoException;
import com.petize.desafio.exception.RegraDeNegocioException;
import com.petize.desafio.model.dto.subtarefa.SubTarefaCreateDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaStatusDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaUpdateDto;
import com.petize.desafio.model.entity.Subtarefa;
import com.petize.desafio.model.entity.Tarefa;
import com.petize.desafio.model.mapper.SubTarefaMapper;
import com.petize.desafio.repository.SubTarefaRepository;
import com.petize.desafio.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.petize.desafio.service.TestConstantes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class SubTarefaServiceTest {
    private SubTarefaRepository subRepo;
    private TarefaRepository tarefaRepo;
    private SubTarefaMapper mapper;
    private SubTarefaService service;

    @BeforeEach
    void setUp() {
        subRepo = mock(SubTarefaRepository.class);
        tarefaRepo = mock(TarefaRepository.class);
        mapper = mock(SubTarefaMapper.class);
        service = new SubTarefaService(subRepo, mapper, tarefaRepo);
    }

    private Tarefa tarefaComStatus(Long id, String titulo, Enum<?> status) {
        Tarefa t = new Tarefa();
        t.setIdTarefa(id);
        t.setTituloTarefa(titulo);
        t.setStatus((com.petize.desafio.model.enums.Status) status);
        return t;
    }

    private Subtarefa subCom(Long id, String titulo, Enum<?> status, Tarefa tarefa) {
        Subtarefa s = new Subtarefa();
        s.setIdSubTarefa(id);
        s.setTituloSubTarefa(titulo);
        s.setStatus((com.petize.desafio.model.enums.Status) status);
        s.setTarefa(tarefa);
        return s;
    }

    @Test
    @DisplayName("deve criar subtarefa com sucesso e retornar DTO")
    void deveCriarSubtarefaComSucesso() {
        Tarefa tarefa = tarefaComStatus(ID_TAREFA_1, TITULO_TAREFA_1, ST_PENDENTE);
        when(tarefaRepo.findById(ID_TAREFA_1)).thenReturn(Optional.of(tarefa));

        Subtarefa entidade = subCom(ID_SUBTAREFA_1, TITULO_SUB_1, ST_PENDENTE, tarefa);
        when(mapper.toEntity(isA(SubTarefaCreateDto.class))).thenReturn(entidade);
        when(subRepo.save(entidade)).thenReturn(entidade);

        SubTarefaDto esperado = SubTarefaDto.builder()
                .idSubTarefa(ID_SUBTAREFA_1)
                .tituloSubTarefa(TITULO_SUB_1)
                .status(ST_PENDENTE)
                .build();
        when(mapper.toDto(entidade)).thenReturn(esperado);

        SubTarefaDto dto = service.criarSubTarefa(ID_TAREFA_1, new SubTarefaCreateDto(TITULO_SUB_1, ST_PENDENTE));

        assertSame(esperado, dto);
        verify(tarefaRepo).findById(ID_TAREFA_1);
        verify(mapper).toEntity(isA(SubTarefaCreateDto.class));
        verify(subRepo).save(entidade);
        verify(mapper).toDto(entidade);
    }

    @Test
    @DisplayName("não deve criar subtarefa quando tarefa não existir")
    void naoDeveCriarQuandoTarefaNaoExiste() {
        when(tarefaRepo.findById(ID_TAREFA_INEXISTENTE)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.criarSubTarefa(ID_TAREFA_INEXISTENTE, new SubTarefaCreateDto(TITULO_SUB_1, ST_PENDENTE)));
        verify(tarefaRepo).findById(ID_TAREFA_INEXISTENTE);
        verifyNoInteractions(mapper, subRepo);
    }

    @Test
    @DisplayName("não deve criar subtarefa quando tarefa estiver CANCELADA")
    void naoDeveCriarQuandoTarefaCancelada() {
        when(tarefaRepo.findById(ID_TAREFA_1)).thenReturn(Optional.of(tarefaComStatus(ID_TAREFA_1, TITULO_TAREFA_1, ST_CANCELADA)));
        assertThrows(RegraDeNegocioException.class,
                () -> service.criarSubTarefa(ID_TAREFA_1, new SubTarefaCreateDto(TITULO_SUB_1, ST_PENDENTE)));
        verify(tarefaRepo).findById(ID_TAREFA_1);
        verifyNoInteractions(mapper, subRepo);
    }

    @Test
    @DisplayName("não deve criar subtarefa quando tarefa estiver CONCLUIDA")
    void naoDeveCriarQuandoTarefaConcluida() {
        when(tarefaRepo.findById(ID_TAREFA_1)).thenReturn(Optional.of(tarefaComStatus(ID_TAREFA_1, TITULO_TAREFA_1, ST_CONCLUIDA)));
        assertThrows(RegraDeNegocioException.class,
                () -> service.criarSubTarefa(ID_TAREFA_1, new SubTarefaCreateDto(TITULO_SUB_1, ST_PENDENTE)));
        verify(tarefaRepo).findById(ID_TAREFA_1);
        verifyNoInteractions(mapper, subRepo);
    }

    @Test
    @DisplayName("deve buscar subtarefa por id com sucesso")
    void deveBuscarPorId() {
        Subtarefa entidade = subCom(ID_SUBTAREFA_1, TITULO_SUB_1, ST_PENDENTE, tarefaComStatus(ID_TAREFA_1, TITULO_TAREFA_1, ST_PENDENTE));
        when(subRepo.findById(ID_SUBTAREFA_1)).thenReturn(Optional.of(entidade));

        SubTarefaDto esperado = SubTarefaDto.builder()
                .idSubTarefa(ID_SUBTAREFA_1)
                .tituloSubTarefa(TITULO_SUB_1)
                .status(ST_PENDENTE)
                .build();
        when(mapper.toDto(entidade)).thenReturn(esperado);

        SubTarefaDto dto = service.buscarSubTarefaPorId(ID_SUBTAREFA_1);

        assertSame(esperado, dto);
        verify(subRepo).findById(ID_SUBTAREFA_1);
        verify(mapper).toDto(entidade);
    }

    @Test
    @DisplayName("deve lançar 404 quando subtarefa não encontrada ao buscar por id")
    void deveLancar404AoBuscarPorId() {
        when(subRepo.findById(ID_SUBTAREFA_INEXISTENTE)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarSubTarefaPorId(ID_SUBTAREFA_INEXISTENTE));
        verify(subRepo).findById(ID_SUBTAREFA_INEXISTENTE);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("deve listar subtarefas por tarefa (lista vazia)")
    void deveListarPorTarefaVazia() {
        when(tarefaRepo.existsById(ID_TAREFA_1)).thenReturn(true);
        when(subRepo.findByTarefa_IdTarefa(ID_TAREFA_1)).thenReturn(new ArrayList<>());

        List<SubTarefaDto> lista = service.listarPorTarefa(ID_TAREFA_1);

        assertTrue(lista.isEmpty());
        verify(tarefaRepo).existsById(ID_TAREFA_1);
        verify(subRepo).findByTarefa_IdTarefa(ID_TAREFA_1);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("deve lançar erro ao listar por tarefa inexistente")
    void deveLancarErrorAoListarPorTarefaInexistente() {
        when(tarefaRepo.existsById(ID_TAREFA_INEXISTENTE)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> service.listarPorTarefa(ID_TAREFA_INEXISTENTE));
        verify(tarefaRepo).existsById(ID_TAREFA_INEXISTENTE);
        verifyNoInteractions(subRepo, mapper);
    }

    @Test
    @DisplayName("deve atualizar título da subtarefa com sucesso")
    void deveAtualizarTitulo() {
        Subtarefa entidade = subCom(ID_SUBTAREFA_1, TITULO_SUB_1, ST_PENDENTE, tarefaComStatus(ID_TAREFA_1, TITULO_TAREFA_1, ST_PENDENTE));
        when(subRepo.findById(ID_SUBTAREFA_1)).thenReturn(Optional.of(entidade));

        doNothing().when(mapper).atualizarSubTarefaMapper(isA(SubTarefaUpdateDto.class), isA(Subtarefa.class));

        SubTarefaDto esperado = SubTarefaDto.builder()
                .idSubTarefa(ID_SUBTAREFA_1)
                .tituloSubTarefa(TITULO_SUB_1)
                .status(ST_PENDENTE)
                .build();
        when(mapper.toDto(entidade)).thenReturn(esperado);

        SubTarefaDto dto = service.atualizarTituloSubTarefa(ID_SUBTAREFA_1, new SubTarefaUpdateDto(TITULO_SUB_1));

        assertSame(esperado, dto);
        verify(subRepo).findById(ID_SUBTAREFA_1);
        verify(mapper).atualizarSubTarefaMapper(isA(SubTarefaUpdateDto.class), isA(Subtarefa.class));
        verify(mapper).toDto(entidade);
    }

    @Test
    @DisplayName("não deve atualizar título quando tarefa estiver CANCELADA")
    void naoDeveAtualizarTituloComTarefaCancelada() {
        Subtarefa entidade = subCom(ID_SUBTAREFA_1, TITULO_SUB_1, ST_PENDENTE, tarefaComStatus(ID_TAREFA_1, TITULO_TAREFA_1, ST_CANCELADA));
        when(subRepo.findById(ID_SUBTAREFA_1)).thenReturn(Optional.of(entidade));
        assertThrows(RegraDeNegocioException.class, () -> service.atualizarTituloSubTarefa(ID_SUBTAREFA_1, new SubTarefaUpdateDto(TITULO_TAREFA_1)));
        verify(subRepo).findById(ID_SUBTAREFA_1);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("não deve atualizar título quando tarefa estiver CONCLUIDA")
    void naoDeveAtualizarTituloComTarefaConcluida() {
        Subtarefa entidade = subCom(ID_TAREFA_1, TITULO_SUB_1, ST_PENDENTE, tarefaComStatus(ID_TAREFA_1, TITULO_TAREFA_1, ST_CONCLUIDA));
        when(subRepo.findById(ID_TAREFA_1)).thenReturn(Optional.of(entidade));
        assertThrows(RegraDeNegocioException.class, () -> service.atualizarTituloSubTarefa(ID_TAREFA_1, new SubTarefaUpdateDto(TITULO_TAREFA_1)));
        verify(subRepo).findById(ID_TAREFA_1);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("deve lançar 404 ao atualizar título de subtarefa inexistente")
    void deveLancar404AoAtualizarTitulo() {
        when(subRepo.findById(ID_SUBTAREFA_INEXISTENTE)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizarTituloSubTarefa(ID_SUBTAREFA_INEXISTENTE, new SubTarefaUpdateDto(TITULO_SUB_2)));
        verify(subRepo).findById(ID_SUBTAREFA_INEXISTENTE);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("deve atualizar status da subtarefa com sucesso")
    void deveAtualizarStatus() {
        Subtarefa entidade = subCom(ID_SUBTAREFA_1, TITULO_SUB_1, ST_EM_ANDAMENTO, tarefaComStatus(ID_TAREFA_1, TITULO_TAREFA_1, ST_PENDENTE));
        when(subRepo.findById(ID_SUBTAREFA_1)).thenReturn(Optional.of(entidade));

        SubTarefaDto esperado = SubTarefaDto.builder()
                .idSubTarefa(ID_SUBTAREFA_1)
                .tituloSubTarefa(TITULO_SUB_1)
                .status(ST_CONCLUIDA)
                .build();
        when(mapper.toDto(entidade)).thenReturn(esperado);

        SubTarefaDto dto = service.atualizarStatusSubTarefa(ID_SUBTAREFA_1, new SubTarefaStatusDto(ST_CONCLUIDA));

        assertEquals(ST_CONCLUIDA, dto.getStatus());
        verify(subRepo).findById(ID_SUBTAREFA_1);
        verify(mapper).toDto(entidade);
    }

    @Test
    @DisplayName("não deve atualizar status quando tarefa estiver CANCELADA")
    void naoDeveAtualizarStatusComTarefaCancelada() {
        Subtarefa entidade = subCom(ID_SUBTAREFA_1, TITULO_SUB_1, ST_PENDENTE, tarefaComStatus(ID_TAREFA_1, TITULO_TAREFA_1, ST_CANCELADA));
        when(subRepo.findById(ID_SUBTAREFA_1)).thenReturn(Optional.of(entidade));
        assertThrows(RegraDeNegocioException.class, () -> service.atualizarStatusSubTarefa(ID_SUBTAREFA_1, new SubTarefaStatusDto(ST_CONCLUIDA)));
        verify(subRepo).findById(ID_SUBTAREFA_1);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("deve lançar 404 ao atualizar status de subtarefa inexistente")
    void deveLancar404AoAtualizarStatus() {
        when(subRepo.findById(ID_SUBTAREFA_INEXISTENTE)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizarStatusSubTarefa(ID_SUBTAREFA_INEXISTENTE, new SubTarefaStatusDto(ST_CONCLUIDA)));
        verify(subRepo).findById(ID_SUBTAREFA_INEXISTENTE);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("deve deletar subtarefa existente")
    void deveDeletar() {
        when(subRepo.existsById(ID_SUBTAREFA_1)).thenReturn(true);
        doNothing().when(subRepo).deleteById(ID_SUBTAREFA_1);

        service.deletarSubTarefa(ID_SUBTAREFA_1);

        verify(subRepo).existsById(ID_SUBTAREFA_1);
        verify(subRepo).deleteById(ID_SUBTAREFA_1);
    }

    @Test
    @DisplayName("deve lançar 404 ao deletar subtarefa inexistente")
    void deveLancar404AoDeletar() {
        when(subRepo.existsById(ID_SUBTAREFA_INEXISTENTE)).thenReturn(false);
        assertThrows(RecursoNaoEncontradoException.class, () -> service.deletarSubTarefa(ID_SUBTAREFA_INEXISTENTE));
        verify(subRepo).existsById(ID_SUBTAREFA_INEXISTENTE);
        verify(subRepo, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("buscarSubTarefaById deve retornar entidade")
    void buscarSubTarefaById() {
        Subtarefa entidade = subCom(ID_SUBTAREFA_1, TITULO_SUB_1, ST_PENDENTE, tarefaComStatus(ID_TAREFA_1, TITULO_TAREFA_1, ST_PENDENTE));
        when(subRepo.findById(ID_SUBTAREFA_1)).thenReturn(Optional.of(entidade));

        Subtarefa s = service.buscarSubTarefaById(ID_SUBTAREFA_1);

        assertSame(entidade, s);
        verify(subRepo).findById(ID_SUBTAREFA_1);
    }

    @Test
    @DisplayName("buscarSubTarefaById deve lançar 404 quando não existir")
    void buscarSubTarefaByIdNaoExistente() {
        when(subRepo.findById(ID_SUBTAREFA_INEXISTENTE)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarSubTarefaById(ID_SUBTAREFA_INEXISTENTE));
        verify(subRepo).findById(ID_SUBTAREFA_INEXISTENTE);
    }

    @Test
    @DisplayName("possuiPendentes deve retornar false")
    void possuiPendentesFalse() {
        when(subRepo.existeNaoConcluida(ID_TAREFA_1, ST_CONCLUIDA)).thenReturn(false);
        assertFalse(service.possuiPendentes(ID_TAREFA_1));
        verify(subRepo).existeNaoConcluida(ID_TAREFA_1, ST_CONCLUIDA);
    }

    @Test
    @DisplayName("possuiPendentes deve retornar true")
    void possuiPendentesTrue() {
        when(subRepo.existeNaoConcluida(ID_TAREFA_1, ST_CONCLUIDA)).thenReturn(true);
        assertTrue(service.possuiPendentes(ID_TAREFA_1));
        verify(subRepo).existeNaoConcluida(ID_TAREFA_1, ST_CONCLUIDA);
    }
}
