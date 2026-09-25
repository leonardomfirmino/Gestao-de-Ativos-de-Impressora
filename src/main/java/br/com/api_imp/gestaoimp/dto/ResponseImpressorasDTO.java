package br.com.api_imp.gestaoimp.dto;


import br.com.api_imp.gestaoimp.model.ImpressorasModel;

public record ResponseImpressorasDTO(
    Long id, 
    Long idLocal,
    String modelo,
    String serial,
    String assetTag,
    String ip,
    String local, 
    String status) {

    public  ResponseImpressorasDTO(ImpressorasModel imp){
        this(
            imp.getId(),
            imp.getLocalAtual() != null ? imp.getLocalAtual().getIdLocal() : null,
            imp.getModelo(),
            imp.getSerial(),
            imp.getAssetTag(),
            imp.getIp(),
            imp.getLocalAtual()!= null ? imp.getLocalAtual().getNomeLocal(): "Sem Local",
            imp.getStatusAtual().name()
        );
    }
    

}
