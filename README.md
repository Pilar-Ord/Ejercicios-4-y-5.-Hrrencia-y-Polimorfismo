# Ejercicios-4-y-5.-Hrrencia-y-Polimorfismo - RentaMovil
**Nombre Completo:** María del Pilar Ordóñez Helton
**Carné:** 26360

## Descripción
RentaMovil es un programa de consola en Java que permite registrar vehículos y clientes, cotizar y confirmar alquileres, gestionar devoluciones y mantenimiento, y consultar reportes. Usa herencia y polimorfismo para que cada tipo de vehículo aplique sus propias reglas de cobro, licencia y mantenimiento, y cada tipo de cliente determine su descuento y límite de alquileres activos. Cotizar no modifica los datos; el cobro y el cambio de estado ocurren únicamente cuando se confirma el alquiler.

## Cómo ejecutar
```bash
javac -d bin src/*.java
java -cp bin Main
```