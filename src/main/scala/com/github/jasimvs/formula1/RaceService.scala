package main.scala.com.github.jasimvs.formula1

import com.github.jasimvs.formula1.StandardFormulas

/**
 * Created by jsulaiman on 6/11/2016.
 */
trait RaceService {
  
  def isAnyTeamYetToCompleteRace(raceConfig: RaceConfig): Boolean

  def updateTeams(raceConfig: RaceConfig, timeInSeconds: Int): RaceConfig
}

class DefaultRaceService extends RaceService {

  override def isAnyTeamYetToCompleteRace(raceConfig: RaceConfig): Boolean =
    !raceConfig.teams.filterNot(team => raceCompleted(team, raceConfig.trackLengthInKms)).isEmpty

  override def updateTeams(raceConfig: RaceConfig, timeInSeconds: Int): RaceConfig = {
    val teams = raceConfig.teams.map(team => {
      if (raceCompleted(team, raceConfig.trackLengthInKms)) {
        team
      } else {
        val index = raceConfig.teams.indexOf(team)
        // Assuming hf is needed only if car ahead is within defined limits, ignoring if car behind is within limits
        val activateHf = (index != 0 && team.trackPositionInMetres - raceConfig.teams(index - 1).trackPositionInMetres <= 10)
        val activateNitro = (index + 1 == (raceConfig.teams.size))
        updateTeam(team, timeInSeconds, activateHf, activateNitro)
      }
    })
    raceConfig.copy(teams = teams.sortWith(teamSortAlgorithm(_, _, raceConfig.trackLengthInKms)))
  }

  private def updateTeam(team: Team, timeInSeconds: Int, activateHf: Boolean, activateNitro: Boolean): Team = {
    val nitroFactor = if (activateNitro && team.nitroAvailability > 0) team.car.nitroPower else 1
    val hfFactor = if (activateHf) team.car.handlingFactor else 1
    val distanceSpeedTuple = calculateDistanceTravelled(team.currentSpeed * nitroFactor * hfFactor,
      team.car.accelerationInMetresPerSecondSquare, timeInSeconds, team.car.topSpeedInMetresPerSecond)

    if (activateNitro && team.nitroAvailability > 0)
      team.copy(trackPositionInMetres = team.trackPositionInMetres + distanceSpeedTuple._1,
        currentSpeed = distanceSpeedTuple._2, raceTime = team.raceTime + timeInSeconds,
        nitroAvailability = team.nitroAvailability -1)
    else {
      team.copy(trackPositionInMetres = team.trackPositionInMetres + distanceSpeedTuple._1,
        currentSpeed = distanceSpeedTuple._2, raceTime = team.raceTime + timeInSeconds)
    }
  }

  private def teamSortAlgorithm(team1: Team, team2: Team, trackLengthInKms: Float): Boolean = {
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

  private def raceCompleted(team: Team, trackLengthInKms: Float) = team.trackPositionInMetres >= trackLengthInKms * 1000

  def calculateDistanceTravelled(u: Double, a: Int, t: Double, maxV: Double): (Double, Double) = {
    val v = StandardFormulas.calculateSpeedAfterTime(u, a, t)
    if (u >= maxV) {
      (StandardFormulas.calculateDistanceTravelled(maxV, 0, t), maxV)
    } else if (v > maxV) {
      val t1 = StandardFormulas.calculateTimeToReachSpeed(u, maxV, a)
      val s1 = StandardFormulas.calculateDistanceTravelled(u, a, t1)
      val s2 = StandardFormulas.calculateDistanceTravelled(maxV, 0, t - t1)
      (s1 + s2, maxV)
    } else {
      (StandardFormulas.calculateDistanceTravelled(u, a, t), v)
    }
  }
}

