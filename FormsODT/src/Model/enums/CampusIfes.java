package Model.enums;

public enum CampusIfes {
    ALEGRE("Alegre"),
    ARACRUZ("Aracruz"),
    BARRA_DE_SAO_FRANCISCO("Barra de São Francisco"),
    CACHOEIRO_DE_ITAPEMIRIM("Cachoeiro de Itapemirim"),
    CARIACICA("Cariacica"),
    CEFOR("Cefor"),
    CENTRO_SERRANO("Centro-Serrano"),
    COLATINA("Colatina"),
    GUARAPARI("Guarapari"),
    IBATIBA("Ibatiba"),
    ITAPINA("Itapina"),
    LINHARES("Linhares"),
    MONTANHA("Montanha"),
    NOVA_VENECIA("Nova Venécia"),
    PIUMA("Piúma"),
    PRESIDENTE_KENNEDY("Presidente Kennedy"),
    REITORIA("Reitoria"),
    SANTA_TERESA("Santa Teresa"),
    SAO_MATEUS("São Mateus"),
    SERRA("Serra"),
    VENDA_NOVA_DO_IMIGRANTE("Venda Nova do Imigrante"),
    VIANA("Viana"),
    VILA_VELHA("Vila Velha"),
    VITORIA("Vitória");

    private final String nome;

    CampusIfes(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
