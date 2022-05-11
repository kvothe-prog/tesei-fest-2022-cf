package ar.edu.unahur.obj2.teseifest

import java.time.LocalDateTime

abstract class Consumible(val precio: Int, val descripcion: String, val foodTruck: FoodTruck, val esVegano: Boolean){
    open fun cantidadAlcohol(): Double{
        return 0.0
    }
    open fun esBebida(): Boolean{
        return false
    }

}

class Comida(precio: Int, descripcion: String, foodTruck: FoodTruck, esVegano: Boolean): Consumible(precio, descripcion, foodTruck, esVegano){
}

class Bebida(precio: Int, descripcion: String, val volumen: Int, val porcentajeAlcohol: Double, foodTruck: FoodTruck, esVegano: Boolean): Consumible(precio, descripcion, foodTruck, esVegano){
    override fun cantidadAlcohol(): Double{
        return porcentajeAlcohol * volumen
    }

    override fun esBebida(): Boolean {
        return true
    }
}

class FoodTruck (val ventas: MutableList<Venta>){
    fun registrarVenta(venta: Venta){
        ventas.add(venta)
    }
}

class Venta(val comprador: Participante, val horario: LocalDateTime, val producto: Consumible){

}