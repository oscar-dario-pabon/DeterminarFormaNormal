package com.db.entities;

import java.util.ArrayList;

public class DependenciaFuncional {

    protected ArrayList<Atribute> implicantes;
    protected ArrayList<Atribute> implicados;

    public DependenciaFuncional(ArrayList<Atribute> implicantes, ArrayList<Atribute> implicados) {
        this.implicantes = implicantes;
        this.implicados = implicados;

    }

    public String getImplicantesAsString(String separator) {
        String elements = "";
        for (Atribute atribute : implicantes) {
            elements += atribute.getNombre() + separator;
        }
        return elements;
    }

    public String getImplicadosAsString(String separator) {
        String elements = "";
        for (Atribute atribute : implicados) {
            elements += atribute.getNombre() + separator;
        }
        return elements;
    }

    public ArrayList<Atribute> getImplicantes() {
        return implicantes;
    }

    public void setImplicantes(ArrayList<Atribute> implicantes) {
        this.implicantes = implicantes;
    }

    public ArrayList<Atribute> getImplicados() {
        return implicados;
    }

    public void setImplicados(ArrayList<Atribute> implicados) {
        this.implicados = implicados;
    }

    @Override
    public String toString() {
        String s = "";
        for (Atribute implicante : implicantes) {
            s += implicante.getNombre();
        }
        s += "->";
        for (Atribute implicado : implicados) {
            s += implicado.getNombre();
        }
        return s;
    }

}
