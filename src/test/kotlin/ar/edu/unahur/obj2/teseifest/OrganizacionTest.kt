package ar.edu.unahur.obj2.teseifest

import ar.edu.unahur.obj2.caralibro.Pop
import ar.edu.unahur.obj2.caralibro.Rock
import ar.edu.unahur.obj2.caralibro.Trap
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeBetween
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class OrganizacionTest : DescribeSpec({
    describe("Test de estadísticas") {
        val participante1 = Participante(17, "insta1", "1155555555", "Sumados", "Trueno", "Tini" )
        val participante2 = Participante(18, "insta2", "1155555555", "Trueno", "Wos", "Abel Pintos")
        val participante3 = Participante(20, "insta3", "1555555555", "Joya Nunca Taxi", "Sumados", "Wos")
        val participante4 = Participante(20, "insta4", "1555555555", "Joya Nunca Taxi", "Sumados", "Trueno")

        val foodtruck1 = FoodTruck()
        val foodtruck2 = FoodTruck()

        val bondiolaVegana = Comida(1000, "BONDIOLA VEGANA", foodtruck1, true )
        val cerveza = Bebida( 500, "CERVEZA", 400, 5.0, foodtruck1, true)
        val bebidaFuerte = Bebida(500, "BEBIDA FUERTE", 200, 40.0, foodtruck2, true)
        val milkshake = Bebida(500, "MILKSHAKE", 500, 0.0, foodtruck2, false)
        val bondiola = Comida(1000, "BONDIOLA", foodtruck2, false)

        Organizacion.foodTrucks.clear()
        Organizacion.registroIngresos.clear()
        Rock.registroVip.clear()
        Pop.registroVip.clear()
        Trap.registroVip.clear()

        Organizacion.agregarFoodTruck(foodtruck1)
        Organizacion.agregarFoodTruck(foodtruck2)

        participante1.ingresarAlFestival()
        participante2.ingresarAlFestival()
        participante3.ingresarAlFestival()

        participante1.compraEntradaFAN()
        participante2.compraEntradaREFAN()

        it("Cuantos accesos vendió"){
            Organizacion.cantidadAccesosVendidos().shouldBe(2)
        }

        it("Cuanto se vendió en total, es decir plata de accesos y plata de foodtrucks"){
            participante1.comprarAlgo(bondiola)
            participante3.comprarAlgo(bebidaFuerte)
            participante2.comprarAlgo(cerveza)
            participante2.comprarAlgo(cerveza)

            // 1000 + 500 + 500 + 500 de los foodtrucks y 1000 + 2000 de los accesos. Total = $ 5500

            Organizacion.cantidadTotalVendida().shouldBe(5500)

        }

        it("Cual es el promedio de plata que gastó un participante"){
            participante1.comprarAlgo(bondiola)
            participante3.comprarAlgo(bebidaFuerte)
            participante2.comprarAlgo(cerveza)
            participante2.comprarAlgo(cerveza)

            // 1000 + 500 + 500 + 500 de los foodtrucks y 1000 + 2000 de los accesos. Total = $ 5500
            // 5500 / 3 = 1833,33

            Organizacion.promedioGastadoPorParticipante().shouldBe(1833)

            participante1.comprarAlgo(bondiola)
            //6500 / 3 = 2166,66

            Organizacion.promedioGastadoPorParticipante().shouldBe(2166)

        }

        it("Zona vip mas concurrida"){
            participante3.compraEntradaSUPERFAN() // participante 3 puede entrar a los 3 vips
            participante1.comprarAlgo(bondiola) // participante 1 ya tenia entrada FAN, compra bondiola de 1000 y ahora puede acceder a 2
                                                // participante 2 puede entrar a 2 por entrada REFAN
            participante1.entrarVip(Rock)
            participante2.entrarVip(Rock)
            participante3.entrarVip(Pop)
            participante3.entrarVip(Trap)

            Organizacion.escenarioVipMasVisitado().shouldBe(Rock)

            participante2.entrarVip(Pop)
            participante1.entrarVip(Pop)

            Organizacion.escenarioVipMasVisitado().shouldBe(Pop)

        }

        it("Promedio de cc de alcohol de los participantes que tomaron alcohol"){
            participante2.comprarAlgo(cerveza)
            participante2.comprarAlgo(cerveza)
            participante2.comprarAlgo(milkshake)
            participante3.comprarAlgo(bebidaFuerte)
            // participante1 es menor, no puede consumir alcohol
            // participante2 tomo 40 cc de alcohol por las 2 cervezas
            // participante3 tomo 80 cc de alcohol solo por la bebida fuerte
            // el promedio debería ser (40+80)/2= 60

            Organizacion.promedioCCAlcohol().shouldBe(60)

            participante2.comprarAlgo(cerveza)

            //ahora debería dar 140/2 = 70

            Organizacion.promedioCCAlcohol().shouldBe(70)
        }

        it("Porcentaje del total de participantes que llegaron a 80cc"){
            participante3.comprarAlgo(bebidaFuerte)

            // 1 de 3 llegao a 80 cc, debería dar 33,33

            Organizacion.porcentajeTomaron80cc().shouldBe(33)

            participante2.comprarAlgo(cerveza)
            participante2.comprarAlgo(cerveza)
            participante2.comprarAlgo(cerveza)
            participante2.comprarAlgo(cerveza)

            // 2 de 3 llegaron a 80 cc, debería dar 66,66

            Organizacion.porcentajeTomaron80cc().shouldBe(66)
        }

        it("Porcentaje de los participantes que no tomaron alcohol"){
            participante3.comprarAlgo(cerveza)

            // Debería dar 66,66
            Organizacion.porcentajeNoTomaron().shouldBe(66)

            participante2.comprarAlgo(cerveza)

            // Debería dar 33,33
            Organizacion.porcentajeNoTomaron().shouldBe(33)

        }

        it("Porcentaje veganos"){
            participante1.comprarAlgo(bondiola)

            Organizacion.porcentajeVeganos().shouldBe(66)

            participante2.comprarAlgo(bondiola)

            Organizacion.porcentajeVeganos().shouldBe(33)

            participante3.comprarAlgo(bondiola)

            Organizacion.porcentajeVeganos().shouldBe(0)
        }

        it("Bebida mas consumida"){
            shouldThrowAny { // todavía no se realizaron ventas
                Organizacion.bebidaMasConsumida()
            }
            participante1.comprarAlgo(milkshake)
            participante1.comprarAlgo(milkshake)
            participante1.comprarAlgo(milkshake)
            participante2.comprarAlgo(cerveza)
            participante3.comprarAlgo(cerveza)


            // Hasta aca lo mas vendido es el milkshake
            Organizacion.bebidaMasConsumida().shouldBe(milkshake)

            participante2.comprarAlgo(cerveza)
            participante3.comprarAlgo(cerveza)

            // Ahora se vendió mas cerveza
            Organizacion.bebidaMasConsumida().shouldBe(cerveza)

            participante2.comprarAlgo(milkshake)

            // Si hay dos que se vendieron lo mismo creo que arroja el primero por orden alfabético
            Organizacion.bebidaMasConsumida().shouldBe(cerveza)

        }

        it("Bebida mas vendida a una hora especifica"){
            // No se como testear esto
            participante1.comprarAlgo(bondiola)
            participante2.comprarAlgo(bondiolaVegana)
            participante3.comprarAlgo(bondiola)

            shouldThrowAny { // Todavía no se registró venta de bebidas
                Organizacion.bebidaMasConsumidaAUnaHoraDeterminada(LocalDateTime.now().minusMinutes(30))
            }

            participante2.comprarAlgo(cerveza)
            participante2.comprarAlgo(cerveza)
            participante3.comprarAlgo(bebidaFuerte)
            participante1.comprarAlgo(milkshake)

            // Nos devuelve cerveza como la mas vendida
            Organizacion.bebidaMasConsumidaAUnaHoraDeterminada(LocalDateTime.now().minusMinutes(30)).shouldBe(cerveza)

            shouldThrowAny { // Cuando corro el test se registran las ventas. Si pregunto por las ventas de hace 3 horas arroja error.
                Organizacion.bebidaMasConsumidaAUnaHoraDeterminada(LocalDateTime.now().minusHours(3))
            }
        }

        it("Dos participantes con la misma onda"){


            // participante 1 tiene como artistas favoritos a Sumados, Trueno y Tini
            // participante 2 tiene como artistas favoritos a Trueno, Wos y Abel Pintos
            // participante 3 tiene como artistas favoritos a JNT , Sumados Y Wos
            // participante 4 tiene como artistas favoritos a JNT, Sumados y Trueno

            // Debería ser cierto porque tienen 2 artistas favoritos en común
            Organizacion.dosPersonasTienenLaMismaOnda(participante3, participante4).shouldBe(true)

            // No debería ser cierto
            Organizacion.dosPersonasTienenLaMismaOnda(participante1, participante2).shouldBe(false)

            participante2.entrarVip(Rock)
            participante1.entrarVip(Rock)
            
            // Debería ser cierto porque ingresaron a alguna zona vip dentro de la misma hora
            Organizacion.dosPersonasTienenLaMismaOnda(participante1, participante2).shouldBe(true)
        }

        Organizacion.foodTrucks.clear()
        Organizacion.registroIngresos.clear()
    }

})
