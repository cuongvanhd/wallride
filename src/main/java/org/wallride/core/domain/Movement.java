package org.wallride.core.domain;

import javax.persistence.*;
import java.io.Serializable;

/*
 * 移籍データ
 */
@Entity
@Table(name="tm11_movement")
public class Movement implements Serializable {

	@Id
	@Column(name="td11_id")
	private int id;

	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="td11_leageid")
	private League league;

	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="td11_clubid")
	private Club club;

	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="td11_playerid")
	private Player player;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
}
