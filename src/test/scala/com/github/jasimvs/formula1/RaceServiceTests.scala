package com.github.jasimvs.formula1

import main.scala.com.github.jasimvs.formula1.{DefaultRaceService}
import org.scalatest.FlatSpec

/**
 * Created by jsulaiman on 6/11/2016.
 */
class RaceServiceTests extends FlatSpec {

  val raceService = new DefaultRaceService

  "distanceTravelled " should " return right tuples" in {
    assert(raceService.calculateDistanceTravelled(10, 5, 2, 10) == (20, 10))
    assert(raceService.calculateDistanceTravelled(10, 5, 2, 5) == (10, 5))
    assert(raceService.calculateDistanceTravelled(10, 5, 2, 20) == (30, 20))
    assert(raceService.calculateDistanceTravelled(10, 10, 2, 20) == (35, 20))
    assert(raceService.calculateDistanceTravelled(10, 10, 4, 20) == (75, 20))
    assert(raceService.calculateDistanceTravelled(5, 10, 2, 20) == (28.75, 20))
  }




}
