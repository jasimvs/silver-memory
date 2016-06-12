package com.github.jasimvs.formula1

import main.scala.com.github.jasimvs.formula1.{RaceConfig, Car, Team, DefaultRaceService}
import org.scalatest.FlatSpec

/**
 * Created by jsulaiman on 6/11/2016.
 */
class RaceServiceTests extends FlatSpec {

  val raceService = new DefaultRaceService

  "isAnyTeamYetToCompleteRace when no car has completed" should " return false" in {
    val team1: Team = Team(1, Car(1), 999)
    val team2: Team = Team(2, Car(2), 100)
    val raceConfig: RaceConfig = new RaceConfig(1, Seq(team1, team2))
    assert(raceService.isAnyTeamYetToCompleteRace(raceConfig) == true)
  }

  "isAnyTeamYetToCompleteRace when one team is yet to finish " should " return false" in {
    val team1: Team = Team(1, Car(1), 999)
    val team2: Team = Team(2, Car(2), 1000)
    val raceConfig: RaceConfig = new RaceConfig(1, Seq(team1, team2))
    assert(raceService.isAnyTeamYetToCompleteRace(raceConfig) == true)
  }

  "isAnyTeamYetToCompleteRace when all teams finished " should " return true " in {
    val team1: Team = Team(1, Car(1), 1001)
    val team2: Team = Team(2, Car(2), 1000)
    val raceConfig: RaceConfig = new RaceConfig(1, Seq(team1, team2))
    assert(raceService.isAnyTeamYetToCompleteRace(raceConfig) == false)
  }

  "raceCompleted" should "return true when team has completed race" in {
    val team1: Team = Team(1, Car(1), 1000)
    assert(raceService.raceCompleted(team1, 1) == true)
  }

  "raceCompleted" should "return false when team has not completed race" in {
    val team1: Team = Team(1, Car(1), 999)
    assert(raceService.raceCompleted(team1, 1) == false)
  }

  "shouldActivateHf" should "return true when two cars are at same position" in {
    val team1: Team = Team(1, Car(1), 0)
    val team2: Team = Team(2, Car(2), 0)
    val teams : Seq[Team] = Seq(team1, team2)
    assert(raceService.shouldActivateHf(teams, team1, 0) == true)
    assert(raceService.shouldActivateHf(teams, team2, 1) == true)
  }

  "shouldActivateHf" should "return true when one car is close enough ahead or behind" in {
    val team1: Team = Team(1, Car(1), 0)
    val team2: Team = Team(2, Car(2), 10)
    val teams : Seq[Team] = Seq(team1, team2)
    assert(raceService.shouldActivateHf(teams, team1, 0) == true)
    assert(raceService.shouldActivateHf(teams, team2, 1) == true)
  }

  "shouldActivateHf" should "return false when one car is not close enough ahead or behind" in {
    val team1: Team = Team(1, Car(1), 0)
    val team2: Team = Team(2, Car(2), 11)
    val teams : Seq[Team] = Seq(team1, team2)
    assert(raceService.shouldActivateHf(teams, team1, 0) == false)
    assert(raceService.shouldActivateHf(teams, team2, 1) == false)
  }

  "teamSortAlgorithm when both teams have not completed race " should "return true when 1st team is ahead of 2nd team and vice versa" in {
    val team1: Team = Team(1, Car(1), 10)
    val team2: Team = Team(2, Car(2), 0)
    assert(raceService.teamSortAlgorithm(team1, team2, 1) == true)
    assert(raceService.teamSortAlgorithm(team2, team1, 1) == false)
  }

  "teamSortAlgorithm when both teams have completed race " should "return true when 1st team is ahead of 2nd team and vice versa" in {
    val team1: Team = Team(1, Car(1), 1000, 0, 5)
    val team2: Team = Team(2, Car(2), 1001, 0, 10)
    assert(raceService.teamSortAlgorithm(team1, team2, 1) == true)
    assert(raceService.teamSortAlgorithm(team2, team1, 1) == false)
  }

  "teamSortAlgorithm when one team have completed race " should "return true when 1st team has completed and vice versa" in {
    val team1: Team = Team(1, Car(1), 1000, 0, 5)
    val team2: Team = Team(2, Car(2), 999, 0, 10)
    assert(raceService.teamSortAlgorithm(team1, team2, 1) == true)
    assert(raceService.teamSortAlgorithm(team2, team1, 1) == false)
  }

  "teamSortAlgorithm" should "return true when 1st team is ahead of 2nd team" in {
    val team1: Team = Team(1, Car(1), 10)
    val team2: Team = Team(2, Car(2), 0)
    assert(raceService.teamSortAlgorithm(team1, team2, 1) == true)
    assert(raceService.teamSortAlgorithm(team2, team1, 1) == false)
  }

  "distanceTravelled " should " return right tuples" in {
    assert(raceService.calculateDistanceTravelled(10, 5, 2, 10) == (20, 10))
    assert(raceService.calculateDistanceTravelled(10, 5, 2, 5) == (10, 5))
    assert(raceService.calculateDistanceTravelled(10, 5, 2, 20) == (30, 20))
    assert(raceService.calculateDistanceTravelled(10, 10, 2, 20) == (35, 20))
    assert(raceService.calculateDistanceTravelled(10, 10, 4, 20) == (75, 20))
    assert(raceService.calculateDistanceTravelled(5, 10, 2, 20) == (28.75, 20))
  }
}
