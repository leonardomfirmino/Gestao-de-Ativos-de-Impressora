package br.com.api_imp.gestaoimp.dto;
public record RequestImpressoraDTO(
    String model,
    String serial,
    String local,
    String ip,
    String assetTag,
    String status,
    String descricaoMotivo
) {}
