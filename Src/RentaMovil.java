import java.util.*;

public class RentaMovil {
    private final List<Vehiculo> flota = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Alquiler> alquileres = new ArrayList<>();
    private final Map<String, Double> ingresosPorCategoria =
        new LinkedHashMap<>();

    private double ingresos, descuentos;
    private int siguienteNumero = 1;

    public RentaMovil() {
        cargarDatosIniciales();
    }

    public List<Vehiculo> getFlota() {
        return Collections.unmodifiableList(flota);
    }

    public List<Cliente> getClientes() {
        return Collections.unmodifiableList(clientes);
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null
                || flota.stream().anyMatch(v ->
                    v.getPlaca().equalsIgnoreCase(
                        vehiculo.getPlaca()))) {
            throw new IllegalArgumentException(
                "Placa duplicada o vehiculo nulo"
            );
        }
        flota.add(vehiculo);
    }

    public void registrarCliente(Cliente cliente) {
        if (cliente == null
                || clientes.stream().anyMatch(c ->
                    c.getId().equalsIgnoreCase(cliente.getId()))) {
            throw new IllegalArgumentException(
                "Identificador duplicado o cliente nulo"
            );
        }
        clientes.add(cliente);
    }

    public Vehiculo buscarVehiculo(String placa) {
        return flota.stream()
            .filter(v -> v.getPlaca().equalsIgnoreCase(placa.trim()))
            .findFirst()
            .orElseThrow(() ->
                new IllegalArgumentException("Placa inexistente"));
    }

    public Cliente buscarCliente(String id) {
        return clientes.stream()
            .filter(c -> c.getId().equalsIgnoreCase(id.trim()))
            .findFirst()
            .orElseThrow(() ->
                new IllegalArgumentException("Cliente inexistente"));
    }

    public Cotizacion cotizar(String placa, String id, int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException(
                "Dias deben ser positivos"
            );
        }

        Vehiculo vehiculo = buscarVehiculo(placa);
        Cliente cliente = buscarCliente(id);
        List<String> problemas = new ArrayList<>();

        if (vehiculo.getEstado() != Estado.DISPONIBLE) {
            problemas.add("vehiculo " + vehiculo.getEstado());
        }

        if (!vehiculo.permiteLicencias(cliente.getLicencias())) {
            problemas.add("licencia inadecuada");
        }

        long activos = alquileres.stream()
            .filter(a -> a.isActivo()
                && a.getCliente() == cliente)
            .count();

        if (activos >= cliente.limiteActivos()) {
            problemas.add(
                "limite de alquileres activos alcanzado"
            );
        }

        double subtotal = vehiculo.calcularSubtotal(dias);
        double descuento =
            subtotal * cliente.porcentajeDescuento();

        return new Cotizacion(
            vehiculo, cliente, dias,
            subtotal, descuento, problemas
        );
    }

    public Alquiler confirmar(String placa, String id, int dias) {
        Cotizacion cotizacion = cotizar(placa, id, dias);

        if (!cotizacion.esValida()) {
            throw new IllegalStateException(
                cotizacion.detalle()
            );
        }

        Alquiler alquiler =
            cotizacion.confirmar(siguienteNumero);

        alquiler.getVehiculo().alquilar();
        alquileres.add(alquiler);
        siguienteNumero++;
        alquiler.getCliente().registrarConfirmacion();

        ingresos += alquiler.getTotal();
        descuentos += alquiler.getDescuento();

        ingresosPorCategoria.merge(
            alquiler.getVehiculo().categoria(),
            alquiler.getTotal(),
            Double::sum
        );

        return alquiler;
    }

    public Alquiler devolver(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);

        Alquiler alquiler = alquileres.stream()
            .filter(a -> a.isActivo()
                && a.getVehiculo() == vehiculo)
            .findFirst()
            .orElseThrow(() ->
                new IllegalStateException(
                    "El vehiculo no esta alquilado"));

        vehiculo.devolver(alquiler.getDias());
        alquiler.finalizar();

        return alquiler;
    }

    public void finalizarMantenimiento(String placa) {
        buscarVehiculo(placa).finalizarMantenimiento();
    }

    public String reporte() {
        StringBuilder resultado =
            new StringBuilder("FLOTA POR CATEGORIA Y ESTADO\n");
        Map<String, int[]> conteos = new LinkedHashMap<>();

        for (Vehiculo vehiculo : flota) {
            int[] cantidades = conteos.computeIfAbsent(
                vehiculo.categoria(), categoria -> new int[4]
            );
            cantidades[0]++;
            cantidades[vehiculo.getEstado().ordinal() + 1]++;
        }

        conteos.forEach((categoria, cantidades) ->
            resultado.append(String.format(
                "%s: total %d, disponibles %d, "
                    + "alquilados %d, mantenimiento %d%n",
                categoria,
                cantidades[0],
                cantidades[1],
                cantidades[2],
                cantidades[3]
            ))
        );

        resultado.append(String.format(
            Locale.US,
            "Ingresos de esta ejecucion: Q%.2f | "
                + "Descuentos: Q%.2f%n",
            ingresos, descuentos
        ));

        for (String categoria : conteos.keySet()) {
            resultado.append(String.format(
                Locale.US,
                "%s: Q%.2f%n",
                categoria,
                ingresosPorCategoria.getOrDefault(
                    categoria, 0.0)
            ));
        }

        resultado.append("ALQUILERES ACTIVOS\n");
        alquileres.stream()
            .filter(Alquiler::isActivo)
            .forEach(a ->
                resultado.append(a).append('\n'));

        return resultado.toString();
    }

    public String historial(String id) {
        Cliente cliente = buscarCliente(id);
        StringBuilder resultado =
            new StringBuilder(cliente.descripcion())
                .append('\n');

        double total = 0;
        for (Alquiler alquiler : alquileres) {
            if (alquiler.getCliente() == cliente) {
                resultado.append(alquiler).append('\n');
                total += alquiler.getTotal();
            }
        }

        resultado.append(String.format(
            Locale.US,
            "Total pagado (incluye historial previo): Q%.2f",
            total
        ));
        return resultado.toString();
    }

    private void cargarDatosIniciales() {
        registrarVehiculo(new Automovil(
            "A001", "Toyota", "Corolla", 200, 0, 5, true));
        registrarVehiculo(new Automovil(
            "A002", "Honda", "Civic", 180, 28, 5, false));

        registrarVehiculo(new Motocicleta(
            "M001", "Honda", "CB", 100, 0, 250));
        registrarVehiculo(new Motocicleta(
            "M002", "Yamaha", "MT", 120, 18, 300));

        registrarVehiculo(new CamionetaCarga(
            "C001", "Ford", "Transit", 200, 0, 1.5));
        registrarVehiculo(new CamionetaCarga(
            "C002", "Isuzu", "NQR", 250, 14, 2.0));

        registrarVehiculo(new Microbus(
            "B001", "Toyota", "Hiace", 450, 0, 15, true));
        registrarVehiculo(new Microbus(
            "B002", "Hyundai", "H1", 350, 24, 12, false));

        Individual previo = new Individual(
            "1234567890123", "Ana Lopez",
            EnumSet.of(Licencia.A, Licencia.M)
        );
        registrarCliente(previo);

        registrarCliente(new Individual(
            "9876543210987", "Luis Perez",
            EnumSet.of(Licencia.C)
        ));

        registrarCliente(new Corporativo(
            "NIT-100", "Logistica Uno", "Elena",
            EnumSet.of(Licencia.B, Licencia.M)
        ));

        registrarCliente(new Corporativo(
            "NIT-200", "Viajes Dos", "Rosa",
            EnumSet.of(Licencia.C)
        ));

        // Tres alquileres finalizados de un periodo anterior.
        // Aparecen en el historial, pero no en los ingresos actuales.
        for (int i = 0; i < 3; i++) {
            Alquiler anterior = new Alquiler(
                siguienteNumero++, previo, flota.get(2),
                1, 100, 0
            );
            anterior.finalizar();
            alquileres.add(anterior);
            previo.registrarConfirmacion();
        }
    }
}