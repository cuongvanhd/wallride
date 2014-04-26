package org.wallride.core.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/*
 * 国
 */
//@Entity
@Table(name="tm06_world")
public class Country implements Serializable {

	@Id
	@Column(name="tm06_id")
	private int id;

	@Column(name="tm06_name")
	private String name;

	@Column(name="tm06_fifa_abbreviation")
	private String fifaAbbr;

	@Column(name="tm06_realname_jp")
	private String realNameJa;

	@Column(name="tm06_realname")
	private String realName;

	@Column(name="tm06_englishname")
	private String nameEn;

	@OneToMany(mappedBy = "country", fetch=FetchType.LAZY, cascade= CascadeType.ALL)
	List<League> leagues;

	@OneToMany(mappedBy = "country", fetch=FetchType.LAZY, cascade= CascadeType.ALL)
	List<Player> players;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFifaAbbr() {
		return fifaAbbr;
	}

	public void setFifaAbbr(String fifaAbbr) {
		this.fifaAbbr = fifaAbbr;
	}

	public String getRealNameJa() {
		return realNameJa;
	}

	public void setRealNameJa(String realNameJa) {
		this.realNameJa = realNameJa;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public List<League> getLeagues() {
		return leagues;
	}

	public void setLeagues(List<League> leagues) {
		this.leagues = leagues;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public String getEnglishName() {
		String englishName = StringUtils.capitalize(getNameEn());
		return englishName;
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
