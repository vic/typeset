package typeset

import scala.annotation.{implicitAmbiguous, implicitNotFound}

@implicitNotFound("Type ${H} already exists in ${T}")
private[typeset] sealed trait CanBeHeaded[H, T <: TypeSet]
private[typeset] object CanBeHeaded {
  private def instance[H, T <: TypeSet] = new CanBeHeaded[H, T] {}

  @implicitAmbiguous("Cannot use a TSet as head of another TSet")
  implicit def setCannotBeHeadOfSet[H <: TypeSet, T <: TypeSet]: CanBeHeaded[H, T] = instance
  implicit def setCannotBeHeadOfSet0[H <: TypeSet, T <: TypeSet]: CanBeHeaded[H, T] = instance

  implicit def anythingCanBeHeadOfNil[H]: CanBeHeaded[H, TNil] =
    instance

  @implicitAmbiguous("Cannot use ${H} as head for ${H} :+: ${T}")
  implicit def cannotDuplicateHead[H, T <: TypeSet]: CanBeHeaded[H, H :+: T] = instance
  implicit def cannotDuplicateHead0[H, T <: TypeSet]: CanBeHeaded[H, H :+: T] = instance

  implicit def canHeadCons[E, H, T <: TypeSet](implicit ev: CanBeHeaded[E, T]): CanBeHeaded[E, H :+: T] = instance

  trait ForNil { self: TNil =>
    def :+:[H](e: H)(implicit ev: CanBeHeaded[H, TNil]): H :+: TNil =
      new :+:(Seq(e))
  }

  trait ForCons[H, T <: TypeSet] { self: H :+: T =>
    def :+:[E](e: E)(implicit ev: CanBeHeaded[E, H :+: T]): E :+: H :+: T =
      new :+:(e +: self.seq)
  }
}
