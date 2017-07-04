/*
 * Copyright 2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package freestyle

import cats.Id
import cats.data.Kleisli
import freestyle.logging.LoggingM

import scala.concurrent.Future

object algebras {

  @free
  trait NonLogging {
    def x: FS[Int]
  }

  implicit def nonLoggingFutureHandler: NonLogging.Handler[Future] =
    new NonLogging.Handler[Future] {
      def x: Future[Int] = Future.successful(1)
    }

  type TestAlgebra[A] = Kleisli[Id, String, A]

  implicit def nonLoggingTestAlgebraHandler: NonLogging.Handler[TestAlgebra] =
    new NonLogging.Handler[TestAlgebra] {
      def x: TestAlgebra[Int] = Kleisli.pure(1)
    }

  @module
  trait App {
    val nonLogging: NonLogging
    val loggingM: LoggingM
  }

  val app = App[App.Op]
}
