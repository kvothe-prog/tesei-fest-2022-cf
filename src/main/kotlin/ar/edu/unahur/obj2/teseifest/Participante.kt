package ar.edu.unahur.obj2.teseifest

import ar.edu.unahur.obj2.caralibro.Escenario
import ar.edu.unahur.obj2.caralibro.Festival
import ar.edu.unahur.obj2.caralibro.HoraNombreEscenario
import java.sql.Time
import java.time.LocalDateTime

class Participante(val edad: Int, val instagram: String, val celular: String, val artista1: String, val artista2: String, val artista3: String){
  val artistasFavoritos = mutableListOf<String>(artista1, artista2, artista3)
  val escenariosVisitados = mutableListOf<Escenario>()

  var dineroGastadoFoodTrucks = 0

  var vipDisponibles: Int = 0

  val registroAlcohol = mutableListOf<ControladorAlcohol>()

  var consiguioAccesoFANPorFoodTruck = false
  var consiguioAccesoREFANPorFoodTruck = false
  var consiguioAccesoSUPERFANPorFoodTruck = false

  var escenariosVipALosQueYaIngreso = mutableListOf<Escenario>()

  fun ingresarAlFestival(){
    Organizacion.registroIngresos.add(Ingreso(this, LocalDateTime.now()))
  }

  fun mostrarProximoEscenarioArtistaInteresALas_(hora: LocalDateTime): Escenario { // esta funcion es lo mas aberrante que hice en mi vida

    var todosLosConciertosConEscenario = mutableListOf<HoraNombreEscenario>()

    Festival.escenarios.forEach { escenario -> escenario.lineUp.forEach { entry -> todosLosConciertosConEscenario.add(
      HoraNombreEscenario(entry.key, entry.value, escenario)
    ) } } // Lista HoraNombreEscenario con todos los conciertos

    todosLosConciertosConEscenario.filter { e -> artistasFavoritos.contains(e.nombre) } // Lista HoraNombreEscenario solo de artistas favoritos

    var proximoConcierto = todosLosConciertosConEscenario.find { e -> e.hora.isAfter(LocalDateTime.now()) }

    if( proximoConcierto == null){
      throw Exception("Tus artistas favoritos ya no tienen mas conciertos")
    }
    todosLosConciertosConEscenario.forEach { e ->
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

    dineroGastadoFoodTrucks += producto.precio

    producto.foodTruck.registrarVenta(Venta(this, LocalDateTime.now(), producto.precio))

    registroAlcohol.add(ControladorAlcohol(LocalDateTime.now(),producto))

    when{
      dineroGastadoFoodTrucks >= 1000 && !consiguioAccesoFANPorFoodTruck -> vipDisponibles ++
      dineroGastadoFoodTrucks >= 2000 && !consiguioAccesoREFANPorFoodTruck -> vipDisponibles ++
      dineroGastadoFoodTrucks >= 3000 && !consiguioAccesoSUPERFANPorFoodTruck -> vipDisponibles ++
    }
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
  }


  fun compraEntradaFAN(){
    vipDisponibles ++
  }
  fun compraEntradaREFAN(){
    vipDisponibles =+ 2
  }
  fun compraEntradaSUPERFAN(){
    vipDisponibles =+ 3
  }

}

class ControladorAlcohol(val horario: LocalDateTime, val bebidaAlcoholica: Consumible){

}
}