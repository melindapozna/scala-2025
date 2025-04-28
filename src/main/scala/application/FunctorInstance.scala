package application

//Functor instance for Lists
given listFunctor: Functor[List] with
  def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)


//Functor instance for Options
given optionFunctor: Functor[Option] with
  def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)


  
//Functor instance for Either
given eitherFunctor[E]: Functor[[X] =>> Either[E, X]] with
  def map[A, B](fa: Either[E, A])(f: A => B): Either[E, B] = fa.map(f)
