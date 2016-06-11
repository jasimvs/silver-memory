package main.scala.com.github.jasimvs.formula1

/**
 * Created by jsulaiman on 6/10/2016.
 */
object Main {

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
    def apply(trackLengthInKms: Int, noOfTeams: Int) = {
      val teams = 1 to noOfTeams map (initializeTeam(_)) sortWith (_.trackPositionInMetres > _.trackPositionInMetres)
      new RaceConfig(trackLengthInKms, teams)
    }
  }

  def initializeTeam(teamNumber: Int): Team = {
    Team(teamNumber, Car(teamNumber), calculateStartingPosition(teamNumber))
  }

  def calculateStartingPosition(teamNumber: Int): Long = {
    teamNumber * (teamNumber - 1) * -100
  }

  def calculateStartingPositionRecursive(teamNumber: Int): Long = {
    if (teamNumber == 1) 0 else ((teamNumber - 1) * -200) + calculateStartingPositionRecursive(teamNumber - 1)
  }

  def main(args: Array[String]) {
    //    println(calculateStartingPosition(4633))
    //    println(calculateStartingPositionRecursive(4633))
    val race1 = race(RaceConfig(10, 5))
  }

  def race(raceConfig: RaceConfig): RaceConfig = {
    var raceConf : RaceConfig = raceConfig
    while (allTeamsComplete(raceConf)) {
      raceConf = updateTeamsAfterTwoSeconds(raceConf)
    }
    raceConf
  }

  def allTeamsComplete(raceConfig: RaceConfig) = !raceConfig.teams.filter(team => !raceCompleted(team, raceConfig.trackLengthInKms)).isEmpty

  def updateTeamsAfterTwoSeconds(raceConfig: RaceConfig) = {
    val teams = raceConfig.teams.map(team => {
      if (raceCompleted(team, raceConfig.trackLengthInKms))
        team
      else
        // TODO: check for activation of hf & nitro
        updateTeamAfterTwoSeconds(team)
    })
    raceConfig.copy(teams = teams.sortWith(teamSortAlgorithm(_, _, raceConfig.trackLengthInKms)))
  }

  def teamSortAlgorithm(team1: Team, team2: Team, trackLengthInKms: Float): Boolean = {

    val team1Completed = raceCompleted(team1, trackLengthInKms)
    val team2Completed = raceCompleted(team2, trackLengthInKms)

    if (team1Completed && team2Completed)
      team1.raceTime < team2.raceTime
    else if (team1Completed)
      true
    else if (team2Completed)
      false
    else
      team1.trackPositionInMetres > team2.trackPositionInMetres
  }

  def updateTeamAfterTwoSeconds(team: Team) = {
    val distanceSpeedTuple = distanceTravelled(team.currentSpeed, team.car.accelerationInMetresPerSecondSquare, 2, team.car.topSpeedInMetresPerSecond)
    team.copy(trackPositionInMetres = team.trackPositionInMetres + distanceSpeedTuple._1, currentSpeed = distanceSpeedTuple._2)
  }

  def raceCompleted(team: Team, trackLengthInKms: Float) = team.trackPositionInMetres > trackLengthInKms * 1000

  def distanceTravelled(u: Double, a: Int, t: Double, maxV: Double) = {
    val v = calculateSpeedAfterTime(u, a, t)
    if (v > maxV) {
      val t1 = timeToReachSpeed(u, maxV, a)
      val s1 = distanceTravelled(u, a, t1)
      val s2 = distanceTravelled(maxV, 0, t - t1)
      (s1 + s2, maxV)
    } else {
      (distanceTravelled(u, a, t), v)
    }
  }

  def calculateSpeedAfterTime(u: Double, a: Int, t: Double) = u + (a * t)

  def distanceTravelled(u: Double, a: Int, t: Double): Double = (u * t) + (a * t * t / 2)

  def timeToReachSpeed(u: Double, v: Double, a: Int): Double = (v - u) / a

}
