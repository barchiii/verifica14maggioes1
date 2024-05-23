enum tipoContratto{abitazione,cellulare,aziendale};

public class Contatto {
    public String nome;
    public String cognome;
    public String telefono;
    public tipoContratto tipo;

    protected boolean nascosto;

    public String stampaN()
    {
        String check = "";
        if (nascosto) {
            check="privato";
        }else {
            check="pubblico";
        }
        return String.format("Nome: %s Cognome: %s Telefono: %s, tipo: %s, check: %s", nome, cognome, telefono, tipo.toString(),check);
    }

    public String stampa()
    {
        return String.format("Nome: %s Cognome: %s Telefono: %s, tipo: %s", nome, cognome, telefono, tipo.toString());
    }
    @Override
    public String toString()
    {
        return String.format("%s,%s,%s,%s", nome, cognome, telefono, tipo.toString());
    }
}
