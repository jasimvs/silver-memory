package test.scala.com.github.jasimvs.formula1

import main.scala.com.github.jasimvs.formula1.{Team, RaceConfig, Car}
import org.scalatest.{FlatSpec}

/**
 * Created by jsulaiman on 6/11/2016.
 */
class DomainTests extends FlatSpec {

  "Creating Car " should "initialize all values " in {
    def topSpeed(i: Int): Float = (150 + (10 * i)) * 5 / 18
    assert (Car(1) == new Car(topSpeed(1), 2 * 1, 0.8, 2))
    assert (Car(5) == new Car(topSpeed(5), 2 * 5, 0.8, 2))
    assert (Car(10) == new Car(topSpeed(10), 2 * 10, 0.8, 2))
  }

  "Creating RaceConfig" should " initialize all values " in {
    def teams: Seq[Team] = 1 to 5 map (RaceConfig.initializeTeam(_)) sortWith (_.trackPositionInMetres > _.trackPositionInMetres)
    assert(RaceConfig(5, 5) == new RaceConfig(5, teams))
  }

  "RaceConfig " should " throw exception if any of the params is 0" in {
    intercept[IllegalArgumentException] {
      RaceConfig(0, 5)
    }
    intercept[IllegalArgumentException] {
      RaceConfig(5, 0)
    }
  }

  "calculateStartingPosition " should "return correct position" in {
    assert(RaceConfig.calculateStartingPosition(1) == 0)
    assert(RaceConfig.calculateStartingPosition(2) == -200)
    assert(RaceConfig.calculateStartingPosition(3) == -600)
    assert(RaceConfig.calculateStartingPosition(4) == -1200)
    assert(RaceConfig.calculateStartingPosition(5) == -2000)
  }

  "calculateStartingPositionRecursive " should "return correct position" in {
    assert(RaceConfig.calculateStartingPositionRecursive(1) == 0)
    assert(RaceConfig.calculateStartingPositionRecursive(2) == -200)
    assert(RaceConfig.calculateStartingPositionRecursive(3) == -600)
    assert(RaceConfig.calculateStartingPositionRecursive(4) == -1200)
    assert(RaceConfig.calculateStartingPositionRecursive(5) == -2000)
  }



}
