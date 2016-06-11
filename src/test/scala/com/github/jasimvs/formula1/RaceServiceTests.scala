package com.github.jasimvs.formula1

import main.scala.com.github.jasimvs.formula1.RaceService
import org.scalatest.FlatSpec

/**
 * Created by jsulaiman on 6/11/2016.
 */
class RaceServiceTests extends FlatSpec {

  "distanceTravelled " should " return right tuples" in {
    assert(RaceService.distanceTravelled(10, 5, 2, 10) == (20, 10))
    assert(RaceService.distanceTravelled(10, 5, 2, 5) == (10, 5))
    assert(RaceService.distanceTravelled(10, 5, 2, 20) == (30, 20))
    assert(RaceService.distanceTravelled(10, 10, 2, 20) == (35, 20))
    assert(RaceService.distanceTravelled(10, 10, 4, 20) == (75, 20))
    assert(RaceService.distanceTravelled(5, 10, 2, 20) == (28.75, 20))
  }

  "calculateSpeedAfterTime " should " " in {
    assert(RaceService.calculateSpeedAfterTime(10, 5, 2) == 20)
    assert(RaceService.calculateSpeedAfterTime(20, 5, 2) == 30)
    assert(RaceService.calculateSpeedAfterTime(10, 10, 2) == 30)
    assert(RaceService.calculateSpeedAfterTime(10, 5, 5) == 35)
  }

  "distanceTravelled " should " return right values" in {
    assert(RaceService.distanceTravelled(10, 5, 2) == 30)
    assert(RaceService.distanceTravelled(20, 5, 2) == 50)
    assert(RaceService.distanceTravelled(10, 10, 2) == 40)
    assert(RaceService.distanceTravelled(10, 10, 5) == 175)
  }

  "timeToReachSpeed" should " return right values" in {
    assert(RaceService.timeToReachSpeed(10, 10, 5) == 0)
    assert(RaceService.timeToReachSpeed(10, 20, 5) == 2)
    assert(RaceService.timeToReachSpeed(10, 20, 10) == 1)
    assert(RaceService.timeToReachSpeed(10, 50, 5) == 8)
    assert(RaceService.timeToReachSpeed(10, 50, 10) == 4)
  }

}
