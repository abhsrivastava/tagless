package example

// future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

// cats
import cats.Monad
import cats.implicits._

// monix
import monix.eval.Task
import monix.cats._
import monix.cats.reverse._

object Application extends App {
  
  type ProductId = Long
  case class Product(id: ProductId, name: String)

  trait ProductRepository[M[_]] {
    def findProduct(productId: ProductId) : M[Option[Product]]
    def saveProduct(product: Product) : M[Unit]
    def incrementProductSales(productId: ProductId, quantity: Long) : M[Unit]
  }

  class ProductRepositoryWithFuture extends ProductRepository[Future] {
    def findProduct(productId: ProductId) : Future[Option[Product]] = {
      Future.successful(Some(Product(productId, "foo")))
    }
    def saveProduct(product: Product) : Future[Unit] = {
      Future.successful()
    }
    def incrementProductSales(productId: ProductId, quanity: Long) : Future[Unit] = {
      Future.successful()
    }
  }

  class ProductRepositoryWithTask extends ProductRepository[Task] {
    def findProduct(productId: ProductId) : Task[Option[Product]] = {
      Task.now(Some(Product(productId, "foo")))
    }
    def saveProduct(product: Product) : Task[Unit] = {
      Task.unit
    }
    def incrementProductSales(productId: ProductId, quantity: Long) : Task[Unit] = {
      Task.unit
    }
  }

  class Program[M[_]: Monad](repo: ProductRepository[M]) {
    def renameProduct(id: ProductId, name: String) : M[Option[Product]] = {
      repo.findProduct(id).flatMap{
        case Some(p) => 
          val newProduct = p.copy(name = name)
          repo.saveProduct(newProduct).map(_ => Some(p))
        case None => 
          Monad[M].pure(None)
      }
    }
  }

  // run
  val pr1 = new ProductRepositoryWithFuture()
  val pr2 = new ProductRepositoryWithTask()
  val p1 = new Program(pr1)
  val p2 = new Program(pr2)
  val f1 = p1.renameProduct(123, "foo")
  val t1 = p2.renameProduct(123, "foo")
}