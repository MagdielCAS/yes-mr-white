package com.equipe.yesmrwhite.quimica;

/**
 * Created by magdi on 20/05/2016.
 */
public class Elemento {
    private String familia;
    private String simbolo;
    private String nome;
    private String imagem;
    private String numeroAtomico;
    private String massaAtomica;
    private String linha;
    private String coluna;

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getNumeroAtomico() {
        return numeroAtomico;
    }

    public void setNumeroAtomico(String numeroAtomico) {
        this.numeroAtomico = numeroAtomico;
    }

    public String getMassaAtomica() {
        return massaAtomica;
    }

    public void setMassaAtomica(String massaAtomica) {
        this.massaAtomica = massaAtomica;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public String getColuna() {
        return coluna;
    }

    public void setColuna(String coluna) {
        this.coluna = coluna;
    }

    public Elemento(){}

    public Elemento(String familia,String simbolo, String nome, String imagem, String numeroAtomico, String massaAtomica, String linha, String coluna ){
        setFamilia(familia);
        setSimbolo(simbolo);
        setNome(nome);
        setImagem(imagem);
        setNumeroAtomico(numeroAtomico);
        setMassaAtomica(massaAtomica);
        setLinha(linha);
        setColuna(coluna);
    }
}
