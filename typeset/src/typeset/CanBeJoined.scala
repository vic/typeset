package typeset

import scala.annotation.implicitNotFound

@implicitNotFound("Cannot join ${A} with ${B}. Possibly due to duplicated types.")
private[typeset] sealed trait CanBeJoined[A <: TypeSet, B <: TypeSet, O <: TypeSet]
private[typeset] object CanBeJoined {
  private def instance[A <: TypeSet, B <: TypeSet, O <: TypeSet] =
    new CanBeJoined[A, B, O] {}

  implicit def joinWithLeftSingleton[A, T <: TypeSet](
      implicit ev1: CanBeHeaded[A, T]
  ): CanBeJoined[A :+: TNil, T, A :+: T] = instance

  implicit def joinWithLeftSet[A, B <: TypeSet, T <: TypeSet, N <: TypeSet](
      implicit
      ev0: CanBeJoined[B, T, N],
      ev1: CanBeHeaded[A, N]
  ): CanBeJoined[A :+: B, T, A :+: N] = instance

  trait ForNil {
    def :+:[L <: :+:[_, _]](left: L): L = left
  }

  trait ForCons[H, T <: TypeSet] { self: H :+: T =>

    def :+:[L <: :+:[_, _], M, N <: TypeSet](left: L)(
        implicit ev: CanBeJoined[L, H :+: T, M :+: N]
    ): M :+: N = new :+:(left.seq ++ self.seq)

  }

}
