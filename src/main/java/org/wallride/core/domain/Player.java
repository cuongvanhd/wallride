package org.wallride.core.domain;


import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/*
 * 選手 (要同期:soccer)
 */
@Entity
@Table(name="tm05_player")
public class Player implements Serializable {

	public enum WorkingFoot { LEFT, RIGHT, BOTH }

	public enum PositionLevel { GREAT, GOOD, SOSO, BAD }

	@Id
	@Column(name="tm05_id")
	private int id;

	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="tm05_nationality1")
	private Country country;

	@Column(name="tm05_position_division")
	@Field
	private String positionDivision;

	/** 正式名(母語1) */
	@Column(name="tm05_fullname")
	@Field
	private String name;

	/** 正式名(日本語) */
	@Column(name="tm05_fullname_jp")
	@Field
	private String nameJa;

	/** 登録名 */
	@Column(name="tm05_name")
	@Field
	private String nameAbbr;

	@Column(name="tm05_tall")
	@Field
	private float height;

	@Column(name="tm05_wait")
	@Field
	private float weight;

	@Column(name="tm05_birthday")
	@Field
	private String birthday;

	@Column(name="tm05_home")
	@Field
	private String home;

	@Column(name="tm05_workingfoot")
	private WorkingFoot workingFoot;

//	@Column(name="")
//	private PositionLevel

	@Lob
	@Column(name="tm05_comment")
	@Field
	private String comment;

	@Lob
	@Column(name="tm05_movie")
	@Field
	private String movie;

	@Column(name="tm05_seekword")
	private String keyword;

	@OneToMany(mappedBy="player", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Movement> movements;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getPositionDivision() {
		return positionDivision;
	}

	public void setPositionDivision(String positionDivision) {
		this.positionDivision = positionDivision;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameJa() {
		return nameJa;
	}

	public void setNameJa(String nameJa) {
		this.nameJa = nameJa;
	}

	public String getNameAbbr() {
		return nameAbbr;
	}

	public void setNameAbbr(String nameAbbr) {
		this.nameAbbr = nameAbbr;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public WorkingFoot getWorkingFoot() {
		return workingFoot;
	}

	public void setWorkingFoot(WorkingFoot workingFoot) {
		this.workingFoot = workingFoot;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getMovie() {
		return movie;
	}

	public void setMovie(String movie) {
		this.movie = movie;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<Movement> getMovements() {
		return movements;
	}

	public void setMovements(List<Movement> movements) {
		this.movements = movements;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || !(other instanceof Player)) return false;
		Player that = (Player) other;
		return (getId() == that.getId());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}
}
