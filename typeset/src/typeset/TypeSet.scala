package typeset

sealed trait TypeSet

object TNil extends TypeSet
    with CanBeHeaded.ForNil
    with CanBeJoined.ForNil

final class :+:[H, T <: TypeSet] private[typeset] (private[typeset] val seq: Seq[Any]) extends TypeSet
    with CanBeHeaded.ForCons[H, T]
    with CanBeDropped.ForCons[H, T]
    with CanBeObtained.ForCons[H, T]
    with CanBeJoined.ForCons[H, T]
    with CanBeOrdered.ForCons[H, T]
