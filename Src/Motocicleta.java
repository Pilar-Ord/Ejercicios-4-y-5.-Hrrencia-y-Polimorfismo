import java.util.*;

public class Motocicleta extends Vehiculo {
    private final int cilindraje;

    public Motocicleta(String placa, String marca, String modelo,
                       double tarifa, int diasPrevios, int cilindraje) {
        super(placa, marca, modelo, tarifa, diasPrevios);
        if (cilindraje <= 0) {
            throw new IllegalArgumentException(
                "Cilindraje debe ser positivo"
            );
        }
        this.cilindraje = cilindraje;
    }

    protected double calcularRecargo(int dias) {
        return cilindraje > 250 ? 75 : 0;
    }

    public boolean permiteLicencias(Set<Licencia> licencias) {
        return autoriza(licencias, Licencia.M);
    }

    public int umbralMantenimiento() { return 20; }
    public String categoria() { return "Motocicleta"; }

    public String caracteristicas() {
        return cilindraje + " cc, requiere M";
    }
}