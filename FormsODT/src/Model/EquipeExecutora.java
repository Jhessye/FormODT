package Model;

public class EquipeExecutora {
    private int estudantesFic = 0;
    private int estudantesTecnico = 0;
    private int estudantesGraduacao = 0;
    private int estudantesPosGraduacao = 0;
    private int servidoresDocentes = 0;
    private int servidoresTecnicoAdministrativos = 0;
    private int colaboradoresExternos = 0;
    private boolean haCoordenadorAdjunto = false;

    public EquipeExecutora() {}

    public int getEstudantesFic() {
        return estudantesFic;
    }

    public void setEstudantesFic(int estudantesFic) {
        this.estudantesFic = estudantesFic;
    }

    public int getEstudantesTecnico() {
        return estudantesTecnico;
    }

    public void setEstudantesTecnico(int estudantesTecnico) {
        this.estudantesTecnico = estudantesTecnico;
    }

    public int getEstudantesGraduacao() {
        return estudantesGraduacao;
    }

    public void setEstudantesGraduacao(int estudantesGraduacao) {
        this.estudantesGraduacao = estudantesGraduacao;
    }

    public int getEstudantesPosGraduacao() {
        return estudantesPosGraduacao;
    }

    public void setEstudantesPosGraduacao(int estudantesPosGraduacao) {
        this.estudantesPosGraduacao = estudantesPosGraduacao;
    }

    public int getServidoresDocentes() {
        return servidoresDocentes;
    }

    public void setServidoresDocentes(int servidoresDocentes) {
        this.servidoresDocentes = servidoresDocentes;
    }

    public int getServidoresTecnicoAdministrativos() {
        return servidoresTecnicoAdministrativos;
    }

    public void setServidoresTecnicoAdministrativos(int servidoresTecnicoAdministrativos) {
        this.servidoresTecnicoAdministrativos = servidoresTecnicoAdministrativos;
    }

    public int getColaboradoresExternos() {
        return colaboradoresExternos;
    }

    public void setColaboradoresExternos(int colaboradoresExternos) {
        this.colaboradoresExternos = colaboradoresExternos;
    }

    public boolean isHaCoordenadorAdjunto() {
        return haCoordenadorAdjunto;
    }

    public void setHaCoordenadorAdjunto(boolean haCoordenadorAdjunto) {
        this.haCoordenadorAdjunto = haCoordenadorAdjunto;
    }
}
