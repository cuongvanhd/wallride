package org.wallride.core.domain;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.io.Serializable;

/*
 * クラブ対戦成績
 */
//@Entity
@Table(name="td13_leageclub")
//@PrimaryKeyJoinColumn
@Analyzer(definition="synonyms")
@Indexed
public class ClubRecord implements Serializable {

	@Id
	@Column(name="td13_id")
	private int id;

	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="td13_leageid")
	private League league;

	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="td13_clubid")
	private Club club;

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

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || !(other instanceof DomainObject)) return false;
		DomainObject that = (DomainObject) other;
		return (getId() == that.getId());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}
}
