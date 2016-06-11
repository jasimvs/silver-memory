package main.scala.com.github.jasimvs.formula1

/**
 * Created by jsulaiman on 6/11/2016.
 */

case class Car(topSpeedInMetresPerSecond: Float, accelerationInMetresPerSecondSquare: Int, handlingFactor: Double = 0.8,
               nitroPower: Int = 2)
object Car {
  def apply(teamNumber: Int) = {
    val topSpeed: Float = (150 + (10 * teamNumber)) * 5 / 18
    new Car(topSpeed, 2 * teamNumber)
  }
}

case class Team(teamNumber: Int, car: Car, trackPositionInMetres: Double, currentSpeed: Double = 0,
                raceTime: Double = 0, nitroAvailability: Int = 1) {

  def getCurrentSpeedInKmph(): Double = {
    currentSpeed * 18 / 5
  }
}

case class RaceConfig(trackLengthInKms: Float, teams: Seq[Team])
object RaceConfig {
  def apply(trackLengthInKms: Int, noOfTeams: Int) = {
    if (trackLengthInKms <= 0 || noOfTeams <= 0) {
      throw new IllegalArgumentException("Input should be positive numbers")
    }
    val teams = 1 to noOfTeams map (initializeTeam(_))
    new RaceConfig(trackLengthInKms, teams)
  }

  def initializeTeam(teamNumber: Int): Team = Team(teamNumber, Car(teamNumber), calculateStartingPosition(teamNumber))

  def calculateStartingPosition(teamNumber: Int): Long = teamNumber * (teamNumber - 1) * -100

  def calculateStartingPositionRecursive(teamNumber: Int): Long =
    if (teamNumber == 1) 0 else ((teamNumber - 1) * -200) + calculateStartingPositionRecursive(teamNumber - 1)

}