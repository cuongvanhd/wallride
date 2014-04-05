package org.wallride.core.domain;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;

/*
 * 移籍データ
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
}
