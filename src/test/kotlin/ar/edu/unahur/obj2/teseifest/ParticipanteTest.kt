package ar.edu.unahur.obj2.caralibro

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
/*
class UsuarioTest : DescribeSpec({
  describe("teseifest") {
    val juanFan = Participante(/**/)
    val reneReFan = Participante(/**/)
    val lauraSuperFan = Participante(/**/)

    describe("Accesos") {
      describe("los distintos perfiles") {
        it("intenta acceder al vip de un escenario") {
          juanFan.puedeAcceder(Rock).shouldBeTrue()
        }
        it("intenta acceder al vip de dos escenario") {
          juanFan.puedeAcceder(Rock).shouldBeTrue()
        }
        it("intenta acceder al vip de todos los escenarios") {
          juanFan.puedeAcceder(Rock).shouldBeTrue()
        }
      }

    }
  }
})


 */
class Prueba : DescribeSpec({
  val fecha1 = LocalDateTime.of(2022,5,6,1,30,30)
  val fecha2 = LocalDateTime.of(2022,5,6,2,30,30)
  val fecha3 = LocalDateTime.of(2022,5,6,3,0,30)

  val lista = mutableListOf<LocalDateTime>(fecha1, fecha2, fecha3)
  describe("Fecha"){
    lista.minByOrNull { fecha -> fecha - LocalDateTime.now() }.shouldBe(fecha1)
  }
})