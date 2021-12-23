package typeset

import scala.annotation.implicitNotFound
import scala.languageFeature.implicitConversions

@implicitNotFound("Could not find a way to order ${R} into ${L}")
private[typeset] final case class CanBeOrdered[L <: TypeSet, R <: TypeSet, RR <: TypeSet](indexes: Seq[Int])
private[typeset] object CanBeOrdered {

  implicit def sameNilHeads[L0, L1, R0, R1, RR <: TypeSet](
      implicit
      ev0: L0 =:= R0,
      ev1: L1 =:= R1,
      l0: CanBeObtained[L0, RR, _],
      l1: CanBeObtained[L1, RR, _]
  ): CanBeOrdered[L0 :+: L1 :+: TNil, R0 :+: R1 :+: TNil, RR] =
    CanBeOrdered(Seq(l0.index, l1.index))

  implicit def swapNilHeads[L0, L1, R0, R1, RR <: TypeSet](
      implicit
      ev0: L0 =:= R1,
      ev1: L1 =:= R0,
      l0: CanBeObtained[L0, RR, _],
      l1: CanBeObtained[L1, RR, _]
  ): CanBeOrdered[L0 :+: L1 :+: TNil, R0 :+: R1 :+: TNil, RR] =
    CanBeOrdered(Seq(l0.index, l1.index))

  implicit def sortFromLeft[L0, L1, L2 <: TypeSet, R0, R1, R2 <: TypeSet, Z0 <: TypeSet, RR <: TypeSet](
      implicit
      l0: CanBeObtained[L0, RR, _],
      ev0: CanBeObtained[L0, R0 :+: R1 :+: R2, Z0],
      ev1: CanBeOrdered[L1 :+: L2, Z0, RR]
  ): CanBeOrdered[L0 :+: L1 :+: L2, R0 :+: R1 :+: R2, RR] = {
    CanBeOrdered(l0.index +: ev1.indexes)
  }

  implicit def ordered[L0, L1, L2 <: TypeSet, R0, R1, R2 <: TypeSet](b: R0 :+: R1 :+: R2)(
      implicit ev: CanBeOrdered[L0 :+: L1 :+: L2, R0 :+: R1 :+: R2, R0 :+: R1 :+: R2]
  ): L0 :+: L1 :+: L2 = {
    val seq: Seq[Any] = ev.indexes.map(b.seq.apply)
    new :+:[L0, L1 :+: L2](seq)
  }

  trait ForCons[H, T <: TypeSet] { self: H :+: T => }

}
