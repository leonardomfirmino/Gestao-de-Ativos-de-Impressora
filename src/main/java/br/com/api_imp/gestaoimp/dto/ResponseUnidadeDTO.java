package br.com.api_imp.gestaoimp.dto;

public record ResponseUnidadeDTO(
    Long id,
    String name,          
    String description,   
    Long printerCount ) {
    
}
