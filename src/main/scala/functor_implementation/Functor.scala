package functor_implementation

//functor[F[_]] so we can also use it with the functor aid
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}