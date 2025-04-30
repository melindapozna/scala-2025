package functor_implementation

//FunctorAid is a helper that makes using the functor easier.
// can call fmap(myData)(myFunction) and scala applies the right Functor (List, Either, Option, etc...)
object FunctorAid {
    def fmap[F[_], A, B](fa: F[A])(f: A => B)(using functor: Functor[F]): F[B] = functor.map(fa)(f)
}