package main.scala.com.github.jasimvs.formula1

/**
 * Created by jsulaiman on 6/11/2016.
 */
object RaceService {

  def initializeTeam(teamNumber: Int): Team = {
    Team(teamNumber, Car(teamNumber), calculateStartingPosition(teamNumber))
  }

  def calculateStartingPosition(teamNumber: Int): Long = {
    teamNumber * (teamNumber - 1) * -100
  }

  def calculateStartingPositionRecursive(teamNumber: Int): Long = {
    if (teamNumber == 1) 0 else ((teamNumber - 1) * -200) + calculateStartingPositionRecursive(teamNumber - 1)
  }

  def allTeamsCompletedRace(raceConfig: RaceConfig): Boolean = !raceConfig.teams.filter(team => !raceCompleted(team, raceConfig.trackLengthInKms)).isEmpty

  def updateTeams(raceConfig: RaceConfig, timeInSeconds: Int): RaceConfig = {
    val teams = raceConfig.teams.map(team => {
      if (raceCompleted(team, raceConfig.trackLengthInKms))
        team
      else
      // TODO: check for activation of hf & nitro
        updateTeam(team, timeInSeconds)
    })
    raceConfig.copy(teams = teams.sortWith(teamSortAlgorithm(_, _, raceConfig.trackLengthInKms)))
  }

  def updateTeam(team: Team, timeInSeconds: Int): Team = {
    val distanceSpeedTuple = distanceTravelled(team.currentSpeed, team.car.accelerationInMetresPerSecondSquare, timeInSeconds, team.car.topSpeedInMetresPerSecond)
    team.copy(trackPositionInMetres = team.trackPositionInMetres + distanceSpeedTuple._1, currentSpeed = distanceSpeedTuple._2, raceTime = team.raceTime + timeInSeconds)
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

  def raceCompleted(team: Team, trackLengthInKms: Float) = team.trackPositionInMetres > trackLengthInKms * 1000

  def distanceTravelled(u: Double, a: Int, t: Double, maxV: Double): (Double, Double) = {
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

  def calculateSpeedAfterTime(u: Double, a: Int, t: Double): Double = u + (a * t)

  def distanceTravelled(u: Double, a: Int, t: Double): Double = (u * t) + (a * t * t / 2)

  def timeToReachSpeed(u: Double, v: Double, a: Int): Double = (v - u) / a

}
