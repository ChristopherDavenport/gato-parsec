package io.chrisdavenport.gatoparsec

import cats.Foldable

object implicits {
  implicit class ParserOps[Input, A](private val p: Parser[Input, A]){
    def ~>[B](p2: => Parser[Input, B]): Parser[Input, B] = Combinator.discardLeft(p, p2)
    def <~[B](p2: Parser[Input, B]): Parser[Input, A] = Combinator.discardRight(p, p2)
    def ~[B](p2: => Parser[Input, B]): Parser[Input, (A, B)] = Combinator.andThen(p, p2)
    def |[B >: A](p2: Parser[Input, B]): Parser[Input, B] = Combinator.orElse(p, p2)
    def ||[B](p2: Parser[Input, B]): Parser[Input, Either[A, B]] = Combinator.either(p, p2)

    def collect[B](f: PartialFunction[A, B]): Parser[Input, B] = Combinator.collect(p, f)
    def filter(f: A => Boolean): Parser[Input, A] = Combinator.filter(p)(f)
    def named(s: String): Parser[Input, A] = Combinator.named(p, s)

    def parseOnly[F[_]: Foldable](i: F[Input]) = Parser.parseOnly(p, i)
    def parse[F[_]: Foldable](i: F[Input]) = Parser.parse(p, i)
  }
}