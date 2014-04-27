package org.wallride.core.domain;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;

/*
 * 移籍データView (要同期:soccer)
 */
@Entity
@Table(name="movement")
@Immutable
public class Movement implements Serializable {

	@Id
	@Column(name="td11_id")
	private int id;

	@Column(name="td11_uniformnumber")
	private String uniformNo;

	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="td11_leageid")
	private League league;

	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="td11_clubid")
	private Club club;

	@ManyToOne(fetch= FetchType.LAZY, optional=true)
	@JoinColumn(name="td11_playerid")
	private Player player;

	@Column(name="seson1")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateAsString")
	private LocalDate seasonStartAt;

	@Column(name="seson2")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateAsString")
	private LocalDate seasonEndAt;

	@Column(name="s")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateAsString")
	private LocalDate joinedAt;

	@Column(name="s2")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateAsString")
	private LocalDate leftAt;

	@Column(name="td11_participation1")
	private Integer participation;

	@Column(name="td11_participation2")
	private Integer starter;

	@Column(name="td11_participation3")
	private Integer substitute;

	@Column(name="td11_score")
	private Integer score;

	@Column(name="td11_participation1_2")
	private Integer playoffParticipation;

	@Column(name="td11_participation2_2")
	private Integer playoffStarter;

	@Column(name="td11_participation3_2")
	private Integer playoffSubstitute;

	@Column(name="td11_score2")
	private Integer playoffScore;

	@Lob
	@Column(name="td11_bikou")
	private String remarks;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUniformNo() {
		return uniformNo;
	}

	public void setUniformNo(String uniformNo) {
		this.uniformNo = uniformNo;
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public LocalDate getSeasonStartAt() {
		return seasonStartAt;
	}

	public void setSeasonStartAt(LocalDate seasonStartAt) {
		this.seasonStartAt = seasonStartAt;
	}

	public LocalDate getSeasonEndAt() {
		return seasonEndAt;
	}

	public void setSeasonEndAt(LocalDate seasonEndAt) {
		this.seasonEndAt = seasonEndAt;
	}

	public LocalDate getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(LocalDate joinedAt) {
		this.joinedAt = joinedAt;
	}

	public LocalDate getLeftAt() {
		return leftAt;
	}

	public void setLeftAt(LocalDate leftAt) {
		this.leftAt = leftAt;
	}

	public Integer getParticipation() {
		return participation;
	}

	public void setParticipation(Integer participation) {
		this.participation = participation;
	}

	public Integer getStarter() {
		return starter;
	}

	public void setStarter(Integer starter) {
		this.starter = starter;
	}

	public Integer getSubstitute() {
		return substitute;
	}

	public void setSubstitute(Integer substitute) {
		this.substitute = substitute;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getPlayoffParticipation() {
		return playoffParticipation;
	}

	public void setPlayoffParticipation(Integer playoffParticipation) {
		this.playoffParticipation = playoffParticipation;
	}

	public Integer getPlayoffStarter() {
		return playoffStarter;
	}

	public void setPlayoffStarter(Integer playoffStarter) {
		this.playoffStarter = playoffStarter;
	}

	public Integer getPlayoffSubstitute() {
		return playoffSubstitute;
	}

	public void setPlayoffSubstitute(Integer playoffSubstitute) {
		this.playoffSubstitute = playoffSubstitute;
	}

	public Integer getPlayoffScore() {
		return playoffScore;
	}

	public void setPlayoffScore(Integer playoffScore) {
		this.playoffScore = playoffScore;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
