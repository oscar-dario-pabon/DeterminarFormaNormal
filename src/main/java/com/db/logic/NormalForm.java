package com.db.logic;

import com.db.entities.Atribute;
import com.db.entities.Clousure;
import com.db.entities.DependenciaFuncional;
import com.db.util.Util;
import java.util.ArrayList;

import org.paukov.combinatorics.ICombinatoricsVector;


public class NormalForm {

    public boolean validarSiEstaEnSegundaFormaNormal(ArrayList<DependenciaFuncional> dependenciasFuncionales,
            ArrayList<Atribute> atributosUniverso, ArrayList<ArrayList<Atribute>> llavesCandidatas) {

        // paso 0 si la union de todos los elementos de la dependencia no esta
        // contenido en la totalidad de los atributos => no esta en 2fn
        ArrayList<Atribute> atributosDeDependencias = unirAtributosDeDependencias(dependenciasFuncionales);
        boolean estaEn2FN = false;

        if (!attDeDependenciasEsIgualAAtributosUniverso(atributosDeDependencias, atributosUniverso)) {
            System.out.println("Un atributo de las dependencias no esta contenido en los atributos definidos "
                    + "esto quiere decir que ese atributo esta definido solo por el mismo, (y que hace parte de la llave)."
                    + " por tanto no esta en 2FN");
            return estaEn2FN;
        } else {
            estaEn2FN = true;
        }

        if (estaEn2FN) {

            // Paso 1 Hallar Recobrimiento minimo
//			MinimalCovering objRecubrimientoMinimo = new MinimalCovering();
//			ArrayList<DependenciaFuncional> recubrimientoMinimo = objRecubrimientoMinimo
//					.runRecubrimientoMinimo(dependenciasFuncionales);
            // Paso 2 Deberia estar en 2FN
            // Paso 3 Obtener Claves
//			Keys objLlaves = new Keys();
//			objLlaves.calcularLLavesCandidatas(recubrimientoMinimo, atributosUniverso);
//			ArrayList<ArrayList<Atribute>> llavesCandidatas = objLlaves.getLlavesCandidatas();
            // Paso 4 Obtener Q(att no principales, los que no estan en las
            // llaves) y P(att principales)
            ArrayList<Atribute> atributosLlave = obtenerAtributosDeLasLLaves(llavesCandidatas);
            //estaEn2FN = llavesSonIgualesALosAtributosPrincipales(atributosLlave, atributosUniverso);

            if (atributosLlave.size() < atributosUniverso.size()) {
                //si la longitud de todas las llaves es uno entonces no hay subconjuntos de estas y por tanto
                //esta en segunda forma normal

                estaEn2FN = longitudTodasLasLlavesEsUno(llavesCandidatas);
                if (estaEn2FN) {
                    System.out.println("La longitud de todas las llaves es uno, no hay subconjuntos de estas"
                            + " por lo tanto esta en segunda forma normal " + estaEn2FN);
                    return true;
                }

                if (!estaEn2FN) {
                    ArrayList<Atribute> conjuntoQ = obtenerConjuntoQ(atributosLlave, atributosUniverso);
                    System.out.print("Q: ");
                    Util.imprimirListaAtt(conjuntoQ);

                    for (ArrayList<Atribute> llave : llavesCandidatas) {
                        //iteramos sobre los distintos subconjuntos de elementos que componen la llave
                        for (ICombinatoricsVector<String> subSet : Util.obtenerTodasLasCombinatorias(Util.obtenerArrayStringListDeListaDeAtributos(llave))) {
                            System.out.println("ES: " + subSet);
                            //No tomamos en cuenta el subconjunto vacio y la llave misma, que sería el unico conjunto
                            //con el mismo tamaño que la llave
                            if (subSet.getSize() > 0 && subSet.getSize() < llave.size()) {

                                ArrayList<Atribute> combinacionAtributos = Util.convertirArrayToArrayList(subSet.getVector());
                                Clousure objCierre = new Clousure();

                                ArrayList<Atribute> alAtributosCierreRecorridos = new ArrayList<>();
                                ArrayList<Atribute> alAtributosCierreCalculado = new ArrayList<>();
                                //obtenemos el cierre del subconjunto

                                ArrayList<Atribute> cierreAtributos = objCierre.calcularCierre(combinacionAtributos,
                                        alAtributosCierreRecorridos, dependenciasFuncionales,
                                        alAtributosCierreCalculado, null, false);

                                System.out.print("atributos");
                                Util.imprimirListaAtt(conjuntoQ);
                                System.out.print("atributos cierre:");
                                Util.imprimirListaAtt(cierreAtributos);

                                //verificamos si el cierre de atributos contiene elementos que la llave implica
                                if (!laInterseccionEsVacia(cierreAtributos, conjuntoQ)) {
                                    System.out.println("Se encuentra un conjunto de elementos que estan implicados por un subconjunto"
                                            + " de la llave, por tanto no esta en 2FN." + estaEn2FN);
                                    //si el subconjunto implica algún elemento el conjunto no se encuentra en segunda
                                    //forma normal y terminamos
                                    estaEn2FN = false;
                                    return estaEn2FN;
                                }
                            }
                        }
                    }
                    estaEn2FN = true;
                    System.out.println("Cinco " + estaEn2FN);
                }
            } else {
                //System.out.println("La llave contiene todos los elementos declarados, por tanto esta en segunda forma normal" + estaEn2FN);
            }
        }

        return estaEn2FN;
    }

    private boolean laInterseccionEsVacia(ArrayList<Atribute> cierreAtributos, ArrayList<Atribute> conjuntoQ) {

        for (Atribute elementoDeQ : conjuntoQ) {
            if (cierreAtributos.contains(elementoDeQ)) {
                return false;
            }
        }
        return true;
    }

    private boolean atributosIguales(ArrayList<Atribute> atributosA, ArrayList<Atribute> atributosB) {

        if (atributosA.size() != atributosB.size()) {
            return false;
        } else {
            for (Atribute a : atributosA) {
                if (!atributosB.contains(a)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean longitudTodasLasLlavesEsUno(ArrayList<ArrayList<Atribute>> llavesCandidatas) {

        for (ArrayList<Atribute> llaveCandidata : llavesCandidatas) {
            if (llaveCandidata.size() > 1) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<Atribute> obtenerConjuntoQ(ArrayList<Atribute> atributosLlave,
            ArrayList<Atribute> atributosUniverso) {

        ArrayList<Atribute> conjuntoQ = new ArrayList<>();
        for (Atribute atributo : atributosUniverso) {
            if (!atributosLlave.contains(atributo)) {
                conjuntoQ.add(atributo);
            }
        }
        return conjuntoQ;
    }

//    /**
//     * Si retorna true, significa que el conjunto Q(atts no estan en las llaves)
//     * es vacio
//     *
//     * @param atributosLlave
//     * @param atributosUniverso
//     * @return
//     */
//    private boolean llavesSonIgualesALosAtributosPrincipales(ArrayList<Atribute> atributosLlave,
//            ArrayList<Atribute> atributosUniverso) {
//        if (atributosLlave.size() == atributosUniverso.size()) {
//            return true;
//        }
//        return false;
//    }
    private boolean attDeDependenciasEsIgualAAtributosUniverso(ArrayList<Atribute> atributosFuente,
            ArrayList<Atribute> atributosUniverso) {

        if (atributosFuente.size() == atributosUniverso.size()) {
            // no esta en segunda forma
            return true;
        }
        return false;
    }

    private ArrayList<Atribute> obtenerAtributosDeLasLLaves(ArrayList<ArrayList<Atribute>> llavesCandidatas) {

        ArrayList<Atribute> attLlaves = new ArrayList<>();
        for (ArrayList<Atribute> arrayList : llavesCandidatas) {
            for (Atribute atribute : arrayList) {
                if (!attLlaves.contains(atribute)) {
                    attLlaves.add(atribute);
                }
            }
        }
        return attLlaves;
    }

    private ArrayList<Atribute> unirAtributosDeDependencias(ArrayList<DependenciaFuncional> dependenciasResultantes) {

        ArrayList<Atribute> dependenciasAgrupadas = new ArrayList<>();

        for (DependenciaFuncional dependenciasResultante : dependenciasResultantes) {

            for (Atribute implicantes : dependenciasResultante.getImplicantes()) {

                if (!dependenciasAgrupadas.contains(implicantes)) {
                    dependenciasAgrupadas.add(implicantes);
                }
            }
            for (Atribute implicados : dependenciasResultante.getImplicados()) {
                if (!dependenciasAgrupadas.contains(implicados)) {
                    dependenciasAgrupadas.add(implicados);
                }
            }
        }

        return dependenciasAgrupadas;
    }

    public boolean validarSiEstaEnTerceraFormaNormal(ArrayList<DependenciaFuncional> dependenciasFuncionales, ArrayList<ArrayList<Atribute>> llavesCandidatas) {
        for (ArrayList<Atribute> llave : llavesCandidatas) {
            for (DependenciaFuncional dependenciaFuncional : dependenciasFuncionales) {
                if (!this.atributosIguales(dependenciaFuncional.getImplicantes(), llave)) {
                    return false;
                }
            }
        }

        return true;
    }

}
