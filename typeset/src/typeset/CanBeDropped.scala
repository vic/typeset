package typeset

private[typeset] object CanBeDropped {

  implicit class DropFromHead[H](private val c: H :+: TNil) extends AnyVal {
    def drop[X]()(implicit ev: H =:= X): (X, TNil) =
      c.seq.head.asInstanceOf[X] -> TNil
  }

  implicit class DropFromCons[H, T <: TypeSet](private val c: H :+: T) extends AnyVal {
    def drop[X]: DropPartiallyApplied[X, H, T] = new DropPartiallyApplied[X, H, T](c.seq)
  }

  trait ForCons[H, T <: TypeSet] { self: H :+: T =>
    def remaining[X, R <: TypeSet](implicit ev: CanBeObtained[X, H :+: T, R]): Remaining[R] = ev
  }

  final class DropPartiallyApplied[X, H, T <: TypeSet](seq: Seq[Any]) {
    def apply[M, N <: TypeSet]()(implicit ev: CanBeObtained[X, H :+: T, M :+: N]): (X, M :+: N) = {
      val item = ev.get(seq)
      val rem  = seq.slice(0, ev.index + 1) ++ seq.drop(ev.index + 1)
      val set  = new :+:[M, N](rem)
      item -> set
    }
  }

}
