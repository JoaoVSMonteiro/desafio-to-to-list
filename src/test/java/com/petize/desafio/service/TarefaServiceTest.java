package com.petize.desafio.service;


import com.petize.desafio.exception.RecursoNaoEncontradoException;
import com.petize.desafio.exception.RegraDeNegocioException;
import com.petize.desafio.model.dto.tarefa.TarefaCreateDto;
import com.petize.desafio.model.dto.tarefa.TarefaDto;
import com.petize.desafio.model.dto.tarefa.TarefaStatusDto;
import com.petize.desafio.model.dto.tarefa.TarefaUpdateDto;
import com.petize.desafio.model.entity.Tarefa;
import com.petize.desafio.model.mapper.TarefaMapper;
import com.petize.desafio.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.petize.desafio.testdata.TestConstantes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class TarefaServiceTest {

    private TarefaRepository repo;
    private TarefaMapper mapper;
    private SubTarefaService subService;
    private TarefaService service;

    @BeforeEach
    void setUp() {
        repo = mock(TarefaRepository.class);
        mapper = mock(TarefaMapper.class);
        subService = mock(SubTarefaService.class);
        service = new TarefaService(repo, mapper, subService);
    }

    @Test
    @DisplayName("deve criar tarefa com sucesso e retornar DTO")
    void deveCriarTarefaComSucesso() {
        TarefaCreateDto entrada = TarefaCreateDto.builder()
                .tituloTarefa(TITULO_TAREFA_1)
                .descricao(DESCRICAO_TAREFA_1)
                .dataVencimento(DATA_VENCIMENTO_PADRAO)
                .status(ST_PENDENTE)
                .prioridade(PRIO_ALTA)
                .build();

        Tarefa entidade = new Tarefa();
        entidade.setIdTarefa(ID_TAREFA_1);
        entidade.setTituloTarefa(TITULO_TAREFA_1);
        entidade.setDescricao(DESCRICAO_TAREFA_1);
        entidade.setDataVencimento(DATA_VENCIMENTO_PADRAO);
        entidade.setStatus(ST_PENDENTE);
        entidade.setPrioridade(PRIO_ALTA);

        TarefaDto esperado = TarefaDto.builder()
                .idTarefa(ID_TAREFA_1)
                .tituloTarefa(TITULO_TAREFA_1)
                .descricao(DESCRICAO_TAREFA_1)
                .dataVencimento(DATA_VENCIMENTO_PADRAO)
                .status(ST_PENDENTE)
                .prioridade(PRIO_ALTA)
                .build();

        when(repo.existsByTituloTarefaIgnoreCase(TITULO_TAREFA_1)).thenReturn(false);
        when(mapper.toEntity(isA(TarefaCreateDto.class))).thenReturn(entidade);
        when(repo.save(entidade)).thenReturn(entidade);
        when(mapper.toDto(entidade)).thenReturn(esperado);

        TarefaDto resultado = service.criarTarefa(entrada);

        assertSame(esperado, resultado);
        verify(repo).existsByTituloTarefaIgnoreCase(TITULO_TAREFA_1);
        verify(mapper).toEntity(entrada);
        verify(repo).save(entidade);
        verify(mapper).toDto(entidade);
    }

    @Test
    @DisplayName("não deve criar tarefa quando título já existir")
    void naoDeveCriarQuandoTituloDuplicado() {
        when(repo.existsByTituloTarefaIgnoreCase(TITULO_TAREFA_DUPLICADA)).thenReturn(true);

        TarefaCreateDto entrada = TarefaCreateDto.builder()
                .tituloTarefa(TITULO_TAREFA_DUPLICADA)
                .build();

        assertThrows(RegraDeNegocioException.class, () -> service.criarTarefa(entrada));
        verify(repo).existsByTituloTarefaIgnoreCase(TITULO_TAREFA_DUPLICADA);
        verifyNoMoreInteractions(repo);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("deve lançar erro quando mapper falhar ao criar entidade")
    void deveLancarErroMapeandoCreate() {
        when(repo.existsByTituloTarefaIgnoreCase(anyString())).thenReturn(false);
        when(mapper.toEntity(isA(TarefaCreateDto.class)))
                .thenThrow(new RegraDeNegocioException(MSG_ERRO_MAPEAR));

        assertThrows(RegraDeNegocioException.class,
                () -> service.criarTarefa(TarefaCreateDto.builder().tituloTarefa(TITULO_TAREFA_1).build()));

        verify(repo).existsByTituloTarefaIgnoreCase(TITULO_TAREFA_1);
        verify(mapper).toEntity(isA(TarefaCreateDto.class));
        verifyNoMoreInteractions(repo);
    }

    @Test
    @DisplayName("deve buscar tarefa por id com sucesso")
    void deveBuscarPorId() {
        Tarefa entidade = new Tarefa();
        entidade.setIdTarefa(ID_TAREFA_1);
        entidade.setTituloTarefa(TITULO_TAREFA_1);

        TarefaDto esperado = TarefaDto.builder()
                .idTarefa(ID_TAREFA_1)
                .tituloTarefa(TITULO_TAREFA_1)
                .build();

        when(repo.findById(ID_TAREFA_1)).thenReturn(Optional.of(entidade));
        when(mapper.toDto(entidade)).thenReturn(esperado);

        TarefaDto dto = service.buscarTarefaPorId(ID_TAREFA_1);

        assertSame(esperado, dto);
        verify(repo).findById(ID_TAREFA_1);
        verify(mapper).toDto(entidade);
    }

    @Test
    @DisplayName("deve lançar 404 quando tarefa não encontrada ao buscar por id")
    void deveLancar404AoBuscarPorId() {
        when(repo.findById(ID_TAREFA_INEXISTENTE)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarTarefaPorId(ID_TAREFA_INEXISTENTE));
        verify(repo).findById(ID_TAREFA_INEXISTENTE);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("deve listar tarefas com filtros e mapear para DTO")
    void deveListarComFiltros() {
        Tarefa t1 = new Tarefa(); t1.setIdTarefa(ID_TAREFA_1); t1.setTituloTarefa(TITULO_TAREFA_1);
        Tarefa t2 = new Tarefa(); t2.setIdTarefa(ID_TAREFA_2); t2.setTituloTarefa(TITULO_TAREFA_2);

        TarefaDto d1 = TarefaDto.builder().idTarefa(ID_TAREFA_1).tituloTarefa(TITULO_TAREFA_1).build();
        TarefaDto d2 = TarefaDto.builder().idTarefa(ID_TAREFA_2).tituloTarefa(TITULO_TAREFA_2).build();

        when(repo.findByFiltros(ST_PENDENTE, PRIO_ALTA, DATA_VENCIMENTO_PADRAO))
                .thenReturn(List.of(t1, t2));
        when(mapper.toDto(t1)).thenReturn(d1);
        when(mapper.toDto(t2)).thenReturn(d2);

        var lista = service.listarTarefas(ST_PENDENTE, PRIO_ALTA, DATA_VENCIMENTO_PADRAO);

        assertEquals(2, lista.size());
        assertEquals(ID_TAREFA_1, lista.get(0).getIdTarefa());
        assertEquals(ID_TAREFA_2, lista.get(1).getIdTarefa());
        verify(repo).findByFiltros(ST_PENDENTE, PRIO_ALTA, DATA_VENCIMENTO_PADRAO);
        verify(mapper).toDto(t1);
        verify(mapper).toDto(t2);
    }

    @Test
    @DisplayName("deve atualizar tarefa com sucesso")
    void deveAtualizarTarefa() {
        TarefaUpdateDto update = new TarefaUpdateDto();
        Tarefa entidade = new Tarefa();
        entidade.setIdTarefa(ID_TAREFA_1);

        TarefaDto esperado = TarefaDto.builder().idTarefa(ID_TAREFA_1).tituloTarefa("Novo título").build();

        when(repo.findById(ID_TAREFA_1)).thenReturn(Optional.of(entidade));
        doNothing().when(mapper).atualizarTarefaMapper(update, entidade);
        when(mapper.toDto(entidade)).thenReturn(esperado);

        TarefaDto dto = service.atualizarTarefa(ID_TAREFA_1, update);

        assertSame(esperado, dto);
        verify(repo).findById(ID_TAREFA_1);
        verify(mapper).atualizarTarefaMapper(update, entidade);
        verify(mapper).toDto(entidade);
    }

    @Test
    @DisplayName("deve lançar 404 ao tentar atualizar tarefa inexistente")
    void deveLancar404AoAtualizar() {
        when(repo.findById(ID_TAREFA_INEXISTENTE)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.atualizarTarefa(ID_TAREFA_INEXISTENTE, new TarefaUpdateDto()));
        verify(repo).findById(ID_TAREFA_INEXISTENTE);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("não deve concluir tarefa com subtarefas pendentes")
    void naoDeveConcluirComPendentes() {
        Tarefa entidade = new Tarefa();
        entidade.setIdTarefa(ID_TAREFA_1);
        entidade.setStatus(ST_PENDENTE);

        when(repo.findById(ID_TAREFA_1)).thenReturn(Optional.of(entidade));
        when(subService.possuiPendentes(ID_TAREFA_1)).thenReturn(true);

        assertThrows(RegraDeNegocioException.class,
                () -> service.atualizarStatusTarefa(ID_TAREFA_1, new TarefaStatusDto(ST_CONCLUIDA)));

        verify(repo).findById(ID_TAREFA_1);
        verify(subService).possuiPendentes(ID_TAREFA_1);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("deve atualizar status quando não houver pendentes")
    void deveAtualizarStatusSemPendentes() {
        Tarefa entidade = new Tarefa();
        entidade.setIdTarefa(ID_TAREFA_1);
        entidade.setStatus(ST_EM_ANDAMENTO);

        TarefaDto esperado = TarefaDto.builder()
                .idTarefa(ID_TAREFA_1)
                .status(ST_CONCLUIDA)
                .build();

        when(repo.findById(ID_TAREFA_1)).thenReturn(Optional.of(entidade));
        when(subService.possuiPendentes(ID_TAREFA_1)).thenReturn(false);
        when(mapper.toDto(entidade)).thenReturn(esperado);

        TarefaDto dto = service.atualizarStatusTarefa(ID_TAREFA_1, new TarefaStatusDto(ST_CONCLUIDA));

        assertEquals(ST_CONCLUIDA, dto.getStatus());
        verify(repo).findById(ID_TAREFA_1);
        verify(subService).possuiPendentes(ID_TAREFA_1);
        verify(mapper).toDto(entidade);
    }

    @Test
    @DisplayName("deve deletar tarefa existente")
    void deveDeletar() {
        when(repo.existsById(ID_TAREFA_1)).thenReturn(true);
        doNothing().when(repo).deleteById(ID_TAREFA_1);

        service.deletarTarefa(ID_TAREFA_1);

        verify(repo).existsById(ID_TAREFA_1);
        verify(repo).deleteById(ID_TAREFA_1);
    }

    @Test
    @DisplayName("deve lançar 404 ao deletar tarefa inexistente")
    void deveLancar404AoDeletar() {
        when(repo.existsById(ID_TAREFA_INEXISTENTE)).thenReturn(false);
        assertThrows(RecursoNaoEncontradoException.class, () -> service.deletarTarefa(ID_TAREFA_INEXISTENTE));
        verify(repo).existsById(ID_TAREFA_INEXISTENTE);
        verify(repo, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("buscarTarefaById deve retornar entidade")
    void buscarTarefaById() {
        Tarefa entidade = new Tarefa();
        entidade.setIdTarefa(ID_TAREFA_1);
        when(repo.findById(ID_TAREFA_1)).thenReturn(Optional.of(entidade));

        Tarefa t = service.buscarTarefaById(ID_TAREFA_1);

        assertSame(entidade, t);
        verify(repo).findById(ID_TAREFA_1);
    }

    @Test
    @DisplayName("buscarTarefaById deve lançar 404 quando não existir")
    void buscarTarefaByIdNaoExistente() {
        when(repo.findById(ID_TAREFA_INEXISTENTE)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarTarefaById(ID_TAREFA_INEXISTENTE));
        verify(repo).findById(ID_TAREFA_INEXISTENTE);
    }
}

