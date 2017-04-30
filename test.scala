object test extends App {
  println("Hello dotty")

  // def foo() { ??? } // error
  def foo() : Unit = { ??? }
  def foo2() = { }

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

  trait Foo
  //val x: Array[T] forSome { type T <: Foo } = Array()
  //↓
  val x: Array[_ <: Foo] = Array()

  object Literals {
    val fortyTwo: 42 = 42
    val `2`: 2 = 2
    val fortyFour: 44 = fortyTwo + `2`
    val text: "text" = "text"

    def id[T](a: T) = a
    val two: 2 = id(`2`)
  }
}
