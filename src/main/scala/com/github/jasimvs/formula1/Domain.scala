package main.scala.com.github.jasimvs.formula1

/**
 * Created by jsulaiman on 6/11/2016.
 */

case class Car(topSpeedInMetresPerSecond: Float, accelerationInMetresPerSecondSquare: Int, handlingFactor: Double = 0.8, nitroAvailability: Int = 1)
object Car {
  def apply(teamNumber: Int) = {
    val topSpeed: Float = (150 + (10 * teamNumber)) * 5 / 18
    new Car(topSpeed, 2 * teamNumber)
  }
}

case class Team(teamNumber: Int, car: Car, trackPositionInMetres: Double, currentSpeed: Double = 0, raceTime: Double = 0)

case class RaceConfig(trackLengthInKms: Float, teams: Seq[Team])
object RaceConfig {
  def apply(trackLengthInKms: Int, noOfTeams: Int, initializeTeam: Int => Team) = {
    val teams = 1 to noOfTeams map (initializeTeam(_)) sortWith (_.trackPositionInMetres > _.trackPositionInMetres)
    new RaceConfig(trackLengthInKms, teams)
  }
}