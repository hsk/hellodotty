object main extends App {
  println("Hello dotty")

  // Union/Intersection Types

  trait A { type T = Int }
  trait B { type T = Double }

  /*
  trait Union {
    type T
    // Scala2の交差型
    //type sc2union1 = (A with B) # T //=> Int
    //type sc2union2 = (B with A) # T //=> Double

    // Dottyの交差型
    type dottyUnion = (A & B) # T //=> Int & Double
    // (A & B) と (B & A) は等価
    // Scala2と比べて順序がなくなった
  }
  */
  // Dottyの合併型
  class C(x: Int)
  class D(x: Int)
  def foo(x: C | D): Int = /*x.x error now*/ 0
  foo(new C(0)) //=> ok
  foo(new D(0)) //=> ok

  val cond = true
  val bar = if (cond) 1 else "string"
  // Scala2 => bar: Any
  // Dotty  => bar: Int | String
  println("bar="+bar)
  // このような合併型はScala2では実現できなかった

  // Improved Type Inference

  def foo[A](a: A, f: A => A): A = f(a)

  // Scala2ではこのように型情報を明示するか
  foo(1, (x: Int) => x * 2)

  // カリー化する必要があった
  def bar[A](a: A)(f: A => A): A = f(a)
  bar(1)(x => x * 2)

  // 一方Dottyはどちらも不要, シンプル！
  foo(1, x => x * 2)

  // Literal-based singleton types

  object Literals {
    val fortyTwo: 42 = 42
    val `2`: 2 = 2
    val fortyFour: 44 = fortyTwo + `2`
    val text: "text" = "text"

    def id[T](a: T) = a
    val two: 2 = id(`2`)
  }

  /*
  forAll { x: Ranged[Int, 1, 100] =>
     val n: Int = x.value // guaranteed to be 1 to 100
  }
  */

  // Trait parameters
  object TraitParameters {

    trait A(x: String) {
      println(x)
    }

    class B extends A("Hello")
    new B //=> Hello
    // Dottyではコンパイルエラー

    //class B extends { val x: String = "Hello" } with A
    //new B //=> Hello
  }

  // @static methods and fields in Scala objects

  object StaticMethod {
    /*
    @static val x = 5
    @static def bar(y: Int): Int = x + y
    */
  }

  // Improved lazy vals initialization
  object ImprovedLazy {
    object A {
      @volatile
      lazy val a0 = B.b
      lazy val a1 = 17
    }
    object B {
      @volatile
      lazy val b = A.a1
    }
  }

  // Option-less pattern matching (name-based pattern matcher)
  object OptionLess {

    final class OptInt(val x: Int) extends AnyVal {
      def get: Int = x
      def isEmpty = x == Int.MinValue // or whatever is appropriate
      // This boxes TWICE: Int => Integer => Some(Integer)
      /*def unapply(x: Int): Option[Int] = x match {
        case null => None
        case x => Some(x)
      }*/
      // This boxes NONCE
      def unapply(x: Int): OptInt = new OptInt(x)
    }
  }

  // Repeated by-name parameters
  object Repeated {
    def foo(xs: => Int*): Option[Int] = xs.headOption
    // Scala2ではコンパイルエラー
  }

  // Multiversal equality
  object MultiversalEquality {
    // 1 == "1" // error
    // 1 != "1" // error
  }

  // Function arity adaption
  object FunctionArity {
    val pairs = Seq((1, 2), (3, 4))

    // Scala2ではこうする必要があったが
    pairs.map {
      case (x, y) => x + y
    }

    // Dottyでは簡潔に書けるようになります
    pairs.map((x, y) => x + y)
  }

  // Named type parameters
  object NamedTypeParameters {
    trait Map[type Key, type Value]
    type IntMap = Map[Key = Int]
  }

  // Contravariant implicit
  object ContravariantImplicit {
    trait A[-T]
    case class B[T]() extends A[T]
    class X
    class Y extends X
    //implicit val x = B[X]
    //implicit val y = B[Y]
    //implicitly[A[X]]
    //implicitly[A[Y]] // Scala2ではこれはコンパイルエラー
  }

  // def foo() { ??? } // error
  def foo() : Unit = { ??? }
  def foo2() = { }

  trait Foo
  //val x: Array[T] forSome { type T <: Foo } = Array()
  //↓
  val x: Array[_ <: Foo] = Array()

  // Long parameters
  def f(
    a0:Int,a1:Int,a2:Int,a3:Int,a4:Int,a5:Int,a6:Int,a7:Int,a8:Int,a9:Int,
    a10:Int,a11:Int,a12:Int,a13:Int,a14:Int,a15:Int,a16:Int,a17:Int,a18:Int,a19:Int,
    a20:Int,a21:Int,a22:Int,a23:Int,a24:Int,a25:Int,a26:Int,a27:Int,a28:Int,a29:Int,
    a30:Int,a31:Int,a32:Int,a33:Int,a34:Int,a35:Int,a36:Int,a37:Int,a38:Int,a39:Int,
    a40:Int,a41:Int,a42:Int,a43:Int,a44:Int,a45:Int,a46:Int,a47:Int,a48:Int,a49:Int,
    a50:Int,a51:Int,a52:Int,a53:Int,a54:Int,a55:Int,a56:Int,a57:Int,a58:Int,a59:Int,
    a60:Int,a61:Int,a62:Int,a63:Int,a64:Int,a65:Int,a66:Int,a67:Int,a68:Int,a69:Int
  ) = println("ok")
  f(0,1,2,3,4,5,6,7,8,9,
    10,11,12,13,14,15,16,17,18,19,
    20,21,22,23,24,25,26,27,28,29,
    30,31,32,33,34,35,36,37,38,39,
    40,41,42,43,44,45,46,47,48,49,
    50,51,52,53,54,55,56,57,58,59,
    60,61,62,63,64,65,66,67,68,69)

}
