package ar.edu.unahur.obj2.teseifest

import ar.edu.unahur.obj2.caralibro.Escenario
import ar.edu.unahur.obj2.caralibro.Festival
import ar.edu.unahur.obj2.caralibro.HoraNombreEscenario
import ar.edu.unahur.obj2.caralibro.RegistroIngresoVip
import java.sql.Time
import java.time.LocalDateTime

class Participante(val edad: Int, val instagram: String, val celular: String, val artista1: String, val artista2: String, val artista3: String){
  val artistasFavoritos = mutableListOf<String>(artista1, artista2, artista3)
  var escenariosVipALosQueYaIngreso = mutableListOf<Escenario>()

  var dineroGastadoFoodTrucks = 0
  var dineroGastadoAcceso = 0

  var vipDisponibles: Int = 0

  val registroAlcohol = mutableListOf<ControladorAlcohol>()

  var soloComioVegano = true

  var consiguioAccesoFANPorFoodTruck = false
  var consiguioAccesoREFANPorFoodTruck = false
  var consiguioAccesoSUPERFANPorFoodTruck = false

  fun ingresarAlFestival(){
    Organizacion.registroIngresos.add(Ingreso(this, LocalDateTime.now()))
  }

  fun mostrarProximoEscenarioArtistaInteresALas_(hora: LocalDateTime): Escenario {

    var todosLosConciertosConEscenario = mutableListOf<HoraNombreEscenario>()

    Festival.escenarios.forEach { escenario -> escenario.lineUp.forEach { entry -> todosLosConciertosConEscenario.add(
      HoraNombreEscenario(entry.key, entry.value, escenario)
    ) } } // Lista HoraNombreEscenario con todos los conciertos

    var conciertosFiltrados = todosLosConciertosConEscenario.filter { e -> artistasFavoritos.contains(e.nombre) && e.hora.isAfter(hora) } // Lista HoraNombreEscenario solo de artistas favoritos

    var proximoConcierto = conciertosFiltrados.maxByOrNull { e -> e.hora }

    if( proximoConcierto == null){
      throw Exception("Tus artistas favoritos ya no tienen mas conciertos")
    }
    conciertosFiltrados.forEach { e ->
        if(e.hora.isAfter(LocalDateTime.now()) && e.hora.isBefore(proximoConcierto!!.hora)){
          proximoConcierto = e
        }
    }
    return proximoConcierto!!.escenario
  }

  fun esMayorDeEdad(): Boolean{
    return edad >= 18
  }

  fun bebidasAlcoholicasUltimasDosHoras(): List<ControladorAlcohol> {
    return registroAlcohol.filter{ registro -> registro.horario >= LocalDateTime.now().minusHours(2) }
  }

  fun cantidadAlcoholUltimasDosHoras(): Double{
    return bebidasAlcoholicasUltimasDosHoras().sumOf{ registro -> registro.bebidaAlcoholica.cantidadAlcohol() }
  }

  fun puedeComprarBebidaAlcoholica_(bebida: Consumible): Boolean{
    return cantidadAlcoholUltimasDosHoras() + bebida.cantidadAlcohol() <= 80 && this.esMayorDeEdad()
  }

  fun comprarAlgo(producto: Consumible){
    if(producto.cantidadAlcohol() > 0 && !puedeComprarBebidaAlcoholica_(producto)){
      throw Exception("Se pasa de copas")
    }
    if(!producto.esVegano){
      soloComioVegano = false
    }

    dineroGastadoFoodTrucks += producto.precio

    producto.foodTruck.registrarVenta(Venta(this, LocalDateTime.now(), producto))

    registroAlcohol.add(ControladorAlcohol(LocalDateTime.now(),producto))

    when{
      dineroGastadoFoodTrucks >= 1000 && !consiguioAccesoFANPorFoodTruck -> conseguirAccesoFANPorFoodTruck()
      dineroGastadoFoodTrucks >= 2000 && !consiguioAccesoREFANPorFoodTruck -> conseguirAccesoREFANPorFoodTruck()
      dineroGastadoFoodTrucks >= 3000 && !consiguioAccesoSUPERFANPorFoodTruck -> conseguirAccesoSUPERFANPorFoodTruck()
    }
  }

  fun conseguirAccesoFANPorFoodTruck(){
    vipDisponibles ++
    consiguioAccesoFANPorFoodTruck = true
  }

  fun conseguirAccesoREFANPorFoodTruck(){
    vipDisponibles ++
    consiguioAccesoREFANPorFoodTruck = true
  }
  fun conseguirAccesoSUPERFANPorFoodTruck(){
    vipDisponibles ++
    consiguioAccesoSUPERFANPorFoodTruck = true
  }

  fun puedeEntrarVip(escenario: Escenario): Boolean{
    return vipDisponibles > 0 || escenariosVipALosQueYaIngreso.contains(escenario)
  }

  fun entrarVip(escenario: Escenario){
    if(!puedeEntrarVip(escenario)){
      throw Exception("No tiene accesos disponibles")
    }
    if (!escenariosVipALosQueYaIngreso.contains(escenario)){
      vipDisponibles --
      escenariosVipALosQueYaIngreso.add(escenario)
      escenario.registroVip.add(RegistroIngresoVip(LocalDateTime.now(), this, escenario)) }
  }

  fun compraEntradaFAN(){
    if ((vipDisponibles + escenariosVipALosQueYaIngreso.size) >= Festival.escenarios.size){
      throw Exception(" Ya podes entrar a cuaquier escenario!")
    }
    vipDisponibles ++
    dineroGastadoAcceso += 1000
  }
  fun compraEntradaREFAN(){
    if ((vipDisponibles + escenariosVipALosQueYaIngreso.size) >= Festival.escenarios.size - 1){
      throw Exception(" Te conviene comprar la entrada FAN ")
    }
    vipDisponibles =+ 2
    dineroGastadoAcceso += 2000
  }

  fun compraEntradaSUPERFAN(){
    if ((vipDisponibles + escenariosVipALosQueYaIngreso.size) >= Festival.escenarios.size - 2){
      throw Exception(" Te conviene comprar la entrada REFAN o FAN")
    }
    vipDisponibles =+ 3
    dineroGastadoAcceso += 3000
  }

}

class ControladorAlcohol(val horario: LocalDateTime, val bebidaAlcoholica: Consumible){

}
