package org.wallride.core.domain;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/*
 * クラブチーム
 */
@Entity
@Table(name="tm02_club")
//@PrimaryKeyJoinColumn
@Analyzer(definition="synonyms")
@Indexed
@SuppressWarnings("serial")
public class Club implements Serializable {

	@Id
	@Column(name = "tm02_id")
	private int id;

	@Column(name = "tm02_years")
	private int years;

	@Column(name = "tm02_alias")
	@Field
	private String alias;

	@Column(name = "tm02_alias_jp")
	@Field
	private String aliasJa;

	@Column(name = "tm02_realname")
	@Field
	private String name;

	@Column(name = "tm02_realname_jp")
	@Field
	private String nameJa;

	@Column(name = "tm02_countrycd")
	@Field
	private int countrycd;

	@Column(name = "tm02_petname")
	@Field
	private String petname;

	@Column(name = "tm02_etc")
	@Field
	private String etc;

	@Column(name = "tm02_websiteurl")
	@Field
	private String url;

	@Column(name = "tm02_baseground")
	@Field
	private String baseground;

	@Column(name = "tm02_establishment")
	@Field
	private String establishment;

	@Column(name = "tm02_chairmanname")
	@Field
	private String chairmanName;

	@Column(name = "tm02_changememo")
	@Field
	private String changeMemo;

	@OneToMany(mappedBy="club", fetch=FetchType.LAZY, cascade= CascadeType.ALL)
	private List<ClubRecord> records;

	@OneToMany(mappedBy="club", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Movement> movements;

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getYears() {
		return years;
	}

	public void setYears(int years) {
		this.years = years;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAliasJa() {
		return aliasJa;
	}

	public void setAliasJa(String aliasJa) {
		this.aliasJa = aliasJa;
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

	public int getCountrycd() {
		return countrycd;
	}

	public void setCountrycd(int countrycd) {
		this.countrycd = countrycd;
	}

	public String getPetname() {
		return petname;
	}

	public void setPetname(String petname) {
		this.petname = petname;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBaseground() {
		return baseground;
	}

	public void setBaseground(String baseground) {
		this.baseground = baseground;
	}

	public String getEstablishment() {
		return establishment;
	}

	public void setEstablishment(String establishment) {
		this.establishment = establishment;
	}

	public String getChairmanName() {
		return chairmanName;
	}

	public void setChairmanName(String chairmanName) {
		this.chairmanName = chairmanName;
	}

	public String getChangeMemo() {
		return changeMemo;
	}

	public void setChangeMemo(String changeMemo) {
		this.changeMemo = changeMemo;
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
