package org.wallride.core.domain;

import javax.persistence.*;
import java.io.Serializable;

/*
 * リーグ　(要同期:soccer)
 */
@Entity
@Table(name="tm14_leage_sub")
public class LeagueDetail implements Serializable {

	@Id
	@Column(name="tm14_id")
	private int id;

	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="td14_leageid")
	private League league;

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
}
