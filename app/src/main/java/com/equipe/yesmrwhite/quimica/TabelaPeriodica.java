package com.equipe.yesmrwhite.quimica;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by magdi on 19/05/2016.
 */
public class TabelaPeriodica {
    private List<Elemento> tabelaPeriodica;
    private final static String[] colunas = {"1A","2A","3B","4B","5B","6B","7B","8B","1B","2B","3A","4A","5A","6A","7A","GasesNobres","Actinidios","Lantanidios"};
    private String[] splitedLine;

    public TabelaPeriodica(List<Elemento> tabela){
        this.tabelaPeriodica = tabela;
    }

    public TabelaPeriodica(Context context) {
        InputStream arq;
        String lstrlinha;
        AssetManager assetManager = context.getResources().getAssets();
        tabelaPeriodica = new ArrayList<Elemento>();

        int i = 0;

        for (String colum:colunas) {

            try {

                arq = assetManager.open("elements/"+colum+".txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(arq));
                while ((lstrlinha = br.readLine()) != null) {
                    splitedLine = lstrlinha.split("-");
                    tabelaPeriodica.add(new Elemento(colum,splitedLine[0],splitedLine[1],splitedLine[2],splitedLine[3],splitedLine[4],splitedLine[5],splitedLine[6]));
                    //System.out.println("Contrutor: ".concat(tabelaPeriodica.get(i).getNome()));
                    i++;
                }
                arq.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Elemento> getTabela(){
        return tabelaPeriodica;
    }

    public Elemento getElementBySymbol(String symbol){
        for (Elemento elemento:this.tabelaPeriodica) {
            if(elemento.getSimbolo().equals(symbol)){
                return elemento;
            }
        }
        return null;
    }

    public Elemento getElementByName(String name){
        //Os nomes tem acento, atentar a isso quando botar o nome
        for (Elemento elemento:this.tabelaPeriodica) {
            if(elemento.getNome().equals(name)){
                return elemento;
            }
        }
        return null;
    }

    public String getPathImageBySymbol(String symbol) {

        for (Elemento elemento:this.tabelaPeriodica) {
            if(elemento.getNome().equals(symbol)){
                return elemento.getImagem();
            }
        }
        return null;
    }

    public String getPathImageByName(String name) {
        //Os nomes tem acento, atentar a isso quando botar o nome
        for (Elemento elemento:this.tabelaPeriodica) {
            if(elemento.getNome().equals(name)){
                return elemento.getImagem();
            }
        }
        return null;
    }

    public List<Elemento> getElementByClassTipo(String c){
        //Os nomes tem acento, atentar a isso quando botar o nome
        List<Elemento> elementos = new ArrayList<Elemento>();
        for (Elemento elemento:this.tabelaPeriodica) {
            if(elemento.getTipo() != null && elemento.getTipo().equals(c)){
                elementos.add(elemento);
            }
        }
        return elementos;
    }
    public List<Elemento> getElementByClassSubTipo(String c){
        //Os nomes tem acento, atentar a isso quando botar o nome
        List<Elemento> elementos = new ArrayList<Elemento>();
        for (Elemento elemento:this.tabelaPeriodica) {
            if(elemento.getSubTipo().equals(c)){
                elementos.add(elemento);
            }
        }
        return elementos;
    }
}
