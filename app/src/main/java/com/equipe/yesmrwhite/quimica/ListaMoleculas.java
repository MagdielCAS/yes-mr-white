package com.equipe.yesmrwhite.quimica;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by magdi on 21/05/2016.
 */
public class ListaMoleculas {
    private List<Molecula> moleculas;

    public Molecula getMoleculeByIndex(int i){
        return this.moleculas.get(i);
    }

    public int size(){
        return this.moleculas.size();
    }

    public ListaMoleculas(Context context){
        InputStream arq;
        String lstrlinha;
        String[] splitedLine;
        AssetManager assetManager = context.getResources().getAssets();
        moleculas = new ArrayList<Molecula>();
        try {

            arq = assetManager.open("elements/Moleculas.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(arq));
            int j=0;
            while ((lstrlinha = br.readLine()) != null) {
                splitedLine = lstrlinha.split("-");
                List<ElementoMolecula> elementos = new ArrayList<ElementoMolecula>();
                int prioridade;
                for (int i = 2;i<splitedLine.length;i++){
                    prioridade = i-2;
                    for(int k =0;k<elementos.size();k++) {
                        if(elementos.get(k).getElemento().equals(splitedLine[i])){
                            prioridade = k;
                            break;
                        }
                    }
                    elementos.add(new ElementoMolecula(splitedLine[i], prioridade));
                }
                moleculas.add(new Molecula(splitedLine[0],splitedLine[1],elementos));
                System.out.println(moleculas.get(j).getNome());
                j++;
            }
            arq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
