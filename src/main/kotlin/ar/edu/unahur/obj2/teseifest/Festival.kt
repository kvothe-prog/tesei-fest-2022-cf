package ar.edu.unahur.obj2.caralibro

import ar.edu.unahur.obj2.teseifest.Participante
import java.time.LocalDateTime
import kotlin.math.ceil

interface Escenario {
    val lineUp: Map<LocalDateTime,String>

    val registroVip: MutableList<RegistroIngresoVip>

    fun ponerHorario(anio: Int,mes: Int,dia: Int,hora: Int,minutos: Int): LocalDateTime{
        return LocalDateTime.of(anio,mes,dia,hora,minutos,0)
    }
}

object Rock: Escenario {
    override val lineUp: Map<LocalDateTime, String> = mapOf<LocalDateTime,String>(ponerHorario(2022,5,30,15,0) to "Sumados", ponerHorario(2022,5,30,20,30) to "Joya Nunca Taxi")
    override val registroVip = mutableListOf<RegistroIngresoVip>()
}

object Trap: Escenario {
    override val lineUp: Map<LocalDateTime, String> = mapOf<LocalDateTime, String>(ponerHorario(2022,5,30,17,30) to "Trueno", ponerHorario(2022,5,31,1,0) to "Wos")
    override val registroVip = mutableListOf<RegistroIngresoVip>()
}

object Pop: Escenario {
    override val lineUp: Map<LocalDateTime, String> = mapOf<LocalDateTime, String>(ponerHorario(2022,5,30,22,0) to "Abel Pintos", ponerHorario(2022,5,31,2,0) to "Tini")
    override val registroVip = mutableListOf<RegistroIngresoVip>()
}

object Festival {
    val escenarios = listOf(Rock, Pop, Trap)

}

class HoraNombreEscenario(val hora: LocalDateTime, val nombre: String, val escenario: Escenario ){

}

class RegistroIngresoVip(val hora: LocalDateTime, val participante: Participante, val escenario: Escenario){
}