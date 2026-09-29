package Model;

import java.util.ArrayList;
import java.util.List;
import Model.enums.AreaTematica;

public class AreasTematicas {
    private AreaTematica areaPrincipal = AreaTematica.EDUCACAO;
    private AreaTematica areaSecundaria = null;
    private List<String> odsSelecionados = new ArrayList<>();

    public AreasTematicas() {}

    public AreaTematica getAreaPrincipal() {
        return areaPrincipal;
    }

    public void setAreaPrincipal(AreaTematica areaPrincipal) {
        this.areaPrincipal = areaPrincipal;
    }

    public AreaTematica getAreaSecundaria() {
        return areaSecundaria;
    }

    public void setAreaSecundaria(AreaTematica areaSecundaria) {
        this.areaSecundaria = areaSecundaria;
    }

    public List<String> getOdsSelecionados() {
        return odsSelecionados;
    }

    public void setOdsSelecionados(List<String> odsSelecionados) {
        this.odsSelecionados = odsSelecionados;
    }
}
