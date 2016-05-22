package com.equipe.yesmrwhite.quimica;

import java.util.List;

/**
 * Created by magdi on 20/05/2016.
 */
public class Molecula {
    private List<String> elementosMolecula;
    private String nome;
    private String molecula;

    public List<String> getElementosMolecula() {
        return elementosMolecula;
    }

    public void setElementosMolecula(List<String> elementosMolecula) {
        this.elementosMolecula = elementosMolecula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMolecula() {
        return molecula;
    }

    public void setMolecula(String molecula) {
        this.molecula = molecula;
    }
}
