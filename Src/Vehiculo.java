import java.util.*;

public abstract class Vehiculo {
    private final String placa, marca, modelo;
    private final double tarifaDiaria;
    private Estado estado = Estado.DISPONIBLE;
    private int diasDesdeMantenimiento;

    protected Vehiculo(String placa, String marca, String modelo,
                       double tarifaDiaria, int diasPrevios) {
        if (placa == null || placa.isBlank()
                || marca == null || marca.isBlank()
                || modelo == null || modelo.isBlank()
                || !Double.isFinite(tarifaDiaria)
                || tarifaDiaria <= 0 || diasPrevios < 0) {
            throw new IllegalArgumentException("Datos invalidos del vehiculo");
        }

        this.placa = placa.trim().toUpperCase(Locale.ROOT);
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.tarifaDiaria = tarifaDiaria;
        this.diasDesdeMantenimiento = diasPrevios;
    }

    public String getPlaca() { return placa; }
    public Estado getEstado() { return estado; }
    public int getDiasDesdeMantenimiento() {
        return diasDesdeMantenimiento;
    }

    public double calcularSubtotal(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los dias deben ser positivos");
        }
        return tarifaDiaria * dias + calcularRecargo(dias);
    }

    protected abstract double calcularRecargo(int dias);
    public abstract boolean permiteLicencias(Set<Licencia> licencias);
    public abstract int umbralMantenimiento();
    public abstract String categoria();
    public abstract String caracteristicas();

    public String descripcion() {
        return String.format(
            Locale.US,
            "%s %s %s (%s), tarifa Q%.2f, %s, dias desde mantenimiento %d; %s",
            placa, marca, modelo, categoria(), tarifaDiaria,
            estado, diasDesdeMantenimiento, caracteristicas()
        );
    }

    public void alquilar() {
        if (estado != Estado.DISPONIBLE) {
            throw new IllegalStateException("Vehiculo no disponible");
        }
        estado = Estado.ALQUILADO;
    }

    public void devolver(int dias) {
        if (estado != Estado.ALQUILADO || dias <= 0) {
            throw new IllegalStateException("Devolucion invalida");
        }

        diasDesdeMantenimiento += dias;
        estado = diasDesdeMantenimiento >= umbralMantenimiento()
            ? Estado.MANTENIMIENTO : Estado.DISPONIBLE;
    }

    public void finalizarMantenimiento() {
        if (estado != Estado.MANTENIMIENTO) {
            throw new IllegalStateException(
                "Vehiculo no esta en mantenimiento"
            );
        }
        diasDesdeMantenimiento = 0;
        estado = Estado.DISPONIBLE;
    }

    protected static boolean autoriza(
            Set<Licencia> licencias, Licencia requisito) {
        if (licencias.contains(requisito)) return true;

        if (requisito == Licencia.C) {
            return licencias.contains(Licencia.B)
                || licencias.contains(Licencia.A);
        }
        if (requisito == Licencia.B) {
            return licencias.contains(Licencia.A);
        }
        return false;
    }
}