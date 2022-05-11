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
        return cantidadAlcohol / soloLosQueTomaronAlcohol.size
    }

    fun porcentajeTomaron80cc(): Int {
        var cantidadQueTomaronMas80 = registroIngresos.count() { e -> e.participante.registroAlcohol.sumBy { e -> e.bebidaAlcoholica.cantidadAlcohol().toInt() } >= 80}
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
        var listaVentaBebidas = listaVentas.filter { e -> e.producto.esBebida()}
        var listaBebidas = listaVentas.map { e -> e.producto }

        return listaBebidas.groupingBy { it }.eachCount().maxByOrNull { e -> e.value }?.key

    }

    fun bebidaMasConsumidaAUnaHoraDeterminada(hora: LocalDateTime): Consumible? {
        if (foodTrucks.sumBy { e -> e.ventas.size } == 0){
            throw Exception("No se realizaron ventas aún")
        }
        var listaVentas = mutableListOf<Venta>()
        foodTrucks.forEach { e -> listaVentas.addAll(e.ventas) }
        var listaVentaBebidas = listaVentas.filter { e -> e.producto.esBebida()}
        var listaVentaBebidasAEsaHora = listaVentaBebidas.filter { e -> e.horario.isAfter(hora) && e.horario.isBefore(hora.plusHours(1)) }
        var listaBebidasAEsaHora = listaVentaBebidasAEsaHora.map { e -> e.producto }

        if(listaBebidasAEsaHora.isEmpty()){
            throw Exception("No se compraron bebidas a esa hora")
        }

        return listaBebidasAEsaHora.groupingBy { it }.eachCount().maxByOrNull { e -> e.value }?.key

    }

    fun dosPersonasEntronAUnaZonaVipALaMismaHora(persona1: Participante, persona2: Participante): Boolean{
        var listaTodosIngresosVip = mutableListOf<RegistroIngresoVip>()
        escenarios.forEach { e -> listaTodosIngresosVip.addAll(e.registroVip) }
        var listaIngresosVipParticipantes = listaTodosIngresosVip.filter { e -> e.participante == persona1 || e.participante == persona2 }
        var listaDateTimeIngresosVip = listaIngresosVipParticipantes.map { e -> e.hora }
        var listaSoloHorasIngresosVip = listaDateTimeIngresosVip.map { e -> e.hour }

        return listaSoloHorasIngresosVip.groupingBy { it }.eachCount().filter { e -> e.value > 1 }.isNotEmpty()
    }

    fun dosPersonasTienenAlMenosDosArtistasFavoritosEnComun(persona1: Participante, persona2: Participante): Boolean{
        return persona1.artistasFavoritos.intersect(persona2.artistasFavoritos).size >= 2
    }

    fun dosPersonasTienenLaMismaOnda(persona1: Participante, persona2: Participante): Boolean{
        return dosPersonasEntronAUnaZonaVipALaMismaHora(persona1, persona2) || dosPersonasTienenAlMenosDosArtistasFavoritosEnComun(persona1, persona2)
    }

}

class Ingreso(val participante: Participante, val horaIngreso: LocalDateTime){

}