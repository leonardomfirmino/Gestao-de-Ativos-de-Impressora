package br.com.api_imp.gestaoimp.dto;

import java.time.LocalDateTime;

import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;

public record ResponseMovimentacaoDTO(
    Long id,
    String serialImpressora,
    String modeloImpressora,
    String localOrigem,
    String localDestino,
    String descricaoMotivo,
    LocalDateTime dataMovimentacao
) {
    public static ResponseMovimentacaoDTO fromModel(MovimentacaoImpressoraModel model) {
        return new ResponseMovimentacaoDTO(
            model.getId(),
            model.getImpressora() != null ? model.getImpressora().getSerial() : "N/A",
            model.getImpressora() != null ? model.getImpressora().getModelo() : "N/A",
            model.getLocalOrigem() != null ? model.getLocalOrigem().getNomeLocal() : "N/A",
            model.getLocalDestino() != null ? model.getLocalDestino().getNomeLocal() : "N/A",
            model.getDescricaoMotivo(),
            model.getDataMovimentacao()
        );
    }
}
