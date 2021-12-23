package typeset

import scala.annotation.implicitNotFound

trait Remaining[O] {
  type LeftOver <: TypeSet
}

@implicitNotFound("Cannot obtain type ${X} from ${T}")
private[typeset] final case class CanBeObtained[X, T <: TypeSet, O <: TypeSet] private[CanBeObtained] (index: Int)
    extends Remaining[O] {
  private[typeset] def get(seq: Seq[Any]): X = seq(index).asInstanceOf[X]
  override type LeftOver = O
}

private[typeset] object CanBeObtained {
  implicit def canObtainedFromHead[H, T <: TypeSet]: CanBeObtained[H, H :+: T, T] =
    CanBeObtained(0)

  implicit def canObtainedFromSet[X, H, I, T <: TypeSet, O <: TypeSet](
      implicit ev0: CanBeObtained[X, I :+: T, O]
  ): CanBeObtained[X, H :+: I :+: T, H :+: O] = ev0.copy(ev0.index + 1)

  trait ForCons[H, T <: TypeSet] { self: H :+: T =>
    def get[X](implicit ev: CanBeObtained[X, H :+: T, _]): X = ev.get(self.seq)
  }
}
