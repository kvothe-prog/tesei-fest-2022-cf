package ar.edu.unahur.obj2.caralibro

import ar.edu.unahur.obj2.teseifest.*
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class FestivalTest : DescribeSpec({
  describe("teseifest") {
      var participante1 = Participante(17, "insta1", "1155555555", "Sumados", "Trueno", "Tini" )
      var participante2 = Participante(18, "insta2", "1155555555", "Trueno", "Wos", "Abel Pintos")
      var participante3 = Participante(20, "insta3", "1555555555", "Joya Nunca Taxi", "Sumados", "Wos")

      var foodtruck1 = FoodTruck()
      var foodtruck2 = FoodTruck()

      var bondiolaVegana = Comida(1000, "BONDIOLA VEGANA", foodtruck1, true )
      var cerveza = Bebida( 500, "CERVEZA", 400, 5.0, foodtruck1, true)
      var bebidaFuerte = Bebida(500, "BEBIDA FUERTE", 200, 40.0, foodtruck2, true)
      var milkshake = Bebida(500, "MILKSHAKE", 500, 0.0, foodtruck2, false)
      var bondiola = Comida(1000, "BONDIOLA", foodtruck2, false)

      Organizacion.foodTrucks.clear()
      Organizacion.registroIngresos.clear()
      Rock.registroVip.clear()
      Pop.registroVip.clear()
      Trap.registroVip.clear()

      Organizacion.agregarFoodTruck(foodtruck1)
      Organizacion.agregarFoodTruck(foodtruck2)

      it("Acceder a escenario VIP por entrada"){
          shouldThrowAny {
              participante1.entrarVip(Rock)
          }
          participante2.compraEntradaFAN()
          participante2.entrarVip(Rock)
          Rock.registroVip.map { e -> e.participante }.contains(participante2)
          shouldThrowAny {
              participante2.entrarVip(Pop)
          }

          shouldThrowAny {
              participante2.compraEntradaSUPERFAN()
              // Va a tirar error porque ya accedió a 1 escenario y son 3 escenarios en total.
              // Debe avisarle que le conviene comprar la entrada REFAN o FAN
          }
      }
      it("Acceder a escenario VIP por foodtrucks"){
          participante2.vipDisponibles.shouldBe(0)
          participante2.comprarAlgo(bondiolaVegana)
          participante2.vipDisponibles.shouldBe(1)
          participante2.comprarAlgo(cerveza)
          participante2.vipDisponibles.shouldBe(1)
          participante2.entrarVip(Rock)
          shouldThrowAny {
              participante2.entrarVip(Trap)
          }
          participante2.entrarVip(Rock)
          participante2.comprarAlgo(cerveza)
          participante2.entrarVip(Pop)
          shouldThrowAny {
              participante2.entrarVip(Trap)
          }
      }

      it("Mostrar para una hora el proximo escenario donde tocará uno de los artistas favoritos "){
        // Participante 3 tiene como favoritos: Joya Nunca Taxi (toca el 30/05 a las 20:30 en escenario ROCK)
        //                                      Sumados (toca el 30/05 a las 15:00 en escenario ROCK)
        //                                      Wos (toca el 31/05 a las 01:00 en escenario TRAP)
        // por lo tanto para  la hora 16:00 del 30/05 debería devolver escenario ROCK
        // para la hora 23:30 del 30/05 debería devolver escenario TRAP
        // para la hora 22:30 del 31/05 el festival ya terminó y va a lanzar una excepción, ya que no van a tocar más sus artistas
        // para el participante2 a las 21:30 debería devolver POP, porque abel pintos está por tocar y le interesa.
          participante3.mostrarProximoEscenarioArtistaInteresALas_(LocalDateTime.of(2022,5, 30, 16, 0, 0)).shouldBe(Rock)
          participante3.mostrarProximoEscenarioArtistaInteresALas_(LocalDateTime.of(2022,5, 30, 22, 30, 0)).shouldBe(Trap)

          shouldThrowAny {
              participante3.mostrarProximoEscenarioArtistaInteresALas_(LocalDateTime.of(2022,5, 31, 22, 30, 0))
          }
          participante2.mostrarProximoEscenarioArtistaInteresALas_(LocalDateTime.of(2022,5, 30, 21, 30, 0)).shouldBe(Pop)

      }

      it("Comprar comidas y bebidas"){
          //participante 1 menor de edad, no debería poder comprar alcohol
          shouldThrowAny {
              participante1.comprarAlgo(cerveza)
          }
          // bebida fuerte tiene 80 cc de alcohol, debería esperar otras 2 horas para comprar otra bebida. No entiendo como mockear la hora actual para hacer que pasen 2 horas sinceramente.
          participante2.comprarAlgo(bebidaFuerte)
          shouldThrowAny {
              participante2.comprarAlgo(cerveza)
          }
          participante2.soloComioVegano.shouldBe(true)
          participante2.comprarAlgo(milkshake)
          participante2.soloComioVegano.shouldBe(false)

      }
      Organizacion.foodTrucks.clear()
      Organizacion.registroIngresos.clear()
  }
})



