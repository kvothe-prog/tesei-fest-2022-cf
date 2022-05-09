package ar.edu.unahur.obj2.teseifest

import java.time.LocalDateTime

object Organizacion {
    val registroIngresos = mutableListOf<Ingreso>()

}

class Ingreso(val participante: Participante, val horaIngreso: LocalDateTime){

}