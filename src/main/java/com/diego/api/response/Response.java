package com.diego.api.response;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {

    private T resultado;
    private List<String> erros;

    public T getResultado() {
        return resultado;
    }

    public void setResultado(T resultado) {
        this.resultado = resultado;
    }

    public List<String> getErros() {
        if(this.erros == null){
            this.erros = new ArrayList<>();
        }
        return erros;
    }

    public void setErros(List<String> erros) {
        this.erros = erros;
    }
}