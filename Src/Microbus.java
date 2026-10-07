import java.util.*;

public class Microbus extends Vehiculo {
    private final int pasajeros;
    private final boolean conPiloto;

    public Microbus(String placa, String marca, String modelo,
                    double tarifa, int diasPrevios,
                    int pasajeros, boolean conPiloto) {
        super(placa, marca, modelo, tarifa, diasPrevios);
        if (pasajeros <= 0) {
            throw new IllegalArgumentException(
                "Pasajeros deben ser positivos"
            );
        }
        this.pasajeros = pasajeros;
        this.conPiloto = conPiloto;
    }

    protected double calcularRecargo(int dias) {
        return conPiloto ? 250 * dias : 0;
    }

    public boolean permiteLicencias(Set<Licencia> licencias) {
        return conPiloto || autoriza(licencias, Licencia.B);
    }

    public int umbralMantenimiento() { return 25; }
    public String categoria() { return "Microbus"; }

    public String caracteristicas() {
        return pasajeros + " pasajeros, "
            + (conPiloto
                ? "con piloto; sin licencia requerida"
                : "sin piloto; requiere B/A");
    }
}