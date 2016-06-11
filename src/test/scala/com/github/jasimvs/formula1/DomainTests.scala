package test.scala.com.github.jasimvs.formula1

import main.scala.com.github.jasimvs.formula1.{Team, RaceService, RaceConfig, Car}
import org.scalatest.{FlatSpec, WordSpec}

/**
 * Created by jsulaiman on 6/11/2016.
 */
class DomainTests extends FlatSpec {

  "Creating Car " should "initialize all values " in {
    def topSpeed(i: Int): Float = (150 + (10 * i)) * 5 / 18
    assert (Car(1) == new Car(topSpeed(1), 2 * 1, 0.8, 1))
    assert (Car(5) == new Car(topSpeed(5), 2 * 5, 0.8, 1))
    assert (Car(10) == new Car(topSpeed(10), 2 * 10, 0.8, 1))
  }

  "Creating RaceConfig" should " initialize all values " in {
    def teams: Seq[Team] = 1 to 5 map (RaceService.initializeTeam(_)) sortWith (_.trackPositionInMetres > _.trackPositionInMetres)
    assert(RaceConfig(5, 5, RaceService.initializeTeam) == new RaceConfig(5, teams))
  }

}
