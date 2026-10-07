import java.util.*;

public abstract class Cliente {
    private final String id, nombre;
    private final Set<Licencia> licencias;
    private int confirmados;

    protected Cliente(String id, String nombre,
                      Set<Licencia> licencias) {
        if (id == null || id.isBlank()
                || nombre == null || nombre.isBlank()
                || licencias == null || licencias.isEmpty()) {
            throw new IllegalArgumentException(
                "Cliente requiere ID, nombre y al menos una licencia"
            );
        }

        this.id = id.trim().toUpperCase(Locale.ROOT);
        this.nombre = nombre.trim();
        this.licencias =
            Collections.unmodifiableSet(EnumSet.copyOf(licencias));
    }

    public String getId() { return id; }
    public Set<Licencia> getLicencias() { return licencias; }
    public int getConfirmados() { return confirmados; }

    public void registrarConfirmacion() {
        confirmados++;
    }

    public abstract double porcentajeDescuento();
    public abstract int limiteActivos();
    public abstract String descripcion();

    protected String datosComunes() {
        return id + " - " + nombre
            + ", licencias " + licencias
            + ", confirmados " + confirmados;
    }
}