import java.util.*;

public class Cotizacion {
    private final Vehiculo vehiculo;
    private final Cliente cliente;
    private final int dias;
    private final double subtotal, descuento;
    private final List<String> impedimentos;

    public Cotizacion(Vehiculo vehiculo, Cliente cliente,
                      int dias, double subtotal, double descuento,
                      List<String> impedimentos) {
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.dias = dias;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.impedimentos = List.copyOf(impedimentos);
    }

    public boolean esValida() {
        return impedimentos.isEmpty();
    }

    public String detalle() {
        return String.format(
            Locale.US,
            "%s%nCliente: %s | %d dias%n"
                + "Subtotal Q%.2f | Descuento Q%.2f | Total Q%.2f%n%s",
            vehiculo.descripcion(),
            cliente.getId(),
            dias,
            subtotal,
            descuento,
            subtotal - descuento,
            esValida()
                ? "Puede alquilarse"
                : "No puede alquilarse: "
                    + String.join("; ", impedimentos)
        );
    }

    public Alquiler confirmar(int numero) {
        if (!esValida()) {
            throw new IllegalStateException(
                String.join("; ", impedimentos)
            );
        }

        return new Alquiler(
            numero, cliente, vehiculo,
            dias, subtotal, descuento
        );
    }
}