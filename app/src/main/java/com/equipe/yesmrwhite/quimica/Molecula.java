package com.equipe.yesmrwhite.quimica;

import java.util.List;

/**
 * Created by magdi on 20/05/2016.
 */
public class Molecula {
    private List<Elemento> molecula;
    private String nome;

    public List<Elemento> getMolecula() {
        return molecula;
    }

    public void setMolecula(List<Elemento> molecula) {
        this.molecula = molecula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
