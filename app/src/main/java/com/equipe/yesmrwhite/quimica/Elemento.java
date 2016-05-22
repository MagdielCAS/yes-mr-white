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

    public Elemento() {
    }

    public Elemento(String familia, String simbolo, String nome, String imagem, String numeroAtomico, String massaAtomica, String linha, String coluna) {
        setFamilia(familia);
        setSimbolo(simbolo);
        setNome(nome);
        setImagem(imagem);
        setNumeroAtomico(numeroAtomico);
        setMassaAtomica(massaAtomica);
        setLinha(linha);
        setColuna(coluna);
    }

    public String getClassificacao() {
        int linha = Integer.parseInt(getLinha());
        int coluna = Integer.parseInt(getColuna());
        if (getFamilia().equals("Lantanidios")) {
            return "Metal/Lantanóide";
        } else {
            if (getFamilia().equals("Actinidios")) {
                return "Metal/Actinóide";
            } else {
                if (getFamilia().equals("GasesNobres")) {
                    return "Não-Metal/Gás Nobre";
                } else {
                    if (linha >= 2) {
                        if (coluna == 1) {
                            return "Metal/Alcalino";
                        } else {
                            if (coluna == 2) {
                                return "Metal/Alcalino-terroso";
                            } else {
                                if (coluna <= 12) {
                                    return "Metal/Metal de Transição";
                                } else {
                                    switch (coluna) {
                                        case 13:
                                            if (linha >= 3) {
                                                return "Metal/Metal de pós-transição";
                                            } else {
                                                return "Semimetal";
                                            }
                                        case 14:
                                            if (linha >= 5) {
                                                return "Metal/Metal de pós-transição";
                                            } else if (linha >= 3) {
                                                return "Semimetal";
                                            } else {
                                                return "Não-Metal/Outro Não-Metal";
                                            }
                                        case 15:
                                            if (linha >= 6) {
                                                return "Metal/Metal de pós-transição";
                                            } else if (linha >= 4) {
                                                return "Semimetal";
                                            } else {
                                                return "Não-Metal/Outro Não-Metal";
                                            }
                                        case 16:
                                            if (linha >= 7) {
                                                return "Metal/Metal de pós-transição";
                                            } else if (linha >= 5) {
                                                return "Semimetal";
                                            } else {
                                                return "Não-Metal/Outro Não-Metal";
                                            }
                                        case 17:
                                            return "Não-Metal/Halogêneo";
                                        default:
                                            if(getSimbolo().equals("H")){
                                                return "Não-Metal/Outro Não-Metal";
                                            }else {
                                                return null;
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public String getTipo(){
        if(getClassificacao()!=null){
            return getClassificacao().split("/")[0];
        }
        return null;
    }
    public String getSubTipo(){
        String[] sub = getClassificacao().split("/");
        return sub.length==1?sub[0]:sub[1];
    }
}

