package ar.edu.unahur.obj2.caralibro

import java.time.LocalDateTime
import kotlin.math.ceil

interface Escenario {
    val lineUp: Map<LocalDateTime,String>
    fun ponerHorario(anio: Int,mes: Int,dia: Int,hora: Int,minutos: Int): LocalDateTime{
        return LocalDateTime.of(anio,mes,dia,hora,minutos,0)
    }
    fun soloNombres(): List<String> {
        return lineUp.map { entry -> entry.value }
    }
    fun soloHorarios(): List<LocalDateTime> {
        return lineUp.map { entry -> entry.key }
    }
}

object Rock: Escenario {
    override val lineUp: Map<LocalDateTime, String> = mapOf<LocalDateTime,String>(ponerHorario(2022,5,30,15,0) to "Sumados", ponerHorario(2022,5,30,20,30) to "Joya Nunca Taxi")
}

object Trap: Escenario {
    override val lineUp: Map<LocalDateTime, String> = mapOf<LocalDateTime, String>(ponerHorario(2022,5,30,17,30) to "Trueno", ponerHorario(2022,5,31,1,0) to "Wos")
}

object Pop: Escenario {
    override val lineUp: Map<LocalDateTime, String> = mapOf<LocalDateTime, String>(ponerHorario(2022,5,30,22,0) to "Abel Pintos", ponerHorario(2022,5,31,2,0) to "Tini")
}

object Festival {
    val escenarios = listOf(Rock, Pop, Trap)

    fun lineUp(): MutableList<String>{
        val listaNombres = mutableListOf<String>()
        escenarios.forEach { escenario -> listaNombres.addAll(escenario.soloNombres()) }
        return listaNombres
    }

    fun todosLosHorariosDeConciertos(): MutableList<LocalDateTime>{
        val listaHorarios = mutableListOf<LocalDateTime>()
        escenarios.forEach { escenario -> listaHorarios.addAll(escenario.soloHorarios()) }
        return listaHorarios
    }

}

class HoraNombreEscenario(val hora: LocalDateTime, val nombre: String, val escenario: Escenario ){
}