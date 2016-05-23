package com.equipe.yesmrwhite.quimica;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by magdi on 20/05/2016.
 */
public class Molecula {
    private List<ElementoMolecula> elementosMolecula;
    private String nome;
    private String molecula;

    public int numElements(){
        return elementosMolecula.size();
    }

    public Molecula(String nome, String molecula, List<ElementoMolecula> elementosMolecula){
        setNome(nome);
        setMolecula(molecula);
        setElementosMolecula(elementosMolecula);
    }

    public ElementoMolecula getElementMoleculeByIndex(int i){
        return elementosMolecula.get(i);
    }

    public List<ElementoMolecula> getElementosMolecula() {
        return elementosMolecula;
    }

    public void setElementosMolecula(List<ElementoMolecula> elementosMolecula) {
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
