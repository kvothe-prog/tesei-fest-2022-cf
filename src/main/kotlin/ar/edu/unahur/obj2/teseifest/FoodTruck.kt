package ar.edu.unahur.obj2.teseifest

import java.time.LocalDateTime

abstract class Consumible(val precio: Int, val descripcion: String, val foodTruck: FoodTruck, val esVegano: Boolean){
    open fun cantidadAlcohol(): Double{
        return 0.0
    }
}

class Comida(precio: Int, descripcion: String, foodTruck: FoodTruck, esVegano: Boolean): Consumible(precio, descripcion, foodTruck, esVegano){
}

class BebidaNoAlcoholica(precio: Int, descripcion: String, val volumen: Int, val porcentajeAlcohol: Double, foodTruck: FoodTruck, esVegano: Boolean): Consumible(precio, descripcion, foodTruck, esVegano){
}

class BebidaAlcoholica(precio: Int, descripcion: String, val volumen: Int, val porcentajeAlcohol: Double, foodTruck: FoodTruck, esVegano: Boolean): Consumible(precio, descripcion, foodTruck, esVegano){
    override fun cantidadAlcohol(): Double{
        return porcentajeAlcohol * volumen
    }
}

class FoodTruck (val ventas: MutableList<Venta>){
    fun registrarVenta(venta: Venta){
        ventas.add(venta)
    }
    fun totalVendido(): Int{
        return ventas.sumBy { venta -> venta.producto.precio }
    }

}

class Venta(val comprador: Participante, val horario: LocalDateTime, val producto: Consumible){

}