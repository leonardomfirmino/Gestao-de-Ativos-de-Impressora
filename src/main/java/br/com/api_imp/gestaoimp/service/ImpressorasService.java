package br.com.api_imp.gestaoimp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.api_imp.gestaoimp.repository.ImpressorasRepository;
import br.com.api_imp.gestaoimp.model.ImpressorasModel;



@Service
public class ImpressorasService {
    private final ImpressorasRepository imp_Repository;

    public ImpressorasService(ImpressorasRepository imp_Repository){
        this.imp_Repository = imp_Repository;
    }

    public List<ImpressorasModel> mostrarImp(){
        return imp_Repository.findAll();
    }
}
