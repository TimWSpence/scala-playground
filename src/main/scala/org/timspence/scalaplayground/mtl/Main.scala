// package org.timwspence.scalaplayground.mtl

// import cats.implicits._
// import cats.Functor
// import cats.data.Chain

// import cats.effect.{ExitCode, IO, IOApp}
// import cats.effect.concurrent.Ref

// import cats.mtl.{ApplicativeAsk, FunctorListen, DefaultFunctorListen}

// object Main extends IOApp {

//   implicit val ask: ApplicativeAsk[IO, Config] = ApplicativeAsk.const(Config("tim"))

//   implicit val writer: FunctorListen[IO, Chain[String]] = {
//     val ref = Ref.unsafe[IO, Chain[String]](Chain.empty)
//     new DefaultFunctorListen[IO, Chain[String]] {

//       override val functor: Functor[IO] = Functor[IO]

//       override def tell(l: Chain[String]): IO[Unit] = ref.update(_ ++ l)

//       override def listen[A](fa: IO[A]): IO[(A, Chain[String])] = for {
//         current <- ref.get
//         _       <- ref.set(Chain.empty)
//         res     <- fa
//         diff    <- ref.get
//         _       <- ref.set(current ++ diff)
//       } yield (res, diff)


//     }
//   }

//   override def run(args: List[String]): IO[ExitCode] = for {
//     p <- FunctorListen[IO, Chain[String]].listen(for {
//     _    <- FunctorListen[IO, Chain[String]].tell(Chain.apply("one", "two", "three"))
//     conf <- ApplicativeAsk[IO, Config].ask
//     _    <- FunctorListen[IO, Chain[String]].tell(Chain.one(conf.toString()))
//   } yield ExitCode.Success)
//     _ <- IO(println(p._2))
//   } yield ExitCode.Success

// }

// case class Config(name: String)
