package io.kool.sample.resources

import com.sun.jersey.spi.resource.Singleton
import java.util.concurrent.atomic.AtomicInteger
import javax.ws.rs.*
import io.kool.sample.model.*

Path("/products")
Produces("application/json")
Singleton
public open class Products() {
    private val idGenerator = AtomicInteger(0)
    protected val collection: MutableList<Product> = arrayList(product(nextId, "Beer", 3.99), product(nextId, "Wine", 5.99))

    public val nextId: String
        get() = idGenerator.incrementAndGet().toString()

    GET
    Produces("application/json")
    public open fun results(): Results<Product> = Results<Product>(collection)

    GET
    Path("id/{id}")
    Produces("application/json")
    public open fun byId([PathParam("id")] id: String?): Product? {
        println("get id '$id'")
        val answer = collection.find { it.id == id }
        return answer
    }

    DELETE
    Path("id/{id}")
    Produces("application/json")
    public open fun remove(PathParam("id") id: String): Product? {
        println("remove id '$id'")
        val element = byId(id)
        if (element != null) {
            collection.remove(element)
        }
        return element
    }

    POST
    public open fun add(element: Product): Product {
        val id = element.id
        if (id != null) {
            remove(id)
        } else {
            element.id = nextId
        }
        println("add $element")
        collection.add(element)
        return element
    }

}
