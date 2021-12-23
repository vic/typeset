# typeset [![Main workflow](https://github.com/vic/typeset/actions/workflows/workflow.yml/badge.svg)](https://github.com/vic/typeset/actions)

An Scala Type indexed set checked at compile time.

a TypeSet is similar to ZIO's `Has[_]` type but without any reflection need.

### Adding to your project.

Jars are available for all versions and commits at Jitpack.io.

[![](https://jitpack.io/v/vic/typeset.svg)](https://jitpack.io/#vic/typeset)


### Usage

Creating a TypeSet is always done by prepending something to `TNil`, which 
represent the empty `TypeSet`.

```scala
import typeset.*

val x = "Hello" :+: TNil
```

Of course, being a Set, you can't duplicate types of values on it.

```scala
val bad = "Hello" :+: "World" :+: TNil
```

The type of the set can be re-arrenged on the left side and any combination
containing the same types should be valid.

```scala
val a: String :+: Boolean :+: Int :+: TNil = "Hello" :+: true :+: 42 :+: TNil
val b: Int :+: String :+: Boolean :+: TNil = a // types are the same.
```

TypeSets can be joined together if they contained types do not cause duplicates.

```scala
val a = 1 :+: TNil
val b = true :+: TNil
val c = a :+: b // Has type Int :+: Boolean :+: TNil
```

Once created, you can obtain the value of a type from a Set.

```scala
val a = true :+: "Hello" :+: TNil
val x: String = a.get[String] // Returns "Hello"
```

Removing a type returns it's current value and another collapsed set without the
type present.

```scala
val a = true :+: "Hello" :+: TNil
val (x: String, b) = a.drop[String]() // type of b is Boolean :+: TNil 
```

That's pretty much it. For more examples you can see the [test code](typeset/test/src).


### Contributing

All contributions are welcome, just please be kind and repectful of other
people's time.

For Building, you will need [mill](https://com-lihaoyi.github.io/mill/mill/Intro_to_Mill.html) installed.

```sh
mill __.test # This will run all tests
```
