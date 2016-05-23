package com.equipe.yesmrwhite.quimica;

/**
 * Created by magdi on 22/05/2016.
 */
public class ElementoMolecula {
    private String elemento;
    private int prioridade;

    public ElementoMolecula(String elemento,int prioridade){
        setElemento(elemento);
        setPrioridade(prioridade);
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }
}
