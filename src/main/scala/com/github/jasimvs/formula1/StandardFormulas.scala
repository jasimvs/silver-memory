package com.github.jasimvs.formula1

/**
 * Created by jsulaiman on 6/11/2016.
 */
object StandardFormulas {
  def calculateSpeedAfterTime(u: Double, a: Int, t: Double): Double = u + (a * t)

  def calculateDistanceTravelled(u: Double, a: Int, t: Double): Double = (u * t) + (a * t * t / 2)

  def calculateTimeToReachSpeed(u: Double, v: Double, a: Int): Double = (v - u) / a
}
