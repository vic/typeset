// -*- mode: scala -*-

import mill._
import mill.api.Loose
import mill.scalalib._
import mill.scalalib.publish._

import scala.util.Properties

object meta {

  val crossVersions = Seq("2.13.8")

  implicit val wd: os.Path = os.pwd

  def nonEmpty(s: String): Option[String] = s.trim match {
    case v if v.isEmpty => None
    case v              => Some(v)
  }

  val MILL_VERSION   = Properties.propOrNull("MILL_VERSION")
  val versionFromEnv = Properties.propOrNone("PUBLISH_VERSION")
  val gitSha         = nonEmpty(os.proc("git", "rev-parse", "--short", "HEAD").call().out.trim)
  val gitTag = nonEmpty(
    os.proc("git", "tag", "-l", "-n0", "--points-at", "HEAD").call().out.trim
  )
  val publishVersion =
    (versionFromEnv orElse gitTag orElse gitSha).getOrElse("latest")
}

object typeset extends Cross[TypeSet](meta.crossVersions: _*)
class TypeSet(val crossScalaVersion: String)
    extends CrossScalaModule
    with PublishModule { self =>
  def publishVersion = meta.publishVersion

  override def artifactName = "typeset"

  def pomSettings = PomSettings(
    description = "Compile-time Type-level Set",
    organization = "com.github.vic",
    url = "https://github.com/vic/typeset",
    licenses = Seq(License.`Apache-2.0`),
    versionControl = VersionControl.github("vic", "typeset"),
    developers = Seq(
      Developer("vic", "Victor Borja", "https://github.com/vic")
    )
  )

  object test extends Tests {
    override def ivyDeps =
      Agg(ivy"com.lihaoyi::utest::0.7.4") ++ self.compileIvyDeps()
    override def testFramework = "utest.runner.Framework"
  }
}
