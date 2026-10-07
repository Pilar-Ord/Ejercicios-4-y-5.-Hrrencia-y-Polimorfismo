import java.util.*;

public class Vista {
    private final Scanner scanner = new Scanner(System.in);

    public void mostrar(String mensaje) {
        System.out.println(mensaje);
    }

    public String leer(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return scanner.nextLine().trim();
    }

    public int entero(String etiqueta) {
        return Integer.parseInt(leer(etiqueta));
    }

    public double decimal(String etiqueta) {
        return Double.parseDouble(
            leer(etiqueta).replace(',', '.')
        );
    }

    public boolean siNo(String etiqueta) {
        String respuesta = leer(etiqueta + " (s/n)");

        if (!respuesta.equalsIgnoreCase("s")
                && !respuesta.equalsIgnoreCase("n")) {
            throw new IllegalArgumentException(
                "Responda s o n"
            );
        }
        return respuesta.equalsIgnoreCase("s");
    }

    public Set<Licencia> licencias() {
        Set<Licencia> resultado =
            EnumSet.noneOf(Licencia.class);

        String entrada =
            leer("Licencias (A,B,C,M separadas por coma)");

        for (String parte : entrada.split(",")) {
            resultado.add(Licencia.valueOf(
                parte.trim().toUpperCase(Locale.ROOT)
            ));
        }
        return resultado;
    }

    public Vehiculo leerVehiculo() {
        int tipo = entero(
            "\nTipo de vehículo:"
            + "\n1. Automóvil"
            + "\n2. Motocicleta"
            + "\n3. Camioneta de carga"
            + "\n4. Microbús"
        );

        String placa = leer("Placa");
        String marca = leer("Marca");
        String modelo = leer("Modelo");
        double tarifa = decimal("Tarifa diaria");

        switch (tipo) {
            case 1:
                return new Automovil(
                    placa, marca, modelo, tarifa, 0,
                    entero("Pasajeros"),
                    siNo("Automatico")
                );
            case 2:
                return new Motocicleta(
                    placa, marca, modelo, tarifa, 0,
                    entero("Cilindraje")
                );
            case 3:
                return new CamionetaCarga(
                    placa, marca, modelo, tarifa, 0,
                    decimal("Capacidad maxima en toneladas")
                );
            case 4:
                return new Microbus(
                    placa, marca, modelo, tarifa, 0,
                    entero("Pasajeros"),
                    siNo("Incluye piloto")
                );
            default:
                throw new IllegalArgumentException(
                    "Tipo de vehiculo invalido"
                );
        }
    }

    public Cliente leerCliente() {
        int tipo = entero(
            "\nTipo de cliente:"
            + "\n1. Individual"
            + "\n2. Corporativo"
        );

        if (tipo == 1) {
            String dpi = leer("DPI");
            String nombre = leer("Nombre");
            return new Individual(dpi, nombre, licencias());
        }

        if (tipo == 2) {
            String nit = leer("NIT");
            String empresa = leer("Empresa");
            String contacto = leer("Contacto");

            return new Corporativo(
                nit, empresa, contacto, licencias()
            );
        }

        throw new IllegalArgumentException(
            "Tipo de cliente invalido"
        );
    }
}