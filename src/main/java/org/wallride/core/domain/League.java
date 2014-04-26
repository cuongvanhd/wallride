package org.wallride.core.domain;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/*
 * リーグ　(要同期:soccer)
 */
//@Entity
@Table(name="tm07_leage")
//@PrimaryKeyJoinColumn
@Analyzer(definition="synonyms")
@Indexed
public class League implements Serializable {

	@Id
	@Column(name = "tm07_id")
	private int id;

//	@Column(name = "tm07_countryid")
//	private String countryId;

	@Column(name = "tm07_countryid_sub")
	private String countrySubId;

	@Column(name = "tm07_leagename")
	@Field
	private String name;

	@Column(name = "tm07_leagename2")
	@Field
	private String nameEn;

	@Column(name = "tm07_leagestart", nullable = false)
	private String startAt;

	@Column(name = "tm07_leageend", nullable = false)
	private String endAt;

	@Column(name = "tm07_division")
	private int division;

	@Column(name = "tm07_image1")
	private String logoImagePath;

	@Column(name = "tm07_warning")
	@Field
	private String warning;

	@Column(name = "tm07_foreigner")
	@Field
	private String foreigner;

	@Column(name = "tm07_move")
	@Field
	private String move;

	@Column(name = "tm07_updateymd")
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime updateAt;

	@OneToMany(mappedBy="league", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<ClubRecord> records;

	@OneToMany(mappedBy="league", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Movement> movements;

	@OneToMany(mappedBy="league", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<LeagueDetail> details;

	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="tm07_countryid")
	private Country country;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountrySubId() {
		return countrySubId;
	}

	public void setCountrySubId(String countrySubId) {
		this.countrySubId = countrySubId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getStartAt() {
		return startAt;
	}

	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

	public int getDivision() {
		return division;
	}

	public void setDivision(int division) {
		this.division = division;
	}

	public String getLogoImagePath() {
		return logoImagePath;
	}

	public void setLogoImagePath(String logoImagePath) {
		this.logoImagePath = logoImagePath;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public String getForeigner() {
		return foreigner;
	}

	public void setForeigner(String foreigner) {
		this.foreigner = foreigner;
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}

	public LocalDateTime getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
	}

	public List<ClubRecord> getRecords() {
		return records;
	}

	public void setRecords(List<ClubRecord> records) {
		this.records = records;
	}

	public List<Movement> getMovements() {
		return movements;
	}

	public void setMovements(List<Movement> movements) {
		this.movements = movements;
	}

	public List<LeagueDetail> getDetails() {
		return details;
	}

	public void setDetails(List<LeagueDetail> details) {
		this.details = details;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
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
