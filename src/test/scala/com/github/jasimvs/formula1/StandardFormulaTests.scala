package com.github.jasimvs.formula1

import org.scalatest.FlatSpec

/**
 * Created by jsulaiman on 6/11/2016.
 */
class StandardFormulaTests  extends FlatSpec {

  "calculateSpeedAfterTime " should " return right values" in {
    assert(StandardFormulas.calculateSpeedAfterTime(10, 5, 2) == 20)
    assert(StandardFormulas.calculateSpeedAfterTime(20, 5, 2) == 30)
    assert(StandardFormulas.calculateSpeedAfterTime(10, 10, 2) == 30)
    assert(StandardFormulas.calculateSpeedAfterTime(10, 5, 5) == 35)
  }

  "calculateDistanceTravelled " should " return right values" in {
    assert(StandardFormulas.calculateDistanceTravelled(10, 5, 2) == 30)
    assert(StandardFormulas.calculateDistanceTravelled(20, 5, 2) == 50)
    assert(StandardFormulas.calculateDistanceTravelled(10, 10, 2) == 40)
    assert(StandardFormulas.calculateDistanceTravelled(10, 10, 5) == 175)
  }

  "calculateTimeToReachSpeed" should " return right values" in {
    assert(StandardFormulas.calculateTimeToReachSpeed(10, 10, 5) == 0)
    assert(StandardFormulas.calculateTimeToReachSpeed(10, 20, 5) == 2)
    assert(StandardFormulas.calculateTimeToReachSpeed(10, 20, 10) == 1)
    assert(StandardFormulas.calculateTimeToReachSpeed(10, 50, 5) == 8)
    assert(StandardFormulas.calculateTimeToReachSpeed(10, 50, 10) == 4)
  }
}
