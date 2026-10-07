import java.util.*;

public class Alquiler {
    private final int numero, dias;
    private final Cliente cliente;
    private final Vehiculo vehiculo;
    private final double subtotal, descuento, total;
    private boolean activo;

    public Alquiler(int numero, Cliente cliente, Vehiculo vehiculo,
                    int dias, double subtotal, double descuento) {
        this.numero = numero;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.dias = dias;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = subtotal - descuento;
        this.activo = true;
    }

    public Cliente getCliente() { return cliente; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public int getDias() { return dias; }
    public double getDescuento() { return descuento; }
    public double getTotal() { return total; }
    public boolean isActivo() { return activo; }

    public void finalizar() {
        if (!activo) {
            throw new IllegalStateException("Alquiler finalizado");
        }
        activo = false;
    }

    public String toString() {
        return String.format(
            Locale.US,
            "#%d %s / %s / %d dias / subtotal Q%.2f / "
                + "descuento Q%.2f / total Q%.2f / %s",
            numero, cliente.getId(), vehiculo.getPlaca(),
            dias, subtotal, descuento, total,
            activo ? "ACTIVO" : "FINALIZADO"
        );
    }
}