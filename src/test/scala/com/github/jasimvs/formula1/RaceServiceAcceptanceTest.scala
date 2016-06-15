package com.github.jasimvs.formula1

import main.scala.com.github.jasimvs.formula1._
import org.scalatest.{Matchers, FlatSpec}

/**
 * Created by jsulaiman on 6/15/2016.
 */
class RaceServiceAcceptanceTest extends FlatSpec with Matchers {

  val raceService: RaceService = new DefaultRaceService

  "RaceService " should " race and return the winners" in {
    val raceConfig: RaceConfig = RaceConfig(4, 4)
    val leaderboard = raceService.race(raceConfig)

    leaderboard shouldBe new RaceConfig(4.0f,Vector(
      Team(2,new Car(47.0f,4,0.8,2),4035.875,           47.0,96.0,  1),
      Team(3,new Car(50.0f,6,0.8,2),4083.3333333333335, 50.0,98.0,  1),
      Team(4,new Car(52.0f,8,0.8,2),4088.24,            52.0,104.0, 0),
      Team(1,new Car(44.0f,2,0.8,2),4059.0400000000004, 44.0,104.0, 0)
    ))
  }

}
