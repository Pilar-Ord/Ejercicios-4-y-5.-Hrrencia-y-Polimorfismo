import java.util.*;

public class Automovil extends Vehiculo {
    private final int pasajeros;
    private final boolean automatico;

    public Automovil(String placa, String marca, String modelo,
                     double tarifa, int diasPrevios,
                     int pasajeros, boolean automatico) {
        super(placa, marca, modelo, tarifa, diasPrevios);
        if (pasajeros <= 0) {
            throw new IllegalArgumentException(
                "Pasajeros deben ser positivos"
            );
        }
        this.pasajeros = pasajeros;
        this.automatico = automatico;
    }

    protected double calcularRecargo(int dias) {
        return automatico ? 50 * dias : 0;
    }

    public boolean permiteLicencias(Set<Licencia> licencias) {
        return autoriza(licencias, Licencia.C);
    }

    public int umbralMantenimiento() { return 30; }
    public String categoria() { return "Automovil"; }

    public String caracteristicas() {
        return pasajeros + " pasajeros, "
            + (automatico ? "automatico" : "manual")
            + ", requiere C/B/A";
    }
}