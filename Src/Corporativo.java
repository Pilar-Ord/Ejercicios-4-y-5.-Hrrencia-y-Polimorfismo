import java.util.*;

public class Corporativo extends Cliente {
    private final String contacto;

    public Corporativo(String nit, String empresa,
                       String contacto, Set<Licencia> licencias) {
        super(nit, empresa, licencias);
        if (contacto == null || contacto.isBlank()) {
            throw new IllegalArgumentException(
                "Contacto obligatorio"
            );
        }
        this.contacto = contacto.trim();
    }

    public double porcentajeDescuento() { return 0.10; }
    public int limiteActivos() { return 3; }

    public String descripcion() {
        return "Corporativo: " + datosComunes()
            + ", contacto " + contacto;
    }
}