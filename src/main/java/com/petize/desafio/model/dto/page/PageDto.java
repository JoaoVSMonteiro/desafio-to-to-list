package com.petize.desafio.model.dto.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {
    @Schema(description = "Total de elementos da página")
    private Long totalElements;

    @Schema(description = "Quantidade total de páginas")
    private Integer totalPages;

    @Schema(description = "Página solicitada (começa por zero)")
    private Integer page;

    @Schema(description = "Quantidade de registros por página solicitado")
    private Integer size;

    @Schema(description = "Lista de registros da página")
    private List<?> content;
}
