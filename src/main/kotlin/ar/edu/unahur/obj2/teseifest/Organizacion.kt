package ar.edu.unahur.obj2.teseifest

import ar.edu.unahur.obj2.caralibro.*
import java.time.LocalDateTime

object Organizacion {
    val registroIngresos = mutableListOf<Ingreso>()
    var escenarios = mutableListOf<Escenario>(Rock, Trap, Pop)
    val foodTrucks = mutableListOf<FoodTruck>()

    fun agregarFoodTruck(foodTruck: FoodTruck){
        foodTrucks.add(foodTruck)
    }

    fun cantidadAccesosVendidos(): Int{
        return registroIngresos.count { e -> e.participante.dineroGastadoAcceso > 0 }
    }

    fun cantidadTotalVendida(): Int{
        return registroIngresos.sumBy { e -> e.participante.dineroGastadoFoodTrucks + e.participante.dineroGastadoAcceso }
    }

    fun promedioGastadoPorParticipante(): Int{
        return cantidadTotalVendida() / registroIngresos.size
    }

    fun escenarioVipMasVisitado(): Escenario?{
        return escenarios.maxByOrNull { e -> e.registroVip.size }
    }

    fun promedioCCAlcohol(): Int{
        var soloLosQueTomaronAlcohol = registroIngresos.filter { e -> e.participante.registroAlcohol.size > 0 }
        var cantidadAlcohol = soloLosQueTomaronAlcohol.sumBy { e -> e.participante.registroAlcohol.sumBy { e -> e.bebidaAlcoholica.cantidadAlcohol().toInt() } }
        return cantidadAlcohol / registroIngresos.size
    }

    fun porcentajeTomaron80cc(): Int {
        var cantidadQueTomaronMas80 = registroIngresos.count() { e -> e.participante.registroAlcohol.sumBy { e -> e.bebidaAlcoholica.cantidadAlcohol().toInt() } > 80}
        return (cantidadQueTomaronMas80 * 100) / registroIngresos.size
    }

    fun porcentajeNoTomaron(): Int {
        var cantidadNoTomaron = registroIngresos.count() { e -> e.participante.registroAlcohol.size == 0}
        return (cantidadNoTomaron * 100) / registroIngresos.size
    }

    fun porcentajeVeganos(): Int{
        var cantidadVeganos = registroIngresos.count() { e -> e.participante.soloComioVegano }
        return (cantidadVeganos * 100) / registroIngresos.size
    }

    fun bebidaMasConsumida(): Consumible? {
        if (foodTrucks.sumBy { e -> e.ventas.size } == 0){
            throw Exception("No se realizaron ventas aún")
        }
        var listaVentas = mutableListOf<Venta>()
        foodTrucks.forEach { e -> listaVentas.addAll(e.ventas) }
        listaVentas.filter { e -> e.producto.esBebida()}
        listaVentas.map { e -> e.producto }

        return listaVentas.groupingBy { it }.eachCount().maxByOrNull { e -> e.value }?.key?.producto

    }

    fun bebidaMasConsumidaAUnaHoraDeterminada(hora: LocalDateTime): Consumible? {
        if (foodTrucks.sumBy { e -> e.ventas.size } == 0){
            throw Exception("No se realizaron ventas aún")
        }
        var listaVentas = mutableListOf<Venta>()
        foodTrucks.forEach { e -> listaVentas.addAll(e.ventas) }
        listaVentas.filter { e -> e.producto.esBebida()}
        listaVentas.filter { e -> e.horario.isAfter(hora) && e.horario.isBefore(hora.plusHours(1)) }
        listaVentas.map { e -> e.producto }

        return listaVentas.groupingBy { it }.eachCount().maxByOrNull { e -> e.value }?.key?.producto

    }

    fun dosPersonasTienenMismaOnda(persona1: Participante, persona2: Participante): Boolean{
        var listaTodosIngresosVip = mutableListOf<RegistroIngresoVip>()
        escenarios.forEach { e -> listaTodosIngresosVip.addAll(e.registroVip) }
        listaTodosIngresosVip.filter { e -> e.participante == persona1 || e.participante == persona2 }

        return listaTodosIngresosVip.groupingBy { it.hora }.eachCount().filter { e -> e.value > 1 }.isNotEmpty()
    }
}

class Ingreso(val participante: Participante, val horaIngreso: LocalDateTime){

}