import java.util.*;

public class CamionetaCarga extends Vehiculo {
    private final double toneladas;

    public CamionetaCarga(String placa, String marca, String modelo,
                          double tarifa, int diasPrevios,
                          double toneladas) {
        super(placa, marca, modelo, tarifa, diasPrevios);
        if (!Double.isFinite(toneladas) || toneladas <= 0) {
            throw new IllegalArgumentException(
                "Capacidad debe ser positiva"
            );
        }
        this.toneladas = toneladas;
    }

    protected double calcularRecargo(int dias) {
        return 100 * toneladas * dias;
    }

    public boolean permiteLicencias(Set<Licencia> licencias) {
        return autoriza(licencias, Licencia.B);
    }

    public int umbralMantenimiento() { return 15; }
    public String categoria() { return "Camioneta de carga"; }

    public String caracteristicas() {
        return toneladas + " toneladas, requiere B/A";
    }
}