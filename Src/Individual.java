import java.util.*;

public class Individual extends Cliente {
    public Individual(String dpi, String nombre,
                      Set<Licencia> licencias) {
        super(dpi, nombre, licencias);
        if (!dpi.matches("[0-9]{13}")) {
            throw new IllegalArgumentException(
                "DPI debe tener 13 digitos"
            );
        }
    }

    public double porcentajeDescuento() {
        return getConfirmados() >= 3 ? 0.05 : 0;
    }

    public int limiteActivos() { return 1; }

    public String descripcion() {
        return "Individual: " + datosComunes();
    }
}