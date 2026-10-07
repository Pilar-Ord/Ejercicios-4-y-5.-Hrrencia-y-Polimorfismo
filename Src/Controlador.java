import java.util.*;

public class Controlador {
    private final Vista vista;
    private final RentaMovil empresa;

    public Controlador(Vista vista, RentaMovil empresa) {
        this.vista = vista;
        this.empresa = empresa;
    }

    public void iniciar() {
        while (true) {
            try {
                vista.mostrar(
                    "\n1. Registrar vehículo"
                    + "\n2. Registrar cliente"
                    + "\n3. Consultar flota"
                    + "\n4. Consultar clientes"
                    + "\n5. Cotizar"
                    + "\n6. Alquilar"
                    + "\n7. Devolver vehículo"
                    + "\n8. Finalizar mantenimiento"
                    + "\n9. Ver reporte"
                    + "\n10. Ver historial"
                    + "\n0. Salir"
                );

                int opcion = vista.entero("Opcion");

                switch (opcion) {
                    case 0:
                        return;

                    case 1:
                        empresa.registrarVehiculo(
                            vista.leerVehiculo());
                        vista.mostrar("Vehiculo registrado");
                        break;

                    case 2:
                        empresa.registrarCliente(
                            vista.leerCliente());
                        vista.mostrar("Cliente registrado");
                        break;

                    case 3:
                        for (Vehiculo v : empresa.getFlota()) {
                            vista.mostrar(v.descripcion());
                        }
                        break;

                    case 4:
                        for (Cliente c : empresa.getClientes()) {
                            vista.mostrar(c.descripcion());
                        }
                        break;

                    case 5:
                        vista.mostrar(
                            pedirCotizacion().detalle());
                        break;

                    case 6:
                        alquilar();
                        break;

                    case 7:
                        vista.mostrar(
                            "Devuelto: " + empresa.devolver(
                                vista.leer("Placa")));
                        break;

                    case 8:
                        empresa.finalizarMantenimiento(
                            vista.leer("Placa"));
                        vista.mostrar("Mantenimiento terminado");
                        break;

                    case 9:
                        vista.mostrar(empresa.reporte());
                        break;

                    case 10:
                        vista.mostrar(empresa.historial(
                            vista.leer("ID del cliente")));
                        break;

                    default:
                        vista.mostrar("Opcion invalida");
                }
            } catch (IllegalArgumentException
                     | IllegalStateException e) {
                vista.mostrar("Error: " + e.getMessage());
            } catch (NoSuchElementException e) {
                return;
            }
        }
    }

    private Cotizacion pedirCotizacion() {
        return empresa.cotizar(
            vista.leer("Placa"),
            vista.leer("ID del cliente"),
            vista.entero("Dias")
        );
    }

    private void alquilar() {
        String placa = vista.leer("Placa");
        String id = vista.leer("ID del cliente");
        int dias = vista.entero("Dias");

        Cotizacion propuesta =
            empresa.cotizar(placa, id, dias);

        vista.mostrar(propuesta.detalle());

        if (propuesta.esValida()
                && vista.siNo("Confirmar alquiler")) {
            vista.mostrar(
                "Confirmado: "
                    + empresa.confirmar(placa, id, dias)
            );
        } else {
            vista.mostrar("Sin cambios");
        }
    }
}